package com.jim.multipos.ui.products_expired.expired_products;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.utils.WriteOffProductDialog;

import java.util.List;

public interface ExpiredProductsFragmentView extends BaseView{
    void setTime(String date);
    void setExpiredProducts(List<StockQueue> stockQueueList);
    void initSearchResults(List<StockQueue> searchResults, String searchText);
    void openFilterDialog(int[] filterConfig);
    void openReturnInvoice(Long productId, Long vendorId, Long stockQueueId);
    void openWriteOffDialog(InventoryItem inventoryItem, WriteOffProductDialog.WriteOffCallback writeOffCallback,StockQueue stockQueue);
    void openWarningDialog(String text);

}
