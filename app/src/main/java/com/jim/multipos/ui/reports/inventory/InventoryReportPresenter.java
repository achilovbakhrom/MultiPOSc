package com.jim.multipos.ui.reports.inventory;

import com.jim.multipos.core.BaseTableReportPresenter;
import com.jim.multipos.data.db.model.till.Till;

public interface InventoryReportPresenter extends BaseTableReportPresenter {
    void setFilterConfigs(int[] config);
    void setPickedTill(Till till);
}
