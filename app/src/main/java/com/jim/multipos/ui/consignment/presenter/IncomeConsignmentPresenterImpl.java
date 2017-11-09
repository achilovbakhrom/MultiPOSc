package com.jim.multipos.ui.consignment.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.ui.consignment.view.IncomeConsignmentView;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class IncomeConsignmentPresenterImpl extends BasePresenterImpl<IncomeConsignmentView> implements IncomeConsignmentPresenter {

    @Inject
    protected IncomeConsignmentPresenterImpl(IncomeConsignmentView incomeConsignmentView) {
        super(incomeConsignmentView);
    }
}
