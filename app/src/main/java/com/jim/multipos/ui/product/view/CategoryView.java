package com.jim.multipos.ui.product.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.products.Category;

/**
 * Created by DEV on 09.08.2017.
 */

public interface CategoryView extends BaseView {
    void setFields(String name, String description, boolean active, String photoPath);
    void clearFields();
    void setData();
    void setError(String error);
    void sendEvent(Category category, String add);
}
