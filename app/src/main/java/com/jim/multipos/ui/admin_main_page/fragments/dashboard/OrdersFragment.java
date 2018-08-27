package com.jim.multipos.ui.admin_main_page.fragments.dashboard;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.adapter.OrdersAdapter;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.model.Orders;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OrdersFragment extends BaseFragment {

    @BindView(R.id.rvOrders)
    RecyclerView rvOrders;

    List<Orders> items;

    @Override
    protected int getLayout() {
        return R.layout.admin_dashboard_orders_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        items = new ArrayList<>();
        items.add(new Orders("17200", "16:00 28.08.2018", "17", "Closed"));
        items.add(new Orders("17200", "16:00 28.08.2018", "17", "Closed"));
        items.add(new Orders("17200", "16:00 28.08.2018", "17", "Closed"));
        items.add(new Orders("17200 ", "16:00 28.08.2018", "17", "Closed"));
        rvOrders.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvOrders.setAdapter(new OrdersAdapter(items));
    }
}
