package com.jim.multipos.ui.reports.order_history;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportPresenter;
import com.jim.multipos.core.Presenter;

import java.util.Calendar;

public interface OrderHistoryPresenter extends BaseTableReportPresenter {
    void onActionClicked(Object[][] objects, int row, int column);
    void filterConfigsChanged(int[] configs);
    void exportExcel(String fileName, String path);
    void exportPdf(String fileName, String path);
    void exportExcelToUSB(String filename, UsbFile root);
    void exportPdfToUSB(String filename, UsbFile root);
}
