package com.jim.multipos.ui.discount.fragments;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.ui.discount.model.DiscountApaterDetials;

import java.util.List;

/**
 * Created by developer on 23.10.2017.
 */

public interface DiscountAddingView extends BaseView {
    void refreshList(List<DiscountApaterDetials> discountApaterDetials);
    void refreshList();
    void notifyItemChanged(int pos);
    void notifyItemAddRange(int from,int to);
    void notifyItemAdd(int pos);
    void notifyItemRemove(int pos);
    void notifyItemRemoveRange(int from,int to);
    void closeAction();
    void closeDiscountActivity();
    void openWarning();
    void sendEvent(int event, Discount discount);
    void sendChangeEvent(int event, Discount updatedDiscount);
}
