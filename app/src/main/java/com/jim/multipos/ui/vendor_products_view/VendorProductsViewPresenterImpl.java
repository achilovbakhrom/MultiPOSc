package com.jim.multipos.ui.vendor_products_view;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.inventory.InventoryState;
import com.jim.multipos.data.db.model.inventory.WarehouseOperations;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.data.db.model.consignment.Consignment.INCOME_CONSIGNMENT;
import static com.jim.multipos.data.db.model.consignment.Consignment.RETURN_CONSIGNMENT;
import static com.jim.multipos.data.db.model.inventory.BillingOperations.PAID_TO_CONSIGNMENT;

/**
 * Created by Portable-Acer on 17.11.2017.
 */

public class VendorProductsViewPresenterImpl extends BasePresenterImpl<VendorProductsView> implements VendorProductsViewPresenter {
    private DatabaseManager databaseManager;
    private long vendorId;
    private List<InventoryState> inventoryStates;
    private Vendor vendor;
    private Double debt;
    private List<BillingOperations> billingOperations;
    private Currency currency;

    @Inject
    public VendorProductsViewPresenterImpl(VendorProductsView vendorProductsView, DatabaseManager databaseManager) {
        super(vendorProductsView);
        this.databaseManager = databaseManager;
        billingOperations = new ArrayList<>();
    }

    @Override
    public void setVendorId(long vendorId) {
        this.vendorId = vendorId;
    }

    @Override
    public void initVendorDetails() {
        this.vendor = databaseManager.getVendorById(vendorId).blockingSingle();
        debt = databaseManager.getVendorDebt(vendorId).blockingGet() * -1;
        billingOperations = databaseManager.getBillingOperationForVendor(vendor.getId()).blockingGet();
        double paid = 0;
        for (BillingOperations operations : billingOperations) {
            if (operations.getOperationType().equals(PAID_TO_CONSIGNMENT))
                paid += operations.getAmount();
        }
        currency = databaseManager.getMainCurrency();
        view.initVendorDetails(vendor.getName(), vendor.getPhotoPath(), vendor.getAddress(), vendor.getContactName(), vendor.getContacts(), debt, paid, currency.getAbbr());
    }

    @Override
    public Vendor getVendor() {
        return databaseManager.getVendorById(vendorId).blockingSingle();
    }

    @Override
    public List<Product> getProducts() {
        return databaseManager.getVendors().blockingSingle().get(0).getProducts();
    }

    @Override
    public List<InventoryState> getInventoryStates() {
        inventoryStates = databaseManager.getInventoryStatesByVendorId(vendorId).blockingSingle();
        sortByProductAsc();
        return inventoryStates;
    }

    @Override
    public InventoryState getInventoryState(int position) {
        return inventoryStates.get(position);
    }

    @Override
    public void sortByProductAsc() {
        Collections.sort(inventoryStates, (o1, o2) -> o1.getProduct().getName().compareTo(o2.getProduct().getName()));
    }

    @Override
    public void sortByProductDesc() {
        Collections.sort(inventoryStates, (o1, o2) -> o2.getProduct().getName().compareTo(o1.getProduct().getName()));
    }

    @Override
    public void sortByInventoryAsc() {
        Collections.sort(inventoryStates, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
    }

    @Override
    public void sortByInventoryDesc() {
        Collections.sort(inventoryStates, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
    }

    @Override
    public void sortByUnitAsc() {
        Collections.sort(inventoryStates, (o1, o2) -> o1.getProduct().getMainUnit().getAbbr().compareTo(o2.getProduct().getMainUnit().getAbbr()));
    }

    @Override
    public void sortByUnitDesc() {
        Collections.sort(inventoryStates, (o1, o2) -> o2.getProduct().getMainUnit().getAbbr().compareTo(o1.getProduct().getMainUnit().getAbbr()));
    }

    @Override
    public void sortByCreatedDate() {
        Collections.sort(inventoryStates, (o1, o2) -> o2.getProduct().getCreatedDate().compareTo(o1.getProduct().getCreatedDate()));
    }

    @Override
    public void openIncomeConsignment() {
        view.sendDataToConsignment(INCOME_CONSIGNMENT, vendorId);
    }

    @Override
    public void openReturnConsignment() {
        view.sendDataToConsignment(RETURN_CONSIGNMENT, vendorId);
    }

    @Override
    public void openConsignmentList() {
        view.sendDataToConsignmentList(vendorId);
    }

    @Override
    public void openIncomeConsignmentToProduct(InventoryState state, int consignmentType) {
        view.openIncomeConsignmentToProduct(consignmentType, vendorId, state.getProduct().getId());
    }

    @Override
    public void openVendorEditing() {
        view.openVendorEditing(vendorId);
    }

    @Override
    public void insertNewWarehouseOperation(InventoryState inventory, double shortage) {
        WarehouseOperations warehouseOperations = new WarehouseOperations();
        warehouseOperations.setProduct(inventory.getProduct());
        warehouseOperations.setVendor(inventory.getVendor());
        warehouseOperations.setCreateAt(System.currentTimeMillis());
        if (shortage > 0)
            warehouseOperations.setValue(WarehouseOperations.VOID_INCOME);
        else warehouseOperations.setValue(WarehouseOperations.WASTE);
        warehouseOperations.setValue(shortage);
        databaseManager.insertWarehouseOperation(warehouseOperations).subscribe((aLong, throwable) -> {
            inventoryStates = databaseManager.getInventoryStatesByVendorId(vendorId).blockingSingle();
            sortByProductAsc();
            view.updateAdapterItems(inventoryStates);
        });
    }

    @Override
    public void openPaymentsList() {
        view.openPaymentsList(vendorId, debt);
    }

    @Override
    public void openPayDialog() {
        view.openPayDialog(this.vendor, databaseManager);
    }

    @Override
    public void updateBillings() {
        debt = databaseManager.getVendorDebt(vendorId).blockingGet() * -1;
        billingOperations = databaseManager.getBillingOperationForVendor(vendor.getId()).blockingGet();
        double paid = 0;
        for (BillingOperations operations : billingOperations) {
            if (operations.getOperationType().equals(PAID_TO_CONSIGNMENT))
                paid += operations.getAmount();
        }
        view.updateVendorBillings(debt, paid, currency.getAbbr());
    }

    @Override
    public ProductClass getProductClassById(Long id) {
        return databaseManager.getProductClass(id).blockingSingle();
    }
}