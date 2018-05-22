package com.jim.multipos.ui.start_configuration.payment_type;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.utils.CompletionMode;
import com.jim.multipos.ui.start_configuration.StartConfigurationActivity;
import com.jim.multipos.ui.start_configuration.connection.StartConfigurationConnection;
import com.jim.multipos.ui.start_configuration.payment_type.adapter.PaymentTypeAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PaymentTypeFragment extends BaseFragment implements PaymentTypeView {


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
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @Inject
    PaymentTypePresenter presenter;
    @Inject
    StartConfigurationConnection connection;
    private PaymentTypeAdapter adapter;
    private CompletionMode mode = CompletionMode.NEXT;

    @Override
    protected int getLayout() {
        return R.layout.payment_type_configure_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        connection.setPaymentTypeView(this);
        presenter.initPaymentTypes();
        rvPaymentTypes.setLayoutManager(new LinearLayoutManager(getContext()));
        btnAdd.setOnClickListener(view -> {
            if (etPaymentTypeName.getText().toString().isEmpty()) {
                etPaymentTypeName.setError(getContext().getString(R.string.enter_payment_method));
            } else {
                presenter.addPaymentType(etPaymentTypeName.getText().toString(), chbActive.isChecked(), spAccount.getSelectedPosition());
                etPaymentTypeName.setText("");
            }
        });

        btnNext.setOnClickListener(view -> {
            if (mode == CompletionMode.NEXT) {
                connection.setPaymentTypeCompletion(true);
                connection.openNextFragment(0);
            } else {
                connection.setPaymentTypeCompletion(true);
                presenter.setAppRunFirstTimeValue(false);
                ((StartConfigurationActivity) getActivity()).openLockScreen();
            }
        });
    }

    @Override
    public void setSpinner(String[] accounts) {
        spAccount.setAdapter(accounts);
    }

    @Override
    public void setPaymentTypes(List<PaymentType> paymentTypes) {
        adapter = new PaymentTypeAdapter(getContext(), paymentTypes, (paymentType, position) -> {
            presenter.deletePaymentType(paymentType, position);
        });
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
    public void setMode(CompletionMode mode) {
        this.mode = mode;
        if (mode == CompletionMode.NEXT) {
            btnNext.setText(getContext().getString(R.string.next));
        } else btnNext.setText(getContext().getString(R.string.finish));
    }

    @Override
    public void checkCompletion() {
        connection.setPaymentTypeCompletion(true);
    }

    @Override
    public void updateAccounts() {
        presenter.initPaymentTypes();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        connection.setPaymentTypeView(null);
    }
}
