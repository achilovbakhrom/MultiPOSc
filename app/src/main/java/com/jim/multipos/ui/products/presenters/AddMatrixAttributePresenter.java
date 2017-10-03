package com.jim.multipos.ui.products.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.products.fragments.AddMatrixAttributeView;

/**
 * Created by DEV on 20.09.2017.
 */

public interface AddMatrixAttributePresenter extends BaseFragmentPresenter<AddMatrixAttributeView>{
    void addAttributeType(String s, boolean checkboxChecked);
    void removeAttrType(String attrTypeName);
}
