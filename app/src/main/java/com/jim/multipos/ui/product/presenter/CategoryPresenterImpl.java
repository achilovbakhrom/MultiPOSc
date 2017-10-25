package com.jim.multipos.ui.product.presenter;


import android.os.Bundle;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.operations.CategoryOperations;
import com.jim.multipos.ui.product.view.CategoryView;

import java.util.List;

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
    private static final String DELETE = "delete";
    private static final int NOT_EXISTS = 0;
    private static final int UPDATED = 1;
    private static final int IS_EXISTS = 2;
    private static final int HAVE_CHILD = 100;
    private static final int IS_ACTIVE = 101;
    private static final int NOT_UPDATED = 102;
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
    public void saveCategory(String name, String description, boolean active) {
        if (this.category == null) {
            category = new Category();
            category.setName(name);
            category.setCreatedDate(System.currentTimeMillis());
            category.setRootId(null);
            category.setDescription(description);
            category.setIsActive(active);
            categoryOperations.isCategoryNameExists(category.getName()).subscribeOn(AndroidSchedulers.mainThread()).subscribe((Boolean aBoolean) -> {
                if (aBoolean) {
                    categoryOperations.addCategory(category).subscribe(aLong -> {
                        view.sendEvent(category, ADD);
                        view.clearFields();
                        category = null;
                    });
                } else {
                    category = null;
                    view.setError("Such name already exists");
                }
            });
        } else {
            temp = new Category();
            temp.setId(category.getId());
            temp.setName(name);
            temp.setIsActive(active);
            temp.setDescription(description);
            temp.setPosition(category.getPosition());
            categoryOperations.getCategoryByName(temp).subscribeOn(AndroidSchedulers.mainThread()).subscribe(integer -> {
                updateMode = integer;
                switch (integer) {
                    case NOT_EXISTS:
                        view.confirmChanges();
                        break;
                    case IS_EXISTS:
                        category = null;
                        view.setError("Such name already exists");
                        break;
                    case UPDATED:
                        if (!category.getIsActive().equals(active) || !category.getDescription().equals(description))
                            view.confirmChanges();
                        else view.showWarningDialog(NOT_UPDATED);
                        break;
                }
            });
        }
    }

    @Override
    public void checkData() {
        if (this.category != null) {
            view.setFields(category.getName(),
                    category.getDescription(),
                    category.getIsActive());
        } else view.clearFields();
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
            case NOT_EXISTS:
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
                categoryOperations.replaceCategory(category).subscribe(aLong -> categoryOperations.replaceCategory(categoryNew).subscribe(aLong1 -> view.sendEvent(categoryNew, UPDATE)));
                break;
            case UPDATED:
                category.setName(temp.getName());
                category.setCreatedDate(System.currentTimeMillis());
                category.setRootId(null);
                category.setDescription(temp.getDescription());
                category.setIsActive(temp.getIsActive());
                category.setPosition(temp.getPosition());
                category.setIsNotModified(false);
                categoryOperations.replaceCategory(category).subscribe(aLong -> view.sendEvent(category, UPDATE));
                break;
        }
    }

    @Override
    public void notAcceptChanges() {
//        view.setFields(category.getName(), category.getDescription(), category.getIsActive());
    }

    @Override
    public void checkDeleteOptions() {
        List<Category> subCategories = categoryOperations.getSubCategories(category).blockingSingle();
        if (subCategories.size() != 0) {
            view.showWarningDialog(HAVE_CHILD);
        } else if (category.getIsActive()) {
            view.showWarningDialog(IS_ACTIVE);
        } else {
            view.confirmDeleting();
        }
    }

    @Override
    public void deleteCategory() {
        category.setIsDeleted(true);
        category.setIsNotModified(false);
        categoryOperations.replaceCategory(category).subscribe(aLong -> view.sendEvent(category, DELETE));
        view.clearFields();
    }
}
