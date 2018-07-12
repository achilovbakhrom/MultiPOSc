package com.jim.multipos.ui.reports.stock_operations;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportPresenter;
import com.jim.multipos.core.Presenter;

public interface StockOperationPresenter extends BaseTableReportPresenter {
    void onAction(Object[][] objects, int row, int column);
    void filterConfigsChanged(int[] configs);
    void exportPdfToUSB(String filename, UsbFile root);
    void exportExcelToUSB(String filename, UsbFile root);
    void exportExcel(String fileName, String path);
    void exportPdf(String fileName, String path);
}
