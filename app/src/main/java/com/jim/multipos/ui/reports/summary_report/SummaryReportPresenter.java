package com.jim.multipos.ui.reports.summary_report;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.Presenter;

import java.util.Calendar;

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
    void exportExcel(String filename, String root);
    void exportPdf(String filename, String root);
    void exportExcelToUSB(String filename, UsbFile root);
    void exportPdfToUSB(String filename, UsbFile root);
}
