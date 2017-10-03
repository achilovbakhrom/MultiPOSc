package com.jim.multipos.ui.product_class.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.product_class.fragments.AddProductClassView;

/**
 * Created by developer on 29.08.2017.
 */

public interface AddProductClassPresenter extends BaseFragmentPresenter<AddProductClassView> {
     void onDestroyView();
     void onSaveButtonPress(String className,String parent,boolean active);
     void onCancelButtonPresed();

}
