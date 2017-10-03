package com.jim.multipos.ui.products.fragments;

/**
 * Created by DEV on 09.08.2017.
 */

public interface CategoryView {
    void backToMain();
    void setFields(String name, String description, boolean active, String photoPath);
    void clearFields();
    void setData();
    void setError();
}
