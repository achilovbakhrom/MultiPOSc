package com.jim.multipos.ui.reports.product_profit;

import com.jim.multipos.core.BaseTableReportPresenter;

public interface ProductProfitPresenter extends BaseTableReportPresenter {
    void filterConfigsChanged(int configs);
}
