package com.jim.multipos.ui.reports.inventory;

import android.content.Context;
import android.os.Bundle;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.inventory.HistoryInventoryState;
import com.jim.multipos.data.db.model.inventory.WarehouseOperations;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Return;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.ui.reports.inventory.model.InventorySummary;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

public class InventoryReportPresenterImpl extends BasePresenterImpl<InventoryReportView> implements InventoryReportPresenter {

    private DatabaseManager databaseManager;
    private Context context;
    private Object[][] firstObjects;
    private Object[][] secondObjects;
    private Object[][] thirdObjects;
    private Object[][] forthObjects;
    private int currentPosition = 0;
    private Calendar fromDate;
    private Calendar toDate;
    private DecimalFormat decimalFormat;
    private int[] filterConfig;
    private SimpleDateFormat simpleDateFormat;
    private Till till;

    @Inject
    protected InventoryReportPresenterImpl(InventoryReportView view, DatabaseManager databaseManager, Context context) {
        super(view);
        this.databaseManager = databaseManager;
        this.context = context;
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat1.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat1.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat = decimalFormat1;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        filterConfig = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1};
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        Date currentDate = new Date();
        fromDate = new GregorianCalendar();
        toDate = new GregorianCalendar();
        boolean isNoTills = databaseManager.isNoTills().blockingGet();
        if (!isNoTills) {
            till = null;
        } else {
            till = databaseManager.getLastClosedTill().blockingGet();
        }
        toDate.setTime(currentDate);
        toDate.set(Calendar.HOUR_OF_DAY, 23);
        toDate.set(Calendar.MINUTE, 59);
        toDate.set(Calendar.SECOND, 59);

