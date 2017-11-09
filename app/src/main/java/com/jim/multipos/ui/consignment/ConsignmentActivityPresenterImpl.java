package com.jim.multipos.ui.consignment;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class ConsignmentActivityPresenterImpl extends BasePresenterImpl<ConsignmentActivityView> implements ConsignmentActivityPresenter {

    @Inject
    protected ConsignmentActivityPresenterImpl(ConsignmentActivityView view) {
        super(view);
    }
}
