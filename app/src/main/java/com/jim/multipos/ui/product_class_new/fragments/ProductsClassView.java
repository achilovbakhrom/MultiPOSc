package com.jim.multipos.ui.product_class_new.fragments;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.ui.product_class_new.model.ProductsClassAdapterDetials;

import java.util.List;

/**
 * Created by developer on 17.10.2017.
 */

public interface ProductsClassView extends BaseView {
    void refreshList(List<ProductsClassAdapterDetials> objects);
    void notifyItemChanged(int pos);
    void notifyItemAddRange(int from,int to);
    void notifyItemAdd(int pos);
    void notifyItemRemove(int pos);
    void notifyItemRemoveRange(int from,int to);
    void closeDiscountActivity();
    void openWarning();
    void closeAction();

}
