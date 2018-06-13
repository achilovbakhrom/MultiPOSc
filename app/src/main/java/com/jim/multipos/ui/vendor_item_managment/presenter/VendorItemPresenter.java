package com.jim.multipos.ui.vendor_item_managment.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment;
import com.jim.multipos.ui.vendor_item_managment.model.VendorManagmentItem;

/**
 * Created by developer on 20.11.2017.
 */

public interface VendorItemPresenter extends Presenter {
    void onIncomeProduct(VendorManagmentItem vendorManagmentItem);
    void onWriteOff(VendorManagmentItem vendorManagmentItem);
    void onConsigmentStory(VendorManagmentItem vendorManagmentItem);
    void onPay(VendorManagmentItem vendorManagmentItem);
    void onPayStory(VendorManagmentItem vendorManagmentItem, Double totalDebt);
    void onMore(VendorManagmentItem vendorManagmentItem);
    void onStockQueueForVendor(VendorManagmentItem vendorManagmentItem);
    void onSearchTyped(String searchText);
    void filterBy(VendorItemFragment.SortModes searchMode);
    void filterInvert();

    void updateData();
}
