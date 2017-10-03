package com.jim.multipos.ui.product_class.fragments;

import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.intosystem.NameIdProdClass;

import java.util.ArrayList;

/**
 * Created by developer on 29.08.2017.
 */

public interface AddProductClassView {
    void fillView(ProductClass productClass);
    void onAddNew();
    void setParentSpinnerItems(ArrayList<NameIdProdClass> productClasss);
    void setParentSpinnerPosition(String parentSpinner);
}
