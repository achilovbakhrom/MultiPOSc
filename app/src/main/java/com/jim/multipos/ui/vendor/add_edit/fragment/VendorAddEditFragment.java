package com.jim.multipos.ui.vendor.add_edit.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.core.ItemRemoveListener;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.Vendor;
import com.jim.multipos.ui.vendor.AddingMode;
import com.jim.multipos.ui.vendor.add_edit.ContentChangeable;
import com.jim.multipos.ui.vendor.add_edit.VendorAddEditActivity;
import com.jim.multipos.ui.vendor.add_edit.VendorAddEditPresenter;
import com.jim.multipos.ui.vendor.add_edit.adapter.ContactAdapter;
import com.jim.multipos.utils.UIUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import lombok.Getter;


/**
 * Created by Achilov Bakhrom on 10/21/17.
 */

public class VendorAddEditFragment extends BaseFragment implements ContentChangeable, ItemRemoveListener<Contact> {

    @NotEmpty(messageId = R.string.warning_ventor_name)
    @BindView(R.id.etVendorName)
    MpEditText vendorName;

    @BindView(R.id.etContactName)
    MpEditText vendorContact;

    @BindView(R.id.etAddress)
    MpEditText address;

    @BindView(R.id.spContactType)
    MPosSpinner contactType;

    @BindView(R.id.etContactData)
    MpEditText contactData;

    @BindView(R.id.chbActive)
    MpCheckbox active;

    @BindView(R.id.btnCancel)
    MpButton cancel;

    @BindView(R.id.btnProducts)
    MpButton products;

    @BindView(R.id.btnDelete)
    MpButton delete;

    @BindView(R.id.btnSave)
    MpButton save;

    @BindView(R.id.ivAdd)
    ImageView add;

    @BindView(R.id.rvContacts)
    RecyclerView contacts;

    @Getter
    private boolean isChangeDetected = false;

    @Override
    protected int getLayout() {
        return R.layout.add_vendor_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        VendorAddEditPresenter presenter = ((VendorAddEditActivity) getContext()).getPresenter();
        presenter.onCreateView(savedInstanceState);
        contactType.setAdapter(presenter.getContactTypes());
        contacts.setLayoutManager(new LinearLayoutManager(getContext()));
        ContactAdapter adapter = new ContactAdapter(new ArrayList<>());
        adapter.setListener(this);
        contacts.setAdapter(adapter);
        contactType.setItemSelectionListener((view, position) -> {
            contactData.setText("");
            if (position == 0)
                contactData.setInputType(InputType.TYPE_CLASS_PHONE);
            else
                contactData.setInputType(InputType.TYPE_CLASS_TEXT);
        });
        vendorName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                detectChange(true);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        vendorContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                detectChange(true);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                detectChange(true);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        active.setCheckedChangeListener(isChecked -> {
            detectChange(true);
        });
        contactData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                detectChange(true);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void detectChange(boolean def) {
        if (vendorName.getText().toString().isEmpty() &&
                vendorContact.getText().toString().isEmpty() &&
                address.getText().toString().isEmpty() &&
                ((VendorAddEditActivity) getContext()).getPresenter().getContacts().isEmpty() &&
                ((VendorAddEditActivity) getContext()).getPresenter().getMode() == AddingMode.ADD) {
            isChangeDetected = false;
        } else {
            isChangeDetected = def;
        }
    }

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }


