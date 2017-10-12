package com.jim.multipos.ui.first_configure.fragments;

import android.os.Bundle;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure.adapters.CurrencySpinnerAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by user on 10.10.17.
 */

public class CurrencyFragment extends BaseFragment {
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @BindView(R.id.btnRevert)
    MpButton btnRevert;
    @BindView(R.id.spCurrency)
    MpSpinner spCurrency;

    @Override
    protected int getLayout() {
        return R.layout.currency_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ((FirstConfigureActivity) getActivity()).getPresenter().setupCurrencyData();

        spCurrency.setOnItemSelectedListener((adapterView, view, i, l) -> {
            ((FirstConfigureActivity) getActivity()).getPresenter().changeCurrency(((Currency) spCurrency.getAdapter().getItem(i)));
        });
    }

    @Override
    protected void rxConnections() {

    }

    public void setCurrencySpinnerData(List<Currency> currencies, int position) {
        spCurrency.setAdapter(new CurrencySpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, currencies));
        spCurrency.setSelection(position);
    }
}
