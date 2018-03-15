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

public class OpenTillAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<TillManagementOperation> items;
    private Currency currency;
    private boolean isLeft = true;
    private static final int ZERO = 0, HOLD = 1;
    private DecimalFormat decimalFormat;

    public OpenTillAdapter(Context context, List<TillManagementOperation> operations, Currency currency) {
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

    public void setData(List<TillManagementOperation> data){
        this.items = data;
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.open_till_confirm_item, parent, false);
                holder = new ConfirmViewHolder(view);
                break;
            case HOLD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.open_till_modify_item, parent, false);
                holder = new ModifyViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ModifyViewHolder) {
            ModifyViewHolder item = (ModifyViewHolder) holder;
            item.tvAccount.setText(items.get(position).getAccount().getName());
            item.tvCurrency.setText(currency.getAbbr());
            item.etModifyAmount.setText(decimalFormat.format(items.get(position).getAmount()));
            item.etModifyAmount.addTextChangedListener(new NumberTextWatcher(item.etModifyAmount));
        } else if (holder instanceof ConfirmViewHolder) {
            ConfirmViewHolder item = (ConfirmViewHolder) holder;
            item.tvAccount.setText(items.get(position).getAccount().getName());
            item.tvCurrency.setText(currency.getAbbr());
            item.tvConfirmAmount.setText(decimalFormat.format(items.get(position).getAmount()));
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

    class ModifyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvAccount)
        TextView tvAccount;
        @BindView(R.id.etTotalClosed)
        MpEditText etModifyAmount;
        @BindView(R.id.tvCurrency)
        TextView tvCurrency;

        ModifyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            etModifyAmount.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        double amount = 0;
                        try {
                            amount = decimalFormat.parse(etModifyAmount.getText().toString()).doubleValue();
                            items.get(getAdapterPosition()).setAmount(amount);
                        } catch (Exception e) {
                            etModifyAmount.setError(context.getString(R.string.invalid));
                        }
                    } else {
                        etModifyAmount.setError(context.getString(R.string.enter_amount));
                        items.get(getAdapterPosition()).setAmount(null);
                    }
                }
            });

        }
    }

    class ConfirmViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvAccount)
        TextView tvAccount;
        @BindView(R.id.tvCurrency)
        TextView tvCurrency;
        @BindView(R.id.tvConfirmAmount)
        TextView tvConfirmAmount;

        ConfirmViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}