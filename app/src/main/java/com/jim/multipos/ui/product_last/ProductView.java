package com.jim.multipos.ui.product_last;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.product_last.adapter.CategoryAdapter;
import com.jim.multipos.ui.product_last.helpers.AddingMode;
import com.jim.multipos.ui.product_last.helpers.FragmentType;

import java.util.List;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public interface ProductView extends BaseView {
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
    void showCannotDeleteActiveItemDialog();
    void selectSubcategoryListItem(Long position);
    Long getSubcategorySelectedPosition();
    Category getSubcategoryByPosition(int position);

    //new time
    void selectAddCategoryItem();
    void selectAddSubcategoryItem();
    void selectCategory(Long id);
    void selectSubcategory(Long id);
    Category getSelectedCategory();
    Category getSelectedSubcategory();
    void editCategory(Category category);
    void editSubcategory(Category category);
    void addCategory(Category category);
    void addSubcategory(Category category);
    void deleteCategory(Category category);
    void deleteSubcategory(Category category);
    void setListToCategories(List<Category> categories);
    void setListToSubcategories(List<Category> subcategories);
    void initRightSide(List<Category> categories);

    void openAddCategoryMode();
    void openAddSubcategoryMode(String parentName);
    void openEditCategoryMode(String name, String desctription, boolean isActive);
    void openEditSubcategoryMode(String name, String description, boolean isActive, String parentName);
    List<Category> getCategories();
    List<Category> getSubcategories();
    void setListToProducts(List<Product> products);

    void unselectSubcategoryList();
    void unselectProductsList();
    void clearProductList();
}
