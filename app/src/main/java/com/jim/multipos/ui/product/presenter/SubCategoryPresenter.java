package com.jim.multipos.ui.product.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.SubCategory;

/**
 * Created by DEV on 18.08.2017.
 */

public interface SubCategoryPresenter extends Presenter{
    void save(String name, String description, boolean checkboxChecked, String photoPath);
    void back();
    void checkData();
    void onDestroy();
    void clickedSubCategory(SubCategory subCategory);
    void setParentCategory(Category category);
    void isVisible(boolean visible);
}
