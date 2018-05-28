package com.jim.multipos.ui.mainpospage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.products.Return;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 05.01.2018.
 */

public class ReturnsAdapter extends RecyclerView.Adapter<ReturnsAdapter.ProductSearchViewHolder> {

    private List<Return> items;
    private DecimalFormat decimalFormat;
    private Context context;
    private onReturnItemCallback callback;


    public ReturnsAdapter(List<Return> returnsList, DecimalFormat decimalFormat, Context context, onReturnItemCallback callback) {
        this.decimalFormat = decimalFormat;
        this.context = context;
        this.callback = callback;
        this.items = returnsList;
    }

    @Override
    public ProductSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.return_item, parent, false);
        return new ProductSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductSearchViewHolder holder, int position) {
        holder.tvProductName.setText(items.get(position).getProduct().getName());
        holder.tvProductPrice.setText(decimalFormat.format(items.get(position).getProduct().getPrice()));
        holder.etReturnPrice.setText(decimalFormat.format(items.get(position).getReturnAmount()));
        holder.etQuantity.setText(decimalFormat.format(items.get(position).getQuantity()));
        holder.tvUnit.setText(items.get(position).getProduct().getMainUnit().getAbbr());
        holder.spVendors.setText(items.get(position).getProduct().getVendor().get(0).getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<Return> returns) {
        this.items = returns;
        notifyDataSetChanged();
    }

    public void addItem(Return item) {
        this.items.add(items.size(), item);
        notifyItemInserted(items.size());
    }

    public interface onReturnItemCallback {
        void onDelete(Return aReturn, int position);
    }

    class ProductSearchViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvProductPrice)
        TextView tvProductPrice;
        @BindView(R.id.etReturnPrice)
        MpEditText etReturnPrice;
        @BindView(R.id.spVendors)
        TextView spVendors;
        @BindView(R.id.etQuantity)
        MpEditText etQuantity;
        @BindView(R.id.ivRemove)
        MpMiniActionButton ivRemove;
        @BindView(R.id.tvUnit)
        TextView tvUnit;

        public ProductSearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            RxView.clicks(ivRemove).subscribe(o -> {
                callback.onDelete(items.get(getAdapterPosition()), getAdapterPosition());
            });

            etReturnPrice.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        double amount;
                        try {
                            amount = decimalFormat.parse(etReturnPrice.getText().toString()).doubleValue();
                            items.get(getAdapterPosition()).setReturnAmount(amount);
                        } catch (Exception e) {
                            etReturnPrice.setError(context.getString(R.string.invalid));
                        }
                    } else {
                        etReturnPrice.setError(context.getString(R.string.enter_return_amount));
                    }
                }
            });

            etQuantity.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        double qty;
                        try {
                            qty = decimalFormat.parse(etQuantity.getText().toString()).doubleValue();
                            items.get(getAdapterPosition()).setQuantity(qty);
                        } catch (Exception e) {
                            etQuantity.setError(context.getString(R.string.invalid));
                        }
                    } else {
                        etQuantity.setError(context.getString(R.string.enter_quantity));
                    }
                }
            });
        }
    }
}
