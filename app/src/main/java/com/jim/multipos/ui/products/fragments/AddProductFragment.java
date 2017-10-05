package com.jim.multipos.ui.products.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.jim.multipos.data.db.model.products.SubCategory;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.first_configure.adapters.CurrencySpinnerAdapter;
import com.jim.multipos.ui.products.ProductsActivity;
import com.jim.multipos.ui.products.adapters.ProductClassSpinnerAdapter;
import com.jim.multipos.ui.products.adapters.UnitSpinnerAdapter;
import com.jim.multipos.ui.products.di.ProductsComponent;
import com.jim.multipos.ui.products.presenters.ProductsPresenter;

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
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnAdvance)
    MpButton btnAdvance;
    @BindView(R.id.btnSave)
    MpButton btnSave;
    @BindView(R.id.ivChooseImage)
    ImageView ivChooseImage;
    @BindView(R.id.ivChild)
    ImageView ivChild;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @BindView(R.id.chbTax)
    MpCheckbox chbTax;
    @BindView(R.id.chbHasRecipe)
    MpCheckbox chbHasRecipe;

    private List<Unit> unitList;
    private List<Currency> currencyList;
    private List<ProductClass> classList;
    private SubCategory subCategory;
    private Unbinder unbinder;
    private boolean isOpen = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_product_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        this.getComponent(ProductsComponent.class).inject(this);
        presenter.init(this);
        presenter.checkData();
        chbHasRecipe.getCheckBox().setOnCheckedChangeListener((compoundButton, b) -> {
            presenter.onHasRecipeChange(chbHasRecipe.isCheckboxChecked());
        });
        return view;
    }

    @OnClick(R.id.btnSave)
    public void onSave() {
        if (etProductName.getText().toString().isEmpty())
            etProductName.setError(getString(R.string.enter_product_name));
        else if (etSKU.getText().toString().isEmpty())
            etSKU.setError(getString(R.string.enter_product_sku));
        else if (etProductPrice.getText().toString().isEmpty())
            etProductPrice.setError(getString(R.string.enter_product_price));
        else if (etProductCost.getText().toString().isEmpty())
            etProductCost.setError(getString(R.string.enter_product_cost));
        else {
            presenter.saveProduct(etProductName.getText().toString(), etBarcode.getText().toString(), etSKU.getText().toString(),
                    etProductPrice.getText().toString(), etProductCost.getText().toString(), currencyList.get(spPriceCurrency.selectedItemPosition()), currencyList.get(spCostCurrency.selectedItemPosition()), unitList.get(spUnit.selectedItemPosition()),
                    null, classList.get(spProductClass.selectedItemPosition()), chbActive.isCheckboxChecked(), chbTax.isCheckboxChecked(), chbHasRecipe.isCheckboxChecked());
        }
    }

    @OnClick(R.id.btnCancel)
    public void onCancel() {
        activity.finish();
    }

    @OnClick(R.id.tvLinked)
    public void onLinkedClick() {
        activity.openGlobalVendors();
    }

    @OnClick(R.id.btnAdvance)
    public void onAdvance() {
        if (etProductName.getText().toString().isEmpty())
            etProductName.setError(getString(R.string.enter_product_name));
        else if (etSKU.getText().toString().isEmpty())
            etSKU.setError(getString(R.string.enter_product_sku));
        else if (etProductPrice.getText().toString().isEmpty())
            etProductPrice.setError(getString(R.string.enter_product_price));
        else if (etProductCost.getText().toString().isEmpty())
            etProductCost.setError(getString(R.string.enter_product_cost));
        else {
            presenter.onAdvance(etProductName.getText().toString(), etBarcode.getText().toString(), etSKU.getText().toString(),
                    etProductPrice.getText().toString(), etProductCost.getText().toString(), currencyList.get(spPriceCurrency.selectedItemPosition()), currencyList.get(spCostCurrency.selectedItemPosition()), unitList.get(spUnit.selectedItemPosition()),
                    null, classList.get(spProductClass.selectedItemPosition()), chbActive.isCheckboxChecked(), chbTax.isCheckboxChecked(), chbHasRecipe.isCheckboxChecked());
        }
    }

    @OnClick(R.id.ivChooseImage)
    public void onLoadImage() {

    }

    @OnClick(R.id.ivChild)
    public void onSetMatrix() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        MatrixDialogFragment matrixDialogFragment = new MatrixDialogFragment();
        this.getComponent(ProductsComponent.class).inject(matrixDialogFragment);
        matrixDialogFragment.show(ft, "dialog");
    }

    @Override
    public void setProductImage(int resId) {

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
    public void setFields(String name, String barcode, String sku, String price, String cost, int unit, int priceCurrency, int costCurrency, String vendor, int productClass, boolean taxed, boolean active, boolean recipe) {
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
        chbHasRecipe.setChecked(recipe);
        chbTax.setChecked(taxed);
    }


    @Override
    public void openAdvanceOptions() {
        activity.openAdvancedOptions();
    }

    @Override
    public void setRecipeState(boolean state) {
        chbHasRecipe.setChecked(state);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onDestroy();
    }
}
