package com.jim.multipos.ui.consignment.presenter;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.consignment.Outvoice;
import com.jim.multipos.data.db.model.intosystem.StockQueueItem;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.consignment.view.ReturnConsignmentView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 24.11.2017.
 */

public class ReturnConsignmentPresenterImpl extends BasePresenterImpl<ReturnConsignmentView> implements ReturnConsignmentPresenter {

    private Vendor vendor;
    private List<OutcomeProduct> outcomeProducts;
    private DatabaseManager databaseManager;
    private final Context context;
    private double sum = 0;
    private Long productId;
    private List<StockQueueItem> stockQueueItemList;
    private OutcomeProduct outcomeProduct;
    private int position;

    @Inject
    protected ReturnConsignmentPresenterImpl(ReturnConsignmentView view, DatabaseManager databaseManager, Context context) {
        super(view);
        this.databaseManager = databaseManager;
        this.context = context;
        stockQueueItemList = new ArrayList<>();
        outcomeProducts = new ArrayList<>();
    }

    @Override
    public void setData(Long productId, Long vendorId) {
        view.setCurrency(databaseManager.getMainCurrency().getAbbr());
        this.productId = productId;
        if (productId != null) {
            setReturnItem(databaseManager.getProductById(productId).blockingSingle());
        }
        this.vendor = databaseManager.getVendorById(vendorId).blockingSingle();
        view.setVendorName(this.vendor.getName());
        view.setConsignmentNumber(databaseManager.getConsignments().blockingSingle().size() + 1);
    }

    @Override
    public void setReturnItem(Product product) {
        OutcomeProduct outcomeProduct = new OutcomeProduct();
        outcomeProduct.setOutcomeType(OutcomeProduct.OUTVOICE_TO_VENDOR);
        outcomeProduct.setProduct(product);
        outcomeProduct.setSumCostValue(databaseManager.getLastCostForProduct(product.getId()).blockingGet());
        outcomeProduct.setSumCountValue(0.0d);
        outcomeProducts.add(outcomeProduct);
        view.fillReturnList(outcomeProducts);
        calculateConsignmentSum();
    }

    @Override
    public void deleteFromList(int position) {
        outcomeProducts.remove(position);
    }

    @Override
    public void calculateConsignmentSum() {
        sum = 0;
        for (OutcomeProduct outcomeProduct : outcomeProducts) {
            if (outcomeProduct.getSumCostValue() != null && outcomeProduct.getSumCostValue() != null)
                sum += outcomeProduct.getSumCostValue() * outcomeProduct.getSumCostValue();
        }
        view.setTotalProductsSum(sum);
    }

    @Override
    public void loadVendorProducts() {
        List<Product> productList = databaseManager.getAllProducts().blockingSingle();
        List<Product> vendorProducts = databaseManager.getVendorProductsByVendorId(vendor.getId()).blockingGet();
        view.fillDialogItems(productList, vendorProducts);
    }

    @Override
    public void saveReturnConsignment(String number, String description) {
        if (outcomeProducts.isEmpty()) {
            view.setError(context.getString(R.string.please_add_product_to_consignment));
        } else {
            int countPos = outcomeProducts.size(), costPos = outcomeProducts.size();
            for (int i = 0; i < outcomeProducts.size(); i++) {
                if (outcomeProducts.get(i).getSumCountValue() == 0)
                    countPos = i;
                if (outcomeProducts.get(i).getSumCostValue() == null)
                    costPos = i;
            }

            if (countPos != outcomeProducts.size()) {
                view.setError(context.getString(R.string.some_counts_are_empty_or_equals_zero));
            } else if (costPos != outcomeProducts.size())
                view.setError(context.getString(R.string.some_costs_are_empty));
            else {
                if (databaseManager.isInvoiceNumberExists(number).blockingGet()) {
                    view.setConsignmentNumberError();
                    return;
                }
                Outvoice outvoice = new Outvoice();
                List<BillingOperations> billingOperationsList = new ArrayList<>();
                BillingOperations operationDebt = new BillingOperations();
                operationDebt.setAmount(sum);
                operationDebt.setCreateAt(System.currentTimeMillis());
                operationDebt.setVendor(this.vendor);
                operationDebt.setDeleted(false);
                operationDebt.setPaymentDate(System.currentTimeMillis());
                operationDebt.setOperationType(BillingOperations.RETURN_TO_VENDOR);
                billingOperationsList.add(operationDebt);


                view.closeFragment(this.vendor);
            }
        }
    }

    @Override
    public void checkChanges(String number, String des) {
        if (!des.equals("")) {
            view.openDiscardDialog();
        } else {
            if (productId != null) {
                if (outcomeProducts.size() > 1)
                    view.openDiscardDialog();
                else view.closeFragment(this.vendor);
            } else {
                if (outcomeProducts.size() > 0)
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
                setReturnItem(product);
        }
    }

    @Override
    public void openCustomStockPositionsDialog(OutcomeProduct outcomeProduct, int position) {
        this.position = position;
        double availableCount = databaseManager.getAvailableCountForProduct(outcomeProduct.getProduct().getId()).blockingGet();
        List<OutcomeProduct> outcomeProductList = new ArrayList<>();
        for (int i = 0; i < outcomeProducts.size(); i++) {
            if (outcomeProducts.get(i).getProduct().getId().equals(outcomeProduct.getProduct().getId()) && i != position) {
                outcomeProductList.add(outcomeProducts.get(i));
                availableCount -= outcomeProducts.get(i).getSumCountValue();
            }
        }
        if (availableCount >= outcomeProduct.getSumCountValue())
            view.openCustomStockPositionsDialog(outcomeProduct, outcomeProductList, null, position);
    }

    @Override
    public void setOutcomeProduct(OutcomeProduct outcomeProduct) {
        outcomeProducts.set(position, outcomeProduct);
        view.updateChangedPosition(position);
    }
}
