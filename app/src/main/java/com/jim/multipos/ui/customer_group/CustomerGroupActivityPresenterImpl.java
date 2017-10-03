package com.jim.multipos.ui.customer_group;

import android.content.Context;

import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.data.operations.CustomerGroupOperations;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;

/**
 * Created by user on 05.09.17.
 */

public class CustomerGroupActivityPresenterImpl extends RxForPresenter implements CustomerGroupActivityPresenter {
    private RxBus rxBus;
    private Context context;
    private CustomerGroupOperations operations;
    private CustomerGroupView view;

    public CustomerGroupActivityPresenterImpl(RxBus rxBus, Context context, CustomerGroupView view, CustomerGroupOperations operations) {
        this.rxBus = rxBus;
        this.context = context;
        this.operations = operations;
        this.view = view;
    }

    @Override
    public void setRxListners() {
        //TODO
    }

    @Override
    public void initConnectors(RxBus rxBus, RxBusLocal rxBusLocal) {

    }
}
