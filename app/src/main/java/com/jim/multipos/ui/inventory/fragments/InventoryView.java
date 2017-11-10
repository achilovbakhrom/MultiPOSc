package com.jim.multipos.ui.inventory.fragments;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.ui.inventory.model.InventoryItem;

import java.util.List;

/**
 * Created by developer on 09.11.2017.
 */

public interface InventoryView extends BaseView {
    void initRecyclerView(List<InventoryItem> inventoryItemList);
}
