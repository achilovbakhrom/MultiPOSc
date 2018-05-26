package com.jim.multipos.ui.product_last.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.mpviews.MPAddItemView;
import com.jim.mpviews.MPListItemView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.MovableBaseAdapter;
import com.jim.multipos.data.db.model.products.Category;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public class CategoryAdapter extends MovableBaseAdapter<Category, BaseViewHolder> {

    private static final int ADD = 0, ITEM = 1;

    public static final int CATEGORY_MODE = 0, SUBCATEGORY_MODE = 1;
    private int mode;

    public CategoryAdapter(List<Category> items, int mode) {
        super(items);
        this.mode = mode;
    }

    public List<Category> getItems(){
        return this.items;
    }

    @Override
    protected void onItemClicked(BaseViewHolder holder, int position) {
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = null;
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
    protected boolean isSinglePositionClickDisabled() {
        return true;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder item = ((ItemViewHolder) holder);
            item.itemView.setActivate(position == selectedPosition);
            item.itemView.setText(items.get(position).getName());
            item.itemView.makeDeleteable(!items.get(position).isActive());
            item.itemView.setTextSize(12);
        } else if (holder instanceof AddViewHolder){
            AddViewHolder item = (AddViewHolder) holder;
            item.itemView.setTextSize(12);
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
        Collections.sort(items, (o1, o2) -> {
            if (o1 != null && o2 != null)
                return o1.getPosition().compareTo(o2.getPosition());
            return -1;
        });
        Collections.sort(items, (o1, o2) -> {
            if (o1 != null && o2 != null)
                return -((Boolean) o1.isActive()).compareTo(o2.isActive());
            return -1;
        });
        notifyDataSetChanged();
    }
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public void addItems(List<Category> items) {
        super.addItems(items);
        sort();
    }

    @Override
    public void addItem(Category item) {
        super.addItem(item);
        sort();
    }

    @Override
    public void removeItem(int position) {
        super.removeItem(position);
        sort();
    }

    @Override
    public void removeItem(Category item) {
        super.removeItem(item);
        sort();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ADD : ITEM;
    }

    public void unselect() {
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    class AddViewHolder extends BaseViewHolder {

        @BindView(R.id.aivAddItem)
        MPAddItemView itemView;

        AddViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ItemViewHolder extends BaseViewHolder {
        @BindView(R.id.aivItem)
        MPListItemView itemView;
        ItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
