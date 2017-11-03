package com.jim.multipos.core;

import com.jim.multipos.data.db.model.products.Category;

import java.util.List;

import lombok.Setter;

/**
 * Created by Sirojiddin on 12.10.2017.
 */

public abstract class ClickableBaseAdapter<T, E extends BaseViewHolder> extends BaseAdapter<T, E> {
    @Setter
    private OnItemClickListener<T> onItemClickListener;
    protected int selectedPosition = -1;

    public ClickableBaseAdapter(List<T> items) {
        super(items);
    }

    @Override
    public void onBindViewHolder(E holder, int position) {
        holder.view.setOnClickListener(view -> {
            if(selectedPosition!=position || isSinglePositionClickDisabled())
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClicked(items.get(position));
                    onItemClickListener.onItemClicked(position);
                    onItemClicked(holder, position);
                    selectedPosition = position;
            }
        });
    }

    protected boolean isSinglePositionClickDisabled() {
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public interface OnItemClickListener<T> {
        void onItemClicked(int position);
        void onItemClicked(T item);
    }

    protected abstract void onItemClicked(E holder, int position);

    @Override
    public void addItems(List<T> items) {
        super.addItems(items);
    }

}
