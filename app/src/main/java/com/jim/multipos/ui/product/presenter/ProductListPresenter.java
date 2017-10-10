package com.jim.multipos.ui.product.presenter;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.core.Presenter;
import com.jim.multipos.ui.product.view.ProductListView;

/**
 * Created by DEV on 17.08.2017.
 */

public interface ProductListPresenter extends Presenter{
    void setCategoryRecyclerView();
    void setSubCategoryRecyclerView();
    void setProductRecyclerView();
    void openCategory();
    void openSubCategory();
    void openProduct();
    void setCategoryItems(int selectedPosition);
    void setSubCategoryItems(int selectedPosition);
    void setProductItems(int selectedPosition);
    void onListCategoryPositionChanged();
    void onListSubCategoryPositionChanged();
    void onListProductPositionChanged();
    void setViewsVisibility(int mode);
    void refreshCategoryList();
    void refreshSubCategoryList();
    void refreshProductList();
    void subCatFragmentOpened();
    void productFragmentOpened();
    void categoryFragmentOpened();
}
