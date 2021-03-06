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
import java.util.Locale;

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

    /**
     * Initialization of vendor and product data
     * @param productId - for what product user creates invoice
     * @param vendorId - from which vendor user purchases
     * User can create invoice only for active vendors
     * Generating unique invoice number
     */
    @Override
    public void setData(Long productId, Long vendorId) {
        view.setCurrency(databaseManager.getMainCurrency().getAbbr());
        getAccounts();
        this.vendor = databaseManager.getVendorById(vendorId).blockingSingle();
        if (!vendor.getActive()){
            view.closeFragment();
            return;
        }
        view.setVendorName(this.vendor.getName());
        int count = 1;
        while (databaseManager.isInvoiceNumberExists(String.valueOf(count)).blockingGet()) {
            count++;
        }
        view.setInvoiceNumber(count);
        if (productId != null) {
            this.productId = productId;
            this.product = databaseManager.getProductById(productId).blockingGet();
            setInvoiceItem(product);
        }
    }

    /**
     * Adding product to invoice
     * @param product - invoice product
     * for each product created a special table StockQueue, which is needed to monitor the state of product
     */
    @Override
    public void setInvoiceItem(Product product) {
        IncomeProduct incomeProduct = new IncomeProduct();
        incomeProduct.setProduct(product);
        double lastCost = databaseManager.getLastCostForProduct(product.getId()).blockingGet();
        incomeProduct.setCostValue(lastCost);
        if (incomeProductList.size() > 0) {
            for (int i = 0; i < incomeProductList.size(); i++) {
                if (incomeProductList.get(i).getProduct().getId().equals(product.getId()))
                    incomeProduct.setCostValue(incomeProductList.get(i).getCostValue());
            }
        }
        incomeProduct.setCountValue(1.0d);
        incomeProduct.setIncomeDate(System.currentTimeMillis());
        incomeProduct.setIncomeType(IncomeProduct.INVOICE_PRODUCT);
        incomeProduct.setVendor(this.vendor);
        incomeProductList.add(incomeProduct);
        StockQueue stockQueue = new StockQueue();
        stockQueue.setProduct(product);
        stockQueue.setIncomeProductDate(System.currentTimeMillis());
        stockQueue.setVendor(this.vendor);
        stockQueueList.add(stockQueue);
        view.fillInvoiceProductList(incomeProductList);
        calculateInvoiceSum();
    }

    /**
     * Saving invoice into database
     * @param number - invoice number
     * @param description - invoice extra description
     * @param totalAmount - total amount of invoice
     * @param paidSum - paid sum for this invoice
     * @param checked - if payment was with account
     * @param selectedPosition - selected account position in the account list
     * The total amount of invoice is recorded in the database as a user's debt to the vendor
     */
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
            int expireCount = 0;
            for (int i = 0; i < stockQueueList.size(); i++) {
                if (stockQueueList.get(i).getExpiredProductDate() == 0 && incomeProductList.get(i).getProduct().getStockKeepType() == Product.FEFO)
                    expireCount++;
            }

            if (countPos != incomeProductList.size()) {
                view.setError(context.getString(R.string.some_counts_are_empty_or_equals_zero));
            } else if (costPos != incomeProductList.size())
                view.setError(context.getString(R.string.some_costs_are_empty));
            else if (expireCount != 0){
                view.setError("Product with FEFO type haven't got expire date");
            } else {
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

    /**
     * Deleting product from invoice
     * @param position - product position
     */
    @Override
    public void deleteFromList(int position) {
        incomeProductList.remove(position);
        stockQueueList.remove(position);
    }

    /**
     * Getting accounts from database
     */
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

    /**
     * Calculating total sum of invoice
     */
    @Override
    public void calculateInvoiceSum() {
        sum = 0;
        for (IncomeProduct incomeProduct : incomeProductList) {
            if (incomeProduct.getCostValue() != null && incomeProduct.getCountValue() != null)
                sum += incomeProduct.getCostValue() * incomeProduct.getCountValue();
        }
        view.setConsignmentSumValue(sum);

    }

    /**
     * Checking if there was some changes
     * @param number - invoice number
     * @param description - invoice extra description
     * @param checked - if payment was with account
     * @param selectedPosition - selected account position in the account list
     */
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

    /**
     * Finding a product by barcode
     * @param barcode - scanned barcode
     */
    @Override
    public void onBarcodeScanned(String barcode) {
        List<Product> productList = databaseManager.getAllProducts().blockingSingle();
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            if (product.getBarcode().equals(barcode))
                setInvoiceItem(product);
        }
    }

    /**
     * Opening extra information dialog for product
     */
    @Override
    public void openSettingsDialogForProduct(IncomeProduct incomeProduct, int position) {
        StockQueue stockQueue = stockQueueList.get(position);
        view.openSettingsDialog(incomeProduct, stockQueue, position);
    }

    /**
     * Setting extra information for product like expire date, creation date, stock id and etc
     */
    @Override
    public void setConfigsToProduct(IncomeProduct incomeProduct, StockQueue stockQueue, int position) {
        incomeProductList.set(position, incomeProduct);
        stockQueueList.set(position, stockQueue);
    }

    /**
     * Loading products for ProductForIncomeDialog
     * productList - all product in the database
     * vendorProducts - all product that have ever been supplied by the vendor
     */
    @Override
    public void loadVendorProducts() {
        List<Product> productList = databaseManager.getAllProducts().blockingSingle();
        List<Product> vendorProducts = databaseManager.getVendorProductsByVendorId(vendor.getId()).blockingGet();
        view.fillDialogItems(productList, vendorProducts);
    }
}
