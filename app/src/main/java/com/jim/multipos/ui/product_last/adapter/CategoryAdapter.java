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

import java.util.List;

import butterknife.BindView;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public class CategoryAdapter extends MovableBaseAdapter<Category, BaseViewHolder> {

    private static final int ADD = 0, ITEM = 1;

    public CategoryAdapter(List<Category> items) {
        super(items);
        selectedPosition = 0;
    }

    @Override
    protected void onItemClicked(BaseViewHolder holder, int position) {
        notifyDataSetChanged();
        //TODO while nothing

    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = null;
        View view;
        switch (viewType) {
            case ADD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_add_item, parent, false);
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
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof AddViewHolder) {
            AddViewHolder item = ((AddViewHolder) holder);
            item.itemView.setActivate(position == selectedPosition);
        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder item = ((ItemViewHolder) holder);
            item.itemView.setActivate(position == selectedPosition);
            item.itemView.setText(items.get(position).getName());
        }
    }

    public void editItem(Category category) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) != null && category.getId().equals(items.get(i).getId())) {
                items.set(i, category);
                notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ADD : ITEM;
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
