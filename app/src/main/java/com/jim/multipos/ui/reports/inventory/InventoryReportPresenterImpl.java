package com.jim.multipos.ui.reports.inventory;

import android.content.Context;
import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.inventory.WarehouseOperations;

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
                break;
            case 2:
                break;
            case 3:
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
    public void setFilterConfigs(int[] config) {
        filterConfig = config;
        updateTable();
    }
}
