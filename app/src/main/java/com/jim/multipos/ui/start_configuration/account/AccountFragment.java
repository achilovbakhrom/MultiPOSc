package com.jim.multipos.ui.start_configuration.account;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.utils.CompletionMode;
import com.jim.multipos.ui.start_configuration.StartConfigurationActivity;
import com.jim.multipos.ui.start_configuration.account.adapter.AccountsConfigureAdapter;
import com.jim.multipos.ui.start_configuration.connection.StartConfigurationConnection;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class AccountFragment extends BaseFragment implements AccountView {

    @BindView(R.id.rvAccounts)
    RecyclerView rvAccounts;
    @BindView(R.id.etAccountName)
    MpEditText etAccountName;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @BindView(R.id.btnAdd)
    MpMiniActionButton btnAdd;
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @Inject
    AccountPresenter presenter;
    @Inject
    StartConfigurationConnection connection;
    private AccountsConfigureAdapter adapter;
    private CompletionMode mode = CompletionMode.NEXT;

    @Override
    protected int getLayout() {
        return R.layout.account_configure_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        connection.setAccountView(this);
        presenter.initAccounts();
        rvAccounts.setLayoutManager(new LinearLayoutManager(getContext()));
        btnAdd.setOnClickListener(view -> {
            if (etAccountName.getText().toString().isEmpty()) {
                etAccountName.setError(getContext().getString(R.string.enter_account_name));
            } else {
                presenter.addAccount(etAccountName.getText().toString(), chbActive.isChecked());
                etAccountName.setText("");
            }
        });
        btnNext.setOnClickListener(view -> {
            if (mode == CompletionMode.NEXT) {
                connection.setAccountCompletion(true);
                connection.openNextFragment(4);
            } else {
                connection.setAccountCompletion(true);
                presenter.setAppRunFirstTimeValue(false);
                ((StartConfigurationActivity) getActivity()).openLockScreen();
            }
        });
    }

    @Override
    public void setAccounts(List<Account> accountList) {
        adapter = new AccountsConfigureAdapter(getContext(), accountList, (account, position) -> {
            presenter.deleteAccount(account, position);
        });
        rvAccounts.setAdapter(adapter);
    }

    @Override
    public void notifyList() {
        adapter.notifyDataSetChanged();
        connection.updateAccounts();
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
        connection.setAccountCompletion(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        connection.setAccountView(null);
    }
}
