package com.jim.multipos.utils.rxevents.main_order_events;

import com.jim.multipos.data.db.model.order.Order;


/**
 * Created by Sirojiddin on 28.02.2018.
 */
public class OrderEvent {
    int type;
    Order order;

    public OrderEvent(int type, Order order) {
        this.type = type;
        this.order = order;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
