package com.jim.multipos.ui.reports.product_profit;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.core.BaseTableReportPresenter;

public interface ProductProfitPresenter extends BaseTableReportPresenter {
    void filterConfigsChanged(int configs);
    void exportExcel(String fileName, String path);
    void exportPdf(String fileName, String path);
    void exportExcelToUSB(String filename, UsbFile root);
    void exportPdfToUSB(String filename, UsbFile root);
    void onBarcodeReaded(String barcode);

}
