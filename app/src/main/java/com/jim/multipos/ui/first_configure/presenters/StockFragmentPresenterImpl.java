package com.jim.multipos.ui.first_configure.presenters;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.stock.Stock;
import com.jim.multipos.data.operations.StockOperations;
import com.jim.multipos.ui.first_configure.fragments.StockFragmentView;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by user on 14.08.17.
 */

public class StockFragmentPresenterImpl implements StockFragmentPresenter {
    private StockFragmentView view;
    private List<Stock> stocks;
    private Context context;
    private StockOperations stockOperations;

    public StockFragmentPresenterImpl(Context context, StockOperations stockOperations) {
        stocks = new ArrayList<>();
        this.context = context;
        this.stockOperations = stockOperations;
    }

    @Override
    public void init(StockFragmentView view) {
        this.view = view;
    }

    @Override
    public void openNextFragment() {
        if (!stocks.isEmpty()) {
            view.openNextFragment();
        } else {
            view.showStockListError(getString(R.string.create_leaste_one_stock));
        }
    }

    private boolean checkData(String stockName, String address) {
        boolean hasError = false;

        if (stockName.isEmpty()) {
            hasError = true;
            view.showStockNameError(getString(R.string.enter_stock_name));
        }

        if (address.isEmpty()) {
            hasError = true;
            view.showAddressError(getString(R.string.enter_address));
        }

        return hasError;
    }

    @Override
    public void addStock(String stockName, String address) {
        boolean hasError = checkData(stockName, address);

        if (!hasError) {
            Stock stock = new Stock();
            stock.setName(stockName);
            stock.setAddress(address);

            stocks.add(0, stock);
            view.stockAdded();
            view.clearViews();
        }
    }

    private String getString(int resId) {
        return context.getString(resId);
    }

    @Override
    public void setData() {
        stockOperations.getAllStocks().subscribe(stocks -> {
            this.stocks = stocks;

            view.showSpinner(stocks);
            view.showRecyclerView(stocks);
        });
    }

    @Override
    public void removeStock(int position) {
        stocks.remove(position);
        view.stockRemoved(position);
    }

    @Override
    public boolean isCompleteData() {
        return !stocks.isEmpty();
    }

    @Override
    public void saveData() {
        stockOperations.removeAllStocks().subscribe(aBoolean -> {
            stockOperations.addStocks(stocks).subscribe();
        });
    }
}
