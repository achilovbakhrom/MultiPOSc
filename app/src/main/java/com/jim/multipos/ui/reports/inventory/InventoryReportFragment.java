package com.jim.multipos.ui.reports.inventory;

import android.os.Bundle;
import android.view.Gravity;

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
import com.jim.multipos.ui.reports.inventory.dialogs.InventoryFilterDialog;
import com.jim.multipos.ui.reports.inventory.dialogs.TillPickerDialog;
import com.jim.multipos.ui.reports.order_history.dialogs.OrderDetialsDialog;
import com.jim.multipos.utils.ExportToDialog;
import com.jim.multipos.utils.ExportUtils;

import java.io.File;

import javax.inject.Inject;

import static com.jim.multipos.utils.ExportUtils.EXCEL;

public class InventoryReportFragment extends BaseTableReportFragment implements InventoryReportView {

    @Inject
    InventoryReportPresenter presenter;
    @Inject
    DatabaseManager databaseManager;
    private ReportView firstView, secondView, forthView;
    private int firstDataType[] = {ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.STATUS, ReportViewConstants.QUANTITY, ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.ACTION, ReportViewConstants.NAME};
    private int firstWeights[] = {15, 10, 15, 8, 10, 10, 8, 8};
    private int firstAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.CENTER, Gravity.LEFT, Gravity.CENTER, Gravity.CENTER};
    private int secondDataType[] = {ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.NAME};
    private int secondWeights[] = {10, 10, 10, 10, 10, 10, 10, 10};
    private int secondAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT};
    private int forthDataType[] = {ReportViewConstants.NAME, ReportViewConstants.DATE, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.QUANTITY, ReportViewConstants.NAME, ReportViewConstants.NAME};
    private int forthWeights[] = {10, 10, 10, 10, 5, 10, 10};
    private int forthAligns[] = {Gravity.LEFT, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT, Gravity.CENTER, Gravity.LEFT, Gravity.LEFT};
    private String panelNames[], firstTitles[], secondTitles[], thirdTitles[], forthTitles[];
    private Object[][][] status;

    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        panelNames = new String[]{getString(R.string.inventory_log), getString(R.string.inventory_summary), getString(R.string.returns)};
        setChoiserPanel(panelNames);
        initDefaults();
        presenter.onCreateView(savedInstanceState);
    }

    private void initDefaults() {
        //TODO: SIROJ-> CHANGE LOGIC INVENOTRY REPORT CAUSE INVENOTRY MANAGMENT LOGIC CHANGED
//        status = new Object[][][]{{{WarehouseOperations.CANCELED_SOLD, getString(R.string.canceled_order), R.color.colorMainText},
//                {WarehouseOperations.CONSIGNMENT_DELETED, getString(R.string.consignment_canceled), R.color.colorMainText},
//                {WarehouseOperations.INCOME_FROM_VENDOR, getString(R.string.income_from_vendor), R.color.colorMainText},
//                {WarehouseOperations.RETURN_HOLDED, getString(R.string.held_product_return), R.color.colorMainText},
//                {WarehouseOperations.RETURN_SOLD, getString(R.string.return_from_customer), R.color.colorMainText},
//                {WarehouseOperations.RETURN_TO_VENDOR, getString(R.string.return_to_vendor), R.color.colorMainText},
//                {WarehouseOperations.SOLD, getString(R.string.sold), R.color.colorMainText},
//                {WarehouseOperations.VOID_INCOME, getString(R.string.void_income), R.color.colorMainText},
//                {WarehouseOperations.WASTE, getString(R.string.wasted), R.color.colorMainText},}};
//        firstTitles = new String[]{getString(R.string.product), getString(R.string.vendor), getString(R.string.action), getString(R.string.qty), getString(R.string.date), getString(R.string.reason), getString(R.string.order), getString(R.string.consignment)};
//        secondTitles = new String[]{getString(R.string.product), getString(R.string.vendor), getString(R.string.sold), getString(R.string.received_from_vendor), getString(R.string.return_to_vendor), getString(R.string.return_from_customer), getString(R.string.void_income), getString(R.string.wasted)};
//        forthTitles = new String[]{getString(R.string.product), getString(R.string.date), getString(R.string.price), getString(R.string.return_cost), getString(R.string.qty), getString(R.string.payment_type), getString(R.string.description)};
//        ReportView.Builder firstBuilder = new ReportView.Builder()
//                .setContext(getContext())
//                .setTitles(firstTitles)
//                .setDataTypes(firstDataType)
//                .setWeight(firstWeights)
//                .setDefaultSort(4)
//                .setStatusTypes(status)
//                .setOnReportViewResponseListener((objects, row, column) -> presenter.onAction(objects, row, column))
//                .setDataAlignTypes(firstAligns)
//                .build();
//        firstView = new ReportView(firstBuilder);
//        ReportView.Builder secondBuilder = new ReportView.Builder()
//                .setContext(getContext())
//                .setTitles(secondTitles)
//                .setDataTypes(secondDataType)
//                .setWeight(secondWeights)
//                .setDataAlignTypes(secondAligns)
//                .build();
//        secondView = new ReportView(secondBuilder);
//        ReportView.Builder forthBuilder = new ReportView.Builder()
//                .setContext(getContext())
//                .setTitles(forthTitles)
//                .setDataTypes(forthDataType)
//                .setWeight(forthWeights)
//                .setDataAlignTypes(forthAligns)
//                .build();
//        forthView = new ReportView(forthBuilder);
    }

    @Override
    public void initTable(Object[][] objects) {
        firstView.getBuilder().init(objects);
        setTable(firstView.getBuilder().getView());
    }


    @Override
    public void onOrderPressed(Order order) {
        OrderDetialsDialog orderDetialsDialog = new OrderDetialsDialog(getContext(), order);
        orderDetialsDialog.show();
    }


    @Override
    public void updateTable(Object[][] objects, int position) {
        switch (position) {
            case 0:
                firstView.getBuilder().update(objects);
                setTable(firstView.getBuilder().getView());
                enableDateIntervalPicker();
                enableFilter();
                disableTillChooseBtn();
                break;
            case 1:
                secondView.getBuilder().update(objects);
                setTable(secondView.getBuilder().getView());
                enableDateIntervalPicker();
                disableFilter();
                disableTillChooseBtn();
                break;
            case 2:
                forthView.getBuilder().update(objects);
                setTable(forthView.getBuilder().getView());
                enableDateIntervalPicker();
                disableFilter();
                disableTillChooseBtn();
                break;
        }
    }

    @Override
    public void setSearchResults(Object[][] searchResults, String searchText, int position) {
        switch (position) {
            case 0:
                firstView.getBuilder().searchResults(searchResults, searchText);
                setTable(firstView.getBuilder().getView());
                enableDateIntervalPicker();
                enableFilter();
                disableTillChooseBtn();
                break;
            case 1:
                secondView.getBuilder().searchResults(searchResults, searchText);
                setTable(secondView.getBuilder().getView());
                enableDateIntervalPicker();
                disableFilter();
                disableTillChooseBtn();
                break;
            case 2:
                forthView.getBuilder().searchResults(searchResults, searchText);
                setTable(forthView.getBuilder().getView());
                enableDateIntervalPicker();
                disableFilter();
                disableTillChooseBtn();
                break;
        }
    }

    @Override
    public void showFilterDialog(int[] filterConfig) {
        InventoryFilterDialog dialog = new InventoryFilterDialog(getContext(), filterConfig, config -> presenter.setFilterConfigs(config));
        dialog.show();
    }

    @Override
    public void showTillPickerDialog() {
        TillPickerDialog dialog = new TillPickerDialog(getContext(), databaseManager, till -> {
            presenter.setPickedTill(till);
        });
        dialog.show();
    }

    @Override
    public void exportTableToExcel(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = getContext().getString(R.string.inventory_log_description);
                ExportUtils.exportToExcel(getContext(), path, fileName, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, status);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.inventory_summary_description);
                ExportUtils.exportToExcel(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, null);
                break;
            case 2:
                String forthDescription = getContext().getString(R.string.returns_inventroy_description);
                ExportUtils.exportToExcel(getContext(), path, fileName, forthDescription, date, filter, searchText, objects, forthTitles, forthWeights, forthDataType, null);
                break;
        }
    }

    @Override
    public void exportTableToPdf(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = getContext().getString(R.string.inventory_log_description);
                ExportUtils.exportToPdf(getContext(), path, fileName, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, status);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.inventory_summary_description);
                ExportUtils.exportToPdf(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, null);
                break;
            case 2:
                String forthDescription = getContext().getString(R.string.returns_inventroy_description);
                ExportUtils.exportToPdf(getContext(), path, fileName, forthDescription, date, filter, searchText, objects, forthTitles, forthWeights, forthDataType, null);
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

    @Override
    public void exportExcelToUSB(String filename, UsbFile root, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = getContext().getString(R.string.inventory_log_description);
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, status);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.inventory_summary_description);
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, null);
                break;
            case 2:
                String forthDescription = getContext().getString(R.string.returns_inventroy_description);
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, forthDescription, date, filter, searchText, objects, forthTitles, forthWeights, forthDataType, null);
                break;
        }
    }

    @Override
    public void exportTableToPdfToUSB(String fileName, UsbFile path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = getContext().getString(R.string.inventory_log_description);
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, status);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.inventory_summary_description);
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, null);
                break;
            case 2:
                String forthDescription = getContext().getString(R.string.returns_inventroy_description);
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, forthDescription, date, filter, searchText, objects, forthTitles, forthWeights, forthDataType, null);
                break;
        }
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

    public void onBarcodeScaned(String barcode) {
        presenter.onBarcodeReaded(barcode);
    }
}
