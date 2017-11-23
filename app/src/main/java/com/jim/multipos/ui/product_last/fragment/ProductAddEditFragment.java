package com.jim.multipos.ui.product_last.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.ui.product_last.ProductPresenter;
import com.jim.multipos.ui.product_last.adapter.ProductClassListAdapter;
import com.jim.multipos.ui.product_last.adapter.ProductCostListAdapter;
import com.jim.multipos.utils.CommonUtils;
import com.jim.multipos.utils.GlideApp;
import com.jim.multipos.utils.OpenPickPhotoUtils;
import com.jim.multipos.utils.PhotoPickDialog;
import com.jim.multipos.utils.UIUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

import static android.app.Activity.RESULT_OK;
import static com.jim.multipos.utils.OpenPickPhotoUtils.RESULT_PICK_IMAGE;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public class ProductAddEditFragment extends BaseFragment implements View.OnClickListener {

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

    @NotEmpty(messageId = R.string.warning_price_empty)
    @BindView(R.id.etProductPrice)
    MpEditText price;

    @BindView(R.id.tvPriceCurrency)
    TextView priceCurrency;

    @NotEmpty(messageId = R.string.warning_cost_empty)
    @BindView(R.id.etProductCost)
    TextView cost;

    @BindView(R.id.tvCostCurrency)
    TextView costCurrency;

    @BindView(R.id.tvProductClass)
    TextView tvProductClass;

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
    private Uri photoSelected;

    @Override
    protected int getLayout() {
        return R.layout.add_product_fragment;
    }

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    private Dialog dialog, costDialog, classDialog;

    private RecyclerView vendorDialogList, productCostList, classList;
    private MpButton vendorDialogBack, btnBackToAddProduct, btnSaveCosts;
    private MpButton vendorDialogOk;
    private ProductClassListAdapter classListAdapter;

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
        vendorDialogList = (RecyclerView) dialogView.findViewById(R.id.rvVendors);
        vendorDialogList.setLayoutManager(new LinearLayoutManager(getContext()));
        vendorDialogBack = (MpButton) dialogView.findViewById(R.id.btnBack);
        vendorDialogBack.setOnClickListener(this);
        vendorDialogOk = (MpButton) dialogView.findViewById(R.id.btnOk);
        vendorDialogOk.setOnClickListener(this);

        costDialog = new Dialog(getContext());
        View costView = LayoutInflater.from(getContext()).inflate(R.layout.choose_product_cost, null, false);
        costDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        costDialog.setContentView(costView);
        productCostList = (RecyclerView) costView.findViewById(R.id.rvProductCostList);
        productCostList.setLayoutManager(new LinearLayoutManager(getContext()));
        btnBackToAddProduct = (MpButton) costView.findViewById(R.id.btnBackToAddProduct);
        btnBackToAddProduct.setOnClickListener(this);
        btnSaveCosts = (MpButton) costView.findViewById(R.id.btnSaveCosts);
        btnSaveCosts.setOnClickListener(this);

        classDialog = new Dialog(getContext());
        View classView = LayoutInflater.from(getContext()).inflate(R.layout.vendor_product_list_dialog, null, false);
        classList = (RecyclerView) classView.findViewById(R.id.rvProductList);
        TextView title = (TextView) classView.findViewById(R.id.tvDialogTitle);
        title.setText(R.string.choose_product_class);
        classList.setLayoutManager(new LinearLayoutManager(getContext()));
        classListAdapter = new ProductClassListAdapter(getContext());
        classList.setAdapter(classListAdapter);
        classDialog.setContentView(classView);
        classDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        classListAdapter.setListeners(productClass -> {
            ((ProductActivity) getContext()).getPresenter().setProductClass(productClass);
            if (productClass != null)
                tvProductClass.setText(productClass.getName());
            else tvProductClass.setText(getString(R.string.not_classified));
            classDialog.dismiss();
        });
        ((ProductActivity) getContext()).getPresenter().initDataForProduct();
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
                                           List<ProductClass> productClasses,
                                           String currencyAbbr) {
        unitsCategory.setAdapter(unitCategoryList);
        unitsCategory.setItemSelectionListener((view, position) -> {
            ((ProductActivity) getContext()).getPresenter().unitCategorySelected(position);
        });
        units.setAdapter(unitList);
        classListAdapter.setData(productClasses);
        classList.setAdapter(classListAdapter);
        priceCurrency.setText(currencyAbbr);
        costCurrency.setText(currencyAbbr);
    }


    @OnClick(value = {R.id.btnSave, R.id.btnCancel, R.id.btnAdvance, R.id.ivChooseImage, R.id.tvVendor, R.id.etProductCost, R.id.tvProductClass})
    public void buttonClick(View view) {
        ProductPresenter presenter = ((ProductActivity) getContext()).getPresenter();
        switch (view.getId()) {
            case R.id.btnSave:
                if (isValid())
                    ((ProductActivity) getContext()).getPresenter().comparePriceWithCost(Double.parseDouble(this.price.getText().toString()));
                break;
            case R.id.tvVendor:
                ((ProductActivity) getContext()).getPresenter().openVendorChooserDialog();
                break;
            case R.id.etProductCost:
                ((ProductActivity) getContext()).getPresenter().setProductCostDialog();
                break;
            case R.id.btnCancel:
                ((ProductActivity) getContext()).getPresenter().finishActivity();
                break;
            case R.id.btnAdvance:
                presenter.deleteProduct();
                break;
            case R.id.tvProductClass:
                classDialog.show();
                break;
            case R.id.ivChooseImage:
                PhotoPickDialog photoPickDialog = new PhotoPickDialog(getActivity(), new PhotoPickDialog.OnButtonsClickListner() {
                    @Override
                    public void onCameraShot(Uri uri) {
                        photoSelected = uri;
                        GlideApp
                                .with(ProductAddEditFragment.this)
                                .load(uri)
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .thumbnail(0.2f)
                                .centerCrop()
                                .transform(new RoundedCorners(20))
                                .into(photoButton);
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

    public void saveProduct(boolean isBigger) {
        if (isBigger) {
            UIUtils.showAlert(getContext(), getString(R.string.yes), getString(R.string.no),
                    getString(R.string.warning), getString(R.string.warning_cost_more_than_price), new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            if (vendors == null || vendors.isEmpty()) {
                                vendor.setTextColor(Color.RED);
                                return;
                            }
                            String tempPrice = price.getText().toString().replace(",",  ".");
                            Double resultPrice = tempPrice.isEmpty() ? 0.0d : Double.parseDouble(tempPrice);
                            ((ProductActivity) getContext()).getPresenter().addProduct(
                                    name.getText().toString(),
                                    barcode.getText().toString(),
                                    sku.getText().toString(),
                                    (photoSelected != null) ? CommonUtils.getRealPathFromURI(getContext(), photoSelected) : "",
                                    isActive.isChecked(),
                                    0,
                                    0,
                                    unitsCategory.getSelectedPosition(),
                                    units.getSelectedPosition(),
                                    vendors,
                                    "Description",
                                    resultPrice
                            );
                        }

                        @Override
                        public void onNegativeButtonClicked() {

                        }
                    });
        } else {
            if (vendors == null || vendors.isEmpty()) {
                vendor.setTextColor(Color.RED);
                return;
            }
            String tempPrice = price.getText().toString().replace(",", ".");
            Double resultPrice = tempPrice.isEmpty() ? 0.0d : Double.parseDouble(tempPrice);
            ((ProductActivity) getContext()).getPresenter().addProduct(
                    name.getText().toString(),
                    barcode.getText().toString(),
                    sku.getText().toString(),
                    (photoSelected != null) ? CommonUtils.getRealPathFromURI(getContext(), photoSelected) : "",
                    isActive.isChecked(),
                    0,
                    0,
                    unitsCategory.getSelectedPosition(),
                    units.getSelectedPosition(),
                    vendors,
                    "Description",
                    resultPrice
            );
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_PICK_IMAGE == requestCode && RESULT_OK == resultCode && data.getData() != null) {
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
        vendors = new ArrayList<>();
        name.setText("");
        name.setError(null);
        barcode.setText("");
        sku.setText("");
        price.setText("");
        price.setError(null);
        cost.setText("");
        cost.setError(null);
        vendor.setText(getString(R.string.vendor_are_not_choosed));
        tvProductClass.setText(getString(R.string.not_classified));
        units.setSelectedPosition(0);
        unitsCategory.setSelectedPosition(0);
        isActive.setChecked(true);
        save.setText(R.string.save);
        photoSelected = null;
        photoButton.setImageResource(R.drawable.camera);
    }

    public void openEditMode(String name,
                             String barCode,
                             String sku,
                             boolean isActive,
                             String priceCurrencyName,
                             String costCurrencyName,
                             String productClassPos,
                             int unitCategoryPos,
                             String[] units,
                             int unitPos,
                             List<Long> vendors,
                             String description,
                             String url,
                             double price) {
        this.name.setText(name);
        this.name.setError(null);
        this.price.setText(String.valueOf(price));
        this.barcode.setText(barCode);
        this.sku.setText(sku);
        this.isActive.setChecked(isActive);
        this.priceCurrency.setText(priceCurrencyName);
        this.costCurrency.setText(costCurrencyName);
        if (productClassPos.equals(""))
            this.tvProductClass.setText(getString(R.string.not_classified));
        else
            this.tvProductClass.setText(productClassPos);
        this.unitsCategory.setSelectedPosition(unitCategoryPos);
        if (url != null && !url.equals("")) {
            photoSelected = Uri.fromFile(new File(url));
            GlideApp
                    .with(ProductAddEditFragment.this)
                    .load(photoSelected)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .thumbnail(0.2f)
                    .centerCrop()
                    .transform(new RoundedCorners(20))
                    .into(photoButton);
        } else {
            photoSelected = null;
            photoButton.setImageResource(R.drawable.camera);
        }

        if (units != null) {
            this.units.setAdapter(units);
            this.units.setSelectedPosition(unitPos);
        }
        ((ProductActivity) getContext()).getPresenter().setVendorName(vendors);
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

    public void openChooseProductCostDialog(List<String> vendors, List<VendorProductCon> costs) {
        if (vendors.size() != 0) {
            ProductCostListAdapter adapter = new ProductCostListAdapter(getContext());
            adapter.setData(vendors, costs);
            productCostList.setAdapter(adapter);
            costDialog.show();
        } else
            UIUtils.showAlert(getContext(), getContext().getString(R.string.ok), getContext().getString(R.string.warning), getContext().getString(R.string.choose_vendor), () -> {});
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

    public String getCost() {
        String temp = cost.getText().toString();
        if (!temp.isEmpty()) {
            return temp;
        }
        return "";
    }

    public String getProductClassSelectedPos() {
        return tvProductClass.getText().toString();
    }

    public boolean getIsActive() {
        return isActive.isChecked();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                if (vendorDialogList.getAdapter() != null) {
                    VendorChooseAdapter adapter = (VendorChooseAdapter) vendorDialogList.getAdapter();
                    vendors.clear();
                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        if (adapter.isVendorChecked(i)) {
                            vendors.add(adapter.getVendorId(i));
                        }
                    }
                    ((ProductActivity) getContext()).getPresenter().setVendorName(vendors);
                }
            case R.id.btnBack:
                dialog.dismiss();
                break;
            case R.id.btnBackToAddProduct:
                costDialog.dismiss();
                break;
            case R.id.btnSaveCosts:
                if (productCostList.getAdapter() != null) {
                    ProductCostListAdapter adapter = (ProductCostListAdapter) productCostList.getAdapter();
                    ((ProductActivity) getContext()).getPresenter().setProductCosts(adapter.getCosts());
                }
                costDialog.dismiss();
                break;
        }
    }

    public void setVendorName(String vendors) {
        if (!vendors.isEmpty()) {
            this.vendor.setTextColor(Color.BLACK);
            this.vendor.setText(vendors);
        } else {
            this.vendor.setTextColor(Color.RED);
            this.vendor.setText(R.string.vendor_are_not_choosed);
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

    public List<Long> getVendors() {
        return vendors;
    }

    public void setCostValue(String result) {
        cost.setText(result);
    }

    public String getPhotoPath() {
        return (photoSelected != null) ? CommonUtils.getRealPathFromURI(getContext(), photoSelected) : "";
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
            holder.vendorChecked.setCheckedChangeListener(isChecked -> {
                items.get(position).setChecked(isChecked);
                if (vendors == null) return;
                if (isChecked) {
                    vendors.add(items.get(position).getVendor().getId());
                } else {
                    vendors.remove(items.get(position).getVendor().getId());
                }
            });
        }

        public boolean isVendorChecked(int position) {
            return items.get(position).isChecked();
        }

        public Long getVendorId(int position) {
            return items.get(position).getVendor().getId();
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
                itemView.setOnClickListener(v -> vendorChecked.setChecked(!vendorChecked.isChecked()));
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
