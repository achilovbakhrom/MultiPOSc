package com.jim.multipos.ui.mainpospage.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 12.10.2017.
 */

public class SquareViewCategoryAdapter extends RecyclerView.Adapter<SquareViewCategoryAdapter.SquareCategoryViewHolder> {

    private List<Category> items;
    private int selectedPosition = -1;
    private OnCategoryItemClickListener listener;

    public SquareViewCategoryAdapter(List<Category> items) {
        this.items = items;
    }

    public void setItems(List<Category> items) {
        this.items = items;
    }

    public void setSelected(int position) {
        this.selectedPosition = position;
    }

    @NonNull
    @Override
    public SquareCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.square_view_item, viewGroup, false);
        return new SquareCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SquareViewCategoryAdapter.SquareCategoryViewHolder holder, int position) {
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
    public int getItemCount() {
        return items.size();
    }

    public void setListener(OnCategoryItemClickListener listener) {
        this.listener = listener;
    }

    public class SquareCategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mpSquareItem)
        MPListItemView mpSquareItem;
        @BindView(R.id.ivNextItem)
        ImageView ivNextItem;

        public SquareCategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mpSquareItem.setOnClickListener(v -> {
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

    public interface OnCategoryItemClickListener {
        void onItemClick(Category category, int position);
    }
}
