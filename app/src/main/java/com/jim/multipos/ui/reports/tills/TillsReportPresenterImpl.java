package com.jim.multipos.ui.reports.tills;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillDetails;
import com.jim.multipos.data.db.model.till.TillManagementOperation;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.utils.ExportUtils.EXCEL;
import static com.jim.multipos.utils.ExportUtils.PDF;

/**
 * Created by Sirojiddin on 27.03.2018.
 */

public class TillsReportPresenterImpl extends BasePresenterImpl<TillsReportView> implements TillsReportPresenter {

    private DatabaseManager databaseManager;
    private Context context;
    private List<Till> tills;
    private Object[][] objects;
    private Calendar fromDate;
    private Calendar toDate;
    private DecimalFormat decimalFormat;
    private SimpleDateFormat simpleDateFormat;
    private boolean isFirstTime = true;
    private String searchText = "";

    @Inject
    protected TillsReportPresenterImpl(TillsReportView view, DatabaseManager databaseManager, Context context) {
        super(view);
        this.databaseManager = databaseManager;
        this.context = context;
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat1.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat1.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat = decimalFormat1;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        tills = new ArrayList<>();
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        Date currentDate = new Date();
        fromDate = new GregorianCalendar();
        toDate = new GregorianCalendar();

        toDate.setTime(currentDate);
        toDate.set(Calendar.HOUR_OF_DAY, 23);
        toDate.set(Calendar.MINUTE, 59);
        toDate.set(Calendar.SECOND, 59);

        fromDate.add(Calendar.MONTH, -1);
        fromDate.set(Calendar.HOUR_OF_DAY, 0);
        fromDate.set(Calendar.MINUTE, 0);
        fromDate.set(Calendar.SECOND, 0);
        view.updateDateIntervalUi(fromDate, toDate);
        new Handler().postDelayed(() -> {
            initTableView();
        }, 50);

    }

    private void initTableView() {
        tills = databaseManager.getClosedTillsInInterval(fromDate, toDate).blockingGet();
        objects = new Object[tills.size()][6];
        for (int i = 0; i < tills.size(); i++) {
            Till till = tills.get(i);
            objects[i][0] = till.getId();
            objects[i][1] = till.getOpenDate();
            objects[i][2] = till.getCloseDate();
            objects[i][5] = context.getString(R.string.details);
            double startMoneyVariance = 0;
            double tillAmountVariance = 0;
            if (till.getId() == 1) {
                objects[i][3] = startMoneyVariance;
                double closedAmount = 0;
                double expectedAmount = 0;
                List<TillManagementOperation> thisTillOperations = databaseManager.getTillManagementOperationsByTillId(till.getId()).blockingGet();
                for (TillManagementOperation operation : thisTillOperations) {
                    if (operation.getType() == TillManagementOperation.CLOSED_WITH)
                        closedAmount += operation.getAmount();
                }
                List<TillDetails> tillDetails = databaseManager.getTillDetailsByTillId(till.getId()).blockingGet();
                for (TillDetails details : tillDetails) {
                    expectedAmount += details.getExpectedTillAmount();
                }
                tillAmountVariance = expectedAmount - closedAmount;
                objects[i][4] = tillAmountVariance;

            } else {
                double moneyLeftFromPrevTill = 0;
                double thisTillStartMoney = 0;
                double closedAmount = 0;
                double expectedAmount = 0;
                double toNextAmount = 0;

                List<TillManagementOperation> thisTillOperations = databaseManager.getTillManagementOperationsByTillId(till.getId()).blockingGet();
                for (TillManagementOperation operation : thisTillOperations) {
                    if (operation.getType() == TillManagementOperation.OPENED_WITH)
                        thisTillStartMoney += operation.getAmount();
                    if (operation.getType() == TillManagementOperation.CLOSED_WITH)
                        closedAmount += operation.getAmount();
                    if (operation.getType() == TillManagementOperation.TO_NEW_TILL)
                        toNextAmount += operation.getAmount();
                }

                List<TillDetails> tillDetails = databaseManager.getTillDetailsByTillId(till.getId()).blockingGet();
                for (TillDetails details : tillDetails) {
                    expectedAmount += details.getExpectedTillAmount();
                }
                List<TillManagementOperation> prevTillOperations = databaseManager.getTillManagementOperationsByTillId(tills.get(i).getId() - 1L).blockingGet();
                for (TillManagementOperation operation : prevTillOperations) {
                    if (operation.getType() == TillManagementOperation.TO_NEW_TILL)
                        moneyLeftFromPrevTill += operation.getAmount();
                }

                startMoneyVariance = thisTillStartMoney - moneyLeftFromPrevTill;
                tillAmountVariance = expectedAmount - closedAmount - toNextAmount;

                objects[i][3] = startMoneyVariance;
                objects[i][4] = tillAmountVariance;
            }
        }
        if (isFirstTime)
            view.fillReportView(objects);
        else view.updateReportView(objects);
        isFirstTime = false;
    }

