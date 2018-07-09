package com.jim.multipos.ui.reports.stock_state;

import com.jim.multipos.core.BaseTableReportView;

public interface StockStateView extends BaseTableReportView {
    void initTable(Object[][] objects);
    void updateTable(Object[][] objects, int position);
    void setSearchResults(Object[][] searchResults, String searchText, int position);
    void showFilterDialog(int[] filterConfig);
    void showSummaryFilterDialog(int filterValue);
}
