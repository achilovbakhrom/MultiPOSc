package com.jim.multipos.ui.reports.stock_operations;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.customer.CustomerPayment;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.reports.debts.model.CustomerDebtLog;
import com.jim.multipos.ui.reports.stock_operations.model.OperationSummaryItem;
import com.jim.multipos.utils.ReportUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

public class StockOperationPresenterImpl extends BasePresenterImpl<StockOperationView> implements StockOperationPresenter {
    private DatabaseManager databaseManager;
    private Context context;
    private Object[][] firstObjects;
    private Object[][] secondObjects;
    private Object[][] thirdObjects;
    private Object[][] forthObjects;
    private Object[][] fifthObjects;
    private int currentPosition = 0;
    private Calendar fromDate;
    private Calendar toDate;
    private DecimalFormat decimalFormat;
    private int[] filterConfig;
    private SimpleDateFormat simpleDateFormat;
    private String searchText = "";

    @Inject
    protected StockOperationPresenterImpl(StockOperationView stockOperationView, DatabaseManager databaseManager, Context context   ) {
        super(stockOperationView);
        this.databaseManager = databaseManager;
        this.context = context;
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat1.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat1.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat = decimalFormat1;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
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
            initReportTable();
            view.initTable(firstObjects);
        }, 50);
    }

    private void initReportTable() {
        switch (currentPosition) {
            case 0:
                List<OperationSummaryItem> operationSummaryItems = databaseManager.getOperationsSummary(fromDate.getTime(),toDate.getTime()).blockingGet();
                firstObjects = new Object[operationSummaryItems.size()][8];
                for (int i = 0; i < operationSummaryItems.size(); i++) {
                    firstObjects[i][0] = ReportUtils.getProductName(operationSummaryItems.get(i).getProduct());
                    firstObjects[i][1] = operationSummaryItems.get(i).getSales();
                    firstObjects[i][2] = operationSummaryItems.get(i).getReciveFromVendor();
                    firstObjects[i][3] = operationSummaryItems.get(i).getReturnToVendor();
                    firstObjects[i][4] = operationSummaryItems.get(i).getReturnFromCustomer();
                    firstObjects[i][5] = operationSummaryItems.get(i).getSurplus();
                    firstObjects[i][6] = operationSummaryItems.get(i).getWaste();
                    firstObjects[i][7] = operationSummaryItems.get(i).getProduct().getMainUnit().getAbbr();
                }
                break;
            case 1:
                List<OutcomeProduct> outcomeProducts = databaseManager.getOutcomeProductsForPeriod(fromDate,toDate).blockingGet();
                secondObjects = new Object[outcomeProducts.size()][8];
                for (int i = 0; i < outcomeProducts.size(); i++) {
                    secondObjects[i][0] = ReportUtils.getProductName(outcomeProducts.get(i).getProduct());
                    secondObjects[i][1] = outcomeProducts.get(i).getOutcomeType();
                    if(outcomeProducts.get(i).getOutcomeType() == OutcomeProduct.ORDER_SALES)
                        secondObjects[i][2] = outcomeProducts.get(i).getOrderProduct().getOrderId();
                    else secondObjects[i][2] = "";
                    String stockKeepingType = "";
                    switch (outcomeProducts.get(i).getProduct().getStockKeepType()){
                        case Product.LIFO:
                            stockKeepingType = "LIFO";
                            break;
                        case Product.FEFO:
                            stockKeepingType = "FEFO";
                            break;
                        case Product.FIFO:
                            stockKeepingType = "FIFO";
                             break;
                    }
                    secondObjects[i][3] = stockKeepingType;
                    secondObjects[i][4] = outcomeProducts.get(i).getOutcomeDate();
                    secondObjects[i][5] = outcomeProducts.get(i).getSumCountValue();
                    secondObjects[i][6] = outcomeProducts.get(i).getSumCostValue();
                    secondObjects[i][7] = outcomeProducts.get(i).getDiscription();
                }
                break;
            case 2:
                List<IncomeProduct> incomeProducts = databaseManager.getIncomeProductsForPeriod(fromDate,toDate).blockingGet();
                thirdObjects = new Object[incomeProducts.size()][8];
                for (int i = 0; i < incomeProducts.size(); i++) {
                    thirdObjects[i][0] = ReportUtils.getProductName(incomeProducts.get(i).getProduct());
                    if(incomeProducts.get(i).getVendor()!=null)
                        thirdObjects[i][1] = incomeProducts.get(i).getVendor().getName();
                    else thirdObjects[i][1] = "";
                    thirdObjects[i][2] = incomeProducts.get(i).getIncomeType();
                    if(incomeProducts.get(i).getInvoice()!=null)
                        thirdObjects[i][3] = incomeProducts.get(i).getInvoice().getId();
                    else thirdObjects[i][3] = "";
                    thirdObjects[i][4] = incomeProducts.get(i).getIncomeDate();
                    thirdObjects[i][5] = incomeProducts.get(i).getCountValue();
                    thirdObjects[i][6] = incomeProducts.get(i).getCostValue()*incomeProducts.get(i).getCountValue();
                    thirdObjects[i][7] = incomeProducts.get(i).getDescription();
                }
                break;
            case 3:
                List<StockQueue>  stockQueues = databaseManager.getStockQueueForPeriod(fromDate,toDate).blockingGet();
                forthObjects = new Object[stockQueues.size()][10];
                for (int i = 0; i < stockQueues.size(); i++) {
                    forthObjects[i][0] = stockQueues.get(i).getId();
                    forthObjects[i][1] = ReportUtils.getProductName(stockQueues.get(i).getProduct());
                    if(stockQueues.get(i).getVendor()!=null)
                        forthObjects[i][2] = stockQueues.get(i).getVendor().getName();
                    else forthObjects[i][2] = "";
                    forthObjects[i][3] = stockQueues.get(i).getIncomeProduct().getIncomeType();
                    if(stockQueues.get(i).getIncomeProduct().getIncomeType() == IncomeProduct.INVOICE_PRODUCT)
                        forthObjects[i][4] = stockQueues.get(i).getIncomeProduct().getInvoiceId();
                    else forthObjects[i][4] = "";
                    forthObjects[i][5] = stockQueues.get(i).getIncomeProductDate();
                    forthObjects[i][6] = stockQueues.get(i).getCost();
                    forthObjects[i][7] = stockQueues.get(i).getIncomeCount();
                    forthObjects[i][8] = stockQueues.get(i).getAvailable();
                    forthObjects[i][9] = stockQueues.get(i).getStockId();
                }
                break;
            case 4:
                List<StockQueue>  stockQueuesForDetial  = databaseManager.getStockQueueUsedForPeriod(fromDate,toDate).blockingGet();
                int size = 0;
                for (int i = 0; i < stockQueuesForDetial.size(); i++) {
                    for (int j = 0; j < stockQueuesForDetial.get(i).getDetialCounts().size(); j++) {
                        size++;
                    }
                }
                int counter = 0;
                fifthObjects = new Object[size][8];
                for (int i = 0; i < stockQueuesForDetial.size(); i++) {
                    for (int j = 0; j < stockQueuesForDetial.get(i).getDetialCounts().size(); j++) {
                        fifthObjects[counter][0] = stockQueuesForDetial.get(i).getId();
                        fifthObjects[counter][1] = ReportUtils.getProductName(stockQueuesForDetial.get(i).getProduct());
                        fifthObjects[counter][2] = stockQueuesForDetial.get(i).getDetialCounts().get(j).getOutcomeProduct().getOutcomeType();
                        if(stockQueuesForDetial.get(i).getDetialCounts().get(j).getOutcomeProduct().getOutcomeType() == OutcomeProduct.ORDER_SALES)
                        fifthObjects[counter][3] = stockQueuesForDetial.get(i).getDetialCounts().get(j).getOutcomeProduct().getOrderProduct().getOrderId();
                        else  fifthObjects[counter][3] = "";
                        fifthObjects[counter][4] = stockQueuesForDetial.get(i).getDetialCounts().get(j).getOutcomeProduct().getOutcomeDate();
                        fifthObjects[counter][5] = stockQueuesForDetial.get(i).getDetialCounts().get(j).getCount();
                        fifthObjects[counter][6] = stockQueuesForDetial.get(i).getDetialCounts().get(j).getCost();
                        fifthObjects[counter][7] = stockQueuesForDetial.get(i).getStockId();
                        counter++;

                    }


                }

                break;
        }
    }


    @Override
    public void onChooseDateInterval(Calendar fromDate, Calendar toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        view.updateDateIntervalUi(fromDate, toDate);
        updateTable();
    }
    private int prev = -1;
    private Object[][] searchResultsTemp;
    @Override
    public void onSearchTyped(String searchText) {

    }

    @Override
    public void onClickedDateInterval() {

    }

    @Override
    public void onClickedExportExcel() {

    }

    @Override
    public void onClickedExportPDF() {

    }

    @Override
    public void onClickedFilter() {

    }

    @Override
    public void onChoisedPanel(int postion) {
        this.currentPosition = postion;
        prev = -1;
        searchResultsTemp = null;

        if (currentPosition == 0) {
            if (firstObjects == null)
                initReportTable();
            view.updateTable(firstObjects, currentPosition);
        }else if(currentPosition == 1) {
            if (secondObjects == null) {
                initReportTable();
            }
            view.updateTable(secondObjects, currentPosition);
        }else if(currentPosition == 2){
            if (thirdObjects == null) {
                initReportTable();
            }
            view.updateTable(thirdObjects, currentPosition);
        }else if(currentPosition == 3){
            if (forthObjects == null) {
                initReportTable();
            }
            view.updateTable(forthObjects, currentPosition);
        }else if(currentPosition == 4){
            if (fifthObjects == null) {
                initReportTable();
            }
            view.updateTable(fifthObjects, currentPosition);
        }
    }

    @Override
    public void onTillPickerClicked() {

    }


    @Override
    public void onAction(Object[][] objects, int row, int column) {

    }

    private void updateTable() {
        switch (currentPosition) {
            case 0:
                initReportTable();
                view.updateTable(firstObjects, currentPosition);
                break;
            case 1:
                initReportTable();
                view.updateTable(secondObjects, currentPosition);
                break;
            case 2:
                initReportTable();
                view.updateTable(thirdObjects, currentPosition);
                break;
            case 3:
                initReportTable();
                view.updateTable(forthObjects, currentPosition);
                break;
            case 4:
                initReportTable();
                view.updateTable(fifthObjects, currentPosition);
                break;
        }
    }

}
