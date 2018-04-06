package com.jim.multipos.ui.reports.service_fee;

import com.jim.multipos.core.BaseTableReportPresenter;

public interface ServiceFeeReportPresenter extends BaseTableReportPresenter {
    void filterConfigsHaveChanged(int[] config);
}
