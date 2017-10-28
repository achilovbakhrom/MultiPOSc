package com.jim.multipos.ui.product.presenter;


import android.os.Bundle;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.operations.CategoryOperations;
import com.jim.multipos.ui.product.view.SubCategoryView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by DEV on 18.08.2017.
 */
@PerFragment
public class SubCategoryPresenterImpl extends BasePresenterImpl<SubCategoryView> implements SubCategoryPresenter {
    private Category subCategory, parent, temp, subCategoryNew;
    private CategoryOperations subCategoryOperations;
    private static final String ADD = "added";
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";
    private static final int NOT_EXISTS = 0;
    private static final int UPDATED = 1;
    private static final int IS_EXISTS = 2;
    private static final int HAVE_CHILD = 1;
    private static final int IS_ACTIVE = 2;
    private static final int NOT_UPDATED = 3;
    private int updateMode;

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
    public void save(String name, String description, boolean isActive) {
        if (this.subCategory == null) {
            subCategory = new Category();
            subCategory.setName(name);
            subCategory.setRootId(null);
            subCategory.setParentId(parent.getId());
            subCategory.setIsActive(isActive);
            subCategory.setCreatedDate(System.currentTimeMillis());
//            subCategoryOperations.isSubCategoryNameExists(catesubCategory).subscribe(aBoolean -> {
//                if (aBoolean) {
//                    subCategoryOperations.addSubCategory(subCategory).subscribe(aLong ->
//                            view.sendEvent(subCategory, ADD));
//                    subCategory = null;
//                    view.clearFields();
//                } else {
//                    subCategory = null;
//                    view.setError("Such name already exits");
//                }
//            });
        } else {
            temp = new Category();
            temp.setId(subCategory.getId());
            temp.setParentId(parent.getId());
            temp.setName(name);
            temp.setIsActive(isActive);
            temp.setDescription(description);
            temp.setPosition(subCategory.getPosition());
            subCategoryOperations.getSubCategoryByName(temp).subscribeOn(AndroidSchedulers.mainThread()).subscribe(integer -> {
                updateMode = integer;
                switch (integer) {
                    case NOT_EXISTS:
                        view.confirmChanges();
                        break;
                    case IS_EXISTS:
                        subCategory = null;
                        view.setError("Such name already exists");
                        break;
                    case UPDATED:
                        if (!subCategory.getIsActive().equals(isActive) || !subCategory.getDescription().equals(description))
                            view.confirmChanges();
                        else view.showWarningDialog(NOT_UPDATED);
                        break;
                }
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
    public void acceptChanges() {
        switch (updateMode) {
            case NOT_EXISTS:
                subCategoryNew = new Category();
                subCategoryNew.setName(temp.getName());
                subCategoryNew.setDescription(temp.getDescription());
                subCategoryNew.setIsActive(temp.getIsActive());
                subCategoryNew.setCreatedDate(System.currentTimeMillis());
                subCategoryNew.setPosition(temp.getPosition());
                subCategoryNew.setParentId(subCategory.getParentId());
                if (subCategory.getRootId() == null) {
                    subCategoryNew.setRootId(subCategory.getId());
                } else subCategoryNew.setRootId(subCategory.getRootId());
                subCategory.setIsDeleted(true);
                subCategory.setIsNotModified(false);
                subCategoryOperations.replaceCategory(subCategory).subscribe(aLong -> {
                    subCategoryOperations.replaceCategory(subCategoryNew).subscribe(aLong1 -> view.sendEvent(subCategoryNew, UPDATE));
                });
                break;
            case UPDATED:
                subCategory.setName(temp.getName());
                subCategory.setCreatedDate(System.currentTimeMillis());
                subCategory.setRootId(null);
                subCategory.setDescription(temp.getDescription());
                subCategory.setIsActive(temp.getIsActive());
                subCategory.setPosition(temp.getPosition());
                subCategory.setIsNotModified(false);
                subCategoryOperations.replaceCategory(subCategory).subscribe(aLong -> {
                    view.sendEvent(subCategory, UPDATE);
                });
                break;
        }
    }

    @Override
    public void notAcceptChanges() {
        view.setFields(subCategory.getName(), subCategory.getDescription(), subCategory.getIsActive());
    }

    @Override
    public void checkDeleteOptions() {
//        if (subCategory.getProducts().size() != 0) {
//            view.showWarningDialog(HAVE_CHILD);
//        } else
        if (subCategory.getIsActive()) {
            view.showWarningDialog(IS_ACTIVE);
        } else {
            view.confirmDeleting();
        }
    }

    @Override
    public void deleteSubCategory() {
        subCategory.setIsDeleted(true);
        subCategory.setIsNotModified(false);
        subCategoryOperations.replaceCategory(subCategory).subscribe(aLong -> view.sendEvent(subCategory, DELETE));
        view.clearFields();
    }

    @Override
    public void clickedSubCategory(Category subCategory) {
        this.subCategory = subCategory;
        if (subCategory == null) {
            view.clearFields();
        } else checkData();
    }
}
