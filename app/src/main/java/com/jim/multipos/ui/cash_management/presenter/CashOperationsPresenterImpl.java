package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.ui.cash_management.view.CashOperationsView;

/**
 * Created by Sirojiddin on 11.01.2018.
 */

public class CashOperationsPresenterImpl extends BasePresenterImpl<CashOperationsView> implements CashOperationsPresenter {

    protected CashOperationsPresenterImpl(CashOperationsView view) {
        super(view);
    }
}
