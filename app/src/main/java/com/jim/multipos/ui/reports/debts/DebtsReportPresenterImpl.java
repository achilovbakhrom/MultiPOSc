package com.jim.multipos.ui.reports.debts;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerPayment;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.order.Order;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

public class DebtsReportPresenterImpl extends BasePresenterImpl<DebtsReportView> implements DebtsReportPresenter {

    private DatabaseManager databaseManager;
    private Object[][] firstObjects;
    private Object[][] secondObjects;
    private Object[][] thirdObjects;
    private Object[][] forthObjects;
    private int currentPosition = 0;
    private Calendar fromDate;
    private Calendar toDate;

    @Inject
    protected DebtsReportPresenterImpl(DebtsReportView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        initReportTable();
        view.initTable(firstObjects);
    }

    private void initReportTable() {
        switch (currentPosition) {
            case 0:
                List<Customer> customers = new ArrayList<>();
                databaseManager.getAllCustomers().subscribe(customers1 -> {
                    for (int i = 0; i < customers1.size(); i++) {
                        if (customers1.get(i).getDebtList().size() > 0)
                            customers.add(customers1.get(i));
                    }
                });
                firstObjects = new Object[customers.size()][5];
                for (int i = 0; i < customers.size(); i++) {
                    Customer customer = customers.get(i);
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
                                if (debt.getCustomerPayments().size() > 0)
                                    for (CustomerPayment payment : debt.getCustomerPayments()) {
                                        totalOverdue -= total - payment.getPaymentAmount();
                                    }
                            }
                        }
                    }
                    firstObjects[i][0] = customer.getName();
                    firstObjects[i][1] = totalDebt;
                    firstObjects[i][2] = totalOverdue;
                    firstObjects[i][3] = totalOverdue;
                    firstObjects[i][4] = customer.getPhoneNumber();
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
    }

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

    }

    @Override
    public void onChoisedPanel(int position) {
        this.currentPosition = position;
        switch (position) {
            case 0:
                initReportTable();
                view.updateTable(firstObjects, currentPosition);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
}
