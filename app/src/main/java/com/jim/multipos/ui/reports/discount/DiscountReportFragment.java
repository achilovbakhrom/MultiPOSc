package com.jim.multipos.ui.reports.discount;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseTableReportFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.DiscountLog;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.ui.reports.discount.dialogs.DiscountFilterDialog;
import com.jim.multipos.ui.reports.order_history.dialogs.OrderDetialsDialog;
import com.jim.multipos.ui.reports.tills.dialog.TillDetailsDialog;
import com.jim.multipos.utils.ExportToDialog;
import com.jim.multipos.utils.ExportUtils;

import java.io.File;

import javax.inject.Inject;

import static com.jim.multipos.utils.ExportUtils.EXCEL;

public class DiscountReportFragment extends BaseTableReportFragment implements DiscountReportView {

    @Inject
    DiscountReportPresenter presenter;
    private ReportView.Builder itemDiscountBuilder, orderDiscountBuilder, discountLogBuilder;
    private ReportView itemDiscountReportView, orderDiscountReportView, discountLogReportView;
    private String[] panelNames;

    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        panelNames = new String[]{getString(R.string.item_discount), getString(R.string.order_discount), getString(R.string.discount_creation_log)};
        setChoiserPanel(panelNames);
        initValues();
        presenter.onCreateView(savedInstanceState);
    }

    public void initValues() {
        initDefaults();
        itemDiscountBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(firstTitles)
                .setDataTypes(firstDataType)
                .setWeight(firstWeights)
                .setDataAlignTypes(firstAligns)
                .setOnReportViewResponseListener((objects, row, column) -> {
                    presenter.onAction(objects, row, column);
                })
                .setStatusTypes(firstStatusTypes)
                .build();
        orderDiscountBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(secondTitles)
                .setDataTypes(secondDataType)
                .setWeight(secondWeights)
                .setDataAlignTypes(secondAligns)
                .setOnReportViewResponseListener((objects, row, column) -> {
                    presenter.onAction(objects, row, column);
                })
                .setStatusTypes(firstStatusTypes)
                .build();
        discountLogBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(titles)
                .setDataTypes(dataType)
                .setWeight(weights)
                .setDataAlignTypes(aligns)
                .setStatusTypes(statusTypes)
                .setOnReportViewResponseListener((objects1, row, column) -> {

                })
                .build();
        itemDiscountReportView = new ReportView(itemDiscountBuilder);
        orderDiscountReportView = new ReportView(orderDiscountBuilder);
        discountLogReportView = new ReportView(discountLogBuilder);
    }

    @Override
    public void initTable(Object[][] objects) {
        itemDiscountReportView.getBuilder().init(objects);
        setTable(itemDiscountReportView.getBuilder().getView());
    }

    @Override
    public void updateTable(Object[][] objects, int position) {
        switch (position) {
            case 0:
                itemDiscountReportView.getBuilder().update(objects);
                setTable(itemDiscountReportView.getBuilder().getView());
                break;
            case 1:
                orderDiscountReportView.getBuilder().update(objects);
                setTable(orderDiscountReportView.getBuilder().getView());
                break;
            case 2:
                discountLogReportView.getBuilder().update(objects);
                setTable(discountLogReportView.getBuilder().getView());
                break;
        }

    }

    @Override
    public void showFilterDialog(int[] filterConfig) {
        DiscountFilterDialog dialog = new DiscountFilterDialog(getContext(), filterConfig, config -> {
            presenter.filterConfigsHaveChanged(config);
            clearSearch();
        });
        dialog.show();
    }

    @Override
    public void setSearchResults(Object[][] objectResults, String searchText, int position) {
        switch (position) {
            case 0:
                itemDiscountReportView.getBuilder().searchResults(objectResults, searchText);
                setTable(itemDiscountReportView.getBuilder().getView());
                break;
            case 1:
                orderDiscountReportView.getBuilder().searchResults(objectResults, searchText);
                setTable(orderDiscountReportView.getBuilder().getView());
                break;
            case 2:
                discountLogReportView.getBuilder().searchResults(objectResults, searchText);
                setTable(discountLogReportView.getBuilder().getView());
                break;
        }
    }

    @Override
    public void onOrderPressed(Order order) {
        OrderDetialsDialog orderDetialsDialog = new OrderDetialsDialog(getContext(), order);
        orderDetialsDialog.show();
    }

    @Override
    public void onTillPressed(DatabaseManager databaseManager, Till till) {
        TillDetailsDialog dialog = new TillDetailsDialog(getContext(), databaseManager, till);
        dialog.show();
    }

    @Override
    public void onTillNotClosed() {
        Toast.makeText(getActivity(), R.string.till_not_closed, Toast.LENGTH_SHORT).show();
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
        dialog.setTitle(getString(R.string.select_a_directory));
        dialog.setDialogSelectionListener(files -> {
            exportDialog.setPath(files);
        });
        dialog.show();
    }

    @Override
    public void exportTableToExcel(String fileName, String path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = getContext().getString(R.string.item_discount_description);
                ExportUtils.exportToExcel(getContext(), path, fileName, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, firstStatusTypes);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.order_discount_description);
                ExportUtils.exportToExcel(getContext(), path, fileName, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, firstStatusTypes);
                break;
            case 2:
                String thirdDescription = getContext().getString(R.string.discount_creation_log_description);
                ExportUtils.exportToExcel(getContext(), path, fileName, thirdDescription, date, filter, searchText, objects, titles, weights, dataType, statusTypes);
                break;
        }
    }

    @Override
    public void exportTableToPdf(String filename, String path, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = getContext().getString(R.string.item_discount_description);
                ExportUtils.exportToPdf(getContext(), path, filename, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, firstStatusTypes);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.order_discount_description);
                ExportUtils.exportToPdf(getContext(), path, filename, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, firstStatusTypes);
                break;
            case 2:
                String thirdDescription = getContext().getString(R.string.discount_creation_log_description);
                ExportUtils.exportToPdf(getContext(), path, filename, thirdDescription, date, filter, searchText, objects, titles, weights, dataType, statusTypes);
                break;
        }
    }

    @Override
    public void exportExcelToUSB(String filename, UsbFile root, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = getContext().getString(R.string.item_discount_description);
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, firstStatusTypes);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.order_discount_description);
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, firstStatusTypes);
                break;
            case 2:
                String thirdDescription = getContext().getString(R.string.discount_creation_log_description);
                ExportUtils.exportToExcelToUSB(getContext(), root, filename, thirdDescription, date, filter, searchText, objects, titles, weights, dataType, statusTypes);
                break;
        }
    }

    @Override
    public void exportTableToPdfToUSB(String filename, UsbFile root, Object[][] objects, int position, String date, String filter, String searchText) {
        switch (position) {
            case 0:
                String description = getContext().getString(R.string.item_discount_description);
                ExportUtils.exportToPdfToUSB(getContext(), root, filename, description, date, filter, searchText, objects, firstTitles, firstWeights, firstDataType, firstStatusTypes);
                break;
            case 1:
                String secondDescription = getContext().getString(R.string.order_discount_description);
                ExportUtils.exportToPdfToUSB(getContext(), root, filename, secondDescription, date, filter, searchText, objects, secondTitles, secondWeights, secondDataType, firstStatusTypes);
                break;
            case 2:
                String thirdDescription = getContext().getString(R.string.discount_creation_log_description);
                ExportUtils.exportToPdfToUSB(getContext(), root, filename, thirdDescription, date, filter, searchText, objects, titles, weights, dataType, statusTypes);
                break;
        }
    }

    private void initDefaults() {
        firstStatusTypes = new Object[][][]{
                {{0, getContext().getString(R.string.static_type), R.color.colorMainText},
                        {1, getContext().getString(R.string.manual), R.color.colorBlue}}
        };
        statusTypes = new Object[][][]{
                {{DiscountLog.DISCOUNT_ADDED, getContext().getString(R.string.add), R.color.colorGreen},
                        {DiscountLog.DISCOUNT_CANCELED, getString(R.string.canceled), R.color.colorBlue},
                        {DiscountLog.DISCOUNT_DELETED, getString(R.string.deleted), R.color.colorRed}},
                {{Discount.PERCENT, getContext().getString(R.string.percentage), R.color.colorMainText},
                        {Discount.VALUE, getContext().getString(R.string.value), R.color.colorMainText}},
                {{Discount.ORDER, getContext().getString(R.string.order), R.color.colorMainText},
                        {Discount.ITEM, getContext().getString(R.string.item), R.color.colorMainText}},
                {{0, getContext().getString(R.string.static_type), R.color.colorMainText},
                        {1, getContext().getString(R.string.manual), R.color.colorBlue}}
        };
        firstTitles = new String[]{getString(R.string.order_id), getString(R.string.till_id), getString(R.string.item), getString(R.string.sku_item), getString(R.string.qty), getString(R.string.discount_value), getString(R.string.date), getString(R.string.reason_for_discount), getString(R.string.type)};
        secondTitles = new String[]{getString(R.string.order_id), getString(R.string.till_id), getString(R.string.order_subtotal), getString(R.string.customer), getString(R.string.discount_value), getString(R.string.date), getString(R.string.reason), getString(R.string.type)};
        titles = new String[]{getString(R.string.date), getString(R.string.operation), getString(R.string.reason), getString(R.string.value), getString(R.string.amount_type), getString(R.string.usage_type), getString(R.string.type)};
    }

    private Object[][][] firstStatusTypes;
    private Object[][][] statusTypes;
    private String titles[];
    private String firstTitles[];
    private String secondTitles[];

    private int firstDataType[] = {
            ReportViewConstants.ACTION, ReportViewConstants.ACTION, ReportViewConstants.NAME,
            ReportViewConstants.NAME, ReportViewConstants.QUANTITY, ReportViewConstants.AMOUNT,
            ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.STATUS};
    private int firstWeights[] = {15, 15, 20, 15, 10, 20, 20, 30, 15};
    private int firstAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.CENTER, Gravity.RIGHT, Gravity.CENTER, Gravity.LEFT, Gravity.CENTER};
    private int secondDataType[] = {
            ReportViewConstants.ACTION, ReportViewConstants.ACTION, ReportViewConstants.AMOUNT,
            ReportViewConstants.NAME, ReportViewConstants.AMOUNT,
            ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.STATUS};
    private int secondWeights[] = {10, 10, 20, 20, 20, 20, 30, 10};
    private int secondAligns[] = new int[]{Gravity.CENTER, Gravity.CENTER, Gravity.RIGHT, Gravity.LEFT, Gravity.RIGHT, Gravity.CENTER, Gravity.LEFT, Gravity.CENTER};
    private int dataType[] = {
            ReportViewConstants.DATE, ReportViewConstants.STATUS, ReportViewConstants.NAME,
            ReportViewConstants.AMOUNT, ReportViewConstants.STATUS, ReportViewConstants.STATUS,
            ReportViewConstants.STATUS};
    private int weights[] = new int[]{20, 10, 20, 10, 10, 10, 10};
    private int aligns[] = new int[]{Gravity.CENTER, Gravity.LEFT, Gravity.LEFT, Gravity.RIGHT, Gravity.CENTER, Gravity.CENTER, Gravity.CENTER};
}
