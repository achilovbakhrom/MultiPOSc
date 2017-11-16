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

import static com.jim.multipos.ui.service_fee_new.Constants.TYPE_PERCENT;
import static com.jim.multipos.ui.service_fee_new.Constants.TYPE_REPRICE;
import static com.jim.multipos.ui.service_fee_new.Constants.TYPE_VALUE;

/**
 * Created by Portable-Acer on 11.11.2017.
 */

public class ServiceFeeAdapter extends BaseAdapter<ServiceFee, ServiceFeeAdapter.ServiceFeeViewHolder> {
    public interface OnClickListener {
        void onItemClicked(ServiceFee serviceFee);
    }

    private ServiceFeeAdapter.OnClickListener listener;
    private Context context;
    private DecimalFormat decimalFormat;

    public ServiceFeeAdapter(Context context, ServiceFeeAdapter.OnClickListener listener, List<ServiceFee> items) {
        super(items);
        this.context = context;
        this.listener = listener;
        this.decimalFormat = getFormatter();
    }

    @Override
    public ServiceFeeAdapter.ServiceFeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ServiceFeeAdapter.ServiceFeeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.service_fee_dialog_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ServiceFeeAdapter.ServiceFeeViewHolder holder, int position) {
        if (getItem(position).getReason().isEmpty()) {
            holder.tvDescription.setText(getAmountTypeFromConst(getItem(position).getType()));
        } else {
            holder.tvDescription.setText(getItem(position).getReason() + " - " + getAmountTypeFromConst(getItem(position).getType()));
        }

        holder.tvDiscount.setText(decimalFormat.format(getItem(position).getAmount()));
        holder.tvCreatedDate.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(getItem(position).getCreatedDate()));

        if (getItem(position).getType().equals(TYPE_PERCENT)) {
            holder.tvDiscount.append(" %");
        } else {
            holder.tvDiscount.append(" UZS");
        }
    }

    private String getAmountTypeFromConst(String amountType) {
        if (amountType.equals(TYPE_VALUE)) {
            return context.getString(R.string.amount);
        } else if (amountType.equals(TYPE_PERCENT)) {
            return context.getString(R.string.percent);
        } else if (amountType.equals(TYPE_REPRICE)) {
            return context.getString(R.string.reprice);
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
