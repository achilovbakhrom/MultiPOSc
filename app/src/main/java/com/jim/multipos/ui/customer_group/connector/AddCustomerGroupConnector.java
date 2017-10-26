package com.jim.multipos.ui.customer_group.connector;

import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group.CustomerGroupActivity;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CustomerGroupEvent;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * Created by user on 07.09.17.
 */

public abstract class AddCustomerGroupConnector extends RxForPresenter {
    public static final String ITEM_CLICKED = "item_clicked";
    public static final String ADD_CUSTOMER_GROUP_CLICKED = "add_group";

    public abstract void showCustomerGroup(CustomerGroup customerGroup);

    public abstract void showCustomerGroup();

    public abstract void addCustomerGroupClicked();

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
            if (o instanceof CustomerGroupEvent) {
                CustomerGroupEvent customerGroupEvent = (CustomerGroupEvent) o;
                if (customerGroupEvent.getEventType().equals(ITEM_CLICKED)) {
                    showCustomerGroup(customerGroupEvent.getCustomerGroup());
                } else if (customerGroupEvent.getEventType().equals(ADD_CUSTOMER_GROUP_CLICKED)) {
                    addCustomerGroupClicked();
                } else if (customerGroupEvent.getEventType().equals(CustomerGroupConnector.CUSTOMER_GROUP_OPENED)) {
                    showCustomerGroup();
                }
            }
        }));

        initUnsubscribers(rxBus, CustomerGroupActivity.class.getName(), subscriptions);
    }
}
