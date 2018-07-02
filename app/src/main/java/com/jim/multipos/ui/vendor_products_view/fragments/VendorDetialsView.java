package com.jim.multipos.ui.vendor_products_view.fragments;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.products.Vendor;

public interface VendorDetialsView extends BaseView {
    void updateView(Vendor vendor,double debt);
    void updateDebt(double debt);
}
