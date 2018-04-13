package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderChangesLog;
import com.jim.multipos.data.db.model.order.OrderProduct;

import org.greenrobot.greendao.query.LazyList;

import java.util.Calendar;
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
    Single<Integer> removeAllOrders();
    Single<Long> insertOrderChangeLog(OrderChangesLog orderChangesLog);
    Single<Long> getLastOrderId();
    Single<Long> getLastArchiveOrderId();
    Single<Order> getOrder(Long orderId);
    Single<Long> deleteOrderProductsOnHold(List<OrderProduct> orderProducts);
    Single<List<Order>> getAllHoldOrders();
    Single<List<Order>> getOrdersByTillId(Long id);
    Single<List<Order>> getAllTillClosedOrders();
    Single<List<Order>> getOrdersInIntervalForReport(Calendar fromDate,Calendar toDate);
    Single<List<Order>> getClosedOrdersInIntervalForReport(Calendar fromDate,Calendar toDate);
    Single<Order> getLastOrderWithCustomer(Long customerId);
    Single<List<Order>> getOrdersWithCustomerInInterval(Long id, Calendar fromDate, Calendar toDate);
}
