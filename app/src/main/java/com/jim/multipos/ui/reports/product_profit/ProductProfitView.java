package com.jim.multipos.ui.reports.product_profit;

import com.jim.multipos.core.BaseTableReportView;

public interface ProductProfitView extends BaseTableReportView {
    void initTable(Object[][] objects, int position);
    void updateTable(Object[][] objects, int position);
    void searchTable(Object[][] objects, int position,String searchtext);
    void showFilterPanel(int config);

}
