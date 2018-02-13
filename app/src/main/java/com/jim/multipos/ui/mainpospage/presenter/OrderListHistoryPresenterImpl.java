package com.jim.multipos.ui.mainpospage.presenter;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.inventory.WarehouseOperations;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.ui.mainpospage.model.DiscountItem;
import com.jim.multipos.ui.mainpospage.model.ServiceFeeItem;
import com.jim.multipos.ui.mainpospage.view.OrderListHistoryView;

import org.greenrobot.greendao.query.LazyList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * Created by developer on 02.02.2018.
 */

public class OrderListHistoryPresenterImpl extends BasePresenterImpl<OrderListHistoryView> implements OrderListHistoryPresenter {

    Order order;
    LazyList<Order> orderList;
    List<Object> list;
    int counter;
    private DatabaseManager databaseManager;

    @Inject
    protected OrderListHistoryPresenterImpl(OrderListHistoryView orderListHistoryView, DatabaseManager databaseManager) {
        super(orderListHistoryView);
        this.databaseManager = databaseManager;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        list = new ArrayList<>();
        view.initOrderListRecycler(list);
//        refreshOrderList();
//        view.updateDetials(order);
    }

    @Override
    public void onNextOrder() {
        if(order == null){
            refreshOrderList();
        }
        counter++;
        if(counter>=orderList.size()) {
            counter = orderList.size()-1;
            view.hideMeAndShowOrderList();
            return;
        }
        order = orderList.get(counter);
        view.updateDetials(order);
        updateItemDetials();
    }

    @Override
    public void onPrevOrder() {
        if(order == null){
            refreshOrderList();
        }
        counter--;
        if(counter<0) counter = 0;
        order = orderList.get(counter);
        view.updateDetials(order);
        updateItemDetials();
    }

    @Override
    public void refreshData() {
        refreshOrderList();
        view.updateDetials(order);
        updateItemDetials();
    }

    @Override
    public void onClickPaymentDetials() {
        view.openPaymentDetailDialog(order.getPayedPartitions(),databaseManager.getMainCurrency());
    }

    @Override
    public void onEditOrder(String reason) {
        view.openEditFragment(reason,order);
        view.hideMeAndShowOrderList();
    }

    @Override
    public void onDeleteOrder(String reason) {
        order.setIsDeleted(true);
        order.setDeleteCause(reason);
        for (int i = 0; i < order.getOrderProducts().size(); i++) {
            //Warehouse Operation
            WarehouseOperations warehouseOperations = new WarehouseOperations();
            warehouseOperations.setValue(order.getOrderProducts().get(i).getCount());
            warehouseOperations.setProduct(order.getOrderProducts().get(i).getProduct());
            warehouseOperations.setCreateAt(System.currentTimeMillis());
            warehouseOperations.setActive(true);
            warehouseOperations.setIsNotModified(true);
            warehouseOperations.setType(WarehouseOperations.CANCELED_SOLD);
            warehouseOperations.setOrderId(order.getId());
            warehouseOperations.setVendorId(order.getOrderProducts().get(i).getVendorId());
            databaseManager.insertWarehouseOperation(warehouseOperations).blockingGet();
            order.getOrderProducts().get(i).setWarehouseReturnId(warehouseOperations.getId());
        }
        order.setDeleteAt(System.currentTimeMillis());
        if(order.getDebt() !=null) {
            order.getDebt().setIsDeleted(true);
            databaseManager.addDebt(order.getDebt());
        }
        databaseManager.insertOrderProducts(order.getOrderProducts()).blockingGet();
        databaseManager.insertOrder(order).blockingGet();
        view.updateDetials(order);
        updateItemDetials();
    }

    @Override
    public void onRestoreDialog() {
        order.setIsDeleted(false);
        order.setDeleteCause("");
        for (int i = 0; i < order.getOrderProducts().size(); i++) {
            //Warehouse Operation
            WarehouseOperations warehouseOperations = new WarehouseOperations();
            warehouseOperations.setValue(order.getOrderProducts().get(i).getCount()*-1);
            warehouseOperations.setProduct(order.getOrderProducts().get(i).getProduct());
            warehouseOperations.setCreateAt(System.currentTimeMillis());
            warehouseOperations.setActive(true);
            warehouseOperations.setIsNotModified(true);
            warehouseOperations.setType(WarehouseOperations.SOLD);
            warehouseOperations.setOrderId(order.getId());
            warehouseOperations.setVendorId(order.getOrderProducts().get(i).getVendorId());
            databaseManager.insertWarehouseOperation(warehouseOperations).blockingGet();
            order.getOrderProducts().get(i).setWarehouseGetId(warehouseOperations.getId());
        }
        order.setDeleteAt(System.currentTimeMillis());
        if(order.getDebt() !=null) {
            order.getDebt().setIsDeleted(false);
            databaseManager.addDebt(order.getDebt());
        }
        databaseManager.insertOrderProducts(order.getOrderProducts()).blockingGet();
        databaseManager.insertOrder(order).blockingGet();
        view.updateDetials(order);
        updateItemDetials();
    }

    @Override
    public void onBruteForce() {
        //TODO SOME ACTION WHEN BRUTE FORCE EDIT ORDER
    }

    @Override
    public void onEditClicked() {
        if(order.getStatus() == Order.SIMPLE_ORDER)
            view.openEditAccsessDialog();
        else {
            //TODO EDIT REPEATLY
            view.openEditAccsessDialog();
        }
    }

    @Override
    public void onCancelClicked() {
        if(!order.getIsDeleted())
            view.openCancelAccsessDialog();
        else {
            //TODO RESTORE ORDER
            view.openRestoreAccsessDialog();
        }
    }


    private void refreshOrderList(){
        orderList = databaseManager.getAllTillLazyOrders().blockingGet();
        counter = orderList.size()-1;
        order = orderList.get(counter);
    }


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
