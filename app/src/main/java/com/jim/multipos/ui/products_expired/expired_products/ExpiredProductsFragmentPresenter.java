package com.jim.multipos.ui.products_expired.expired_products;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.inventory.StockQueue;

public interface ExpiredProductsFragmentPresenter extends Presenter {
    void onFilterClicked();
    void search(String searchText);
    void onWriteOff(StockQueue stockQueue);
    void onOutVoice(StockQueue stockQueue);
    void onFilterApplied(int[] config);
}
