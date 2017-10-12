package com.jim.multipos.ui.first_configure.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure.FirstConfigurePresenterImpl;
import com.jim.multipos.ui.first_configure.adapters.SystemAccountsAdapter;
import com.jim.multipos.utils.RxBusLocal;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

/**
 * Created by user on 07.10.17.
 */

public class AccountFragment extends BaseFragment {
    @Inject
    RxBusLocal rxBusLocal;
    @NotEmpty(messageId = R.string.enter_account_name)
    @BindView(R.id.etAccountName)
    EditText etAccountName;
    @BindView(R.id.spType)
    MpSpinner spType;
    @BindView(R.id.spCirculation)
    MpSpinner spCirculation;
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @BindView(R.id.btnRevert)
    MpButton btnRevert;
    @BindView(R.id.rvSystemAccounts)
    RecyclerView rvSystemAccounts;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;

    @Override
    protected int getLayout() {
        return R.layout.account_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (((FirstConfigureActivity) getActivity()).getPresenter().isNextButton()) {
            btnNext.setText(R.string.next);
        } else {
            btnNext.setText(R.string.save);
        }

        rvSystemAccounts.setLayoutManager(new LinearLayoutManager(getContext()));

        RxView.clicks(btnNext).subscribe(aVoid -> {
            ((FirstConfigureActivity) getActivity()).getPresenter().checkAccountData();
        });

        RxView.clicks(ivAdd).subscribe(aVoid -> {
            if (isValid()) {
                ((FirstConfigureActivity) getActivity()).getPresenter().addAccount(etAccountName.getText().toString(), spType.selectedItemPosition(), spCirculation.selectedItemPosition());
                etAccountName.setText("");
            }
        });

        RxView.clicks(btnRevert).subscribe(aVoid -> {
            ((FirstConfigureActivity) getActivity()).getPresenter().openPrevFragment();
        });

        ((FirstConfigureActivity) getActivity()).getPresenter().fillAccountsRV(rvSystemAccounts);

        spType.setItems(((FirstConfigureActivity) getActivity()).getPresenter().getTypes());
        spType.setAdapter();
        spCirculation.setItems(((FirstConfigureActivity) getActivity()).getPresenter().getCirculations());
        spCirculation.setAdapter();
    }

    @Override
    public boolean isValid() {
        boolean result = super.isValid();
        if (((FirstConfigureActivity) getActivity()).getPresenter().isAccountNameExists(etAccountName.getText().toString())) {
            etAccountName.setError(getString(R.string.account_name_exists));
            return false;
        }
        return result;
    }

    @Override
    protected void rxConnections() {

    }

    public void updateAccountList(Account account) {
        ((SystemAccountsAdapter) rvSystemAccounts.getAdapter()).addItem(account);
    }

    public  void removeAccountItem(Account account) {
        ((SystemAccountsAdapter) rvSystemAccounts.getAdapter()).removeItem(account);
    }
}
