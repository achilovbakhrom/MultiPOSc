package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderChangesLog;
import com.jim.multipos.ui.cash_management.view.CloseTillFirstStepView;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 12.01.2018.
 */

public class CloseTillFirstStepPresenterImpl extends BasePresenterImpl<CloseTillFirstStepView> implements CloseTillFirstStepPresenter {

    private final DatabaseManager databaseManager;
    private List<Order> orderList;
    public static final String ORDER_CLOSED = "Order closed";
    public static final String ORDER_CANCELED = "Order canceled";

    @Inject
    protected CloseTillFirstStepPresenterImpl(CloseTillFirstStepView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
        orderList = new ArrayList<>();
    }

    @Override
    public void initAdapterData() {
        orderList = databaseManager.getAllHoldOrders().blockingGet();
        view.setAdapterList(orderList);
    }

    @Override
    public void checkCompletion() {
        if (orderList.size() > 0) {
            view.firstStepCompletionStatus(false);
        } else {
            view.firstStepCompletionStatus(true);
        }
    }

    @Override
    public void onReturn(Order order, int position, String reason) {
        order.setStatus(Order.CANCELED_ORDER);
        OrderChangesLog orderChangesLog = new OrderChangesLog();
        orderChangesLog.setToStatus(Order.CANCELED_ORDER);
        orderChangesLog.setChangedAt(System.currentTimeMillis());
        orderChangesLog.setReason(reason);
        orderChangesLog.setChangedCauseType(OrderChangesLog.HAND_AT_CLOSE_TILL);
        orderChangesLog.setOrderId(order.getId());
        databaseManager.insertOrderChangeLog(orderChangesLog).blockingGet();
        order.setLastChangeLogId(orderChangesLog.getId());

        databaseManager.cancelOutcomeProductWhenOrderProductCanceled(order.getOrderProducts()).blockingGet();

        if (order.getDebt() != null) {
            order.getDebt().setIsDeleted(true);
            databaseManager.addDebt(order.getDebt());
        }

        databaseManager.insertOrder(order).blockingGet();
        orderList.remove(position);
        view.updateOrderList();
        view.sendEvent(GlobalEventConstants.CANCEL, order);
        view.sendInventoryStateChangeEvent(GlobalEventConstants.UPDATE);
    }

    @Override
    public void onClose(Order order, int position) {
        order.setStatus(Order.CLOSED_ORDER);
        order.setIsArchive(false);
        OrderChangesLog orderChangesLog = new OrderChangesLog();
        orderChangesLog.setToStatus(Order.CLOSED_ORDER);
        orderChangesLog.setChangedAt(System.currentTimeMillis());
        orderChangesLog.setReason("");
        orderChangesLog.setChangedCauseType(OrderChangesLog.PAYED);
        orderChangesLog.setOrderId(order.getId());
        databaseManager.insertOrderChangeLog(orderChangesLog).blockingGet();
        order.setLastChangeLogId(orderChangesLog.getId());
        double totalPayedSum = 0;
        order.resetPayedPartitions();
        for (int i = 0; i < order.getPayedPartitions().size(); i++) {
            totalPayedSum += order.getPayedPartitions().get(i).getValue();
        }
        order.setTotalPayed(totalPayedSum);
        databaseManager.insertOrder(order).blockingGet();
        orderList.remove(position);
        view.updateOrderList();
        view.sendEvent(GlobalEventConstants.CLOSE, order);
    }
}
