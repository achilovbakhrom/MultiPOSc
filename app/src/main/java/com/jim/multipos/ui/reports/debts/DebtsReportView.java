package com.jim.multipos.ui.reports.debts;

import com.jim.multipos.core.BaseTableReportView;

public interface DebtsReportView extends BaseTableReportView {
    void initTable(Object[][] objects);
    void updateTable(Object[][] objects, int position);
    void setSearchResults(Object[][] searchResults, String searchText, int position);
    void showFilterDialog(int[] filterConfig);
}
