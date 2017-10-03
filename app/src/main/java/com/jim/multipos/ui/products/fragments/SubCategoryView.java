package com.jim.multipos.ui.products.fragments;

/**
 * Created by DEV on 18.08.2017.
 */

public interface SubCategoryView {
    void backToMain();
    void setFields(String name, String description, boolean active, String photoPath);
    void clearFields();
    void setParentCategoryName(String parentCategory);
    void setData();
    void setError();
}
