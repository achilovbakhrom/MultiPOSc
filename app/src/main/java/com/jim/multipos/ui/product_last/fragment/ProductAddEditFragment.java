package com.jim.multipos.ui.product_last.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.Vendor;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.ui.product_last.ProductPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

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
        units.setAdapter(unitList);
        productClass.setAdapter(productClasses);
        priceCurrency.setText(currencyAbbr);
        costCurrency.setText(currencyAbbr);
    }

    @OnClick(value = {R.id.btnSave, R.id.btnCancel, R.id.btnAdvance})
    public void buttonClick(View view) {
        ProductPresenter presenter = ((ProductActivity) getContext()).getPresenter();
        switch (view.getId()) {
            case R.id.btnSave:

                break;

            case R.id.btnCancel:

                break;

            case R.id.btnAdvance:

                break;
        }
    }

    public void openAddMode() {
        name.setText("");
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
                             int unitPos,
                             String vendorName,
                             String description) {
        this.name.setText(name);
        this.price.setText(Double.toString(price));
        this.cost.setText(Double.toString(cost));
        this.barcode.setText(barCode);
        this.sku.setText(sku);
        this.isActive.setChecked(isActive);
        this.priceCurrency.setText(priceCurrencyName);
        this.costCurrency.setText(costCurrencyName);
        this.productClass.setSelectedPosition(productClassPos);
        this.unitsCategory.setSelectedPosition(unitCategoryPos);
        this.units.setSelectedPosition(unitPos);
        this.vendor.setText(vendorName);
    }


}
