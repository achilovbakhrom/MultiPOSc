package com.jim.multipos.ui.consignment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReturnItemsListAdapter extends RecyclerView.Adapter<ReturnItemsListAdapter.ReturnItemViewHolder> {

    private final Context context;
    private final String currencyAbbr;
    private DatabaseManager databaseManager;
    private List<OutcomeProduct> items;
    private DecimalFormat decimalFormat;
    private OnOutComeItemSelectListener listener;

    public ReturnItemsListAdapter(Context context, String currencyAbbr, DatabaseManager databaseManager) {
        this.context = context;
        this.currencyAbbr = currencyAbbr;
        this.databaseManager = databaseManager;
        decimalFormat = BaseAppModule.getFormatterGrouping();
    }

    public void setItems(List<OutcomeProduct> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReturnItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outcome_item, parent, false);
        return new ReturnItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReturnItemViewHolder holder, int position) {
        if (position == 0)
            holder.etProductCost.post(() -> holder.etProductCost.requestFocus());
        holder.tvProductName.setText(items.get(position).getProduct().getName());
        holder.tvCurrencyAbbr.setText(currencyAbbr);
        holder.etProductCount.setText(decimalFormat.format(items.get(position).getSumCountValue()));
        if (items.get(position).getSumCountValue() == null) {
            holder.etProductCount.setText("");
            holder.etProductCount.setHint("0.0");
        }
        if (items.get(position).getSumCostValue() == null) {
            holder.etProductCost.setText(String.valueOf(0.0d));
            holder.tvProductSum.setText(String.valueOf(0.0d));
        } else {
            holder.etProductCost.setText(decimalFormat.format(items.get(position).getSumCostValue()));
            if (items.get(position).getSumCountValue() != null)
                holder.tvProductSum.setText(decimalFormat.format(items.get(position).getSumCostValue() * items.get(position).getSumCountValue()));
            else holder.tvProductSum.setText(decimalFormat.format(0));
        }
        holder.tvProductUnit.setText(items.get(position).getProduct().getMainUnit().getAbbr());
        switch (items.get(position).getProduct().getStockKeepType()) {
            case Product.LIFO:
                holder.tvStockType.setText("LIFO");
                break;
            case Product.FIFO:
                holder.tvStockType.setText("FIFO");
                break;
            case Product.FEFO:
                holder.tvStockType.setText("FEFO");
                break;
        }
        if (items.get(position).getCustomPickSock()) {
            holder.tvStockType.setText("Custom");
        }
        if (items.get(position).getProduct().getMainUnit().getAbbr().equals("pcs"))
            holder.etProductCount.setInputType(InputType.TYPE_CLASS_NUMBER);
        else holder.etProductCount.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if (items.size() > 1) {
            holder.btnDeleteProduct.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        else
            return items.size();
    }

    public void setListener(OnOutComeItemSelectListener listener) {
        this.listener = listener;
    }

    public class ReturnItemViewHolder extends RecyclerView.ViewHolder {

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
        @BindView(R.id.tvStockType)
        TextView tvStockType;
        @BindView(R.id.flEditType)
        FrameLayout flEditType;

        public ReturnItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            flEditType.setOnClickListener(v -> listener.onCustomStock(items.get(getAdapterPosition()), getAdapterPosition()));

            btnDeleteProduct.setOnClickListener(view -> listener.onDelete(getAdapterPosition()));

            etProductCount.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        double count = 0;
                        try {
                            double availableCount = databaseManager.getAvailableCountForProduct(items.get(getAdapterPosition()).getProduct().getId()).blockingGet();
                            count = decimalFormat.parse(etProductCount.getText().toString().replace(",", ".")).doubleValue();
                            for (int j = 0; j < items.size(); j++) {
                                if (items.get(j).getProduct().getId().equals(items.get(getAdapterPosition()).getProduct().getId()) && j != getAdapterPosition())
                                    availableCount -= items.get(j).getSumCountValue();
                            }
                            if (count > availableCount) {
                                items.get(getAdapterPosition()).setSumCountValue(count);
                                etProductCount.setError("Count can/'t be bigger available products count");
                            } else {
                                items.get(getAdapterPosition()).setSumCountValue(count);
                            }

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
                        listener.onSumChanged();
                    } else {
                        tvProductSum.setText(decimalFormat.format(0));
                        items.get(getAdapterPosition()).setSumCountValue(0d);
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
                            items.get(getAdapterPosition()).setSumCostValue(cost);
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
                        listener.onSumChanged();
                    } else {
                        tvProductSum.setText(decimalFormat.format(0));
                        items.get(getAdapterPosition()).setSumCostValue(null);
                        etProductCost.setError(context.getString(R.string.enter_product_cost));
                    }
                }
            });
        }
    }

    public interface OnOutComeItemSelectListener {
        void onSumChanged();

        void onDelete(int position);

        void onCustomStock(OutcomeProduct outcomeProduct, int position);
    }
}
