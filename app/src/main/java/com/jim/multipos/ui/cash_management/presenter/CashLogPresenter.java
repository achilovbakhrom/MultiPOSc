package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.core.Presenter;

/**
 * Created by Sirojiddin on 11.01.2018.
 */

public interface CashLogPresenter extends Presenter {
    void initData();
    void setDataToDetailsFragment(int position);
    void changeAccount(Long accountId);
}
