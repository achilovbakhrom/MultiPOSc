package com.jim.multipos.ui.reports.tills;

import com.jim.multipos.core.BaseTableReportView;
import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.till.Till;

/**
 * Created by Sirojiddin on 27.03.2018.
 */

public interface TillsReportView extends BaseTableReportView {
    void fillReportView(Object[][] objects);
    void openTillDetailsDialog(Till till);
    void setSearchResults(Object[][] objects, String searchText);
    void updateReportView(Object[][] objects);
}
