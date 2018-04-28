package com.jim.multipos.ui.reports.tills;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportView;
import com.jim.multipos.data.db.model.till.Till;

/**
 * Created by Sirojiddin on 27.03.2018.
 */

public interface TillsReportView extends BaseTableReportView {
    void fillReportView(Object[][] objects);
    void openTillDetailsDialog(Till till);
    void setSearchResults(Object[][] objects, String searchText);
    void updateReportView(Object[][] objects);
    void exportTableToExcel(String fileName, String path, Object[][] objects, String date, String searchText);
    void exportTableToPdf(String fileName, String path, Object[][] objects, String date, String searchText);
    void openExportDialog(int mode);
    void exportExcelToUSB(String filename, UsbFile root, Object[][] objects, String date, String searchText);
    void exportTableToPdfToUSB(String fileName, UsbFile path, Object[][] objects, String date, String searchText);
}
