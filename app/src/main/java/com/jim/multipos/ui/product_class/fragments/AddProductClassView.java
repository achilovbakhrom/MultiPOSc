package com.jim.multipos.ui.product_class.fragments;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.ProductClass;

import java.util.ArrayList;

/**
 * Created by developer on 29.08.2017.
 */

public interface AddProductClassView extends BaseView {
    void fillView(ProductClass productClass);
    void onAddNew();
    void setParentSpinnerItems(ArrayList<String> parentSpinnerItems);
    void setParentSpinnerPosition(int position);
    void classNameShort();
    void classNameEmpty();
}
