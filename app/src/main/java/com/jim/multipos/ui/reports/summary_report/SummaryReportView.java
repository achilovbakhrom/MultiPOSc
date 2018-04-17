package com.jim.multipos.ui.reports.summary_report;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.ui.reports.summary_report.adapter.PairString;
import com.jim.multipos.ui.reports.summary_report.adapter.TripleString;

import java.util.Calendar;
import java.util.List;

public interface SummaryReportView extends BaseView {
    void updateDateIntervalUi(Calendar fromDate, Calendar toDate);
    void openDateInterval(Calendar fromDate,Calendar toDate);
    void setSingleTitle(String titleReport);
    void setChoiserPanel(String[] titles);
    void showExportPanel();
    void activeSalesSummary();
    void activePaymentsSummary();
    void updateRecyclerViewSummary(List<PairString> pairStringList);
    void updateRecyclerViewPayments(List<TripleString> tripleStrings);
    void updatecyclerViewAnalitcs(List<PairString> pairStringList);
}
