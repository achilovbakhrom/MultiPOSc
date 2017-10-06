package com.jim.multipos.ui.products.fragments;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.products.ProductsActivity;
import com.jim.multipos.ui.products.di.ProductsComponent;
import com.jim.multipos.ui.products.presenters.SubCategoryPresenter;
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
 * Created by DEV on 18.08.2017.
 */

public class AddSubCategoryFragment extends Fragment { // BaseFragment implements SubCategoryView {
    @BindView(R.id.etSubCategoryName)
    MpEditText etSubCategoryName;
    @BindView(R.id.etSubCategoryDescription)
    EditText etSubCategoryDescription;
    @BindView(R.id.btnSubCategoryCancel)
    MpButton btnCancel;
    @BindView(R.id.btnSubCategorySave)
    MpButton btnSave;
    @BindView(R.id.ivLoadSubCategoryImage)
    ImageView ivLoadImage;
    @BindView(R.id.tvChooseCategory)
    TextView tvChooseCategory;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @Inject
    PosFragmentManager posFragmentManager;
    @Inject
    RxBus rxBus;
    @Inject
    SubCategoryPresenter presenter;
    @Inject
    ProductsActivity activity;
    @Inject
    RxPermissions rxPermissions;
    private Unbinder unbinder;
    private String categoryName = "";
    private Uri photoSelected;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_sub_category_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
//        this.getComponent(ProductsComponent.class).inject(this);
//        presenter.init(this);
        presenter.checkData();
        tvChooseCategory.setText(categoryName);
        return view;
    }

    @OnClick(R.id.btnSubCategoryCancel)
    public void onBack() {
        presenter.back();
    }

    @OnClick(R.id.btnSubCategorySave)
    public void onSave() {
        if (!etSubCategoryName.getText().toString().isEmpty()) {
            if (etSubCategoryName.getText().length() > 3)
                presenter.save(etSubCategoryName.getText().toString(),
                        etSubCategoryDescription.getText().toString(),
                        chbActive.isCheckboxChecked()
                        , (photoSelected != null) ? CommonUtils.getRealPathFromURI(getContext(), photoSelected) : "");
            else etSubCategoryName.setError("Name length must be greater than 3");
        } else {
            etSubCategoryName.setError("Please, enter subcategory name");
        }
    }

    @OnClick(R.id.ivLoadSubCategoryImage)
    public void onLoadImage() {
        PhotoPickDialog photoPickDialog = new PhotoPickDialog(getContext(), new PhotoPickDialog.OnButtonsClickListner() {
            @Override
            public void onCameraShot(Uri uri) {
                photoSelected = uri;
//                GlideApp.with(AddSubCategoryFragment.this).load(uri).diskCacheStrategy(DiskCacheStrategy.RESOURCE).thumbnail(0.2f).centerCrop().transform(new RoundedCorners(20)).into(ivLoadImage);

            }

            @Override
            public void onGallery() {
                rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                    if (aBoolean) {
                        OpenPickPhotoUtils.startPhotoPick(AddSubCategoryFragment.this);
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

    public void backToMain() {
        getActivity().finish();
    }

    public void setFields(String name, String description, boolean active, String photoPath) {
        etSubCategoryName.setText(name);
        etSubCategoryDescription.setText(description);
        chbActive.setChecked(active);
        if(!photoPath.equals("")) {
            photoSelected = Uri.fromFile(new File(photoPath));
//            GlideApp.with(AddSubCategoryFragment.this).load(photoSelected).diskCacheStrategy(DiskCacheStrategy.RESOURCE).thumbnail(0.2f).centerCrop().transform(new RoundedCorners(20)).into(ivLoadImage);
        }
        else {
            photoSelected = null;
            ivLoadImage.setImageResource(R.drawable.camera);
        }
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
        etSubCategoryName.setError("Such name already exists");
    }


    public void clearFields() {
        etSubCategoryName.setText("");
        etSubCategoryDescription.setText("");
        photoSelected = null;
        ivLoadImage.setImageResource(R.drawable.camera);
    }

    public void setParentCategoryName(String parentCategoryName) {
        tvChooseCategory.setText(parentCategoryName);
        categoryName = parentCategoryName;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (OpenPickPhotoUtils.RESULT_PICK_IMAGE == requestCode && RESULT_OK == resultCode && data.getData() != null) {
            Uri imageUri = data.getData();
            photoSelected = imageUri;
//            GlideApp.with(AddSubCategoryFragment.this).load(imageUri).diskCacheStrategy(DiskCacheStrategy.RESOURCE).thumbnail(0.2f).centerCrop().transform(new RoundedCorners(20)).into(ivLoadImage);
        }
    }
}
