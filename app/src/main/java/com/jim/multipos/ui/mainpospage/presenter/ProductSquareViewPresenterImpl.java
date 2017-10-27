package com.jim.multipos.ui.mainpospage.presenter;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.operations.CategoryOperations;
import com.jim.multipos.ui.mainpospage.view.ProductSquareView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 27.10.2017.
 */

public class ProductSquareViewPresenterImpl extends BasePresenterImpl<ProductSquareView> implements ProductSquareViewPresenter {

    private List<Category> categoryList, subcategoryList;
    private Category parentCategory, parentSubCategory;
    private CategoryOperations categoryOperations;

    @Inject
    protected ProductSquareViewPresenterImpl(ProductSquareView view, DatabaseManager databaseManager) {
        super(view);
        this.categoryOperations = databaseManager.getCategoryOperations();
        categoryList = new ArrayList<>();
        subcategoryList = new ArrayList<>();
    }

    @Override
    public void setCategoryRecyclerView() {

        for (int i = 0; i < 5; i ++){
            Category category = new Category();
            category.setName("Category " + i);
            categoryList.add(category);
        }
        view.setCategoryRecyclerViewItems(categoryList);
//        categoryOperations.getAllActiveCategories().subscribe(categories -> {
//            this.categoryList = categories;
//            view.setCategoryRecyclerViewItems(categories);
//        });
    }

    @Override
    public void setSubCategoryRecyclerView() {

//        categoryOperations.getAllActiveSubCategories(parentCategory).subscribe(subCategories -> {
//            this.subcategoryList = subCategories;
//            view.setSubCategoryRecyclerView(subCategories);
//        });
    }

    @Override
    public void setClickedCategory(Category category) {
        this.parentCategory = category;
    }

    @Override
    public void setClickedSubCategory(Category subcategory) {
        this.parentSubCategory = subcategory;
    }
}
