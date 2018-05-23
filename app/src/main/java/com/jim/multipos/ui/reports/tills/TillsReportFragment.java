package com.jim.multipos.ui.reports.tills;

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
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.ui.reports.tills.dialog.TillDetailsDialog;
import com.jim.multipos.utils.ExportToDialog;
import com.jim.multipos.utils.ExportUtils;

import java.io.File;

import javax.inject.Inject;

import static com.jim.multipos.utils.ExportUtils.EXCEL;

/**
 * Created by Sirojiddin on 27.03.2018.
 */

public class TillsReportFragment extends BaseTableReportFragment implements TillsReportView {

    @Inject
    TillsReportPresenter presenter;
    @Inject
    DatabaseManager databaseManager;
    private FrameLayout fl;
    private ReportView.Builder builder;
    private ReportView reportView;
    private int dataType[] = {
            ReportViewConstants.ID, ReportViewConstants.DATE, ReportViewConstants.DATE, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.ACTION};
    private int weights[] = {10, 20, 20, 20, 20, 10};
    private int aligns[] = {Gravity.RIGHT, Gravity.CENTER, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT, Gravity.CENTER};
    private String titles[];

    @Override
    protected void init(Bundle savedInstanceState) {
        description = getContext().getString(R.string.till_reports_description);
        init(presenter);
        disableFilter();
        titles = new String[]{getContext().getString(R.string.till_id), getContext().getString(R.string.opened_time), getContext().getString(R.string.closed_time), getContext().getString(R.string.total_starting_amount), getContext().getString(R.string.expexted_amount_in_till_report), getContext().getString(R.string.action)};
        setSingleTitle(getString(R.string.tills_reports));
        builder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(titles)
                .setDataTypes(dataType)
                .setWeight(weights)
                .setDataAlignTypes(aligns)
                .setOnReportViewResponseListener((objects1, row, column) -> {
                    presenter.openTillDetailsDialog(objects1, row, column);
                })
                .build();
        reportView = new ReportView(builder);
        presenter.onCreateView(savedInstanceState);
    }

    @Override
    public void fillReportView(Object[][] objects) {
        reportView.getBuilder().init(objects);
        fl = reportView.getBuilder().getView();
        setTable(fl);
    }

    @Override
    public void openTillDetailsDialog(Till till) {
        TillDetailsDialog dialog = new TillDetailsDialog(getContext(), databaseManager, till);
        dialog.show();
    }

    @Override
    public void setSearchResults(Object[][] objects, String searchText) {
        reportView.getBuilder().searchResults(objects, searchText);
        fl = reportView.getBuilder().getView();
        setTable(fl);
    }

    @Override
    public void updateReportView(Object[][] objects) {
        reportView.getBuilder().update(objects);
        fl = reportView.getBuilder().getView();
        setTable(fl);
    }

    String description;

    @Override
    public void exportTableToExcel(String fileName, String path, Object[][] objects, String date, String searchText) {
        ExportUtils.exportToExcel(getContext(), path, fileName, description, date, "", searchText, objects, titles, weights, dataType, null);
    }

    @Override
    public void exportTableToPdf(String fileName, String path, Object[][] objects, String date, String searchText) {
        ExportUtils.exportToPdf(getContext(), path, fileName, description, date, "", searchText, objects, titles, weights, dataType, null);
    }

    ExportToDialog exportDialog;

    @Override
    public void openExportDialog(int mode) {
        exportDialog = new ExportToDialog(getContext(), mode, getString(R.string.tills_reports), new ExportToDialog.OnExportListener() {
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
    public void exportExcelToUSB(String filename, UsbFile root, Object[][] objects, String date, String searchText) {
        ExportUtils.exportToExcelToUSB(getContext(), root, filename, description, date, "", searchText, objects, titles, weights, dataType, null);
    }

    @Override
    public void exportTableToPdfToUSB(String fileName, UsbFile path, Object[][] objects, String date, String searchText) {
        ExportUtils.exportToPdfToUSB(getContext(), path, fileName, description, date, "", searchText, objects, titles, weights, dataType, null);
    }
}
