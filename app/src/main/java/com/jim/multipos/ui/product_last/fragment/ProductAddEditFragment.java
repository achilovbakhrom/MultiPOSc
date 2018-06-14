package com.jim.multipos.ui.product_last.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.ui.product_class_new.ProductsClassActivity;
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.ui.product_last.ProductPresenter;
import com.jim.multipos.ui.product_last.adapter.ProductClassListAdapter;
import com.jim.multipos.utils.CommonUtils;
import com.jim.multipos.utils.GlideApp;
import com.jim.multipos.utils.NumberTextWatcher;
import com.jim.multipos.utils.OpenPickPhotoUtils;
import com.jim.multipos.utils.PhotoPickDialog;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;
import com.jim.multipos.utils.rxevents.product_events.ProductClassEvent;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

import static android.app.Activity.RESULT_OK;
import static com.jim.multipos.utils.OpenPickPhotoUtils.RESULT_PICK_IMAGE;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public class ProductAddEditFragment extends BaseFragment {

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

    @BindView(R.id.ivScanBarcode)
    ImageView ivScanBarcode;

    private Uri photoSelected;
    private ArrayList<Disposable> subscriptions;

    @Override
    protected int getLayout() {
        return R.layout.add_product_fragment;
    }

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    private Dialog classDialog;

    private RecyclerView classList;
    private ProductClassListAdapter classListAdapter;
    private DecimalFormat formatter;

    @Override
    protected void init(Bundle savedInstanceState) {
        argumentsRead = true;
        formatter = BaseAppModule.getFormatter();
        price.addTextChangedListener(new NumberTextWatcher(price));

        classDialog = new Dialog(getContext());
        View classView = LayoutInflater.from(getContext()).inflate(R.layout.vendor_product_list_dialog, null, false);
        classList = classView.findViewById(R.id.rvProductList);
        TextView title = classView.findViewById(R.id.tvDialogTitle);
        title.setText(getContext().getString(R.string.select_product_class));
        Button btnAddClass = classView.findViewById(R.id.btnAdd);
        btnAddClass.setVisibility(View.VISIBLE);
        btnAddClass.setOnClickListener(view -> {
            getContext().startActivity(new Intent(getContext(), ProductsClassActivity.class));
            classDialog.dismiss();
        });
        classList.setLayoutManager(new LinearLayoutManager(getContext()));
        classListAdapter = new ProductClassListAdapter(getContext());
        classList.setAdapter(classListAdapter);
        classDialog.setContentView(classView);
        classDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        classListAdapter.setListeners(productClass -> {
            ((ProductActivity) getContext()).getPresenter().setProductClass(productClass);
            if (productClass != null)
                tvProductClass.setText(productClass.getName());
            else tvProductClass.setText(getContext().getString(R.string.not_classified));
            classDialog.dismiss();
        });
        ((ProductActivity) getContext()).getPresenter().initDataForProduct();
        ivScanBarcode.setOnClickListener(view -> scan());
        ((ProductActivity) getActivity()).getBarcodeStack().register(barcode1 -> {
            if (isAdded() && isVisible())
                barcode.setText(barcode1);
        });
    }

    @Override
    public void onDetach() {
        ((ProductActivity) getActivity()).getBarcodeStack().unregister();
        super.onDetach();
    }

    @Override
    protected void rxConnections() {
        super.rxConnections();
        subscriptions = new ArrayList<>();
        subscriptions.add(
                ((ProductActivity) getContext()).getRxBus().toObservable().subscribe(o -> {
                    if (o instanceof ProductClassEvent) {
                        ProductClassEvent event = (ProductClassEvent) o;
                        switch (event.getType()) {
                            case GlobalEventConstants.DELETE:
                            case GlobalEventConstants.UPDATE:
                            case GlobalEventConstants.ADD: {
                                classListAdapter.setData(((ProductActivity) getContext()).getPresenter().updateProductClass());
                                classList.setAdapter(classListAdapter);
                                classDialog.show();
                                break;
                            }
                        }
                    }
                }));
    }

    private void scan() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    int unitPosition = 0;

    public void initProductAddEditFragment(String[] unitCategoryList,
                                           String[] unitList,
                                           List<ProductClass> productClasses,
                                           String currencyAbbr) {
        unitsCategory.setAdapter(unitCategoryList);
        unitsCategory.setItemSelectionListener((view, position) -> {
            if (categoryPos != position) {
                ((ProductActivity) getContext()).getPresenter().unitCategorySelected(position);
            } else {
                ((ProductActivity) getContext()).getPresenter().unitCategorySelectedWithPosition(position, unitPosition);
                unitPosition = 0;
            }
        });
        units.setAdapter(unitList);
        classListAdapter.setData(productClasses);
        classList.setAdapter(classListAdapter);
        priceCurrency.setText(currencyAbbr);
    }


    @OnClick(value = {R.id.btnSave, R.id.btnCancel, R.id.btnAdvance, R.id.ivChooseImage, R.id.tvProductClass})
    public void buttonClick(View view) {
        ProductPresenter presenter = ((ProductActivity) getContext()).getPresenter();
        switch (view.getId()) {
            case R.id.btnSave:
                if (name.getText().toString().isEmpty()) {
                    name.setError(getContext().getString(R.string.name_validation));
                } else if (price.getText().toString().isEmpty()) {
                    price.setError(getContext().getString(R.string.warning_price_empty));
                } else {
                    if (presenter.isProductNameExists(name.getText().toString())) {
                        name.setError(getContext().getString(R.string.such_product_name_exists));
                        return;
                    }
                    if (presenter.isProductSkuExists(sku.getText().toString())) {
                        sku.setError(getString(R.string.such_product_sku_exists));
                        return;
                    }
                    saveProduct();
                }
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
        }

    }

    public void saveProduct() {
            String tempPrice = price.getText().toString().replace(",", ".");
            Double resultPrice;
            try {
                resultPrice = tempPrice.isEmpty() ? 0.0d : formatter.parse(tempPrice).doubleValue();
            } catch (ParseException e) {
                resultPrice = 0.0d;
            }
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
                    getContext().getString(R.string.description),
                    resultPrice
            );
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
                    .into(photoButton);

        }

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                barcode.setText(intentResult.getContents());
            }
        }
    }

    boolean argumentsRead = true;

    public void openAddMode() {
        name.setText("");
        name.setError(null);
        barcode.setText("");
        sku.setText("");
        price.setText("");
        price.setError(null);
        tvProductClass.setText(getString(R.string.not_classified));
        units.setSelectedPosition(0);
        unitsCategory.setSelectedPosition(0);
        isActive.setChecked(true);
        save.setText(R.string.save);
        photoSelected = null;
        photoButton.setImageResource(R.drawable.camera);
        if (getArguments() != null && argumentsRead) {
            String barcodeText = getArguments().getString("PRODUCT_BARCODE");
            barcode.setText(barcodeText);
            argumentsRead = false;
        }
    }

    int categoryPos = 0;

    public void openEditMode(String name,
                             String barCode,
                             String sku,
                             boolean isActive,
                             String priceCurrencyName,
                             String productClassPos,
                             int unitCategoryPos,
                             String[] units,
                             int unitPos,
                             String description,
                             String url,
                             double price) {
        categoryPos = unitCategoryPos;

        this.name.setText(name);
        this.name.setError(null);
        this.price.setText(formatter.format(price));
        this.barcode.setText(barCode);
        this.sku.setText(sku);
        this.isActive.setChecked(isActive);
        this.priceCurrency.setText(priceCurrencyName);
        if (productClassPos.equals(""))
            this.tvProductClass.setText(getContext().getString(R.string.not_classified));
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
                    .into(photoButton);
        } else {
            photoSelected = null;
            photoButton.setImageResource(R.drawable.camera);
        }

        if (units != null) {
            this.units.setAdapter(units);
            this.units.setSelectedPosition(unitPos);
            unitPosition = unitPos;
        }
        this.save.setText(getContext().getString(R.string.update));
    }

    public void setUnits(String[] units, int unitPos) {
        this.units.setAdapter(units);
        this.units.setSelectedPosition(unitPos);
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
            try {
                return formatter.parse(temp).doubleValue();
            } catch (ParseException e) {
                return 0.0d;
            }
        }
        return 0.0d;
    }

    public String getProductClassSelectedPos() {
        return tvProductClass.getText().toString();
    }

    public boolean getIsActive() {
        return isActive.isChecked();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.removeListners(subscriptions);
    }

    public String getPhotoPath() {
        return (photoSelected != null) ? CommonUtils.getRealPathFromURI(getContext(), photoSelected) : "";
    }

    public void showCannotDeleteItemWithPlusValue(double value) {
        UIUtils.showAlert(getContext(), getContext().getString(R.string.ok), getContext().getString(R.string.warning),
                getContext().getString(R.string.you_have) + value + getContext().getString(R.string.item_in_the_inventory_you_cant_delete), () -> Log.d("sss", "onButtonClicked: "));
    }

    public void showCannotDeleteItemWithMinusValue(double value) {
        UIUtils.showAlert(getContext(), getContext().getString(R.string.ok), getContext().getString(R.string.warning),
                getContext().getString(R.string.you_have) + value + getContext().getString(R.string.item_in_the_inventory_you_cant_delete), () -> Log.d("sss", "onButtonClicked: "));
    }

    public void setBarcode(String barcode) {
        this.barcode.setText(barcode);
    }
}
