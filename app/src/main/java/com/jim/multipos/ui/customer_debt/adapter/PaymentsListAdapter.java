package com.jim.multipos.ui.customer_debt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.CustomerPayment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 29.12.2017.
 */

public class PaymentsListAdapter extends RecyclerView.Adapter<PaymentsListAdapter.CustomerPaymentsListViewHolder> {
    private List<CustomerPayment> items;
    private String searchText;
    private Context context;
    private DecimalFormat decimalFormat;
    private Currency currency;

    public PaymentsListAdapter(Context context, DecimalFormat decimalFormat, Currency currency) {
        this.context = context;
        this.decimalFormat = decimalFormat;
        this.currency = currency;
    }

    public void setItems(List<CustomerPayment> items) {
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
        holder.tvDate.setText(simpleDateFormat.format(items.get(position).getPaymentDate()));
        holder.tvPaymentType.setText(items.get(position).getPaymentType().getName());
        holder.tvPayment.setText(decimalFormat.format(items.get(position).getPaymentAmount()) + " " + currency.getAbbr());
        holder.tvDueSum.setText(decimalFormat.format(items.get(position).getDebtDue()) + " " + currency.getAbbr());
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
