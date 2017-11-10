package com.jim.multipos.ui.product_last.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpSearchView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.data.db.model.Vendor;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.product.view.AddCategoryFragment;
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.ui.product_last.ProductPresenter;
import com.jim.multipos.utils.GlideApp;
import com.jim.multipos.utils.OpenPickPhotoUtils;
import com.jim.multipos.utils.PhotoPickDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public class ProductAddEditFragment extends BaseFragment implements View.OnClickListener{

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

    private static final String VENDOR_LIST_COUNT = "VENDOR_LIST_COUNT";
    private static final String VENDOR_ID = "VENDOR_ID_";
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

    private Dialog dialog;

    private MpSearchView vendorDialogSearchView;
    private RecyclerView vendorDialogList;
    private MpButton vendorDialogBack;
    private MpButton vendorDialogOk;

    private List<Long> vendors;

    @Override
    protected void init(Bundle savedInstanceState) {
        vendors = new ArrayList<>();
        if (savedInstanceState != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            int count = preferences.getInt(VENDOR_LIST_COUNT, 0);
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    vendors.add(preferences.getLong(VENDOR_ID + i, -1L));
                }
            }
        }

        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.choose_vendor_dialog, null, false);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.setContentView(dialogView);
        vendorDialogSearchView = (MpSearchView) dialogView.findViewById(R.id.searchView);
        vendorDialogList = (RecyclerView) dialogView.findViewById(R.id.rvVendors);
        vendorDialogBack = (MpButton) dialogView.findViewById(R.id.btnBack);
        vendorDialogBack.setOnClickListener(this);
        vendorDialogOk = (MpButton) dialogView.findViewById(R.id.btnOk);
        vendorDialogOk.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        preferences.edit().putInt(VENDOR_LIST_COUNT, vendors.size()).apply();
        for (int i = 0; i < vendors.size(); i++) {
            preferences.edit().putLong(VENDOR_ID + i, vendors.get(i)).apply();
        }
    }

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



    @OnClick(value = {R.id.btnSave, R.id.btnCancel, R.id.btnAdvance, R.id.ivChooseImage, R.id.tvVendor})
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
            case R.id.tvVendor:
                ((ProductActivity) getContext()).getPresenter().openVendorChooserDialog();
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
                             List<Long> vendors,
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
        this.vendors = vendors;
    }

    public void openVendorChooserDialog(List<Vendor> vendorList) {
        List<VendorWithCheckedPosition> result = new ArrayList<>();
        for (Vendor vendor : vendorList) {
            VendorWithCheckedPosition vendorWithCheckedPosition = new VendorWithCheckedPosition();
            vendorWithCheckedPosition.setVendor(vendor);
            vendorWithCheckedPosition.setChecked(containsVendor(vendor.getId()));
            result.add(vendorWithCheckedPosition);
        }
        VendorChooseAdapter adapter = new VendorChooseAdapter(result);
        vendorDialogList.setAdapter(adapter);
        dialog.show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:

            case R.id.btnBack:
                dialog.dismiss();
                break;
        }
    }

    private boolean containsVendor(Long vendorId) {
        for (Long id : vendors) {
            if (id.equals(vendorId)) {
                return true;
            }
        }
        return false;
    }

    class VendorChooseAdapter extends BaseAdapter<VendorWithCheckedPosition, VendorChooseAdapter.VendorChooseItemHolder> {

        public VendorChooseAdapter(List<VendorWithCheckedPosition> items) {
            super(items);
        }

        @Override
        public VendorChooseItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_vendor_dialog_item, parent, false);
            return new VendorChooseItemHolder(view);
        }

        @Override
        public void onBindViewHolder(VendorChooseItemHolder holder, int position) {
            holder.vendorName.setText(items.get(position).getVendor().getName());
            holder.vendorContact.setText(items.get(position).getVendor().getContactName());
            holder.vendorAddress.setText(items.get(position).getVendor().getAddress());
            holder.vendorChecked.setChecked(items.get(position).isChecked());
        }

        class VendorChooseItemHolder extends BaseViewHolder {
            @BindView(R.id.tvVendorName)
            TextView vendorName;
            @BindView(R.id.tvVendorContact)
            TextView vendorContact;
            @BindView(R.id.tvVendorAddress)
            TextView vendorAddress;
            @BindView(R.id.chbVendorChecked)
            MpCheckbox vendorChecked;

            VendorChooseItemHolder(View itemView) {
                super(itemView);
            }
        }
    }

    class VendorWithCheckedPosition implements Serializable {
        private Vendor vendor;
        private boolean isChecked;
        public Vendor getVendor() {
            return vendor;
        }
        public void setVendor(Vendor vendor) {
            this.vendor = vendor;
        }
        public boolean isChecked() {
            return isChecked;
        }
        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }
}