    @OnClick(value = {R.id.btnSave, R.id.btnDelete, R.id.btnProducts, R.id.btnCancel, R.id.ivAdd})
    public void buttonClicked(View view) {
        UIUtils.closeKeyboard(view, getContext());
        VendorAddEditPresenter presenter = ((VendorAddEditActivity) getContext()).getPresenter();
        switch (view.getId()) {
            case R.id.btnSave:
                if (presenter.getMode() == AddingMode.EDIT) {
                    UIUtils.showAlert(getContext(), getString(R.string.yes), getString(R.string.no),
                            getString(R.string.vendor_data_upate_title), getString(R.string.vendor_data_update_message),
                            new UIUtils.AlertListener() {
                                @Override
                                public void onPositiveButtonClicked() {
                                    detectChange(false);
                                    presenter.addVendor(vendorName.getText().toString(),
                                            vendorContact.getText().toString(),
                                            address.getText().toString(),
                                            active.isChecked());
                                    presenter.setMode(AddingMode.ADD, null);
                                }
                                @Override
                                public void onNegativeButtonClicked() {}
                            });
                } else {
                    if (presenter.isVendorNameExists(vendorName.getText().toString())) {
                        vendorName.setError(getString(R.string.warning_vendor_name_exist));
                        return;
                    }
                    detectChange(false);
                    presenter.addVendor(vendorName.getText().toString(),
                            vendorContact.getText().toString(),
                            address.getText().toString(),
                            active.isChecked());
                    presenter.setMode(AddingMode.ADD, null);
                }
                break;

            case  R.id.btnDelete:
                if (((VendorAddEditActivity) getContext()).getPresenter().getVendor() != null &&
                        ((VendorAddEditActivity) getContext()).getPresenter().getVendor().isActive()) {
                    ((VendorAddEditActivity) getContext()).showCantDeleteActiveItemMessage();
                } else {
                    UIUtils.showAlert(getContext(), getString(R.string.yes), getString(R.string.no),
                            getString(R.string.deleting_vendor_title), getString(R.string.warning_deleting_vendor),
                            new UIUtils.AlertListener() {
                                @Override
                                public void onPositiveButtonClicked() {
                                    detectChange(false);
                                    presenter.removeVendor();
                                    presenter.setMode(AddingMode.ADD, null);
                                }
                                @Override
                                public void onNegativeButtonClicked() {}
                            });
                }

                break;

            case R.id.btnProducts:
                //TODO after creating products
                break;

            case R.id.btnCancel:
                if (isChangeDetected) {
                    UIUtils.showAlert(getContext(), getString(R.string.yes),
                            getString(R.string.no), getString(R.string.discard_changes),
                            getString(R.string.warning_discard_changes), new UIUtils.AlertListener() {
                                @Override
                                public void onPositiveButtonClicked() {
                                    setMode(AddingMode.ADD, null);
                                }

                                @Override
                                public void onNegativeButtonClicked() {

                                }
                            });
                }
                else {
                    getActivity().finish();
                }
                break;
            case R.id.ivAdd:
                if (contactData.getText().toString().isEmpty()) {
                    contactData.setError(getString(R.string.warning_contact_is_empty));
                    return;
                }
                if (contactType.getSelectedPosition() == 1) {
                    if (!Patterns.EMAIL_ADDRESS.matcher(contactData.getText().toString()).matches()) {
                        contactData.setError("Email address is not valid!!!");
                        return;
                    }
                }
                detectChange(true);
                presenter.addContact(contactType.getSelectedPosition(), contactData.getText().toString());
                contactType.setSelection(0);
                contactData.setText("");
                break;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((VendorAddEditActivity) getContext()).getPresenter().onSaveInstanceState(outState);
    }

    private void clearContactsList() {
        if (contacts != null ) {
            ((ContactAdapter) contacts.getAdapter()).removeAllItems();
        }
    }

    public void addContact(Contact contact) {
        if (contacts != null && contacts.getAdapter() != null) {
            ((ContactAdapter) contacts.getAdapter()).addItem(contact);
        }
    }

    @Override
    public void setMode(AddingMode mode, Vendor vendor) {
        switch (mode) {
            case ADD:
                vendorName.setText("");
                vendorName.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(vendorName, InputMethodManager.SHOW_IMPLICIT);
                vendorContact.setText("");
                address.setText("");
                contactType.setSelection(0);
                contactData.setText("");
                active.setChecked(true);
                products.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                save.setText(R.string.add);
                clearContactsList();
                break;
            case EDIT:
                if (vendor != null) {
                    vendorName.setText(vendor.getName());
                    vendorName.setError(null);
                    vendorContact.setText(vendor.getContactName());
                    vendorContact.setError(null);
                    address.setText(vendor.getAddress());
                    address.setError(null);
                    contactType.setSelection(0);
                    contactData.setText("");
                    contactData.setError(null);
                    active.setChecked(true);
                    active.setChecked(vendor.getIsActive());
                    products.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    save.setText(R.string.update);
                    if (vendor.getContacts() != null && !vendor.getContacts().isEmpty())
                        ((ContactAdapter) contacts.getAdapter()).setItems(vendor.getContacts());
                    else
                        ((ContactAdapter) contacts.getAdapter()).removeAllItems();
                }
                break;
        }
        detectChange(false);
    }

    public void removeContact(Contact contact) {
        ((ContactAdapter) contacts.getAdapter()).removeItem(contact);
    }

    @Override
    public void onItemRemove(int position, Contact item) {
        UIUtils.showAlert(getContext(), getString(R.string.yes), getString(R.string.no),
                getString(R.string.warning_contact_deletion_title),
                "Do you really want to delete the contact " + item.getName() + "?",
                new UIUtils.AlertListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        ((VendorAddEditActivity) getContext()).getPresenter().removeContact(item);
                    }
                    @Override
                    public void onNegativeButtonClicked() {}
                });
    }
}
