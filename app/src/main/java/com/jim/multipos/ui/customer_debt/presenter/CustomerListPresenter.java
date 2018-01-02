package com.jim.multipos.ui.customer_debt.presenter;

import com.jim.multipos.core.Presenter;

/**
 * Created by Sirojiddin on 29.12.2017.
 */

public interface CustomerListPresenter extends Presenter {
    void initData();
    void setSearchText(String searchText);
}
