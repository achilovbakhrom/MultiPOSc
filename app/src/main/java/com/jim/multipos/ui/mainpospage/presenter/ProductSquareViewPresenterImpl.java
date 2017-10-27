package com.jim.multipos.ui.mainpospage.presenter;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.operations.CategoryOperations;
import com.jim.multipos.ui.mainpospage.view.ProductSquareView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sirojiddin on 27.10.2017.
 */

public class ProductSquareViewPresenterImpl extends BasePresenterImpl<ProductSquareView> implements ProductSquareViewPresenter {

    private List<Category> categoryList, subcategoryList;
    private CategoryOperations categoryOperations;

    protected ProductSquareViewPresenterImpl(ProductSquareView view, DatabaseManager databaseManager) {
        super(view);
        this.categoryOperations = databaseManager.getCategoryOperations();
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        categoryList = new ArrayList<>();
        subcategoryList = new ArrayList<>();
    }

    @Override
    public void setCategoryRecyclerView() {
        categoryOperations.getAllActiveCategories().subscribe(categories -> {
            this.categoryList = categories;
            view.setCategoryRecyclerViewItems(categories);
        });
    }

    @Override
    public void setSubCategoryRecyclerView() {

    }

    @Override
    public void getClickedCategory(Category category) {

    }
}
