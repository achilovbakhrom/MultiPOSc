package com.jim.multipos.ui.vendor.add_edit.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.core.ItemRemoveListener;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendor.AddingMode;
import com.jim.multipos.ui.vendor.add_edit.ContentChangeable;
import com.jim.multipos.ui.vendor.add_edit.VendorAddEditActivity;
import com.jim.multipos.ui.vendor.add_edit.VendorAddEditPresenter;
import com.jim.multipos.ui.vendor.add_edit.adapter.ContactAdapter;
import com.jim.multipos.utils.CommonUtils;
import com.jim.multipos.utils.GlideApp;
import com.jim.multipos.utils.OpenPickPhotoUtils;
import com.jim.multipos.utils.PhotoPickDialog;
import com.jim.multipos.utils.UIUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import lombok.Getter;

import static android.app.Activity.RESULT_OK;
import static com.jim.multipos.utils.OpenPickPhotoUtils.RESULT_PICK_IMAGE;


/**
 * Created by Achilov Bakhrom on 10/21/17.
 */

public class VendorAddEditFragment extends BaseFragment implements ContentChangeable {

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

    @BindView(R.id.btnDelete)
    MpButton delete;

    @BindView(R.id.btnSave)
    MpButton save;

    @BindView(R.id.ivAdd)
    MpMiniActionButton add;

    @BindView(R.id.rvContacts)
    RecyclerView contacts;

    @BindView(R.id.ivVendorImage)
    ImageView ivVendorImage;

