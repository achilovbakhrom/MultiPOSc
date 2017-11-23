package com.jim.multipos.ui.product_last.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.ProductClass;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class ProductClassListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ProductClass> items;
    private Context context;
    private OnProductClassSelectedCallback selectedCallback;
    private static final int EMPTY_ITEM = 0;
    private static final int CLASS_ITEM = 1;
    private static final int SUB_CLASS_ITEM = 2;

    public ProductClassListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ProductClass> items) {
        this.items = items;
        items.add(0, null);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == EMPTY_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_class_dialog_item, parent, false);
            return new EmptyProductClassItemViewHolder(view);
        } else if (viewType == CLASS_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_class_dialog_item, parent, false);
            return new ProductClassItemViewHolder(view);
        } else if (viewType == SUB_CLASS_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_class_dialog_child_item, parent, false);
            return new SubProductClassItemViewHolder(view);
        } else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EmptyProductClassItemViewHolder) {
            EmptyProductClassItemViewHolder emptyHolder = (EmptyProductClassItemViewHolder) holder;
            emptyHolder.tvClassName.setText(context.getString(R.string.not_classified));
        } else if (holder instanceof ProductClassItemViewHolder) {
            ProductClassItemViewHolder viewHolder = (ProductClassItemViewHolder) holder;
            viewHolder.tvClassName.setText(items.get(position).getName());
        } else if (holder instanceof SubProductClassItemViewHolder) {
            SubProductClassItemViewHolder itemViewHolder = (SubProductClassItemViewHolder) holder;
            itemViewHolder.tvClassName.setText(items.get(position).getName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return EMPTY_ITEM;
        } else if (items.get(position).getParentId() == null) {
            return CLASS_ITEM;
        } else
            return SUB_CLASS_ITEM;

    }

    public void setListeners(OnProductClassSelectedCallback listeners) {
        this.selectedCallback = listeners;
    }

    public interface OnProductClassSelectedCallback {
        void onSelect(ProductClass productClass);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ProductClassItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvClassName)
        TextView tvClassName;

        public ProductClassItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvClassName.setOnClickListener(view -> selectedCallback.onSelect(items.get(getAdapterPosition())));
        }
    }

    class SubProductClassItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvClassName)
        TextView tvClassName;

        public SubProductClassItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvClassName.setOnClickListener(view -> selectedCallback.onSelect(items.get(getAdapterPosition())));
        }
    }

    class EmptyProductClassItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvClassName)
        TextView tvClassName;

        public EmptyProductClassItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvClassName.setOnClickListener(view -> selectedCallback.onSelect(items.get(getAdapterPosition())));
        }
    }
}
