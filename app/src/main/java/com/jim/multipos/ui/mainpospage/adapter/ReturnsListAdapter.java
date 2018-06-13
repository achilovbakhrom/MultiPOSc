package com.jim.multipos.ui.mainpospage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.products.Return;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 05.01.2018.
 */

public class ReturnsListAdapter extends RecyclerView.Adapter<ReturnsListAdapter.ProductSearchViewHolder> {

    private List<Return> items;
    private DecimalFormat decimalFormat;
    private Context context;


    public ReturnsListAdapter(DecimalFormat decimalFormat, Context context) {
        this.decimalFormat = decimalFormat;
        this.context = context;
        items = new ArrayList<>();
    }

    @Override
    public ProductSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.return_list_item, parent, false);
        return new ProductSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductSearchViewHolder holder, int position) {
        holder.tvProductName.setText(items.get(position).getProduct().getName());
        holder.tvProductPrice.setText(decimalFormat.format(items.get(position).getProduct().getPrice()));
        holder.tvReturnAmount.setText(decimalFormat.format(items.get(position).getReturnAmount()));
        holder.tvQuantity.setText(decimalFormat.format(items.get(position).getQuantity()));
        holder.tvUnit.setText(items.get(position).getProduct().getMainUnit().getAbbr());
        holder.tvVendorName.setText(items.get(position).getVendor().getName());
        holder.tvTotalReturn.setText(decimalFormat.format(items.get(position).getReturnAmount() * items.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<Return> returns) {
        this.items = returns;
        notifyDataSetChanged();
    }

    class ProductSearchViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvProductPrice)
        TextView tvProductPrice;
        @BindView(R.id.tvReturnAmount)
        TextView tvReturnAmount;
        @BindView(R.id.tvQuantity)
        TextView tvQuantity;
        @BindView(R.id.tvUnit)
        TextView tvUnit;
        @BindView(R.id.tvVendorName)
        TextView tvVendorName;
        @BindView(R.id.tvTotalReturn)
        TextView tvTotalReturn;

        public ProductSearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
