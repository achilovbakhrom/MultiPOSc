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


    @Override
    public void onBruteForce() {
        //SOME ACTION WHEN BRUTE FORCE EDIT ORDER
    }

    @Override
    public void onEditClicked() {
        if(order.getStatus() == Order.CLOSED_ORDER)
            view.openEditAccsessDialog();
        else if(order.getStatus() == Order.HOLD_ORDER){
            view.onContinuePressed(order);
        }else {
            //EDIT REPEATLY
            view.openEditAccsessDialog();
        }
    }

    @Override
    public void onCancelClicked() {
        if(order.getStatus()!=Order.CANCELED_ORDER)
            view.openCancelAccsessDialog();

    }

    @Override
    public void reprintOrder() {
        view.reprint(order,databaseManager,preferencesHelper);
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
