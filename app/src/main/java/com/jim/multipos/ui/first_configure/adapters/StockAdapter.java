package com.jim.multipos.ui.first_configure.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.stock.Stock;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 14.08.17.
 */

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {
    private List<Stock> stocks;
    private OnClick onClickCallback;

    public StockAdapter(List<Stock> stocks, OnClick onClickCallback) {
        this.stocks = stocks;
        this.onClickCallback = onClickCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Stock stock = stocks.get(position);

        holder.tvStockName.setText(stock.getName());
        holder.tvAddress.setText(stock.getAddress());
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvStockName)
        TextView tvStockName;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.ivRemove)
        ImageView ivRemove;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            RxView.clicks(ivRemove).subscribe(aVoid -> {
                onClickCallback.remove(getAdapterPosition());
            });
        }
    }

    public interface OnClick {
        void remove(int position);
    }
}
