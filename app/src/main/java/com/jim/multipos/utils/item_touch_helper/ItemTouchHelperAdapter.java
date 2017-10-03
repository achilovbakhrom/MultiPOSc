package com.jim.multipos.utils.item_touch_helper;

/**
 * Created by DEV on 22.08.2017.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemMoved();
}
