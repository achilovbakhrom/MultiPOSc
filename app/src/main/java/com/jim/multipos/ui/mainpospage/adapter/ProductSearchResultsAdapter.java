package com.jim.multipos.ui.mainpospage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.products.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 05.01.2018.
 */

public class ProductSearchResultsAdapter extends RecyclerView.Adapter<ProductSearchResultsAdapter.ProductSearchViewHolder> {

    private List<Product> items;
    private onProductSearchResultClickListener listener;

    public ProductSearchResultsAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public ProductSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_for_return_item, parent, false);
        return new ProductSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductSearchViewHolder holder, int position) {
        holder.tvProductName.setText(items.get(position).getName());
        holder.tvProductBarcode.setText("Barcode: " + items.get(position).getBarcode());
        holder.tvProductSKU.setText("SKU: " + items.get(position).getSku());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<Product> products) {
        this.items = products;
        notifyDataSetChanged();
    }

    public void setListener(onProductSearchResultClickListener listener) {
        this.listener = listener;
    }

    public interface onProductSearchResultClickListener {
        void onClick(Product product);
    }

    class ProductSearchViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvProductBarcode)
        TextView tvProductBarcode;
        @BindView(R.id.tvProductSKU)
        TextView tvProductSKU;

        public ProductSearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> listener.onClick(items.get(getAdapterPosition())));
        }
    }
}
