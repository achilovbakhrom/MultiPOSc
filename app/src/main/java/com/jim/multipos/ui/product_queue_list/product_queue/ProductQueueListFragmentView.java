package com.jim.multipos.ui.product_queue_list.product_queue;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.utils.WriteOffProductDialog;

import java.util.Calendar;
import java.util.List;

public interface ProductQueueListFragmentView extends BaseView {
    void setTitle(String name);
    void fillRecyclerView(List<StockQueue> stockQueueList);
    void updateDateIntervalUI(String date);
    void openDateIntervalPicker(Calendar fromDate, Calendar toDate);
    void initSearchResults(List<StockQueue> searchResults, String searchText);
    void openFilterDialog(int[] filterConfig);
    void openWriteOffDialog(InventoryItem inventoryItem, WriteOffProductDialog.WriteOffCallback writeOffCallback);
    void openReturnInvoice(Long productId, Long vendorId, Long id);
    void openWarningDialog(String text);
    void setMode(int i);
}
