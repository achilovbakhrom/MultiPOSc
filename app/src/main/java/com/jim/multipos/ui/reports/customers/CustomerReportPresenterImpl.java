package com.jim.multipos.ui.reports.customers;

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
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.reports.customers.model.CustomerGroupOrder;
import com.jim.multipos.ui.reports.customers.model.CustomerPaymentLog;

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

public class CustomerReportPresenterImpl extends BasePresenterImpl<CustomerReportView> implements CustomerReportPresenter {

    private DatabaseManager databaseManager;
    private Context context;
    private Object[][] firstObjects;
    private Object[][] secondObjects;
    private Object[][] thirdObjects;
    private int currentPosition = 0;
    private Calendar fromDate;
    private Calendar toDate;
    private DecimalFormat decimalFormat;
    private int[] filterConfig;
    private int filterValue = 0, secondValue = 0;
    private SimpleDateFormat simpleDateFormat;
    private String searchText;

    @Inject
    protected CustomerReportPresenterImpl(CustomerReportView view, DatabaseManager databaseManager, Context context) {
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
        new Handler().postDelayed(()->{
            initReportTable();
            view.initTable(firstObjects);
        },50);

    }

    private void initReportTable() {
        switch (currentPosition) {
            case 0:
                if (filterValue == 0) {
                    List<Customer> customerList = databaseManager.getAllCustomers().blockingSingle();
                    firstObjects = new Object[customerList.size()][8];
                    for (int i = 0; i < customerList.size(); i++) {
                        Customer customer = customerList.get(i);
                        int closedCount = 0;
                        int canceledCount = 0;
                        double totalOrdersAmount = 0;
                        double totalToDebts = 0;
                        double totalDiscounts = 0;
                        double totalServiceFees = 0;
                        List<Order> orderList = databaseManager.getOrdersWithCustomerInInterval(customer.getId(), fromDate, toDate).blockingGet();
                        for (int j = 0; j < orderList.size(); j++) {
                            Order order = orderList.get(j);
                            if (order.getStatus() == Order.CLOSED_ORDER)
                                closedCount++;
                            if (order.getStatus() == Order.CANCELED_ORDER)
                                canceledCount++;
                            totalOrdersAmount += order.getSubTotalValue();
                            totalDiscounts += order.getDiscountTotalValue();
                            totalServiceFees += order.getServiceTotalValue();
                        }
                        if (customer.getActiveDebts().size() > 0) {
                            for (int k = 0; k < customer.getActiveDebts().size(); k++) {
                                Debt debt = customer.getActiveDebts().get(k);
                                totalToDebts += debt.getTotalDebtAmount();
                                if (debt.getCustomerPayments().size() > 0) {
                                    for (int l = 0; l < debt.getCustomerPayments().size(); l++) {
                                        totalToDebts -= debt.getCustomerPayments().get(l).getPaymentAmount();
                                    }
                                }
                            }
                        }
                        firstObjects[i][0] = customer.getClientId();
                        firstObjects[i][1] = customer.getName();
                        firstObjects[i][2] = closedCount;
                        firstObjects[i][3] = canceledCount;
                        firstObjects[i][4] = totalOrdersAmount;
                        firstObjects[i][5] = totalToDebts;
                        firstObjects[i][6] = totalDiscounts;
                        firstObjects[i][7] = totalServiceFees;
                    }
                } else {
                    List<CustomerGroup> customerGroupList = databaseManager.getAllCustomerGroups().blockingSingle();
                    firstObjects = new Object[customerGroupList.size()][9];
                    for (int i = 0; i < customerGroupList.size(); i++) {
                        CustomerGroup customerGroup = customerGroupList.get(i);
                        int closedCount = 0;
                        int canceledCount = 0;
                        double totalOrdersAmount = 0;
                        double totalToDebts = 0;
                        double totalDiscounts = 0;
                        double totalServiceFees = 0;
                        for (int j = 0; j < customerGroup.getCustomers().size(); j++) {
                            Customer customer = customerGroup.getCustomers().get(j);
                            List<Order> orderList = databaseManager.getOrdersWithCustomerInInterval(customer.getId(), fromDate, toDate).blockingGet();
                            for (int c = 0; c < orderList.size(); c++) {
                                Order order = orderList.get(c);
                                if (order.getStatus() == Order.CLOSED_ORDER)
                                    closedCount++;
                                if (order.getStatus() == Order.CANCELED_ORDER)
                                    canceledCount++;
                                totalOrdersAmount += order.getSubTotalValue();
                                totalDiscounts += order.getDiscountTotalValue();
                                totalServiceFees += order.getServiceTotalValue();
                            }
                            if (customer.getActiveDebts().size() > 0) {
                                for (int k = 0; k < customer.getActiveDebts().size(); k++) {
                                    Debt debt = customer.getActiveDebts().get(k);
                                    totalToDebts += debt.getTotalDebtAmount();
                                    if (debt.getCustomerPayments().size() > 0) {
                                        for (int l = 0; l < debt.getCustomerPayments().size(); l++) {
                                            totalToDebts -= debt.getCustomerPayments().get(l).getPaymentAmount();
                                        }
                                    }
                                }
                            }
                        }
                        firstObjects[i][0] = customerGroup.getId();
                        firstObjects[i][1] = customerGroup.getName();
                        firstObjects[i][2] = closedCount;
                        firstObjects[i][3] = canceledCount;
                        firstObjects[i][4] = totalOrdersAmount;
                        firstObjects[i][5] = totalToDebts;
                        firstObjects[i][6] = totalDiscounts;
                        firstObjects[i][7] = totalServiceFees;
                    }
                }
                break;
            case 1:
                if (secondValue == 0) {
                    List<Order> orderList = new ArrayList<>();
                    databaseManager.getOrdersInIntervalForReport(fromDate, toDate).subscribe(orders -> {
                        for (Order order : orders) {
                            if (order.getCustomer() != null)
                                orderList.add(order);
                        }
                    });
                    secondObjects = new Object[orderList.size()][6];
                    for (int i = 0; i < orderList.size(); i++) {
                        Order order = orderList.get(i);
                        Customer customer = orderList.get(i).getCustomer();
                        secondObjects[i][0] = customer.getId();
                        secondObjects[i][1] = customer.getName();
                        secondObjects[i][2] = order.getCreateAt();
                        secondObjects[i][3] = order.getId();
                        secondObjects[i][4] = order.getStatus();
                        secondObjects[i][5] = order.getSubTotalValue();
                    }
                } else {
                    List<CustomerGroupOrder> groupOrders = new ArrayList<>();
                    databaseManager.getOrdersInIntervalForReport(fromDate, toDate).subscribe(orders -> {
                        for (Order order : orders) {
                            if (order.getCustomer() != null && order.getCustomer().getCustomerGroups().size() > 0) {
                                for (int i = 0; i < order.getCustomer().getCustomerGroups().size(); i++) {
                                    CustomerGroup group = order.getCustomer().getCustomerGroups().get(i);
                                    CustomerGroupOrder customerGroupOrder = new CustomerGroupOrder();
                                    customerGroupOrder.setId(group.getId());
                                    customerGroupOrder.setName(group.getName());
                                    customerGroupOrder.setDate(order.getCreateAt());
                                    customerGroupOrder.setOrderId(order.getId());
                                    customerGroupOrder.setOrderStatus(order.getStatus());
                                    customerGroupOrder.setTotalAmount(order.getSubTotalValue());
                                    groupOrders.add(customerGroupOrder);
                                }
                            }
                        }
                    });
                    secondObjects = new Object[groupOrders.size()][6];
                    for (int i = 0; i < groupOrders.size(); i++) {
                        CustomerGroupOrder order = groupOrders.get(i);
                        secondObjects[i][0] = order.getId();
                        secondObjects[i][1] = order.getName();
                        secondObjects[i][2] = order.getDate();
                        secondObjects[i][3] = order.getOrderId();
                        secondObjects[i][4] = order.getOrderStatus();
                        secondObjects[i][5] = order.getTotalAmount();
                    }
                }
                break;
            case 2:
                List<CustomerPaymentLog> paymentLogs = new ArrayList<>();
                databaseManager.getOrdersInIntervalForReport(fromDate, toDate).subscribe(orders -> {
                    for (Order order : orders) {
                        if (order.getCustomer() != null) {
                            if (filterConfig[0] == 1) {
                                for (int i = 0; i < order.getPayedPartitions().size(); i++) {
                                    PayedPartitions partitions = order.getPayedPartitions().get(i);
                                    CustomerPaymentLog paymentLog = new CustomerPaymentLog();
                                    paymentLog.setId(order.getCustomer().getId());
                                    paymentLog.setName(order.getCustomer().getName());
                                    paymentLog.setDate(order.getCreateAt());
                                    paymentLog.setPaymentType(partitions.getPaymentType().getName());
                                    paymentLog.setAmount(partitions.getValue());
                                    paymentLog.setReason(CustomerPaymentLog.FOR_ORDER);
                                    paymentLogs.add(paymentLog);
                                }
                            }
                            if (filterConfig[2] == 1) {
                                double change = order.getChange();
                                if (change > 0) {
                                    PaymentType paymentType = null;
                                    for (PaymentType paymentType1 : databaseManager.getPaymentTypes()) {
                                        if (paymentType1.getAccount().getStaticAccountType() == 1) {
                                            paymentType = paymentType1;
                                            break;
                                        }
                                    }
                                    CustomerPaymentLog paymentLog = new CustomerPaymentLog();
                                    paymentLog.setId(order.getCustomer().getId());
                                    paymentLog.setName(order.getCustomer().getName());
                                    paymentLog.setDate(order.getCreateAt());
                                    if (paymentType != null)
                                        paymentLog.setPaymentType(paymentType.getName());
                                    paymentLog.setAmount(change);
                                    paymentLog.setReason(CustomerPaymentLog.CHANGE);
                                    paymentLogs.add(paymentLog);
                                }
                            }
                            if (filterConfig[1] == 1) {
                                for (int i = 0; i < order.getCustomer().getDebtList().size(); i++) {
                                    Debt debt = order.getCustomer().getDebtList().get(i);
                                    if (debt.getOrder().getId().equals(order.getId())) {
                                        if (debt.getCustomerPayments().size() > 0) {
                                            for (int j = 0; j < debt.getCustomerPayments().size(); j++) {
                                                CustomerPayment payment = debt.getCustomerPayments().get(j);
                                                CustomerPaymentLog paymentLog = new CustomerPaymentLog();
                                                paymentLog.setId(order.getCustomer().getId());
                                                paymentLog.setName(order.getCustomer().getName());
                                                paymentLog.setDate(order.getCreateAt());
                                                paymentLog.setPaymentType(payment.getPaymentType().getName());
                                                paymentLog.setAmount(payment.getPaymentAmount());
                                                paymentLog.setReason(CustomerPaymentLog.FOR_DEBT);
                                                paymentLogs.add(paymentLog);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
                thirdObjects = new Object[paymentLogs.size()][6];
                Collections.sort(paymentLogs, (customerDebtLog, t1) -> customerDebtLog.getDate().compareTo(t1.getDate()));
                for (int i = 0; i < paymentLogs.size(); i++) {
                    CustomerPaymentLog paymentLog = paymentLogs.get(i);
                    thirdObjects[i][0] = paymentLog.getId();
                    thirdObjects[i][1] = paymentLog.getName();
                    thirdObjects[i][2] = paymentLog.getDate();
                    thirdObjects[i][3] = paymentLog.getReason();
                    thirdObjects[i][4] = paymentLog.getPaymentType();
                    thirdObjects[i][5] = paymentLog.getAmount();
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
                            if (String.valueOf((int) firstObjects[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((int) firstObjects[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
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
                            if ((decimalFormat.format((double) firstObjects[i][7])).toUpperCase().contains(searchText.toUpperCase())) {
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
                            if (String.valueOf((long) searchResultsTemp[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((int) searchResultsTemp[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((int) searchResultsTemp[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
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
                            if ((decimalFormat.format((double) searchResultsTemp[i][7])).toUpperCase().contains(searchText.toUpperCase())) {
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
                            if (String.valueOf((long) secondObjects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) secondObjects[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) secondObjects[i][2])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((long) secondObjects[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) secondObjects[i][5])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int orderStatus = (int) secondObjects[i][4];
                            if (orderStatus == Order.CLOSED_ORDER) {
                                text = context.getString(R.string.order_status_closed);
                            } else if (orderStatus == Order.HOLD_ORDER) {
                                text = context.getString(R.string.order_status_held);
                            } else if (orderStatus == Order.CANCELED_ORDER) {
                                text = context.getString(R.string.order_status_canceled);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }

                        }

                        int sumSize = 0;
                        for (int i = 0; i < secondObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][6];

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
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][2])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((long) searchResultsTemp[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][5])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int orderStatus = (int) searchResultsTemp[i][4];
                            if (orderStatus == Order.CLOSED_ORDER) {
                                text = context.getString(R.string.order_status_closed);
                            } else if (orderStatus == Order.HOLD_ORDER) {
                                text = context.getString(R.string.order_status_held);
                            } else if (orderStatus == Order.CANCELED_ORDER) {
                                text = context.getString(R.string.order_status_canceled);
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
                case 2:
                    if (searchText.length() <= prev || prev == -1) {
                        int searchRes[] = new int[thirdObjects.length];
                        for (int i = 0; i < thirdObjects.length; i++) {
                            if (String.valueOf((long) thirdObjects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) thirdObjects[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) thirdObjects[i][2])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) thirdObjects[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) thirdObjects[i][5])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int orderStatus = (int) thirdObjects[i][3];
                            if (orderStatus == CustomerPaymentLog.FOR_ORDER) {
                                text = context.getString(R.string.for_order);
                            } else if (orderStatus == CustomerPaymentLog.FOR_DEBT) {
                                text = context.getString(R.string.for_debt);
                            } else if (orderStatus == CustomerPaymentLog.CHANGE) {
                                text = context.getString(R.string.change);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }

                        }

                        int sumSize = 0;
                        for (int i = 0; i < thirdObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][6];

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
                            if (String.valueOf((long) searchResultsTemp[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][2])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][5])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int orderStatus = (int) searchResultsTemp[i][3];
                            if (orderStatus == CustomerPaymentLog.FOR_ORDER) {
                                text = context.getString(R.string.for_order);
                            } else if (orderStatus == CustomerPaymentLog.FOR_DEBT) {
                                text = context.getString(R.string.for_debt);
                            } else if (orderStatus == CustomerPaymentLog.CHANGE) {
                                text = context.getString(R.string.change);
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
                view.showSummaryFilterDialog(filterValue);
                break;
            case 1:
                view.showSummaryFilterDialog(secondValue);
                break;
            case 2:
                view.showFilterDialog(filterConfig);
                break;
        }
    }

    @Override
    public void onChoisedPanel(int position) {
        this.currentPosition = position;
        updateTable();
    }

    @Override
    public void onTillPickerClicked() {

    }

    @Override
    public void changeSummaryConfig(int config) {
        if (currentPosition == 0)
            this.filterValue = config;
        else
            this.secondValue = config;
        updateTable();
    }

    @Override
    public void changePaymentFilter(int[] config) {
        this.filterConfig = config;
        updateTable();
    }

    @Override
    public void exportExcel(String fileName, String path) {
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        String filter = "";
        switch (currentPosition) {
            case 0:
                if (filterValue == 0)
                    filter = context.getString(R.string.customer);
                else filter = context.getString(R.string.customer_groups);
                view.exportTableToExcel(fileName, path, firstObjects, currentPosition, date, filter, searchText);
                break;
            case 1:
                if (secondValue == 0)
                    filter = context.getString(R.string.customer);
                else filter = context.getString(R.string.customer_groups);
                view.exportTableToExcel(fileName, path, secondObjects, currentPosition, date, filter, searchText);
                break;
            case 2:
                StringBuilder filters = new StringBuilder();
                if (filterConfig[0] == 1) {
                    filters.append(context.getString(R.string.order_status_closed)).append(" ");
                }
                if (filterConfig[1] == 1) {
                    filters.append(context.getString(R.string.order_status_held)).append(" ");
                }
                if (filterConfig[2] == 1) {
                    filters.append(context.getString(R.string.order_status_canceled));
                }
                filter = filters.toString();
                view.exportTableToExcel(fileName, path, thirdObjects, currentPosition, date, filter, searchText);
                break;
        }
    }

    @Override
    public void exportPdf(String fileName, String path) {
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        String filter = "";
        switch (currentPosition) {
            case 0:
                if (filterValue == 0)
                    filter = context.getString(R.string.customer);
                else filter = context.getString(R.string.customer_groups);
                view.exportTableToPdf(fileName, path, firstObjects, currentPosition, date, filter, searchText);
                break;
            case 1:
                if (secondValue == 0)
                    filter = context.getString(R.string.customer);
                else filter = context.getString(R.string.customer_groups);
                view.exportTableToPdf(fileName, path, secondObjects, currentPosition, date, filter, searchText);
                break;
            case 2:
                StringBuilder filters = new StringBuilder();
                if (filterConfig[0] == 1) {
                    filters.append(context.getString(R.string.order_status_closed)).append(" ");
                }
                if (filterConfig[1] == 1) {
                    filters.append(context.getString(R.string.order_status_held)).append(" ");
                }
                if (filterConfig[2] == 1) {
                    filters.append(context.getString(R.string.order_status_canceled));
                }
                filter = filters.toString();
                view.exportTableToPdf(fileName, path, thirdObjects, currentPosition, date, filter, searchText);
                break;
        }
    }

    @Override
    public void exportExcelToUSB(String filename, UsbFile root) {
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        String filter = "";
        switch (currentPosition) {
            case 0:
                if (filterValue == 0)
                    filter = context.getString(R.string.customer);
                else filter = context.getString(R.string.customer_groups);
                view.exportExcelToUSB(filename, root, firstObjects, currentPosition, date, filter, searchText);
                break;
            case 1:
                if (secondValue == 0)
                    filter = context.getString(R.string.customer);
                else filter = context.getString(R.string.customer_groups);
                view.exportExcelToUSB(filename, root, secondObjects, currentPosition, date, filter, searchText);
                break;
            case 2:
                StringBuilder filters = new StringBuilder();
                if (filterConfig[0] == 1) {
                    filters.append(context.getString(R.string.order_status_closed)).append(" ");
                }
                if (filterConfig[1] == 1) {
                    filters.append(context.getString(R.string.order_status_held)).append(" ");
                }
                if (filterConfig[2] == 1) {
                    filters.append(context.getString(R.string.order_status_canceled));
                }
                filter = filters.toString();
                view.exportExcelToUSB(filename, root, thirdObjects, currentPosition, date, filter, searchText);
                break;
        }
    }

    @Override
    public void exportPdfToUSB(String fileName, UsbFile path) {
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        String filter = "";
        switch (currentPosition) {
            case 0:
                if (filterValue == 0)
                    filter = context.getString(R.string.customer);
                else filter = context.getString(R.string.customer_groups);
                view.exportTableToPdfToUSB(fileName, path, firstObjects, currentPosition, date, filter, searchText);
                break;
            case 1:
                if (secondValue == 0)
                    filter = context.getString(R.string.customer);
                else filter = context.getString(R.string.customer_groups);
                view.exportTableToPdfToUSB(fileName, path, secondObjects, currentPosition, date, filter, searchText);
                break;
            case 2:
                StringBuilder filters = new StringBuilder();
                if (filterConfig[0] == 1) {
                    filters.append(context.getString(R.string.order_status_closed)).append(" ");
                }
                if (filterConfig[1] == 1) {
                    filters.append(context.getString(R.string.order_status_held)).append(" ");
                }
                if (filterConfig[2] == 1) {
                    filters.append(context.getString(R.string.order_status_canceled));
                }
                filter = filters.toString();
                view.exportTableToPdfToUSB(fileName, path, thirdObjects, currentPosition, date, filter, searchText);
                break;
        }
    }


    @Override
    public void onBarcodeReaded(String barcode) {
        databaseManager.getAllCustomers().subscribe(customers -> {
            for(Customer customer:customers)
                if(customer.getQrCode() !=null && customer.getQrCode().equals(barcode)){
                    view.setTextToSearch(customer.getName());
                }
        });
    }
}
