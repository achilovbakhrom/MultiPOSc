package com.jim.multipos.ui.reports.product_profit;

import android.content.Context;
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
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.ui.reports.order_history.dialogs.OrderDetialsDialog;
import com.jim.multipos.ui.reports.product_profit.dialog.ProductProfitFilterDialog;
import com.jim.multipos.utils.ExportToDialog;
import com.jim.multipos.utils.ExportUtils;

import java.io.File;

import javax.inject.Inject;

import static com.jim.multipos.utils.ExportUtils.EXCEL;

public class ProductProfitFragment extends BaseTableReportFragment implements ProductProfitView {
    @Inject
    ProductProfitPresenter presenter;
    @Inject
    DatabaseManager databaseManager;
    private FrameLayout fl;

    private ReportView.Builder profitSummaryBuilder, profitLogBuilder;
    private ReportView profitSummaryView, profitLogView;

    int profitSumType[] = {ReportViewConstants.NAME, ReportViewConstants.QUANTITY, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT};
    String profitSumTitles[];
    int profitSumWeights[] = {10, 10, 10, 10, 10, 10, 10, 10};
    int profitSumAligns[] = {Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT};

    int profitLogType[] = {ReportViewConstants.NAME, ReportViewConstants.DATE, ReportViewConstants.ACTION, ReportViewConstants.QUANTITY, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT,  ReportViewConstants.NAME};
    String profitLogTitles[];
    int profitLogWeights[] = {10, 10, 6, 10, 10, 10, 10,  10};
    int profitLogAligns[] = {Gravity.LEFT, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT,Gravity.RIGHT,  Gravity.LEFT};
    private String panelNames[];

    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        panelNames = getContext().getResources().getStringArray(R.array.product_profit_titles);
        setChoiserPanel(panelNames);
        initTableConfigs(getContext());
        profitSummaryBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(profitSumTitles)
                .setDataTypes(profitSumType)
                .setWeight(profitSumWeights)
                .setDataAlignTypes(profitSumAligns)
                .setDefaultSort(0)
                .build();
        profitLogBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(profitLogTitles)
                .setDataTypes(profitLogType)
                .setWeight(profitLogWeights)
                .setDataAlignTypes(profitLogAligns)
                .setDefaultSort(1)
                .setOnReportViewResponseListener((objects1, row, column) -> {
                    presenter.onActionClicked(objects1,row,column);
                })
                .build();
        profitSummaryView = new ReportView(profitSummaryBuilder);
        profitLogView = new ReportView(profitLogBuilder);
        presenter.onCreateView(savedInstanceState);

    }

    private void initTableConfigs(Context context) {
        profitSumTitles = context.getResources().getStringArray(R.array.product_profit_summary_table_titles);
        profitLogTitles = context.getResources().getStringArray(R.array.product_profit_log_table_titles);
    }

    @Override
    public void initTable(Object[][] objects, int position) {
        switch (position) {
            case 0:
                profitSummaryView.getBuilder().init(objects);
                fl = profitSummaryView.getBuilder().getView();
                setTable(fl);
                enableFilter();
                clearSearch();
                break;
            case 1:
                profitLogView.getBuilder().init(objects);
                fl = profitLogView.getBuilder().getView();
                setTable(fl);
                disableFilter();
                clearSearch();
                break;
        }

    }

    @Override
    public void updateTable(Object[][] objects, int position) {
        switch (position) {
            case 0:
                profitSummaryView.getBuilder().update(objects);
                fl = profitSummaryView.getBuilder().getView();
                enableFilter();
                clearSearch();
                break;
            case 1:
                profitLogView.getBuilder().update(objects);
                fl = profitLogView.getBuilder().getView();
                disableFilter();
                clearSearch();
        }
        setTable(fl);
    }

    @Override
    public void searchTable(Object[][] objects, int position, String searchtext) {
        switch (position) {
            case 0:
                profitSummaryView.getBuilder().searchResults(objects, searchtext);
                fl = profitSummaryView.getBuilder().getView();
                enableFilter();

                break;
            case 1:
                profitLogView.getBuilder().searchResults(objects, searchtext);
                fl = profitLogView.getBuilder().getView();
                disableFilter();
        }
        setTable(fl);
    }

    @Override
    public void showFilterPanel(int config) {
        ProductProfitFilterDialog productProfitFilterDialog = new ProductProfitFilterDialog(getContext(), config, config1 -> {
            presenter.filterConfigsChanged(config1);
            clearSearch();
        });
        productProfitFilterDialog.show();
    }

    @Override
    public void exportTableToExcel(String filename, String path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = getContext().getString(R.string.product_profit_summary_description);
                ExportUtils.exportToExcel(getContext(), path, filename, description, date, filter, searchText, objects, profitSumTitles, profitSumWeights, profitSumType, null);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.product_profit_sales_log_description);
                ExportUtils.exportToExcel(getContext(), path, filename, secondDescription, date, filter, searchText, objects, profitLogTitles, profitLogWeights, profitLogType, null);
                break;
        }
    }

    @Override
    public void exportTableToPdf(String filename, String path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = getContext().getString(R.string.product_profit_summary_description);
                ExportUtils.exportToPdf(getContext(), path, filename, description, date, filter, searchText, objects, profitSumTitles, profitSumWeights, profitSumType, null);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.product_profit_sales_log_description);
                ExportUtils.exportToPdf(getContext(), path, filename, secondDescription, date, filter, searchText, objects, profitLogTitles, profitLogWeights, profitLogType, null);
                break;
        }
    }

    ExportToDialog exportDialog;

    @Override
    public void openExportDialog(int position, int mode) {
        exportDialog = new ExportToDialog(getContext(), mode, panelNames[position], new ExportToDialog.OnExportListener() {
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
        dialog.setNegativeBtnName(getContext().getString(R.string.cancel));
        dialog.setPositiveBtnName(getContext().getString(R.string.select));
        dialog.setTitle(getContext().getString(R.string.select_a_directory));
        dialog.setDialogSelectionListener(files -> {
            exportDialog.setPath(files);
        });
        dialog.show();
    }

    @Override
    public void exportExcelToUSB(String filename, UsbFile root, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = getContext().getString(R.string.product_profit_summary_description);
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, description, date, filter, searchText, objects, profitSumTitles, profitSumWeights, profitSumType, null);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.product_profit_sales_log_description);
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, secondDescription, date, filter, searchText, objects, profitLogTitles, profitLogWeights, profitLogType, null);
                break;
        }
    }

    @Override
    public void exportTableToPdfToUSB(String fileName, UsbFile path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = getContext().getString(R.string.product_profit_summary_description);
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, description, date, filter, searchText, objects, profitSumTitles, profitSumWeights, profitSumType, null);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.product_profit_sales_log_description);
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, profitLogTitles, profitLogWeights, profitLogType, null);
                break;
        }
    }

    @Override
    public void onOrderPressed(Order order) {
        OrderDetialsDialog orderDetialsDialog = new OrderDetialsDialog(getContext(), order, databaseManager);
        orderDetialsDialog.show();
    }

    public void onBarcodeScaned(String barcode){
        presenter.onBarcodeReaded(barcode);
    }
}
