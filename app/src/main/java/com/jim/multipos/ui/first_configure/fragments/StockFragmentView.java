package com.jim.multipos.ui.first_configure.fragments;

import com.jim.multipos.data.db.model.stock.Stock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 14.08.17.
 */

public interface StockFragmentView {
    void showStockNameError(String error);
    void showAddressError(String error);
    void showStockListError(String error);
    void openNextFragment();
    void stockAdded();
    void showRecyclerView(List<Stock> stocks);
    void stockRemoved(int position);
    void clearViews();
    void showSpinner(List<Stock> stocks);
}
