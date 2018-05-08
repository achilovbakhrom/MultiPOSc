package com.jim.multipos.ui.settings.currency;

import android.os.Bundle;
import android.view.View;

import com.jim.mpviews.MPosSpinner;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.settings.SettingsActivity;

import javax.inject.Inject;

import butterknife.BindView;

public class CurrencySettingsFragment extends BaseFragment implements CurrencyView{

    @BindView(R.id.spCurrency)
    MPosSpinner spCurrency;
    @Inject
    CurrencyPresenter presenter;

    @Override
    protected int getLayout() {
        return R.layout.currency_settings_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.initCurrency();
    }

    @Override
    public void setItemsToSpinner(String[] items) {
        spCurrency.setAdapter(items);
        spCurrency.setItemSelectionListener((view, position) -> {
            presenter.setCurrency(position);
            ((SettingsActivity) getContext()).setChanged(true);
        });
    }

    @Override
    public void setSelection(int position) {
        spCurrency.setSelection(position);
    }
}
