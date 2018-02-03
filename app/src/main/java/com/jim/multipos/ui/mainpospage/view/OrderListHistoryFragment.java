package com.jim.multipos.ui.mainpospage.view;

import android.os.Bundle;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.ui.mainpospage.presenter.OrderListHistoryPresenter;

import javax.inject.Inject;

/**
 * Created by developer on 02.02.2018.
 */

public class OrderListHistoryFragment extends BaseFragment implements OrderListHistoryView{

    @Inject
    OrderListHistoryPresenter presenter;
    @Inject
    MainPageConnection mainPageConnection;

    @Override
    protected int getLayout() {
        return R.layout.fragment_order_list_history;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mainPageConnection.setOrderListHistoryView(this);
    }

    public void refreshData(){

    }
    public void onNextOrder(){
        presenter.onNextOrder();
    }
    public void onPrevOrder(){
        presenter.onPrevOrder();
    }
    @Override
    public void onDetach() {
        mainPageConnection.setOrderListHistoryView(null);
        super.onDetach();
    }

    @Override
    public void hideMeAndShowOrderList() {
        ((MainPosPageActivity)getActivity()).showOrderListFragmentWhenOrderHistoryEnds();
    }

    @Override
    public void updateDetials(Order order) {
        //Todo all update operations
    }
}
