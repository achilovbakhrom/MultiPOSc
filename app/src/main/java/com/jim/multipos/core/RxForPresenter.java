package com.jim.multipos.core;

import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.Unsibscribe;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * Created by developer on 29.08.2017.
 */

public abstract class RxForPresenter {
    ArrayList<Disposable> subscriptions;
    RxBus rxBus;
    String activityName;

    public abstract void setRxListners();

    public abstract void initConnectors(RxBus rxBus, RxBusLocal rxBusLocal);

    public void initUnsubscribers(RxBus rxBus, String activityName, ArrayList<Disposable> subscriptions) {
        this.rxBus = rxBus;
        this.activityName = activityName;
        this.subscriptions = subscriptions;
        unsubscribeRx();
    }

    public void unsubscribeRx() {
        subscriptions.add(rxBus.toObservable().subscribe(o -> {
            if (o instanceof Unsibscribe) {
                if (subscriptions != null && !subscriptions.isEmpty())
                    for (Disposable disposable : subscriptions)
                        disposable.dispose();
            }
        }));

    }
}
