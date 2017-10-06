package com.jim.multipos.ui.products.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpSpinner;
import com.jim.mpviews.RecyclerViewWithMaxHeight;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Recipe;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.products.ProductsActivity;
import com.jim.multipos.ui.products.adapters.IngredientsAdapter;
import com.jim.multipos.ui.products.adapters.ProductsSpinnerAdapter;
import com.jim.multipos.ui.products.adapters.SubUnitsAdapter;
import com.jim.multipos.ui.products.adapters.UnitSpinnerAdapter;
import com.jim.multipos.ui.products.di.ProductsComponent;
import com.jim.multipos.ui.products.presenters.AdvancedOptionPresenter;
import com.jim.multipos.utils.managers.PosFragmentManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by DEV on 31.08.2017.
 */
public class AdvancedOptionFragment extends Fragment { //BaseFragment implements AdvancedOptionView {
    private Unbinder unbinder;
    @Inject
    ProductsActivity activity;
    @Inject
    AdvancedOptionPresenter presenter;
    @Inject
    PosFragmentManager posFragmentManager;
    @BindView(R.id.tvMainUnit)
    TextView tvMainUnit;
    @BindView(R.id.etMainUnitQty)
    MpEditText etMainUnitQty;
    @BindView(R.id.etProductDescription)
    EditText etProductDescription;
    @BindView(R.id.etSubUnitName)
    MpEditText etSubUnitName;
    @BindView(R.id.etIngredientsQty)
    MpEditText etIngredientsQty;
    @BindView(R.id.ivAddSubUnit)
    ImageView ivAddSubUnit;
    @BindView(R.id.ivAddIngredients)
    ImageView ivAddIngredients;
    @BindView(R.id.spIngredients)
    MpSpinner spIngredients;
    @BindView(R.id.spIngredientsUnit)
    MpSpinner spIngredientsUnit;
    @BindView(R.id.rvIngredients)
    RecyclerView rvIngredients;
    @BindView(R.id.rvSubUnits)
    RecyclerViewWithMaxHeight rvSubUnits;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @BindView(R.id.chbHasRecipe)
    MpCheckbox chbHasRecipe;
    @BindView(R.id.btnBackTo)
    MpButton btnBackTo;
    @BindView(R.id.btnSaveOptions)
    MpButton btnSaveOptions;
    private SubUnitsAdapter subUnitsAdapter;
    private IngredientsAdapter ingredientsAdapter;
    private List<Unit> unitList;
    private LinearLayoutManager layoutManager;
    private List<Product> productList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_advance_options, container, false);
        unbinder = ButterKnife.bind(this, view);
//        this.getComponent(ProductsComponent.class).inject(this);
        layoutManager = new LinearLayoutManager(getContext());
//        presenter.init(this);
        checkRecipe();
        RxView.clicks(btnSaveOptions).subscribe(o ->
                presenter.saveOptions(chbHasRecipe.isCheckboxChecked())
        );
        chbHasRecipe.getCheckBox().setOnCheckedChangeListener((compoundButton, b) -> {
            presenter.onHasRecipeChange(chbHasRecipe.isCheckboxChecked());
            checkRecipe();
        });
        RxView.clicks(ivAddIngredients).subscribe(o -> {
            if (!etIngredientsQty.getText().toString().isEmpty())
                presenter.setIngredients(productList.get(spIngredients.selectedItemPosition()), etIngredientsQty.getText().toString(), unitList.get(spIngredientsUnit.selectedItemPosition()));
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.btnBackTo)
    public void onBack() {
        posFragmentManager.popBackStack();
    }

    @OnClick(R.id.ivAddSubUnit)
    public void addSubUnit() {
        if (!etSubUnitName.getText().toString().isEmpty() && !etMainUnitQty.getText().toString().isEmpty())
            presenter.setSubUnits(etSubUnitName.getText().toString(), etMainUnitQty.getText().toString(), chbActive.isCheckboxChecked());
        else etSubUnitName.setError("Please, enter sub unit name");
    }

    public void setSubUnitsRecyclerView(List<Unit> subUnitsLists) {
        subUnitsAdapter = new SubUnitsAdapter(subUnitsLists, presenter);
        rvSubUnits.setLayoutManager(layoutManager);
        rvSubUnits.setAdapter(subUnitsAdapter);
    }

    public void setIngredientsRecyclerView(List<Recipe> recipe) {
        rvIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientsAdapter = new IngredientsAdapter(recipe, presenter);
        rvIngredients.setAdapter(ingredientsAdapter);
    }

    public void setSpinnerUnits(List<Unit> list) {
        unitList = list;
        UnitSpinnerAdapter adapter = new UnitSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, unitList);
        spIngredientsUnit.setAdapter(adapter);
    }


    public void updateSubUnits() {
        subUnitsAdapter.notifyDataSetChanged();
    }

    public void updateIngredients() {
        ingredientsAdapter.notifyDataSetChanged();
    }

    public void setMainUnit(Unit mainUnit) {
        tvMainUnit.setText(mainUnit.getAbbr());
    }

    public void setRecipeStatus(boolean hasRecipe) {
        chbHasRecipe.setChecked(hasRecipe);
    }

    public void popBackStack() {
        posFragmentManager.popBackStack();
    }

    public void setRecipeState(boolean state) {
        chbHasRecipe.setChecked(state);
    }

    public void setIngredientSpinner(List<Product> productsList) {
        productList = productsList;
        ProductsSpinnerAdapter adapter = new ProductsSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, productsList);
        spIngredients.setAdapter(adapter);
    }

    private void checkRecipe() {
        if (chbHasRecipe.isCheckboxChecked()) {
            rvIngredients.setVisibility(View.VISIBLE);
            spIngredients.setSpinnerEnabled(true);
            spIngredientsUnit.setSpinnerEnabled(true);
            etIngredientsQty.setEnabled(true);
            ivAddIngredients.setClickable(true);
        } else {
            rvIngredients.setVisibility(View.GONE);
            spIngredients.setSpinnerEnabled(false);
            spIngredientsUnit.setSpinnerEnabled(false);
            etIngredientsQty.setEnabled(false);
            ivAddIngredients.setClickable(false);
        }
    }
}
