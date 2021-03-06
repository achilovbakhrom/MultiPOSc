package com.jim.multipos.ui.reports.debts;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportPresenter;

public interface DebtsReportPresenter extends BaseTableReportPresenter {
    void filterConfigsHaveChanged(int[] config);
    void exportExcel(String fileName, String path);
    void exportPdf(String fileName, String path);
    void exportExcelToUSB(String filename, UsbFile root);
    void exportPdfToUSB(String filename, UsbFile root);
    void onAction(Object[][] objects, int row, int column);
}
