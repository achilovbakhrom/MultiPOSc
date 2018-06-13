package com.jim.multipos.ui.cash_management.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.PaymentType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class PaymentTypesAdapter extends RecyclerView.Adapter<PaymentTypesAdapter.VendorItemViewHolder> {

    private List<PaymentType> items;
    private OnPaymentTypeSelectCallback onPaymentTypeSelectCallback;

    public void setData(List<PaymentType> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public VendorItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_type_dialog_item, parent, false);
        return new VendorItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VendorItemViewHolder holder, int position) {
        holder.tvPaymentTypeName.setText(items.get(position).getName());
    }

    public void setListener(OnPaymentTypeSelectCallback listener) {
        this.onPaymentTypeSelectCallback = listener;
    }

    public interface OnPaymentTypeSelectCallback {
        void onSelect(PaymentType paymentType);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VendorItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvPaymentTypeName)
        TextView tvPaymentTypeName;
        @BindView(R.id.llListContainer)
        LinearLayout llListContainer;

        public VendorItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            llListContainer.setOnClickListener(view -> onPaymentTypeSelectCallback.onSelect(items.get(getAdapterPosition())));
        }
    }
}
