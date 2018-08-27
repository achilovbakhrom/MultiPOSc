package com.jim.multipos.ui.admin_main_page.fragments.product_class.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.ui.admin_main_page.fragments.product_class.model.ProductClass;

import java.util.List;

import butterknife.BindView;

public class ProductClassAdapter extends BaseAdapter<ProductClass, ProductClassAdapter.ViewHolder> {


    public ProductClassAdapter(List<ProductClass> items) {
        super(items);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_product_class_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    class ViewHolder extends BaseViewHolder{

        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDescription)
        TextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
