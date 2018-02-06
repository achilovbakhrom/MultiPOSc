package com.jim.multipos.ui.mainpospage.presenter;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ServiceFee;
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
