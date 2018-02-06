package com.jim.multipos.ui.mainpospage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.db.model.products.Vendor;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class PaymentDetailsAdapter extends RecyclerView.Adapter<PaymentDetailsAdapter.VendorItemViewHolder> {

    private List<PayedPartitions> items;
    private DecimalFormat decimalFormat;
    private Currency mainCurrency;

    public PaymentDetailsAdapter(List<PayedPartitions> items, DecimalFormat decimalFormat, Currency mainCurrency) {
        this.items = items;
        this.decimalFormat = decimalFormat;
        this.mainCurrency = mainCurrency;
        notifyDataSetChanged();
    }

    @Override
    public VendorItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payed_partitions_dialog_item, parent, false);
        return new VendorItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VendorItemViewHolder holder, int position) {
        holder.tvPaymentName.setText(items.get(position).getPaymentType().getName());
        holder.tvAmount.setText(decimalFormat.format(items.get(position).getValue())+" "+mainCurrency.getAbbr());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class VendorItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvPaymentName)
        TextView tvPaymentName;
        @BindView(R.id.tvAmount)
        TextView tvAmount;

        public VendorItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
