package com.jim.multipos.ui.reports.stock_operations;

import com.jim.multipos.core.BaseTableReportView;
import com.jim.multipos.core.BaseView;

public interface StockOperationView extends BaseTableReportView {
    void initTable(Object[][] objects);
    void updateTable(Object[][] objects, int position);
    void setSearchResults(Object[][] searchResults, String searchText, int position);
    void showFilterDialog(int[] filterConfig,int currentPage);
}
