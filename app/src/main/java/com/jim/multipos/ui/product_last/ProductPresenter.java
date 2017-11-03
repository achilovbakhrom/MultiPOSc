package com.jim.multipos.ui.product_last;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.product_last.helpers.AddingMode;
import com.jim.multipos.ui.product_last.helpers.FragmentType;

import java.util.List;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public interface ProductPresenter extends Presenter {

    void categorySelected(Category category);
    void subcategorySelected(Category category);
    void addCategory(String name, String description, boolean isActive);
    void addProduct(Product product);
    List<Category> getSubcategories(Category category);
    boolean isSubcategoryNameUnique(String categoryName, String subcategoryName);
    boolean isCategoryNameUnique(String categoryName);
    AddingMode getMode();
    void setMode(AddingMode mode);
    FragmentType getType();
    void setType(FragmentType type);
    Category getCategory();
    void setCategory(Category category);
    Category getCategoryById(Long id);
    void deleteCategory();
    void setCategoryItemsMoved();
    void setSubcategoryItemsMoved();
}
