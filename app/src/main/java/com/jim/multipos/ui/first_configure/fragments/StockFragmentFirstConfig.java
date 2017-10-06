package com.jim.multipos.ui.first_configure.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.stock.Stock;
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure.adapters.StockAdapter;
import com.jim.multipos.ui.first_configure.adapters.StockSpinnerAdapter;
import com.jim.multipos.ui.first_configure.common.BaseFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.di.FirstConfigureActivityComponent;
import com.jim.multipos.ui.first_configure.presenters.StockFragmentPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockFragmentFirstConfig extends Fragment { //} BaseFragmentFirstConfig implements StockFragmentView, StockAdapter.OnClick {
    @Inject
    StockFragmentPresenter presenter;
    @Inject
    FirstConfigureActivity activity;
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @BindView(R.id.btnRevert)
    MpButton btnRevert;
    @BindView(R.id.rvStocks)
    RecyclerView rvStocks;
    @BindView(R.id.etStockName)
    MpEditText etStockName;
    @BindView(R.id.etAddress)
    MpEditText etAddress;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    @BindView(R.id.spStockName)
    MpSpinner spStockName;
    private StockAdapter adapter;

    public StockFragmentFirstConfig() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.stock_fragment, container, false);

//        this.getComponent(FirstConfigureActivityComponent.class).inject(this);
//        presenter.init(this);
        ButterKnife.bind(this, view);
        presenter.setData();

        if (FirstConfigureActivity.SAVE_BUTTON_MODE == activity.getButtonMode()) {
            btnNext.setText(R.string.save);
        } else if (FirstConfigureActivity.NEXT_BUTTON_MODE == activity.getButtonMode()) {
            btnNext.setText(R.string.next);
        }

        RxView.clicks(ivAdd).subscribe(aVoid -> {
            String stockName = etStockName.getText().toString();
            String address = etAddress.getText().toString();

            presenter.addStock(stockName, address);
        });

        RxView.clicks(btnNext).subscribe(aVoid -> {
            presenter.openNextFragment();
        });

        RxView.clicks(btnRevert).subscribe(aVoid -> {
            activity.finish();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        hideKeyboard();
    }

    public boolean checkData() {
        return presenter.isCompleteData();
    }

    public HashMap<String, String> getData() {
        return null;
    }

    public void showStockNameError(String error) {
        etStockName.setError(error);
    }

    public void showAddressError(String error) {
        etAddress.setError(error);
    }

    public void showStockListError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    public void openNextFragment() {
        activity.openNextFragment();
    }

    public void stockAdded() {
        adapter.notifyItemInserted(0);
        spStockName.notifyDataSetChanged();
        rvStocks.scrollToPosition(0);
        etStockName.requestFocus();
    }

    public void showRecyclerView(List<Stock> stocks) {
//        adapter = new StockAdapter(stocks, this);
        rvStocks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvStocks.setAdapter(adapter);
    }

    public void remove(int position) {
        presenter.removeStock(position);
    }

    public void stockRemoved(int position) {
        adapter.notifyDataSetChanged();
        spStockName.setAdapter();
    }

    public void clearViews() {
        etAddress.setText("");
        etStockName.setText("");
    }

    public void showSpinner(List<Stock> stocks) {
        spStockName.setAdapter(new StockSpinnerAdapter(getContext(), R.layout.item_spinner, stocks));
    }

    public void saveData() {
        presenter.saveData();
    }
}
