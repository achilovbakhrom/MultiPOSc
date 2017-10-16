package com.jim.multipos.ui.product.presenter;


import android.os.Bundle;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.operations.CategoryOperations;
import com.jim.multipos.ui.product.view.SubCategoryView;

import javax.inject.Inject;

/**
 * Created by DEV on 18.08.2017.
 */
@PerFragment
public class SubCategoryPresenterImpl extends BasePresenterImpl<SubCategoryView> implements SubCategoryPresenter {
    private Category parent;
    private Category subCategory;
    private CategoryOperations subCategoryOperations;
    private static final String ADD = "added";
    private static final String UPDATE = "update";

    @Inject
    SubCategoryPresenterImpl(SubCategoryView view, DatabaseManager databaseManager) {
        super(view);
        subCategoryOperations = databaseManager.getCategoryOperations();
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        checkData();
    }

    @Override
    public void save(String name, String description, boolean checkboxChecked) {
        if (name.isEmpty()) {
            view.setError("Please, enter the name");
        } else if (name.length() < 3) {
            view.setError("Subcategory name should be longer than 3 letters");
        } else if (this.subCategory == null) {
            Category subCategory = new Category();
            subCategory.setName(name);
            subCategory.setDescription(description);
            subCategory.setParentId(parent.getId());
            subCategory.setIsActive(checkboxChecked);
            view.clearFields();
            subCategoryOperations.addCategory(subCategory).subscribe(aLong -> view.sendEvent(subCategory, ADD));
        } else {
            subCategory.setName(name);
            subCategory.setDescription(description);
            subCategory.setParentId(parent.getId());
            subCategory.setIsActive(checkboxChecked);
            subCategoryOperations.replaceCategory(subCategory).subscribe(aLong -> {
                view.sendEvent(subCategory, UPDATE);
                subCategory = null;
            });
        }
    }

    @Override
    public void checkData() {
        if (this.subCategory != null) {
            view.setFields(this.subCategory.getName(),
                    this.subCategory.getDescription(),
                    this.subCategory.getIsActive());
        } else view.clearFields();
    }

    @Override
    public void setParentCategory(Category category) {
        this.parent = category;
        view.setParentCategoryName(category.getName());
    }

    @Override
    public void clickedSubCategory(Category subCategory) {
        this.subCategory = subCategory;
        if (subCategory == null) {
            view.clearFields();
        } else checkData();
    }
}