    @Override
    public void openTillDetailsDialog(Object[][] objects, int row, int column) {
        if (objects[row][0] instanceof Long) {
            Long id = (Long) objects[row][0];
            Till till = databaseManager.getTillById(id).blockingGet();
            view.openTillDetailsDialog(till);
        }

    }

    @Override
    public void exportExcel(String fileName, String path) {
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        if (searchResultsTemp != null) {
            view.exportTableToExcel(fileName, path, searchResultsTemp, date, searchText);
        } else
            view.exportTableToExcel(fileName, path, objects, date, searchText);
    }

    @Override
    public void exportPdf(String fileName, String path) {
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        if (searchResultsTemp != null) {
            view.exportTableToPdf(fileName, path, searchResultsTemp, date, searchText);
        } else
            view.exportTableToPdf(fileName, path, objects, date, searchText);
    }

    @Override
    public void exportExcelToUSB(String filename, UsbFile root) {
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        if (searchResultsTemp != null) {
            view.exportExcelToUSB(filename, root, searchResultsTemp, date, searchText);
        } else
            view.exportExcelToUSB(filename, root, objects, date, searchText);
    }

    @Override
    public void exportPdfToUSB(String filename, UsbFile root) {
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        if (searchResultsTemp != null) {
            view.exportTableToPdfToUSB(filename, root, searchResultsTemp, date, searchText);
        } else
            view.exportTableToPdfToUSB(filename, root, objects, date, searchText);
    }

    @Override
    public void onChooseDateInterval(Calendar fromDate, Calendar toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        view.updateDateIntervalUi(fromDate, toDate);
        initTableView();
    }

    private int prev = -1;
    private Object[][] searchResultsTemp;

    @Override
    public void onSearchTyped(String searchText) {
        this.searchText = searchText;
        if (searchText.isEmpty()) {
            view.updateReportView(objects);
            prev = -1;

        } else {
            if (searchText.length() <= prev || prev == -1) {
                int searchRes[] = new int[objects.length];
                for (int i = 0; i < objects.length; i++) {
                    if (String.valueOf((long) objects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }
                    if (simpleDateFormat.format(new Date((long) objects[i][1])).contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }
                    if (simpleDateFormat.format(new Date((long) objects[i][2])).contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }
                    if (decimalFormat.format((double) objects[i][3]).contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }
                    if (decimalFormat.format((double) objects[i][4]).contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }
                }

                int sumSize = 0;
                for (int i = 0; i < objects.length; i++) {
                    if (searchRes[i] == 1)
                        sumSize++;
                }
                Object[][] objectResults = new Object[sumSize][6];

                int pt = 0;
                for (int i = 0; i < objects.length; i++) {
                    if (searchRes[i] == 1) {
                        objectResults[pt] = objects[i];
                        pt++;
                    }
                }
                searchResultsTemp = objectResults.clone();
                view.setSearchResults(objectResults, searchText);
            } else {
                int searchRes[] = new int[searchResultsTemp.length];
                for (int i = 0; i < searchResultsTemp.length; i++) {
                    if (String.valueOf((long) searchResultsTemp[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }
                    if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][1])).contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }
                    if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][2])).contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }
                    if (decimalFormat.format((double) searchResultsTemp[i][3]).contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }
                    if (decimalFormat.format((double) searchResultsTemp[i][4]).contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }
                }


                int sumSize = 0;
                for (int i = 0; i < searchResultsTemp.length; i++) {
                    if (searchRes[i] == 1)
                        sumSize++;
                }
                Object[][] objectResults = new Object[sumSize][6];

                int pt = 0;
                for (int i = 0; i < searchResultsTemp.length; i++) {
                    if (searchRes[i] == 1) {
                        objectResults[pt] = searchResultsTemp[i];
                        pt++;
                    }
                }
                searchResultsTemp = objectResults.clone();
                view.setSearchResults(objectResults, searchText);
            }
            prev = searchText.length();
        }

    }

    @Override
    public void onClickedDateInterval() {
        view.openDateInterval(fromDate, toDate);
    }

    @Override
    public void onClickedExportExcel() {
        view.openExportDialog(EXCEL);
    }

    @Override
    public void onClickedExportPDF() {
        view.openExportDialog(PDF);
    }

    @Override
    public void onClickedFilter() {

    }

    @Override
    public void onChoisedPanel(int postion) {

    }

    @Override
    public void onTillPickerClicked() {

    }
}
