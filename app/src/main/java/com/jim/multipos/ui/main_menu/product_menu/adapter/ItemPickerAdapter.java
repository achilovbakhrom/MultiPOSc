package com.jim.multipos.ui.main_menu.product_menu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemPickerAdapter extends RecyclerView.Adapter<ItemPickerAdapter.ItemPickerViewHolder> {

    private String[] items;
    private OnItemPicked listener;

    public ItemPickerAdapter(String[] items) {
        this.items = items;
    }

    public void setListener(OnItemPicked listener) {
        this.listener = listener;
    }

    @Override
    public ItemPickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picker_item, parent, false);
        return new ItemPickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemPickerViewHolder holder, int position) {
        holder.tvItem.setText(items[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    class ItemPickerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvItem)
        TextView tvItem;

        public ItemPickerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvItem.setOnClickListener(view -> listener.onPicked(getAdapterPosition()));
        }
    }

    public interface OnItemPicked {
        void onPicked(int position);
    }
}
