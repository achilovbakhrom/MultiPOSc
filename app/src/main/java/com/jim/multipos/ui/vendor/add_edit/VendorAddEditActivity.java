package com.jim.multipos.ui.vendor.add_edit;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.Vendor;
import com.jim.multipos.ui.vendor.AddingMode;
import com.jim.multipos.ui.vendor.add_edit.fragment.VendorAddEditFragment;
import com.jim.multipos.ui.vendor.add_edit.fragment.VendorsListFragment;
import com.jim.multipos.utils.WarningDialog;

import javax.inject.Inject;

import lombok.Getter;

/**
 * Created by Achilov Bakhrom on 10/21/17.
 */

public class VendorAddEditActivity extends DoubleSideActivity implements VendorAddEditView{

    public static final String ADDING_MODE_KEY = "ADDING_MODE_KEY";
    public static final String VENDOR_ID = "VENDOR_ID";

    @Inject
    @Getter
    VendorAddEditPresenter presenter;

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setMode(AddingMode.ADD, null);
        addFragmentToLeft(new VendorAddEditFragment());
        addFragmentToRight(new VendorsListFragment());
    }

    @Override
    public void refreshVendorsList() {
        VendorsListFragment fragment = (VendorsListFragment) getCurrentFragmentRight();
        fragment.updateVendorsList();
    }

    @Override
    public void prepareAddMode() {
        VendorsListFragment listFragment = (VendorsListFragment) getCurrentFragmentRight();
        if (listFragment != null)
            listFragment.setAddMode();
        VendorAddEditFragment fragment = (VendorAddEditFragment) getCurrentFragmentLeft();
        if (fragment != null)
            fragment.setMode(AddingMode.ADD, null);
    }

    @Override
    public void prepareEditMode(Vendor vendor) {
        VendorAddEditFragment fragment = (VendorAddEditFragment) getCurrentFragmentLeft();
        fragment.setMode(AddingMode.EDIT, vendor);
    }

    @Override
    public void selectAddItem() {
        prepareAddMode();
    }

    @Override
    public void selectItem(int position) {
        if (position != 0) {
            if ( position - 1 < presenter.getVendors().size() && position - 1 > 0) {
                Vendor vendor = presenter.getVendors().get(position);
                prepareEditMode(vendor);
            }
        }
    }

    @Override
    public void addContactToAddEditView(Contact contact) {
        ((VendorAddEditFragment)getCurrentFragmentLeft()).addContact(contact);
    }

    @Override
    public void removeContact(Contact contact) {
        ((VendorAddEditFragment)getCurrentFragmentLeft()).removeContact(contact);
    }

    @Override
    public void showCantDeleteActiveItemMessage() {
        WarningDialog dialog = new WarningDialog(this);
        dialog.setWarningText(getString(R.string.warning_deletion_of_active_elements));

    }

    @Override
    public void showAddEditChangeMessage() {
        WarningDialog dialog = new WarningDialog(this);
        dialog.setWarningText("sss");
    }
}