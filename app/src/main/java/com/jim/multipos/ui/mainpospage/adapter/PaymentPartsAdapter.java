package com.jim.multipos.ui.mainpospage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.order.PayedPartitions;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developer on 13.01.2018.
 */

public class PaymentPartsAdapter extends RecyclerView.Adapter<PaymentPartsAdapter.PaymentsViewHolder> {
    List<PayedPartitions> list;
    private CallbackPaymentList callbackPaymentList;
    private DecimalFormat decimalFormat;

    public PaymentPartsAdapter(List<PayedPartitions> list, CallbackPaymentList callbackPaymentList, DecimalFormat decimalFormat){
        this.list = list;
        this.callbackPaymentList = callbackPaymentList;
        this.decimalFormat = decimalFormat;
    }
    @Override
    public PaymentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_list,parent,false);
        return new PaymentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PaymentsViewHolder holder, int position) {
        holder.tvPaymentType.setText(list.get(position).getPaymentType().getName());
        holder.tvPaymentAmount.setText(decimalFormat.format(list.get(position).getValue()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public interface CallbackPaymentList{
        void onRemovePaymentPart(int position);
    }
    public class PaymentsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvPaymentType)
        TextView tvPaymentType;
        @BindView(R.id.tvPaymentAmount)
        TextView tvPaymentAmount;
        @BindView(R.id.ivRemovePayment)
        ImageView ivRemovePayment;
        public PaymentsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivRemovePayment.setOnClickListener(view -> {
                callbackPaymentList.onRemovePaymentPart(getAdapterPosition());
            });
        }
    }
}
