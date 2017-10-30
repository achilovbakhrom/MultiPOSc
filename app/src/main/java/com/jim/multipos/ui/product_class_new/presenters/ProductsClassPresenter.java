package com.jim.multipos.ui.product_class_new.presenters;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.ProductClass;

/**
 * Created by developer on 17.10.2017.
 */

public interface ProductsClassPresenter extends Presenter {
    void onAddPressed(String name, boolean active);
    void onAddSubPressed(String name, boolean active, ProductClass parent);
    void onSave(String name, boolean active, ProductClass productClass);
    void onDelete(ProductClass productClass);
    boolean nameIsUnique(String checkName,ProductClass currentProductClass);

}
