package com.jim.multipos.ui.reports.payments;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportPresenter;

public interface PaymentsReportPresenter extends BaseTableReportPresenter {
    void filterConfigsChanged(int[] configs);
    void onActionPressed(Object[][] objects, int row, int column);
    void exportExcel(String fileName, String path);
    void exportPdf(String fileName, String path);
    void exportExcelToUSB(String filename, UsbFile root);
    void exportPdfToUSB(String filename, UsbFile root);
}
