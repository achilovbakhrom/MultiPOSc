package com.jim.multipos.ui.service_fee;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.ui.first_configure.adapters.CurrencySpinnerAdapter;
import com.jim.multipos.ui.service_fee.adapters.ServiceFeeAdapter;
import com.jim.multipos.ui.service_fee.dialogs.AutoApplyConfigure;
import com.jim.multipos.ui.service_fee.dialogs.UsageTypeDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ServiceFeeActivity extends BaseActivity implements ServiceFeeView, ServiceFeeAdapter.OnClick, UsageTypeDialog.OnClickDialog, AutoApplyConfigure.OnClickDialog {
    @Inject
    ServiceFeePresenter presenter;
    @BindView(R.id.toolbar)
    MpToolbar toolbar;
    @BindView(R.id.etName)
    MpEditText etName;
    @BindView(R.id.etAmount)
    MpEditText etAmount;
    @BindView(R.id.spType)
    MPosSpinner spType;
    @BindView(R.id.spCurrency)
    MPosSpinner spCurrency;
    @BindView(R.id.spAppType)
    MPosSpinner spAppType;
   /* @BindView(R.id.chbTaxed)
    MpCheckbox chbTaxed;*/
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    @BindView(R.id.rvItems)
    RecyclerView rvItems;
    @BindView(R.id.btnSave)
    MpButton btnSave;
    Unbinder unbinder;
    private ServiceFeeAdapter serviceFeeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_service_fee_fragment);

        unbinder = ButterKnife.bind(this);

        toolbar.setMode(MpToolbar.DEFAULT_TYPE);

        presenter.getSpTypes();
        presenter.getCurrencies();
        presenter.getAppTypes();
        presenter.getServiceFeeData();

        spCurrency.setEnabled(false);

        /*RxView.clicks(ivAdd).subscribe(aVoid -> {
            presenter.openUsageTypeDialog(etName.getText().toString(),
                    etAmount.getText().toString(),
                    spType.getSelectedPosition(),
                    spCurrency.getSelectedPosition(),
                    spAppType.getSelectedPosition(),
                    chbTaxed.isChecked(),
                    chbActive.isChecked());
        });*/

        RxView.clicks(btnSave).subscribe(aVoid -> {
            presenter.saveData();
        });

        spType.setItemSelectionListener((view, position) -> {
            presenter.changeType(position);
        });
    }

    @Override
    public void showSpType(String[] types) {
        /*spType.setItems(types);
        spType.setAdapter()*/;
        spType.setAdapter(types);
    }

    @Override
    public void showSpCurrency(List<Currency> currencies) {
        CurrencySpinnerAdapter adapter = new CurrencySpinnerAdapter(this, R.layout.item_spinner, currencies);
        spCurrency.setAdapter(adapter);
    }

    @Override
    public void showAppType(String[] appTypes) {
        /*spAppType.setItems(appTypes);
        spAppType.setAdapter();*/
        spAppType.setAdapter(appTypes);
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
        /*spCurrency.setSpinnerEnabled(enable);
        spCurrency.notifyDataSetChanged();*/
        spCurrency.setEnabled(enable);
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
        //rxBus.send(new Unsibscribe(ServiceFeeActivity.class.getName()));
        unbinder.unbind();

        super.onDestroy();
    }

    @Override
    public void closeActivity() {
        finish();
    }

    private void addItem(int paymentTypePosition) {
       /* if (paymentTypePosition != -1)
            presenter.addItem(etName.getText().toString(),
                    etAmount.getText().toString(),
                    spType.getSelectedPosition(),
                    spCurrency.getSelectedPosition(),
                    spAppType.getSelectedPosition(),
                    chbTaxed.isChecked(),
                    chbActive.isChecked(),
                    paymentTypePosition);
        else
            presenter.addItem(etName.getText().toString(),
                    etAmount.getText().toString(),
                    spType.getSelectedPosition(),
                    spCurrency.getSelectedPosition(),
                    spAppType.getSelectedPosition(),
                    chbTaxed.isChecked(),
                    chbActive.isChecked(),
                    -1);*/
    }
}
