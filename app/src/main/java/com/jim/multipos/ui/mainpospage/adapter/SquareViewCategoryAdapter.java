package com.jim.multipos.ui.mainpospage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jim.mpviews.MPListItemView;
import com.jim.mpviews.MpItem;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.products.Category;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 12.10.2017.
 */

    public class SquareViewCategoryAdapter extends ClickableBaseAdapter<Category, SquareViewCategoryAdapter.SquareCategoryViewHolder> {

    public SquareViewCategoryAdapter(List items) {
        super(items);
    }

    @Override
    public void onBindViewHolder(SquareCategoryViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.mpSquareItem.setTextSize(12);
        holder.mpSquareItem.setText(items.get(position).getName());
        if (position == selectedPosition) {
            holder.mpSquareItem.setActivate(true);
            holder.ivNextItem.setVisibility(View.VISIBLE);
        } else {
            holder.mpSquareItem.setActivate(false);
            holder.ivNextItem.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setItems(List<Category> items) {
        super.setItems(items);
    }

    public void setSelected(int position){
        this.selectedPosition = position;
    }

    @Override
    public SquareCategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.square_view_item, viewGroup, false);
        return new SquareCategoryViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected void onItemClicked(SquareCategoryViewHolder holder, int position) {
        notifyDataSetChanged();
    }


    public class SquareCategoryViewHolder extends BaseViewHolder {
        @BindView(R.id.mpSquareItem)
        MPListItemView mpSquareItem;
        @BindView(R.id.ivNextItem)
        ImageView ivNextItem;

        public SquareCategoryViewHolder(View itemView) {
            super(itemView);
        }
    }
}
