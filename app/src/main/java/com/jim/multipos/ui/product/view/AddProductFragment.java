package com.jim.multipos.ui.product.view;

import android.os.Bundle;
import android.widget.TextView;

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
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;


/**
 * Created by DEV on 26.08.2017.
 */

public class AddProductFragment extends BaseFragment implements ProductsView {

    @Inject
    ProductsPresenter presenter;
    @Inject
    ProductsActivity activity;
    @Inject
    RxBus rxBus;
    @Inject
    RxBusLocal rxBusLocal;
    @BindView(R.id.etProductName)
    MpEditText etProductName;
//    @BindView(R.id.tvLinked)
//    TextView tvLinked;
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
    ArrayList<Disposable> subscriptions;

    private final static String ADVANCE = "advanced_options";
    private final static String UNIT_ADDED = "unit_added";
    private final static String UNIT_REMOVED = "unit_removed";
    private final static String CURRENCY = "currency_added";
    private final static String PRODUCT_CLASS = "product_class_added";
    private static final String CLICK = "click";
    private final static String PRODUCT_OPENED = "product";
    private final static String OPEN_ADVANCE = "open_advance";
    private static final String ADD = "added";
    private static final String UPDATE = "update";

    @Override
    protected int getLayout() {
        return R.layout.add_product_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
    }

    @Override
    protected void rxConnections() {
//        subscriptions = new ArrayList<>();
//        subscriptions.add(
//                rxBusLocal.toObservable().subscribe(o -> {
//                    if (o instanceof MessageEvent) {
//                        MessageEvent event = (MessageEvent) o;
//                        if (event.getMessage().equals(ADVANCE)) {
//                            advancedOptionsOpened();
//                        }
//                        if (event.getMessage().equals("product")) {
//                            isVisible();
//                        }
//                        if (event.getMessage().equals(UNIT_ADDED) || event.getMessage().equals(UNIT_REMOVED)) {
//                            getUnits();
//                        }
//                    }
//                    if (o instanceof ProductEvent) {
//                        ProductEvent event = (ProductEvent) o;
//                        if (event.getEventType().equals(CLICK)) {
//                            setProduct(event.getProduct());
//                        }
//                        if (event.getEventType().equals(ADVANCE)) {
//                            saveProduct(event.getProduct());
//                        }
//                    }
//                    if (o instanceof SubCategoryEvent) {
//                        SubCategoryEvent event = (SubCategoryEvent) o;
//                        if (event.getEventType().equals("parent")) {
//                            setParentSubCategory(event.getSubCategory());
//                        }
//                    }
//                }));
//        subscriptions.add(
//                rxBus.toObservable().subscribe(o -> {
//                    if (o instanceof MessageEvent) {
//                        MessageEvent event = (MessageEvent) o;
//                        if (event.getMessage().equals(UNIT_ADDED)) {
//                            getUnits();
//                        }
//                        if (event.getMessage().equals(CURRENCY)) {
//                            getCurrencies();
//                        }
//                        if (event.getMessage().equals(PRODUCT_CLASS)) {
//                            getProductClass();
//                        }
//                    }
//                }));

    }

    @OnClick(R.id.btnSave)
    public void onSave() {
//            presenter.saveProduct(etProductName.getText().toString(), etBarcode.getText().toString(), etSKU.getText().toString(),
//                    etProductPrice.getText().toString(), etProductCost.getText().toString(),spPriceCurrency.getSelectedItem(), spCostCurrency.getSelectedItem(), spUnit.getSelectedItem(),
//                    null, spProductClass.getSelectedItem(), chbActive.isChecked());
    }

    @OnClick(R.id.btnCancel)
    public void onCancel() {
        activity.finish();
    }

//    @OnClick(R.id.tvLinked)
//    public void onChooseVendor() {
//
//    }

    @OnClick(R.id.btnAdvance)
    public void onAdvance() {

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
//        tvLinked.setText("");
    }

    @Override
    public void setData() {
        presenter.checkData();
    }

    @Override
    public void setFields(String name, String barcode, String sku, String price, String cost, int unit, int priceCurrency, int costCurrency, String vendor, int productClass, boolean active, String photoPath) {
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
    }
}
