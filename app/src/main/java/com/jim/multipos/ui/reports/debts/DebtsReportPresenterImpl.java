package com.jim.multipos.ui.reports.debts;

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
import com.jim.multipos.ui.reports.debts.model.CustomerDebtLog;

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

public class DebtsReportPresenterImpl extends BasePresenterImpl<DebtsReportView> implements DebtsReportPresenter {

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
    protected DebtsReportPresenterImpl(DebtsReportView view, DatabaseManager databaseManager, Context context) {
        super(view);
        this.databaseManager = databaseManager;
        this.context = context;
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat1.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat1.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat = decimalFormat1;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        filterConfig = new int[]{1, 1};
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
                List<Customer> customers = new ArrayList<>();
                databaseManager.getCustomers().subscribe(customers1 -> {
                    for (int i = 0; i < customers1.size(); i++) {
                        customers1.get(i).resetDebtList();
                        if (customers1.get(i).getDebtList().size() > 0)
                            customers.add(customers1.get(i));
                    }
                });
                firstObjects = new Object[customers.size()][5];
                for (int i = 0; i < customers.size(); i++) {
                    Customer customer = customers.get(i);
                    customer.resetDebtList();
                    double totalDebt = 0;
                    double totalOverdue = 0;
                    for (int j = 0; j < customer.getDebtList().size(); j++) {
                        Debt debt = customer.getDebtList().get(j);
                        double total = debt.getDebtAmount() + (debt.getFee() * debt.getDebtAmount() / 100);
                        totalDebt += total;
                        if (debt.getStatus() == Debt.ACTIVE) {
                            GregorianCalendar now = new GregorianCalendar();
                            Date date = new Date(debt.getEndDate());
                            GregorianCalendar calendar = new GregorianCalendar();
                            calendar.setTime(date);
                            if (now.after(calendar) || now.equals(calendar)) {
                                totalOverdue += total;
                                if (debt.getCustomerPayments().size() > 0) {
                                    for (CustomerPayment payment : debt.getCustomerPayments()) {
                                        totalOverdue -= payment.getPaymentAmount();
                                    }
                                }
                            }
                        }
                    }
                    Order order = databaseManager.getLastOrderWithCustomer(customer.getId()).blockingGet();
                    firstObjects[i][0] = customer.getName();
                    firstObjects[i][1] = totalDebt;
                    firstObjects[i][2] = totalOverdue;
                    firstObjects[i][3] = order.getCreateAt();
                    firstObjects[i][4] = customer.getPhoneNumber();
                }
                break;
            case 1:
                List<Customer> customersList = new ArrayList<>();
                databaseManager.getCustomers().subscribe(customers1 -> {
                    for (int i = 0; i < customers1.size(); i++) {
                        customers1.get(i).resetDebtList();
                        if (customers1.get(i).getDebtList().size() > 0)
                            customersList.add(customers1.get(i));
                    }
                });
                secondObjects = new Object[customersList.size()][6];
                for (int i = 0; i < customersList.size(); i++) {
                    Customer customer = customersList.get(i);
                    customer.resetDebtList();
                    double totalDebt = 0;
                    double totalOverdue = 0;
                    int debtOrdersCount = 0;
                    double avgDebtTaken = 0;
                    double avgDebtClosed = 0;
                    int paymentsSize = 0;
                    for (int j = 0; j < customer.getDebtList().size(); j++) {
                        Debt debt = customer.getDebtList().get(j);
                        GregorianCalendar takenDate = new GregorianCalendar();
                        takenDate.setTime(new Date(debt.getTakenDate()));
                        if ((takenDate.after(fromDate) || fromDate.equals(takenDate)) && (takenDate.before(toDate) || toDate.equals(takenDate))) {
                            double total = debt.getDebtAmount() + (debt.getFee() * debt.getDebtAmount() / 100);
                            totalDebt += total;
                            if (debt.getCustomerPayments().size() > 0) {
                                for (CustomerPayment payment : debt.getCustomerPayments()) {
                                    totalOverdue += payment.getPaymentAmount();
                                    paymentsSize++;
                                }
                            }
                            debtOrdersCount++;
                        }
                    }
                    if (totalDebt == 0) {
                        avgDebtTaken = 0;
                    } else avgDebtTaken = totalDebt / debtOrdersCount;
                    if (totalOverdue == 0) {
                        avgDebtClosed = 0;
                    } else avgDebtClosed = totalOverdue / paymentsSize;
                    secondObjects[i][0] = customer.getName();
                    secondObjects[i][1] = totalDebt;
                    secondObjects[i][2] = totalOverdue;
                    secondObjects[i][3] = debtOrdersCount;
                    secondObjects[i][4] = avgDebtTaken;
                    secondObjects[i][5] = avgDebtClosed;
                }
                break;
            case 2:
                List<CustomerDebtLog> customerDebtLogs = new ArrayList<>();
                databaseManager.getAllCustomerDebtsInInterval(fromDate, toDate).subscribe(debts -> {
                    for (Debt debt : debts) {
                        CustomerDebtLog customerDebtLog = new CustomerDebtLog();
                        customerDebtLog.setName(debt.getCustomer().getName());
                        customerDebtLog.setAction(CustomerDebtLog.DEBT_TAKEN);
                        customerDebtLog.setAmount(debt.getTotalDebtAmount());
                        customerDebtLog.setDate(debt.getTakenDate());
                        customerDebtLog.setOrderId(debt.getOrderId());
                        if (debt.getCustomer().getCustomerGroups().size() > 0) {
                            StringBuilder customerGroupsName = new StringBuilder();
                            for (CustomerGroup customerGroup : debt.getCustomer().getCustomerGroups()) {
                                if (customerGroupsName.length() == 0)
                                    customerGroupsName = new StringBuilder(customerGroup.getName());
                                else
                                    customerGroupsName.append(", ").append(customerGroup.getName());
                            }
                            customerDebtLog.setGroups(customerGroupsName.toString());

                        }
                        if (filterConfig[0] == 1) {
                            customerDebtLogs.add(customerDebtLog);
                        }
                        if (debt.getStatus() == Debt.CLOSED) {
                            CustomerDebtLog log = new CustomerDebtLog();
                            log.setName(debt.getCustomer().getName());
                            log.setAction(CustomerDebtLog.DEBT_CLOSED);
                            log.setAmount(debt.getTotalDebtAmount());
                            log.setDate(debt.getClosedDate());
                            log.setOrderId(debt.getOrderId());
                            if (debt.getCustomer().getCustomerGroups().size() > 0) {
                                StringBuilder customerGroupsName = new StringBuilder();
                                for (CustomerGroup customerGroup : debt.getCustomer().getCustomerGroups()) {
                                    if (customerGroupsName.length() == 0)
                                        customerGroupsName = new StringBuilder(customerGroup.getName());
                                    else
                                        customerGroupsName.append(", ").append(customerGroup.getName());
                                }
                                log.setGroups(customerGroupsName.toString());
                            }
                            if (debt.getCustomerPayments().size() > 0) {
                                List<PaymentType> paymentTypeList = new ArrayList<>();
                                for (CustomerPayment payment : debt.getCustomerPayments()) {
                                    if (!paymentTypeList.contains(payment.getPaymentType()))
                                        paymentTypeList.add(payment.getPaymentType());
                                }
                                StringBuilder paymentTypes = new StringBuilder();
                                for (PaymentType paymentType : paymentTypeList) {
                                    if (paymentTypes.length() == 0)
                                        paymentTypes = new StringBuilder(paymentType.getName());
                                    else paymentTypes.append(", ").append(paymentType.getName());
                                }
                                log.setPaymentTypes(paymentTypes.toString());
                            }
                            if (filterConfig[1] == 1) {
                                customerDebtLogs.add(log);
                            }
                        }
                    }
                });
                Collections.sort(customerDebtLogs, (customerDebtLog, t1) -> customerDebtLog.getDate().compareTo(t1.getDate()));
                thirdObjects = new Object[customerDebtLogs.size()][7];
                for (int i = 0; i < customerDebtLogs.size(); i++) {
                    CustomerDebtLog customerDebtLog = customerDebtLogs.get(i);
                    thirdObjects[i][0] = customerDebtLog.getName();
                    thirdObjects[i][1] = customerDebtLog.getDate();
                    thirdObjects[i][2] = customerDebtLog.getOrderId();
                    thirdObjects[i][3] = customerDebtLog.getAction();
                    thirdObjects[i][4] = customerDebtLog.getAmount();
                    thirdObjects[i][5] = customerDebtLog.getPaymentTypes();
                    thirdObjects[i][6] = customerDebtLog.getGroups();
                }
                break;
            case 3:
                List<Order> orders = new ArrayList<>();
                databaseManager.getOrdersInIntervalForReport(fromDate, toDate).subscribe(orders1 -> {
                    for (Order order : orders1) {
                        if (order.getDebt() != null)
                            orders.add(order);
                    }
                });
                forthObjects = new Object[orders.size()][9];
                for (int i = 0; i < orders.size(); i++) {
                    Order order = orders.get(i);
                    Debt debt = order.getDebt();
                    double paid = 0;
                    long lastPaymentDate = 0;
                    if (debt.getCustomerPayments().size() > 0)
                        lastPaymentDate = debt.getCustomerPayments().get(debt.getCustomerPayments().size() - 1).getPaymentDate();
                    if (debt.getCustomerPayments().size() > 0) {
                        for (CustomerPayment payment : debt.getCustomerPayments()) {
                            paid += payment.getPaymentAmount();
                        }
                    }
                    forthObjects[i][0] = order.getId();
                    forthObjects[i][1] = order.getCreateAt();
                    forthObjects[i][2] = order.getDebt().getTotalDebtAmount();
                    forthObjects[i][3] = paid;
                    if (lastPaymentDate == 0)
                        forthObjects[i][4] = lastPaymentDate + "";
                    else
                        forthObjects[i][4] = lastPaymentDate;
                    forthObjects[i][5] = order.getDebt().getTotalDebtAmount() - paid;
                    forthObjects[i][6] = order.getCustomer().getName();
                    forthObjects[i][7] = debt.getEndDate();
                    forthObjects[i][8] = debt.getFee();
                }
                break;
            case 4:
                List<Debt> debts = databaseManager.getAllCustomerDebtsInInterval(fromDate, toDate).blockingGet();
                fifthObjects = new Object[debts.size()][11];
                for (int i = 0; i < debts.size(); i++) {
                    Debt debt = debts.get(i);
                    fifthObjects[i][0] = debt.getId();
                    fifthObjects[i][1] = debt.getTakenDate();
                    fifthObjects[i][2] = debt.getEndDate();
                    fifthObjects[i][3] = debt.getClosedDate();
                    fifthObjects[i][4] = debt.getCustomer().getName();
                    fifthObjects[i][5] = debt.getOrder().getId();
                    fifthObjects[i][6] = debt.getDebtType();
                    fifthObjects[i][7] = debt.getFee();
                    fifthObjects[i][8] = debt.getDebtAmount();
                    fifthObjects[i][9] = debt.getStatus();
                    fifthObjects[i][10] = debt.getTotalDebtAmount();
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
                case 4:
                    view.updateTable(fifthObjects, currentPosition);
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
                            if (simpleDateFormat.format(new Date((long) firstObjects[i][3])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) firstObjects[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < firstObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][5];

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
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][3])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
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
                case 1:
                    if (searchText.length() <= prev || prev == -1) {
                        int searchRes[] = new int[secondObjects.length];
                        for (int i = 0; i < secondObjects.length; i++) {
                            if (((String) secondObjects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) secondObjects[i][1])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) secondObjects[i][2])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((double) secondObjects[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) secondObjects[i][4])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) secondObjects[i][5])).toUpperCase().contains(searchText.toUpperCase())) {
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
                            if (String.valueOf((double) searchResultsTemp[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
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
                            if (((String) thirdObjects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) thirdObjects[i][1])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((long) thirdObjects[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) thirdObjects[i][4])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) thirdObjects[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) thirdObjects[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String type = "";
                            int debtType = (int) thirdObjects[i][3];
                            if (debtType == CustomerDebtLog.DEBT_CLOSED) {
                                type = context.getString(R.string.debt_closed);
                            } else if (debtType == CustomerDebtLog.DEBT_TAKEN) {
                                type = context.getString(R.string.debt_taken);
                            }
                            if (type.toUpperCase().contains(searchText.toUpperCase())) {
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
                            if (String.valueOf((long) searchResultsTemp[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
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
                            if (((String) searchResultsTemp[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String type = "";
                            int debtType = (int) searchResultsTemp[i][3];
                            if (debtType == CustomerDebtLog.DEBT_CLOSED) {
                                type = context.getString(R.string.debt_closed);
                            } else if (debtType == CustomerDebtLog.DEBT_TAKEN) {
                                type = context.getString(R.string.debt_taken);
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
                case 3:
                    if (searchText.length() <= prev || prev == -1) {
                        int searchRes[] = new int[forthObjects.length];
                        for (int i = 0; i < forthObjects.length; i++) {
                            if (String.valueOf((long) forthObjects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
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
                            if (forthObjects[i][4] instanceof String) {
                                if (((String) forthObjects[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                                    searchRes[i] = 1;
                                    continue;
                                }
                            } else {
                                if (simpleDateFormat.format(new Date((long) forthObjects[i][4])).contains(searchText.toUpperCase())) {
                                    searchRes[i] = 1;
                                    continue;
                                }
                            }
                            if ((decimalFormat.format((double) forthObjects[i][5])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }

                            if (((String) forthObjects[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((simpleDateFormat.format(new Date((long) forthObjects[i][7]))).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                            }
                            if ((decimalFormat.format((double) forthObjects[i][8])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < forthObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][9];

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
                            if (searchResultsTemp[i][4] instanceof String) {
                                if (((String) searchResultsTemp[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                                    searchRes[i] = 1;
                                    continue;
                                }
                            } else {
                                if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][4])).contains(searchText.toUpperCase())) {
                                    searchRes[i] = 1;
                                    continue;
                                }
                            }
                            if ((decimalFormat.format((double) searchResultsTemp[i][5])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }

                            if (((String) searchResultsTemp[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((simpleDateFormat.format(new Date((long) searchResultsTemp[i][7]))).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                            }

                            if ((decimalFormat.format((double) searchResultsTemp[i][8])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
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
                            if (simpleDateFormat.format(new Date((long) fifthObjects[i][1])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) fifthObjects[i][2])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (fifthObjects[i][3] instanceof Long){
                                if (simpleDateFormat.format(new Date((long) fifthObjects[i][3])).contains(searchText.toUpperCase())) {
                                    searchRes[i] = 1;
                                    continue;
                                }
                            } else {
                                if (((String) fifthObjects[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                    searchRes[i] = 1;
                                    continue;
                                }
                            }
                            if (((String) fifthObjects[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((long) fifthObjects[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) fifthObjects[i][7])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) fifthObjects[i][8])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if ((decimalFormat.format((double) fifthObjects[i][10])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String type = "";
                            int debtType = (int) fifthObjects[i][6];
                            if (debtType == Debt.PARTICIPLE) {
                                type = context.getString(R.string.part_payment);
                            } else if (debtType == Debt.ALL) {
                                type = context.getString(R.string.all);
                            }
                            if (type.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String status = "";
                            int statusType = (int) fifthObjects[i][9];
                            if (statusType == Debt.CLOSED) {
                                status = context.getString(R.string.closed);
                            } else if (statusType == Debt.ACTIVE) {
                                status = context.getString(R.string.active);
                            }
                            if (status.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < fifthObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][11];

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
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][1])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][2])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (searchResultsTemp[i][3] instanceof Long){
                                if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][3])).contains(searchText.toUpperCase())) {
                                    searchRes[i] = 1;
                                    continue;
                                }
                            } else {
                                if (((String) searchResultsTemp[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                    searchRes[i] = 1;
                                    continue;
                                }
                            }
                            if (((String) searchResultsTemp[i][4]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (String.valueOf((long) searchResultsTemp[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
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
                            if ((decimalFormat.format((double) searchResultsTemp[i][10])).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String type = "";
                            int debtType = (int) searchResultsTemp[i][6];
                            if (debtType == Debt.PARTICIPLE) {
                                type = context.getString(R.string.part_payment);
                            } else if (debtType == Debt.ALL) {
                                type = context.getString(R.string.all);
                            }
                            if (type.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String status = "";
                            int statusType = (int) searchResultsTemp[i][9];
                            if (statusType == Debt.CLOSED) {
                                status = context.getString(R.string.closed);
                            } else if (statusType == Debt.ACTIVE) {
                                status = context.getString(R.string.active);
                            }
                            if (status.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }
                        int sumSize = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][11];

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
    public void filterConfigsHaveChanged(int[] config) {
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
                StringBuilder filters = new StringBuilder();
                if (filterConfig[0] == 1) {
                    filters.append(context.getString(R.string.debt_taken)).append(" ");
                }
                if (filterConfig[1] == 1) {
                    filters.append(context.getString(R.string.debt_closed)).append(" ");
                }
                filter = filters.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToExcel(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToExcel(fileName, path, thirdObjects, currentPosition, date, filter, searchText);
                break;
            case 3:
                if (searchResultsTemp != null) {
                    view.exportTableToExcel(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToExcel(fileName, path, forthObjects, currentPosition, date, filter, searchText);
                break;
            case 4:
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
                if (searchResultsTemp != null) {
                    view.exportTableToPdf(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdf(fileName, path, secondObjects, currentPosition, date, filter, searchText);
                break;
            case 2:
                StringBuilder filters = new StringBuilder();
                if (filterConfig[0] == 1) {
                    filters.append(context.getString(R.string.debt_taken)).append(" ");
                }
                if (filterConfig[1] == 1) {
                    filters.append(context.getString(R.string.debt_closed)).append(" ");
                }
                filter = filters.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToPdf(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdf(fileName, path, thirdObjects, currentPosition, date, filter, searchText);
                break;
            case 3:
                if (searchResultsTemp != null) {
                    view.exportTableToPdf(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdf(fileName, path, forthObjects, currentPosition, date, filter, searchText);
                break;
            case 4:
                if (searchResultsTemp != null) {
                    view.exportTableToPdf(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdf(fileName, path, fifthObjects, currentPosition, date, filter, searchText);
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
                StringBuilder filters = new StringBuilder();
                if (filterConfig[0] == 1) {
                    filters.append(context.getString(R.string.debt_taken)).append(" ");
                }
                if (filterConfig[1] == 1) {
                    filters.append(context.getString(R.string.debt_closed)).append(" ");
                }
                filter = filters.toString();
                if (searchResultsTemp != null) {
                    view.exportExcelToUSB(filename, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportExcelToUSB(filename, root, thirdObjects, currentPosition, date, filter, searchText);
                break;
            case 3:
                if (searchResultsTemp != null) {
                    view.exportExcelToUSB(filename, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportExcelToUSB(filename, root, forthObjects, currentPosition, date, filter, searchText);
                break;
            case 4:
                if (searchResultsTemp != null) {
                    view.exportExcelToUSB(filename, root, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportExcelToUSB(filename, root, fifthObjects, currentPosition, date, filter, searchText);
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
                StringBuilder filters = new StringBuilder();
                if (filterConfig[0] == 1) {
                    filters.append(context.getString(R.string.debt_taken)).append(" ");
                }
                if (filterConfig[1] == 1) {
                    filters.append(context.getString(R.string.debt_closed)).append(" ");
                }
                filter = filters.toString();
                if (searchResultsTemp != null) {
                    view.exportTableToPdfToUSB(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdfToUSB(fileName, path, thirdObjects, currentPosition, date, filter, searchText);
                break;
            case 3:
                if (searchResultsTemp != null) {
                    view.exportTableToPdfToUSB(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdfToUSB(fileName, path, forthObjects, currentPosition, date, filter, searchText);
                break;
            case 4:
                if (searchResultsTemp != null) {
                    view.exportTableToPdfToUSB(fileName, path, searchResultsTemp, currentPosition, date, filter, searchText);
                } else
                    view.exportTableToPdfToUSB(fileName, path, fifthObjects, currentPosition, date, filter, searchText);
                break;
        }
    }

    @Override
    public void onAction(Object[][] objects, int row, int column) {
        if (currentPosition == 2) {
            if (column == 2) {
                if (!objects[row][column].equals("")) {
                    long orderId = (long) objects[row][column];
                    databaseManager.getOrder(orderId).subscribe(order -> {
                        view.onOrderPressed(order);
                    });
                }
            }
        } else if (currentPosition == 3){
            if (column == 0) {
                if (!objects[row][column].equals("")) {
                    long orderId = (long) objects[row][column];
                    databaseManager.getOrder(orderId).subscribe(order -> {
                        view.onOrderPressed(order);
                    });
                }
            }
        } else if (currentPosition == 4){
            if (column == 5) {
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
    public void onChoisedPanel(int position) {
        this.currentPosition = position;
        view.clearSearch();
        updateTable();
    }

    @Override
    public void onTillPickerClicked() {

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
