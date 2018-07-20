package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.products.Category;

/**
 * Created by Sirojiddin on 27.10.2017.
 */

public interface ProductSquareViewPresenter extends Presenter {
    void setCategoryRecyclerView();

    void setSubCategoryRecyclerView();

    void setProductRecyclerView();

    void setClickedCategory(Category category);

    void setClickedSubCategory(Category subcategory);

    void refreshCategories();

    void refreshSubCategories();

    void refreshProducts();

    void setSelectedCategory(int lastPositionCategory);

    void setSelectedSubCategory(int lastPositionSubCategory);

    void updateTitles();
}
