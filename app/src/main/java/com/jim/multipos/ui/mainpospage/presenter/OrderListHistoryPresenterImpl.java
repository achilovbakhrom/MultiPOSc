package com.jim.multipos.ui.mainpospage.presenter;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.ui.mainpospage.view.OrderListHistoryView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * Created by developer on 02.02.2018.
 */

public class OrderListHistoryPresenterImpl extends BasePresenterImpl<OrderListHistoryView> implements OrderListHistoryPresenter {
    Order order;
    private DatabaseManager databaseManager;

    @Inject
    protected OrderListHistoryPresenterImpl(OrderListHistoryView orderListHistoryView, DatabaseManager databaseManager) {
        super(orderListHistoryView);
        this.databaseManager = databaseManager;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
    }

    @Override
    public void onNextOrder() {

    }

    @Override
    public void onPrevOrder() {
        if(order == null){
            Single<List<Order>> allTillOrders = databaseManager.getAllTillOrders();
        }
    }
}
