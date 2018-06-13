package com.jim.multipos.ui.consignment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.products.Product;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class VendorItemsListAdapter extends RecyclerView.Adapter<VendorItemsListAdapter.VendorItemViewHolder> {

    private List<Product> items;
    private Context context;
    private OnProductSelectCallback onProductSelectCallback;

    public VendorItemsListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Product> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public VendorItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_dialog_item, parent, false);
        return new VendorItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VendorItemViewHolder holder, int position) {
        holder.tvProductName.setText(items.get(position).getName());
        holder.tvProductSKU.setText(items.get(position).getSku());
    }

    public void setListener(OnProductSelectCallback listener){
        this.onProductSelectCallback = listener;
    }

    public interface OnProductSelectCallback {
        void onSelect(Product product);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VendorItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvProductSKU)
        TextView tvProductSKU;
        @BindView(R.id.llListContainer)
        LinearLayout llListContainer;

        public VendorItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            llListContainer.setOnClickListener(view -> onProductSelectCallback.onSelect(items.get(getAdapterPosition())));
        }
    }
}
