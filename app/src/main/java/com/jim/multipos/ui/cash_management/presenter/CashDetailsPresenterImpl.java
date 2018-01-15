package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.ui.cash_management.view.CashDetailsView;

/**
 * Created by Sirojiddin on 12.01.2018.
 */

public class CashDetailsPresenterImpl extends BasePresenterImpl<CashDetailsView> implements CashDetailsPresenter {

    protected CashDetailsPresenterImpl(CashDetailsView view) {
        super(view);
    }
}
