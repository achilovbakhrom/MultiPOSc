package com.jim.multipos.ui.start_configuration.currency;

import android.os.Bundle;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.utils.CompletionMode;
import com.jim.multipos.ui.start_configuration.StartConfigurationActivity;
import com.jim.multipos.ui.start_configuration.connection.StartConfigurationConnection;
import com.jim.multipos.utils.UIUtils;

import javax.inject.Inject;

import butterknife.BindView;

public class CurrencyFragment extends BaseFragment implements CurrencyView {

    @BindView(R.id.spCurrency)
    MPosSpinner spCurrency;
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @Inject
    CurrencyPresenter presenter;
    @Inject
    StartConfigurationConnection connection;
    private CompletionMode mode = CompletionMode.NEXT;

    @Override
    protected int getLayout() {
        return R.layout.currency_configure_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        connection.setCurrencyView(this);
        UIUtils.closeKeyboard(spCurrency, getContext());
        presenter.initCurrency();
        btnNext.setOnClickListener(view -> {
            if (mode == CompletionMode.NEXT) {
                connection.setCurrencyCompletion(true);
                connection.openNextFragment(3);
            } else {
                connection.setCurrencyCompletion(true);
                presenter.setAppRunFirstTimeValue(false);
                ((StartConfigurationActivity) getActivity()).openLockScreen();
            }
        });
    }

    @Override
    public void setItemsToSpinner(String[] items) {
        spCurrency.setAdapter(items);
        spCurrency.setItemSelectionListener((view, position) -> {
            presenter.setCurrency(position);
        });
    }

    @Override
    public void setSelection(int position) {
        spCurrency.setSelection(position);
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
        connection.setCurrencyCompletion(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        connection.setCurrencyView(null);
    }
}
