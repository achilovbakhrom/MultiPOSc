package com.jim.multipos.core;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Sirojiddin on 12.10.2017.
 */

public abstract class BaseAdapter<T, E extends BaseViewHolder> extends RecyclerView.Adapter<E> {

    protected List<T> items;

    public BaseAdapter(List<T> items) {
        this.items = items;
    }

    public T getItem(int position){
        return items.get(position);
    }

    public void addItem(T item){
        items.add(item);
        notifyDataSetChanged();
    }

    public void addItems(List<T> items){
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        items.remove(position);
        notifyDataSetChanged();
    }

    public void removeItem(T item) {
        items.remove(item);
        notifyDataSetChanged();
    }

    public void removeAllItems() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
