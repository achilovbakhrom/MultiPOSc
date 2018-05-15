package com.jim.multipos.ui.start_configuration.payment_type.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.PaymentType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentTypeAdapter extends RecyclerView.Adapter<PaymentTypeAdapter.PaymentTypeViewHolder> {

    private final Context context;
    private final List<PaymentType> items;
    private OnSaveClicked callback;

    public PaymentTypeAdapter(Context context, List<PaymentType> items, OnSaveClicked callback) {
        this.context = context;
        this.items = items;
        this.callback = callback;
    }

    @Override
    public PaymentTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_type_configure_item, parent, false);
        return new PaymentTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PaymentTypeViewHolder holder, int position) {
        holder.tvAccount.setText(items.get(position).getAccount().getName());
        holder.chbActive.setEnabled(false);
        if (items.get(position).getTypeStaticPaymentType() == PaymentType.CASH_PAYMENT_TYPE) {
            holder.ivDelete.setEnabled(false);
            holder.ivDelete.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorGreyDark));
            holder.chbActive.setCheckboxColor(R.color.colorGreyDark);
        }
        holder.tvPaymentTypeName.setText(items.get(position).getName());
        holder.chbActive.setChecked(items.get(position).getIsActive());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class PaymentTypeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvPaymentTypeName)
        TextView tvPaymentTypeName;
        @BindView(R.id.chbActive)
        MpCheckbox chbActive;
        @BindView(R.id.ivDelete)
        MpMiniActionButton ivDelete;
        @BindView(R.id.tvAccount)
        TextView tvAccount;

        public PaymentTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivDelete.setOnClickListener(view -> {
                callback.onDelete(items.get(getAdapterPosition()), getAdapterPosition());
            });
        }
    }

    public interface OnSaveClicked {
        void onDelete(PaymentType paymentType, int position);
    }
}
