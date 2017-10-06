package com.jim.multipos.ui.product.presenter;


import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.SubCategory;
import com.jim.multipos.data.operations.SubCategoryOperations;
import com.jim.multipos.ui.product.view.SubCategoryView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.SubCategoryEvent;

import javax.inject.Inject;

import static com.jim.multipos.utils.rxevents.GlobalEventsConstants.ADD;
import static com.jim.multipos.utils.rxevents.GlobalEventsConstants.UPDATE;

/**
 * Created by DEV on 18.08.2017.
 */
@PerFragment
public class SubCategoryPresenterImpl extends BasePresenterImpl<SubCategoryView> implements SubCategoryPresenter {
    private Category category;
    private SubCategory subCategory;
    private SubCategoryOperations subCategoryOperations;
    private boolean isVisible = false;
    private static final String ADD = "added";
    private static final String UPDATE = "update";

    @Inject
    public SubCategoryPresenterImpl(SubCategoryView view,DatabaseManager databaseManager) {
        super(view);
        subCategoryOperations = databaseManager.getSubCategoryOperations();
    }

    @Override
    public void save(String name, String description, boolean checkboxChecked, String photoPath) {
        if (name.isEmpty())
        {
            view.setError("Please, enter the name");
        } else if (name.length() < 3){
            view.setError("Subcategory name should be longer than 3 letters");
        } else if (this.subCategory == null) {
            SubCategory subCategory = new SubCategory();
            subCategory.setName(name);
            subCategory.setDescription(description);
            subCategory.setCategoryId(category.getId());
            subCategory.setActive(checkboxChecked);
            subCategory.setPhotoPath(photoPath);
            view.clearFields();
            subCategoryOperations.addSubCategory(subCategory).subscribe(aLong -> view.sendEvent(subCategory, ADD));
        } else {
            subCategory.setName(name);
            subCategory.setDescription(description);
            subCategory.setCategoryId(category.getId());
            subCategory.setActive(checkboxChecked);
            subCategory.setPhotoPath(photoPath);
            subCategoryOperations.replaceSubCategory(subCategory).subscribe(aLong -> {
                view.sendEvent(subCategory, UPDATE);
                subCategory = null;
            });
        }
    }

    @Override
    public void back() {
        view.backToMain();
    }

    @Override
    public void checkData() {
        if (this.subCategory != null) {
            view.setFields(this.subCategory.getName(),
                    this.subCategory.getDescription(),
                    this.subCategory.getActive(),
                    this.subCategory.getPhotoPath());
        } else view.clearFields();
    }

    @Override
    public void onDestroy() {
        isVisible = false;
    }

    @Override
    public void isVisible(boolean visible) {
        isVisible = visible;
    }

    @Override
    public void setParentCategory(Category category) {
        this.category = category;
        view.setParentCategoryName(category.getName());
    }

    @Override
    public void clickedSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
        if (isVisible && subCategory == null) {
            view.clearFields();
        }
    }
}
