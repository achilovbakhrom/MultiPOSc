package com.jim.multipos.utils;

public interface OnItemClickListener<T> {
    void onItemClicked(int position);

    void onItemClicked(T item);
}
