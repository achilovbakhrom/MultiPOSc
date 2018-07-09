package com.jim.multipos.ui.reports.stock_state;

import android.os.Bundle;
import android.os.Handler;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.ui.reports.stock_state.module.InventoryItemReport;
import com.jim.multipos.utils.ReportUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class StockStatePresenterImpl extends BasePresenterImpl<StockStateView> implements StockStatePresenter {

    private Object[][] firstObjects;
    private Object[][] secondObjects;
    private Object[][] thirdObjects;
    private int currentPosition = 0;
    private int[] filterConfig;
    private DatabaseManager databaseManager;
    private DecimalFormat decimalFormat;
    private SimpleDateFormat simpleDateFormat;

    @Inject
    protected StockStatePresenterImpl(StockStateView stockStateView, DatabaseManager databaseManager) {
        super(stockStateView);
        this.databaseManager = databaseManager;
        filterConfig = new int[]{1, 1};

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat1.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat1.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat = decimalFormat1;

    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        new Handler().postDelayed(() -> {
            initReportTable();
            view.initTable(firstObjects);
        }, 50);
    }

    private void initReportTable() {
        switch (currentPosition) {
            case 0:
                List<InventoryItemReport> itemWithSummaryCosts = databaseManager.getInventoryWithSummaryCost().blockingGet();
                firstObjects = new Object[itemWithSummaryCosts.size()][4];
                for (int i = 0; i < itemWithSummaryCosts.size(); i++) {
                    InventoryItemReport itemWithSummaryCost = itemWithSummaryCosts.get(i);
                        firstObjects[i][0] = ReportUtils.getProductName(itemWithSummaryCost.getProduct());
                        firstObjects[i][1] = itemWithSummaryCost.getAvailable();
                        firstObjects[i][2] = itemWithSummaryCost.getProduct().getMainUnit().getAbbr();
                        firstObjects[i][3] = itemWithSummaryCost.getSumCost();
                    }
                break;
            case 1:
                List<InventoryItemReport> itemVendorWithSummaryCosts = databaseManager.getInventoryVendorWithSummaryCost().blockingGet();
                secondObjects = new Object[itemVendorWithSummaryCosts.size()][5];
                for (int i = 0; i < itemVendorWithSummaryCosts.size(); i++) {
                    InventoryItemReport itemWithSummaryCost = itemVendorWithSummaryCosts.get(i);
                    secondObjects[i][0] = ReportUtils.getProductName(itemWithSummaryCost.getProduct());
                    secondObjects[i][1] = (itemWithSummaryCost.getVendor()!=null)?itemWithSummaryCost.getVendor().getName():"";
                    secondObjects[i][2] = itemWithSummaryCost.getAvailable();
                    secondObjects[i][3] = itemWithSummaryCost.getProduct().getMainUnit().getAbbr();
                    secondObjects[i][4] = itemWithSummaryCost.getSumCost();
                }
                break;
            case 2:
                List<StockQueue> stockQueuesEx = databaseManager.getExpiredStockQueue().blockingGet();
                thirdObjects = new Object[stockQueuesEx.size()][7];
                for (int i = 0; i < stockQueuesEx.size(); i++) {
                    StockQueue stockQueue = stockQueuesEx.get(i);
                    thirdObjects[i][0] = ReportUtils.getProductName(stockQueue.getProduct());
                    thirdObjects[i][1] = stockQueue.getCreatedProductDate();
                    thirdObjects[i][2] = stockQueue.getIncomeProductDate();
                    thirdObjects[i][3] = stockQueue.getExpiredProductDate();

                    String expiredLeft = "";
                    int t[] = ReportUtils.getDateDifferenceInDDMMYYYY(Calendar.getInstance().getTime(), new Date(stockQueue.getExpiredProductDate()));
                    if (t[0] * t[1] * t[2] < 0 && (t[0] + t[1] + t[2]) != 0) {
                        expiredLeft = "Expired";
                    } else {
                        if (t[0] != 0) {
                            expiredLeft = Integer.toString(t[0]) + " " + "год.";
                        }
                        if (t[1] != 0) {
                            if (!expiredLeft.equals("")) {
                                expiredLeft += " ";
                            }
                            expiredLeft += Integer.toString(t[1]) + " " + "мес.";
                        }
                        if (t[2] != 0) {
                            if (!expiredLeft.equals("")) {
                                expiredLeft += " ";
                            }
                            expiredLeft += Integer.toString(t[2]) + " " + "день.";
                        }
                    }

                    thirdObjects[i][4] = expiredLeft;
                    thirdObjects[i][5] = stockQueue.getAvailable();
                    thirdObjects[i][6] = stockQueue.getProduct().getMainUnit().getAbbr();

                }

                break;
        }
    }


    @Override
    public void onChooseDateInterval(Calendar fromDate, Calendar toDate) {

    }
    private int prev = -1;
    private Object[][] searchResultsTemp;
    private String searchText;

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
            }
            prev = -1;

        } else {
            switch (currentPosition){
                case 0:
                    if (searchText.length() <= prev || prev == -1) {
                        int searchRes[] = new int[firstObjects.length];
                        for (int i = 0; i < firstObjects.length; i++) {
                            if (((String) firstObjects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((double) firstObjects[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) firstObjects[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) firstObjects[i][3])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < firstObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][4];

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
                            if (String.valueOf((double) searchResultsTemp[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][3])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }
                        int sumSize = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][4];

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
                            if (((String) secondObjects[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((double) secondObjects[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) secondObjects[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) secondObjects[i][4])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < secondObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][5];

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
                            if (((String) searchResultsTemp[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((double) searchResultsTemp[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][4])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }
                        int sumSize = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][5];

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
                            if (simpleDateFormat.format(new Date((long) thirdObjects[i][1])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) thirdObjects[i][2])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) thirdObjects[i][3])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) thirdObjects[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((double) thirdObjects[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) thirdObjects[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < thirdObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][7];

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
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][1])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][2])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][3])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][4])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((double) searchResultsTemp[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }
                        int sumSize = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][7];

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
        }else {
            if (thirdObjects == null) {
                initReportTable();
            }
            view.updateTable(thirdObjects, currentPosition);
        }

    }


    @Override
    public void onTillPickerClicked() {

    }



}
