package com.jim.multipos.ui.product_last.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.intosystem.ProductCost;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class ProductCostListAdapter extends RecyclerView.Adapter<ProductCostListAdapter.ProductCostViewHolder> {

    private List<String> vendors;
    private List<VendorProductCon> costs;
    private Context context;

    public ProductCostListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> vendors, List<VendorProductCon> costs) {
        this.vendors = vendors;
        this.costs = costs;
        notifyDataSetChanged();
    }

    @Override
    public ProductCostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_product_cost_item, parent, false);
        return new ProductCostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductCostViewHolder holder, int position) {
        holder.tvProductName.setText(vendors.get(position));
        if (costs.get(position).getCost() != null)
            holder.etProductCost.setText(String.valueOf(costs.get(position).getCost()));
    }

    public List<VendorProductCon> getCosts() {
        return costs;
    }

    @Override
    public int getItemCount() {
        return vendors.size();
    }

    class ProductCostViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.etProductCost)
        MpEditText etProductCost;

        public ProductCostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            etProductCost.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() != 0) {
                        double cost = 0;
                        try {
                            cost = Double.parseDouble(etProductCost.getText().toString());
                            costs.get(getAdapterPosition()).setCost(cost);
                        } catch (Exception e) {
//                            etProductCost.setError(context.getString(R.string.invalid));
                            return;
                        }
                    } else
                        etProductCost.setError(context.getString(R.string.enter_product_cost));
                }
            });
        }
    }
}
