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
import com.jim.multipos.data.db.model.Discount;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Provides;

/**
 * Created by Portable-Acer on 09.11.2017.
 */

public class DiscountAdapter extends BaseAdapter<Discount, DiscountAdapter.DiscountViewHolder> {
    public interface OnClickListener {
        void onItemClicked(Discount discount);
    }

    private String[] amountTypes;
    private OnClickListener listener;
    private Context context;
    private DecimalFormat decimalFormat;

    public DiscountAdapter(Context context, OnClickListener listener, List<Discount> items, String[] amountTypes) {
        super(items);
        this.context = context;
        this.listener = listener;
        this.amountTypes = amountTypes;
        this.decimalFormat = getFormatter();
    }

    @Override
    public DiscountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiscountViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_dialog_item, parent, false));
    }

    @Override
    public void onBindViewHolder(DiscountViewHolder holder, int position) {
        if (getItem(position).getName().isEmpty()) {
            holder.tvDescription.setText(getAmountTypeFromConst(getItem(position).getAmountType()));
        } else {
            holder.tvDescription.setText(getItem(position).getName() + " - " + getAmountTypeFromConst(getItem(position).getAmountType()));
        }

        holder.tvDiscount.setText(decimalFormat.format(getItem(position).getAmount()));
        holder.tvCreatedDate.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(getItem(position).getCreatedDate()));

        if (getItem(position).getAmountType() == Discount.PERCENT) {
            holder.tvDiscount.append(" %");
        } else {
            holder.tvDiscount.append(" "+"UZS");
        }
    }

    private String getAmountTypeFromConst(int amountType) {
        String[] discountUsedTypes = context.getResources().getStringArray(R.array.discount_amount_types);

        if (amountType == Discount.PERCENT) {
            return discountUsedTypes[0];
        } else if (amountType == Discount.VALUE) {
            return discountUsedTypes[1];
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

    class DiscountViewHolder extends BaseViewHolder {
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.tvCreatedDate)
        TextView tvCreatedDate;
        @BindView(R.id.tvDiscount)
        TextView tvDiscount;

        public DiscountViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            RxView.clicks(itemView).subscribe(o -> {
                listener.onItemClicked(getItem(getAdapterPosition()));
            });
        }
    }
}
