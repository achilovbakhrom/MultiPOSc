package com.jim.multipos.ui.product_last.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jim.mpviews.MPAddItemView;
import com.jim.mpviews.MPListItemView;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.products.Category;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ADD = 0, ITEM = 1;
    private int selectedPosition = 0;
    public static final int CATEGORY_MODE = 0, SUBCATEGORY_MODE = 1;
    private int mode;
    private List<Category> items;
    private OnItemClickListener listener;

    public CategoryAdapter(List<Category> items, int mode, OnItemClickListener onItemClickListener) {
        this.mode = mode;
        this.items = items;
        this.listener = onItemClickListener;
    }

    public List<Category> getItems() {
        return this.items;
    }

    public void setItems(List<Category> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void removeAllItems() {
        this.items.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view;
        switch (viewType) {
            case ADD:
                if (mode == CATEGORY_MODE)
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_add_item, parent, false);
                else
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_add_item, parent, false);
                holder = new AddViewHolder(view);
                break;
            case ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);
                holder = new ItemViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder item = ((ItemViewHolder) holder);
            item.mpListItemView.setActivate(position == selectedPosition);
            item.mpListItemView.setText(items.get(position).getName());
            item.mpListItemView.makeDeleteable(!items.get(position).getActive());
            item.mpListItemView.setTextSize(12);
            if (position == selectedPosition) {
                item.ivNextItem.setVisibility(View.VISIBLE);
            } else item.ivNextItem.setVisibility(View.INVISIBLE);
        } else if (holder instanceof AddViewHolder) {
            AddViewHolder item = (AddViewHolder) holder;
            item.itemView.setTextSize(12);
            item.itemView.setActivate(position == selectedPosition);
        }
    }

    public void editItem(Category category) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) != null && category.getId().equals(items.get(i).getId())) {
                items.set(i, category);
                sort();
                break;
            }
        }
    }

    public Category getSelectedItem() {
        return items.get(selectedPosition);
    }

    public void setSelectedPositionWithId(Long id) {
        for (Category category : items) {
            if (category == null) continue;
            if (category.getId().equals(id)) {
                this.selectedPosition = items.indexOf(category);
                notifyDataSetChanged();
                break;
            }
        }
    }

    private void sort() {
        items.remove(0);
        Collections.sort(items, (o1, o2) -> {
            if (o1 != null && o2 != null)
                return -((Boolean) o1.getActive()).compareTo(o2.getActive());
            return -1;
        });
        items.add(0, null);
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public void addItems(List<Category> items) {
        this.items.addAll(items);
        sort();
    }

    public void addItem(Category item) {
        this.items.add(item);
        sort();
    }

    public void removeItem(int position) {
        items.remove(position);
        sort();
    }

    public void removeItem(Category item) {
        items.remove(item);
        sort();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ADD : ITEM;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void unselect() {
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    class AddViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.aivAddItem)
        MPListItemView itemView;

        AddViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView.setOnClickListener(v -> {
                if (selectedPosition != getAdapterPosition()) {
                    int prevPosition = selectedPosition;
                    selectedPosition = getAdapterPosition();
                    listener.onItemClick(items.get(getAdapterPosition()), getAdapterPosition());
                    notifyItemChanged(selectedPosition);
                    notifyItemChanged(prevPosition);
                }
            });
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.aivItem)
        MPListItemView mpListItemView;
        @BindView(R.id.ivNextItem)
        ImageView ivNextItem;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mpListItemView.setOnClickListener(v -> {
                if (selectedPosition != getAdapterPosition()) {
                    int prevPosition = selectedPosition;
                    selectedPosition = getAdapterPosition();
                    listener.onItemClick(items.get(getAdapterPosition()), getAdapterPosition());
                    notifyItemChanged(selectedPosition);
                    notifyItemChanged(prevPosition);
                } else {
                    listener.onItemClick(items.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Category category, int position);
    }
}
