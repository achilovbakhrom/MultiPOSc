package com.jim.multipos.core;

import java.util.List;


/**
 * Created by Sirojiddin on 12.10.2017.
 */

public abstract class ClickableBaseAdapter<T, E extends BaseViewHolder> extends BaseAdapter<T, E> {
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

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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

    @Override
    public void setItems(List<T> items) {
        super.setItems(items);
        if (!items.isEmpty() && items.size() <= selectedPosition) {
            selectedPosition = items.size() - 1;
        } else if (items.isEmpty()) {
            selectedPosition = -1;
        }
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyItemChanged(selectedPosition);
        if (onItemClickListener != null) {
            onItemClickListener.onItemClicked(items.get(position));
            onItemClickListener.onItemClicked(position);
        }
    }
}
