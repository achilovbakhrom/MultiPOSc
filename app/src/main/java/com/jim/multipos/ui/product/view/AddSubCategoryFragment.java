package com.jim.multipos.ui.product.view;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.product.presenter.SubCategoryPresenter;
import com.jim.multipos.utils.CommonUtils;
//import com.jim.multipos.utils.GlideApp;
import com.jim.multipos.utils.GlideApp;
import com.jim.multipos.utils.OpenPickPhotoUtils;
import com.jim.multipos.utils.PhotoPickDialog;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CategoryEvent;
import com.jim.multipos.utils.rxevents.MessageEvent;
import com.jim.multipos.utils.rxevents.SubCategoryEvent;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

import static android.app.Activity.RESULT_OK;

/**
 * Created by DEV on 18.08.2017.
 */

public class AddSubCategoryFragment extends BaseFragment implements SubCategoryView {
    @Override
    public void setFields(String name, String description, boolean active, String photoPath) {

    }

    @Override
    public void clearFields() {

    }

    @Override
    public void setParentCategoryName(String parentCategory) {

    }

    @Override
    public void setData() {

    }

    @Override
    public void setError(String error) {

    }

    @Override
    protected int getLayout() {
        return 0;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void rxConnections() {

    }
/*@BindView(R.id.etSubCategoryName)
    MpEditText etSubCategoryName;
    @BindView(R.id.etSubCategoryDescription)
    EditText etSubCategoryDescription;
    @BindView(R.id.ivLoadSubCategoryImage)
    ImageView ivLoadImage;
    @BindView(R.id.tvChooseCategory)
    TextView tvChooseCategory;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @Inject
    SubCategoryPresenter presenter;
    @Inject
    RxPermissions rxPermissions;
    @Inject
    RxBusLocal rxBusLocal;
    @Inject
    RxBus rxBus;
    private String categoryName = "";
    private Uri photoSelected;
    private static final String CLICK = "click";
    private final static String FRAGMENT_OPENED = "subcategory";
    private final static String PARENT = "parent";
    private static final String ADD = "added";
    private static final String UPDATE = "update";
    ArrayList<Disposable> subscriptions;

    @Override
    protected int getLayout() {
        return R.layout.add_sub_category_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tvChooseCategory.setText(categoryName);
        rxBus.send(new MessageEvent(FRAGMENT_OPENED));
    }

    @Override
    protected void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBusLocal.toObservable().subscribe(o -> {
                    if (o instanceof SubCategoryEvent) {
                        SubCategoryEvent event = (SubCategoryEvent) o;
                        if (event.getEventType().equals(CLICK)) {
                            presenter.clickedSubCategory(event.getSubCategory());
                        }
                    }
                    if (o instanceof CategoryEvent) {
                        CategoryEvent event = (CategoryEvent) o;
                        if (event.getEventType().equals(PARENT)) {
                            presenter.setParentCategory(event.getCategory());
                        }
                    }
                }));
    }

    @OnClick(R.id.btnSubCategoryCancel)
    public void onBack() {
        getActivity().finish();
    }

    @OnClick(R.id.btnSubCategorySave)
    public void onSave() {
        presenter.save(etSubCategoryName.getText().toString(),
                etSubCategoryDescription.getText().toString(),
                chbActive.isCheckboxChecked(),
                (photoSelected != null) ? CommonUtils.getRealPathFromURI(getContext(), photoSelected) : "");
    }

    @OnClick(R.id.ivLoadSubCategoryImage)
    public void onLoadImage() {
        PhotoPickDialog photoPickDialog = new PhotoPickDialog(getContext(), new PhotoPickDialog.OnButtonsClickListner() {
            @Override
            public void onCameraShot(Uri uri) {
                photoSelected = uri;
                GlideApp.with(AddSubCategoryFragment.this).load(uri).diskCacheStrategy(DiskCacheStrategy.RESOURCE).thumbnail(0.2f).centerCrop().transform(new RoundedCorners(20)).into(ivLoadImage);
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

    @Override
    public void setFields(String name, String description, boolean active, String photoPath) {
        etSubCategoryName.setText(name);
        etSubCategoryDescription.setText(description);
        chbActive.setChecked(active);
        if (!photoPath.equals("")) {
            photoSelected = Uri.fromFile(new File(photoPath));
            GlideApp.with(AddSubCategoryFragment.this).load(photoSelected).diskCacheStrategy(DiskCacheStrategy.RESOURCE).thumbnail(0.2f).centerCrop().transform(new RoundedCorners(20)).into(ivLoadImage);
        } else {
            photoSelected = null;
            ivLoadImage.setImageResource(R.drawable.camera);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.removeListners(subscriptions);
    }

    @Override
    public void setData() {
        presenter.checkData();
    }

    @Override
    public void setError(String error) {
        etSubCategoryName.setError(error);
    }

    @Override
    public void sendEvent(SubCategory subCategory, String event) {
        switch (event) {
            case ADD:
                rxBus.send(new SubCategoryEvent(subCategory, ADD));
                break;
            case UPDATE:
                rxBus.send(new SubCategoryEvent(subCategory, UPDATE));
                break;
        }
    }


    @Override
    public void clearFields() {
        etSubCategoryName.setText("");
        etSubCategoryDescription.setText("");
        photoSelected = null;
        ivLoadImage.setImageResource(R.drawable.camera);
    }

    @Override
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
            GlideApp.with(AddSubCategoryFragment.this).load(imageUri).diskCacheStrategy(DiskCacheStrategy.RESOURCE).thumbnail(0.2f).centerCrop().transform(new RoundedCorners(20)).into(ivLoadImage);
        }
    }*/
}
