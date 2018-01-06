package com.jim.multipos.ui.customer_debt.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Debt;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 29.12.2017.
 */

public class CustomerDebtsAdapter extends RecyclerView.Adapter<CustomerDebtsAdapter.CustomerPaymentsListViewHolder> {
    private List<Debt> items;
    private Context context;
    private DecimalFormat decimalFormat;
    private Currency currency;

    public CustomerDebtsAdapter(Context context, DecimalFormat decimalFormat, Currency currency) {
        this.context = context;
        this.decimalFormat = decimalFormat;
        this.currency = currency;
    }

    public void setItems(List<Debt> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public CustomerPaymentsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_payment_item, parent, false);
        return new CustomerPaymentsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerPaymentsListViewHolder holder, int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        holder.tvDate.setText(String.valueOf(items.get(position).getOrderId()));
        holder.tvPaymentType.setText(simpleDateFormat.format(items.get(position).getTakenDate()));
        double feeAmount = items.get(position).getFee() * items.get(position).getDebtAmount() / 100;
        double total = items.get(position).getDebtAmount() + feeAmount;
        holder.tvPayment.setText(decimalFormat.format(total) + " " + currency.getAbbr());
        if (items.get(position).getStatus() == Debt.CLOSED) {
            holder.tvDueSum.setText(context.getString(R.string.closed));
            holder.tvDueSum.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        } else {
            holder.tvDueSum.setText(context.getString(R.string.active));
            holder.tvDueSum.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CustomerPaymentsListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvPaymentType)
        TextView tvPaymentType;
        @BindView(R.id.tvPayment)
        TextView tvPayment;
        @BindView(R.id.tvDueSum)
        TextView tvDueSum;

        public CustomerPaymentsListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
