package com.jim.multipos.ui.consignment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private int type;
    private Context context;
    private OnConsignmentCallback onConsignmentCallback;
    private DecimalFormat decimalFormat;
    private String currencyAbbr;

    public IncomeItemsListAdapter(Context context, DecimalFormat decimalFormat, String currencyAbbr) {
        this.context = context;
        this.decimalFormat = decimalFormat;
        this.currencyAbbr = currencyAbbr;
    }

    public void setData(List<ConsignmentProduct> items, int type) {
        this.items = items;
        this.type = type;
        notifyDataSetChanged();
    }

    @Override
    public IncomeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_item, parent, false);
        return new IncomeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IncomeItemViewHolder holder, int position) {
        if (position == 0)
            holder.etProductCount.post(() -> holder.etProductCount.requestFocus());
        holder.tvProductName.setText(items.get(position).getProduct().getName());
        holder.tvCurrencyAbbr.setText(currencyAbbr);
        holder.etProductCount.setText(decimalFormat.format(items.get(position).getCountValue()));
        if (items.get(position).getCountValue() == null) {
            holder.etProductCount.setText("");
            holder.etProductCount.setHint("0.0");
        }
        if (items.get(position).getCostValue() == null) {
            holder.etProductCost.setText(String.valueOf(0.0d));
            holder.tvProductSum.setText(String.valueOf(0.0d));
        } else {
            holder.etProductCost.setText(decimalFormat.format(items.get(position).getCostValue()));
            if (items.get(position).getCountValue() != null)
                holder.tvProductSum.setText(decimalFormat.format(items.get(position).getCostValue() * items.get(position).getCountValue()));
            else holder.tvProductSum.setText(decimalFormat.format(0));
        }
        holder.tvProductUnit.setText(items.get(position).getProduct().getMainUnit().getAbbr());
        if (items.get(position).getProduct().getMainUnit().getAbbr().equals("pcs"))
            holder.etProductCount.setInputType(InputType.TYPE_CLASS_NUMBER);
        else holder.etProductCount.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if (items.size() > 1) {
            holder.btnDeleteProduct.setEnabled(true);
        }
        if (type == 1) {
            if (items.size() > 1) {
                holder.btnDeleteProduct.setAlpha(1f);
                holder.btnDeleteProduct.setEnabled(true);
            } else {
                holder.btnDeleteProduct.setAlpha(0.5f);
                holder.btnDeleteProduct.setEnabled(false);
            }
        }

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
        if (items == null)
            return 0;
        else
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
                            count = decimalFormat.parse(etProductCount.getText().toString().replace(",", ".")).doubleValue();
                            items.get(getAdapterPosition()).setCountValue(count);
                        } catch (Exception e) {
                            etProductCount.setError(context.getString(R.string.invalid));
                            return;
                        }
                        double cost = 0;
                        try {
                            cost = decimalFormat.parse(etProductCost.getText().toString().replace(",", ".")).doubleValue();
                        } catch (Exception e) {
                            return;
                        }
                        tvProductSum.setText(decimalFormat.format(cost * count));
                        onConsignmentCallback.onSumChanged();
                    } else {
                        tvProductSum.setText(decimalFormat.format(0));
                        items.get(getAdapterPosition()).setCountValue(0d);
                        etProductCount.setError(context.getString(R.string.please_enter_product_count));
                    }
                }
            });
            etProductCost.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        double cost = 0;
                        try {
                            cost = decimalFormat.parse(etProductCost.getText().toString().replace(",", ".")).doubleValue();
                            items.get(getAdapterPosition()).setCostValue(cost);
                        } catch (Exception e) {
                            etProductCost.setError(context.getString(R.string.invalid));
                            return;
                        }
                        double count = 0;
                        try {
                            count = decimalFormat.parse(etProductCount.getText().toString().replace(",", ".")).doubleValue();
                        } catch (Exception e) {
                            return;
                        }
                        tvProductSum.setText(decimalFormat.format(cost * count));
                        onConsignmentCallback.onSumChanged();
                    } else {
                        tvProductSum.setText(decimalFormat.format(0));
                        items.get(getAdapterPosition()).setCostValue(null);
                        etProductCost.setError(context.getString(R.string.enter_product_cost));
                    }
                }
            });
        }
    }
}
