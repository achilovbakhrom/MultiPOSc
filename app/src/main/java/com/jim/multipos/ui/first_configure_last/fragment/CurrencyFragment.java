package com.jim.multipos.ui.first_configure_last.fragment;

import android.os.Bundle;
import android.view.View;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCompletedStateView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.ui.first_configure_last.CompletionMode;
import com.jim.multipos.ui.first_configure_last.ChangeableContent;
import com.jim.multipos.ui.first_configure_last.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure_last.FirstConfigurePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Achilov Bakhrom on 10/18/17.
 */

public class CurrencyFragment extends BaseFragment implements ChangeableContent {

    @BindView(R.id.btnNext)
    MpButton next;
    @BindView(R.id.btnRevert)
    MpButton revert;
    @BindView(R.id.spCurrency)
    MPosSpinner currency;
    private CompletionMode mode = CompletionMode.NEXT;

    @Override
    protected int getLayout() {
        return R.layout.currency_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            setMode((CompletionMode) getArguments().getSerializable(FirstConfigureActivity.CONFIRM_BUTTON_KEY));
        }
        initCurrencies();
    }

    private void initCurrencies() {
        List<Currency> dbCurrencies = ((FirstConfigureActivity) getContext()).getPresenter().getDbCurrencies();
        if (dbCurrencies == null || dbCurrencies.isEmpty()) {
            ((FirstConfigureActivity) getContext()).getPresenter().createCurrency(null, null);
        }
        currency.setAdapter(((FirstConfigureActivity) getContext()).getPresenter().getSpinnerCurrencies());
        currency.setItemSelectionListener((view, position) -> {
            List<Currency> currencies = ((FirstConfigureActivity) getContext()).getPresenter().getCurrencies();
            ((FirstConfigureActivity) getContext()).getPresenter().createCurrency(currencies.get(position).getName(), currencies.get(position).getAbbr());
        });
    }

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    @OnClick(value = {R.id.btnNext, R.id.btnRevert})
    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                if (hasAnyCurrency()) {
                    if (mode == CompletionMode.NEXT) {
                        FirstConfigureActivity activity = (FirstConfigureActivity) getContext();
                        activity.getPresenter().setCompletedForFragment(getClass().getName(), true);
                        activity.getPresenter().openPaymentType();
                        activity.changeState(FirstConfigurePresenter.CURRENCY_POSITION, MpCompletedStateView.COMPLETED_STATE);
                    } else {
                        ((FirstConfigureActivity) getActivity()).openLockScreen();
                    }
                } else {
                    ((FirstConfigureActivity) getContext())
                            .changeState(FirstConfigurePresenter.CURRENCY_POSITION, MpCompletedStateView.WARNING_STATE);
                }
                break;
            case R.id.btnRevert:
                ((FirstConfigureActivity) getContext()).getPresenter().openAccount();
                break;
        }
    }

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

    private boolean hasAnyCurrency() {
        return !((FirstConfigureActivity) getContext()).getPresenter().getDbCurrencies().isEmpty();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(FirstConfigureActivity.CONFIRM_BUTTON_KEY, mode);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            ((FirstConfigureActivity) getContext()).getPresenter().checkCurrencyCorrection();
        }
    }
}
