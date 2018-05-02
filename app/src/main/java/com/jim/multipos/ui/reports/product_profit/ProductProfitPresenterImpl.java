package com.jim.multipos.ui.reports.product_profit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.reports.product_profit.data.ProductLog;
import com.jim.multipos.ui.reports.product_profit.data.ProfitData;
import com.jim.multipos.utils.ReportUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.utils.ExportUtils.EXCEL;
import static com.jim.multipos.utils.ExportUtils.PDF;

public class ProductProfitPresenterImpl extends BasePresenterImpl<ProductProfitView> implements ProductProfitPresenter {
    Calendar fromDateSummary, toDateSummary;
    Calendar fromDateLog, toDateLog;
    int currentPosition = 0;
    private DatabaseManager databaseManager;
    private Context context;
    List<Order> ordersSummary;
    List<Order> ordersLog;
    DecimalFormat decimalFormat;
    SimpleDateFormat simpleDateFormat;

    int filterType = 0;
    public static final int FILTER_BY_PRODUCT = 0;
    public static final int FILTER_BY_CATEGORY = 1;
    public static final int FILTER_BY_SUBCATEGORY = 2;
    public static final int FILTER_BY_PRODUCT_CLASS = 3;

    Object[][] summaryObjects;
    Object[][] logObjects;
    private String searchText;

    @Inject
    protected ProductProfitPresenterImpl(ProductProfitView productProfitView, DatabaseManager databaseManager, Context context) {
        super(productProfitView);
        this.databaseManager = databaseManager;
        this.context = context;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        initDateInterval();
        initDecimal();
        view.updateDateIntervalUi(fromDateSummary,toDateSummary);

        new Handler().postDelayed(()->{
            databaseManager.getOrdersInIntervalForReport(this.fromDateSummary,this.toDateSummary).subscribe((orders1, throwable) -> {
                ordersSummary = orders1;
                ordersLog = orders1;
                updateObejctsForTable();
                view.initTable(summaryObjects,currentPosition);
            });
        },50);

    }

