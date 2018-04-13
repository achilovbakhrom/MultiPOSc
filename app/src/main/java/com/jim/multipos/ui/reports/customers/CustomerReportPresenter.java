package com.jim.multipos.ui.reports.customers;

import com.jim.multipos.core.BaseTableReportPresenter;

public interface CustomerReportPresenter extends BaseTableReportPresenter {
    void changeSummaryConfig(int config);
    void changePaymentFilter(int[] config);
}
