package com.jim.multipos.ui.product.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.SubCategory;

import java.util.List;

/**
 * Created by DEV on 17.08.2017.
 */

public interface ProductListView extends BaseView {
    void setCategoryRecyclerViewItems(List<Category> categories);
    void setSubCategoryRecyclerView(List<SubCategory> subCategories);
    void setProductRecyclerView(List<Product> products);
    void updateCategoryItems();
    void updateProductItems();
    void updateSubCategoryItems();
    void openCategory();
    void openSubCategory();
    void openProduct();
    void setProductName(String name);
    void setCategoryName(String name);
    void setSubCategoryName(String name);
    void setViewsVisibility(int mode);
    void categoryMode();
    void subCategoryMode();
    void productMode();
    void allInvisible();
    void sendSubCategoryEvent(SubCategory subCategory, String parent);
    void sendProductEvent(Product product, String click);
    void sendCategoryEvent(Category category, String event);
}
