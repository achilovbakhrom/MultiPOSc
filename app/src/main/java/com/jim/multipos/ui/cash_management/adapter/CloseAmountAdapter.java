package com.jim.multipos.ui.cash_management.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.till.TillManagementOperation;
import com.jim.multipos.utils.NumberTextWatcher;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 13.01.2018.
 */

public class CloseAmountAdapter extends RecyclerView.Adapter<CloseAmountAdapter.CloseAmountViewHolder> {

    private final DecimalFormat decimalFormat;
    private Context context;
    private final List<TillManagementOperation> items;
    private final Currency currency;

    public CloseAmountAdapter(Context context, List<TillManagementOperation> operations, Currency currency) {
        this.context = context;
        this.items = operations;
        this.currency = currency;
        decimalFormat = BaseAppModule.getFormatterGrouping();
    }

    @Override
    public CloseAmountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.close_amout_item, parent, false);
        return new CloseAmountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CloseAmountViewHolder holder, int position) {
        holder.tvAccount.setText(items.get(position).getAccount().getName());
        holder.tvCurrency.setText(currency.getAbbr());
        holder.etTotalClosed.addTextChangedListener(new NumberTextWatcher(holder.etTotalClosed));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CloseAmountViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvAccount)
        TextView tvAccount;
        @BindView(R.id.etTotalClosed)
        MpEditText etTotalClosed;
        @BindView(R.id.tvCurrency)
        TextView tvCurrency;
        @BindView(R.id.etDescription)
        MpEditText etDescription;

        CloseAmountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            etTotalClosed.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        double amount = 0;
                        try {
                            amount = decimalFormat.parse(etTotalClosed.getText().toString().replace(",", ".")).doubleValue();
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
}
