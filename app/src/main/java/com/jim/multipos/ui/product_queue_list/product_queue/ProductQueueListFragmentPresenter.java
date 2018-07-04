package com.jim.multipos.ui.product_queue_list.product_queue;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.inventory.StockQueue;

import java.util.Calendar;

public interface ProductQueueListFragmentPresenter extends Presenter {
    void initData(Long productId, Long vendorId);
    void search(String string);
    void openDateIntervalPicker();
    void onFilterClicked();
    void dateIntervalPicked(Calendar fromDate1, Calendar toDate1);
    void onFilterApplied(int[] config);
    void onWriteOff(StockQueue stockQueue);
    void onOutVoice(StockQueue stockQueue);
}
