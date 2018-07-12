package com.jim.multipos.ui.reports.stock_operations;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.R;
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

import static com.jim.multipos.utils.ExportUtils.EXCEL;
import static com.jim.multipos.utils.ExportUtils.PDF;

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
    private int[] secondFilterConfig;
    private int[] thirdFilterConfig;
    private int[] forthFilterConfig;
    private int[] fifthFilterConfig;
    private SimpleDateFormat simpleDateFormat;
    private String searchText = "";

    @Inject
    protected StockOperationPresenterImpl(StockOperationView stockOperationView, DatabaseManager databaseManager, Context context) {
        super(stockOperationView);
        this.databaseManager = databaseManager;
        this.context = context;
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat1.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat1.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat = decimalFormat1;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        secondFilterConfig = new int[]{1, 1, 1};
        thirdFilterConfig = new int[]{1, 1, 1};
        forthFilterConfig = new int[]{1, 1, 1};
        fifthFilterConfig = new int[]{1, 1, 1};
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
                List<OperationSummaryItem> operationSummaryItems = databaseManager.getOperationsSummary(fromDate.getTime(), toDate.getTime()).blockingGet();
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
                List<OutcomeProduct> temp = databaseManager.getOutcomeProductsForPeriod(fromDate, toDate).blockingGet();
                List<OutcomeProduct> outcomeProducts = new ArrayList<>();

                for (int i = 0; i < temp.size(); i++) {
                    if (secondFilterConfig[0] == 1 && temp.get(i).getOutcomeType() == OutcomeProduct.ORDER_SALES) {
                        outcomeProducts.add(temp.get(i));
                        continue;
                    }
                    if (secondFilterConfig[1] == 1 && temp.get(i).getOutcomeType() == OutcomeProduct.OUTVOICE_TO_VENDOR) {
                        outcomeProducts.add(temp.get(i));
                        continue;
                    }
                    if (secondFilterConfig[2] == 1 && temp.get(i).getOutcomeType() == OutcomeProduct.WASTE) {
                        outcomeProducts.add(temp.get(i));
                        continue;
                    }
                }
                secondObjects = new Object[outcomeProducts.size()][8];
                for (int i = 0; i < outcomeProducts.size(); i++) {
                    secondObjects[i][0] = ReportUtils.getProductName(outcomeProducts.get(i).getProduct());
                    secondObjects[i][1] = outcomeProducts.get(i).getOutcomeType();
                    if (outcomeProducts.get(i).getOutcomeType() == OutcomeProduct.ORDER_SALES)
                        secondObjects[i][2] = outcomeProducts.get(i).getOrderProduct().getOrderId();
                    else secondObjects[i][2] = "";
                    String stockKeepingType = "";
                    switch (outcomeProducts.get(i).getProduct().getStockKeepType()) {
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
                List<IncomeProduct> temp2 = databaseManager.getIncomeProductsForPeriod(fromDate, toDate).blockingGet();
                List<IncomeProduct> incomeProducts = new ArrayList<>();
                for (int i = 0; i < temp2.size(); i++) {
                    if (thirdFilterConfig[0] == 1 && temp2.get(i).getIncomeType() == IncomeProduct.INVOICE_PRODUCT) {
                        incomeProducts.add(temp2.get(i));
                        continue;
                    }
                    if (thirdFilterConfig[1] == 1 && temp2.get(i).getIncomeType() == IncomeProduct.SURPLUS_PRODUCT) {
                        incomeProducts.add(temp2.get(i));
                        continue;
                    }
                    if (thirdFilterConfig[2] == 1 && temp2.get(i).getIncomeType() == IncomeProduct.RETURNED_PRODUCT) {
                        incomeProducts.add(temp2.get(i));
                        continue;
                    }
                }
                thirdObjects = new Object[incomeProducts.size()][8];
                for (int i = 0; i < incomeProducts.size(); i++) {
                    thirdObjects[i][0] = ReportUtils.getProductName(incomeProducts.get(i).getProduct());
                    if (incomeProducts.get(i).getVendor() != null)
                        thirdObjects[i][1] = incomeProducts.get(i).getVendor().getName();
                    else thirdObjects[i][1] = "";
                    thirdObjects[i][2] = incomeProducts.get(i).getIncomeType();
                    if (incomeProducts.get(i).getInvoice() != null)
                        thirdObjects[i][3] = incomeProducts.get(i).getInvoice().getId();
                    else thirdObjects[i][3] = "";
                    thirdObjects[i][4] = incomeProducts.get(i).getIncomeDate();
                    thirdObjects[i][5] = incomeProducts.get(i).getCountValue();
                    thirdObjects[i][6] = incomeProducts.get(i).getCostValue() * incomeProducts.get(i).getCountValue();
                    thirdObjects[i][7] = incomeProducts.get(i).getDescription();
                }
                break;
            case 3:
                List<StockQueue> temp3 = databaseManager.getStockQueueForPeriod(fromDate, toDate).blockingGet();
                List<StockQueue> stockQueues = new ArrayList<>();
                for (int i = 0; i < temp3.size(); i++) {
                    if (thirdFilterConfig[0] == 1 && temp3.get(i).getIncomeProduct().getIncomeType() == IncomeProduct.INVOICE_PRODUCT) {
                        stockQueues.add(temp3.get(i));
                        continue;
                    }
                    if (thirdFilterConfig[1] == 1 && temp3.get(i).getIncomeProduct().getIncomeType() == IncomeProduct.SURPLUS_PRODUCT) {
                        stockQueues.add(temp3.get(i));
                        continue;
                    }
                    if (thirdFilterConfig[2] == 1 && temp3.get(i).getIncomeProduct().getIncomeType() == IncomeProduct.RETURNED_PRODUCT) {
                        stockQueues.add(temp3.get(i));
                        continue;
                    }
                }

                forthObjects = new Object[stockQueues.size()][10];
                for (int i = 0; i < stockQueues.size(); i++) {
                    forthObjects[i][0] = stockQueues.get(i).getId();
                    forthObjects[i][1] = ReportUtils.getProductName(stockQueues.get(i).getProduct());
                    if (stockQueues.get(i).getVendor() != null)
                        forthObjects[i][2] = stockQueues.get(i).getVendor().getName();
                    else forthObjects[i][2] = "";
                    forthObjects[i][3] = stockQueues.get(i).getIncomeProduct().getIncomeType();
                    if (stockQueues.get(i).getIncomeProduct().getIncomeType() == IncomeProduct.INVOICE_PRODUCT)
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
                List<StockQueue> stockQueuesForDetial = databaseManager.getStockQueueUsedForPeriod(fromDate, toDate).blockingGet();
                int size = 0;
                for (int i = 0; i < stockQueuesForDetial.size(); i++) {
                    for (int j = 0; j < stockQueuesForDetial.get(i).getDetialCounts().size(); j++) {
                        if (fifthFilterConfig[0] == 0 && stockQueuesForDetial.get(i).getDetialCounts().get(j).getOutcomeProduct().getOutcomeType() == IncomeProduct.INVOICE_PRODUCT) {
                            continue;
                        }
                        if (thirdFilterConfig[1] == 0 && stockQueuesForDetial.get(i).getDetialCounts().get(j).getOutcomeProduct().getOutcomeType() == IncomeProduct.SURPLUS_PRODUCT) {
                            continue;
                        }
                        if (thirdFilterConfig[2] == 0 && stockQueuesForDetial.get(i).getDetialCounts().get(j).getOutcomeProduct().getOutcomeType() == IncomeProduct.RETURNED_PRODUCT) {
                            continue;
                        }
                        size++;
                    }
                }
                int counter = 0;
                fifthObjects = new Object[size][8];
                for (int i = 0; i < stockQueuesForDetial.size(); i++) {
                    for (int j = 0; j < stockQueuesForDetial.get(i).getDetialCounts().size(); j++) {

                        if (fifthFilterConfig[0] == 0 && stockQueuesForDetial.get(i).getDetialCounts().get(j).getOutcomeProduct().getOutcomeType() == IncomeProduct.INVOICE_PRODUCT) {
                            continue;
                        }
                        if (thirdFilterConfig[1] == 0 && stockQueuesForDetial.get(i).getDetialCounts().get(j).getOutcomeProduct().getOutcomeType() == IncomeProduct.SURPLUS_PRODUCT) {
                            continue;
                        }
                        if (thirdFilterConfig[2] == 0 && stockQueuesForDetial.get(i).getDetialCounts().get(j).getOutcomeProduct().getOutcomeType() == IncomeProduct.RETURNED_PRODUCT) {
                            continue;
                        }

                        fifthObjects[counter][0] = stockQueuesForDetial.get(i).getId();
                        fifthObjects[counter][1] = ReportUtils.getProductName(stockQueuesForDetial.get(i).getProduct());
                        fifthObjects[counter][2] = stockQueuesForDetial.get(i).getDetialCounts().get(j).getOutcomeProduct().getOutcomeType();
                        if (stockQueuesForDetial.get(i).getDetialCounts().get(j).getOutcomeProduct().getOutcomeType() == OutcomeProduct.ORDER_SALES)
                            fifthObjects[counter][3] = stockQueuesForDetial.get(i).getDetialCounts().get(j).getOutcomeProduct().getOrderProduct().getOrderId();
                        else fifthObjects[counter][3] = "";
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
        this.searchText = searchText;
        if (searchText.isEmpty()) {
            switch (currentPosition) {
                case 0:
                    view.updateTable(firstObjects, currentPosition);
                    break;
                case 1:
                    view.updateTable(secondObjects, currentPosition);
                    break;
                case 2:
                    view.updateTable(thirdObjects, currentPosition);
                    break;
                case 3:
                    view.updateTable(forthObjects, currentPosition);
                    break;
            }
            prev = -1;

        } else {
            switch (currentPosition) {
                case 0:
                    if (searchText.length() <= prev || prev == -1) {
                        int searchRes[] = new int[firstObjects.length];
                        for (int i = 0; i < firstObjects.length; i++) {
                            if (((String) firstObjects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) firstObjects[i][1])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) firstObjects[i][2])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) firstObjects[i][3])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) firstObjects[i][4])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) firstObjects[i][5])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) firstObjects[i][6])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) firstObjects[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < firstObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][8];

                        int pt = 0;
                        for (int i = 0; i < firstObjects.length; i++) {
                            if (searchRes[i] == 1) {
                                objectResults[pt] = firstObjects[i];
                                pt++;
                            }
                        }
                        searchResultsTemp = objectResults.clone();
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    } else {
                        int searchRes[] = new int[searchResultsTemp.length];
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (((String) searchResultsTemp[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][1])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][2])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][3])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][4])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][5])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][6])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }
                        int sumSize = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][8];

                        int pt = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1) {
                                objectResults[pt] = searchResultsTemp[i];
                                pt++;
                            }
                        }
                        searchResultsTemp = objectResults.clone();
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    }
                    prev = searchText.length();
                    break;
                case 1:
                    if (searchText.length() <= prev || prev == -1) {
                        int searchRes[] = new int[secondObjects.length];
                        for (int i = 0; i < secondObjects.length; i++) {
                            if (((String) secondObjects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int orderStatus = (int) secondObjects[i][1];
                            if (orderStatus == OutcomeProduct.ORDER_SALES) {
                                text = context.getString(R.string.order_rep);
                            } else if (orderStatus == OutcomeProduct.OUTVOICE_TO_VENDOR) {
                                text = context.getString(R.string.outvoice_rep);
                            } else if (orderStatus == OutcomeProduct.WASTE) {
                                text = context.getString(R.string.waste_rep);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((long) secondObjects[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }

                            if (((String) secondObjects[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) secondObjects[i][4])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }

                            if ((decimalFormat.format((double) secondObjects[i][5])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) secondObjects[i][6])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) secondObjects[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < secondObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][8];

                        int pt = 0;
                        for (int i = 0; i < secondObjects.length; i++) {
                            if (searchRes[i] == 1) {
                                objectResults[pt] = secondObjects[i];
                                pt++;
                            }
                        }
                        searchResultsTemp = objectResults.clone();
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    } else {
                        int searchRes[] = new int[searchResultsTemp.length];
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (((String) searchResultsTemp[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int orderStatus = (int) searchResultsTemp[i][1];
                            if (orderStatus == OutcomeProduct.ORDER_SALES) {
                                text = context.getString(R.string.order_rep);
                            } else if (orderStatus == OutcomeProduct.OUTVOICE_TO_VENDOR) {
                                text = context.getString(R.string.outvoice_rep);
                            } else if (orderStatus == OutcomeProduct.WASTE) {
                                text = context.getString(R.string.waste_rep);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((long) searchResultsTemp[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }

                            if (((String) searchResultsTemp[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][4])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }

                            if ((decimalFormat.format((double) searchResultsTemp[i][5])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][6])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }
                        int sumSize = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][8];

                        int pt = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1) {
                                objectResults[pt] = searchResultsTemp[i];
                                pt++;
                            }
                        }
                        searchResultsTemp = objectResults.clone();
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    }
                    prev = searchText.length();
                    break;
                case 2:
                    if (searchText.length() <= prev || prev == -1) {
                        int searchRes[] = new int[thirdObjects.length];
                        for (int i = 0; i < thirdObjects.length; i++) {
                            if (((String) thirdObjects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) thirdObjects[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int orderStatus = (int) thirdObjects[i][2];
                            if (orderStatus == IncomeProduct.INVOICE_PRODUCT) {
                                text = context.getString(R.string.invoice_rep);
                            } else if (orderStatus == IncomeProduct.SURPLUS_PRODUCT) {
                                text = context.getString(R.string.surplus_rep);
                            } else if (orderStatus == IncomeProduct.RETURNED_PRODUCT) {
                                text = context.getString(R.string.customer_rep);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((long) thirdObjects[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) thirdObjects[i][4])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) thirdObjects[i][5])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) thirdObjects[i][6])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) thirdObjects[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }

                        }

                        int sumSize = 0;
                        for (int i = 0; i < thirdObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][8];

                        int pt = 0;
                        for (int i = 0; i < thirdObjects.length; i++) {
                            if (searchRes[i] == 1) {
                                objectResults[pt] = thirdObjects[i];
                                pt++;
                            }
                        }
                        searchResultsTemp = objectResults.clone();
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    } else {
                        int searchRes[] = new int[searchResultsTemp.length];
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (((String) searchResultsTemp[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int orderStatus = (int) searchResultsTemp[i][2];
                            if (orderStatus == IncomeProduct.INVOICE_PRODUCT) {
                                text = context.getString(R.string.invoice_rep);
                            } else if (orderStatus == IncomeProduct.SURPLUS_PRODUCT) {
                                text = context.getString(R.string.surplus_rep);
                            } else if (orderStatus == IncomeProduct.RETURNED_PRODUCT) {
                                text = context.getString(R.string.customer_rep);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((long) searchResultsTemp[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][4])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][5])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][6])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }
                        int sumSize = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][8];

                        int pt = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1) {
                                objectResults[pt] = searchResultsTemp[i];
                                pt++;
                            }
                        }
                        searchResultsTemp = objectResults.clone();
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    }
                    prev = searchText.length();
                    break;
                case 3:
                    if (searchText.length() <= prev || prev == -1) {
                        int searchRes[] = new int[forthObjects.length];
                        for (int i = 0; i < forthObjects.length; i++) {
                            if (String.valueOf((long) forthObjects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) forthObjects[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) forthObjects[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }

                            String text = "";
                            int orderStatus = (int) forthObjects[i][3];
                            if (orderStatus == IncomeProduct.INVOICE_PRODUCT) {
                                text = context.getString(R.string.invoice_rep);
                            } else if (orderStatus == IncomeProduct.SURPLUS_PRODUCT) {
                                text = context.getString(R.string.surplus_rep);
                            } else if (orderStatus == IncomeProduct.RETURNED_PRODUCT) {
                                text = context.getString(R.string.customer_rep);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((long) forthObjects[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) forthObjects[i][5])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) forthObjects[i][6])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) forthObjects[i][7])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) forthObjects[i][8])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) forthObjects[i][9]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }

                        }

                        int sumSize = 0;
                        for (int i = 0; i < forthObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][10];

                        int pt = 0;
                        for (int i = 0; i < forthObjects.length; i++) {
                            if (searchRes[i] == 1) {
                                objectResults[pt] = forthObjects[i];
                                pt++;
                            }
                        }
                        searchResultsTemp = objectResults.clone();
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    } else {
                        int searchRes[] = new int[searchResultsTemp.length];
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (String.valueOf((long) searchResultsTemp[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
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

                            String text = "";
                            int orderStatus = (int) searchResultsTemp[i][3];
                            if (orderStatus == IncomeProduct.INVOICE_PRODUCT) {
                                text = context.getString(R.string.invoice_rep);
                            } else if (orderStatus == IncomeProduct.SURPLUS_PRODUCT) {
                                text = context.getString(R.string.surplus_rep);
                            } else if (orderStatus == IncomeProduct.RETURNED_PRODUCT) {
                                text = context.getString(R.string.customer_rep);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((long) searchResultsTemp[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][5])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][6])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][7])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][8])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][9]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
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
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    }
                    prev = searchText.length();
                    break;
                case 4:
                    if (searchText.length() <= prev || prev == -1) {
                        int searchRes[] = new int[fifthObjects.length];
                        for (int i = 0; i < fifthObjects.length; i++) {
                            if (String.valueOf((long) fifthObjects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) fifthObjects[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int orderStatus = (int) fifthObjects[i][2];
                            if (orderStatus == IncomeProduct.INVOICE_PRODUCT) {
                                text = context.getString(R.string.invoice_rep);
                            } else if (orderStatus == IncomeProduct.SURPLUS_PRODUCT) {
                                text = context.getString(R.string.surplus_rep);
                            } else if (orderStatus == IncomeProduct.RETURNED_PRODUCT) {
                                text = context.getString(R.string.customer_rep);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((long) fifthObjects[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) fifthObjects[i][4])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) fifthObjects[i][5])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) fifthObjects[i][6])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) fifthObjects[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < fifthObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][8];

                        int pt = 0;
                        for (int i = 0; i < fifthObjects.length; i++) {
                            if (searchRes[i] == 1) {
                                objectResults[pt] = fifthObjects[i];
                                pt++;
                            }
                        }
                        searchResultsTemp = objectResults.clone();
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    } else {
                        int searchRes[] = new int[searchResultsTemp.length];
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (String.valueOf((long) searchResultsTemp[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int orderStatus = (int) searchResultsTemp[i][2];
                            if (orderStatus == IncomeProduct.INVOICE_PRODUCT) {
                                text = context.getString(R.string.invoice_rep);
                            } else if (orderStatus == IncomeProduct.SURPLUS_PRODUCT) {
                                text = context.getString(R.string.surplus_rep);
                            } else if (orderStatus == IncomeProduct.RETURNED_PRODUCT) {
                                text = context.getString(R.string.customer_rep);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((long) searchResultsTemp[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][4])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][5])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][6])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }
                        int sumSize = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][8];

                        int pt = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1) {
                                objectResults[pt] = searchResultsTemp[i];
                                pt++;
                            }
                        }
                        searchResultsTemp = objectResults.clone();
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    }
                    prev = searchText.length();
                    break;
            }
        }
    }

    @Override
    public void onClickedDateInterval() {
        view.openDateInterval(fromDate, toDate);
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
        switch (currentPosition) {
            case 0:
                new Exception("Some think Wrong !!! For Operation Summary disable filter!!!").printStackTrace();
                break;
            case 1:
                view.showFilterDialog(secondFilterConfig, currentPosition);
                break;
            case 2:
                view.showFilterDialog(thirdFilterConfig, currentPosition);
                break;
            case 3:
                view.showFilterDialog(forthFilterConfig, currentPosition);
                break;
            case 4:
                view.showFilterDialog(fifthFilterConfig, currentPosition);
                break;
        }
    }

    @Override
    public void onChoisedPanel(int postion) {
        this.currentPosition = postion;
        prev = -1;
        searchResultsTemp = null;
        updateTable();
    }

    @Override
    public void onTillPickerClicked() {

    }


    @Override
    public void onAction(Object[][] objects, int row, int column) {

    }

    @Override
    public void filterConfigsChanged(int[] configs) {
        switch (currentPosition) {
            case 0:
                new Exception("Some think Wrong !!! For Operation Summary disable filter!!!").printStackTrace();
                break;
            case 1:
                secondFilterConfig = configs;
                break;
            case 2:
                thirdFilterConfig = configs;
                break;
            case 3:
                forthFilterConfig = configs;
                break;
            case 4:
                fifthFilterConfig = configs;
                break;
        }
        updateTable();
    }

    @Override
    public void exportPdfToUSB(String fileName, UsbFile root) {
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        String filter = "";
        switch (currentPosition) {
            case 0:
                if (searchResultsTemp != null) {
                    view.exportTableToPdfUSB(fileName, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdfUSB(fileName, root, firstObjects, currentPosition, date, filter, searchText);
                break;
            case 1:
                StringBuilder filters = new StringBuilder();
                if (secondFilterConfig[0] == 1) {
                    filters.append(context.getString(R.string.order_rep)).append(" ");
                }
                if (secondFilterConfig[1] == 1) {
                    filters.append(context.getString(R.string.outvoice_rep)).append(" ");
                }
                if (secondFilterConfig[2] == 1) {
                    filters.append(context.getString(R.string.waste_rep)).append(" ");
                }
                filter = filters.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToPdfUSB(fileName, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdfUSB(fileName, root, secondObjects, currentPosition, date, filter, searchText);
                break;
            case 2:
                StringBuilder filters1 = new StringBuilder();
                if (thirdFilterConfig[0] == 1) {
                    filters1.append(context.getString(R.string.invoice_rep)).append(" ");
                }
                if (thirdFilterConfig[1] == 1) {
                    filters1.append(context.getString(R.string.surplus_rep)).append(" ");
                }
                if (thirdFilterConfig[2] == 1) {
                    filters1.append(context.getString(R.string.customer_rep)).append(" ");
                }
                filter = filters1.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToPdfUSB(fileName, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdfUSB(fileName, root, thirdObjects, currentPosition, date, filter, searchText);
                break;
            case 3:
                StringBuilder filters2 = new StringBuilder();
                if (forthFilterConfig[0] == 1) {
                    filters2.append(context.getString(R.string.invoice_rep)).append(" ");
                }
                if (forthFilterConfig[1] == 1) {
                    filters2.append(context.getString(R.string.surplus_rep)).append(" ");
                }
                if (forthFilterConfig[2] == 1) {
                    filters2.append(context.getString(R.string.customer_rep)).append(" ");
                }
                filter = filters2.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToPdfUSB(fileName, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdfUSB(fileName, root, forthObjects, currentPosition, date, filter, searchText);
                break;
            case 4:
                StringBuilder filters3 = new StringBuilder();
                if (fifthFilterConfig[0] == 1) {
                    filters3.append(context.getString(R.string.order_rep)).append(" ");
                }
                if (fifthFilterConfig[1] == 1) {
                    filters3.append(context.getString(R.string.outvoice_rep)).append(" ");
                }
                if (fifthFilterConfig[2] == 1) {
                    filters3.append(context.getString(R.string.waste_rep)).append(" ");
                }
                filter = filters3.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToPdfUSB(fileName, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdfUSB(fileName, root, fifthObjects, currentPosition, date, filter, searchText);
                break;
        }
    }

    @Override
    public void exportExcelToUSB(String fileName, UsbFile root) {
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        String filter = "";
        switch (currentPosition) {
            case 0:
                if (searchResultsTemp != null) {
                    view.exportTableToExcelUSB(fileName, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToExcelUSB(fileName, root, firstObjects, currentPosition, date, filter, searchText);
                break;
            case 1:
                StringBuilder filters = new StringBuilder();
                if (secondFilterConfig[0] == 1) {
                    filters.append(context.getString(R.string.order_rep)).append(" ");
                }
                if (secondFilterConfig[1] == 1) {
                    filters.append(context.getString(R.string.outvoice_rep)).append(" ");
                }
                if (secondFilterConfig[2] == 1) {
                    filters.append(context.getString(R.string.waste_rep)).append(" ");
                }
                filter = filters.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToExcelUSB(fileName, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToExcelUSB(fileName, root, secondObjects, currentPosition, date, filter, searchText);
                break;
            case 2:
                StringBuilder filters1 = new StringBuilder();
                if (thirdFilterConfig[0] == 1) {
                    filters1.append(context.getString(R.string.invoice_rep)).append(" ");
                }
                if (thirdFilterConfig[1] == 1) {
                    filters1.append(context.getString(R.string.surplus_rep)).append(" ");
                }
                if (thirdFilterConfig[2] == 1) {
                    filters1.append(context.getString(R.string.customer_rep)).append(" ");
                }
                filter = filters1.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToExcelUSB(fileName, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToExcelUSB(fileName, root, thirdObjects, currentPosition, date, filter, searchText);
                break;
            case 3:
                StringBuilder filters2 = new StringBuilder();
                if (forthFilterConfig[0] == 1) {
                    filters2.append(context.getString(R.string.invoice_rep)).append(" ");
                }
                if (forthFilterConfig[1] == 1) {
                    filters2.append(context.getString(R.string.surplus_rep)).append(" ");
                }
                if (forthFilterConfig[2] == 1) {
                    filters2.append(context.getString(R.string.customer_rep)).append(" ");
                }
                filter = filters2.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToExcelUSB(fileName, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToExcelUSB(fileName, root, forthObjects, currentPosition, date, filter, searchText);
                break;
            case 4:
                StringBuilder filters3 = new StringBuilder();
                if (fifthFilterConfig[0] == 1) {
                    filters3.append(context.getString(R.string.order_rep)).append(" ");
                }
                if (fifthFilterConfig[1] == 1) {
                    filters3.append(context.getString(R.string.outvoice_rep)).append(" ");
                }
                if (fifthFilterConfig[2] == 1) {
                    filters3.append(context.getString(R.string.waste_rep)).append(" ");
                }
                filter = filters3.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToExcelUSB(fileName, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToExcelUSB(fileName, root, fifthObjects, currentPosition, date, filter, searchText);
                break;

        }
    }

    @Override
    public void exportExcel(String fileName, String path) {
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        String filter = "";
        switch (currentPosition) {
            case 0:
                if (searchResultsTemp != null) {
                    view.exportTableToExcel(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToExcel(fileName, path, firstObjects, currentPosition, date, filter, searchText);
                break;
            case 1:
                StringBuilder filters = new StringBuilder();
                if (secondFilterConfig[0] == 1) {
                    filters.append(context.getString(R.string.order_rep)).append(" ");
                }
                if (secondFilterConfig[1] == 1) {
                    filters.append(context.getString(R.string.outvoice_rep)).append(" ");
                }
                if (secondFilterConfig[2] == 1) {
                    filters.append(context.getString(R.string.waste_rep)).append(" ");
                }
                filter = filters.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToExcel(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToExcel(fileName, path, secondObjects, currentPosition, date, filter, searchText);
                break;
            case 2:
                StringBuilder filters1 = new StringBuilder();
                if (thirdFilterConfig[0] == 1) {
                    filters1.append(context.getString(R.string.invoice_rep)).append(" ");
                }
                if (thirdFilterConfig[1] == 1) {
                    filters1.append(context.getString(R.string.surplus_rep)).append(" ");
                }
                if (thirdFilterConfig[2] == 1) {
                    filters1.append(context.getString(R.string.customer_rep)).append(" ");
                }
                filter = filters1.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToExcel(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToExcel(fileName, path, thirdObjects, currentPosition, date, filter, searchText);
                break;
            case 3:
                StringBuilder filters2 = new StringBuilder();
                if (forthFilterConfig[0] == 1) {
                    filters2.append(context.getString(R.string.invoice_rep)).append(" ");
                }
                if (forthFilterConfig[1] == 1) {
                    filters2.append(context.getString(R.string.surplus_rep)).append(" ");
                }
                if (forthFilterConfig[2] == 1) {
                    filters2.append(context.getString(R.string.customer_rep)).append(" ");
                }
                filter = filters2.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToExcel(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToExcel(fileName, path, forthObjects, currentPosition, date, filter, searchText);
                break;
            case 4:
                StringBuilder filters3 = new StringBuilder();
                if (fifthFilterConfig[0] == 1) {
                    filters3.append(context.getString(R.string.order_rep)).append(" ");
                }
                if (fifthFilterConfig[1] == 1) {
                    filters3.append(context.getString(R.string.outvoice_rep)).append(" ");
                }
                if (fifthFilterConfig[2] == 1) {
                    filters3.append(context.getString(R.string.waste_rep)).append(" ");
                }
                filter = filters3.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToExcel(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToExcel(fileName, path, fifthObjects, currentPosition, date, filter, searchText);
                break;

        }
    }

    @Override
    public void exportPdf(String fileName, String path) {
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        String filter = "";
        switch (currentPosition) {
            case 0:
                if (searchResultsTemp != null) {
                    view.exportTableToPdf(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdf(fileName, path, firstObjects, currentPosition, date, filter, searchText);
                break;
            case 1:
                StringBuilder filters = new StringBuilder();
                if (secondFilterConfig[0] == 1) {
                    filters.append(context.getString(R.string.order_rep)).append(" ");
                }
                if (secondFilterConfig[1] == 1) {
                    filters.append(context.getString(R.string.outvoice_rep)).append(" ");
                }
                if (secondFilterConfig[2] == 1) {
                    filters.append(context.getString(R.string.waste_rep)).append(" ");
                }
                filter = filters.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToPdf(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdf(fileName, path, secondObjects, currentPosition, date, filter, searchText);
                break;
            case 2:
                StringBuilder filters1 = new StringBuilder();
                if (thirdFilterConfig[0] == 1) {
                    filters1.append(context.getString(R.string.invoice_rep)).append(" ");
                }
                if (thirdFilterConfig[1] == 1) {
                    filters1.append(context.getString(R.string.surplus_rep)).append(" ");
                }
                if (thirdFilterConfig[2] == 1) {
                    filters1.append(context.getString(R.string.customer_rep)).append(" ");
                }
                filter = filters1.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToPdf(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdf(fileName, path, thirdObjects, currentPosition, date, filter, searchText);
                break;
            case 3:
                StringBuilder filters2 = new StringBuilder();
                if (forthFilterConfig[0] == 1) {
                    filters2.append(context.getString(R.string.invoice_rep)).append(" ");
                }
                if (forthFilterConfig[1] == 1) {
                    filters2.append(context.getString(R.string.surplus_rep)).append(" ");
                }
                if (forthFilterConfig[2] == 1) {
                    filters2.append(context.getString(R.string.customer_rep)).append(" ");
                }
                filter = filters2.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToPdf(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdf(fileName, path, forthObjects, currentPosition, date, filter, searchText);
                break;
            case 4:
                StringBuilder filters3 = new StringBuilder();
                if (fifthFilterConfig[0] == 1) {
                    filters3.append(context.getString(R.string.order_rep)).append(" ");
                }
                if (fifthFilterConfig[1] == 1) {
                    filters3.append(context.getString(R.string.outvoice_rep)).append(" ");
                }
                if (fifthFilterConfig[2] == 1) {
                    filters3.append(context.getString(R.string.waste_rep)).append(" ");
                }
                filter = filters3.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToPdf(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdf(fileName, path, fifthObjects, currentPosition, date, filter, searchText);
                break;
        }
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
