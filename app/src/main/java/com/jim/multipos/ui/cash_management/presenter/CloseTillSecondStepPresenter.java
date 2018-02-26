package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.core.Presenter;

/**
 * Created by Sirojiddin on 13.01.2018.
 */

public interface CloseTillSecondStepPresenter extends Presenter{
    void initData();
    void checkCompletion();
    void collectData();
}
