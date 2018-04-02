package com.jim.multipos.ui.reports.discount;

import com.jim.multipos.core.BaseTableReportView;

public interface DiscountReportView extends BaseTableReportView {
    void initItemDiscount();
    void initOrderDiscount();
    void initDiscountCreationLog();
    void initTable(Object[][] objects);
}
