package com.jim.multipos.ui.mainpospage.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;

import java.util.List;

/**
 * Created by Sirojiddin on 27.10.2017.
 */

public interface ProductSquareView extends BaseView {
    void setCategoryRecyclerViewItems(List<Category> categories);
    void setSubCategoryRecyclerView(List<Category> subCategories);
    void setProductRecyclerView(List<Product> products);
    void refreshCategories(List<Category> categoryList);
    void refreshSubCategories(List<Category> subCategoryList);
    void refreshProducts(List<Product> productList);
    void setSelectedCategory(int position);
    void setSelectedSubCategory(int position);
    void sendEvent(Category category, String subcategoryTitle);
}

