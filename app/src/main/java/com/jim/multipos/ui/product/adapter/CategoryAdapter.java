package com.jim.multipos.ui.product.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.mpviews.MpItem;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.core.MovableBaseAdapter;
import com.jim.multipos.data.db.model.products.Category;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jim.multipos.utils.CategoryUtils.isSubcategory;

/**
 * Created by Sirojiddin on 12.10.2017.
 */

public class CategoryAdapter extends MovableBaseAdapter<Category, BaseViewHolder> {

    private final int ADD_ITEM = 0;
    private final int DYNAMIC_ITEMS = 1;
    private int mode;

    public CategoryAdapter(List items, int mode) {
        super(items);
        this.mode = mode;
        if (!this.items.isEmpty()) {
            if (this.items.get(0) != null)
                this.items.add(0, null);
        } else this.items.add(null);
    }

    public void notifyDataSetChangedWithFirstItem() {
        if (!this.items.isEmpty()) {
            if (this.items.get(0) != null)
                this.items.add(0, null);
        } else this.items.add(null);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof CategoryFirstViewHolder) {
            CategoryFirstViewHolder firstViewHolder = (CategoryFirstViewHolder) holder;
            switch (mode){
                case 0:
                    firstViewHolder.tvFirstItem.setText("Category");
                    break;
                case 1:
                    firstViewHolder.tvFirstItem.setText("Subcategory");
                    break;
                case 2:
                    break;
            }
        } else if (holder instanceof CategoryViewHolder) {
            CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
            categoryViewHolder.mpItem.setText(items.get(position).getName());
            if (position == selectedPosition) {
                categoryViewHolder.mpItem.setBackgroundResource(R.drawable.item_pressed_bg);
            } else {
                categoryViewHolder.mpItem.setBackgroundResource(R.drawable.item_bg);
            }
            if (items.get(position).getIsActive())
                categoryViewHolder.mpItem.setAlpha(1f);
            else
                categoryViewHolder.mpItem.setAlpha(0.5f);
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == ADD_ITEM) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_first_item, viewGroup, false);
            return new CategoryFirstViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_item, viewGroup, false);
            return new CategoryViewHolder(view);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) == null) {
            return ADD_ITEM;
        } else return DYNAMIC_ITEMS;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        super.onItemMove(fromPosition, toPosition);
        Collections.swap(items, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        if (fromPosition == selectedPosition) {
            selectedPosition = toPosition;
            notifyItemChanged(fromPosition);
            notifyItemChanged(selectedPosition);
        } else if (toPosition == selectedPosition) {
            selectedPosition = fromPosition;
            notifyItemChanged(toPosition);
            notifyItemChanged(selectedPosition);
        } else {
            notifyItemChanged(fromPosition);
            notifyItemChanged(toPosition);
        }
    }

    public void setSelectedPositionWithId(Long id) {
        for (Category category : items) {
            if (category == null) continue;
            if (category.getId().equals(id)) {
                this.selectedPosition = items.indexOf(category);
                notifyItemChanged(selectedPosition);
                break;
            }
        }
    }

    @Override
    protected void onItemClicked(BaseViewHolder holder, int position) {
        int prevPosition = selectedPosition;
        notifyItemChanged(prevPosition);
        notifyItemChanged(position);
    }

    public void setPosition(int position) {
        this.selectedPosition = position;
    }

    public class CategoryViewHolder extends BaseViewHolder {
        @BindView(R.id.mpListItem)
        MpItem mpItem;

        public CategoryViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class CategoryFirstViewHolder extends BaseViewHolder {
        @BindView(R.id.rlFirstItemBg)
        RelativeLayout rlFirstItem;
        @BindView(R.id.tvFirstItem)
        TextView tvFirstItem;
        @BindView(R.id.ivItemBg)
        ImageView ivItemBg;
        @BindView(R.id.ivAddPlus)
        ImageView ivAddPlus;

        public CategoryFirstViewHolder(View itemView) {
            super(itemView);
        }
    }
}
