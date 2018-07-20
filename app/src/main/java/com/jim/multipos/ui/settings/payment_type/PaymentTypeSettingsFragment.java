package com.jim.multipos.ui.settings.payment_type;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.ui.settings.SettingsActivity;
import com.jim.multipos.ui.settings.connection.SettingsConnection;
import com.jim.multipos.ui.settings.payment_type.adapter.PaymentTypeSettingsAdapter;
import com.jim.multipos.ui.settings.payment_type.model.PaymentTypeItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PaymentTypeSettingsFragment extends BaseFragment implements PaymentTypeSettingsView {

    @Inject
    PaymentTypeSettingsPresenter presenter;
    @BindView(R.id.rvPaymentTypes)
    RecyclerView rvPaymentTypes;
    @BindView(R.id.etPaymentTypeName)
    MpEditText etPaymentTypeName;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @BindView(R.id.btnAdd)
    MpMiniActionButton btnAdd;
    @BindView(R.id.spAccount)
    MPosSpinner spAccount;
    private PaymentTypeSettingsAdapter adapter;
    @Inject
    SettingsConnection connection;

    @Override
    protected int getLayout() {
        return R.layout.payment_type_settings_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        connection.setPaymentTypeSettingsView(this);
        presenter.initPaymentTypes();
        rvPaymentTypes.setLayoutManager(new LinearLayoutManager(getContext()));
        btnAdd.setOnClickListener(view -> {
            if (etPaymentTypeName.getText().toString().isEmpty()) {
                etPaymentTypeName.setError(getContext().getString(R.string.enter_payment_method));
            } else {
                presenter.addPaymentType(etPaymentTypeName.getText().toString(), chbActive.isChecked(), spAccount.getSelectedPosition());
                ((SettingsActivity) getContext()).setChanged(true);
                etPaymentTypeName.setText("");
            }
        });
    }

    @Override
    public void setSpinner(String[] accounts) {
        spAccount.setAdapter(accounts);
    }

    @Override
    public void setPaymentTypes(List<PaymentTypeItem> paymentTypes, List<Account> accountList) {
        adapter = new PaymentTypeSettingsAdapter(getContext(), paymentTypes, (paymentType, position) -> {
            presenter.savePaymentType(paymentType);
            ((SettingsActivity) getContext()).setChanged(true);
        }, accountList);
        rvPaymentTypes.setAdapter(adapter);
    }

    @Override
    public void setError() {
        Toast.makeText(getContext(), getContext().getString(R.string.payment_type_cant_be_active), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifyList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateAccounts() {
        presenter.initPaymentTypes();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setSuccess() {
        Toast.makeText(getContext(), getContext().getString(R.string.successfully_saved), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        connection.setPaymentTypeSettingsView(null);
    }
}
