package com.jim.multipos.ui.reports.tills;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.till.Till;

/**
 * Created by Sirojiddin on 27.03.2018.
 */

public interface TillsReportView extends BaseView {
    void fillReportView(Object[][] objects, int[] dataType, String[] titles, int[] weights, int[] aligns);
    void openTillDetailsDialog(Till till);
}
