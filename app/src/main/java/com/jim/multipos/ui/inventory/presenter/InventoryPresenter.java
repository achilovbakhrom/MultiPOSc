package com.jim.multipos.ui.inventory.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.ui.inventory.fragments.InventoryFragment;
import com.jim.multipos.ui.inventory.model.InventoryItem;

/**
 * Created by developer on 09.11.2017.
 */

public interface InventoryPresenter extends Presenter {
    void onStockAlertChange(double newAlertCount, InventoryItem inventoryItem);
    void onIncomeProduct(InventoryItem inventoryItem);
    void onWriteOff(InventoryItem inventoryItem);
    void onSetActually(InventoryItem inventoryItem);
    void onConsigmentIn(InventoryItem inventoryItem);
    void onConsigmentOut(InventoryItem inventoryItem);
    void onSearchTyped(String searchText);

    void filterBy(InventoryFragment.SortModes searchMode);
    void filterInvert();
    void setVendorId(Long vendorId);
    void setProductId(Long productId);
}
