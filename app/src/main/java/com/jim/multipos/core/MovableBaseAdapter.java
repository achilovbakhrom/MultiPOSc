package com.jim.multipos.core;

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
        if (moveListener != null){
            moveListener.onItemMove(fromPosition, toPosition);
        }
    }

}
