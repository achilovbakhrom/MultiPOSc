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
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure.adapters.SystemAccountsAdapter;
import com.jim.multipos.ui.first_configure.common.BaseFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.di.FirstConfigureActivityComponent;
import com.jim.multipos.ui.first_configure.presenters.AccountFragmentPresenter;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragmentFirstConfig extends BaseFragmentFirstConfig implements AccountFragmentView, SystemAccountsAdapter.OnClick {
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
    @Inject
    AccountFragmentPresenter presenter;
    @Inject
    FirstConfigureActivity activity;
    private String accountName;
    private SystemAccountsAdapter adapter;

    public AccountFragmentFirstConfig() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.account_fragment, container, false);

        this.getComponent(FirstConfigureActivityComponent.class).inject(this);
        presenter.init(this);

        ButterKnife.bind(this, view);

        presenter.showAccountType();
        presenter.showAccountCirculation();
        presenter.setAdapterData();

        if (FirstConfigureActivity.SAVE_BUTTON_MODE == activity.getButtonMode()) {
            btnNext.setText(R.string.save);
        } else if (FirstConfigureActivity.NEXT_BUTTON_MODE == activity.getButtonMode()) {
            btnNext.setText(R.string.next);
        }

        if (accountName != null) {
            etAccountName.setText(accountName);
        }

        RxView.clicks(btnNext).subscribe(aVoid -> {
            presenter.openNextFragment(getData());
        });

        RxView.clicks(ivAdd).subscribe(aVoid -> {
            String name = etAccountName.getText().toString();
            int type = spType.selectedItem();
            int circulation = spCirculation.selectedItem();

            presenter.addAccount(name, type, circulation);
        });

        RxView.clicks(btnRevert).subscribe(aVoid -> {
            activity.finish();
        });

        return view;
    }

    @Override
    public void showAddedAccount() {
        adapter.notifyItemInserted(0);
        rvSystemAccounts.scrollToPosition(0);
    }

    @Override
    public void clearViews() {
        etAccountName.setText("");
    }

    @Override
    public void onStop() {
        super.onStop();

        accountName = etAccountName.getText().toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        hideKeyboard();
    }

    @Override
    public HashMap<String, String> getData() {
        HashMap<String, String> datas = new HashMap<>();

        String accountName = etAccountName.getText().toString();
        datas.put("accountName", accountName);

        return datas;
    }


    @Override
    public boolean checkData() {
        return presenter.isCompleteData();
    }

    @Override
    public void showAccountNameError(String error) {
        etAccountName.setError(error);
    }

    @Override
    public void showAccountType(String[] accountType) {
        spType.setItems(accountType);
        spType.setAdapter();
    }

    @Override
    public void showAccountCirculation(String[] accountCirculation) {
        spCirculation.setItems(accountCirculation);
        spCirculation.setAdapter();
    }

    @Override
    public void showAdapter(List<Account> accounts, String[] accountType, String[] accountCirculation) {
        adapter = new SystemAccountsAdapter(accounts, accountType, accountCirculation, this);
        rvSystemAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSystemAccounts.setAdapter(adapter);
    }

    @Override
    public void openNextFragment() {
        activity.openNextFragment();
    }

    @Override
    public void showAccountEmptyError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeAccount(int position) {
        presenter.removeAccount(position);
    }

    @Override
    public void accountRemoved(int position) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void saveData() {
        presenter.saveData();
    }
}
