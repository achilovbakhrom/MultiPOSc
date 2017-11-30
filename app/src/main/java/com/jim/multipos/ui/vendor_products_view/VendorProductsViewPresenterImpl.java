package com.jim.multipos.ui.vendor_products_view;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.inventory.model.InventoryItem;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Portable-Acer on 17.11.2017.
 */

public class VendorProductsViewPresenterImpl extends BasePresenterImpl<VendorProductsView> implements VendorProductsViewPresenter {
    private DatabaseManager databaseManager;
    private long vendorId;
    private Vendor vendor;
    private List<InventoryItem> inventoryItems;

    @Inject
    public VendorProductsViewPresenterImpl(VendorProductsView vendorProductsView, DatabaseManager databaseManager) {
        super(vendorProductsView);
        this.databaseManager = databaseManager;
    }

    @Override
    public void setVendorId(long vendorId) {
        this.vendorId = vendorId;
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
    public List<InventoryItem> getInventoryItems() {
        inventoryItems = databaseManager.getInventoryItems().blockingGet();
        sortByProductAsc();

        return inventoryItems;
    }

    @Override
    public InventoryItem getInventoryItem(int position) {
        return inventoryItems.get(position);
    }

    @Override
    public void sortByProductAsc() {
        Collections.sort(inventoryItems, (o1, o2) -> o1.getProduct().getName().compareTo(o2.getProduct().getName()));
    }

    @Override
    public void sortByProductDesc() {
        Collections.sort(inventoryItems, (o1, o2) -> o2.getProduct().getName().compareTo(o1.getProduct().getName()));
    }

    @Override
    public void sortByInventoryAsc() {
        Collections.sort(inventoryItems, (o1, o2) -> o1.getInventory().compareTo(o2.getInventory()));
    }

    @Override
    public void sortByInventoryDesc() {
        Collections.sort(inventoryItems, (o1, o2) -> o2.getInventory().compareTo(o1.getInventory()));
    }

    @Override
    public void sortByUnitAsc() {
        Collections.sort(inventoryItems, (o1, o2) -> o1.getProduct().getMainUnit().getAbbr().compareTo(o2.getProduct().getMainUnit().getAbbr()));
    }

    @Override
    public void sortByUnitDesc() {
        Collections.sort(inventoryItems, (o1, o2) -> o2.getProduct().getMainUnit().getAbbr().compareTo(o1.getProduct().getMainUnit().getAbbr()));
    }

    @Override
    public void sortByCreatedDate() {
        Collections.sort(inventoryItems, (o1, o2) -> o2.getProduct().getCreatedDate().compareTo(o1.getProduct().getCreatedDate()));
    }

    @Override
    public ProductClass getProductClassById(Long id) {
        return databaseManager.getProductClass(id).blockingSingle();
    }
}
