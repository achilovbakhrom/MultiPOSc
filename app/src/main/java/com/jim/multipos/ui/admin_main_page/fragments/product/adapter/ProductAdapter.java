package com.jim.multipos.ui.admin_main_page.fragments.product.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.mpviews.MpRoundedImageView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.ui.admin_main_page.fragments.product.model.Product;
import com.jim.multipos.utils.OnItemClickListener;

import java.util.List;

import butterknife.BindView;

public class ProductAdapter extends BaseAdapter<Product, ProductAdapter.ViewHolder>{

    OnItemClickListener<Product> listener;

    public ProductAdapter(List<Product> items, OnItemClickListener<Product> listener) {
        super(items);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_product_list_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = items.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvBarcode.setText(product.getBarcode());
        holder.tvSku.setText(product.getSku());
        holder.tvPriceUnit1.setText(product.getPriceUnit_1());
        holder.tvPriceUnit2.setText(product.getPriceUnit_2());
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(product));
    }

    class ViewHolder extends BaseViewHolder{

        @BindView(R.id.ivProductImage)
        MpRoundedImageView ivProductImage;
        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvBarcode)
        TextView tvBarcode;
        @BindView(R.id.tvSku)
        TextView tvSku;
        @BindView(R.id.tvPriceUnit1)
        TextView tvPriceUnit1;
        @BindView(R.id.tvPriceUnit2)
        TextView tvPriceUnit2;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
