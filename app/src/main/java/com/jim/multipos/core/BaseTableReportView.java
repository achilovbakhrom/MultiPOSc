package com.jim.multipos.core;

import java.util.Calendar;

public interface BaseTableReportView extends BaseView {
    void updateDateIntervalUi(Calendar fromDate,Calendar toDate);
    void openDateInterval(Calendar fromDate,Calendar toDate);
    void setTitleReport(String titleReport);
    void setChoiserPanel(String[] titles);
    void showExportPanel();
    void showFilterPanel();
}
