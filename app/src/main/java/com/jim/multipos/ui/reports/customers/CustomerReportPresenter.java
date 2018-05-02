package com.jim.multipos.ui.reports.customers;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportPresenter;

public interface CustomerReportPresenter extends BaseTableReportPresenter {
    void changeSummaryConfig(int config);
    void changePaymentFilter(int[] config);
    void exportExcel(String fileName, String path);
    void exportPdf(String fileName, String path);
    void exportExcelToUSB(String filename, UsbFile root);
    void exportPdfToUSB(String filename, UsbFile root);
    void onBarcodeReaded(String barcode);
}
