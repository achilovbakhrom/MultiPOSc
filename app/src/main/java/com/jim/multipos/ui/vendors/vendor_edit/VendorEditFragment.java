package com.jim.multipos.ui.vendors.vendor_edit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.InputType;
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
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendors.adapter.VendorContactsAdapter;
import com.jim.multipos.ui.vendors.connection.VendorConnection;
import com.jim.multipos.ui.vendors.model.ContactItem;
import com.jim.multipos.utils.CommonUtils;
import com.jim.multipos.utils.GlideApp;
import com.jim.multipos.utils.OpenPickPhotoUtils;
import com.jim.multipos.utils.PhotoPickDialog;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.rxevents.inventory_events.VendorEvent;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

import static android.app.Activity.RESULT_OK;
import static com.jim.multipos.utils.OpenPickPhotoUtils.RESULT_PICK_IMAGE;

public class VendorEditFragment extends BaseFragment implements VendorEditFragmentView {

    private static final int ADD = 0;
    private static final int EDIT = 1;

    @NotEmpty(messageId = R.string.length_validation)
    @BindView(R.id.etVendorName)
    MpEditText etVendorName;
    @BindView(R.id.etContactName)
    MpEditText etContactName;
    @BindView(R.id.etAddress)
    MpEditText etAddress;
    @BindView(R.id.spContactType)
    MPosSpinner spContactType;
    @BindView(R.id.etContactData)
    MpEditText etContactData;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnDelete)
    MpButton btnDelete;
    @BindView(R.id.btnSave)
    MpButton btnSave;
    @BindView(R.id.ivAdd)
    MpMiniActionButton ivAdd;
    @BindView(R.id.rvContacts)
    RecyclerView rvContacts;
    @BindView(R.id.ivVendorImage)
    ImageView ivVendorImage;
    @Inject
    RxPermissions rxPermissions;
    @Inject
    VendorEditFragmentPresenter presenter;
    @Inject
    VendorConnection connection;
    @Inject
    RxBus rxBus;
    private Uri photoSelected;
    private int mode = 0;
    private VendorContactsAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.add_vendor_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        connection.setVendorEditFragmentView(this);
        String contactTypes[] = getContext().getResources().getStringArray(R.array.contact_types);
        spContactType.setAdapter(contactTypes);
        spContactType.setItemSelectionListener((view, position) -> {
            etContactData.setText("");
            if (position == 0) {
                etContactData.setInputType(InputType.TYPE_CLASS_PHONE);
                etContactData.setHint(getString(R.string.enter_phone));
            } else {
                etContactData.setInputType(InputType.TYPE_CLASS_TEXT);
                etContactData.setHint(getString(R.string.enter_email_address));
            }
        });
        adapter = new VendorContactsAdapter(getContext());
        adapter.setListener((position, contact) -> {

        });
        ((SimpleItemAnimator) rvContacts.getItemAnimator()).setSupportsChangeAnimations(false);
        rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContacts.setAdapter(adapter);

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spContactType.getSelectedPosition() == 1) {
                    if (!Patterns.EMAIL_ADDRESS.matcher(etContactData.getText().toString()).matches()) {
                        etContactData.setError(getContext().getString(R.string.email_adress_is_not_valid));
                        return;
                    }
                }
                presenter.addContact(spContactType.getSelectedPosition(), etContactData.getText().toString());
                etContactData.setSelection(0);
                etContactData.setText("");
            }
        });
        ivVendorImage.setOnClickListener(v -> {
            PhotoPickDialog photoPickDialog = new PhotoPickDialog(getActivity(), new PhotoPickDialog.OnButtonsClickListner() {
                @Override
                public void onCameraShot(Uri uri) {
                    photoSelected = uri;
                    GlideApp
                            .with(VendorEditFragment.this)
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .thumbnail(0.2f)
                            .centerCrop()
                            .transform(new RoundedCorners(20))
                            .into(ivVendorImage);
                }

                @Override
                public void onGallery() {
                    rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                        if (aBoolean) {
                            OpenPickPhotoUtils.startPhotoPick(VendorEditFragment.this);
                        }
                    });
                }

                @Override
                public void onRemove() {
                    photoSelected = null;
                    ivVendorImage.setImageResource(R.drawable.camera);
                }
            });
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                if (aBoolean) {
                    if (photoSelected != null)
                        photoPickDialog.showDialog(photoSelected);
                    else photoPickDialog.showDialog();
                }
            });
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showAlert(getContext(), getString(R.string.yes), getString(R.string.no),
                        getString(R.string.deleting_vendor_title), getString(R.string.warning_deleting_vendor),
                        new UIUtils.AlertListener() {
                            @Override
                            public void onPositiveButtonClicked() {
                                presenter.deleteVendor();
                                connection.setSelectedPosition(0);
                            }

                            @Override
                            public void onNegativeButtonClicked() {
                            }
                        });

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    switch (mode) {
                        case ADD:
                            presenter.saveVendor(etVendorName.getText().toString(), etAddress.getText().toString(), etContactName.getText().toString(), chbActive.isChecked(), (photoSelected != null) ? CommonUtils.getRealPathFromURI(getContext(), photoSelected) : "");
                            break;
                        case EDIT:
                            UIUtils.showAlert(getContext(), getString(R.string.yes), getString(R.string.no),
                                    getString(R.string.vendor_data_upate_title), getString(R.string.vendor_data_update_message),
                                    new UIUtils.AlertListener() {
                                        @Override
                                        public void onPositiveButtonClicked() {
                                            presenter.saveVendor(etVendorName.getText().toString(), etAddress.getText().toString(), etContactName.getText().toString(), chbActive.isChecked(), (photoSelected != null) ? CommonUtils.getRealPathFromURI(getContext(), photoSelected) : "");
                                        }

                                        @Override
                                        public void onNegativeButtonClicked() {
                                        }
                                    });
                            break;
                    }

                }
                ;
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter.checkChanges(etVendorName.getText().toString(), etAddress.getText().toString(), etContactName.getText().toString(), chbActive.isChecked(), (photoSelected != null) ? CommonUtils.getRealPathFromURI(getContext(), photoSelected) : "")) {
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
                } else getActivity().finish();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_PICK_IMAGE == requestCode && RESULT_OK == resultCode && data.getData() != null) {
            Uri imageUri = data.getData();
            photoSelected = imageUri;
            GlideApp.with(this)
                    .load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE).thumbnail(0.2f)
                    .centerCrop()
                    .transform(new RoundedCorners(10))
                    .into(ivVendorImage);

        }

    }

    @Override
    public void setSelectedVendor(Vendor vendor, int position) {
        presenter.setVendor(vendor, position);
        if (vendor != null) {
            mode = EDIT;
            UIUtils.closeKeyboard(etVendorName, getContext());
            etVendorName.requestFocus();
            btnSave.setText(R.string.update);
            etVendorName.setText(vendor.getName());
            etAddress.setText(vendor.getAddress());
            etContactName.setText(vendor.getContactName());
            chbActive.setChecked(vendor.getActive());
            presenter.setContacts(vendor);
            etContactData.setText("");
            if (vendor.getPhotoPath() != null && !vendor.getPhotoPath().equals("")) {
                photoSelected = Uri.fromFile(new File(vendor.getPhotoPath()));
                GlideApp
                        .with(this)
                        .load(photoSelected)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .thumbnail(0.2f)
                        .centerCrop()
                        .transform(new RoundedCorners(10))
                        .into(ivVendorImage);
            } else {
                photoSelected = null;
                ivVendorImage.setImageResource(R.drawable.camera);
            }
            presenter.checkDeletable();
        } else {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etVendorName, InputMethodManager.SHOW_IMPLICIT);
            etVendorName.requestFocus();
            mode = ADD;
            etContactData.setText("");
            spContactType.setSelection(0);
            btnSave.setText(R.string.save);
            etVendorName.setText("");
            etAddress.setText("");
            etContactName.setText("");
            chbActive.setChecked(true);
            photoSelected = null;
            ivVendorImage.setImageResource(R.drawable.camera);
            presenter.setContacts(null);
            btnDelete.setVisibility(View.GONE);

        }
    }

    @Override
    public void fillContactsList(List<ContactItem> contactItems) {
        adapter.setItems(contactItems);
    }

    @Override
    public void sendEvent(int type, Vendor vendor) {
        rxBus.send(new VendorEvent(vendor, type));
    }

    @Override
    public void setVendorNameError() {
        etVendorName.setError(getString(R.string.warning_vendor_name_exist));
    }

    @Override
    public void refreshVendorsList() {
        connection.refreshVendorsList();
    }

    @Override
    public void notifyItemUpdated() {
        connection.notifyItemUpdated();
    }

    @Override
    public boolean hasChanges() {
        return presenter.checkChanges(etVendorName.getText().toString(), etAddress.getText().toString(), etContactName.getText().toString(), chbActive.isChecked(), (photoSelected != null) ? CommonUtils.getRealPathFromURI(getContext(), photoSelected) : "");
    }

    @Override
    public void showCantDeleteWarningDialog() {
        UIUtils.showAlert(getContext(),
                getString(R.string.ok),
                getString(R.string.cannot_delete_active_item),
                getString(R.string.warning_cannot_delete_element), (UIUtils.SingleButtonAlertListener) () -> {

                });
    }

    @Override
    public void clearViews() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etVendorName, InputMethodManager.SHOW_IMPLICIT);
        etVendorName.requestFocus();
        mode = ADD;
        etContactData.setText("");
        spContactType.setSelection(0);
        btnSave.setText(R.string.save);
        etVendorName.setText("");
        etAddress.setText("");
        etContactName.setText("");
        chbActive.setChecked(true);
        photoSelected = null;
        ivVendorImage.setImageResource(R.drawable.camera);
        presenter.setContacts(null);
        btnDelete.setVisibility(View.GONE);
    }

    @Override
    public void setButtonDeleteVisibility(int visible) {
        btnDelete.setVisibility(visible);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        connection.setVendorEditFragmentView(null);
    }
}
