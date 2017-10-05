package com.jim.multipos.ui.first_configure.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure.adapters.CurrencyAdapter;
import com.jim.multipos.ui.first_configure.adapters.CurrencySpinnerAdapter;
import com.jim.multipos.ui.first_configure.common.BaseFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.di.FirstConfigureActivityComponent;
import com.jim.multipos.ui.first_configure.presenters.CurrencyFragmentPresenter;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrencyFragmentFirstConfig extends BaseFragmentFirstConfig implements CurrencyFragmentView, CurrencyAdapter.OnClick {
    @Inject
    CurrencyFragmentPresenter presenter;
    @Inject
    FirstConfigureActivity activity;
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @BindView(R.id.btnRevert)
    MpButton btnRevert;
    @BindView(R.id.spSystemCurrency)
    MpSpinner spSystemCurrency;
    @BindView(R.id.spOtherCurrencies)
    MpSpinner spOtherCurrencies;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    @BindView(R.id.rvCurrencies)
    RecyclerView rvCurrencies;
    private CurrencyAdapter adapter;

    public CurrencyFragmentFirstConfig() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.currency_fragment, container, false);

        this.getComponent(FirstConfigureActivityComponent.class).inject(this);
        presenter.init(this);

        ButterKnife.bind(this, view);

        presenter.setData();

        spSystemCurrency.setOnItemSelectedListener((adapterView, view1, i, l) -> {
            presenter.changeMainCurrency(i);
        });

        if (FirstConfigureActivity.SAVE_BUTTON_MODE == activity.getButtonMode()) {
            btnNext.setText(R.string.save);
        } else if (FirstConfigureActivity.NEXT_BUTTON_MODE == activity.getButtonMode()) {
            btnNext.setText(R.string.next);
        }

        RxView.clicks(btnNext).subscribe(aVoid -> {
            activity.openNextFragment();
        });

        RxView.clicks(ivAdd).subscribe(aVoid -> {
            presenter.addCurrency(spOtherCurrencies.selectedItemPosition());
        });

        RxView.clicks(btnRevert).subscribe(aVoid -> {
            activity.finish();
        });

        return view;
    }


    @Override
    public HashMap<String, String> getData() {
        return null;
    }


    @Override
    public boolean checkData() {
        return presenter.isCompleteData();
    }

    @Override
    public void onClick(int position) {
        presenter.removeSelectedCurrency(position);
    }

    @Override
    public void showMainCurrencies(List<Currency> currencies, int position) {
        CurrencySpinnerAdapter adapter = new CurrencySpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, currencies);
        spSystemCurrency.setAdapter(adapter);
        spSystemCurrency.setSelection(position);
    }

    @Override
    public void showOtherCurrencies(List<Currency> currencies) {
        CurrencySpinnerAdapter adapter = new CurrencySpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, currencies);
        spOtherCurrencies.setAdapter(adapter);
    }

    @Override
    public void updateView() {
        spOtherCurrencies.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
        rvCurrencies.scrollToPosition(0);
    }

    @Override
    public void showRecyclerView(List<Currency> currencies) {
        adapter = new CurrencyAdapter(currencies, this);
        rvCurrencies.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCurrencies.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        hideKeyboard();
    }

    @Override
    public void saveData() {
        presenter.saveData();
    }
}
