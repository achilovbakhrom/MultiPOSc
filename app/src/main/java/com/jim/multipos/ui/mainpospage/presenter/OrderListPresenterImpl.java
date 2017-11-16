package com.jim.multipos.ui.mainpospage.presenter;

import android.os.Bundle;
import android.util.Log;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.ui.mainpospage.view.OrderListView;

import org.greenrobot.greendao.database.Database;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public class OrderListPresenterImpl extends BasePresenterImpl<OrderListView> implements OrderListPresenter {
    private DatabaseManager databaseManager;

    @Inject
    public OrderListPresenterImpl(OrderListView orderListView, DatabaseManager databaseManager) {
        super(orderListView);

        this.databaseManager = databaseManager;
    }

    @Override
    public List<Discount> getDiscounts() {
        Log.d("myLogs", "Size of discounts = " + databaseManager.getAllDiscounts().blockingGet());

        return null;
    }
}
