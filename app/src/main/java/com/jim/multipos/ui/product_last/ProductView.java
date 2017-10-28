package com.jim.multipos.ui.product_last;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.product_last.helpers.AddingMode;
import com.jim.multipos.ui.product_last.helpers.FragmentType;

import java.util.List;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public interface ProductView extends BaseView {
    void addProductAddEditFragment();
    void addCategoryAddEditFragment();
    void addCategoryListFragment();
    void setModeToProductAddEditFragment(AddingMode mode);
    void setTypeToCategoryFragment(FragmentType type);
    void openProductAddEditFragment(AddingMode mode, Product product);
    void openCategoryAddEditFragment(AddingMode mode, FragmentType type, Category category);
    void addToCategoryList(Category category);
    void addToSubcategoryList(Category category);
    void clearSubcategoryList();
    void setListToSubcategoryList(List<Category> categories);
    void setListToCategoryList(List<Category> categories);
    void suchCategoryNameExists(String name);
    void suchSubcategoryNameExists(String name);
    void editCategory(Category category);
    void editSubcategory(Category category);
    void showCannotDeleteActiveItemDialog();
    void selectSubcategoryListItem(int position);
}
