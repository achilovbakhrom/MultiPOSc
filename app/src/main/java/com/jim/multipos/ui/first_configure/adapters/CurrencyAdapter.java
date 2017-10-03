package com.jim.multipos.ui.first_configure.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.currency.Currency;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 07.08.17.
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {
    private List<Currency> currencies;
    private OnClick callBack;

    public CurrencyAdapter(List<Currency> currencies, OnClick callBack) {
        this.currencies = currencies;
        this.callBack = callBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String currName = currencies.get(position).getName();
        String currAbbr = currencies.get(position).getAbbr();

        StringBuilder str = new StringBuilder();
        str.append(currName);
        str.append(" (");
        str.append(currAbbr);
        str.append(")");

        holder.currency.setText(str.toString());
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public interface OnClick {
        void onClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.currency)
        TextView currency;
        @BindView(R.id.ivRemove)
        ImageView ivRemove;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            RxView.clicks(ivRemove).subscribe(aVoid -> {
                callBack.onClick(getAdapterPosition());
            });
        }
    }
}
