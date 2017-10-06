package com.jim.multipos.ui.product.presenter;


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

public class CategoryPresenterImpl extends BasePresenterImpl<CategoryView> implements CategoryPresenter {
    private Category category;
    private CategoryOperations categoryOperations;
    private boolean isVisible = false;
    private static final String ADD = "added";
    private static final String UPDATE = "update";

    @Inject
    public CategoryPresenterImpl(CategoryView view, DatabaseManager databaseManager) {
        super(view);
        categoryOperations = databaseManager.getCategoryOperations();
    }

    @Override
    public void saveCategory(String name, String description, boolean checked, String photoPath) {
        if (name.isEmpty())
        {
            view.setError("Please, enter the name");
        } else if (name.length() < 3){
            view.setError("Category name should be longer than 3 letters");
        } else if (this.category == null) {
            Category category = new Category();
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
            category.setName(name);
            category.setDescription(description);
            category.setActive(checked);
            category.setPhotoPath(photoPath);
            categoryOperations.getCategoryByName(category).subscribeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
                if (aBoolean) {
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
    public void isVisible(boolean visible) {
        isVisible = visible;
    }

    @Override
    public void clickedCategory(Category category) {
        this.category = category;
        if (isVisible && category == null) {
            view.clearFields();
        }
    }

    @Override
    public void onDestroy() {
        isVisible = false;
    }

}
