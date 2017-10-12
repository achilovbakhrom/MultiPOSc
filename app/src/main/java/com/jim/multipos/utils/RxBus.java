package com.jim.multipos.utils;


import com.jim.multipos.utils.rxevents.Unsibscribe;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by DEV on 16.08.2017.
 */

public class RxBus {
    public RxBus() {
    }

    private PublishSubject<Object> bus = PublishSubject.create();

    public void send(Object o) {
        bus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
    public static void removeListners(List<Disposable> disposables){
                if (disposables != null && !disposables.isEmpty())
                    for (Disposable disposable : disposables)
                        disposable.dispose();
    }
}