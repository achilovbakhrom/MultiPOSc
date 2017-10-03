package com.jim.multipos.ui.products.presenters;


import android.content.Context;
import android.net.Uri;

import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.operations.CategoryOperations;
import com.jim.multipos.utils.CommonUtils;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.ui.products.fragments.CategoryView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.CategoryEvent;
import com.jim.multipos.utils.rxevents.MessageEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.jim.multipos.utils.rxevents.GlobalEventsConstants.ADD;
import static com.jim.multipos.utils.rxevents.GlobalEventsConstants.UPDATE;

/**
 * Created by DEV on 10.08.2017.
 */

public class CategoryPresenterImpl extends CategoryConnector implements CategoryPresenter {
    private CategoryView view;
    private RxBus rxBus;
    private RxBusLocal rxBusLocal;
    private Category category;
    private String temp;
    private CategoryOperations categoryOperations;
    public final static String FRAGMENT_OPENED = "category";
    private boolean isVisible = false;


    public CategoryPresenterImpl(DatabaseManager databaseManager, RxBus rxBus, RxBusLocal rxBusLocal) {
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;
        categoryOperations = databaseManager.getCategoryOperations();
    }

    @Override
    public void init(CategoryView view) {
        this.view = view;
        initConnectors(rxBus, rxBusLocal);
        rxBusLocal.send(new MessageEvent(FRAGMENT_OPENED));
    }


    @Override
    public void backPressed() {
        view.backToMain();
    }

    @Override
    public void saveCategory(String name, String description, boolean checked, String photoPath) {
        if (this.category == null) {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            category.setActive(checked);
            category.setPhotoPath(photoPath);
            view.clearFields();
            categoryOperations.getCategoryByName(category).subscribeOn(AndroidSchedulers.mainThread()).subscribe(category1 -> {
                if (category1.getId().equals(category.getId())) {
                    categoryOperations.addCategory(category).subscribe(aLong -> rxBus.send(new CategoryEvent(category1, ADD)));
                } else view.setError();
            });
        } else {
            category.setName(name);
            category.setDescription(description);
            category.setActive(checked);
            category.setPhotoPath(photoPath);
            categoryOperations.getMatchCategories(category, temp).subscribeOn(AndroidSchedulers.mainThread()).subscribe(category1 -> {
                if (category1.getId().equals(category.getId())) {
                    categoryOperations.replaceCategory(category).subscribe(aLong -> {
                        rxBus.send(new CategoryEvent(category1, UPDATE));
                        category = null;
                    });
                } else {
                    category = null;
                    view.setError();
                }
            });
        }
    }

    @Override
    public void loadImage() {

    }

    @Override
    public void checkData() {
        if (this.category != null) {
            temp = this.category.getName();
            view.setFields(category.getName(), category.getDescription(), category.isActive(), category.getPhotoPath());
        } else {
            view.clearFields();
        }
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
    void clickedCategory(Category category) {
        this.category = category;
        if (isVisible && category == null){
            view.clearFields();
        }
    }
}
