package com.jim.multipos.ui.reports.summary_report;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.ui.reports.summary_report.adapter.PairString;
import com.jim.multipos.ui.reports.summary_report.adapter.TripleString;

import java.util.Calendar;
import java.util.List;

public interface SummaryReportPresenter extends Presenter {
    void onChooseDateInterval(Calendar fromDate, Calendar toDate);
    void onSearchTyped(String searchText);
    void onClickedDateInterval();
    void onClickedExportExcel();
    void onClickedExportPDF();
    void onClickedFilter();
    void onChoisedPanel(int postion);
    void onSalesSummary();
    void onPaymentsSummary();

}
