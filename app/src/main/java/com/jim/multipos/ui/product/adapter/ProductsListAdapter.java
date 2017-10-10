package com.jim.multipos.ui.product.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpItem;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.intosystem.NamePhotoPathId;
import com.jim.multipos.ui.product.presenter.ProductListPresenter;
import com.jim.multipos.utils.item_touch_helper.ItemTouchHelperAdapter;
import com.jim.multipos.utils.item_touch_helper.OnStartDragListener;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DEV on 17.08.2017.
 */

public class ProductsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {
    private List<NamePhotoPathId> list;
    private int listType = -1;
    private int selectedPosition = -1;
    private ProductListPresenter presenter;
    public static final int ADD_ITEM = 0;
    public static final int DYNAMIC_ITEMS = 1;
    private final OnStartDragListener onStartDragListener;

    public ProductsListAdapter(List<? extends NamePhotoPathId> list, ProductListPresenter presenter, int listType, OnStartDragListener onStartDragListener) {
        this.list = (List<NamePhotoPathId>) list;
        this.presenter = presenter;
        this.listType = listType;
        this.onStartDragListener = onStartDragListener;
        if (!this.list.isEmpty()) {
            if (this.list.get(0) != null)
                this.list.add(0, null);
        } else this.list.add(null);
    }

    public void notifyDataSetChangedWithZeroButton() {
        if (!this.list.isEmpty()) {
            if (this.list.get(0) != null)
                this.list.add(0, null);
        } else this.list.add(null);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ADD_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_first_item, parent, false);
            return new ProductsListAdapter.FirstItemViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
            return new ProductsListAdapter.ProductListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProductListViewHolder) {
            ProductListViewHolder productViewHolder = (ProductListViewHolder) holder;
            productViewHolder.mpItem.setText(list.get(position).getName());
            if (position == selectedPosition) {
                productViewHolder.mpItem.setBackgroundResource(R.drawable.item_pressed_bg);
                switch (listType) {
                    case 0:
                        presenter.setCategoryItems(selectedPosition);
                        presenter.openCategory();
                        presenter.setViewsVisibility(listType + 1);
                        break;
                    case 1:
                        presenter.setViewsVisibility(listType + 1);
                        break;
                }
            } else {
                productViewHolder.mpItem.setBackgroundResource(R.drawable.item_bg);
            }
            if (list.get(position).isActive())
                productViewHolder.mpItem.setAlpha(1f);
            else
                productViewHolder.mpItem.setAlpha(0.5f);

            RxView.touches(((ProductListViewHolder) holder).mpItem, event -> false).subscribe((MotionEvent event) -> {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDragListener.onStartDrag(holder);
                }
            });

        } else if (holder instanceof FirstItemViewHolder) {
            FirstItemViewHolder itemViewHolder = (FirstItemViewHolder) holder;
            if (position == selectedPosition) {
                itemViewHolder.tvFirstItem.setTextColor(Color.parseColor("#419fd9"));
                itemViewHolder.ivItemBg.setImageTintList(ColorStateList.valueOf(Color.parseColor("#419fd9")));
            } else {
                itemViewHolder.tvFirstItem.setTextColor(Color.parseColor("#cccccc"));
                itemViewHolder.ivItemBg.setImageTintList(ColorStateList.valueOf(Color.parseColor("#cccccc")));
            }

            switch (listType) {
                case 0:
                    itemViewHolder.tvFirstItem.setText("Add \n Category");
                    break;
                case 1:
                    itemViewHolder.tvFirstItem.setText("Add \n SubCategory");
                    break;
                case 2:
                    itemViewHolder.tvFirstItem.setText("Add \n Product");
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == null) {
            return ADD_ITEM;
        } else return DYNAMIC_ITEMS;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setPosition(int position) {
        this.selectedPosition = position;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        if (fromPosition == selectedPosition) {
            selectedPosition = toPosition;
            notifyItemChanged(fromPosition);
            notifyItemChanged(selectedPosition);
        } else if (toPosition == selectedPosition) {
            selectedPosition = fromPosition;
            notifyItemChanged(toPosition);
            notifyItemChanged(selectedPosition);
        }
        switch (listType) {
            case 0:
//                presenter.setCategoryItems(selectedPosition);
                presenter.onListCategoryPositionChanged();
                break;
            case 1:
//                presenter.setSubCategoryItems(selectedPosition);
                presenter.onListSubCategoryPositionChanged();
                break;
            case 2:
//                presenter.setProductItems(selectedPosition);
                presenter.onListProductPositionChanged();
                break;
        }
        return true;
    }

    @Override
    public void onItemMoved() {

    }


    public class ProductListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mpListItem)
        MpItem mpItem;

        public ProductListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            RxView.clicks(mpItem).subscribe(aVoid -> {
                int prevPosition = selectedPosition;
                selectedPosition = getAdapterPosition();
                notifyItemChanged(prevPosition);
                notifyItemChanged(selectedPosition);
                switch (listType) {
                    case 0:
                        presenter.setCategoryItems(selectedPosition);
                        presenter.openCategory();
                        presenter.setViewsVisibility(listType + 1);
                        break;
                    case 1:
                        presenter.setSubCategoryItems(selectedPosition);
                        presenter.openSubCategory();
                        presenter.setViewsVisibility(listType + 1);
                        break;
                    case 2:
                        presenter.setProductItems(selectedPosition);
                        presenter.openProduct();
                        break;
                }
            });
        }

    }

    public class FirstItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rlFirstItemBg)
        RelativeLayout rlFirstItem;
        @BindView(R.id.tvFirstItem)
        TextView tvFirstItem;
        @BindView(R.id.ivItemBg)
        ImageView ivItemBg;

        public FirstItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            RxView.clicks(rlFirstItem).subscribe(aVoid -> {
                int prevPosition = selectedPosition;
                selectedPosition = getAdapterPosition();
                notifyItemChanged(prevPosition);
                notifyItemChanged(selectedPosition);
                switch (listType) {
                    case 0:
                        presenter.openCategory();
                        presenter.setCategoryItems(-1);
                        presenter.setViewsVisibility(listType);
                        break;
                    case 1:
                        presenter.setSubCategoryItems(-1);
                        presenter.openSubCategory();
                        presenter.setViewsVisibility(listType);
                        break;
                    case 2:
                        presenter.setProductItems(-1);
                        presenter.openProduct();
                        presenter.setViewsVisibility(listType);
                        break;
                }
            });
        }


    }
}
