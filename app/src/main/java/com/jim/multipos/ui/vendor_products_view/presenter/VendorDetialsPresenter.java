package com.jim.multipos.ui.vendor_products_view.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.products.Vendor;

public interface VendorDetialsPresenter extends Presenter{
    void updateMyDebtState();
    void updateVendorDetials(Vendor updated);
}
