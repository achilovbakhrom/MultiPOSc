package com.jim.multipos.ui.reports.inventory;

import com.jim.multipos.core.BaseTableReportPresenter;

public interface InventoryReportPresenter extends BaseTableReportPresenter {
    void setFilterConfigs(int[] config);
}
