package com.jim.multipos.ui.reports.vendor;

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
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.Invoice;
import com.jim.multipos.data.db.model.consignment.Outvoice;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.ui.reports.vendor.dialogs.BillingsFilterDialog;
import com.jim.multipos.ui.reports.vendor.dialogs.ConsignmentDetailsDialog;
import com.jim.multipos.utils.BundleConstants;
import com.jim.multipos.utils.ExportToDialog;
import com.jim.multipos.utils.ExportUtils;

import java.io.File;

import javax.inject.Inject;

import static com.jim.multipos.utils.ExportUtils.EXCEL;

public class VendorReportFragment extends BaseTableReportFragment implements VendorReportView {

    @Inject
    VendorReportPresenter presenter;
    @Inject
    DatabaseManager databaseManager;
    private ReportView firstView, secondView, thirdView, forthView;
    private int firstDataType[] = {ReportViewConstants.ACTION, ReportViewConstants.NAME, ReportViewConstants.STATUS, ReportViewConstants.DATE, ReportViewConstants.AMOUNT, ReportViewConstants.NAME};
    private int firstWeights[] = {10, 10, 10, 10, 10, 10};
    private int firstAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.RIGHT, Gravity.LEFT};
    private int secondDataType[] = {ReportViewConstants.ID, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.DATE, ReportViewConstants.STATUS, ReportViewConstants.NAME, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT};
    private int secondWeights[] = {10, 10, 10, 10, 10, 10, 10, 10};
    private int secondAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT};
    private int thirdDataType[] = {ReportViewConstants.NAME, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT};
    private int thirdWeights[] = {10, 10, 10, 10};
    private int thirdAligns[] = {Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT};
    private int forthDataType[] = {ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.STATUS, ReportViewConstants.AMOUNT, ReportViewConstants.NAME, ReportViewConstants.ACTION, ReportViewConstants.DATE, ReportViewConstants.NAME};
    private int forthWeights[] = {10, 10, 10, 10, 10, 10, 10, 10};
    private int forthAligns[] = {Gravity.CENTER, Gravity.LEFT, Gravity.CENTER, Gravity.RIGHT, Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.LEFT};
    private String panelNames[], firstTitles[], secondTitles[], thirdTitles[], forthTitles[];
    private Object[][][] status, billingStatus;

    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        panelNames = new String[]{getString(R.string.consignment_report), getString(R.string.consignment_products), getString(R.string.debt_state), getString(R.string.money_transactions)};
        setChoiserPanel(panelNames);
        initDefaults();
        presenter.onCreateView(savedInstanceState);
    }

    private void initDefaults() {
        status = new Object[][][]{{{BundleConstants.INVOICE, getString(R.string.received_vendor), R.color.colorMainText},
                {BundleConstants.OUTVOICE, getString(R.string.returned_vendor), R.color.colorMainText}}};
        billingStatus = new Object[][][]{{{BillingOperations.RETURN_TO_VENDOR, getString(R.string.returned_vendor), R.color.colorMainText},
                {BillingOperations.PAID_TO_CONSIGNMENT, getContext().getString(R.string.pay), R.color.colorMainText},
                {BillingOperations.DEBT_CONSIGNMENT, getString(R.string.debt_), R.color.colorMainText}}};
        firstTitles = new String[]{getString(R.string.consignment_number), getString(R.string.vendor), getString(R.string.type), getString(R.string.data_time), getString(R.string.total_cost), getString(R.string.extra_info)};
        secondTitles = new String[]{getString(R.string.consignment_number), getString(R.string.product), getString(R.string.vendor), getString(R.string.data_time), getString(R.string.type), getString(R.string.qty), getString(R.string.each), getString(R.string.debt_total)};
        thirdTitles = new String[]{getString(R.string.vendor_name), getString(R.string.taken_value), getString(R.string.paid_), getString(R.string.difference)};
        forthTitles = new String[]{getString(R.string.date), getString(R.string.vendor), getString(R.string.type), getString(R.string.amount), getString(R.string.account), getString(R.string.consignment), getString(R.string.created_date_), getString(R.string.description)};
        ReportView.Builder firstBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(firstTitles)
                .setDataTypes(firstDataType)
                .setWeight(firstWeights)
                .setStatusTypes(status)
                .setDataAlignTypes(firstAligns)
                .setOnReportViewResponseListener((objects, row, column) -> {
                    presenter.onConsignmentClicked(objects, row, column);
                })
                .build();
        firstView = new ReportView(firstBuilder);
        ReportView.Builder secondBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(secondTitles)
                .setDataTypes(secondDataType)
                .setWeight(secondWeights)
                .setStatusTypes(status)
                .setDataAlignTypes(secondAligns)
                .build();
        secondView = new ReportView(secondBuilder);
        ReportView.Builder thirdBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(thirdTitles)
                .setDataTypes(thirdDataType)
                .setWeight(thirdWeights)
                .setDataAlignTypes(thirdAligns)
                .build();
        thirdView = new ReportView(thirdBuilder);
        ReportView.Builder forthBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(forthTitles)
                .setStatusTypes(billingStatus)
                .setDataTypes(forthDataType)
                .setWeight(forthWeights)
                .setDataAlignTypes(forthAligns)
                .setOnReportViewResponseListener((objects, row, column) -> {
                    presenter.onConsignmentClicked(objects, row, column);
                })
                .build();
        forthView = new ReportView(forthBuilder);
    }

    @Override
    public void initTable(Object[][] objects) {
        disableFilter();
        firstView.getBuilder().init(objects);
        setTable(firstView.getBuilder().getView());
    }

    @Override
    public void updateTable(Object[][] objects, int position) {
        switch (position) {
            case 0:
                firstView.getBuilder().update(objects);
                setTable(firstView.getBuilder().getView());
                disableFilter();
                break;
            case 1:
                disableFilter();
                secondView.getBuilder().update(objects);
                setTable(secondView.getBuilder().getView());
                break;
            case 2:
                disableFilter();
                thirdView.getBuilder().update(objects);
                setTable(thirdView.getBuilder().getView());
                break;
            case 3:
                enableFilter();
                forthView.getBuilder().update(objects);
                setTable(forthView.getBuilder().getView());
                break;
        }
    }

    @Override
    public void showFilterDialog(int[] filterConfig) {
        BillingsFilterDialog dialog = new BillingsFilterDialog(getContext(), filterConfig, config -> presenter.onFilterChange(config));
        dialog.show();
    }

    @Override
    public void setSearchResults(Object[][] searchResults, String searchText, int position) {
        switch (position) {
            case 0:
                firstView.getBuilder().searchResults(searchResults, searchText);
                setTable(firstView.getBuilder().getView());
                disableFilter();
                break;
            case 1:
                secondView.getBuilder().searchResults(searchResults, searchText);
                setTable(secondView.getBuilder().getView());
                disableFilter();
                break;
            case 2:
                thirdView.getBuilder().searchResults(searchResults, searchText);
                setTable(thirdView.getBuilder().getView());
                disableFilter();
                break;
            case 3:
                forthView.getBuilder().searchResults(searchResults, searchText);
                setTable(forthView.getBuilder().getView());
                enableFilter();
                break;
        }
    }

    @Override
    public void exportTableToExcel(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = getContext().getString(R.string.vendors_consignment_report_description);
                ExportUtils.exportToExcel(getContext(), path, fileName, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, status);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.vendors_sonsignment_products_description);
                ExportUtils.exportToExcel(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, status);
                break;
            case 2:
                String thirdDescription = getContext().getString(R.string.vendors_debt_state_description);
                ExportUtils.exportToExcel(getContext(), path, fileName, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, null);
                break;
            case 3:
                String forthDescription = getContext().getString(R.string.vendors_money_transactions_description);
                ExportUtils.exportToExcel(getContext(), path, fileName, forthDescription, date, filter, searchText, objects, forthTitles, forthWeights, forthDataType, billingStatus);
                break;
        }
    }

    @Override
    public void exportTableToPdf(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = getContext().getString(R.string.vendors_consignment_report_description);
                ExportUtils.exportToPdf(getContext(), path, fileName, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, status);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.vendors_sonsignment_products_description);
                ExportUtils.exportToPdf(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, status);
                break;
            case 2:
                String thirdDescription = getContext().getString(R.string.vendors_debt_state_description);
                ExportUtils.exportToPdf(getContext(), path, fileName, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, null);
                break;
            case 3:
                String forthDescription = getContext().getString(R.string.vendors_money_transactions_description);
                ExportUtils.exportToPdf(getContext(), path, fileName, forthDescription, date, filter, searchText, objects, forthTitles, forthWeights, forthDataType, billingStatus);
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
                String description = getContext().getString(R.string.vendors_consignment_report_description);
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, status);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.vendors_sonsignment_products_description);
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, status);
                break;
            case 2:
                String thirdDescription = getContext().getString(R.string.vendors_debt_state_description);
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, null);
                break;
            case 3:
                String forthDescription = getContext().getString(R.string.vendors_money_transactions_description);
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, forthDescription, date, filter, searchText, objects, forthTitles, forthWeights, forthDataType, billingStatus);
                break;
        }
    }

    @Override
    public void exportTableToPdfToUSB(String fileName, UsbFile path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = getContext().getString(R.string.vendors_consignment_report_description);
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, status);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.vendors_sonsignment_products_description);
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, status);
                break;
            case 2:
                String thirdDescription = getContext().getString(R.string.vendors_debt_state_description);
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, null);
                break;
            case 3:
                String forthDescription = getContext().getString(R.string.vendors_money_transactions_description);
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, forthDescription, date, filter, searchText, objects, forthTitles, forthWeights, forthDataType, billingStatus);
                break;
        }
    }

    @Override
    public void openInvoiceDetailsDialog(Outvoice outvoice, Invoice invoice) {
        ConsignmentDetailsDialog dialog = new ConsignmentDetailsDialog(getContext(), invoice, outvoice, databaseManager);
        dialog.show();
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
