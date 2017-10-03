package com.jim.multipos.ui.products.presenters;


import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.SubCategory;
import com.jim.multipos.data.operations.CategoryOperations;
import com.jim.multipos.data.operations.SubCategoryOperations;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.products.fragments.SubCategoryView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.MessageEvent;
import com.jim.multipos.utils.rxevents.SubCategoryEvent;

import static com.jim.multipos.utils.rxevents.GlobalEventsConstants.ADD;
import static com.jim.multipos.utils.rxevents.GlobalEventsConstants.UPDATE;

/**
 * Created by DEV on 18.08.2017.
 */

public class SubCategoryPresenterImpl extends SubCategoryConnector implements SubCategoryPresenter {
    private SubCategoryView view;
    private Category category;
    private SubCategory subCategory;
    private DatabaseManager databaseManager;
    private SubCategoryOperations subCategoryOperations;
    private CategoryOperations categoryOperations;
    private RxBusLocal rxBusLocal;
    private PreferencesHelper preferencesHelper;
    private RxBus rxBus;
    private boolean isVisible = false;
    public final static String FRAGMENT_OPENED = "subcategory";

    public SubCategoryPresenterImpl(DatabaseManager databaseManager, RxBus rxBus, RxBusLocal rxBusLocal, PreferencesHelper preferencesHelper) {
        this.databaseManager = databaseManager;
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;
        this.preferencesHelper = preferencesHelper;
        categoryOperations = databaseManager.getCategoryOperations();
        subCategoryOperations = databaseManager.getSubCategoryOperations();
    }

    @Override
    public void init(SubCategoryView view) {
        this.view = view;
        initConnectors(rxBus, rxBusLocal);
        rxBusLocal.send(new MessageEvent(FRAGMENT_OPENED));
    }

    @Override
    public void save(String name, String description, boolean checkboxChecked, String photoPath) {
        if (this.subCategory == null) {
            SubCategory subCategory = new SubCategory();
            subCategory.setName(name);
            subCategory.setDescription(description);
            subCategory.setCategoryId(category.getId());
            subCategory.setActive(checkboxChecked);
            subCategory.setPhotoPath(photoPath);
            view.clearFields();
            subCategoryOperations.getSubCategoryByName(subCategory).subscribe(subCategory1 -> {
                if (subCategory1.getId().equals(subCategory.getId())) {
                    subCategoryOperations.addSubCategory(subCategory).subscribe(aLong -> rxBus.send(new SubCategoryEvent(subCategory, ADD)));
                } else view.setError();
            });
        } else {
            subCategory.setName(name);
            subCategory.setDescription(description);
            subCategory.setCategoryId(category.getId());
            subCategory.setActive(checkboxChecked);
            subCategory.setPhotoPath(photoPath);
            subCategoryOperations.getSubCategoryByName(subCategory).subscribe(subCategory1 -> {
                if (subCategory1.getId().equals(subCategory.getId())) {
                    subCategoryOperations.replaceSubCategory(subCategory).subscribe(aLong -> {
                        rxBus.send(new SubCategoryEvent(subCategory, UPDATE));
                        subCategory = null;
                    });
                } else view.setError();
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
        this.view = null;
        isVisible = false;
    }

    @Override
    void isVisible() {
        isVisible = true;
    }

    @Override
    void setParentCategory(Category category) {
        this.category = category;
        view.setParentCategoryName(category.getName());
    }

    @Override
    void clickedSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
        if (isVisible && subCategory == null) {
            view.clearFields();
        }
    }
}
