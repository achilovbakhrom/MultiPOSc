package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderProduct;

import org.greenrobot.greendao.query.LazyList;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by developer on 30.01.2018.
 */

public interface OrderOperations {
    Single<Order> insertOrder(Order order);
    Single<List<OrderProduct>> insertOrderProducts(List<OrderProduct> orderProducts);
    Single<List<Order>> getAllTillOrders();
    Single<LazyList<Order>> getAllTillLazyOrders();

}
