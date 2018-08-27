package com.jim.multipos.ui.admin_main_page.fragments.product.presenter;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.ui.admin_main_page.fragments.product.model.Product;

public interface ProductView extends BaseView {
    void setUpEditor(Product product);
    void onAddMode();
}
