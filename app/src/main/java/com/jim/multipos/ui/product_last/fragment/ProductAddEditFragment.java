package com.jim.multipos.ui.product_last.fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.product.view.AddCategoryFragment;
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.ui.product_last.ProductPresenter;
import com.jim.multipos.utils.GlideApp;
import com.jim.multipos.utils.OpenPickPhotoUtils;
import com.jim.multipos.utils.PhotoPickDialog;

import butterknife.BindView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public class ProductAddEditFragment extends BaseFragment {

    @NotEmpty(messageId = R.string.name_validation)
    @BindView(R.id.etProductName)
    MpEditText name;

    @BindView(R.id.ivChooseImage)
    ImageView photoButton;

    @BindView(R.id.etBarcode)
    MpEditText barcode;

    @BindView(R.id.etSKU)
    MpEditText sku;

    @BindView(R.id.spUnit)
    MPosSpinner units;

    @BindView(R.id.spUnitCategories)
    MPosSpinner unitsCategory;

    @BindView(R.id.etProductPrice)
    MpEditText price;

    @BindView(R.id.tvPriceCurrency)
    TextView priceCurrency;

    @BindView(R.id.etProductCost)
    MpEditText cost;

    @BindView(R.id.tvCostCurrency)
    TextView costCurrency;

    @BindView(R.id.spProductClass)
    MPosSpinner productClass;

    @BindView(R.id.chbActive)
    MpCheckbox isActive;

    @BindView(R.id.btnCancel)
    MpButton cancel;

    @BindView(R.id.btnAdvance)
    MpButton advance;

    @BindView(R.id.btnSave)
    MpButton save;

    @BindView(R.id.tvVendor)
    TextView vendor;

    private int vendorSelectedPos = 0;
    private String photoPath = null;
    private Uri photoSelected;
    @Override
    protected int getLayout() {
        return R.layout.add_product_fragment;
    }

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {}

    public void initProductAddEditFragment(String[] unitCategoryList,
                                           String[] unitList,
                                           String[] productClasses,
                                           String currencyAbbr) {
        unitsCategory.setAdapter(unitCategoryList);
        unitsCategory.setItemSelectionListener((view, position) -> {
            ((ProductActivity)getContext()).getPresenter().unitCategorySelected(position);
        });
        units.setAdapter(unitList);
        productClass.setAdapter(productClasses);
        priceCurrency.setText(currencyAbbr);
        costCurrency.setText(currencyAbbr);
    }

    @OnClick(value = {R.id.btnSave, R.id.btnCancel, R.id.btnAdvance, R.id.ivChooseImage})
    public void buttonClick(View view) {
        ProductPresenter presenter = ((ProductActivity) getContext()).getPresenter();
        switch (view.getId()) {
            case R.id.btnSave:
                if (isValid()) {
                    ((ProductActivity) getContext()).getPresenter().addProduct(
                            name.getText().toString(),
                            Double.parseDouble(price.getText().toString().replaceAll(",", ".")),
                            Double.parseDouble(cost.getText().toString().replaceAll(",", ".")),
                            barcode.getText().toString(),
                            sku.getText().toString(),
                            null,
                            isActive.isChecked(),
                            0,
                            0,
                            0,
                            unitsCategory.getSelectedPosition(),
                            units.getSelectedPosition(),
                            0,
                            "Description"
                    );
                }
                break;

            case R.id.btnCancel:

                break;

            case R.id.btnAdvance:

                break;
            case R.id.ivChooseImage:
                PhotoPickDialog photoPickDialog = new PhotoPickDialog(getActivity(), new PhotoPickDialog.OnButtonsClickListner() {
                    @Override
                    public void onCameraShot(Uri uri) {
                        photoSelected = uri;
                        GlideApp.with(ProductAddEditFragment.this).load(uri).diskCacheStrategy(DiskCacheStrategy.RESOURCE).thumbnail(0.2f).centerCrop().transform(new RoundedCorners(20)).into(photoButton);

                    }

                    @Override
                    public void onGallery() {
                        ((ProductActivity) getContext()).getPermissions().request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                            if (aBoolean) {
                                OpenPickPhotoUtils.startPhotoPick(ProductAddEditFragment.this);
                            }
                        });
                    }

                    @Override
                    public void onRemove() {
                        photoSelected = null;
                        photoButton.setImageResource(R.drawable.camera);
                    }
                });
                ((ProductActivity) getContext()).getPermissions().request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (OpenPickPhotoUtils.RESULT_PICK_IMAGE == requestCode && RESULT_OK == resultCode && data.getData() != null) {
            Uri imageUri = data.getData();
            photoSelected = imageUri;
            GlideApp.with(ProductAddEditFragment.this)
                    .load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE).thumbnail(0.2f)
                    .centerCrop()
                    .transform(new RoundedCorners(20))
                    .into(photoButton);
        }
    }

    public void openAddMode() {
        name.setText("");
        name.setError(null);
        barcode.setText("");
        sku.setText("");
        price.setText("");
        cost.setText("");
        productClass.setSelection(0);
        units.setSelectedPosition(0);
        unitsCategory.setSelectedPosition(0);
        isActive.setChecked(true);
        save.setText(R.string.save);
    }

    public void openEditMode(String name,
                             double price,
                             double cost,
                             String barCode,
                             String sku,
                             boolean isActive,
                             String priceCurrencyName,
                             String costCurrencyName,
                             int productClassPos,
                             int unitCategoryPos,
                             String[] units,
                             int unitPos,
                             String vendorName,
                             String description) {
        this.name.setText(name);
        this.name.setError(null);
        this.price.setText(Double.toString(price));
        this.cost.setText(Double.toString(cost));
        this.barcode.setText(barCode);
        this.sku.setText(sku);
        this.isActive.setChecked(isActive);
        this.priceCurrency.setText(priceCurrencyName);
        this.costCurrency.setText(costCurrencyName);
        this.productClass.setSelectedPosition(productClassPos);
        this.unitsCategory.setSelectedPosition(unitCategoryPos);
        if (units != null) {
            this.units.setAdapter(units);
            this.units.setSelectedPosition(unitPos);
        }
        this.vendor.setText(vendorName);
        this.save.setText(R.string.update);
    }

    public void setUnits(String[] units) {
        this.units.setAdapter(units);
    }

    public String getProductName() {
        return name.getText().toString();
    }

    public String getBarCode() {
        return barcode.getText().toString();
    }

    public String getSku() {
        return sku.getText().toString();
    }

    public int getUnitCategorySelectedPos() {
        return unitsCategory.getSelectedPosition();
    }

    public int getUnitSelectedPos() {
        return units.getSelectedPosition();
    }

    public Double getPrice() {
        String temp = price.getText().toString();
        if (!temp.isEmpty()) {
            return Double.parseDouble(temp);
        }
        return 0.0d;
    }
    public Double getCost() {
        String temp = cost.getText().toString();
        if (!temp.isEmpty()) {
            return Double.parseDouble(temp);
        }
        return 0.0d;
    }

    public int getVendorSelectedPos() {
        return vendorSelectedPos;
    }

    public int getProductClassSelectedPos() {
        return productClass.getSelectedPosition();
    }

    public boolean getIsActive() {
        return isActive.isChecked();
    }

}