    @Override
    public void onChooseDateInterval(Calendar fromDate, Calendar toDate) {
        if (currentPosition == 0) {
            this.fromDateSummary = fromDate;
            this.toDateSummary = toDate;
        } else {
            this.fromDateLog = fromDate;
            this.toDateLog = toDate;
        }
        databaseManager.getOrdersInIntervalForReport(fromDate, toDate).subscribe((orders1, throwable) -> {
            if (currentPosition == 0) ordersSummary = orders1;
            else ordersLog = orders1;
            updateObejctsForTable();
            view.initTable(currentPosition == 0 ? summaryObjects : logObjects, currentPosition);
            view.updateDateIntervalUi(currentPosition == 0 ? fromDateSummary : fromDateLog, currentPosition == 0 ? toDateSummary : toDateLog);
        });
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
                        if (decimalFormat.format((double) objects[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }

                        if (decimalFormat.format((double) objects[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) objects[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }

                        if (decimalFormat.format((double) objects[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) objects[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
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
                        if (simpleDateFormat.format(new Date((long) objects[i][1])).contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (String.valueOf((long) objects[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (String.valueOf((long) objects[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) objects[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) objects[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) objects[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) objects[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (((String) objects[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
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
                Object[][] objectResults = new Object[sumSize][9];

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
                        if (decimalFormat.format((double) searchResultsTemp[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }

                        if (decimalFormat.format((double) searchResultsTemp[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) searchResultsTemp[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }

                        if (decimalFormat.format((double) searchResultsTemp[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) searchResultsTemp[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
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
                        if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][1])).contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (String.valueOf((long) searchResultsTemp[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (String.valueOf((long) searchResultsTemp[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) searchResultsTemp[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) searchResultsTemp[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) searchResultsTemp[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (decimalFormat.format((double) searchResultsTemp[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
                            searchRes[i] = 1;
                            continue;
                        }
                        if (((String) searchResultsTemp[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
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
                Object[][] objectResults = new Object[sumSize][9];

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
        if (currentPosition == 0) {
            view.showFilterPanel(filterType);
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
            if (logObjects == null)
                updateObejctsForTable();
            view.updateTable(logObjects, currentPosition);
            view.updateDateIntervalUi(fromDateLog, toDateLog);
        }
    }

    @Override
    public void onTillPickerClicked() {

    }

    private void updateObejctsForTable() {
        if (currentPosition == 0) {
            HashMap<Long, ProfitData> profitDatas = new HashMap<>();
            List<Order> orders = ordersSummary;
            switch (filterType) {
                case FILTER_BY_PRODUCT:
                    for (int i = 0; i < orders.size(); i++) {
                        if (orders.get(i).getStatus() == Order.CLOSED_ORDER) {
                            for (int j = 0; j < orders.get(i).getOrderProducts().size(); j++) {
                                ProfitData profitData = profitDatas.get(orders.get(i).getOrderProducts().get(j).getProduct().getId());
                                if (profitData == null) {
                                    profitData = new ProfitData();
                                    profitData.setId(orders.get(i).getOrderProducts().get(j).getProduct().getId());
                                    profitData.setName(ReportUtils.getProductName(orders.get(i).getOrderProducts().get(j).getProduct()));
                                }
                                profitData.plusCount(orders.get(i).getOrderProducts().get(j).getCount());
                                profitData.plusCost(orders.get(i).getOrderProducts().get(j).getCount() * orders.get(i).getOrderProducts().get(j).getCost());
                                profitData.plusSale(orders.get(i).getOrderProducts().get(j).getCount() * orders.get(i).getOrderProducts().get(j).getPrice());
                                profitData.plusDiscount(orders.get(i).getOrderProducts().get(j).getDiscountAmount());
                                profitData.plusServiceFee(orders.get(i).getOrderProducts().get(j).getServiceAmount());
                                profitDatas.put(profitData.getId(), profitData);
                            }
                        }
                    }
                    break;
                case FILTER_BY_CATEGORY:
                    for (int i = 0; i < orders.size(); i++) {
                        if (orders.get(i).getStatus() == Order.CLOSED_ORDER) {
                            for (int j = 0; j < orders.get(i).getOrderProducts().size(); j++) {
                                ProfitData profitData = profitDatas.get(orders.get(i).getOrderProducts().get(j).getProduct().getCategory().getParentId());
                                if (profitData == null) {
                                    profitData = new ProfitData();
                                    profitData.setId(orders.get(i).getOrderProducts().get(j).getProduct().getCategory().getParentId());
                                    profitData.setName(orders.get(i).getOrderProducts().get(j).getProduct().getCategory().getParentCategory().getName());
                                }
                                profitData.plusCount(orders.get(i).getOrderProducts().get(j).getCount());
                                profitData.plusCost(orders.get(i).getOrderProducts().get(j).getCount() * orders.get(i).getOrderProducts().get(j).getCost());
                                profitData.plusSale(orders.get(i).getOrderProducts().get(j).getCount() * orders.get(i).getOrderProducts().get(j).getPrice());
                                profitData.plusDiscount(orders.get(i).getOrderProducts().get(j).getDiscountAmount());
                                profitData.plusServiceFee(orders.get(i).getOrderProducts().get(j).getServiceAmount());
                                profitDatas.put(profitData.getId(), profitData);
                            }
                        }
                    }
                    break;
                case FILTER_BY_SUBCATEGORY:
                    for (int i = 0; i < orders.size(); i++) {
                        if (orders.get(i).getStatus() == Order.CLOSED_ORDER) {
                            for (int j = 0; j < orders.get(i).getOrderProducts().size(); j++) {
                                ProfitData profitData = profitDatas.get(orders.get(i).getOrderProducts().get(j).getProduct().getCategory().getId());
                                if (profitData == null) {
                                    profitData = new ProfitData();
                                    profitData.setId(orders.get(i).getOrderProducts().get(j).getProduct().getCategory().getId());
                                    profitData.setName(orders.get(i).getOrderProducts().get(j).getProduct().getCategory().getName());
                                }
                                profitData.plusCount(orders.get(i).getOrderProducts().get(j).getCount());
                                profitData.plusCost(orders.get(i).getOrderProducts().get(j).getCount() * orders.get(i).getOrderProducts().get(j).getCost());
                                profitData.plusSale(orders.get(i).getOrderProducts().get(j).getCount() * orders.get(i).getOrderProducts().get(j).getPrice());
                                profitData.plusDiscount(orders.get(i).getOrderProducts().get(j).getDiscountAmount());
                                profitData.plusServiceFee(orders.get(i).getOrderProducts().get(j).getServiceAmount());
                                profitDatas.put(profitData.getId(), profitData);
                            }
                        }
                    }
                    break;
                case FILTER_BY_PRODUCT_CLASS:
                    ProfitData profitDataDefault = new ProfitData();
                    profitDataDefault.setId(-1);
                    profitDataDefault.setName(context.getString(R.string.un_classed));
                    profitDatas.put(-1l, profitDataDefault);
                    for (int i = 0; i < orders.size(); i++) {
                        if (orders.get(i).getStatus() == Order.CLOSED_ORDER) {
                            for (int j = 0; j < orders.get(i).getOrderProducts().size(); j++) {
                                if (orders.get(i).getOrderProducts().get(j).getProduct().getProductClass() == null) {
                                    ProfitData profitData = profitDatas.get(-1l);
                                    profitData.plusCount(orders.get(i).getOrderProducts().get(j).getCount());
                                    profitData.plusCost(orders.get(i).getOrderProducts().get(j).getCount() * orders.get(i).getOrderProducts().get(j).getCost());
                                    profitData.plusSale(orders.get(i).getOrderProducts().get(j).getCount() * orders.get(i).getOrderProducts().get(j).getPrice());
                                    profitData.plusDiscount(orders.get(i).getOrderProducts().get(j).getDiscountAmount());
                                    profitData.plusServiceFee(orders.get(i).getOrderProducts().get(j).getServiceAmount());
                                    profitDatas.put(profitData.getId(), profitData);
                                } else {
                                    ProfitData profitData = profitDatas.get(orders.get(i).getOrderProducts().get(j).getProduct().getProductClass().getId());
                                    if (profitData == null) {
                                        profitData = new ProfitData();
                                        profitData.setId(orders.get(i).getOrderProducts().get(j).getProduct().getProductClass().getId());
                                        profitData.setName(orders.get(i).getOrderProducts().get(j).getProduct().getProductClass().getName());
                                    }
                                    profitData.plusCount(orders.get(i).getOrderProducts().get(j).getCount());
                                    profitData.plusCost(orders.get(i).getOrderProducts().get(j).getCount() * orders.get(i).getOrderProducts().get(j).getCost());
                                    profitData.plusSale(orders.get(i).getOrderProducts().get(j).getCount() * orders.get(i).getOrderProducts().get(j).getPrice());
                                    profitData.plusDiscount(orders.get(i).getOrderProducts().get(j).getDiscountAmount());
                                    profitData.plusServiceFee(orders.get(i).getOrderProducts().get(j).getServiceAmount());
                                    profitDatas.put(profitData.getId(), profitData);
                                }
                            }
                        }
                    }
                    break;
            }

            summaryObjects = new Object[profitDatas.size()][8];
            int counter = 0;
            for (ProfitData profitData : profitDatas.values()) {
                summaryObjects[counter][0] = profitData.getName();
                summaryObjects[counter][1] = profitData.getCount();
                summaryObjects[counter][2] = profitData.getTotalCost();
                summaryObjects[counter][3] = profitData.getTotalSale();
                summaryObjects[counter][4] = profitData.getNET();
                summaryObjects[counter][5] = profitData.getProfit();
                summaryObjects[counter][6] = profitData.getTotalDiscount();
                summaryObjects[counter][7] = profitData.getTotalServiceFee();
                counter++;
            }
        } else {
            List<ProductLog> productLogs = new ArrayList<>();
            List<Order> orders = ordersLog;

            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getStatus() == Order.CLOSED_ORDER) {
                    for (int j = 0; j < orders.get(i).getOrderProducts().size(); j++) {

                        ProductLog productLog = new ProductLog();
                        productLog.setName(ReportUtils.getProductName(orders.get(i).getOrderProducts().get(j).getProduct()));
                        productLog.setQty(orders.get(i).getOrderProducts().get(j).getCount());
                        productLog.setCostEach(orders.get(i).getOrderProducts().get(j).getCost());
                        productLog.setPriceEach(orders.get(i).getOrderProducts().get(j).getPrice());
                        productLog.setDate(orders.get(i).getCreateAt());
                        productLog.setDiscription(orders.get(i).getOrderProducts().get(j).getDiscription());
                        productLog.setOrderId(orders.get(i).getId());
                        productLogs.add(productLog);

                    }
                }
            }

            logObjects = new Object[productLogs.size()][8];
            for (int i = 0; i < productLogs.size(); i++) {
                logObjects[i][0] = productLogs.get(i).getName();
                logObjects[i][1] = productLogs.get(i).getDate();
                logObjects[i][2] = productLogs.get(i).getOrderId();
                logObjects[i][3] = productLogs.get(i).getQty();
                logObjects[i][4] = productLogs.get(i).getCostEach();
                logObjects[i][5] = productLogs.get(i).getPriceEach();
                logObjects[i][6] = productLogs.get(i).getTotal();
                logObjects[i][7] = productLogs.get(i).getDiscription() == null ? "" : productLogs.get(i).getDiscription();
            }

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
    public void filterConfigsChanged(int configs) {
        if (configs == filterType) return;
        filterType = configs;
        updateObejctsForTable();
        view.initTable(currentPosition == 0 ? summaryObjects : logObjects, currentPosition);

    }

    @Override
    public void exportExcel(String fileName, String path) {
        String filter = "";
        switch (currentPosition) {
            case 0:
                if (filterType == 1) {
                    filter = context.getString(R.string.product);
                } else if (filterType == 2) {
                    filter = context.getString(R.string.category);
                } else if (filterType == 3) {
                    filter = context.getString(R.string.subcategory);
                } else if (filterType == 4) {
                    filter = context.getString(R.string.product_class);
                }
                String date = simpleDateFormat.format(fromDateSummary.getTime()) + " - " + simpleDateFormat.format(toDateSummary.getTime());
                view.exportTableToExcel(fileName, path, summaryObjects, currentPosition, date, filter, searchText);
                break;
            case 1:
                String date1 = simpleDateFormat.format(fromDateLog.getTime()) + " - " + simpleDateFormat.format(toDateLog.getTime());
                view.exportTableToExcel(fileName, path, logObjects, currentPosition, date1, filter, searchText);
                break;
        }
    }

    @Override
    public void exportPdf(String fileName, String path) {
        String filter = "";
        switch (currentPosition) {
            case 0:
                if (filterType == 1) {
                    filter = context.getString(R.string.product);
                } else if (filterType == 2) {
                    filter = context.getString(R.string.category);
                } else if (filterType == 3) {
                    filter = context.getString(R.string.subcategory);
                } else if (filterType == 4) {
                    filter = context.getString(R.string.product_class);
                }
                String date = simpleDateFormat.format(fromDateSummary.getTime()) + " - " + simpleDateFormat.format(toDateSummary.getTime());
                view.exportTableToPdf(fileName, path, summaryObjects, currentPosition, date, filter, searchText);
                break;
            case 1:
                String date1 = simpleDateFormat.format(fromDateLog.getTime()) + " - " + simpleDateFormat.format(toDateLog.getTime());
                view.exportTableToPdf(fileName, path, logObjects, currentPosition, date1, filter, searchText);
                break;
        }
    }

    @Override
    public void exportExcelToUSB(String filename, UsbFile root) {
        String filter = "";
        switch (currentPosition) {
            case 0:
                if (filterType == 1) {
                    filter = context.getString(R.string.product);
                } else if (filterType == 2) {
                    filter = context.getString(R.string.category);
                } else if (filterType == 3) {
                    filter = context.getString(R.string.subcategory);
                } else if (filterType == 4) {
                    filter = context.getString(R.string.product_class);
                }
                String date = simpleDateFormat.format(fromDateSummary.getTime()) + " - " + simpleDateFormat.format(toDateSummary.getTime());
                view.exportExcelToUSB(filename, root, summaryObjects, currentPosition, date, filter, searchText);
                break;
            case 1:
                String date1 = simpleDateFormat.format(fromDateLog.getTime()) + " - " + simpleDateFormat.format(toDateLog.getTime());
                view.exportExcelToUSB(filename, root, logObjects, currentPosition, date1, filter, searchText);
                break;
        }
    }

    @Override
    public void exportPdfToUSB(String filename, UsbFile root) {
        String filter = "";
        switch (currentPosition) {
            case 0:
                if (filterType == 1) {
                    filter = context.getString(R.string.product);
                } else if (filterType == 2) {
                    filter = context.getString(R.string.category);
                } else if (filterType == 3) {
                    filter = context.getString(R.string.subcategory);
                } else if (filterType == 4) {
                    filter = context.getString(R.string.product_class);
                }
                String date = simpleDateFormat.format(fromDateSummary.getTime()) + " - " + simpleDateFormat.format(toDateSummary.getTime());
                view.exportTableToPdfToUSB(filename, root, summaryObjects, currentPosition, date, filter, searchText);
                break;
            case 1:
                String date1 = simpleDateFormat.format(fromDateLog.getTime()) + " - " + simpleDateFormat.format(toDateLog.getTime());
                view.exportTableToPdfToUSB(filename, root, logObjects, currentPosition, date1, filter, searchText);
                break;
        }
    }
    @Override
    public void onBarcodeReaded(String barcode) {
        databaseManager.getAllProducts().subscribe(products -> {
            for(Product product:products)
                if(product.getBarcode() !=null && product.getBarcode().equals(barcode)){
                    view.setTextToSearch(product.getName());
                }
        });
    }
}
