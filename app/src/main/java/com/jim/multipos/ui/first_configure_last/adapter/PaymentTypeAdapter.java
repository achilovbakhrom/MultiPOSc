package com.jim.multipos.ui.first_configure_last.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.ui.first_configure_last.ItemRemoveListener;

import java.util.List;

import butterknife.BindView;
import lombok.Setter;

/**
 * Created by bakhrom on 10/19/17.
 */

public class PaymentTypeAdapter extends BaseAdapter<PaymentType,PaymentTypeAdapter.ViewHolder> {

    @Setter
    private ItemRemoveListener<PaymentType> listener;

    public PaymentTypeAdapter(List<PaymentType> items) {
        super(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_type_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.paymentTypeName.setText(items.get(position).getName());
        holder.account.setText(items.get(position).getAccount().getName());
        holder.currency.setText(items.get(position).getCurrency().getName());
        holder.remove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemRemove(position, items.get(position));
            }
        });
    }

    class ViewHolder extends BaseViewHolder {

        @BindView(R.id.tvPaymentTypeName)
        TextView paymentTypeName;
        @BindView(R.id.tvAccount)
        TextView account;
        @BindView(R.id.tvItemText)
        TextView currency;
        @BindView(R.id.btnRemove)
        ImageView remove;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
