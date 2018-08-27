package com.jim.multipos.ui.admin_main_page.fragments.dashboard.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.model.Orders;

import java.util.List;

import butterknife.BindView;

public class OrdersAdapter extends BaseAdapter<Orders, OrdersAdapter.OrdersViewHolder> {

    public OrdersAdapter(List<Orders> items) {
        super(items);
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdersViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_dashboard_orders_rv_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        Orders orders = items.get(position);
        holder.tvDate.setText(orders.getDate());
        holder.tvProducts.setText(orders.getProducts());
        holder.tvStatus.setText(orders.getStatus());
        holder.tvSum.setText(orders.getSum());
    }

    public class OrdersViewHolder extends BaseViewHolder {

        @BindView(R.id.tvSum)
        TextView tvSum;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvProducts)
        TextView tvProducts;
        @BindView(R.id.tvStatus)
        TextView tvStatus;

        public OrdersViewHolder(View itemView) {
            super(itemView);
        }
    }
}
