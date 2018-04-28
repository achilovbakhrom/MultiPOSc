package com.jim.multipos.ui.mainpospage.presenter;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.model.DiscountItem;
import com.jim.multipos.ui.mainpospage.model.ServiceFeeItem;
import com.jim.multipos.ui.mainpospage.view.OrderListHistoryView;

import org.greenrobot.greendao.query.LazyList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by developer on 02.02.2018.
 */

public class OrderListHistoryPresenterImpl extends BasePresenterImpl<OrderListHistoryView> implements OrderListHistoryPresenter {

    Order order;
    List<Object> list;
    private DatabaseManager databaseManager;
    private PreferencesHelper preferencesHelper;

    @Inject
    protected OrderListHistoryPresenterImpl(OrderListHistoryView orderListHistoryView, DatabaseManager databaseManager, PreferencesHelper preferencesHelper) {
        super(orderListHistoryView);
        this.databaseManager = databaseManager;
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        list = new ArrayList<>();
        view.initOrderListRecycler(list);
        if(bundle!=null){
            order = databaseManager.getOrder(bundle.getLong(MainPosPageActivity.INIT_ORDER)).blockingGet();
            goToEdit = false;
            updateItemDetials();
            view.updateDetials(order);
            //TODO INIT UI FOR UI
        }
    }


    @Override
    public void refreshData(Order order) {
        goToEdit = false;
        this.order = order;
        view.updateDetials(order);
        updateItemDetials();

    }

    @Override
    public void onClickPaymentDetials() {
        view.openPaymentDetailDialog(order.getPayedPartitions(),databaseManager.getMainCurrency());
    }
    boolean goToEdit = false;
//    @Override
//    public void onEditOrder(String reason) {
//        goToEdit = true;
//        view.openEditFragment(reason,order);
//    }
//

//
//    @Override
//    public void onRestoreDialog() {
//        OrderChangesLog orderChangesLog = new OrderChangesLog();
//
//
//        List<OrderChangesLog> orderChangesLogs = order.getOrderChangesLogsHistory();
//        for (int i = orderChangesLogs.size()-1; i > 0 ; i--) {
//            if(orderChangesLogs.get(i).getToStatus() == Order.CANCELED_ORDER){
//                if(orderChangesLogs.get(i-1).getToStatus() == Order.HOLD_ORDER ){
//                    order.setStatus(Order.HOLD_ORDER);
//                    orderChangesLog.setToStatus(Order.HOLD_ORDER);
//                }else if(orderChangesLogs.get(i-1).getToStatus() == Order.CLOSED_ORDER){
//                    order.setStatus(Order.CLOSED_ORDER);
//                    orderChangesLog.setToStatus(Order.CLOSED_ORDER);
//                }
//            }
//        }
//
//        orderChangesLog.setChangedAt(System.currentTimeMillis());
//        orderChangesLog.setReason("");
//        orderChangesLog.setChangedCauseType(OrderChangesLog.HAND);
//        orderChangesLog.setOrderId(order.getId());
//        databaseManager.insertOrderChangeLog(orderChangesLog).blockingGet();
//        order.setLastChangeLogId(orderChangesLog.getId());
//        for (int i = 0; i < order.getOrderProducts().size(); i++) {
//            //Warehouse Operation
//            WarehouseOperations warehouseOperations = new WarehouseOperations();
//            warehouseOperations.setValue(order.getOrderProducts().get(i).getCount()*-1);
//            warehouseOperations.setProduct(order.getOrderProducts().get(i).getProduct());
//            warehouseOperations.setCreateAt(System.currentTimeMillis());
//            warehouseOperations.setActive(true);
//            warehouseOperations.setIsNotModified(true);
//            warehouseOperations.setType(WarehouseOperations.SOLD);
//            warehouseOperations.setOrderId(order.getId());
//            warehouseOperations.setVendorId(order.getOrderProducts().get(i).getVendorId());
//            databaseManager.insertWarehouseOperation(warehouseOperations).blockingGet();
//            order.getOrderProducts().get(i).setWarehouseGetId(warehouseOperations.getId());
//        }
//        if(order.getDebt() !=null) {
//            order.getDebt().setIsDeleted(false);
//            databaseManager.addDebt(order.getDebt());
//        }
//        databaseManager.insertOrderProducts(order.getOrderProducts()).blockingGet();
//        databaseManager.insertOrder(order).blockingGet();
//        view.updateDetials(order);
//        updateItemDetials();
//
//    }

