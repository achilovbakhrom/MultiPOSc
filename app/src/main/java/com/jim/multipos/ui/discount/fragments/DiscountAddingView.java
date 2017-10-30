package com.jim.multipos.ui.discount.fragments;

import com.jim.multipos.core.BaseView;

import java.util.List;

/**
 * Created by developer on 23.10.2017.
 */

public interface DiscountAddingView extends BaseView {
    void refreshList(List<Object> objects);
    void notifyItemChanged(List<Object> objects,int pos);
}
