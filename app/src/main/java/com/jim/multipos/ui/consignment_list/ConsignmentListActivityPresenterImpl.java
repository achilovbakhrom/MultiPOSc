package com.jim.multipos.ui.consignment_list;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.ui.consignment.ConsignmentActivityPresenter;
import com.jim.multipos.ui.consignment.ConsignmentActivityView;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class ConsignmentListActivityPresenterImpl extends BasePresenterImpl<ConsignmentListActivityView> implements ConsignmentListActivityPresenter {

    @Inject
    protected ConsignmentListActivityPresenterImpl(ConsignmentListActivityView view) {
        super(view);
    }
}
