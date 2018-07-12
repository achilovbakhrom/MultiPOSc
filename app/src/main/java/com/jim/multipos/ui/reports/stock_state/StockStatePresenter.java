package com.jim.multipos.ui.reports.stock_state;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportPresenter;

public interface StockStatePresenter extends BaseTableReportPresenter {
    void exportExcelToUSB(String filename, UsbFile root);
    void exportPdfToUSB(String filename, UsbFile root);
    void exportExcel(String fileName, String path);
    void exportPdf(String fileName, String path);
}
