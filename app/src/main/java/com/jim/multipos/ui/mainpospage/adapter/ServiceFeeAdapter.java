package com.jim.multipos.ui.mainpospage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.data.db.model.ServiceFee;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Portable-Acer on 11.11.2017.
 */

public class ServiceFeeAdapter extends BaseAdapter<ServiceFee, ServiceFeeAdapter.ServiceFeeViewHolder> {
    public interface OnClickListener {
        void onItemClicked(ServiceFee serviceFee);
    }

    private ServiceFeeAdapter.OnClickListener listener;
    private final String abbr;
    private Context context;
    private DecimalFormat decimalFormat;

    public ServiceFeeAdapter(Context context, ServiceFeeAdapter.OnClickListener listener, List<ServiceFee> items, String abbr) {
        super(items);
        this.context = context;
        this.listener = listener;
        this.abbr = abbr;
        this.decimalFormat = getFormatter();
    }

    @Override
    public ServiceFeeAdapter.ServiceFeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ServiceFeeAdapter.ServiceFeeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.service_fee_dialog_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ServiceFeeAdapter.ServiceFeeViewHolder holder, int position) {
        if (getItem(position).getName().isEmpty()) {
            holder.tvDescription.setText(getAmountTypeFromConst(getItem(position).getType()));
        } else {
            holder.tvDescription.setText(getItem(position).getName() + " - " + getAmountTypeFromConst(getItem(position).getType()));
        }

        holder.tvDiscount.setText(decimalFormat.format(getItem(position).getAmount()));
        holder.tvCreatedDate.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(getItem(position).getCreatedDate()));

        if (getItem(position).getType() == ServiceFee.PERCENT) {
            holder.tvDiscount.append(" %");
        } else {
            holder.tvDiscount.append(" " + abbr);
        }
    }

    private String getAmountTypeFromConst(int amountType) {
        String[] serviceFeeTypes = context.getResources().getStringArray(R.array.service_fee_type);

        if (amountType == ServiceFee.PERCENT) {
            return serviceFeeTypes[0];
        } else if (amountType == ServiceFee.VALUE) {
            return serviceFeeTypes[1];
        }

        return null;
    }

    private DecimalFormat getFormatter() {
        DecimalFormat formatter;
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        formatter = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);

        return formatter;
    }

    class ServiceFeeViewHolder extends BaseViewHolder {
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.tvCreatedDate)
        TextView tvCreatedDate;
        @BindView(R.id.tvDiscount)
        TextView tvDiscount;

        public ServiceFeeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            RxView.clicks(itemView).subscribe(o -> {
                listener.onItemClicked(getItem(getAdapterPosition()));
            });
        }
    }
}
