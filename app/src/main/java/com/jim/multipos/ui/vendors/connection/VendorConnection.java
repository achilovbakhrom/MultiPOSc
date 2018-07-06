package com.jim.multipos.ui.vendors.connection;

import android.content.Context;

import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendors.vendor_edit.VendorEditFragmentView;
import com.jim.multipos.ui.vendors.vendor_list.VendorListFragmentView;

public class VendorConnection {

    private Context context;
    private VendorEditFragmentView vendorEditFragmentView;
    private VendorListFragmentView vendorListFragmentView;

    public VendorConnection(Context context) {
        this.context = context;
    }

    public void setVendorEditFragmentView(VendorEditFragmentView vendorEditFragmentView) {
        this.vendorEditFragmentView = vendorEditFragmentView;
    }

    public void setVendorListFragmentView(VendorListFragmentView vendorListFragmentView) {
        this.vendorListFragmentView = vendorListFragmentView;
    }


    public void setSelectedVendor(Vendor vendor, int position) {
        if (vendorEditFragmentView != null)
            vendorEditFragmentView.setSelectedVendor(vendor, position);
    }

    public void refreshVendorsList() {
        if (vendorListFragmentView != null)
            vendorListFragmentView.refreshVendorsList();
    }

    public void notifyItemUpdated() {
        if (vendorListFragmentView != null)
            vendorListFragmentView.notifyItemUpdated();
    }

    public void setSelectedPosition(int position) {
        if (vendorListFragmentView != null)
            vendorListFragmentView.setSelectedPosition(position);
    }

    public boolean hasChanges() {
        if (vendorEditFragmentView != null)
            return vendorEditFragmentView.hasChanges();
        else
            return false;
    }
}
