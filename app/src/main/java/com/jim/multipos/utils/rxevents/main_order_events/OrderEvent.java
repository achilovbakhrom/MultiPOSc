package com.jim.multipos.utils.rxevents.main_order_events;

import com.jim.multipos.data.db.model.order.Order;

import lombok.Data;

/**
 * Created by Sirojiddin on 28.02.2018.
 */
@Data
public class OrderEvent {
    int type;
    Order order;

    public OrderEvent(int type, Order order) {
        this.type = type;
        this.order = order;
    }
}
