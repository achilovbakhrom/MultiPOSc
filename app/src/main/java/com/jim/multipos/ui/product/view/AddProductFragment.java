package com.jim.multipos.ui.product.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.first_configure.adapters.CurrencySpinnerAdapter;
import com.jim.multipos.ui.product.ProductsActivity;
import com.jim.multipos.ui.product.adapter.ProductClassSpinnerAdapter;
import com.jim.multipos.ui.product.adapter.UnitSpinnerAdapter;
import com.jim.multipos.ui.product.presenter.ProductsPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by DEV on 26.08.2017.
 */

public class AddProductFragment extends BaseFragment implements ProductsView {

    @Inject
    ProductsPresenter presenter;
    @Inject
    ProductsActivity activity;
    @BindView(R.id.etProductName)
    MpEditText etProductName;
    @BindView(R.id.tvLinked)
    TextView tvLinked;
    @BindView(R.id.etBarcode)
    MpEditText etBarcode;
    @BindView(R.id.etSKU)
    MpEditText etSKU;
    @BindView(R.id.spUnit)
    MpSpinner spUnit;
    @BindView(R.id.etProductPrice)
    MpEditText etProductPrice;
    @BindView(R.id.spPriceCurrency)
    MpSpinner spPriceCurrency;
    @BindView(R.id.etProductCost)
    MpEditText etProductCost;
    @BindView(R.id.spCostCurrency)
    MpSpinner spCostCurrency;
    @BindView(R.id.spProductClass)
    MpSpinner spProductClass;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;

    private List<Unit> unitList;
    private List<Currency> currencyList;
    private List<ProductClass> classList;

    @Override
    protected int getLayout() {
        return R.layout.add_product_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.checkData();
    }

    @Override
    protected void rxConnections() {

    }

    @OnClick(R.id.btnSave)
    public void onSave() {
//        if (etProductName.getText().toString().isEmpty())
//            etProductName.setError(getString(R.string.enter_product_name));
//        else if (etSKU.getText().toString().isEmpty())
//            etSKU.setError(getString(R.string.enter_product_sku));
//        else if (etProductPrice.getText().toString().isEmpty())
//            etProductPrice.setError(getString(R.string.enter_product_price));
//        else if (etProductCost.getText().toString().isEmpty())
//            etProductCost.setError(getString(R.string.enter_product_cost));
//        else {
//            presenter.saveProduct(etProductName.getText().toString(), etBarcode.getText().toString(), etSKU.getText().toString(),
//                    etProductPrice.getText().toString(), etProductCost.getText().toString(), currencyList.get(spPriceCurrency.selectedItemPosition()), currencyList.get(spCostCurrency.selectedItemPosition()), unitList.get(spUnit.selectedItemPosition()),
//                    null, classList.get(spProductClass.selectedItemPosition()), chbActive.isCheckboxChecked(), chbTax.isCheckboxChecked(), );
//        }
    }

    @OnClick(R.id.btnCancel)
    public void onCancel() {
        activity.finish();
    }

    @OnClick(R.id.tvLinked)
    public void onChooseVendor() {

    }

    @OnClick(R.id.btnAdvance)
    public void onAdvance() {
//        if (etProductName.getText().toString().isEmpty())
//            etProductName.setError(getString(R.string.enter_product_name));
//        else if (etSKU.getText().toString().isEmpty())
//            etSKU.setError(getString(R.string.enter_product_sku));
//        else if (etProductPrice.getText().toString().isEmpty())
//            etProductPrice.setError(getString(R.string.enter_product_price));
//        else if (etProductCost.getText().toString().isEmpty())
//            etProductCost.setError(getString(R.string.enter_product_cost));
//        else {
//            presenter.onAdvance(etProductName.getText().toString(), etBarcode.getText().toString(), etSKU.getText().toString(),
//                    etProductPrice.getText().toString(), etProductCost.getText().toString(), currencyList.get(spPriceCurrency.selectedItemPosition()), currencyList.get(spCostCurrency.selectedItemPosition()), unitList.get(spUnit.selectedItemPosition()),
//                    null, classList.get(spProductClass.selectedItemPosition()), chbActive.isCheckboxChecked(), chbTax.isCheckboxChecked(), chbHasRecipe.isCheckboxChecked());
//        }
    }

    @OnClick(R.id.ivChooseImage)
    public void onLoadImage() {

    }

    @Override
    public void setUnitItems(List<Unit> unitItems) {
        unitList = unitItems;
        UnitSpinnerAdapter adapter = new UnitSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, unitItems);
        spUnit.setAdapter(adapter);
    }

    @Override
    public void setClassItems(List<ProductClass> classItems) {
        classList = classItems;
        ProductClassSpinnerAdapter adapter = new ProductClassSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, classItems);
        spProductClass.setAdapter(adapter);
    }

    @Override
    public void setCurrencyItems(List<Currency> currencyItems) {
        currencyList = currencyItems;
        CurrencySpinnerAdapter adapter = new CurrencySpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, currencyItems);
        spPriceCurrency.setAdapter(adapter);
        spCostCurrency.setAdapter(adapter);
    }


    @Override
    public void clearFields() {
        etProductName.setText("");
        etBarcode.setText("");
        etProductCost.setText("");
        etProductPrice.setText("");
        etSKU.setText("");
        tvLinked.setText("");
    }

    @Override
    public void setData() {
        presenter.checkData();
    }

    @Override
    public void setFields(String name, String barcode, String sku, String price, String cost, int unit, int priceCurrency, int costCurrency, String vendor, int productClass, boolean taxed, boolean active, String photoPath) {
        etProductName.setText(name);
        etSKU.setText(sku);
        etProductPrice.setText(price);
        etProductCost.setText(cost);
        etBarcode.setText(barcode);
        spUnit.setSelection(unit);
        spPriceCurrency.setSelection(priceCurrency);
        spCostCurrency.setSelection(costCurrency);
        spProductClass.setSelection(productClass);
        chbActive.setChecked(active);
    }


    @Override
    public void openAdvanceOptions() {
        activity.openAdvancedOptions();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
    }
}
