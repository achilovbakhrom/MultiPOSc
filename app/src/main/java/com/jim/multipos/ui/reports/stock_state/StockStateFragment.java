package com.jim.multipos.ui.reports.stock_state;

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
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.customer.CustomerPayment;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.ui.reports.customers.model.CustomerGroupOrder;
import com.jim.multipos.ui.reports.customers.model.CustomerPaymentLog;
import com.jim.multipos.utils.ExportToDialog;
import com.jim.multipos.utils.ExportUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.utils.ExportUtils.EXCEL;

public class StockStateFragment extends BaseTableReportFragment implements StockStateView {
    @Inject
    StockStatePresenter presenter;

    private ReportView.Builder firstBuilder, secondBuilder, thirdBuilder;
    private ReportView firstView, secondView, thirdView;
    private int firstDataType[] = {ReportViewConstants.NAME,  ReportViewConstants.QUANTITY, ReportViewConstants.NAME, ReportViewConstants.AMOUNT};
    private int firstWeights[] = {10, 10, 5, 15};
    private int firstAligns[] = { Gravity.LEFT, Gravity.CENTER, Gravity.LEFT, Gravity.RIGHT};
    private int secondDataType[] = {ReportViewConstants.NAME,ReportViewConstants.NAME,  ReportViewConstants.QUANTITY, ReportViewConstants.NAME, ReportViewConstants.AMOUNT};
    private int secondWeights[] = {10, 10, 10, 5, 15};
    private int secondAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.CENTER, Gravity.LEFT, Gravity.RIGHT};
    private int thirdDataType[] = {ReportViewConstants.NAME, ReportViewConstants.DATE, ReportViewConstants.DATE, ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.QUANTITY,ReportViewConstants.NAME };
    private int thirdWeights[] = {14, 10, 10, 10, 13, 8, 5};
    private int thirdAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.CENTER , Gravity.LEFT};
    private String firstTitles[], secondTitles[], thirdTitles[];

    private String panelNames[];
    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        disableDateIntervalPicker();
        disableFilter();
        panelNames = new String[]{"Inventory", "Vendor Inventory", "Expired Products"};
        setChoiserPanel(panelNames);
        initDefaults();
        presenter.onCreateView(savedInstanceState);
    }

    private void initDefaults() {
        firstTitles = new String[]{"Product","Available","Unit","Summary Cost (uzs)"};
        secondTitles = new String[]{"Product", "Vendor","Available","Unit","Summary Cost (uzs)"};
        thirdTitles = new String[]{"Product", "Created Date","Income Date","Expired Date","Left Date","Count", "Unit"};

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
                .setDefaultSort(3) // EXPIRED DATE
                //TODO default sort by ASC DESC ->
                .setWeight(thirdWeights)
                .setDataAlignTypes(thirdAligns)
                .build();
        thirdView = new ReportView(thirdBuilder);

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
                clearSearch();
                firstView.getBuilder().update(objects);
                setTable(firstView.getBuilder().getView());
                break;
            case 1:
                clearSearch();
                secondView.getBuilder().update(objects);
                setTable(secondView.getBuilder().getView());
                break;
            case 2:
                clearSearch();
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

    }

    @Override
    public void showSummaryFilterDialog(int filterValue) {

    }

    ExportToDialog exportDialog;

    @Override
    public void openExportDialog(int currentPosition, int type) {
        exportDialog = new ExportToDialog(getContext(), type, panelNames[currentPosition], new ExportToDialog.OnExportListener() {
            @Override
            public void onFilePickerClicked() {
                openFilePickerDialog();
            }

            @Override
            public void onSaveToUSBClicked(String filename, UsbFile root) {
                if (type == EXCEL)
                    presenter.exportExcelToUSB(filename, root);
                else presenter.exportPdfToUSB(filename, root);
            }

            @Override
            public void onSaveClicked(String fileName, String path) {
                if (type == EXCEL)
                    presenter.exportExcel(fileName, path);
                else presenter.exportPdf(fileName, path);
            }
        });
        exportDialog.show();
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
                ExportUtils.exportToExcel(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, null);
                break;
            case 2:
                String thirdDescription = "";
                ExportUtils.exportToExcel(getContext(), path, fileName, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, null);
                break;
        }
    }

    @Override
    public void exportTableToExcelUSB(String filename, UsbFile root, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = "";
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, null);
                break;
            case 1:
                String secondDescription = "";
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, null);
                break;
            case 2:
                String thirdDescription = "";
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, null);
                break;
        }
    }

    @Override
    public void exportTableToPdf(String filename, String root, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = "";
                ExportUtils.exportToPdf(getContext(), root, filename, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, null);
                break;
            case 1:
                String secondDescription = "";
                ExportUtils.exportToPdf(getContext(), root, filename, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, null);
                break;
            case 2:
                String thirdDescription = "";
                ExportUtils.exportToPdf(getContext(), root, filename, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, null);
                break;
        }
    }

    @Override
    public void exportTableToPdfUSB(String filename, UsbFile root, Object[][] objects, int currentPosition, String date, String filter, String searchText) {
        switch (currentPosition) {
            case 0:
                String description = "";
                ExportUtils.exportToPdfToUSB(getContext(), root, filename, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, null);
                break;
            case 1:
                String secondDescription = "";
                ExportUtils.exportToPdfToUSB(getContext(), root, filename, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, null);
                break;
            case 2:
                String thirdDescription = "";
                ExportUtils.exportToPdfToUSB(getContext(), root, filename, thirdDescription, date, filter, searchText, objects, thirdTitles, thirdWeights, thirdDataType, null);
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
