package com.jim.multipos.ui.product.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.products.Category;

import java.util.List;

/**
 * Created by DEV on 17.08.2017.
 */

public interface ProductListPresenter extends Presenter{
    void setCategoryRecyclerView();
    void setSubCategoryRecyclerView(List<Category> subCategories);
    void setProductRecyclerView();
    void openCategory();
    void openSubCategory();
    void openProduct();
    void productFragmentOpened();
    void categoryFragmentOpened();    void setCategoryItems(int selectedPosition);
    void setSubCategoryItems(int selectedPosition);
    void setProductItems(int selectedPosition);
    void setViewsVisibility(int mode);
    void refreshCategoryList();
    void refreshSubCategoryList();
    void refreshProductList();
    void subCatFragmentOpened();

}
