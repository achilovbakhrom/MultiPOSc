package com.jim.multipos.ui.reports.stock_operations;

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
import com.jim.multipos.data.db.model.consignment.Invoice;
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.ui.reports.debts.dialogs.DebtFilterDialog;
import com.jim.multipos.ui.reports.order_history.dialogs.OrderDetialsDialog;
import com.jim.multipos.ui.reports.stock_operations.dialog.IncomeFilterDialog;
import com.jim.multipos.ui.reports.stock_operations.dialog.OutcomeFilterDialog;
import com.jim.multipos.ui.reports.vendor.dialogs.ConsignmentDetailsDialog;
import com.jim.multipos.utils.ExportToDialog;
import com.jim.multipos.utils.ExportUtils;

import java.io.File;

import javax.inject.Inject;

import static com.jim.multipos.utils.ExportUtils.EXCEL;

public class StockOperationFragment extends BaseTableReportFragment implements StockOperationView {
    @Inject
    StockOperationPresenter presenter;

    private ReportView firstView, secondView, thirdView, forthView, fifthView;
    private int firstDataType[] = {ReportViewConstants.NAME, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.NAME};
    private int firstWeights[] = {15, 10, 10, 10, 10, 10, 10, 10, 7};
    private int firstAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT};

    private int secondDataType[] = {ReportViewConstants.NAME, ReportViewConstants.STATUS, ReportViewConstants.ACTION, ReportViewConstants.NAME, ReportViewConstants.DATE, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.NAME};
    private int secondWeights[] = {10, 8, 6, 8, 8, 8, 8, 10};
    private int secondAligns[] = {Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.LEFT, Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT, Gravity.LEFT};

    private int thirdDataType[] = {ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.STATUS, ReportViewConstants.ACTION, ReportViewConstants.DATE, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.NAME};
    private int thirdWeights[] = {10, 8, 6, 8, 8, 8, 8, 10};
    private int thirdAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT};

    private int forthDataType[] = {ReportViewConstants.ID, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.STATUS, ReportViewConstants.ACTION, ReportViewConstants.DATE, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.NAME};
    private int forthWeights[] = {8, 10, 10, 8, 8, 10, 10, 8, 8, 12};
    private int forthAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT, Gravity.LEFT};

    private int fifthDataTypes[] = {ReportViewConstants.ID, ReportViewConstants.NAME, ReportViewConstants.STATUS, ReportViewConstants.ACTION, ReportViewConstants.DATE, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.NAME};
    private int fifthWeights[] = {8, 10, 8, 8, 8, 8, 10, 10};
    private int fifthAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT, Gravity.LEFT};

    private String firstTitles[], secondTitles[], thirdTitles[], forthTitles[], panelNames[], fifthTitles[];

    private Object[][][] secondStatusTypes, thirdStatusTypes, forthStatusTypes, fifthStatusTypes;

    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        panelNames = new String[]{getString(R.string.operation_summary), getString(R.string.outcome_logs), getString(R.string.income_logs), getString(R.string.stock_positions), getString(R.string.outcome_details)};
        setChoiserPanel(panelNames);
        initDefaults();
        presenter.onCreateView(savedInstanceState);
    }

    void initDefaults() {
        disableFilter();

        secondStatusTypes = new Object[][][]{
                {
                        {OutcomeProduct.ORDER_SALES, getString(R.string.order_rep), R.color.colorGreen},
                        {OutcomeProduct.OUTVOICE_TO_VENDOR, getString(R.string.outvoice_rep), R.color.colorBlue},
                        {OutcomeProduct.WASTE, getString(R.string.waste_rep), R.color.colorRed}
                }
        };
        thirdStatusTypes = new Object[][][]{
                {
                        {IncomeProduct.INVOICE_PRODUCT, getString(R.string.invoice_rep), R.color.colorGreen},
                        {IncomeProduct.SURPLUS_PRODUCT, getString(R.string.surplus_rep), R.color.colorRed},
                        {IncomeProduct.RETURNED_PRODUCT, getString(R.string.customer_rep), R.color.colorBlue}
                }
        };

        forthStatusTypes = new Object[][][]{
                {
                        {IncomeProduct.INVOICE_PRODUCT, getString(R.string.invoice_rep), R.color.colorGreen},
                        {IncomeProduct.SURPLUS_PRODUCT, getString(R.string.surplus_rep), R.color.colorRed},
                        {IncomeProduct.RETURNED_PRODUCT, getString(R.string.customer_rep), R.color.colorBlue}
                }
        };

        fifthStatusTypes = new Object[][][]{
                {
                        {OutcomeProduct.ORDER_SALES, getString(R.string.order_rep), R.color.colorGreen},
                        {OutcomeProduct.OUTVOICE_TO_VENDOR, getString(R.string.outvoice_rep), R.color.colorBlue},
                        {OutcomeProduct.WASTE, getString(R.string.waste_rep), R.color.colorRed}
                }
        };

        firstTitles = new String[]{getString(R.string.product), getString(R.string.sales), getString(R.string.recive_from_vendor), getString(R.string.return_to_vendor_), getString(R.string.return_from_customer_), getString(R.string.surplus_), getString(R.string.waste_), getString(R.string.unit)};
        secondTitles = new String[]{getString(R.string.product), getString(R.string.outcome_for), getString(R.string.order_id), getString(R.string.getting_type), getString(R.string.outcome_date), getString(R.string.sum_count), getString(R.string.sum_costs), getString(R.string.reason)};
        thirdTitles = new String[]{getString(R.string.product), getString(R.string.vendor), getString(R.string.income_type), getString(R.string.invoice_id), getString(R.string.income_date), getString(R.string.count),  getString(R.string.sum_costs), getString(R.string.reason)};
        forthTitles = new String[]{getString(R.string.stock_position_id), getString(R.string.product), getString(R.string.vendor), getString(R.string.income_type), getString(R.string.invoice_id), getString(R.string.income_date), getString(R.string.income_cost), getString(R.string.income_count), getString(R.string.available), getString(R.string.stock_position_id)};
        fifthTitles = new String[]{getString(R.string.stock_position_id), getString(R.string.product), getString(R.string.outcome_for), getString(R.string.order_id), getString(R.string.date), getString(R.string.count), getString(R.string.cost), getString(R.string.stock_keeping_type)};


        ReportView.Builder firstBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(firstTitles)
                .setDataTypes(firstDataType)
                .setWeight(firstWeights)
                .setDataAlignTypes(firstAligns)
                .build();
        firstView = new ReportView(firstBuilder);
        ReportView.Builder secondBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(secondTitles)
                .setDataTypes(secondDataType)
                .setWeight(secondWeights)
                .setStatusTypes(secondStatusTypes)
                .setOnReportViewResponseListener((objects, row, column) -> {
                    presenter.onAction(objects, row, column);
                })
                .setDataAlignTypes(secondAligns)
                .build();
        secondView = new ReportView(secondBuilder);
        ReportView.Builder thirdBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(thirdTitles)
                .setDataTypes(thirdDataType)
                .setWeight(thirdWeights)
                .setStatusTypes(thirdStatusTypes)
                .setOnReportViewResponseListener((objects, row, column) -> {
                    presenter.onAction(objects, row, column);
                })
                .setDataAlignTypes(thirdAligns)
                .build();
        thirdView = new ReportView(thirdBuilder);
        ReportView.Builder forthBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(forthTitles)
                .setDataTypes(forthDataType)
                .setWeight(forthWeights)
                .setStatusTypes(forthStatusTypes)
                .setDataAlignTypes(forthAligns)
                .setOnReportViewResponseListener((objects, row, column) -> {
                    presenter.onAction(objects, row, column);
                })
                .build();
        forthView = new ReportView(forthBuilder);
        ReportView.Builder fifthBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(fifthTitles)
                .setDataTypes(fifthDataTypes)
                .setWeight(fifthWeights)
                .setStatusTypes(fifthStatusTypes)
                .setDataAlignTypes(fifthAligns)
                .setOnReportViewResponseListener((objects, row, column) -> {
                    presenter.onAction(objects, row, column);
                })
                .build();
        fifthView = new ReportView(fifthBuilder);
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
                disableFilter();
                clearSearch();
                firstView.getBuilder().update(objects);
                setTable(firstView.getBuilder().getView());
                break;
            case 1:
                enableFilter();
                clearSearch();
                secondView.getBuilder().update(objects);
                setTable(secondView.getBuilder().getView());
                break;
            case 2:
                enableFilter();
                clearSearch();
                thirdView.getBuilder().update(objects);
                setTable(thirdView.getBuilder().getView());
                break;
            case 3:
                enableFilter();
                clearSearch();
                forthView.getBuilder().update(objects);
                setTable(forthView.getBuilder().getView());

                break;
            case 4:
                enableFilter();
                clearSearch();
                fifthView.getBuilder().update(objects);
                setTable(fifthView.getBuilder().getView());
                break;
        }
    }

    @Override
    public void setSearchResults(Object[][] searchResults, String searchText, int position) {
        switch (position) {
            case 0:
                disableFilter();
                firstView.getBuilder().searchResults(searchResults, searchText);
                setTable(firstView.getBuilder().getView());
                break;
            case 1:
                enableFilter();
                secondView.getBuilder().searchResults(searchResults, searchText);
                setTable(secondView.getBuilder().getView());
                break;
            case 2:
                enableFilter();
                thirdView.getBuilder().searchResults(searchResults, searchText);
                setTable(thirdView.getBuilder().getView());
                break;
            case 3:
                enableFilter();
                forthView.getBuilder().searchResults(searchResults, searchText);
                setTable(forthView.getBuilder().getView());
                break;
            case 4:
                enableFilter();
                fifthView.getBuilder().searchResults(searchResults, searchText);
                setTable(fifthView.getBuilder().getView());
                break;
        }
    }

    @Override
    public void showFilterDialog(int[] filterConfig, int currentPage) {
        if (currentPage == 1 || currentPage == 4) {
            OutcomeFilterDialog dialog = new OutcomeFilterDialog(getContext(), filterConfig, config -> {
                presenter.filterConfigsChanged(config);
                clearSearch();
            });
            dialog.show();
        } else if (currentPage == 2 || currentPage == 3) {
            IncomeFilterDialog dialog = new IncomeFilterDialog(getContext(), filterConfig, config -> {
                presenter.filterConfigsChanged(config);
                clearSearch();
            });
            dialog.show();
        }

    }

    ExportToDialog exportDialog;

    @Override
    public void openExportDialog(int currentPosition, int mode) {
        exportDialog = new ExportToDialog(getContext(), mode, panelNames[currentPosition], new ExportToDialog.OnExportListener() {
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
    public void onOrderPressed(Order order) {
        OrderDetialsDialog orderDetialsDialog = new OrderDetialsDialog(getContext(), order);
        orderDetialsDialog.show();
    }
    @Override
    public void onInvoicePressed(Invoice invoice,DatabaseManager databaseManager) {
        ConsignmentDetailsDialog dialog = new ConsignmentDetailsDialog(getContext(), invoice, null, databaseManager);
        dialog.show();
    }

    @Override
    public void exportTableToExcel(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = "";
                ExportUtils.exportToExcel(getContext(), path, fileName, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, null);
                break;
            case 1:
                String secondDescription = "";
                ExportUtils.exportToExcel(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, secondStatusTypes);
                break;
            case 2:
                String thirdDescription = "";
                ExportUtils.exportToExcel(getContext(), path, fileName, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, thirdStatusTypes);
                break;
            case 3:
                String forthDescription = "";
                ExportUtils.exportToExcel(getContext(), path, fileName, forthDescription, date, filter, searchText, objects, forthTitles, forthWeights, forthDataType, forthStatusTypes);
                break;
            case 4:
                String fifthDescription = "";
                ExportUtils.exportToExcel(getContext(), path, fileName, fifthDescription, date, filter, searchText, objects, fifthTitles, fifthWeights, fifthDataTypes, fifthStatusTypes);
                break;
        }
    }

    @Override
    public void exportTableToExcelUSB(String fileName, UsbFile path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = "";
                ExportUtils.exportToExcelToUSB(getContext(), path, fileName, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, null);
                break;
            case 1:
                String secondDescription = "";
                ExportUtils.exportToExcelToUSB(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, secondStatusTypes);
                break;
            case 2:
                String thirdDescription = "";
                ExportUtils.exportToExcelToUSB(getContext(), path, fileName, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, thirdStatusTypes);
                break;
            case 3:
                String forthDescription = "";
                ExportUtils.exportToExcelToUSB(getContext(), path, fileName, forthDescription, date, filter, searchText, objects, forthTitles, forthWeights, forthDataType, forthStatusTypes);
                break;
            case 4:
                String fifthDescription = "";
                ExportUtils.exportToExcelToUSB(getContext(), path, fileName, fifthDescription, date, filter, searchText, objects, fifthTitles, fifthWeights, fifthDataTypes, fifthStatusTypes);
                break;
        }
    }

    @Override
    public void exportTableToPdfUSB(String fileName, UsbFile path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = "";
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, null);
                break;
            case 1:
                String secondDescription = "";
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, secondStatusTypes);
                break;
            case 2:
                String thirdDescription = "";
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, thirdStatusTypes);
                break;
            case 3:
                String forthDescription = "";
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, forthDescription, date, filter, searchText, objects, forthTitles, forthWeights, forthDataType, forthStatusTypes);
                break;
            case 4:
                String fifthDescription = "";
                ExportUtils.exportToPdfToUSB(getContext(), path, fileName, fifthDescription, date, filter, searchText, objects, fifthTitles, fifthWeights, fifthDataTypes, fifthStatusTypes);
                break;
        }
    }

    @Override
    public void exportTableToPdf(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = "";
                ExportUtils.exportToPdf(getContext(), path, fileName, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, null);
                break;
            case 1:
                String secondDescription = "";
                ExportUtils.exportToPdf(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, secondStatusTypes);
                break;
            case 2:
                String thirdDescription = "";
                ExportUtils.exportToPdf(getContext(), path, fileName, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, thirdStatusTypes);
                break;
            case 3:
                String forthDescription = "";
                ExportUtils.exportToPdf(getContext(), path, fileName, forthDescription, date, filter, searchText, objects, forthTitles, forthWeights, forthDataType, forthStatusTypes);
                break;
            case 4:
                String fifthDescription = "";
                ExportUtils.exportToPdf(getContext(), path, fileName, fifthDescription, date, filter, searchText, objects, fifthTitles, fifthWeights, fifthDataTypes, fifthStatusTypes);
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


}
