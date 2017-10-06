package com.jim.multipos.ui.products.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.products.di.ProductsComponent;
import com.jim.multipos.ui.products.presenters.CategoryPresenter;

import com.jim.multipos.utils.CommonUtils;

//import com.jim.multipos.utils.GlideApp;
import com.jim.multipos.utils.OpenPickPhotoUtils;
import com.jim.multipos.utils.PhotoPickDialog;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.managers.PosFragmentManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;


/**
 * Created by DEV on 09.08.2017.
 */

public class AddCategoryFragment extends Fragment { //BaseFragment implements CategoryView {

    @BindView(R.id.etCategoryName)
    MpEditText etCategoryName;
    @BindView(R.id.etCategoryDescription)
    EditText etCategoryDescription;
    @BindView(R.id.btnCategoryCancel)
    MpButton btnCancel;
    @BindView(R.id.btnCategorySave)
    MpButton btnSave;
    @BindView(R.id.ivLoadImage)
    ImageView ivLoadImage;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @Inject
    PosFragmentManager posFragmentManager;
    @Inject
    CategoryPresenter presenter;
    @Inject
    RxBus rxBus;
    @Inject
    RxPermissions rxPermissions;
    private Uri photoSelected;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_category_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
//        this.getComponent(ProductsComponent.class).inject(this);
//        presenter.init(this);
        presenter.checkData();
        return view;
    }

    @OnClick(R.id.btnCategoryCancel)
    public void onBack() {
        presenter.backPressed();
    }

    @OnClick(R.id.btnCategorySave)
    public void onSave() {
        if (!etCategoryName.getText().toString().isEmpty()) {
            if (etCategoryName.getText().length() > 3)
                presenter.saveCategory(etCategoryName.getText().toString(),
                        etCategoryDescription.getText().toString(),
                        chbActive.isCheckboxChecked(),
                        (photoSelected != null) ? CommonUtils.getRealPathFromURI(getContext(), photoSelected) : "");
            else etCategoryName.setError("Name length must be greater than 3");
        } else {
            etCategoryName.setError("Please, enter category name");
        }
    }

    @OnClick(R.id.ivLoadImage)
    public void onLoadImage() {
        PhotoPickDialog photoPickDialog = new PhotoPickDialog(getActivity(), new PhotoPickDialog.OnButtonsClickListner() {
            @Override
            public void onCameraShot(Uri uri) {
                photoSelected = uri;
//                GlideApp.with(AddCategoryFragment.this).load(uri).diskCacheStrategy(DiskCacheStrategy.RESOURCE).thumbnail(0.2f).centerCrop().transform(new RoundedCorners(20)).into(ivLoadImage);

            }

            @Override
            public void onGallery() {
                rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                    if (aBoolean) {
                        OpenPickPhotoUtils.startPhotoPick(AddCategoryFragment.this);
                    }
                });
            }

            @Override
            public void onRemove() {
                photoSelected = null;
                ivLoadImage.setImageResource(R.drawable.camera);
            }
        });

        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
            if (aBoolean) {
                if (photoSelected != null)
                    photoPickDialog.showDialog(photoSelected);
                else photoPickDialog.showDialog();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (OpenPickPhotoUtils.RESULT_PICK_IMAGE == requestCode && RESULT_OK == resultCode && data.getData() != null) {
            Uri imageUri = data.getData();
            photoSelected = imageUri;
//            GlideApp.with(AddCategoryFragment.this).load(imageUri).diskCacheStrategy(DiskCacheStrategy.RESOURCE).thumbnail(0.2f).centerCrop().transform(new RoundedCorners(20)).into(ivLoadImage);
        }
    }

    public void backToMain() {
        getActivity().finish();
    }

    public void setFields(String name, String description, boolean active, String photoPath) {
        etCategoryName.setText(name);
        etCategoryDescription.setText(description);
        chbActive.setChecked(active);
        if(!photoPath.equals("")) {
            photoSelected = Uri.fromFile(new File(photoPath));
//            GlideApp.with(AddCategoryFragment.this).load(photoSelected).diskCacheStrategy(DiskCacheStrategy.RESOURCE).thumbnail(0.2f).centerCrop().transform(new RoundedCorners(20)).into(ivLoadImage);
        }
        else {
            photoSelected = null;
            ivLoadImage.setImageResource(R.drawable.camera);
        }
    }

    public void clearFields() {
        etCategoryName.setText("");
        etCategoryDescription.setText("");
        photoSelected = null;
        ivLoadImage.setImageResource(R.drawable.camera);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onDestroy();
    }

    public void setData() {
        presenter.checkData();
    }

    public void setError() {
        etCategoryName.setError("Such name already exists");
    }
}
