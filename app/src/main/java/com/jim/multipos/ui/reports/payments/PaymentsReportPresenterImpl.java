package com.jim.multipos.ui.reports.payments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillOperation;
import com.jim.multipos.ui.reports.payments.data.PaymentsReport;
import com.jim.multipos.ui.reports.payments.data.SummaryPayments;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.utils.ExportUtils.EXCEL;
import static com.jim.multipos.utils.ExportUtils.PDF;

public class PaymentsReportPresenterImpl extends BasePresenterImpl<PaymentsReportView> implements PaymentsReportPresenter {
    int[] filterConfig;
    private DatabaseManager databaseManager;
    private Context context;
    DecimalFormat decimalFormat;
    SimpleDateFormat simpleDateFormat;
    int currentPosition = 0;

    Calendar fromDateSummary, toDateSummary;
    Calendar fromDateLog, toDateLog;

    Object[][] summaryObjects;
    Object[][] logObjects;
    List<Order> ordersSummary;
    PaymentType cashPaymentType;
    List<PaymentsReport> paymentsReports;
    private String searchText;

    @Inject
    protected PaymentsReportPresenterImpl(PaymentsReportView paymentsReportView, DatabaseManager databaseManager, Context context) {
        super(paymentsReportView);
        this.databaseManager = databaseManager;
        this.context = context;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        paymentsReports = new ArrayList<>();
        filterConfig = new int[]{1, 1, 1, 1, 1, 1, 1};
        cashPaymentType = databaseManager.getCashPaymentType();

    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        initDateInterval();
        initDecimal();
        new Handler().postDelayed(() -> {
            databaseManager.getOrdersInIntervalForReport(this.fromDateSummary, this.toDateSummary).subscribe((orders1, throwable) -> {
                ordersSummary = orders1;
                updateObejctsForTable();
                view.initTable(summaryObjects, currentPosition);
            });
        }, 50);
        view.updateDateIntervalUi(fromDateSummary, toDateSummary);

    }

    @Override
    public void onChooseDateInterval(Calendar fromDate, Calendar toDate) {
        if (currentPosition == 0) {
            this.fromDateSummary = fromDate;
            this.toDateSummary = toDate;
            databaseManager.getOrdersInIntervalForReport(fromDate, toDate).subscribe((orders1, throwable) -> {
                ordersSummary = orders1;
                updateObejctsForTable();
                view.updateTable(summaryObjects, currentPosition);
                view.updateDateIntervalUi(fromDateSummary, toDateSummary);
            });
        } else {
            this.fromDateLog = fromDate;
            this.toDateLog = toDate;
            fillPaymentsDetialReportPreData(fromDate, toDate);
            updateObejctsForTable();
            view.updateTable(logObjects, currentPosition);
            view.updateDateIntervalUi(fromDateLog, toDateLog);
        }


    }

    int prev = -1;
    Object[][] searchResultsTemp;

