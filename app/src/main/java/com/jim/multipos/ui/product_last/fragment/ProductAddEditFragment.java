package com.jim.multipos.ui.product_last.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.ui.product_last.ProductPresenter;

import java.util.Currency;
import java.util.List;

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

    @BindView(R.id.etProductPrice)
    MpEditText price;

    @BindView(R.id.spPriceCurrency)
    MPosSpinner priceCurrency;

    @BindView(R.id.etProductCost)
    MpEditText cost;

    @BindView(R.id.spCostCurrency)
    MPosSpinner costCurrency;

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

    @Override
    protected int getLayout() {
        return R.layout.add_product_fragment;
    }

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    public void initProductAddEditFragment(String[] unitList,
                                                     String[] priceCurrency,
                                                     String[] costCurrency,
                                                     String[] productClasses) {

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
        priceCurrency.setSelection(0);
        cost.setText("");
        costCurrency.setSelection(0);
        productClass.setSelection(0);
        isActive.setChecked(true);
        save.setText(R.string.save);
    }

    public void openEditMode(Product product) {

    }


}
