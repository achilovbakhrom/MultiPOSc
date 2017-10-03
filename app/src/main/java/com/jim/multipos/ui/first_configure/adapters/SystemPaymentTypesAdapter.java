package com.jim.multipos.ui.first_configure.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.PaymentType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 07.08.17.
 */

public class SystemPaymentTypesAdapter extends RecyclerView.Adapter<SystemPaymentTypesAdapter.ViewHolder> {
    public interface OnClick {
        void removeItem(int position);
    }

    private List<PaymentType> systemPaymentTypes;
    private OnClick onClickCallback;

    public SystemPaymentTypesAdapter(List<PaymentType> systemPaymentTypes) {
        this.systemPaymentTypes = systemPaymentTypes;
    }

    public SystemPaymentTypesAdapter(List<PaymentType> systemPaymentTypes, OnClick onClick) {
        this.systemPaymentTypes = systemPaymentTypes;
        this.onClickCallback = onClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_type_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PaymentType systemPaymentType = systemPaymentTypes.get(position);

        holder.tvPaymentTypeName.setText(systemPaymentType.getName());
        holder.tvAccount.setText(systemPaymentType.getAccount().getName());
        holder.tvCurrency.setText(systemPaymentType.getCurrency().getName());
    }

    @Override
    public int getItemCount() {
        return systemPaymentTypes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvPaymentTypeName)
        TextView tvPaymentTypeName;
        @BindView(R.id.tvAccount)
        TextView tvAccount;
        @BindView(R.id.tvItemText)
        TextView tvCurrency;
        @BindView(R.id.btnRemove)
        ImageView btnRemove;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            RxView.clicks(btnRemove).subscribe(aVoid -> {
                onClickCallback.removeItem(getAdapterPosition());
            });
        }
    }
}
