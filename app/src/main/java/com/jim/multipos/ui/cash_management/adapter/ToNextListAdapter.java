package com.jim.multipos.ui.cash_management.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.till.TillManagementOperation;
import com.jim.multipos.utils.NumberTextWatcher;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 13.01.2018.
 */

public class ToNextListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final DecimalFormat decimalFormat;
    private Context context;
    private final List<TillManagementOperation> items;
    private final Currency currency;
    private boolean isLeft = true;
    private static final int ZERO = 0, HOLD = 1;

    public ToNextListAdapter(Context context, List<TillManagementOperation> operations, Currency currency) {
        this.context = context;
        this.items = operations;
        this.currency = currency;
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
    }

    @Override
    public int getItemViewType(int position) {
        return isLeft ? ZERO : HOLD;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view;
        switch (viewType) {
            case ZERO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_next_zero_in_till, parent, false);
                holder = new ZeroInTillViewHolder(view);
                break;
            case HOLD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_next_item, parent, false);
                holder = new ToNextViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ToNextViewHolder) {
            ToNextViewHolder item = (ToNextViewHolder) holder;
            item.tvAccount.setText(items.get(position).getAccount().getName());
            item.tvCurrency.setText(currency.getAbbr());
            item.etTotalClosed.addTextChangedListener(new NumberTextWatcher(item.etTotalClosed));
        } else if (holder instanceof ZeroInTillViewHolder) {
            ZeroInTillViewHolder item = (ZeroInTillViewHolder) holder;
            item.tvAccount.setText(items.get(position).getAccount().getName());
            item.tvCurrency.setText(currency.getAbbr());
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLeft(boolean left) {
        isLeft = left;
        notifyDataSetChanged();

    }

    class ToNextViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvAccount)
        TextView tvAccount;
        @BindView(R.id.etTotalClosed)
        MpEditText etTotalClosed;
        @BindView(R.id.tvCurrency)
        TextView tvCurrency;
        @BindView(R.id.etDescription)
        MpEditText etDescription;

        ToNextViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            etTotalClosed.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        double amount = 0;
                        try {
                            amount = decimalFormat.parse(etTotalClosed.getText().toString()).doubleValue();
                            items.get(getAdapterPosition()).setAmount(amount);
                        } catch (Exception e) {
                            etTotalClosed.setError(context.getString(R.string.invalid));
                        }
                    } else {
                        etTotalClosed.setError(context.getString(R.string.enter_amount));
                        items.get(getAdapterPosition()).setAmount(null);
                    }
                }
            });

            etDescription.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        items.get(getAdapterPosition()).setDescription(etDescription.getText().toString());
                    }
                }
            });
        }
    }

    class ZeroInTillViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvAccount)
        TextView tvAccount;
        @BindView(R.id.tvCurrency)
        TextView tvCurrency;

        ZeroInTillViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}