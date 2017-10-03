package com.jim.multipos.ui.product_class.presenters;

import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.ui.product_class.ProductClassActivity;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.ProductClassEvent;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * Created by developer on 06.09.2017.
 */

public abstract class AddProductClassConnector extends RxForPresenter {
    public static final String CLICK_PRODUCT_CLASS = "click_prog";
    public static final String ADD_PRODUCT_CLASS = "add_prog";
    public abstract void onClickProductClass(ProductClass productClass);
    public abstract void addProductClass();


    private RxBus rxBus;
    private RxBusLocal rxBusLocal;
    @Override
    public void initConnectors(RxBus rxBus,RxBusLocal rxBusLocal){
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;
        setRxListners();
    };
    @Override
    public void setRxListners() {
        ArrayList<Disposable> subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBusLocal.toObservable().subscribe(o -> {
                    if(o instanceof ProductClassEvent){
                        ProductClassEvent productClassEvent = (ProductClassEvent) o;
                        if(productClassEvent.getEventType().equals(CLICK_PRODUCT_CLASS)){
                            onClickProductClass(productClassEvent.getProductClass());

                        }else if(productClassEvent.getEventType().equals(ADD_PRODUCT_CLASS)){
                            addProductClass();


                        }
                    }}));

        initUnsubscribers(rxBus, ProductClassActivity.class.getName(),subscriptions);
    }
}
