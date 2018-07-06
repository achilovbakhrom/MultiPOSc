package com.jim.multipos.ui.vendors.vendor_list;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.List;

public interface VendorListFragmentView extends BaseView {
    void setVendorsList(List<Vendor> vendorList);
    void refreshVendorsList();
    void notifyItemUpdated();
    void setSelectedPosition(int position);
}
