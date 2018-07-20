package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.operations.CategoryOperations;
import com.jim.multipos.data.operations.ProductOperations;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.mainpospage.view.ProductSquareView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 27.10.2017.
 */

public class ProductSquareViewPresenterImpl extends BasePresenterImpl<ProductSquareView> implements ProductSquareViewPresenter {

    private List<Category> categoryList, subcategoryList;
    private List<Product> productList;
    private Category parentCategory, parentSubCategory;
    private CategoryOperations categoryOperations;
    private ProductOperations productOperations;
    private PreferencesHelper preferencesHelper;
    private static final String SUBCATEGORY_TITLE = "subcategory_title";
    private static final String CATEGORY_TITLE = "category_title";

    @Inject
    protected ProductSquareViewPresenterImpl(ProductSquareView view, DatabaseManager databaseManager, PreferencesHelper preferencesHelper) {
        super(view);
        this.categoryOperations = databaseManager.getCategoryOperations();
        this.productOperations = databaseManager.getProductOperations();
        this.preferencesHelper = preferencesHelper;
        categoryList = new ArrayList<>();
        subcategoryList = new ArrayList<>();
        productList = new ArrayList<>();
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
        categoryOperations.getAllActiveSubCategories(parentCategory).subscribe(subCategories -> {
            this.subcategoryList = subCategories;
            view.setSubCategoryRecyclerView(subcategoryList);
            setProductRecyclerView();
        });
    }

    @Override
    public void setProductRecyclerView() {
        if (parentSubCategory != null)
            productOperations.getAllActiveProducts(parentSubCategory).subscribe(products -> {
                this.productList = products;
                view.setProductRecyclerView(productList);
            });
        else view.setProductRecyclerView(productList);
    }

    @Override
    public void setClickedCategory(Category category) {
        this.parentCategory = category;
        refreshSubCategories();
    }

    @Override
    public void setClickedSubCategory(Category subcategory) {
        this.parentSubCategory = subcategory;
        refreshProducts();
    }


    @Override
    public void setSelectedCategory(int lastPositionCategory) {
        this.parentCategory = categoryList.get(lastPositionCategory);
        setSubCategoryRecyclerView();
    }

    @Override
    public void setSelectedSubCategory(int lastPositionSubCategory) {
        this.parentSubCategory = subcategoryList.get(lastPositionSubCategory);
    }

    @Override
    public void updateTitles() {
        view.sendEvent(parentCategory, CATEGORY_TITLE);
        view.sendEvent(parentSubCategory, SUBCATEGORY_TITLE);
    }

    @Override
    public void refreshCategories() {
        categoryOperations.getAllActiveCategories().subscribe(categories -> {
            categoryList.clear();
            categoryList.addAll(categories);
            view.refreshCategories(categoryList);
        });
    }

    @Override
    public void refreshSubCategories() {
        categoryOperations.getAllActiveSubCategories(parentCategory).subscribe(subCategories -> {
            subcategoryList.clear();
            subcategoryList.addAll(subCategories);
            view.refreshSubCategories(subcategoryList);
        });
        view.setSelectedSubCategory(preferencesHelper.getLastPositionSubCategory(String.valueOf(parentCategory.getId())));
        if (!subcategoryList.isEmpty()) {
            view.sendEvent(subcategoryList.get(preferencesHelper.getLastPositionSubCategory(String.valueOf(parentCategory.getId()))), SUBCATEGORY_TITLE);
            setSelectedSubCategory(preferencesHelper.getLastPositionSubCategory(String.valueOf(parentCategory.getId())));
        } else view.sendEvent(null, SUBCATEGORY_TITLE);
        refreshProducts();
    }

    @Override
    public void refreshProducts() {
        if (!subcategoryList.isEmpty()) {
            productOperations.getAllActiveProducts(parentSubCategory).subscribe(products -> {
                productList.clear();
                productList.addAll(products);
                view.refreshProducts(productList);
            });
        } else {
            productList.clear();
            view.refreshProducts(productList);
        }

    }
}
