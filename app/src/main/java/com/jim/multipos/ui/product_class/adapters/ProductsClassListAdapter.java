package com.jim.multipos.ui.product_class.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpItem;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.intosystem.NameId;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DEV on 17.08.2017.
 */

public class ProductsClassListAdapter extends ClickableBaseAdapter<NameId, BaseViewHolder> {
    private static final int ADD_ITEM = 0;
    private static final int DYNAMIC_ITEMS = 1;

    public ProductsClassListAdapter(List<NameId> items) {
        super(items);
        if (!this.items.isEmpty()) {
            for (int i = items.size() - 1; i >= 0; i--) {
                if (items.get(i) == null)
                    items.remove(i);
            }
            items.add(0, null);
        } else this.items.add(null);
    }



    public void setToDefault() {
        selectedPosition = 0;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ADD_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_first_item, parent, false);
            return new ProductsClassListAdapter.FirstItemViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
            return new ProductsClassListAdapter.ProductListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder,position);
        if (holder instanceof ProductListViewHolder) {
            ProductListViewHolder productViewHolder = (ProductListViewHolder) holder;
            productViewHolder.mpItem.setText(items.get(position).getName());
            if (position == selectedPosition) {
                productViewHolder.mpItem.setBackgroundResource(R.drawable.item_pressed_bg);
            } else {
                productViewHolder.mpItem.setBackgroundResource(R.drawable.item_bg);
            }
            if (items.get(position).isActive())
                productViewHolder.mainView.setAlpha(1f);
            else
                productViewHolder.mainView.setAlpha(0.5f);


        } else if (holder instanceof FirstItemViewHolder) {
            FirstItemViewHolder itemViewHolder = (FirstItemViewHolder) holder;
            if (position == selectedPosition) {
                itemViewHolder.tvFirstItem.setTextColor(Color.parseColor("#419fd9"));
                itemViewHolder.ivItemBg.setImageTintList(ColorStateList.valueOf(Color.parseColor("#419fd9")));
            } else {
                itemViewHolder.tvFirstItem.setTextColor(Color.parseColor("#cccccc"));
                itemViewHolder.ivItemBg.setImageTintList(ColorStateList.valueOf(Color.parseColor("#cccccc")));
            }
            itemViewHolder.tvFirstItem.setText("+Add\n Class");

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) == null) {
            return ADD_ITEM;
        } else return DYNAMIC_ITEMS;
    }

    @Override
    protected void onItemClicked(BaseViewHolder holder, int position) {
        if (selectedPosition != position) {
            int prevPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(prevPosition);
            notifyItemChanged(selectedPosition);
        }
    }


    public void setPosition(int position) {
        this.selectedPosition = position;
    }


    public class ProductListViewHolder extends BaseViewHolder {
        @BindView(R.id.mpListItem)
        MpItem mpItem;
        View mainView;

        public ProductListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mainView = itemView;

        }

    }

    public class FirstItemViewHolder extends BaseViewHolder {

        @BindView(R.id.rlFirstItemBg)
        RelativeLayout rlFirstItem;
        @BindView(R.id.tvFirstItem)
        TextView tvFirstItem;
        @BindView(R.id.ivItemBg)
        ImageView ivItemBg;

        public FirstItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}