    @Getter
    private boolean isChangeDetected = false;
    private boolean isFirstTime = false;
    private Uri photoSelected;

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
        ContactAdapter adapter = new ContactAdapter(new ArrayList<>(), getContext());
        adapter.setListener(new ContactAdapter.OnContactClickListener() {
            @Override
            public void onRemove(int position, Contact contact) {
                UIUtils.showAlert(getContext(), getString(R.string.yes), getString(R.string.no),
                        getString(R.string.warning_contact_deletion_title),
                        "Do you really want to delete the contact " + contact.getName() + "?",
                        new UIUtils.AlertListener() {
                            @Override
                            public void onPositiveButtonClicked() {
                                ((VendorAddEditActivity) getContext()).getPresenter().removeContact(contact);
                            }

                            @Override
                            public void onNegativeButtonClicked() {
                            }
                        });
            }

            @Override
            public void onSave(int position, Contact contact) {

            }
        });
        contacts.setAdapter(adapter);
        contactType.setItemSelectionListener((view, position) -> {
            if (isFirstTime) {
                contactData.setText("");
                if (position == 0)
                    contactData.setInputType(InputType.TYPE_CLASS_PHONE);
                else
                    contactData.setInputType(InputType.TYPE_CLASS_TEXT);
            } else isFirstTime = true;
        });
        vendorName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                detectChange(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        vendorContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                detectChange(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        active.setCheckedChangeListener(isChecked -> {
            detectChange(true);
        });
        contactData.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        contactData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    detectChange(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
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


    @OnClick(value = {R.id.btnSave, R.id.btnDelete, R.id.btnCancel, R.id.ivAdd, R.id.ivVendorImage})
    public void buttonClicked(View view) {
        UIUtils.closeKeyboard(view, getContext());
        VendorAddEditPresenter presenter = ((VendorAddEditActivity) getContext()).getPresenter();
        switch (view.getId()) {
            case R.id.btnSave:
                if (presenter.getMode() == AddingMode.EDIT) {
                    if (isValid()) {
                        if (presenter.isVendorNameExists(vendorName.getText().toString())) {
                            vendorName.setError(getString(R.string.warning_vendor_name_exist));
                            return;
                        }
                        UIUtils.showAlert(getContext(), getString(R.string.yes), getString(R.string.no),
                                getString(R.string.vendor_data_upate_title), getString(R.string.vendor_data_update_message),
                                new UIUtils.AlertListener() {
                                    @Override
                                    public void onPositiveButtonClicked() {

                                        detectChange(false);
                                        presenter.addVendor(vendorName.getText().toString(),
                                                vendorContact.getText().toString(),
                                                address.getText().toString(),
                                                (photoSelected != null) ? CommonUtils.getRealPathFromURI(getContext(), photoSelected) : "",
                                                active.isChecked());
                                        presenter.setMode(AddingMode.ADD, null);
                                    }

                                    @Override
                                    public void onNegativeButtonClicked() {
                                    }
                                });
                    }
                } else {
                    if (presenter.isVendorNameExists(vendorName.getText().toString())) {
                        vendorName.setError(getString(R.string.warning_vendor_name_exist));
                        return;
                    }
                    detectChange(false);
                    if (isValid()) {
                        presenter.addVendor(vendorName.getText().toString(),
                                vendorContact.getText().toString(),
                                address.getText().toString(),
                                (photoSelected != null) ? CommonUtils.getRealPathFromURI(getContext(), photoSelected) : "",
                                active.isChecked());
                        presenter.setMode(AddingMode.ADD, null);
                    }
                }
                break;

            case R.id.btnDelete:
                if (((VendorAddEditActivity) getContext()).getPresenter().getVendor() != null &&
                        ((VendorAddEditActivity) getContext()).getPresenter().getVendor().isActive()) {
                    ((VendorAddEditActivity) getContext()).showCantDeleteActiveItemMessage(() -> {
                    });
                } else {
                    presenter.checkVendorInventoryState();
                }

                break;

            case R.id.btnCancel:
                if (isChangeDetected) {
                    UIUtils.showAlert(getContext(), getString(R.string.yes),
                            getString(R.string.no), getString(R.string.discard_changes),
                            getString(R.string.warning_discard_changes), new UIUtils.AlertListener() {
                                @Override
                                public void onPositiveButtonClicked() {
                                    getActivity().finish();
                                }

                                @Override
                                public void onNegativeButtonClicked() {

                                }
                            });
                } else {
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
            case R.id.ivVendorImage:
                PhotoPickDialog photoPickDialog = new PhotoPickDialog(getActivity(), new PhotoPickDialog.OnButtonsClickListner() {
                    @Override
                    public void onCameraShot(Uri uri) {
                        photoSelected = uri;
                        GlideApp
                                .with(VendorAddEditFragment.this)
                                .load(uri)
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .thumbnail(0.2f)
                                .centerCrop()
                                .transform(new RoundedCorners(20))
                                .into(ivVendorImage);
                    }

                    @Override
                    public void onGallery() {
                        ((VendorAddEditActivity) getContext()).getRxPermissions().request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                            if (aBoolean) {
                                OpenPickPhotoUtils.startPhotoPick(VendorAddEditFragment.this);
                            }
                        });
                    }

                    @Override
                    public void onRemove() {
                        photoSelected = null;
                        ivVendorImage.setImageResource(R.drawable.camera);
                    }
                });
                ((VendorAddEditActivity) getContext()).getRxPermissions().request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                    if (aBoolean) {
                        if (photoSelected != null)
                            photoPickDialog.showDialog(photoSelected);
                        else photoPickDialog.showDialog();
                    }
                });
                break;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((VendorAddEditActivity) getContext()).getPresenter().onSaveInstanceState(outState);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_PICK_IMAGE == requestCode && RESULT_OK == resultCode && data.getData() != null) {
            Uri imageUri = data.getData();
            photoSelected = imageUri;
            GlideApp.with(VendorAddEditFragment.this)
                    .load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE).thumbnail(0.2f)
                    .centerCrop()
                    .transform(new RoundedCorners(20))
                    .into(ivVendorImage);

        }

    }

    private void clearContactsList() {
        if (contacts != null) {
            ((ContactAdapter) contacts.getAdapter()).removeAllItems();
        }
    }

    public void addContact(Contact contact) {
        if (contacts != null && contacts.getAdapter() != null) {
            ((ContactAdapter) contacts.getAdapter()).addContactItem(contact, contacts.getAdapter().getItemCount() + 1);
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
                delete.setVisibility(View.GONE);
                save.setText(R.string.save);
                photoSelected = null;
                ivVendorImage.setImageResource(R.drawable.camera);
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
                    delete.setVisibility(View.VISIBLE);
                    save.setText(R.string.update);
                    if (vendor.getPhotoPath() != null && !vendor.getPhotoPath().equals("")) {
                        photoSelected = Uri.fromFile(new File(vendor.getPhotoPath()));
                        GlideApp
                                .with(VendorAddEditFragment.this)
                                .load(photoSelected)
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .thumbnail(0.2f)
                                .centerCrop()
                                .transform(new RoundedCorners(20))
                                .into(ivVendorImage);
                    } else {
                        photoSelected = null;
                        ivVendorImage.setImageResource(R.drawable.camera);
                    }
                    vendor.resetContacts();
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

    public void showVendorHasProductsMessage() {
        UIUtils.showAlert(getContext(), getString(R.string.ok), getString(R.string.warning), "This vendor have products connected with him", () -> {
        });
    }

    public void showDeleteDialog() {
        VendorAddEditPresenter presenter = ((VendorAddEditActivity) getContext()).getPresenter();
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
                    public void onNegativeButtonClicked() {
                    }
                });
    }

    public void updateContacts() {
        contacts.getAdapter().notifyDataSetChanged();
    }
}
