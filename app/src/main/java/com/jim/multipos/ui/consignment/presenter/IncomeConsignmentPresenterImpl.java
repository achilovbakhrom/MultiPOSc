package com.jim.multipos.ui.consignment.presenter;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.consignment.Invoice;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.consignment.view.IncomeConsignmentView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.data.db.model.inventory.BillingOperations.PAID_TO_CONSIGNMENT;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class IncomeConsignmentPresenterImpl extends BasePresenterImpl<IncomeConsignmentView> implements IncomeConsignmentPresenter {

    private Product product;
    private Vendor vendor;
    private List<IncomeProduct> incomeProductList;
    private List<StockQueue> stockQueueList;
    private DatabaseManager databaseManager;
    private DecimalFormat decimalFormat;
    private Context context;
    private double sum = 0;
    private List<Account> accountList;
    private Long productId;

    @Inject
    protected IncomeConsignmentPresenterImpl(IncomeConsignmentView incomeConsignmentView, DatabaseManager databaseManager, DecimalFormat decimalFormat, Context context) {
        super(incomeConsignmentView);
        this.databaseManager = databaseManager;
        this.decimalFormat = decimalFormat;
        this.context = context;
        incomeProductList = new ArrayList<>();
        accountList = new ArrayList<>();
        stockQueueList = new ArrayList<>();
    }


    @Override
    public void setData(Long productId, Long vendorId) {
        view.setCurrency(databaseManager.getMainCurrency().getAbbr());
        getAccounts();
        this.vendor = databaseManager.getVendorById(vendorId).blockingSingle();
        view.setVendorName(this.vendor.getName());

        view.setInvoiceNumber(databaseManager.getAllInvoices().blockingGet().size() + 1);
        if (productId != null) {
            this.productId = productId;
            this.product = databaseManager.getProductById(productId).blockingSingle();
            setInvoiceItem(product);
        }
    }

    @Override
    public void setInvoiceItem(Product product) {
        IncomeProduct incomeProduct = new IncomeProduct();
        incomeProduct.setCostValue(null);
        incomeProduct.setCountValue(0.0d);
        incomeProduct.setIncomeDate(System.currentTimeMillis());
        incomeProduct.setIncomeType(IncomeProduct.INVOICE_PRODUCT);
        incomeProduct.setProduct(product);
        incomeProduct.setVendor(this.vendor);
        incomeProductList.add(incomeProduct);
        StockQueue stockQueue = new StockQueue();
        stockQueue.setProduct(product);
        stockQueue.setVendor(this.vendor);
        stockQueueList.add(stockQueue);
        view.fillInvoiceProductList(incomeProductList);
        calculateInvoiceSum();
    }

    @Override
    public void saveInvoice(String number, String description, String totalAmount, String paidSum, boolean checked, int selectedPosition) {
        double paid = 0;
        try {
            paid = decimalFormat.parse(paidSum).doubleValue();
        } catch (ParseException e) {
            paid = 0;
            e.printStackTrace();
        }
        boolean hasOpenTill = databaseManager.hasOpenTill().blockingGet();
        if (!hasOpenTill && checked) {
            view.setError(context.getString(R.string.opened_till_wnt_found_pls_open_till));
        } else if (incomeProductList.isEmpty()) {
            view.setError(context.getString(R.string.please_add_product_to_consignment));
        } else {
            int countPos = incomeProductList.size(), costPos = incomeProductList.size();
            for (int i = 0; i < incomeProductList.size(); i++) {
                if (incomeProductList.get(i).getCountValue() == 0)
                    countPos = i;
                if (incomeProductList.get(i).getCostValue() == 0)
                    costPos = i;
            }

            if (countPos != incomeProductList.size()) {
                view.setError(context.getString(R.string.some_counts_are_empty_or_equals_zero));
            } else if (costPos != incomeProductList.size())
                view.setError(context.getString(R.string.some_costs_are_empty));
            else {
                if (databaseManager.isInvoiceNumberExists(number).blockingGet()) {
                    view.setInvoiceNumberError();
                    return;
                }
                Invoice invoice = new Invoice();
                invoice.setConsigmentNumber(number);
                invoice.setCreatedDate(System.currentTimeMillis());
                invoice.setDescription(description);
                invoice.setVendor(this.vendor);
                invoice.setTotalAmount(sum);
                invoice.setIsFromAccount(checked);
                List<BillingOperations> billingOperationsList = new ArrayList<>();
                BillingOperations operationDebt = new BillingOperations();
                operationDebt.setAmount(sum * -1);
                operationDebt.setCreateAt(System.currentTimeMillis());
                operationDebt.setVendor(this.vendor);
                operationDebt.setPaymentDate(System.currentTimeMillis());
                operationDebt.setOperationType(BillingOperations.DEBT_CONSIGNMENT);
                billingOperationsList.add(operationDebt);
                if (paid != 0) {
                    BillingOperations operationPaid = new BillingOperations();
                    operationPaid.setAmount(paid);
                    operationPaid.setCreateAt(System.currentTimeMillis());
                    operationPaid.setVendor(this.vendor);
                    operationPaid.setPaymentDate(System.currentTimeMillis());
                    operationPaid.setOperationType(PAID_TO_CONSIGNMENT);
                    if (checked) {
                        operationPaid.setAccount(accountList.get(selectedPosition));
                    }
                    billingOperationsList.add(operationPaid);
                }
                databaseManager.insertInvoiceWithBillingAndIncomeProduct(invoice, incomeProductList, stockQueueList, billingOperationsList).subscribe();
                view.closeFragment(this.vendor);
            }
        }

    }

    @Override
    public void deleteFromList(int position) {
        incomeProductList.remove(position);
    }

    @Override
    public void getAccounts() {
        this.accountList = databaseManager.getAccounts();
        List<String> strings = new ArrayList<>();
        if (!this.accountList.isEmpty())
            for (Account account : this.accountList) {
                strings.add(account.getName());
            }
        view.fillAccountsList(strings);

    }

    @Override
    public void calculateInvoiceSum() {
        sum = 0;
        for (IncomeProduct incomeProduct : incomeProductList) {
            if (incomeProduct.getCostValue() != null && incomeProduct.getCountValue() != null)
                sum += incomeProduct.getCostValue() * incomeProduct.getCountValue();
        }
        view.setConsignmentSumValue(sum);

    }

    @Override
    public void checkChanges(String number, String description, String totalPaid, boolean checked, int selectedPosition) {
        if (!description.equals("") || !totalPaid.equals("0") || !checked || selectedPosition != 0) {
            view.openDiscardDialog();
        } else {
            if (productId != null) {
                if (incomeProductList.size() > 1)
                    view.openDiscardDialog();
                else view.closeFragment(this.vendor);
            } else {
                if (incomeProductList.size() > 0)
                    view.openDiscardDialog();
                else view.closeFragment(this.vendor);
            }
        }
    }

    @Override
    public void onBarcodeScanned(String barcode) {
        List<Product> productList = databaseManager.getAllProducts().blockingSingle();
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            if (product.getBarcode().equals(barcode))
                setInvoiceItem(product);
        }
    }

    @Override
    public void openSettingsDialogForProduct(IncomeProduct incomeProduct, int position) {
        StockQueue stockQueue = stockQueueList.get(position);
        view.openSettingsDialog(incomeProduct, stockQueue, position);
    }

    @Override
    public void setConfigsToProduct(IncomeProduct incomeProduct, StockQueue stockQueue, int position) {
        incomeProductList.set(position, incomeProduct);
        stockQueueList.set(position, stockQueue);
    }

    @Override
    public void loadVendorProducts() {
        List<Product> productList = databaseManager.getAllProducts().blockingSingle();
        List<Product> vendorProducts = databaseManager.getVendorProductsByVendorId(vendor.getId()).blockingGet();
        view.fillDialogItems(productList, vendorProducts);
    }
}
