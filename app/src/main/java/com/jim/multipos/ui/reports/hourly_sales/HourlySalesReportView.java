package com.jim.multipos.ui.reports.hourly_sales;

import com.jim.multipos.core.BaseTableReportView;

public interface HourlySalesReportView extends BaseTableReportView{
    void initTable(Object[][] objects);
    void updateTable(Object[][] objects);
}
