package com.jim.multipos.ui.first_configure_last.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCompletedStateView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.first_configure_last.CompletionMode;
import com.jim.multipos.ui.first_configure_last.ChangeableContent;
import com.jim.multipos.ui.first_configure_last.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure_last.FirstConfigurePresenter;
import com.jim.multipos.ui.first_configure_last.adapter.AccountAdapter;
import com.jim.multipos.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

/**
 * Created by Achilov Bakhrom on 10/18/17.
 */

public class AccountFragment extends BaseFragment implements ChangeableContent {

    @NotEmpty(messageId = R.string.enter_account_name)
    @BindView(R.id.etAccountName)
    EditText accountName;
    @BindView(R.id.btnNext)
    MpButton next;
    @BindView(R.id.btnRevert)
    MpButton revert;
    @BindView(R.id.rvSystemAccounts)
    RecyclerView accounts;
    @BindView(R.id.llAccountsWarning)
    LinearLayout warning;
    @BindView(R.id.tvWarningText)
    TextView tvWarningText;

    private CompletionMode mode = CompletionMode.NEXT;

    private AccountAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.account_fragment;
    }

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            setMode((CompletionMode) getArguments().getSerializable(FirstConfigureActivity.CONFIRM_BUTTON_KEY));
        }
        FirstConfigurePresenter presenter = ((FirstConfigureActivity) getContext()).getPresenter();
        accounts.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.getObservableAccounts().subscribe(accounts -> {
            adapter = new AccountAdapter(accounts);
            AccountFragment.this.accounts.setAdapter(adapter);
            adapter.setItemRemoveListener((position, item) -> {
                Context context = AccountFragment.this.getContext();
                UIUtils.showAlert(context, context.getString(R.string.yes), context.getString(R.string.no),
                        getString(R.string.deleting_account), getString(R.string.deleting_account_message),
                        new UIUtils.AlertListener() {
                            @Override
                            public void onPositiveButtonClicked() {
                                adapter.removeItem(position);
                                presenter.removeAccount(item);
                            }

                            @Override
                            public void onNegativeButtonClicked() {

                            }
                        });
            });
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (hasAnyAccount()) {
                warning.setVisibility(View.GONE);
            } else {
                warning.setVisibility(View.VISIBLE);
                tvWarningText.setText(getString(R.string.warning_account_add));
            }
            ((FirstConfigureActivity) getContext()).getPresenter().checkAccountCorrection();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(FirstConfigureActivity.CONFIRM_BUTTON_KEY, mode);
    }

    @OnClick(value = {R.id.ivAdd, R.id.btnRevert, R.id.btnNext})
    public void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.ivAdd:
                UIUtils.closeKeyboard(view, getContext());
                if (isValid()) {
                    FirstConfigurePresenter presenter = ((FirstConfigureActivity) getContext()).getPresenter();
                    if (!presenter.isAccountNameExists(accountName.getText().toString())) {
                        ((AccountAdapter) accounts.getAdapter()).addItem(
                                ((FirstConfigureActivity) getContext()).getPresenter().addAccount(
                                        accountName.getText().toString()
                                )
                        );
                    } else {
                        accountName.setError(getString(R.string.account_name_exists));
                    }
                }
                accountName.setText("");
                break;
            case R.id.btnRevert:
                ((FirstConfigureActivity) getContext()).getPresenter().openCurrency();
                break;
            case R.id.btnNext:
                if (hasAnyAccount()) {
                    if (mode == CompletionMode.NEXT) {
                        warning.setVisibility(View.GONE);
                        FirstConfigureActivity activity = (FirstConfigureActivity) getContext();
                        activity.getPresenter().setCompletedForFragment(getClass().getName(), true);
                        activity.getPresenter().openPaymentType();
                        activity.changeState(FirstConfigurePresenter.ACCOUNT_POSITION, MpCompletedStateView.COMPLETED_STATE);
                    } else {
                        ((FirstConfigureActivity) getContext()).getPreferencesHelper().setAppRunFirstTimeValue(false);
                        ((FirstConfigureActivity) getActivity()).openLockScreen();
                    }
                } else {
                    tvWarningText.setText(R.string.warning_account_add);
                    warning.setVisibility(View.VISIBLE);
                    ((FirstConfigureActivity) getContext())
                            .changeState(FirstConfigurePresenter.ACCOUNT_POSITION, MpCompletedStateView.WARNING_STATE);
                }
                break;
        }
    }

    public boolean hasAnyAccount() {
        return !((FirstConfigureActivity) getContext()).getPresenter().getAccounts().isEmpty();
    }

    /**
     * setter for type of next button, which allowed
     * two type: NEXT and FINISH
     *
     * @param mode - for the button, which type of CompletionMode
     */
    @Override
    public void setMode(CompletionMode mode) {
        this.mode = mode;
        switch (mode) {
            case NEXT:
                next.setText(R.string.next);
                break;
            case FINISH:
                next.setText(R.string.finish);
                break;
        }
    }

}
