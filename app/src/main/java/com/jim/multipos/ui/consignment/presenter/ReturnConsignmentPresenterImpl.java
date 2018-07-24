package com.jim.multipos.ui.consignment.presenter;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.consignment.Outvoice;
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
    private StockQueue stockQueue;
    private final Context context;
    private double sum = 0;
    private Long productId;
    private int position;

    @Inject
    protected ReturnConsignmentPresenterImpl(ReturnConsignmentView view, DatabaseManager databaseManager, Context context) {
        super(view);
        this.databaseManager = databaseManager;
        this.context = context;
        outcomeProducts = new ArrayList<>();
    }

    /**
     * Initialization of vendor and product data
     * @param productId - for what product user creates return invoice
     * @param vendorId - for which vendor user returns products
     * @param stockQueueId - custom picked stock queue id
     * User can create return invoice only for active vendors
     * Generating unique return invoice number
     */
    @Override
    public void setData(Long productId, Long vendorId, Long stockQueueId) {
        view.setCurrency(databaseManager.getMainCurrency().getAbbr());
        this.vendor = databaseManager.getVendorById(vendorId).blockingSingle();
        this.productId = productId;
        if (productId != null) {
            if (stockQueueId != null) {
                stockQueue = databaseManager.getStockQueueById(stockQueueId).blockingGet();
                setCustomReturnPick(databaseManager.getProductById(productId).blockingGet());
            } else
                setReturnItem(databaseManager.getProductById(productId).blockingGet());
        }
        view.setVendorName(this.vendor.getName());
        int count = 1;
        while (databaseManager.isOutvoiceNumberExists(String.valueOf(count)).blockingGet()) {
            count++;
        }
        view.setConsignmentNumber(count);
    }
    /**
     * Adding custom picked product to return invoice
     * @param product - return invoice product
     */
    private void setCustomReturnPick(Product product) {
        OutcomeProduct outcomeProduct = new OutcomeProduct();
        outcomeProduct.setOutcomeType(OutcomeProduct.OUTVOICE_TO_VENDOR);
        outcomeProduct.setProduct(product);
        outcomeProduct.setSumCostValue(databaseManager.getLastCostForProduct(product.getId()).blockingGet());
        outcomeProduct.setSumCountValue(stockQueue.getAvailable());
        outcomeProduct.setCustomPickSock(true);
        outcomeProduct.setStockQueue(stockQueue);
        outcomeProduct.setPickedStockQueueId(stockQueue.getId());
        outcomeProducts.add(outcomeProduct);
        view.fillReturnList(outcomeProducts);
        calculateConsignmentSum();
    }
    /**
     * Adding product to return invoice
     * @param product - return invoice product
     */
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
    /**
     * Deleting product from invoice
     * @param position - product position
     */
    @Override
    public void deleteFromList(int position) {
        outcomeProducts.remove(position);
    }
    /**
     * Calculating total sum of invoice
     */
    @Override
    public void calculateConsignmentSum() {
        sum = 0;
        for (OutcomeProduct outcomeProduct : outcomeProducts) {
            if (outcomeProduct.getSumCostValue() != null && outcomeProduct.getSumCostValue() != null)
                sum += outcomeProduct.getSumCostValue() * outcomeProduct.getSumCountValue();
        }
        view.setTotalProductsSum(sum);
    }

    /**
     * Loading products for ProductForIncomeDialog
     * productList - all product in the database
     * vendorProducts - all product that have ever been supplied by the vendor
     */
    @Override
    public void loadVendorProducts() {
        List<Product> vendorProducts = databaseManager.getVendorProductsByVendorId(vendor.getId()).blockingGet();
        view.fillDialogItems(null, vendorProducts);
    }

    /**
     * Saving return invoice into database
     * @param number - return invoice number
     * @param description - return invoice extra description
     */
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
                if (databaseManager.isOutvoiceNumberExists(number).blockingGet()) {
                    view.setConsignmentNumberError();
                    return;
                }
                Outvoice outvoice = new Outvoice();
                outvoice.setConsigmentNumber(number);
                outvoice.setCreatedDate(System.currentTimeMillis());
                outvoice.setTotalAmount(sum);
                outvoice.setVendor(vendor);
                outvoice.setVendorId(vendor.getId());
                BillingOperations operationDebt = new BillingOperations();
                operationDebt.setAmount(sum);
                operationDebt.setCreateAt(System.currentTimeMillis());
                operationDebt.setVendor(this.vendor);
                operationDebt.setDeleted(false);
                operationDebt.setPaymentDate(System.currentTimeMillis());
                operationDebt.setOperationType(BillingOperations.RETURN_TO_VENDOR);
                databaseManager.insertOutvoiceWithBillingAndOutcomeProducts(outvoice, outcomeProducts, operationDebt).subscribe();
                view.closeFragment(this.vendor);
            }
        }
    }
    /**
     * Checking if there was some changes
     * @param number - invoice number
     * @param des - invoice extra description
     */
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
                setReturnItem(product);
        }
    }

    /**
     * Init data for stock positions dialog
     * @param outcomeProduct - for which product creates dialog
     * @param position - product position in the list
     */
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