    @Override
    public void onSearchTyped(String searchText) {
        this.searchText = searchText;
        if (searchText.isEmpty()) {
            view.initTable(currentPosition == 0 ? summaryObjects : logObjects, currentPosition);
            prev = -1;
        } else {
            if (searchText.length() <= prev || prev == -1) {
                Object[][] objects;
                if (currentPosition == 0) objects = summaryObjects;
                else objects = logObjects;

                int searchRes[] = new int[objects.length];

                if (currentPosition == 0) {
                    for (int i = 0; i < objects.length; i++) {
                        if (((String) objects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) objects[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) objects[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                    }
                } else {
                    for (int i = 0; i < objects.length; i++) {
                        if (((String) objects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (((String) objects[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (((String) objects[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (((String) objects[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (String.valueOf(objects[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (String.valueOf(objects[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (simpleDateFormat.format(new Date((long) objects[i][6])).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) objects[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                    }
                }
                int sumSize = 0;
                for (int i = 0; i < objects.length; i++) {
                    if (searchRes[i] == 1)
                        sumSize++;
                }
                Object[][] objectResults = new Object[sumSize][10];

                int pt = 0;
                for (int i = 0; i < objects.length; i++) {
                    if (searchRes[i] == 1) {
                        objectResults[pt] = objects[i];
                        pt++;
                    }
                }
                searchResultsTemp = objectResults.clone();
                view.searchTable(objectResults, currentPosition, searchText);

            } else {
                int searchRes[] = new int[searchResultsTemp.length];

                if (currentPosition == 0) {
                    for (int i = 0; i < searchResultsTemp.length; i++) {
                        if (((String) searchResultsTemp[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) searchResultsTemp[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) searchResultsTemp[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                    }
                } else {
                    for (int i = 0; i < searchResultsTemp.length; i++) {
                        if (((String) searchResultsTemp[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (((String) searchResultsTemp[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (((String) searchResultsTemp[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (((String) searchResultsTemp[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (String.valueOf(searchResultsTemp[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (String.valueOf(searchResultsTemp[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][6])).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) searchResultsTemp[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                    }
                }

                int sumSize = 0;
                for (int i = 0; i < searchResultsTemp.length; i++) {
                    if (searchRes[i] == 1)
                        sumSize++;
                }
                Object[][] objectResults = new Object[sumSize][10];

                int pt = 0;
                for (int i = 0; i < searchResultsTemp.length; i++) {
                    if (searchRes[i] == 1) {
                        objectResults[pt] = searchResultsTemp[i];
                        pt++;
                    }
                }
                searchResultsTemp = objectResults.clone();
                view.searchTable(objectResults, currentPosition, searchText);

            }

        }
    }

    @Override
    public void onClickedDateInterval() {
        view.openDateInterval(currentPosition == 0 ? fromDateSummary : fromDateLog, currentPosition == 0 ? toDateSummary : toDateLog);

    }

    @Override
    public void onClickedExportExcel() {
        view.openExportDialog(currentPosition, EXCEL);
    }

    @Override
    public void onClickedExportPDF() {
        view.openExportDialog(currentPosition, PDF);
    }

    @Override
    public void onClickedFilter() {
        if (currentPosition == 1) {
            view.showFilterPanel(filterConfig);
        }
    }

    @Override
    public void onChoisedPanel(int postion) {
        currentPosition = postion;
        prev = -1;
        searchResultsTemp = null;
        if (currentPosition == 0) {
            if (summaryObjects == null)
                updateObejctsForTable();
            view.updateTable(summaryObjects, currentPosition);
            view.updateDateIntervalUi(fromDateSummary, toDateSummary);
        } else {
            if (logObjects == null) {
                fillPaymentsDetialReportPreData(fromDateLog, toDateLog);
                updateObejctsForTable();
            }
            view.updateTable(logObjects, currentPosition);
            view.updateDateIntervalUi(fromDateLog, toDateLog);
        }
    }

    @Override
    public void onTillPickerClicked() {

    }

    private void updateObejctsForTable() {
        if (currentPosition == 0) {
            HashMap<Long, SummaryPayments> summaryPaymentsHP = new HashMap<>();
            for (int i = 0; i < ordersSummary.size(); i++) {
                if (ordersSummary.get(i).getStatus() != Order.CANCELED_ORDER) {
                    for (int j = 0; j < ordersSummary.get(i).getPayedPartitions().size(); j++) {
                        SummaryPayments summaryPayments = summaryPaymentsHP.get(ordersSummary.get(i).getPayedPartitions().get(j).getPaymentId());
                        if (summaryPayments == null) {
                            summaryPayments = new SummaryPayments();
                            summaryPayments.setId(ordersSummary.get(i).getPayedPartitions().get(j).getPaymentId());
                            summaryPayments.setName(ordersSummary.get(i).getPayedPartitions().get(j).getPaymentType().getName());
                        }
                        summaryPayments.plusGoToOrder(ordersSummary.get(i).getPayedPartitions().get(j).getValue());
                        summaryPaymentsHP.put(summaryPayments.getId(), summaryPayments);
                    }
                    if (ordersSummary.get(i).getChange() != 0) {
                        SummaryPayments summaryPayments = summaryPaymentsHP.get(cashPaymentType.getId());
                        if (summaryPayments == null) {
                            summaryPayments = new SummaryPayments();
                            summaryPayments.setId(cashPaymentType.getId());
                            summaryPayments.setName(cashPaymentType.getName());
                        }
                        summaryPayments.plusGoToOrder(ordersSummary.get(i).getChange());
                        summaryPaymentsHP.put(summaryPayments.getId(), summaryPayments);
                    }
                }
            }
            double allAmounts = 0;
            for (SummaryPayments summaryPayments : summaryPaymentsHP.values()) {
                allAmounts += summaryPayments.getGotToOrder();
            }

            summaryObjects = new Object[summaryPaymentsHP.size()][3];
            int cont = 0;
            for (SummaryPayments summaryPayments : summaryPaymentsHP.values()) {
                summaryObjects[cont][0] = summaryPayments.getName();
                summaryObjects[cont][1] = summaryPayments.getGotToOrder();
                summaryObjects[cont][2] = summaryPayments.getGotToOrder() / allAmounts * 100;
                cont++;
            }
        } else {
            List<PaymentsReport> paymentsReportsTemp = new ArrayList<>();
            for (int i = 0; i < paymentsReports.size(); i++) {
                if (filterConfig[0] == 0 && paymentsReports.get(i).getFilterId() == FILTER_PAY_TO_ORDER) {
                    continue;
                }
                if (filterConfig[1] == 0 && paymentsReports.get(i).getFilterId() == FILTER_CHANGE) {
                    continue;
                }
                if (filterConfig[2] == 0 && paymentsReports.get(i).getFilterId() == FILTER_PAY_IN) {
                    continue;
                }
                if (filterConfig[3] == 0 && paymentsReports.get(i).getFilterId() == FILTER_PAY_OUT) {
                    continue;
                }
                if (filterConfig[4] == 0 && paymentsReports.get(i).getFilterId() == FILTER_PAY_TO_VENDOR) {
                    continue;
                }
                if (filterConfig[5] == 0 && paymentsReports.get(i).getFilterId() == FILTER_BANK_DROP) {
                    continue;
                }
                if (filterConfig[6] == 0 && paymentsReports.get(i).getFilterId() == FILTER_DEBTS_IN) {
                    continue;
                }
                paymentsReportsTemp.add(paymentsReports.get(i));
            }
            Object[][] objects = new Object[paymentsReportsTemp.size()][8];
            Collections.sort(paymentsReportsTemp, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));

            for (int i = 0; i < paymentsReportsTemp.size(); i++) {
                objects[i][0] = paymentsReportsTemp.get(i).getAccountName();
                objects[i][1] = paymentsReportsTemp.get(i).getPaymentName();
                objects[i][2] = paymentsReportsTemp.get(i).getReason();
                objects[i][3] = paymentsReportsTemp.get(i).getDescription();
                objects[i][4] = paymentsReportsTemp.get(i).getOrderId() == -1 ? "" : paymentsReportsTemp.get(i).getOrderId();
                objects[i][5] = paymentsReportsTemp.get(i).getTillId() == -1 ? "" : paymentsReportsTemp.get(i).getTillId();
                objects[i][6] = paymentsReportsTemp.get(i).getDate();
                objects[i][7] = paymentsReportsTemp.get(i).getAmount();
            }
            logObjects = objects;
        }

    }

    public void initDateInterval() {
        fromDateSummary = new GregorianCalendar();
        fromDateSummary.set(Calendar.HOUR, 0);
        fromDateSummary.set(Calendar.MINUTE, 0);
        fromDateSummary.set(Calendar.MILLISECOND, 0);
        // init first last month
        fromDateSummary.add(Calendar.MONTH, -1);

        toDateSummary = new GregorianCalendar();
        toDateSummary.set(Calendar.HOUR, 23);
        toDateSummary.set(Calendar.MINUTE, 59);
        toDateSummary.set(Calendar.MILLISECOND, 999);

        fromDateLog = (Calendar) fromDateSummary.clone();
        toDateLog = (Calendar) toDateSummary.clone();

        view.updateDateIntervalUi(currentPosition == 0 ? fromDateSummary : toDateSummary, currentPosition == 0 ? fromDateLog : toDateLog);
    }

    private void initDecimal() {
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat1.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat1.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat = decimalFormat1;
    }

    @Override
    public void filterConfigsChanged(int[] configs) {
        this.filterConfig = configs;
        updateObejctsForTable();
        view.initTable(currentPosition == 0 ? summaryObjects : logObjects, currentPosition);
    }

    @Override
    public void onActionPressed(Object[][] objects, int row, int column) {
        if (currentPosition == 1) {
            if (column == 5) {
                long tillId = (Long) objects[row][column];
                databaseManager.getTillById(tillId).subscribe(till -> {
                    if (till.getStatus() == Till.CLOSED)
                        view.onTillPressed(databaseManager, till);
                    else view.onTillNotClosed();
                });
            } else if (column == 4) {
                if (!objects[row][column].equals("")) {
                    long orderId = (long) objects[row][column];
                    databaseManager.getOrder(orderId).subscribe(order -> {
                        view.onOrderPressed(order);
                    });
                }
            }
        }
    }

    @Override
    public void exportExcel(String fileName, String path) {
        String filter = "";
        switch (currentPosition) {
            case 0:
                String date = simpleDateFormat.format(fromDateSummary.getTime()) + " - " + simpleDateFormat.format(toDateSummary.getTime());
                if (searchResultsTemp != null) {
                    view.exportTableToExcel(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToExcel(fileName, path, summaryObjects, currentPosition, date, filter, searchText);
                break;
            case 1:
                String date1 = simpleDateFormat.format(fromDateLog.getTime()) + " - " + simpleDateFormat.format(toDateLog.getTime());
                StringBuilder filters = new StringBuilder();
                if (filterConfig[0] == 1) {
                    filters.append(context.getString(R.string.payment_to_order)).append(" ");
                }
                if (filterConfig[1] == 1) {
                    filters.append(context.getString(R.string.change)).append(" ");
                }
                if (filterConfig[2] == 1) {
                    filters.append(context.getString(R.string.pay_in)).append(" ");
                }
                if (filterConfig[3] == 1) {
                    filters.append(context.getString(R.string.pay_out)).append(" ");
                }
                if (filterConfig[4] == 1) {
                    filters.append(context.getString(R.string.payment_to_vendor)).append(" ");
                }
                if (filterConfig[5] == 1) {
                    filters.append(context.getString(R.string.bank_drop)).append(" ");
                }
                if (filterConfig[6] == 1) {
                    filters.append(context.getString(R.string.debt_in));
                }
                filter = filters.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToExcel(fileName, path, searchResultsTemp, currentPosition, date1, filter, searchText);
                } else
                    view.exportTableToExcel(fileName, path, logObjects, currentPosition, date1, filter, searchText);
                break;
        }
    }

    @Override
    public void exportPdf(String fileName, String path) {
        String filter = "";
        switch (currentPosition) {
            case 0:
                String date = simpleDateFormat.format(fromDateSummary.getTime()) + " - " + simpleDateFormat.format(toDateSummary.getTime());
                if (searchResultsTemp != null) {
                    view.exportTableToPdf(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdf(fileName, path, summaryObjects, currentPosition, date, filter, searchText);
                break;
            case 1:
                String date1 = simpleDateFormat.format(fromDateLog.getTime()) + " - " + simpleDateFormat.format(toDateLog.getTime());
                StringBuilder filters = new StringBuilder();
                if (filterConfig[0] == 1) {
                    filters.append(context.getString(R.string.payment_to_order)).append(" ");
                }
                if (filterConfig[1] == 1) {
                    filters.append(context.getString(R.string.change)).append(" ");
                }
                if (filterConfig[2] == 1) {
                    filters.append(context.getString(R.string.pay_in)).append(" ");
                }
                if (filterConfig[3] == 1) {
                    filters.append(context.getString(R.string.pay_out)).append(" ");
                }
                if (filterConfig[4] == 1) {
                    filters.append(context.getString(R.string.payment_to_vendor)).append(" ");
                }
                if (filterConfig[5] == 1) {
                    filters.append(context.getString(R.string.bank_drop)).append(" ");
                }
                if (filterConfig[6] == 1) {
                    filters.append(context.getString(R.string.debt_in));
                }
                filter = filters.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToPdf(fileName, path, searchResultsTemp, currentPosition, date1, filter, searchText);
                } else
                    view.exportTableToPdf(fileName, path, logObjects, currentPosition, date1, filter, searchText);
                break;
        }
    }

    @Override
    public void exportExcelToUSB(String filename, UsbFile root) {
        String filter = "";
        switch (currentPosition) {
            case 0:
                String date = simpleDateFormat.format(fromDateSummary.getTime()) + " - " + simpleDateFormat.format(toDateSummary.getTime());
                if (searchResultsTemp != null) {
                    view.exportExcelToUSB(filename, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportExcelToUSB(filename, root, summaryObjects, currentPosition, date, filter, searchText);
                break;
            case 1:
                String date1 = simpleDateFormat.format(fromDateLog.getTime()) + " - " + simpleDateFormat.format(toDateLog.getTime());
                StringBuilder filters = new StringBuilder();
                if (filterConfig[0] == 1) {
                    filters.append(context.getString(R.string.payment_to_order)).append(" ");
                }
                if (filterConfig[1] == 1) {
                    filters.append(context.getString(R.string.change)).append(" ");
                }
                if (filterConfig[2] == 1) {
                    filters.append(context.getString(R.string.pay_in)).append(" ");
                }
                if (filterConfig[3] == 1) {
                    filters.append(context.getString(R.string.pay_out)).append(" ");
                }
                if (filterConfig[4] == 1) {
                    filters.append(context.getString(R.string.payment_to_vendor)).append(" ");
                }
                if (filterConfig[5] == 1) {
                    filters.append(context.getString(R.string.bank_drop)).append(" ");
                }
                if (filterConfig[6] == 1) {
                    filters.append(context.getString(R.string.debt_in));
                }
                filter = filters.toString();
                if (searchResultsTemp != null) {
                    view.exportExcelToUSB(filename, root, searchResultsTemp, currentPosition, date1, filter, searchText);
                } else
                    view.exportExcelToUSB(filename, root, logObjects, currentPosition, date1, filter, searchText);

                break;
        }
    }

    @Override
    public void exportPdfToUSB(String filename, UsbFile root) {
        String filter = "";
        switch (currentPosition) {
            case 0:
                String date = simpleDateFormat.format(fromDateSummary.getTime()) + " - " + simpleDateFormat.format(toDateSummary.getTime());
                if (searchResultsTemp != null) {
                    view.exportTableToPdfToUSB(filename, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdfToUSB(filename, root, summaryObjects, currentPosition, date, filter, searchText);

                break;
            case 1:
                String date1 = simpleDateFormat.format(fromDateLog.getTime()) + " - " + simpleDateFormat.format(toDateLog.getTime());
                StringBuilder filters = new StringBuilder();
                if (filterConfig[0] == 1) {
                    filters.append(context.getString(R.string.payment_to_order)).append(" ");
                }
                if (filterConfig[1] == 1) {
                    filters.append(context.getString(R.string.change)).append(" ");
                }
                if (filterConfig[2] == 1) {
                    filters.append(context.getString(R.string.pay_in)).append(" ");
                }
                if (filterConfig[3] == 1) {
                    filters.append(context.getString(R.string.pay_out)).append(" ");
                }
                if (filterConfig[4] == 1) {
                    filters.append(context.getString(R.string.payment_to_vendor)).append(" ");
                }
                if (filterConfig[5] == 1) {
                    filters.append(context.getString(R.string.bank_drop)).append(" ");
                }
                if (filterConfig[6] == 1) {
                    filters.append(context.getString(R.string.debt_in));
                }
                filter = filters.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToPdfToUSB(filename, root, searchResultsTemp, currentPosition, date1, filter, searchText);
                } else
                    view.exportTableToPdfToUSB(filename, root, logObjects, currentPosition, date1, filter, searchText);
                break;
        }
    }

    private void fillPaymentsDetialReportPreData(Calendar fromDate, Calendar toDate) {
        paymentsReports.clear();
        databaseManager.getOrdersInIntervalForReport(fromDate, toDate).subscribe((orders, throwable) -> {
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getStatus() != Order.CANCELED_ORDER) {
                    for (int j = 0; j < orders.get(i).getPayedPartitions().size(); j++) {
                        PaymentsReport paymentsReport = new PaymentsReport();
                        paymentsReport.setFilterId(FILTER_PAY_TO_ORDER);
                        paymentsReport.setPaymentName(orders.get(i).getPayedPartitions().get(j).getPaymentType().getName());
                        paymentsReport.setAccountName(orders.get(i).getPayedPartitions().get(j).getPaymentType().getAccount().getName());
                        paymentsReport.setReason(context.getString(R.string.payment_to_order));
                        paymentsReport.setOrderId(orders.get(i).getId());
                        paymentsReport.setTillId(orders.get(i).getTillId());
                        paymentsReport.setDate(orders.get(i).getCreateAt());
                        paymentsReport.setAmount(orders.get(i).getPayedPartitions().get(j).getValue());
                        paymentsReports.add(paymentsReport);
                    }
                    if (orders.get(i).getChange() != 0) {
                        PaymentsReport paymentsReport = new PaymentsReport();
                        paymentsReport.setFilterId(FILTER_CHANGE);
                        paymentsReport.setPaymentName(cashPaymentType.getName());
                        paymentsReport.setAccountName(cashPaymentType.getAccount().getName());
                        paymentsReport.setReason(context.getString(R.string.change));
                        paymentsReport.setOrderId(orders.get(i).getId());
                        paymentsReport.setTillId(orders.get(i).getTillId());
                        paymentsReport.setDate(orders.get(i).getCreateAt() + 1);
                        paymentsReport.setAmount(orders.get(i).getChange()*-1);
                        paymentsReports.add(paymentsReport);
                    }
                }

            }
            databaseManager.getTillOperationsInterval(fromDate, toDate).subscribe((tillOperations, throwable1) -> {
                for (int i = 0; i < tillOperations.size(); i++) {
                    PaymentsReport paymentsReport = new PaymentsReport();
                    paymentsReport.setPaymentName(tillOperations.get(i).getPaymentType().getName());
                    paymentsReport.setAccountName(tillOperations.get(i).getPaymentType().getAccount().getName());
                    if (tillOperations.get(i).getType() == TillOperation.PAY_IN) {
                        paymentsReport.setReason(context.getString(R.string.pay_in));
                        paymentsReport.setAmount(tillOperations.get(i).getAmount());
                        paymentsReport.setFilterId(FILTER_PAY_IN);
                    } else if (tillOperations.get(i).getType() == TillOperation.PAY_OUT) {
                        paymentsReport.setReason(context.getString(R.string.pay_out));
                        paymentsReport.setAmount(-1*tillOperations.get(i).getAmount());
                        paymentsReport.setFilterId(FILTER_PAY_OUT);
                    } else if (tillOperations.get(i).getType() == TillOperation.BANK_DROP) {
                        paymentsReport.setReason(context.getString(R.string.bank_drop));
                        paymentsReport.setFilterId(FILTER_BANK_DROP);
                        paymentsReport.setAmount(-1*tillOperations.get(i).getAmount());
                    }
                    paymentsReport.setDescription(tillOperations.get(i).getDescription());
                    paymentsReport.setOrderId(-1); // empty String
                    paymentsReport.setTillId(tillOperations.get(i).getTillId());
                    paymentsReport.setDate(tillOperations.get(i).getCreateAt());
                    paymentsReports.add(paymentsReport);
                }
                databaseManager.getBillingOperationsInInterval(fromDate, toDate).subscribe((billingOperations, throwable2) -> {
                    for (int i = 0; i < billingOperations.size(); i++) {
                        PaymentsReport paymentsReport = new PaymentsReport();
                        paymentsReport.setFilterId(FILTER_PAY_TO_VENDOR);
                        paymentsReport.setPaymentName("");
                        paymentsReport.setReason(context.getString(R.string.payment_to_vendor));
                        paymentsReport.setDescription(billingOperations.get(i).getDescription());
                        if (billingOperations.get(i).getAccount() != null)
                            paymentsReport.setAccountName(billingOperations.get(i).getAccount().getName());
                        else paymentsReport.setAccountName(context.getString(R.string.none));
                        paymentsReport.setOrderId(-1);
                        paymentsReport.setTillId(-1);
                        paymentsReport.setDate(billingOperations.get(i).getCreateAt());
                        paymentsReport.setAmount(-1*billingOperations.get(i).getAmount());
                        paymentsReports.add(paymentsReport);
                    }

                    databaseManager.getCustomerPaymentsByInterval(fromDate, toDate).subscribe((customerPayments, throwable3) -> {
                        for (int i = 0; i < customerPayments.size(); i++) {
                            PaymentsReport paymentsReport = new PaymentsReport();
                            paymentsReport.setFilterId(FILTER_DEBTS_IN);
                            paymentsReport.setPaymentName(customerPayments.get(i).getPaymentType().getName());
                            paymentsReport.setAccountName(customerPayments.get(i).getPaymentType().getAccount().getName());
                            paymentsReport.setOrderId(customerPayments.get(i).getDebt().getOrderId());
                            paymentsReport.setReason(context.getString(R.string.debt_in));
                            paymentsReport.setTillId(-1);
                            paymentsReport.setDate(customerPayments.get(i).getPaymentDate());
                            paymentsReport.setAmount(customerPayments.get(i).getPaymentAmount());
                            paymentsReports.add(paymentsReport);
                        }
                    });
                });
            });
        });

    }

    public static final int FILTER_PAY_TO_ORDER = 0;
    public static final int FILTER_CHANGE = 1;
    public static final int FILTER_PAY_IN = 2;
    public static final int FILTER_PAY_OUT = 3;
    public static final int FILTER_PAY_TO_VENDOR = 4;
    public static final int FILTER_BANK_DROP = 5;
    public static final int FILTER_DEBTS_IN = 6;

}
