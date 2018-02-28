package com.jim.multipos.ui.mainpospage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpActionButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.order.Order;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 26.02.2018.
 */

public class HeldOrdersAdapter extends RecyclerView.Adapter<HeldOrdersAdapter.HoldOrdersViewHolder> {

    private List<Order> items;
    private Context context;
    private Currency mainCurrency;
    private onExtraOptionItemClicked callback;

    public HeldOrdersAdapter(Context context, Currency mainCurrency, onExtraOptionItemClicked callback) {
        this.context = context;
        this.mainCurrency = mainCurrency;
        this.callback = callback;
        items = new ArrayList<>();
    }

    public void setData(List<Order> data) {
        this.items = data;
        notifyDataSetChanged();
    }

    @Override
    public HoldOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hold_order_dialog_item, parent, false);
        return new HoldOrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HoldOrdersViewHolder holder, int position) {
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
        holder.tvDueAmount.setText(decimalFormat.format(order.getForPayAmmount() - order.getTotalPayed()) + " " + mainCurrency.getAbbr());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class HoldOrdersViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.llBackground)
        LinearLayout llBackground;
        @BindView(R.id.tvOrderNumber)
        TextView tvOrderNumber;
        @BindView(R.id.tvCustomer)
        TextView tvCustomer;
        @BindView(R.id.tvDateOpened)
        TextView tvDateOpened;
        @BindView(R.id.tvTotal)
        TextView tvDueAmount;
        @BindView(R.id.btnClose)
        MpActionButton btnClose;
        @BindView(R.id.btnCancel)
        MpActionButton btnCancel;
        @BindView(R.id.btnSelect)
        MpActionButton btnSelect;

        public HoldOrdersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnCancel.setOnClickListener(view -> {
                callback.onCancel(items.get(getAdapterPosition()));
            });
            btnClose.setOnClickListener(view -> {
                callback.onClose(items.get(getAdapterPosition()));
            });
            btnSelect.setOnClickListener(view -> {
                callback.onSelect(items.get(getAdapterPosition()));
            });
        }
    }

    public interface onExtraOptionItemClicked {
        void onCancel(Order order);
        void onClose(Order order);
        void onSelect(Order order);
    }
}
