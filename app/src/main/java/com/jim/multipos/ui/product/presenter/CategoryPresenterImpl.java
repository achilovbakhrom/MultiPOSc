package com.jim.multipos.ui.product.presenter;


import android.os.Bundle;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.operations.CategoryOperations;
import com.jim.multipos.ui.product.view.CategoryView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by DEV on 10.08.2017.
 */
@PerFragment
public class CategoryPresenterImpl extends BasePresenterImpl<CategoryView> implements CategoryPresenter {

    private Category category, categoryNew, temp;
    private CategoryOperations categoryOperations;
    private static final String ADD = "added";
    private static final String UPDATE = "update";
    private int updateMode;

    @Inject
    CategoryPresenterImpl(CategoryView view, DatabaseManager databaseManager) {
        super(view);
        categoryOperations = databaseManager.getCategoryOperations();
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        checkData();
    }

    @Override
    public void saveCategory(String name, String description, boolean checked) {
        if (this.category == null) {
            category = new Category();
            category.setName(name);
            category.setCreatedDate(System.currentTimeMillis());
            category.setRootId(null);
            category.setDescription(description);
            category.setIsActive(checked);
            view.clearFields();
            categoryOperations.isCategoryNameExists(category.getName()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
                if (aBoolean) {
                    categoryOperations.addCategory(category).subscribe(aLong ->
                            view.sendEvent(category, ADD));
                    category = null;
                } else view.setError("Such name already exits");
            });
        } else {
            temp = new Category();
            temp.setId(category.getId());
            temp.setName(category.getName());
            temp.setIsActive(category.getIsActive());
            temp.setDescription(category.getDescription());
            temp.setPosition(category.getPosition());
            categoryOperations.getCategoryByName(temp).subscribeOn(AndroidSchedulers.mainThread()).subscribe(integer -> {
                if (integer < 2) {
                    updateMode = integer;
                    view.confirmChanges();
                } else {
                    category = null;
                    view.setError("Such name already exits");
                }
            });
        }
    }

    @Override
    public void checkData() {
        if (this.category != null) {
            view.setFields(category.getName(), category.getDescription(), category.getIsActive());
        } else {
            view.clearFields();
        }
    }

    @Override
    public void clickedCategory(Category category) {
        this.category = category;
        if (category == null) {
            view.clearFields();
        } else checkData();
    }

    @Override
    public void acceptChanges() {

        switch (updateMode) {
            case 0:
                categoryNew = new Category();
                categoryNew.setName(temp.getName());
                categoryNew.setDescription(temp.getDescription());
                categoryNew.setIsActive(temp.getIsActive());
                categoryNew.setCreatedDate(System.currentTimeMillis());
                categoryNew.setPosition(temp.getPosition());
                if (category.getRootId() == null) {
                    categoryNew.setRootId(category.getId());
                } else categoryNew.setRootId(category.getRootId());
                category.setIsDeleted(true);
                category.setIsNotModified(false);
                categoryOperations.replaceCategory(categoryNew).subscribe(aLong -> {
                    view.sendEvent(categoryNew, UPDATE);
                });
                categoryOperations.replaceCategory(category).subscribe(aLong -> category = null);
                break;
            case 1:
                category.setName(temp.getName());
                category.setCreatedDate(System.currentTimeMillis());
                category.setRootId(null);
                category.setDescription(temp.getDescription());
                category.setIsActive(temp.getIsActive());
                category.setPosition(temp.getPosition());
                category.setIsNotModified(false);
                categoryOperations.replaceCategory(category).subscribe(aLong -> {
                    view.sendEvent(categoryNew, UPDATE);
                    category = null;
                });
                break;
        }
    }

    @Override
    public void notAcceptChanges() {
        view.setFields(category.getName(), category.getDescription(), category.getIsActive());
    }
}
