package com.jim.multipos.ui.mainpospage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpActionButton;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.order.Order;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 26.02.2018.
 */

public class TodayOrdersAdapter extends RecyclerView.Adapter<TodayOrdersAdapter.TodayOrdersViewHolder> {

    private List<Order> items;
    private Context context;
    private Currency mainCurrency;
    private onOrderListItemSelect listener;
    private DecimalFormat decimalFormat;

    public TodayOrdersAdapter(Context context, Currency mainCurrency, onOrderListItemSelect listener) {
        this.context = context;
        this.mainCurrency = mainCurrency;
        this.listener = listener;
        items = new ArrayList<>();
        decimalFormat = BaseAppModule.getFormatterWithoutGroupingTwoDecimal();
    }

    public void setData(List<Order> data) {
        this.items = data;
    }

    @Override
    public TodayOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_order_dialog_item, parent, false);
        return new TodayOrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodayOrdersViewHolder holder, int position) {
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
        holder.tvTotal.setText(decimalFormat.format(order.getForPayAmmount()) + " " + mainCurrency.getAbbr());
        switch (order.getStatus()) {
            case Order.HOLD_ORDER:
                holder.tvStatus.setText(context.getString(R.string.hold));
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));
                break;
            case Order.CANCELED_ORDER:
                holder.tvStatus.setText(context.getString(R.string.canceled));
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                break;
            case Order.CLOSED_ORDER:
                holder.tvStatus.setText(context.getString(R.string.closed));
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class TodayOrdersViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.btnSelect)
        MpActionButton btnSelect;

        public TodayOrdersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnSelect.setOnClickListener(view -> listener.onSelect(items.get(getAdapterPosition())));
        }
    }

    public interface onOrderListItemSelect {
        void onSelect(Order order);
    }
}
