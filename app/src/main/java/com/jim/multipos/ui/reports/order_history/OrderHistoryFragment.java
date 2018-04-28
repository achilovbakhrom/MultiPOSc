package com.jim.multipos.ui.reports.order_history;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseTableReportFragment;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.ui.reports.order_history.dialogs.OrderDetialsDialog;
import com.jim.multipos.ui.reports.order_history.dialogs.OrderHistoryFilterDialog;
import com.jim.multipos.utils.ExportToDialog;
import com.jim.multipos.utils.ExportUtils;

import java.io.File;

import javax.inject.Inject;

import static com.jim.multipos.utils.ExportUtils.EXCEL;

public class OrderHistoryFragment extends BaseTableReportFragment implements OrderHistoryView {
    @Inject
    OrderHistoryPresenter presenter;


    int dataType[] = {ReportViewConstants.ID, ReportViewConstants.ACTION, ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.STATUS, ReportViewConstants.NAME, ReportViewConstants.AMOUNT,ReportViewConstants.AMOUNT,ReportViewConstants.ACTION};
    String titles[], description;
    int weights[] = {9, 7, 12, 12, 9, 12, 12,12,10};
    Object[][][] statusTypes ;
    int aligns[] = {Gravity.CENTER, Gravity.CENTER, Gravity.CENTER, Gravity.LEFT, Gravity.CENTER, Gravity.LEFT, Gravity.RIGHT,Gravity.RIGHT,Gravity.CENTER};
    ReportView reportView;
    ReportView.Builder builder;

    @Override
    protected void init(Bundle savedInstanceState) {
        initDefaults();
        init(presenter);
        builder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(titles)
                .setDataTypes(dataType)
                .setWeight(weights)
                .setDataAlignTypes(aligns)
                .setDefaultSort(0)
                .setStatusTypes(statusTypes)
                .setOnReportViewResponseListener((relivantObjects,row, column) -> {
                    presenter.onActionClicked(relivantObjects,row,column);
                })
                .build();
        reportView = new ReportView(builder);
        presenter.onCreateView(savedInstanceState);
        setSingleTitle(getContext().getString(R.string.order_history_report_title));
    }



    @Override
    public void setToTable(Object[][] toTable){
        reportView.getBuilder().update(toTable);
        setTable(reportView.getBuilder().getView());
    }

    @Override
    public void setToTableFromSearch(Object[][] toTable, String searchedText) {
        reportView.getBuilder().searchResults(toTable, searchedText);
        setTable(reportView.getBuilder().getView());
    }


    @Override
    public void showFilterPanel(int[] config) {
        OrderHistoryFilterDialog orderHistoryFilterDialog = new OrderHistoryFilterDialog(getContext(), config, config1 -> {
            presenter.filterConfigsChanged(config1);
            clearSearch();
        });
        orderHistoryFilterDialog.show();
    }

    @Override
    public void initTable(Object[][] toTable) {
        reportView.getBuilder().init(toTable);
        setTable(reportView.getBuilder().getView());
    }

    @Override
    public void openOrderDetialsDialog(Order order) {
        OrderDetialsDialog orderDetialsDialog = new OrderDetialsDialog(getContext(),order);
        orderDetialsDialog.show();
    }

    @Override
    public void exportTableToExcel(String fileName, String path, Object[][] objects, String date, String filter, String searchText) {
        ExportUtils.exportToExcel(getContext(), path, fileName, description, date, filter, searchText, objects, titles, weights, dataType, null);
    }

    @Override
    public void exportTableToPdf(String fileName, String path, Object[][] objects, String date, String filter, String searchText) {
        ExportUtils.exportToPdf(getContext(), path, fileName, description, date, filter, searchText, objects, titles, weights, dataType, null);
    }

    ExportToDialog exportDialog;
    @Override
    public void openExportDialog(int mode) {
        exportDialog = new ExportToDialog(getContext(), mode, getString(R.string.order_history_report_title), new ExportToDialog.OnExportListener() {
            @Override
            public void onFilePickerClicked() {
                openFilePickerDialog();
            }

            @Override
            public void onSaveToUSBClicked(String filename, UsbFile root) {
                if (mode == EXCEL)
                    presenter.exportExcelToUSB(filename, root);
                else presenter.exportPdfToUSB(filename, root);
            }

            @Override
            public void onSaveClicked(String fileName, String path) {
                if (mode == EXCEL)
                    presenter.exportExcel(fileName, path);
                else presenter.exportPdf(fileName, path);
            }
        });
        exportDialog.show();
    }

    private void openFilePickerDialog() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.DIR_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;
        FilePickerDialog dialog = new FilePickerDialog(getContext(), properties);
        dialog.setTitle(getContext().getString(R.string.select_a_directory));
        dialog.setDialogSelectionListener(files -> {
            exportDialog.setPath(files);
        });
        dialog.show();
    }

    @Override
    public void exportExcelToUSB(String filename, UsbFile root, Object[][] objects, String date, String filter, String searchText) {
        ExportUtils.exportToExcelToUSB(getContext(), root, filename, description, date, filter, searchText, objects, titles, weights, dataType, null);

    }

    @Override
    public void exportTableToPdfToUSB(String fileName, UsbFile path, Object[][] objects, String date, String filter, String searchText) {
        ExportUtils.exportToPdfToUSB(getContext(), path, fileName, description, date, filter, searchText, objects, titles, weights, dataType, null);

    }

    private void initDefaults(){
        statusTypes = new Object[][][] {
                {{Order.CLOSED_ORDER, getString(R.string.order_status_closed),R.color.colorGreen },
                {Order.HOLD_ORDER, getString(R.string.order_status_held), R.color.colorBlue},
                {Order.CANCELED_ORDER, getString(R.string.order_status_canceled), R.color.colorRed}}
        };
        titles = new String[] {"Order ID", "Till ID", "Closed time", "Customer", "Status", "Cancel Reason", "Sub Total","To Pay Total","Actions"};
        description = "Order history report";
    }
}
