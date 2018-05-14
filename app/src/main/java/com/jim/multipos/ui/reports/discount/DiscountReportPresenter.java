package com.jim.multipos.ui.reports.discount;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportPresenter;

public interface DiscountReportPresenter extends BaseTableReportPresenter {
    void filterConfigsHaveChanged(int[] config);
    void exportExcelToUSB(String filename, UsbFile root);
    void exportPdfToUSB(String filename, UsbFile root);
    void exportExcel(String fileName, String path);
    void exportPdf(String fileName, String path);
    void onAction(Object[][] objects, int row, int column);
}
