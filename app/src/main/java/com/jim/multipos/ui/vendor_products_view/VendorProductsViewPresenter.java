package com.jim.multipos.ui.vendor_products_view;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendor_products_view.model.ProductState;

import java.util.List;

/**
 * Created by Portable-Acer on 17.11.2017.
 */

public interface VendorProductsViewPresenter extends Presenter {
    Vendor getVendor();
    List<Product> getProducts();
    void setVendorId(long vendorId);
    void initVendorDetails();
    List<ProductState> getProductStates();
    ProductState getProductState(int position);
    ProductClass getProductClassById(Long id);
    void sortByProductAsc();
    void sortByProductDesc();
    void sortByInventoryAsc();
    void sortByInventoryDesc();
    void sortByUnitAsc();
    void sortByUnitDesc();
    void sortByCreatedDate();
    void openIncomeConsignment();
    void openReturnConsignment();
    void openConsignmentList();
    void openIncomeConsignmentToProduct(ProductState state, int consignmentType);
    void openVendorEditing();
    void insertNewWarehouseOperation(ProductState inventory, double shortage);
    void openPaymentsList();
    void openPayDialog();
    void updateBillings();
    void updateInventoryItems();
}
