package com.jim.multipos.ui.reports.service_fee;

import com.jim.multipos.core.BaseTableReportView;

public interface ServiceFeeReportView extends BaseTableReportView {
    void initTable(Object[][] objects);
    void updateTable(Object[][] objects, int position);
    void setSearchResults(Object[][] objectResults, String searchText, int position);
    void showFilterDialog(int[] filterConfig);
}
