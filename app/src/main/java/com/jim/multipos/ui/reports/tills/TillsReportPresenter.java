package com.jim.multipos.ui.reports.tills;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportPresenter;

/**
 * Created by Sirojiddin on 27.03.2018.
 */

public interface TillsReportPresenter extends BaseTableReportPresenter {
    void openTillDetailsDialog(Object[][] objects, int row, int column);
    void exportExcel(String fileName, String path);
    void exportPdf(String fileName, String path);
    void exportExcelToUSB(String filename, UsbFile root);
    void exportPdfToUSB(String filename, UsbFile root);
}
