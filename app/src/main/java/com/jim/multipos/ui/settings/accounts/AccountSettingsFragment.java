package com.jim.multipos.ui.settings.accounts;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.ui.settings.SettingsActivity;
import com.jim.multipos.ui.settings.accounts.adapter.AccountsSettingsAdapter;
import com.jim.multipos.ui.settings.connection.SettingsConnection;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class AccountSettingsFragment extends BaseFragment implements AccountSettingsView {

    @BindView(R.id.rvAccounts)
    RecyclerView rvAccounts;
    @BindView(R.id.etAccountName)
    MpEditText etAccountName;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @BindView(R.id.btnAdd)
    MpMiniActionButton btnAdd;
    @Inject
    AccountSettingsPresenter presenter;
    @Inject
    SettingsConnection connection;
    private AccountsSettingsAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.account_settings_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.initAccounts();
        rvAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
        btnAdd.setOnClickListener(view -> {
            if (etAccountName.getText().toString().isEmpty()) {
                etAccountName.setError(getContext().getString(R.string.enter_account_name));
            } else {
                presenter.addAccount(etAccountName.getText().toString(), chbActive.isChecked());
                ((SettingsActivity) getContext()).setChanged(true);
                etAccountName.setText("");
            }
        });
    }

    @Override
    public void setAccounts(List<Account> accountList) {
        adapter = new AccountsSettingsAdapter(getContext(), accountList, (account, position) -> {
            presenter.saveChanges(account, position);
            ((SettingsActivity) getContext()).setChanged(true);
            connection.updateAccounts();
        });
        rvAccounts.setAdapter(adapter);
    }

    @Override
    public void notifyList() {
        adapter.notifyDataSetChanged();
        connection.updateAccounts();
    }

    @Override
    public void setSuccess() {
        Toast.makeText(getContext(), getContext().getString(R.string.successfully_saved), Toast.LENGTH_SHORT).show();
    }
}
