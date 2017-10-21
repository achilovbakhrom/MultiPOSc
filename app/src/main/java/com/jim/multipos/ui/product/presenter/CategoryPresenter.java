package com.jim.multipos.ui.product.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.products.Category;

/**
 * Created by DEV on 10.08.2017.
 */

public interface CategoryPresenter extends Presenter {
    void saveCategory(String name, String description, boolean checked);
    void checkData();
    void clickedCategory(Category category);
    void acceptChanges();
    void notAcceptChanges();
    void checkDeleteOptions();
    void deleteCategory();
}
