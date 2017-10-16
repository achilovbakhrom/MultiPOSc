package com.jim.multipos.ui.product_class.presenters;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.ProductClass;

/**
 * Created by developer on 29.08.2017.
 */

public interface AddProductClassPresenter extends Presenter {
     void onDestroyView();
     void onSaveButtonPress(String className,int spinnerPos,boolean active);
     void onCancelButtonPresed();
     void onClickProductClass(ProductClass productClass);
     void addProductClass();
     void deleteProductClass();
}
