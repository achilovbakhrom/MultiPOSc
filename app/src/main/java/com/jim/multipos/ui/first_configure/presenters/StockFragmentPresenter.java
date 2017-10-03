package com.jim.multipos.ui.first_configure.presenters;

import com.jim.multipos.ui.first_configure.common.BaseFragmentPresenterFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.StockFragmentView;

/**
 * Created by user on 14.08.17.
 */

public interface StockFragmentPresenter extends BaseFragmentPresenterFirstConfig<StockFragmentView> {
    void openNextFragment();
    void addStock(String stockName, String address);
    void setData();
    void removeStock(int position);
    boolean isCompleteData();
}
