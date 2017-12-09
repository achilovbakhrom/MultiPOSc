package com.jim.multipos.ui.vendor_item_managment.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.ui.inventory.fragments.InventoryFragment;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment;
import com.jim.multipos.ui.vendor_item_managment.model.VendorWithDebt;

/**
 * Created by developer on 20.11.2017.
 */

public interface VendorItemPresenter extends Presenter {
    void onIncomeProduct(VendorWithDebt vendorWithDebt);
    void onWriteOff(VendorWithDebt vendorWithDebt);
    void onConsigmentStory(VendorWithDebt vendorWithDebt);
    void onPay(VendorWithDebt vendorWithDebt);
    void onPayStory(VendorWithDebt vendorWithDebt,Double totalDebt);
    void onMore(VendorWithDebt vendorWithDebt);

    void onSearchTyped(String searchText);
    void filterBy(VendorItemFragment.SortModes searchMode);
    void filterInvert();

    void updateData();
}
