package com.jim.multipos.utils;


import io.reactivex.Observable;
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
}