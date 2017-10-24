package com.jim.multipos.core;

/**
 * Created by bakhrom on 10/19/17.
 */

public interface ItemRemoveListener<T> {
    void onItemRemove(int position, T item);
}
