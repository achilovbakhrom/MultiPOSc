package com.jim.multipos.ui.vendor_products_view;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.inventory.model.InventoryItem;

import java.util.List;

/**
 * Created by Portable-Acer on 17.11.2017.
 */

public interface VendorProductsViewPresenter extends Presenter {
    Vendor getVendor();
    List<Product> getProducts();
    void setVendorId(long vendorId);
    List<InventoryItem> getInventoryItems();
    InventoryItem getInventoryItem(int position);
    void sortByProductAsc();
    void sortByProductDesc();
    void sortByInventoryAsc();
    void sortByInventoryDesc();
    void sortByUnitAsc();
    void sortByUnitDesc();
    void sortByCreatedDate();
}
