package com.jim.multipos.ui.first_configure.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure.adapters.AccountSpinnerAdapter;
import com.jim.multipos.ui.first_configure.adapters.CurrencySpinnerAdapter;
import com.jim.multipos.ui.first_configure.adapters.SystemPaymentTypesAdapter;
import com.jim.multipos.ui.first_configure.common.BaseFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.di.FirstConfigureActivityComponent;
import com.jim.multipos.ui.first_configure.presenters.PaymentTypeFragmentPresenter;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentTypeFragmentFirstConfig extends BaseFragmentFirstConfig implements PaymentTypeFragmentView, SystemPaymentTypesAdapter.OnClick {
    @BindView(R.id.etPaymentTypeName)
    EditText etPaymentTypeName;
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @BindView(R.id.btnRevert)
    MpButton btnRevert;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    @BindView(R.id.rvSystemPaymentType)
    RecyclerView rvSystemPaymentType;
    @BindView(R.id.spAccount)
    MpSpinner spAccount;
    @BindView(R.id.spCurrency)
    MpSpinner spCurrency;
    private String paymentTypeName;
    @Inject
    PaymentTypeFragmentPresenter presenter;
    @Inject
    FirstConfigureActivity activity;
    private SystemPaymentTypesAdapter adapter;
    //private List<SystemPaymentType> systemPaymentTypes;

    public PaymentTypeFragmentFirstConfig() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.payment_type_fragment, container, false);

        this.getComponent(FirstConfigureActivityComponent.class).inject(this);
        presenter.init(this);

        ButterKnife.bind(this, view);

        presenter.setData();

        if (FirstConfigureActivity.SAVE_BUTTON_MODE == activity.getButtonMode()) {
            btnNext.setText(R.string.save);
        } else if (FirstConfigureActivity.NEXT_BUTTON_MODE == activity.getButtonMode()) {
            btnNext.setText(R.string.next);
        }

        if (paymentTypeName != null) {
            etPaymentTypeName.setText(paymentTypeName);
        }

        RxView.clicks(btnNext).subscribe(aVoid -> {
            presenter.openNextFragment();
        });

        RxView.clicks(ivAdd).subscribe(aVoid -> {
            String paymentTypeName = etPaymentTypeName.getText().toString();
            int accountPosition = spAccount.selectedItem();
            int currencyPosition = spCurrency.selectedItem();

            presenter.addData(paymentTypeName, accountPosition, currencyPosition);
        });

        RxView.clicks(btnRevert).subscribe(aVoid -> {
            activity.finish();
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        paymentTypeName = etPaymentTypeName.getText().toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        hideKeyboard();
    }

    @Override
    public HashMap<String, String> getData() {
        HashMap<String, String> datas = new HashMap<>();

        String paymentTypeName = etPaymentTypeName.getText().toString();

        datas.put("paymentTypeName", paymentTypeName);

        return datas;
    }

    @Override
    public boolean checkData() {
        if (presenter != null) {
            return presenter.isCompleteData();
        }

        return false;
    }

    @Override
    public void showCurrencies(List<Currency> currencies) {
        CurrencySpinnerAdapter adapter = new CurrencySpinnerAdapter(getContext(), R.layout.item_spinner, currencies);
        spCurrency.setAdapter(adapter);
    }

    @Override
    public void showAccount(List<Account> accounts) {
        spAccount.setAdapter(new AccountSpinnerAdapter(getContext(), R.layout.item_spinner, accounts));
    }

    @Override
    public void clearViews() {
        etPaymentTypeName.setText("");
    }

    @Override
    public void showRecyclerView(List<PaymentType> systemPaymentTypes) {
        adapter = new SystemPaymentTypesAdapter(systemPaymentTypes, this);
        rvSystemPaymentType.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSystemPaymentType.setAdapter(adapter);
    }

    @Override
    public void showPaymentTypeListEmpty(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAccountError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPaymentTypeNameError(String error) {
        etPaymentTypeName.setError(error);
    }

    @Override
    public void paymentTypeAdded() {
        adapter.notifyItemInserted(0);
        rvSystemPaymentType.scrollToPosition(0);
    }

    @Override
    public void openNextFragment() {
        activity.openNextFragment();
    }

    @Override
    public void updateRecyclerView() {
        /*adapter.notifyDataSetChanged();
        rvSystemPaymentType.scrollToPosition(0);*/
    }

    @Override
    public void removeItem(int position) {
        presenter.removeItem(position);
    }

    @Override
    public void saveData() {
        presenter.saveData();
    }

    @Override
    public void paymentTypeRemoved() {
        adapter.notifyDataSetChanged();
    }
}
