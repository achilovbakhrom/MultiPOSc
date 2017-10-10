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
    private Category category;
    private CategoryOperations categoryOperations;
    private static final String ADD = "added";
    private static final String UPDATE = "update";

    @Inject
    CategoryPresenterImpl(CategoryView view, DatabaseManager databaseManager) {
        super(view);
        categoryOperations = databaseManager.getCategoryOperations();
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        if (this.category != null) {
            view.setFields(category.getName(), category.getDescription(), category.isActive(), category.getPhotoPath());
        } else {
            view.clearFields();
        }
    }

    @Override
    public void saveCategory(String name, String description, boolean checked, String photoPath) {
        if (name.isEmpty())
        {
            view.setError("Please, enter the name");
        } else if (name.length() < 3){
            view.setError("Category name should be longer than 3 letters");
        } else if (this.category == null) {
            category = new Category();
            category.setName(name);
            category.setDescription(description);
            category.setActive(checked);
            category.setPhotoPath(photoPath);
            view.clearFields();
            categoryOperations.getMatchCategory(category).subscribeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
                if (aBoolean) {
                    categoryOperations.addCategory(category).subscribe(aLong ->
                            view.sendEvent(category, ADD));
                } else view.setError("Such name already exits");
            });
        } else {
            Category categoryNew = new Category();
            categoryNew.setId(category.getId());
            categoryNew.setName(name);
            categoryNew.setDescription(description);
            categoryNew.setActive(checked);
            categoryNew.setPhotoPath(photoPath);
            categoryOperations.getCategoryByName(categoryNew).subscribeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
                if (aBoolean) {
                    category.setName(name);
                    category.setDescription(description);
                    category.setActive(checked);
                    category.setPhotoPath(photoPath);
                    categoryOperations.replaceCategory(category).subscribe(aLong -> {
                        view.sendEvent(category, UPDATE);
                        category = null;
                    });
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
            view.setFields(category.getName(), category.getDescription(), category.isActive(), category.getPhotoPath());
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
}
