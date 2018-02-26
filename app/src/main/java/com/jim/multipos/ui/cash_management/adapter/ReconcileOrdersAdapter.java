package com.jim.multipos.ui.cash_management.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.ui.cash_management.dialog.ExtraOptionsDialog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 13.01.2018.
 */

public class ReconcileOrdersAdapter extends RecyclerView.Adapter<ReconcileOrdersAdapter.ReconcileOrdersViewHolder> {

    private List<Order> items;
    private Context context;
    private ReconcileOrdersAdapter.onExtraOptionItemClicked onExtraOptionItemClicked;

    public ReconcileOrdersAdapter(Context context, onExtraOptionItemClicked onExtraOptionItemClicked) {
        this.onExtraOptionItemClicked = onExtraOptionItemClicked;
        items = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ReconcileOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reconcile_order_list_item, parent, false);
        return new ReconcileOrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReconcileOrdersViewHolder holder, int position) {
        Order order = items.get(position);
        if (position % 2 == 0)
            holder.llBackground.setBackgroundColor(Color.parseColor("#e4f5ff"));
        else holder.llBackground.setBackgroundColor(Color.parseColor("#d1eafa"));
        holder.tvOrderNumber.setText(String.valueOf(order.getId()));
        if (order.getCustomer() != null)
            holder.tvCustomer.setText(order.getCustomer().getName());
        else holder.tvCustomer.setText("-");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        holder.tvDateOpened.setText(format.format(order.getCreateAt()));
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
        holder.tvTotal.setText(decimalFormat.format(order.getForPayAmmount()));
        holder.tvDueSum.setText(decimalFormat.format(order.getForPayAmmount() - order.getTotalPayed()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<Order> orderList) {
        this.items = orderList;
        notifyDataSetChanged();
    }

    class ReconcileOrdersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llBackground)
        LinearLayout llBackground;
        @BindView(R.id.tvOrderNumber)
        TextView tvOrderNumber;
        @BindView(R.id.tvCustomer)
        TextView tvCustomer;
        @BindView(R.id.tvDateOpened)
        TextView tvDateOpened;
        @BindView(R.id.tvTotal)
        TextView tvTotal;
        @BindView(R.id.tvDueSum)
        TextView tvDueSum;
        @BindView(R.id.llExtraOptions)
        LinearLayout llExtraOptions;

        public ReconcileOrdersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            llExtraOptions.setOnClickListener(view -> {
                ExtraOptionsDialog extraOptionsDialog = new ExtraOptionsDialog(context, new ExtraOptionsDialog.onExtraOptionClicked() {
                    @Override
                    public void onReturn() {
                        onExtraOptionItemClicked.onReturn(items.get(getAdapterPosition()), getAdapterPosition());
                    }

                    @Override
                    public void onClose() {
                        onExtraOptionItemClicked.onClose(items.get(getAdapterPosition()), getAdapterPosition());
                    }
                });
                extraOptionsDialog.show();
            });

        }
    }

    public interface onExtraOptionItemClicked{
        void onReturn(Order order, int position);
        void onClose(Order order, int position);
    }
}
