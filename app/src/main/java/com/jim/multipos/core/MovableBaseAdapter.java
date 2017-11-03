package com.jim.multipos.core;

import java.util.Collections;
import java.util.List;

import lombok.Setter;

/**
 * Created by Sirojiddin on 16.10.2017.
 */

public abstract class MovableBaseAdapter<T, E extends BaseViewHolder> extends ClickableBaseAdapter<T, E> implements ItemMoveListener{

    @Setter
    private ItemMoveListener moveListener;

    public MovableBaseAdapter(List<T> items) {
        super(items);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        if (moveListener != null){
            moveListener.onItemMove(fromPosition, toPosition);
        }
    }

}
