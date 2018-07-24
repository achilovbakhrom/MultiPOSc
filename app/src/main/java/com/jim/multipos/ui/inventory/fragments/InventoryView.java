package com.jim.multipos.ui.inventory.fragments;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.utils.SurplusProductDialog;
import com.jim.multipos.utils.WriteOffProductDialog;

import java.util.List;

/**
 * Created by developer on 09.11.2017.
 */

public interface InventoryView extends BaseView {
    void initRecyclerView(List<InventoryItem> inventoryItemList);
    void initSearchResults(List<InventoryItem> inventoryItems,String searchText);
    void initDefault(List<InventoryItem> inventoryItems);
    void notifyList();
    void closeKeyboard();
    void openWriteOffDialog(InventoryItem inventoryItem, WriteOffProductDialog.WriteOffCallback writeOffCallback);
    void openAddDialog(InventoryItem inventoryItem, SurplusProductDialog.SurplusCallback surplusCallback);
    void openChooseVendorDialog(List<Vendor> vendorList, List<Vendor> vendorsWithProduct);
    void sendDataToConsignment(Long productId, Long vendorId, int consignment_type);
    void sendInventoryStateEvent(int event);
    void openStockQueueForProduct(Long id);
    void showVendorListEmptyDialog();
    void noVendorsWarning();
}
