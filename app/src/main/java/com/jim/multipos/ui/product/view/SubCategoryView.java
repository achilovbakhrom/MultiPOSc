package com.jim.multipos.ui.product.view;

import com.jim.multipos.core.BaseView;

/**
 * Created by DEV on 18.08.2017.
 */

public interface SubCategoryView extends BaseView {
    void setFields(String name, String description, boolean active, String photoPath);
    void clearFields();
    void setParentCategoryName(String parentCategory);
    void setData();
    void setError(String error);
}
