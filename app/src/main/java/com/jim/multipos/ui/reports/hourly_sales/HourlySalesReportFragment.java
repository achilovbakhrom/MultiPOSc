package com.jim.multipos.ui.reports.hourly_sales;

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
import com.jim.multipos.utils.ExportToDialog;
import com.jim.multipos.utils.ExportUtils;

import java.io.File;

import javax.inject.Inject;

import static com.jim.multipos.utils.ExportUtils.EXCEL;

public class HourlySalesReportFragment extends BaseTableReportFragment implements HourlySalesReportView {

    @Inject
    HourlySalesReportPresenter presenter;
    private ReportView.Builder builder;
    private ReportView reportView;
    private String titles[];
    private int dataType[] = {
            ReportViewConstants.NAME, ReportViewConstants.QUANTITY, ReportViewConstants.QUANTITY, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.NAME};
    private int weights[] = {1, 2, 2, 2, 2, 1};
    private int aligns[] = {Gravity.CENTER, Gravity.CENTER, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT, Gravity.CENTER};

    @Override
    protected void init(Bundle savedInstanceState) {
        description = getContext().getString(R.string.hourly_sales_description);
        init(presenter);
        disableFilter();
        disableSearch();
        initDefaults();
        setSingleTitle(getString(R.string.hourly_sales_report));
        builder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(titles)
                .setDataTypes(dataType)
                .setWeight(weights)
                .setDataAlignTypes(aligns)
                .build();
        reportView = new ReportView(builder);
        presenter.onCreateView(savedInstanceState);
    }

    @Override
    public void initTable(Object[][] objects) {
        reportView.getBuilder().init(objects);
        setTable(reportView.getBuilder().getView());
    }

    @Override
    public void updateTable(Object[][] objects) {
        reportView.getBuilder().update(objects);
        setTable(reportView.getBuilder().getView());
    }

    String description;

    @Override
    public void exportTableToExcel(String fileName, String path, Object[][] objects, String date) {
        ExportUtils.exportToExcel(getContext(), path, fileName, description, date, "", "", objects, titles, weights, dataType, null);
    }

    @Override
    public void exportTableToPdf(String fileName, String path, Object[][] objects, String date) {
        ExportUtils.exportToPdf(getContext(), path, fileName, description, date, "", "", objects, titles, weights, dataType, null);
    }

    ExportToDialog exportDialog;

    @Override
    public void openExportDialog(int mode) {
        exportDialog = new ExportToDialog(getContext(), mode, getString(R.string.hourly_sales_report), new ExportToDialog.OnExportListener() {
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
    public void exportExcelToUSB(String filename, UsbFile root, Object[][] objects, String date) {
        ExportUtils.exportToExcelToUSB(getContext(), root, filename, description, date, "", "", objects, titles, weights, dataType, null);
    }

    @Override
    public void exportTableToPdfToUSB(String fileName, UsbFile path, Object[][] objects, String date) {
        ExportUtils.exportToPdfToUSB(getContext(), path, fileName, description, date, "", "", objects, titles, weights, dataType, null);
    }

    private void initDefaults() {
        titles = new String[]{getContext().getString(R.string.time_interval), getContext().getString(R.string.transactions), getContext().getString(R.string.sold_items), getContext().getString(R.string.avg_sales), getContext().getString(R.string.sales_summary), getContext().getString(R.string.percent_sales)};
    }
}
