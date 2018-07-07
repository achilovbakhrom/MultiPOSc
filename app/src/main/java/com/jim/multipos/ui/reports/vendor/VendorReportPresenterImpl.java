package com.jim.multipos.ui.reports.vendor;

import android.content.Context;
import android.os.Bundle;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

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

public class VendorReportPresenterImpl extends BasePresenterImpl<VendorReportView> implements VendorReportPresenter {
    private DatabaseManager databaseManager;
    private Context context;
    private Object[][] firstObjects;
    private Object[][] secondObjects;
    private Object[][] thirdObjects;
    private Object[][] forthObjects;
    private int currentPosition = 0;
    private Calendar fromDate;
    private Calendar toDate;
    private int[] filterConfig;
    private DecimalFormat decimalFormat;
    private SimpleDateFormat simpleDateFormat;
    private String searchText = "";

    @Inject
    protected VendorReportPresenterImpl(VendorReportView view, DatabaseManager databaseManager, Context context) {
        super(view);
        this.databaseManager = databaseManager;
        this.context = context;
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat1.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat1.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat = decimalFormat1;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        filterConfig = new int[]{1, 1, 1};
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
                List<Consignment> consignments = databaseManager.getConsignmentsInInterval(fromDate, toDate).blockingGet();
                firstObjects = new Object[consignments.size()][6];
                for (int i = 0; i < consignments.size(); i++) {
                    Consignment consignment = consignments.get(i);
                    firstObjects[i][0] = consignment.getId();
                    firstObjects[i][1] = consignment.getVendor().getName();
                    firstObjects[i][2] = consignment.getConsignmentType();
                    firstObjects[i][3] = consignment.getCreatedDate();
                    firstObjects[i][4] = consignment.getTotalAmount();
                    firstObjects[i][5] = consignment.getDescription();
                }
                break;
            case 1:
                List<ConsignmentProduct> consignmentProducts = databaseManager.getConsignmentProductsInterval(fromDate, toDate).blockingGet();
                secondObjects = new Object[consignmentProducts.size()][8];
                for (int i = 0; i < consignmentProducts.size(); i++) {
                    ConsignmentProduct product = consignmentProducts.get(i);
                    secondObjects[i][0] = product.getConsignmentId();
                    secondObjects[i][1] = product.getProduct().getName();
                    secondObjects[i][2] = product.getConsignment().getVendor().getName();
                    secondObjects[i][3] = product.getCreatedDate();
                    secondObjects[i][4] = product.getConsignment().getConsignmentType();
                    secondObjects[i][5] = product.getCountValue() + " " + product.getProduct().getMainUnit().getAbbr();
                    secondObjects[i][6] = product.getCostValue();
                    secondObjects[i][7] = product.getCostValue() * product.getCountValue();
                }
                break;
            case 2:
                List<Vendor> vendors = databaseManager.getVendors().blockingSingle();
                thirdObjects = new Object[vendors.size()][4];
                for (int i = 0; i < vendors.size(); i++) {
                    Vendor vendor = vendors.get(i);
                    double paidAmount = 0;
                    double debtAmount = 0;
                    List<BillingOperations> billingOperations = databaseManager.getBillingOperationInteval(vendor.getId(), fromDate, toDate).blockingGet();
                    for (BillingOperations operations : billingOperations) {
                        if (operations.getOperationType() == BillingOperations.DEBT_CONSIGNMENT) {
                            debtAmount += operations.getAmount();
                        }
                        if (operations.getOperationType() == BillingOperations.PAID_TO_CONSIGNMENT) {
                            paidAmount += operations.getAmount();
                        }
                        if (operations.getOperationType() == BillingOperations.RETURN_TO_VENDOR) {
                            paidAmount += operations.getAmount();
                        }
                    }
                    thirdObjects[i][0] = vendor.getName();
                    thirdObjects[i][1] = debtAmount * -1;
                    thirdObjects[i][2] = paidAmount;
                    thirdObjects[i][3] = (debtAmount * -1) - paidAmount;
                }
                break;
            case 3:
                List<BillingOperations> billingOperations = new ArrayList<>();
                databaseManager.getAllBillingOperationsInInterval(fromDate, toDate).subscribe(billingOperations1 -> {
                    for (BillingOperations operations : billingOperations1) {
                        if (filterConfig[0] == 1 && operations.getOperationType() == BillingOperations.PAID_TO_CONSIGNMENT) {
                            billingOperations.add(operations);
                            continue;
                        }
                        if (filterConfig[1] == 1 && operations.getOperationType() == BillingOperations.RETURN_TO_VENDOR) {
                            billingOperations.add(operations);
                            continue;
                        }
                        if (filterConfig[2] == 1 && operations.getOperationType() == BillingOperations.DEBT_CONSIGNMENT) {
                            billingOperations.add(operations);
                            continue;
                        }
                    }
                });
                forthObjects = new Object[billingOperations.size()][8];
                for (int i = 0; i < billingOperations.size(); i++) {
                    BillingOperations operation = billingOperations.get(i);
                    forthObjects[i][0] = operation.getPaymentDate();
                    forthObjects[i][1] = operation.getVendor().getName();
                    forthObjects[i][2] = operation.getOperationType();
                    if (operation.getAmount() < 0)
                        forthObjects[i][3] = operation.getAmount() * -1;
                    else forthObjects[i][3] = operation.getAmount();
                    if (operation.getAccount() != null)
                        forthObjects[i][4] = operation.getAccount().getName();
                    else forthObjects[i][4] = "";
                    if (operation.getInvoice() != null)
                        forthObjects[i][5] = operation.getInvoice().getId();
                    else forthObjects[i][5] = "";
                    forthObjects[i][6] = operation.getCreateAt();
                    forthObjects[i][7] = operation.getDescription();
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
                            if (String.valueOf((long) firstObjects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) firstObjects[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) firstObjects[i][3])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) firstObjects[i][4])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) firstObjects[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String type = "";
                            int consignmentType = (int) firstObjects[i][2];
                            if (consignmentType == Consignment.INCOME_CONSIGNMENT) {
                                type = context.getString(R.string.receive);
                            } else if (consignmentType == Consignment.RETURN_CONSIGNMENT) {
                                type = context.getString(R.string.return_);
                            }
                            if (type.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < firstObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][6];

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
                            if (String.valueOf((long) searchResultsTemp[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][3])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][4])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String type = "";
                            int consignmentType = (int) searchResultsTemp[i][2];
                            if (consignmentType == Consignment.INCOME_CONSIGNMENT) {
                                type = context.getString(R.string.receive);
                            } else if (consignmentType == Consignment.RETURN_CONSIGNMENT) {
                                type = context.getString(R.string.return_);
                            }
                            if (type.toUpperCase().contains(searchText.toUpperCase())) {
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
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    }
                    prev = searchText.length();
                    break;
                case 1:
                    if (searchText.length() <= prev || prev == -1) {
                        int searchRes[] = new int[secondObjects.length];
                        for (int i = 0; i < secondObjects.length; i++) {
                            if (String.valueOf((long) secondObjects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
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
                            if (simpleDateFormat.format(new Date((long) secondObjects[i][3])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) secondObjects[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) secondObjects[i][6])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) secondObjects[i][7])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String type = "";
                            int consignmentType = (int) secondObjects[i][4];
                            if (consignmentType == Consignment.INCOME_CONSIGNMENT) {
                                type = context.getString(R.string.receive);
                            } else if (consignmentType == Consignment.RETURN_CONSIGNMENT) {
                                type = context.getString(R.string.return_);
                            }
                            if (type.toUpperCase().contains(searchText.toUpperCase())) {
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
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][3])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
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
                            String type = "";
                            int consignmentType = (int) searchResultsTemp[i][4];
                            if (consignmentType == Consignment.INCOME_CONSIGNMENT) {
                                type = context.getString(R.string.receive);
                            } else if (consignmentType == Consignment.RETURN_CONSIGNMENT) {
                                type = context.getString(R.string.return_);
                            }
                            if (type.toUpperCase().contains(searchText.toUpperCase())) {
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
                            if ((decimalFormat.format((double) thirdObjects[i][1])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) thirdObjects[i][2])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) thirdObjects[i][3])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < thirdObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][4];

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
                case 3:
                    if (searchText.length() <= prev || prev == -1) {
                        int searchRes[] = new int[forthObjects.length];
                        for (int i = 0; i < forthObjects.length; i++) {
                            if (simpleDateFormat.format(new Date((long) forthObjects[i][0])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) forthObjects[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) forthObjects[i][3])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) forthObjects[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (forthObjects[i][5] instanceof Long) {
                                if (String.valueOf((long) forthObjects[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                                    searchRes[i] = 1;
                                    continue;
                                }
                            }
                            if (simpleDateFormat.format(new Date((long) forthObjects[i][6])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) forthObjects[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String type = "";
                            int consignmentType = (int) forthObjects[i][2];
                            if (consignmentType == BillingOperations.RETURN_TO_VENDOR) {
                                type = context.getString(R.string.return_);
                            } else if (consignmentType == BillingOperations.DEBT_CONSIGNMENT) {
                                type = context.getString(R.string.debt_);
                            } else if (consignmentType == BillingOperations.PAID_TO_CONSIGNMENT) {
                                type = context.getString(R.string.pay);
                            }
                            if (type.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < forthObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][8];

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
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][0])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][3])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (searchResultsTemp[i][5] instanceof Long) {
                                if (String.valueOf((long) searchResultsTemp[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                                    searchRes[i] = 1;
                                    continue;
                                }
                            }
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][6])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String type = "";
                            int consignmentType = (int) searchResultsTemp[i][2];
                            if (consignmentType == BillingOperations.RETURN_TO_VENDOR) {
                                type = context.getString(R.string.return_);
                            } else if (consignmentType == BillingOperations.DEBT_CONSIGNMENT) {
                                type = context.getString(R.string.debt_);
                            } else if (consignmentType == BillingOperations.PAID_TO_CONSIGNMENT) {
                                type = context.getString(R.string.pay);
                            }
                            if (type.toUpperCase().contains(searchText.toUpperCase())) {
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
        view.showFilterDialog(filterConfig);
    }

    @Override
    public void onChoisedPanel(int position) {
        this.currentPosition = position;
        view.clearSearch();
        updateTable();
    }

    @Override
    public void onTillPickerClicked() {

    }

    @Override
    public void onConsignmentClicked(Object[][] objects, int row, int column) {
        if (currentPosition == 0) {
            if (objects[row][0] instanceof Long) {
                Long id = (Long) objects[row][0];
                Consignment consignment = databaseManager.getConsignmentById(id).blockingGet();
                view.openConsignmentId(consignment);
            }
        } else if (currentPosition == 3){
            if (objects[row][5] instanceof Long) {
                Long id = (Long) objects[row][5];
                Consignment consignment = databaseManager.getConsignmentById(id).blockingGet();
                view.openConsignmentId(consignment);
            }
        }
    }

    @Override
    public void onFilterChange(int[] config) {
        this.filterConfig = config;
        updateTable();
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
                if (searchResultsTemp != null) {
                    view.exportTableToExcel(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToExcel(fileName, path, secondObjects, currentPosition, date, filter, searchText);
                break;
            case 2:
                if (searchResultsTemp != null) {
                    view.exportTableToExcel(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToExcel(fileName, path, thirdObjects, currentPosition, date, filter, searchText);
                break;
            case 3:
                StringBuilder filters = new StringBuilder();
                if (filterConfig[0] == 1) {
                    filters.append(context.getString(R.string.pay)).append(" ");
                }
                if (filterConfig[1] == 1) {
                    filters.append(context.getString(R.string.return_)).append(" ");
                }
                if (filterConfig[2] == 1) {
                    filters.append(context.getString(R.string.debt_));
                }
                filter = filters.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToExcel(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToExcel(fileName, path, forthObjects, currentPosition, date, filter, searchText);
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
                if (searchResultsTemp != null) {
                    view.exportTableToPdf(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdf(fileName, path, secondObjects, currentPosition, date, filter, searchText);
                break;
            case 2:
                if (searchResultsTemp != null) {
                    view.exportTableToPdf(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdf(fileName, path, thirdObjects, currentPosition, date, filter, searchText);
                break;
            case 3:
                StringBuilder filters = new StringBuilder();
                if (filterConfig[0] == 1) {
                    filters.append(context.getString(R.string.pay)).append(" ");
                }
                if (filterConfig[1] == 1) {
                    filters.append(context.getString(R.string.return_)).append(" ");
                }
                if (filterConfig[2] == 1) {
                    filters.append(context.getString(R.string.debt_));
                }
                filter = filters.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToPdf(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdf(fileName, path, forthObjects, currentPosition, date, filter, searchText);
                break;
        }
    }

    @Override
    public void exportExcelToUSB(String filename, UsbFile root) {
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        String filter = "";
        switch (currentPosition) {
            case 0:
                if (searchResultsTemp != null) {
                    view.exportExcelToUSB(filename, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportExcelToUSB(filename, root, firstObjects, currentPosition, date, filter, searchText);

                break;
            case 1:
                if (searchResultsTemp != null) {
                    view.exportExcelToUSB(filename, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportExcelToUSB(filename, root, secondObjects, currentPosition, date, filter, searchText);
                break;
            case 2:
                if (searchResultsTemp != null) {
                    view.exportExcelToUSB(filename, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportExcelToUSB(filename, root, thirdObjects, currentPosition, date, filter, searchText);
                break;
            case 3:
                StringBuilder filters = new StringBuilder();
                if (filterConfig[0] == 1) {
                    filters.append(context.getString(R.string.pay)).append(" ");
                }
                if (filterConfig[1] == 1) {
                    filters.append(context.getString(R.string.return_)).append(" ");
                }
                if (filterConfig[2] == 1) {
                    filters.append(context.getString(R.string.debt_));
                }
                filter = filters.toString();
                if (searchResultsTemp != null) {
                    view.exportExcelToUSB(filename, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportExcelToUSB(filename, root, forthObjects, currentPosition, date, filter, searchText);
                break;
        }
    }

    @Override
    public void exportPdfToUSB(String fileName, UsbFile path) {
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        String filter = "";
        switch (currentPosition) {
            case 0:
                if (searchResultsTemp != null) {
                    view.exportTableToPdfToUSB(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdfToUSB(fileName, path, firstObjects, currentPosition, date, filter, searchText);

                break;
            case 1:
                if (searchResultsTemp != null) {
                    view.exportTableToPdfToUSB(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdfToUSB(fileName, path, secondObjects, currentPosition, date, filter, searchText);
                break;
            case 2:
                if (searchResultsTemp != null) {
                    view.exportTableToPdfToUSB(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdfToUSB(fileName, path, thirdObjects, currentPosition, date, filter, searchText);
                break;
            case 3:
                StringBuilder filters = new StringBuilder();
                if (filterConfig[0] == 1) {
                    filters.append(context.getString(R.string.pay)).append(" ");
                }
                if (filterConfig[1] == 1) {
                    filters.append(context.getString(R.string.return_)).append(" ");
                }
                if (filterConfig[2] == 1) {
                    filters.append(context.getString(R.string.debt_));
                }
                filter = filters.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToPdfToUSB(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdfToUSB(fileName, path, forthObjects, currentPosition, date, filter, searchText);
                break;
        }
    }

    @Override
    public void onBarcodeReaded(String barcode) {
        databaseManager.getAllProducts().subscribe(products -> {
            for (Product product : products)
                if (product.getBarcode() != null && product.getBarcode().equals(barcode)) {
                    view.setTextToSearch(product.getName());
                }
        });
    }
}
