package com.jim.multipos.ui.service_fee;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.ui.HasComponent;
import com.jim.multipos.ui.first_configure.adapters.CurrencySpinnerAdapter;
import com.jim.multipos.ui.service_fee.adapters.ServiceFeeAdapter;
import com.jim.multipos.ui.service_fee.di.ServiceFeeActivityComponent;
import com.jim.multipos.ui.service_fee.di.ServiceFeeActivityModule;
import com.jim.multipos.ui.service_fee.dialogs.AutoApplyConfigure;
import com.jim.multipos.ui.service_fee.dialogs.UsageTypeDialog;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.Unsibscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ServiceFeeActivity extends BaseActivity implements HasComponent<ServiceFeeActivityComponent>, ServiceFeeView, ServiceFeeAdapter.OnClick, UsageTypeDialog.OnClickDialog, AutoApplyConfigure.OnClickDialog {
    @Inject
    ServiceFeePresenter presenter;
    @Inject
    RxBus rxBus;
    @BindView(R.id.etName)
    MpEditText etName;
    @BindView(R.id.etAmount)
    MpEditText etAmount;
    @BindView(R.id.spType)
    MpSpinner spType;
    @BindView(R.id.spCurrency)
    MpSpinner spCurrency;
    @BindView(R.id.spAppType)
    MpSpinner spAppType;
    @BindView(R.id.chbTaxed)
    MpCheckbox chbTaxed;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    @BindView(R.id.rvItems)
    RecyclerView rvItems;
    @BindView(R.id.btnSave)
    MpButton btnSave;
    Unbinder unbinder;
    ServiceFeeActivityComponent serviceFeeComponent;
    private ServiceFeeAdapter serviceFeeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_service_fee_fragment);

        unbinder = ButterKnife.bind(this);

        presenter.getSpTypes();
        presenter.getCurrencies();
        presenter.getAppTypes();
        presenter.getServiceFeeData();

        spCurrency.setSpinnerEnabled(false);

        RxView.clicks(ivAdd).subscribe(aVoid -> {
            String name = etName.getText().toString();
            String amount = etAmount.getText().toString();
            int type = spType.selectedItemPosition();
            int currency = spCurrency.selectedItemPosition();
            int appType = spAppType.selectedItemPosition();
            boolean isTaxed = chbTaxed.isCheckboxChecked();
            boolean isActive = chbActive.isCheckboxChecked();

            presenter.openUsageTypeDialog(name, amount, type, currency, appType, isTaxed, isActive);
        });

        RxView.clicks(btnSave).subscribe(aVoid -> {
            presenter.saveData();
        });

        spType.setOnItemSelectedListener((adapterView, view, i, l) -> {
            presenter.changeType(i);
        });
    }

    protected void setupComponent(BaseAppComponent baseAppComponent) {
//        serviceFeeComponent = baseAppComponent.plus(new ServiceFeeActivityModule(this));
        serviceFeeComponent.inject(this);
    }

    @Override
    public ServiceFeeActivityComponent getComponent() {
        return serviceFeeComponent;
    }

    @Override
    public void showSpType(String[] types) {
        spType.setItems(types);
        spType.setAdapter();
    }

    @Override
    public void showSpCurrency(List<Currency> currencies) {
        CurrencySpinnerAdapter adapter = new CurrencySpinnerAdapter(this, R.layout.item_spinner, currencies);
        spCurrency.setAdapter(adapter);
    }

    @Override
    public void showAppType(String[] appTypes) {
        spAppType.setItems(appTypes);
        spAppType.setAdapter();
    }

    @Override
    public void updateRecyclerView() {
        serviceFeeAdapter.notifyDataSetChanged();
    }

    @Override
    public void showRVServiceData(List<ServiceFee> serviceFeeList, List<Currency> currencies, String[] types, String[] appTypes) {
        serviceFeeAdapter = new ServiceFeeAdapter(serviceFeeList, currencies, this, types, appTypes);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(serviceFeeAdapter);
    }

    @Override
    public void setCheckedTaxed(boolean state, int position) {
        presenter.setCheckedTaxed(state, position);
    }

    @Override
    public void setCheckedActive(boolean state, int position) {
        presenter.setCheckedActive(state, position);
    }

    @Override
    public void clearViews() {
        etAmount.setText("");
        etName.setText("");
        etName.requestFocus();
    }

    @Override
    public void showNameError(String message) {
        etName.setError(message);
    }

    @Override
    public void showAmountError(String message) {
        etAmount.setError(message);
    }

    @Override
    public void enableCurrency(boolean enable) {
        spCurrency.setSpinnerEnabled(enable);
        spCurrency.notifyDataSetChanged();
    }

    @Override
    public void closeUsageTypeDialog() {
        clearViews();
    }

    @Override
    public void nextDialog(int position) {
        if (position != 0) {
            presenter.getPaymentType();
        } else {
            addItem(-1);
            clearViews();
        }
    }

    @Override
    public void closeAutoApplyDialog() {
        openUsageTypeDialog();
    }

    @Override
    public void saveAutoApplyDialog(int paymentTypePosition) {
        addItem(paymentTypePosition);
    }

    @Override
    public void openAutoApplyDialog(List<PaymentType> paymentTypes) {
        Dialog dialog = new AutoApplyConfigure(this, paymentTypes, this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void openUsageTypeDialog() {
        Dialog dialog = new UsageTypeDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        rxBus.send(new Unsibscribe(ServiceFeeActivity.class.getName()));
        unbinder.unbind();

        super.onDestroy();
    }

    @Override
    public void closeActivity() {
        finish();
    }

    private void addItem(int paymentTypePosition) {
        String name = etName.getText().toString();
        String amount = etAmount.getText().toString();
        int type = spType.selectedItemPosition();
        int currency = spCurrency.selectedItemPosition();
        int appType = spAppType.selectedItemPosition();
        boolean isTaxed = chbTaxed.isCheckboxChecked();
        boolean isActive = chbActive.isCheckboxChecked();

        if (paymentTypePosition != -1)
            presenter.addItem(name, amount, type, currency, appType, isTaxed, isActive, paymentTypePosition);
        else
            presenter.addItem(name, amount, type, currency, appType, isTaxed, isActive, -1);
    }
}
