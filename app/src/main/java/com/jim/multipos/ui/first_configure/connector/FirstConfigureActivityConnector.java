package com.jim.multipos.ui.first_configure.connector;

import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.ui.customer_group.CustomerGroupActivity;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.FirstConfigureActivityEvent;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * Created by user on 05.10.17.
 */

public abstract class FirstConfigureActivityConnector extends RxForPresenter {
    public static final String OPEN_NEXT_FROM_POS_DETAILS = "open_next_from_pos_details";
    public static final String OPEN_NEXT_FROM_ACCOUNT = "open_next_from_account";

    public abstract void getPosDetailsData();

    private RxBus rxBus;
    private RxBusLocal rxBusLocal;

    @Override
    public void initConnectors(RxBus rxBus, RxBusLocal rxBusLocal) {
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;
        setRxListners();
    }

    @Override
    public void setRxListners() {
        ArrayList<Disposable> subscriptions = new ArrayList<>();
        subscriptions.add(rxBusLocal.toObservable().subscribe(o -> {
            if (o instanceof FirstConfigureActivityEvent) {
                if (((FirstConfigureActivityEvent) o).getEventType().equals(OPEN_NEXT_FROM_POS_DETAILS)) {
                    getPosDetailsData();
                }
            }
        }));

        initUnsubscribers(rxBus, CustomerGroupActivity.class.getName(), subscriptions);
    }
}
