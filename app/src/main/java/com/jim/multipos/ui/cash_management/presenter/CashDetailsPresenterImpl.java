package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.CustomerPayment;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillOperation;
import com.jim.multipos.ui.cash_management.view.CashDetailsView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 12.01.2018.
 */

public class CashDetailsPresenterImpl extends BasePresenterImpl<CashDetailsView> implements CashDetailsPresenter {

    private DatabaseManager databaseManager;
    private List<TillOperation> allOperations;

    private Account account;
    private Till till;

    @Inject
    protected CashDetailsPresenterImpl(CashDetailsView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
        allOperations = new ArrayList<>();
    }

    @Override
    public void calculateCashDetails(Account account, Till till) {
        this.account = account;
        this.till = till;
        double payIn = 0, payOut = 0, bankDrop = 0, payToVendor = 0, incomeDebt = 0, cashTransactions = 0, tips = 0;
        if (till != null) {
            allOperations = databaseManager.getTillOperationsByAccountId(account.getId(), till.getId()).blockingGet();

            if (!allOperations.isEmpty()) {
                for (int i = 0; i < allOperations.size(); i++) {
                    if (allOperations.get(i).getType() == TillOperation.PAY_IN)
                        payIn += allOperations.get(i).getAmount();
                    if (allOperations.get(i).getType() == TillOperation.PAY_OUT)
                        payOut += allOperations.get(i).getAmount();
                    if (allOperations.get(i).getType() == TillOperation.BANK_DROP)
                        bankDrop += allOperations.get(i).getAmount();
                }
            }
            Calendar fromDate = new GregorianCalendar(), toDate = new GregorianCalendar();
            if (till.getStatus() == Till.OPEN) {
                fromDate.setTimeInMillis(till.getOpenDate());
                toDate.setTimeInMillis(System.currentTimeMillis());
                List<BillingOperations> billingOperationsList = databaseManager.getBillingOperationsByInterval(fromDate, toDate).blockingGet();
                for (int i = 0; i < billingOperationsList.size(); i++) {
                    if (billingOperationsList.get(i).getAccount() != null && billingOperationsList.get(i).getAccount().getId().equals(account.getId()))
                        payToVendor += billingOperationsList.get(i).getAmount();
                }

                List<CustomerPayment> customerPayments = databaseManager.getCustomerPaymentsByInterval(fromDate, toDate).blockingGet();
                for (int i = 0; i < customerPayments.size(); i++) {
                    if (customerPayments.get(i).getPaymentType().getAccount().getId().equals(account.getId()))
                        incomeDebt += customerPayments.get(i).getPaymentAmount();
                }

                List<Order> ordersList = databaseManager.getAllTillOrders().blockingGet();
                for (int i = 0; i < ordersList.size(); i++) {
                    Order order = ordersList.get(i);
                    for (int j = 0; j < order.getPayedPartitions().size(); j++) {
                        PayedPartitions payedPartitions = order.getPayedPartitions().get(j);
                        if (payedPartitions.getPaymentType().getAccount().getId().equals(account.getId())) {
                            cashTransactions += payedPartitions.getValue();
                        }
                    }
                    double change = order.getChange();
                    if (change > 0) {
                        if (account.getCirculation() == 0)
                            cashTransactions -= change;
                    }
                    tips += order.getTips();
                }

            } else {
                fromDate.setTimeInMillis(till.getOpenDate());
                toDate.setTimeInMillis(till.getCloseDate());
                List<BillingOperations> billingOperationsList = databaseManager.getBillingOperationsByInterval(fromDate, toDate).blockingGet();
                for (int i = 0; i < billingOperationsList.size(); i++) {
                    if (billingOperationsList.get(i).getAccount().getId().equals(account.getId()))
                        payToVendor += billingOperationsList.get(i).getAmount();
                }

                List<CustomerPayment> customerPayments = databaseManager.getCustomerPaymentsByInterval(fromDate, toDate).blockingGet();
                for (int i = 0; i < customerPayments.size(); i++) {
                    if (customerPayments.get(i).getPaymentType().getAccount().getId().equals(account.getId()))
                        incomeDebt += customerPayments.get(i).getPaymentAmount();
                }
            }

            double expectedCash = payIn - payOut - payToVendor + incomeDebt - bankDrop + cashTransactions;

            view.fillTillDetails(payOut, payIn, payToVendor, incomeDebt, bankDrop, expectedCash, tips, cashTransactions);
        }

    }

    @Override
    public void updateDetails() {
        calculateCashDetails(account, till);
    }
}