        fromDate.add(Calendar.MONTH, -1);
        fromDate.set(Calendar.HOUR_OF_DAY, 0);
        fromDate.set(Calendar.MINUTE, 0);
        fromDate.set(Calendar.SECOND, 0);
        view.updateDateIntervalUi(fromDate, toDate);
        initReportTable();
        view.initTable(firstObjects);
    }

    private void initReportTable() {
        switch (currentPosition) {
            case 0:
                List<WarehouseOperations> warehouseOperations = new ArrayList<>();
                databaseManager.getWarehouseOperationsInInterval(fromDate, toDate).subscribe(warehouseOperations1 -> {
                    for (WarehouseOperations operations : warehouseOperations1) {
                        if (filterConfig[0] == 1 && operations.getType() == WarehouseOperations.CANCELED_SOLD) {
                            warehouseOperations.add(operations);
                            continue;
                        }
                        if (filterConfig[1] == 1 && operations.getType() == WarehouseOperations.CONSIGNMENT_DELETED) {
                            warehouseOperations.add(operations);
                            continue;
                        }
                        if (filterConfig[2] == 1 && operations.getType() == WarehouseOperations.INCOME_FROM_VENDOR) {
                            warehouseOperations.add(operations);
                            continue;
                        }
                        if (filterConfig[3] == 1 && operations.getType() == WarehouseOperations.RETURN_HOLDED) {
                            warehouseOperations.add(operations);
                            continue;
                        }
                        if (filterConfig[4] == 1 && operations.getType() == WarehouseOperations.RETURN_SOLD) {
                            warehouseOperations.add(operations);
                            continue;
                        }
                        if (filterConfig[5] == 1 && operations.getType() == WarehouseOperations.RETURN_TO_VENDOR) {
                            warehouseOperations.add(operations);
                            continue;
                        }
                        if (filterConfig[6] == 1 && operations.getType() == WarehouseOperations.SOLD) {
                            warehouseOperations.add(operations);
                            continue;
                        }
                        if (filterConfig[7] == 1 && operations.getType() == WarehouseOperations.VOID_INCOME) {
                            warehouseOperations.add(operations);
                            continue;
                        }
                        if (filterConfig[8] == 1 && operations.getType() == WarehouseOperations.WASTE) {
                            warehouseOperations.add(operations);
                            continue;
                        }
                    }
                });
                firstObjects = new Object[warehouseOperations.size()][8];
                for (int i = 0; i < warehouseOperations.size(); i++) {
                    WarehouseOperations operations = warehouseOperations.get(i);
                    firstObjects[i][0] = operations.getProduct().getName();
                    firstObjects[i][1] = operations.getVendor().getName();
                    firstObjects[i][2] = operations.getType();
                    if (operations.getValue() < 0)
                        firstObjects[i][3] = operations.getValue() * -1 + " " + operations.getProduct().getMainUnit().getAbbr();
                    else
                        firstObjects[i][3] = operations.getValue() + " " + operations.getProduct().getMainUnit().getAbbr();
                    firstObjects[i][4] = operations.getCreatedDate();
                    firstObjects[i][5] = operations.getDescription();
                    if (operations.getOrder() != null)
                        firstObjects[i][6] = "#" + operations.getOrder().getId();
                    else firstObjects[i][6] = "";
                    Long id = databaseManager.getConsignmentByWarehouseId(operations.getId()).blockingGet();
                    if (id == -1L) {
                        firstObjects[i][7] = "";
                    } else firstObjects[i][7] = "#" + id;
                }
                break;
            case 1:
                List<InventorySummary> inventorySummaries = new ArrayList<>();
                List<Product> productList = databaseManager.getAllProducts().blockingSingle();
                List<WarehouseOperations> operationsList = databaseManager.getWarehouseOperationsInInterval(fromDate, toDate).blockingGet();
                for (int i = 0; i < productList.size(); i++) {
                    Product product = productList.get(i);
                    for (int j = 0; j < product.getVendor().size(); j++) {
                        Vendor vendor = product.getVendor().get(j);
                        double sold = 0;
                        double receivedFromVendor = 0;
                        double returnToVendor = 0;
                        double returnFromCustomer = 0;
                        double voidIncome = 0;
                        double wasted = 0;
                        for (int k = 0; k < operationsList.size(); k++) {
                            WarehouseOperations operations = operationsList.get(k);
                            if (product.getRootId().equals(operations.getProduct().getRootId()) && vendor.getId().equals(operations.getVendorId())) {
                                if (operations.getType() == WarehouseOperations.SOLD)
                                    sold += operations.getValue();
                                if (operations.getType() == WarehouseOperations.INCOME_FROM_VENDOR)
                                    receivedFromVendor += operations.getValue();
                                if (operations.getType() == WarehouseOperations.RETURN_TO_VENDOR)
                                    returnToVendor += operations.getValue();
                                if (operations.getType() == WarehouseOperations.RETURN_SOLD)
                                    returnFromCustomer += operations.getValue();
                                if (operations.getType() == WarehouseOperations.VOID_INCOME)
                                    voidIncome += operations.getValue();
                                if (operations.getType() == WarehouseOperations.WASTE)
                                    wasted += operations.getValue();
                            }
                        }
                        InventorySummary inventorySummary = new InventorySummary();
                        inventorySummary.setProduct(product);
                        inventorySummary.setVendor(vendor);
                        inventorySummary.setSold(sold);
                        inventorySummary.setReceivedFromVendor(receivedFromVendor);
                        inventorySummary.setRetrunFromCustomer(returnFromCustomer);
                        inventorySummary.setVoidIncome(voidIncome);
                        inventorySummary.setRetrunToVendor(returnToVendor);
                        inventorySummary.setWasted(wasted);
                        inventorySummaries.add(inventorySummary);
                    }
                }
                secondObjects = new Object[inventorySummaries.size()][8];
                for (int i = 0; i < inventorySummaries.size(); i++) {
                    InventorySummary inventorySummary = inventorySummaries.get(i);
                    String unit = inventorySummary.getProduct().getMainUnit().getAbbr();
                    secondObjects[i][0] = inventorySummary.getProduct().getName();
                    secondObjects[i][1] = inventorySummary.getVendor().getName();
                    if (inventorySummary.getSold() < 0)
                        secondObjects[i][2] = inventorySummary.getSold() * -1 + " " + unit;
                    else
                        secondObjects[i][2] = inventorySummary.getSold() + " " + unit;
                    secondObjects[i][3] = inventorySummary.getReceivedFromVendor() + " " + unit;
                    if (inventorySummary.getRetrunToVendor() < 0)
                        secondObjects[i][4] = inventorySummary.getRetrunToVendor() * -1 + " " + unit;
                    else
                        secondObjects[i][4] = inventorySummary.getRetrunToVendor() + " " + unit;
                    secondObjects[i][5] = inventorySummary.getRetrunFromCustomer() + " " + unit;
                    secondObjects[i][6] = inventorySummary.getVoidIncome() + " " + unit;
                    if (inventorySummary.getWasted() < 0)
                        secondObjects[i][7] = inventorySummary.getWasted() * -1 + " " + unit;
                    else
                        secondObjects[i][7] = inventorySummary.getWasted() + " " + unit;
                }
                break;
            case 2:
                if (till != null) {
                    List<HistoryInventoryState> inventoryStates = databaseManager.getHistoryInventoryStatesByTillId(till.getId()).blockingGet();
                    thirdObjects = new Object[inventoryStates.size()][5];
                    for (int i = 0; i < inventoryStates.size(); i++) {
                        HistoryInventoryState state = inventoryStates.get(i);
                        Product product = databaseManager.getProductByRootId(state.getProductId()).blockingGet();
                        thirdObjects[i][0] = product.getName();
                        thirdObjects[i][1] = state.getVendor().getName();
                        thirdObjects[i][2] = state.getValue() + " " + state.getProduct().getMainUnit().getAbbr();
                        thirdObjects[i][3] = product.getPrice();
                        thirdObjects[i][4] = product.getPrice() * state.getValue();
                    }
                } else {
                    thirdObjects = new Object[0][5];
                }
                break;
            case 3:
                List<Return> returnList = databaseManager.getReturnList(fromDate, toDate).blockingGet();
                forthObjects = new Object[returnList.size()][7];
                for (int i = 0; i < returnList.size(); i++) {
                    Return item = returnList.get(i);
                    forthObjects[i][0] = item.getProduct().getName();
                    forthObjects[i][1] = item.getCreateAt();
                    forthObjects[i][2] = item.getProduct().getPrice();
                    forthObjects[i][3] = item.getReturnAmount();
                    forthObjects[i][4] = item.getQuantity();
                    forthObjects[i][5] = item.getPaymentType().getName();
                    forthObjects[i][6] = item.getDescription();
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
        }
    }

    private int prev = -1;
    private Object[][] searchResultsTemp;

    @Override
    public void onSearchTyped(String searchText) {
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
                            if (((String) firstObjects[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) firstObjects[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) firstObjects[i][4])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) firstObjects[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) firstObjects[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) firstObjects[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int status = (int) firstObjects[i][2];
                            if (status == 0) {
                                text = context.getString(R.string.canceled_order);
                            }
                            if (status == 1) {
                                text = context.getString(R.string.consignment_canceled);
                            }
                            if (status == 2) {
                                text = context.getString(R.string.income_from_vendor);
                            }
                            if (status == 3) {
                                text = context.getString(R.string.held_product_return);
                            }
                            if (status == 4) {
                                text = context.getString(R.string.return_from_customer);
                            }
                            if (status == 5) {
                                text = context.getString(R.string.return_to_vendor);
                            }
                            if (status == 6) {
                                text = context.getString(R.string.sold);
                            }
                            if (status == 7) {
                                text = context.getString(R.string.void_income);
                            }
                            if (status == 8) {
                                text = context.getString(R.string.wasted);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
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
                            if (((String) searchResultsTemp[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
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
                            if (((String) searchResultsTemp[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int status = (int) searchResultsTemp[i][2];
                            if (status == 0) {
                                text = context.getString(R.string.canceled_order);
                            }
                            if (status == 1) {
                                text = context.getString(R.string.consignment_canceled);
                            }
                            if (status == 2) {
                                text = context.getString(R.string.income_from_vendor);
                            }
                            if (status == 3) {
                                text = context.getString(R.string.held_product_return);
                            }
                            if (status == 4) {
                                text = context.getString(R.string.return_from_customer);
                            }
                            if (status == 5) {
                                text = context.getString(R.string.return_to_vendor);
                            }
                            if (status == 6) {
                                text = context.getString(R.string.sold);
                            }
                            if (status == 7) {
                                text = context.getString(R.string.void_income);
                            }
                            if (status == 8) {
                                text = context.getString(R.string.wasted);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
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
                            if (((String) secondObjects[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) secondObjects[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) secondObjects[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) secondObjects[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) secondObjects[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) secondObjects[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
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
                            if (((String) searchResultsTemp[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
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
                            if (((String) thirdObjects[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (decimalFormat.format((double) thirdObjects[i][3]).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (decimalFormat.format((double) thirdObjects[i][4]).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < thirdObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][5];

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
                            if (((String) searchResultsTemp[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
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
                case 3:
                    if (searchText.length() <= prev || prev == -1) {
                        int searchRes[] = new int[forthObjects.length];
                        for (int i = 0; i < forthObjects.length; i++) {
                            if (((String) forthObjects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) forthObjects[i][1])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) forthObjects[i][2])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) forthObjects[i][3])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((double) forthObjects[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) forthObjects[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) forthObjects[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < forthObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][7];

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
                            if (((String) searchResultsTemp[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][1])).contains(searchText.toUpperCase())) {
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
                            if (String.valueOf((double) searchResultsTemp[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
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
        view.openDateInterval(fromDate, toDate);
    }

    @Override
    public void onClickedExportExcel() {

    }

    @Override
    public void onClickedExportPDF() {

    }

    @Override
    public void onClickedFilter() {
        view.showFilterDialog(filterConfig);
    }

    @Override
    public void onChoisedPanel(int position) {
        this.currentPosition = position;
        updateTable();
    }

    @Override
    public void onTillPickerClicked() {
        view.showTillPickerDialog();
    }

    @Override
    public void setFilterConfigs(int[] config) {
        filterConfig = config;
        updateTable();
    }

    @Override
    public void setPickedTill(Till till) {
        this.till = till;
        updateTable();
    }
}
