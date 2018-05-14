package com.jim.multipos.ui.reports.debts;

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
import com.jim.multipos.ui.reports.debts.dialogs.DebtFilterDialog;
import com.jim.multipos.ui.reports.order_history.dialogs.OrderDetialsDialog;
import com.jim.multipos.utils.ExportToDialog;
import com.jim.multipos.utils.ExportUtils;

import java.io.File;

import javax.inject.Inject;

import static com.jim.multipos.utils.ExportUtils.EXCEL;

public class DebtsReportFragment extends BaseTableReportFragment implements DebtsReportView {

    @Inject
    DebtsReportPresenter presenter;
    private ReportView.Builder firstBuilder, secondBuilder, thirdBuilder, forthBuilder;
    private ReportView firstView, secondView, thirdView, forthView;
    private int firstDataType[] = {ReportViewConstants.NAME, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.DATE, ReportViewConstants.NAME};
    private int firstWeights[] = {1, 1, 1, 1, 1};
    private int firstAligns[] = {Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT, Gravity.CENTER, Gravity.LEFT};
    private int secondDataType[] = {ReportViewConstants.NAME, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.QUANTITY, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT};
    private int secondWeights[] = {10, 10, 10, 10, 10, 10};
    private int secondAligns[] = {Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT};
    private int thirdDataType[] = {ReportViewConstants.NAME, ReportViewConstants.DATE, ReportViewConstants.ACTION, ReportViewConstants.STATUS, ReportViewConstants.AMOUNT, ReportViewConstants.NAME, ReportViewConstants.NAME};
    private int thirdWeights[] = {10, 10, 5, 5, 10, 10, 10};
    private int thirdAligns[] = {Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.CENTER, Gravity.RIGHT, Gravity.LEFT, Gravity.LEFT};
    private int forthDataType[] = {ReportViewConstants.ACTION, ReportViewConstants.DATE, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.DATE, ReportViewConstants.AMOUNT, ReportViewConstants.NAME, ReportViewConstants.DATE, ReportViewConstants.AMOUNT};
    private int forthWeights[] = {5, 10, 10, 10, 10, 10, 10, 10, 10};
    private int forthAligns[] = {Gravity.CENTER, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT, Gravity.CENTER, Gravity.RIGHT, Gravity.LEFT, Gravity.CENTER, Gravity.RIGHT};
    private String firstTitles[], secondTitles[], thirdTitles[], forthTitles[], panelNames[];
    private Object[][][] statusTypes;

    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        disableFilter();
        disableDateIntervalPicker();
        panelNames = new String[]{getString(R.string.debts_list), getString(R.string.debt_summary), getString(R.string.debt_transactions_list), getString(R.string.orders_with_Debt)};
        setChoiserPanel(panelNames);
        initDefaults();
        presenter.onCreateView(savedInstanceState);
    }

    private void initDefaults() {
        statusTypes = new Object[][][]{
                {{0, getContext().getString(R.string.debt_taken), R.color.colorMainText},
                        {1, getContext().getString(R.string.debt_closed), R.color.colorBlue}}};
        firstTitles = new String[]{getString(R.string.name), getString(R.string.total_debt), getString(R.string.total_overdue), getString(R.string.last_visit), getString(R.string.customer_contacts)};
        secondTitles = new String[]{getString(R.string.name), getString(R.string.debt_taken), getString(R.string.debt_closed), getString(R.string.debt_orders_count), getString(R.string.debt_taken_avg), getString(R.string.debt_closed_avg)};
        thirdTitles = new String[]{getString(R.string.name), getString(R.string.date), getString(R.string.order), getString(R.string.type), getString(R.string.amount), getString(R.string.payment_type), getString(R.string.group)};
        forthTitles = new String[]{getString(R.string.order_num), getString(R.string.created_at), getString(R.string.order_amount), getString(R.string.paid_report_text), getString(R.string.last_pay_date), getString(R.string.due_debt), getString(R.string.customer), getString(R.string.should_close_date), getContext().getString(R.string.fee)};
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
                .setDataAlignTypes(secondAligns)
                .build();
        secondView = new ReportView(secondBuilder);
        thirdBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(thirdTitles)
                .setDataTypes(thirdDataType)
                .setWeight(thirdWeights)
                .setStatusTypes(statusTypes)
                .setOnReportViewResponseListener((objects, row, column) -> {
                    presenter.onAction(objects, row, column);
                })
                .setDataAlignTypes(thirdAligns)
                .build();
        thirdView = new ReportView(thirdBuilder);
        forthBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(forthTitles)
                .setDataTypes(forthDataType)
                .setWeight(forthWeights)
                .setDataAlignTypes(forthAligns)
                .setOnReportViewResponseListener((objects, row, column) -> {
                    presenter.onAction(objects, row, column);
                })
                .build();
        forthView = new ReportView(forthBuilder);

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
                disableFilter();
                disableDateIntervalPicker();
                break;
            case 1:
                secondView.getBuilder().update(objects);
                setTable(secondView.getBuilder().getView());
                enableDateIntervalPicker();
                disableFilter();
                break;
            case 2:
                thirdView.getBuilder().update(objects);
                setTable(thirdView.getBuilder().getView());
                enableDateIntervalPicker();
                enableFilter();
                break;
            case 3:
                forthView.getBuilder().update(objects);
                setTable(forthView.getBuilder().getView());
                disableFilter();
                enableDateIntervalPicker();
                break;
        }
    }

    @Override
    public void setSearchResults(Object[][] searchResults, String searchText, int position) {
        switch (position) {
            case 0:
                firstView.getBuilder().searchResults(searchResults, searchText);
                setTable(firstView.getBuilder().getView());
                disableFilter();
                disableDateIntervalPicker();
                break;
            case 1:
                secondView.getBuilder().searchResults(searchResults, searchText);
                setTable(secondView.getBuilder().getView());
                enableDateIntervalPicker();
                disableFilter();
                break;
            case 2:
                thirdView.getBuilder().searchResults(searchResults, searchText);
                setTable(thirdView.getBuilder().getView());
                enableDateIntervalPicker();
                enableFilter();
                break;
            case 3:
                forthView.getBuilder().searchResults(searchResults, searchText);
                setTable(forthView.getBuilder().getView());
                disableFilter();
                enableDateIntervalPicker();
                break;
        }
    }

    @Override
    public void showFilterDialog(int[] filterConfig) {
        DebtFilterDialog dialog = new DebtFilterDialog(getContext(), filterConfig, config -> {
            presenter.filterConfigsHaveChanged(config);
            clearSearch();
        });
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
                ExportUtils.exportToExcel(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, null);
                break;
            case 2:
                String thirdDescription = " Debt states of each vendor";
                ExportUtils.exportToExcel(getContext(), path, fileName, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, statusTypes);
                break;
            case 3:
                String forthDescription = " All money transactions";
                ExportUtils.exportToExcel(getContext(), path, fileName, forthDescription, date, filter, searchText, objects, forthTitles, forthWeights, forthDataType, null);
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
                ExportUtils.exportToPdf(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, null);
                break;
            case 2:
                String thirdDescription = " Debt states of each vendor";
                ExportUtils.exportToPdf(getContext(), path, fileName, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, statusTypes);
                break;
            case 3:
                String forthDescription = " All money transactions";
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
                String description = "In this report you can find all info about consignments";
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, null);
                break;
            case 1:
                String secondDescription = " All Consignment products";
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, null);
                break;
            case 2:
                String thirdDescription = " Debt states of each vendor";
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, statusTypes);
                break;
            case 3:
                String forthDescription = " All money transactions";
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, forthDescription, date, filter, searchText, objects, forthTitles, forthWeights, forthDataType, null);
                break;
        }
    }

    @Override
    public void exportTableToPdfToUSB(String fileName, UsbFile path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = "In this report you can find all info about consignments";
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, null);
                break;
            case 1:
                String secondDescription = " All Consignment products";
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, null);
                break;
            case 2:
                String thirdDescription = " Debt states of each vendor";
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, statusTypes);
                break;
            case 3:
                String forthDescription = " All money transactions";
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, forthDescription, date, filter, searchText, objects, forthTitles, forthWeights, forthDataType, null);
                break;
        }
    }

    @Override
    public void onOrderPressed(Order order) {
        OrderDetialsDialog orderDetialsDialog = new OrderDetialsDialog(getContext(), order);
        orderDetialsDialog.show();
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
}
