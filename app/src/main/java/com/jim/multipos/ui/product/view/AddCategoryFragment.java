package com.jim.multipos.ui.product.view;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.ui.product.presenter.CategoryPresenter;

import com.jim.multipos.utils.CommonUtils;

//import com.jim.multipos.utils.GlideApp;
import com.jim.multipos.utils.OpenPickPhotoUtils;
import com.jim.multipos.utils.PhotoPickDialog;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CategoryEvent;
import com.jim.multipos.utils.rxevents.MessageEvent;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

import static android.app.Activity.RESULT_OK;


/**
 * Created by DEV on 09.08.2017.
 */

public class AddCategoryFragment extends BaseFragment implements CategoryView {

    @BindView(R.id.etCategoryName)
    MpEditText etCategoryName;
    @BindView(R.id.etCategoryDescription)
    EditText etCategoryDescription;
    @BindView(R.id.ivLoadImage)
    ImageView ivLoadImage;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @Inject
    RxPermissions rxPermissions;
    @Inject
    CategoryPresenter presenter;
    @Inject
    RxBusLocal rxBusLocal;
    @Inject
    RxBus rxBus;
    private Uri photoSelected;
    private static final String CLICK = "click";
    private final static String FRAGMENT_OPENED = "category";
    private static final String ADD = "added";
    private static final String UPDATE = "update";
    ArrayList<Disposable> subscriptions;

    @Override
    protected int getLayout() {
        return R.layout.add_category_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.checkData();
        presenter.isVisible(isVisible());
        rxBusLocal.send(new MessageEvent(FRAGMENT_OPENED));
    }

    @Override
    protected void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBusLocal.toObservable().subscribe(o -> {
                    if (o instanceof CategoryEvent) {
                        CategoryEvent event = (CategoryEvent) o;
                        if (event.getEventType().equals(CLICK)) {
                            presenter.clickedCategory(event.getCategory());
                        }
                    }
                }));
    }

    @OnClick(R.id.btnCategoryCancel)
    public void onBack() {
        getActivity().finish();
    }

    @OnClick(R.id.btnCategorySave)
    public void onSave() {
        presenter.saveCategory(etCategoryName.getText().toString(),
                etCategoryDescription.getText().toString(),
                chbActive.isCheckboxChecked(),
                (photoSelected != null) ? CommonUtils.getRealPathFromURI(getContext(), photoSelected) : "");

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

    @Override
    public void setFields(String name, String description, boolean active, String photoPath) {
        etCategoryName.setText(name);
        etCategoryDescription.setText(description);
        chbActive.setChecked(active);
        if (!photoPath.equals("")) {
            photoSelected = Uri.fromFile(new File(photoPath));
//            GlideApp.with(AddCategoryFragment.this).load(photoSelected).diskCacheStrategy(DiskCacheStrategy.RESOURCE).thumbnail(0.2f).centerCrop().transform(new RoundedCorners(20)).into(ivLoadImage);
        } else {
            photoSelected = null;
            ivLoadImage.setImageResource(R.drawable.camera);
        }
    }

    @Override
    public void clearFields() {
        etCategoryName.setText("");
        etCategoryDescription.setText("");
        photoSelected = null;
        ivLoadImage.setImageResource(R.drawable.camera);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.removeListners(subscriptions);
        presenter.onDestroy();
    }

    @Override
    public void setData() {
        presenter.checkData();
    }

    @Override
    public void setError(String error) {
        etCategoryName.setError(error);
    }

    @Override
    public void sendEvent(Category category, String event) {
        switch (event) {
            case ADD:
                rxBus.send(new CategoryEvent(category, ADD));
                break;
            case UPDATE:
                rxBus.send(new CategoryEvent(category, UPDATE));
                break;
        }
    }

}
