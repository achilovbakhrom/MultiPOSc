package com.jim.multipos.ui.mainpospage;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderChangesLog;
import com.jim.multipos.utils.managers.NotifyManager;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by developer on 07.08.2017.
 */

public class MainPosPageActivityImpl extends BasePresenterImpl<MainPosPageActivityView> implements MainPosPageActivityPresenter {
    private NotifyManager notifyManager;
    private DatabaseManager databaseManager;
    private int current; // current Order position from orderList
    private Long lastArchiveOrderId ;
    List<Order> orderList;

    @Inject
    protected MainPosPageActivityImpl(MainPosPageActivityView mainPosPageActivityView, NotifyManager notifyManager,DatabaseManager databaseManager) {
        super(mainPosPageActivityView);
        this.notifyManager = notifyManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        updateToLastOrderId();
        openNewOrder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPrevClick() {
        current --;
        if(current<0){
            current = 0;
            return;
        }else {
            view.openOrUpdateOrderHistory(orderList.get(current));
        }
    }

    @Override
    public void onNextClick() {
        if(current == orderList.size()) return;
        current ++;
        if(current >= orderList.size()){
            openNewOrder();
        }else {
            view.openOrUpdateOrderHistory(orderList.get(current));
        }
    }

    @Override
    public void openOrderForEdit(Long orderId) {

    }

    @Override
    public void openOrderForView(Long orderId) {

    }

    @Override
    public void openNewOrder() {
        int location = orderList.size() - 1;
        long newOrderId = -1;
        if(location <0) {
            current = 0;
            view.openNewOrderFrame(lastArchiveOrderId + 1);
        }else {
            Order order = orderList.get(location);
            newOrderId = order.getId() + 1;
            current = location + 1 ;
            view.openNewOrderFrame(newOrderId);
        }
    }


    int oldPositionForEdit = -1;
    @Override
    public void onEditOrder(String reason) {
        Order orderForEdit = orderList.get(current);
        oldPositionForEdit = current;
        int location = orderList.size() - 1;
        long newOrderId = -1;
        if(location <0) {
            current = 0;
            view.openOrderForEdit(reason,orderForEdit,lastArchiveOrderId + 1);

        }else {
            Order order = orderList.get(location);
            newOrderId = order.getId() + 1;
            current = location + 1 ;
            view.openOrderForEdit(reason,orderForEdit,newOrderId);
        }

    }

    @Override
    public void onCancelOrder(String reason) {
        //PRIMOY SHUYOGA KELADI CANCEL
        Order order = orderList.get(current);
        order.setStatus(Order.CANCELED_ORDER);
        OrderChangesLog orderChangesLog = new OrderChangesLog();
        orderChangesLog.setToStatus(Order.CANCELED_ORDER);
        orderChangesLog.setChangedAt(System.currentTimeMillis());
        orderChangesLog.setReason(reason);
        orderChangesLog.setChangedCauseType(OrderChangesLog.HAND);
        orderChangesLog.setOrderId(order.getId());
        databaseManager.insertOrderChangeLog(orderChangesLog).blockingGet();
        order.setLastChangeLogId(orderChangesLog.getId());
            //OrderCanceled
        databaseManager.cancelOutcomeProductWhenOrderProductCanceled(order.getOrderProducts()).blockingGet();
        if(order.getDebt() !=null) {
            order.getDebt().setIsDeleted(true);
            databaseManager.addDebt(order.getDebt()).blockingGet();
        }
        databaseManager.insertOrder(order).blockingGet();
        orderList.set(current,order);
        view.openOrUpdateOrderHistory(orderList.get(current));
    }


    private void updateToLastOrderId(){
        orderList = databaseManager.getAllTillOrders().blockingGet();
        lastArchiveOrderId = databaseManager.getLastArchiveOrderId().blockingGet();
    }

    /**
     *
     * 6 events Order operations
     * Configure here
     *
     * Order exactle inserted to database, in here we only control orderlist manageing logic
     *
     * **/

    @Override
    public void holdOrderClosed(Order order) {
        order.resetOrderProducts();
        order.resetOrderChangesLogsHistory();
        order.resetPayedPartitions();
        orderList.set(current,order);
        view.openOrUpdateOrderHistory(orderList.get(current));
    }

    @Override
    public void newOrderHolded(Order order) {

        orderList.add(order);
        openNewOrder();
    }

    @Override
    public void holdOrderHolded(Order order) {
        order.resetOrderProducts();
        order.resetOrderChangesLogsHistory();
        order.resetPayedPartitions();
        orderList.set(current,order);
        view.openOrUpdateOrderHistory(orderList.get(current));
    }

    @Override
    public void editedOrderHolded(String reason, Order newHoldOrder) {
        Order order = orderList.get(oldPositionForEdit);
        //TODO CHECK REFRESH OPTIMIZATION
        order = databaseManager.getOrder(order.getId()).blockingGet();
        orderList.add(newHoldOrder);
        orderList.set(oldPositionForEdit,order);
        view.openOrUpdateOrderHistory(orderList.get(current));
    }

    @Override
    public void onTillClose() {
        updateToLastOrderId();
        openNewOrder();
    }

    @Override
    public void onOrderCanceledFromOutSide(Long orderId) {
        for (int i = 0; i < orderList.size(); i++) {
            if(orderList.get(i).getId().equals(orderId)){
                orderList.get(i).refresh();
                orderList.get(i).resetOrderProducts();
                orderList.get(i).resetOrderChangesLogsHistory();
                orderList.get(i).resetPayedPartitions();
                if(current == i){
                    view.openOrUpdateOrderHistory(orderList.get(current));
                }
            }
        }
    }

    @Override
    public void onOrderClosedFromOutSide(Long orderId) {
        for (int i = 0; i < orderList.size(); i++) {
            if(orderList.get(i).getId().equals(orderId)){
                orderList.get(i).refresh();
                orderList.get(i).resetOrderProducts();
                orderList.get(i).resetOrderChangesLogsHistory();
                orderList.get(i).resetPayedPartitions();
                if(current == i){
                    view.openOrUpdateOrderHistory(orderList.get(current));
                }
            }
        }
    }

    @Override
    public void onHeldOrderSelected(Order selectedOrder) {
        for(int i = 0; i<orderList.size();i++){
            if(orderList.get(i).getId().equals(selectedOrder.getId())){
                current = i;
                view.openOrUpdateOrderHistory(orderList.get(current));
                return;
            }
        }
    }

    @Override
    public void onTodayOrderSelected(Order selectedOrder) {
        for(int i = 0; i<orderList.size();i++){
            if(orderList.get(i).getId().equals(selectedOrder.getId())){
                current = i;
                view.openOrUpdateOrderHistory(orderList.get(current));
                return;
            }
        }
    }


    @Override
    public void orderAdded(Order order) {
        orderList.add(order);
        openNewOrder();
    }



    @Override
    public void onEditComplete(String reason,Order newOrder) {
        //old Order
        Order order = orderList.get(oldPositionForEdit);
        orderList.add(newOrder);
        orderList.set(oldPositionForEdit,order);
        //CONFIG EDIT QILINGAN YANGI VERSIYADA QOSINMI ILI YANGI ORDER QIVORSINMI?
//        openNewOrder();
        view.openOrUpdateOrderHistory(orderList.get(current));

    }
}