    @Override
    public void onBruteForce() {
        //TODO SOME ACTION WHEN BRUTE FORCE EDIT ORDER
    }

    @Override
    public void onEditClicked() {
        if(order.getStatus() == Order.CLOSED_ORDER)
            view.openEditAccsessDialog();
        else if(order.getStatus() == Order.HOLD_ORDER){
            view.onContinuePressed(order);
        }else {
            //TODO EDIT REPEATLY
            view.openEditAccsessDialog();
        }
    }

    @Override
    public void onCancelClicked() {
        if(order.getStatus()!=Order.CANCELED_ORDER)
            view.openCancelAccsessDialog();
        else {
            //TODO RESTORE ORDER
            view.openRestoreAccsessDialog();
        }
    }

    @Override
    public void reprintOrder() {
        view.checkOrder(order,databaseManager,preferencesHelper);
    }

//    @Override
//    public void onEditComplete(String reason,Long orderId) {
//        order.setStatus(Order.CANCELED_ORDER);
//
//        OrderChangesLog orderChangesLog = new OrderChangesLog();
//        orderChangesLog.setToStatus(Order.CANCELED_ORDER);
//        orderChangesLog.setChangedAt(System.currentTimeMillis());
//        orderChangesLog.setReason(reason);
//        orderChangesLog.setChangedCauseType(OrderChangesLog.EDITED);
//        orderChangesLog.setOrderId(order.getId());
//        orderChangesLog.setRelationshipOrderId(orderId);
//        databaseManager.insertOrderChangeLog(orderChangesLog).blockingGet();
//        order.setLastChangeLogId(orderChangesLog.getId());
//
//        for (int i = 0; i < order.getOrderProducts().size(); i++) {
//            //Warehouse Operation
//            WarehouseOperations warehouseOperations = new WarehouseOperations();
//            warehouseOperations.setValue(order.getOrderProducts().get(i).getCount());
//            warehouseOperations.setProduct(order.getOrderProducts().get(i).getProduct());
//            warehouseOperations.setCreateAt(System.currentTimeMillis());
//            warehouseOperations.setActive(true);
//            warehouseOperations.setIsNotModified(true);
//            warehouseOperations.setType(WarehouseOperations.CANCELED_SOLD);
//            warehouseOperations.setOrderId(order.getId());
//            warehouseOperations.setVendorId(order.getOrderProducts().get(i).getVendorId());
//            databaseManager.insertWarehouseOperation(warehouseOperations).blockingGet();
//            order.getOrderProducts().get(i).setWarehouseReturnId(warehouseOperations.getId());
//        }
//
//        if(order.getDebt() !=null) {
//            order.getDebt().setIsDeleted(true);
//            databaseManager.addDebt(order.getDebt());
//        }
//        databaseManager.insertOrderProducts(order.getOrderProducts()).blockingGet();
//        databaseManager.insertOrder(order).blockingGet();
//        view.updateDetials(order);
//        updateItemDetials();
//    }





    private void updateItemDetials(){
        list.clear();
        list.addAll(order.getOrderProducts());
        if(order.getDiscount() != null){
            DiscountItem discountItem = new DiscountItem();
            discountItem.setDiscount(order.getDiscount());
            discountItem.setAmmount(order.getDiscountAmount());
            list.add(discountItem);
        }
        if(order.getServiceFee() != null){
            ServiceFeeItem serviceFeeItem = new ServiceFeeItem();
            serviceFeeItem.setServiceFee(order.getServiceFee());
            serviceFeeItem.setAmmount(order.getServiceAmount());
            list.add(serviceFeeItem);
        }
        view.notifyList();
    }
}
