package com.jim.multipos.ui.reports.inventory;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportPresenter;
import com.jim.multipos.data.db.model.till.Till;

public interface InventoryReportPresenter extends BaseTableReportPresenter {
    void setFilterConfigs(int[] config);
    void setPickedTill(Till till);
    void exportExcel(String fileName, String path);
    void exportPdf(String fileName, String path);
    void exportExcelToUSB(String filename, UsbFile root);
    void exportPdfToUSB(String filename, UsbFile root);
    void onBarcodeReaded(String barcode);
    void onAction(Object[][] objects, int row, int column);
}
