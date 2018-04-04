package com.jim.multipos.ui.reports.discount;

import com.jim.multipos.core.BaseTableReportView;

public interface DiscountReportView extends BaseTableReportView {
    void initValues();
    void initTable(Object[][] objects);
    void updateTable(Object[][] objects, int position);
    void showFilterDialog(int[] filterConfig);
    void setSearchResults(Object[][] objectResults, String searchText, int currentPosition);
}
