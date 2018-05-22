package com.jim.multipos.ui.reports.customers;

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
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.ui.reports.customers.dialogs.CustomerPaymentFilterDialog;
import com.jim.multipos.ui.reports.customers.dialogs.CustomerSummaryFilterDialog;
import com.jim.multipos.utils.ExportToDialog;
import com.jim.multipos.utils.ExportUtils;
import com.jim.multipos.utils.RxBus;

import java.io.File;

import javax.inject.Inject;

import static com.jim.multipos.utils.ExportUtils.EXCEL;

public class CustomerReportFragment extends BaseTableReportFragment implements CustomerReportView {

    @Inject
    CustomerReportPresenter presenter;
    @Inject
    RxBus rxBus;
    private ReportView.Builder firstBuilder, secondBuilder, thirdBuilder;
    private ReportView firstView, secondView, thirdView;
    private int firstDataType[] = {ReportViewConstants.ID, ReportViewConstants.NAME, ReportViewConstants.QUANTITY, ReportViewConstants.QUANTITY, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT};
    private int firstWeights[] = {5, 10, 10, 10, 13, 10, 10, 10};
    private int firstAligns[] = {Gravity.CENTER, Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT};
    private int secondDataType[] = {ReportViewConstants.ID, ReportViewConstants.NAME, ReportViewConstants.DATE, ReportViewConstants.ID, ReportViewConstants.STATUS, ReportViewConstants.AMOUNT};
    private int secondWeights[] = {5, 15, 10, 5, 5, 10};
    private int secondAligns[] = {Gravity.CENTER, Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.CENTER, Gravity.RIGHT};
    private int thirdDataType[] = {ReportViewConstants.ID, ReportViewConstants.NAME, ReportViewConstants.DATE, ReportViewConstants.STATUS, ReportViewConstants.NAME, ReportViewConstants.AMOUNT};
    private int thirdWeights[] = {1, 1, 1, 1, 1, 1};
    private int thirdAligns[] = {Gravity.CENTER, Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.LEFT, Gravity.RIGHT};
    private String firstTitles[], secondTitles[], thirdTitles[], panelNames[];
    private Object[][][] secondStatusTypes, thirdStatusTypes;
    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        panelNames = new String[]{getString(R.string.customer_summary), getString(R.string.customer_order_list), getString(R.string.customer_payment_log)};
        setChoiserPanel(panelNames);
        initDefaults();
        presenter.onCreateView(savedInstanceState);
    }

    private void initDefaults() {
        secondStatusTypes = new Object[][][]{
                {{Order.CLOSED_ORDER, getString(R.string.order_status_closed), R.color.colorGreen},
                        {Order.HOLD_ORDER, getString(R.string.order_status_held), R.color.colorBlue},
                        {Order.CANCELED_ORDER, getString(R.string.order_status_canceled), R.color.colorRed}}
        };
        thirdStatusTypes = new Object[][][]{
                {{0, getString(R.string.for_order), R.color.colorGreen},
                        {1, getString(R.string.for_debt), R.color.colorBlue},
                        {2, getString(R.string.change), R.color.colorRed}}
        };

        firstTitles = new String[]{getString(R.string.id), getString(R.string.name), getString(R.string.closed_orders), getString(R.string.cancelled_orders), getString(R.string.total_orders_amount), getString(R.string.total_to_debt), getString(R.string.total_discounts), getString(R.string.total_service_fees)};
        secondTitles = new String[]{getString(R.string.id), getString(R.string.name), getString(R.string.date), getString(R.string.order), getString(R.string.order_status), getString(R.string.total_order_amount)};
        thirdTitles = new String[]{getString(R.string.id), getString(R.string.name), getString(R.string.date), getString(R.string.reason), getString(R.string.payment_type), getString(R.string.amount)};

        firstBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(firstTitles)
                .setDataTypes(firstDataType)
                .setWeight(firstWeights)
                .setDataAlignTypes(firstAligns)
                .build();
        firstView = new ReportView(firstBuilder);
        secondBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(secondTitles)
                .setDataTypes(secondDataType)
                .setWeight(secondWeights)
                .setStatusTypes(secondStatusTypes)
                .setDataAlignTypes(secondAligns)
                .build();
        secondView = new ReportView(secondBuilder);
        thirdBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(thirdTitles)
                .setDataTypes(thirdDataType)
                .setWeight(thirdWeights)
                .setStatusTypes(thirdStatusTypes)
                .setDataAlignTypes(thirdAligns)
                .build();
        thirdView = new ReportView(thirdBuilder);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void initTable(Object[][] objects) {
        firstView.getBuilder().init(objects);
        setTable(firstView.getBuilder().getView());
    }

    @Override
    public void updateTable(Object[][] objects, int position) {
        switch (position) {
            case 0:
                firstView.getBuilder().update(objects);
                setTable(firstView.getBuilder().getView());
                break;
            case 1:
                secondView.getBuilder().update(objects);
                setTable(secondView.getBuilder().getView());
                break;
            case 2:
                thirdView.getBuilder().update(objects);
                setTable(thirdView.getBuilder().getView());
                break;
        }
    }

    @Override
    public void setSearchResults(Object[][] searchResults, String searchText, int position) {
        switch (position) {
            case 0:
                firstView.getBuilder().searchResults(searchResults, searchText);
                setTable(firstView.getBuilder().getView());
                break;
            case 1:
                secondView.getBuilder().searchResults(searchResults, searchText);
                setTable(secondView.getBuilder().getView());
                break;
            case 2:
                thirdView.getBuilder().searchResults(searchResults, searchText);
                setTable(thirdView.getBuilder().getView());
                break;
        }
    }

    @Override
    public void showFilterDialog(int[] filterConfig) {
        CustomerPaymentFilterDialog dialog = new CustomerPaymentFilterDialog(getContext(), filterConfig, config -> {
            presenter.changePaymentFilter(config);
        });
        dialog.show();
    }

    @Override
    public void showSummaryFilterDialog(int filterValue) {
        CustomerSummaryFilterDialog dialog = new CustomerSummaryFilterDialog(getContext(), filterValue, config -> presenter.changeSummaryConfig(config));
        dialog.show();
    }

    @Override
    public void exportTableToExcel(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = "In this report you can find all info about consignments";
                ExportUtils.exportToExcel(getContext(), path, fileName, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, null);
                break;
            case 1:
                String secondDescription = " All Consignment products";
                ExportUtils.exportToExcel(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, secondStatusTypes);
                break;
            case 2:
                String thirdDescription = " Debt states of each vendor";
                ExportUtils.exportToExcel(getContext(), path, fileName, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, thirdStatusTypes);
                break;
        }
    }

    @Override
    public void exportTableToPdf(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = "In this report you can find all info about consignments";
                ExportUtils.exportToPdf(getContext(), path, fileName, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, null);
                break;
            case 1:
                String secondDescription = " All Consignment products";
                ExportUtils.exportToPdf(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, secondStatusTypes);
                break;
            case 2:
                String thirdDescription = " Debt states of each vendor";
                ExportUtils.exportToPdf(getContext(), path, fileName, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, thirdStatusTypes);
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
                String description = getContext().getString(R.string.customer_summary_description);
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, null);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.customer_order_list_description);
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, secondStatusTypes);
                break;
            case 2:
                String thirdDescription = getContext().getString(R.string.customer_payment_log_description);
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, thirdStatusTypes);
                break;
        }
    }

    @Override
    public void exportTableToPdfToUSB(String fileName, UsbFile path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = getContext().getString(R.string.customer_summary_description);
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, null);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.customer_order_list_description);
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, secondStatusTypes);
                break;
            case 2:
                String thirdDescription = getContext().getString(R.string.customer_payment_log_description);
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, thirdStatusTypes);
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
        dialog.setTitle(getContext().getString(R.string.select_a_directory));
        dialog.setDialogSelectionListener(files -> {
            exportDialog.setPath(files);
        });
        dialog.show();
    }

    public void onBarcodeScaned(String barcode){
        presenter.onBarcodeReaded(barcode);
    }


}
