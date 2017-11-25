package com.jim.multipos.ui.consignment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.TextView;

import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class IncomeItemsListAdapter extends RecyclerView.Adapter<IncomeItemsListAdapter.IncomeItemViewHolder> {

    private List<ConsignmentProduct> items;
    private Context context;
    private OnConsignmentCallback onConsignmentCallback;
    private DecimalFormat decimalFormat;

    public IncomeItemsListAdapter(Context context, DecimalFormat decimalFormat) {
        this.context = context;
        this.decimalFormat = decimalFormat;
    }

    public void setData(List<ConsignmentProduct> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public IncomeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_item, parent, false);
        return new IncomeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IncomeItemViewHolder holder, int position) {
        holder.tvProductName.setText(items.get(position).getProduct().getName());
        holder.tvCurrencyAbbr.setText(items.get(position).getProduct().getCostCurrency().getAbbr());
        holder.etProductCount.setText(String.valueOf(items.get(position).getCountValue()));
        if (items.get(position).getCostValue() == null) {
            holder.etProductCost.setText(String.valueOf(0.0d));
            holder.tvProductSum.setText(String.valueOf(0.0d));
        } else {
            holder.etProductCost.setText(String.valueOf(items.get(position).getCostValue()));
            holder.tvProductSum.setText(String.valueOf(items.get(position).getCostValue() * items.get(position).getCountValue()));
        }
        holder.tvProductUnit.setText(items.get(position).getProduct().getMainUnit().getAbbr());

    }

    public void setListeners(OnConsignmentCallback listeners) {
        this.onConsignmentCallback = listeners;
    }

    public interface OnConsignmentCallback {
        void onDelete(ConsignmentProduct consignmentProduct);
        void onSumChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class IncomeItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etProductCost)
        MpEditText etProductCost;
        @BindView(R.id.etProductCount)
        MpEditText etProductCount;
        @BindView(R.id.tvProductSum)
        TextView tvProductSum;
        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvCurrencyAbbr)
        TextView tvCurrencyAbbr;
        @BindView(R.id.tvProductUnit)
        TextView tvProductUnit;
        @BindView(R.id.btnDeleteProduct)
        MpMiniActionButton btnDeleteProduct;

        public IncomeItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnDeleteProduct.setOnClickListener(view -> onConsignmentCallback.onDelete(items.get(getAdapterPosition())));

            etProductCount.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        double count = 0;
                        try {
                            count = Double.parseDouble(etProductCount.getText().toString());
                            items.get(getAdapterPosition()).setCountValue(count);
                        } catch (Exception e) {
                            etProductCount.setError(context.getString(R.string.invalid));
                            return;
                        }
                        double cost = 0;
                        try {
                            cost = Double.parseDouble(etProductCost.getText().toString());
                        } catch (Exception e) {
                            return;
                        }
                        tvProductSum.setText(decimalFormat.format(cost * count));
                        onConsignmentCallback.onSumChanged();
                    } else
                        etProductCount.setError(context.getString(R.string.please_enter_product_count));
                }
            });
            etProductCost.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        double cost = 0;
                        try {
                            cost = Double.parseDouble(etProductCost.getText().toString());
                            items.get(getAdapterPosition()).setCostValue(cost);
                        } catch (Exception e) {
                            etProductCost.setError(context.getString(R.string.invalid));
                            return;
                        }
                        double count = 0;
                        try {
                            count = Double.parseDouble(etProductCount.getText().toString());
                        } catch (Exception e) {
                            return;
                        }
                        tvProductSum.setText(decimalFormat.format(cost * count));
                        onConsignmentCallback.onSumChanged();
                    } else
                        etProductCost.setError(context.getString(R.string.enter_product_cost));
                }
            });
        }
    }
}
