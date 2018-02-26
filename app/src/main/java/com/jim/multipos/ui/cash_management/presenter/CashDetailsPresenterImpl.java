package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillDetails;
import com.jim.multipos.data.db.model.till.TillManagementOperation;
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
        double payIn, payOut, bankDrop, payToVendor, incomeDebt, cashTransactions = 0, tips = 0, totalStartingCash;
        if (till != null) {
            if (till.getStatus() == Till.OPEN) {
                totalStartingCash = databaseManager.getTotalTillManagementOperationsAmount(account.getId(), till.getId(), TillManagementOperation.OPENED_WITH).blockingGet();
                payIn = databaseManager.getTotalTillOperationsAmount(account.getId(), till.getId(), TillOperation.PAY_IN).blockingGet();
                payOut = databaseManager.getTotalTillOperationsAmount(account.getId(), till.getId(), TillOperation.PAY_OUT).blockingGet();
                bankDrop = databaseManager.getTotalTillOperationsAmount(account.getId(), till.getId(), TillOperation.BANK_DROP).blockingGet();

                Calendar fromDate = new GregorianCalendar(), toDate = new GregorianCalendar();
                fromDate.setTimeInMillis(till.getOpenDate());
                toDate.setTimeInMillis(System.currentTimeMillis());
                payToVendor = databaseManager.getBillingOperationsAmountInInterval(account.getId(), fromDate, toDate).blockingGet();
                incomeDebt = databaseManager.getCustomerPaymentsInInterval(account.getId(), fromDate, toDate).blockingGet();
                List<Order> ordersList = databaseManager.getOrdersByTillId(till.getId()).blockingGet();
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
                        if (account.getStaticAccountType() == 1)
                            cashTransactions -= change;
                    }
                    tips += order.getTips();
                }
            } else {
                TillDetails tillDetails = databaseManager.getTillDetailsByAccountId(account.getId(), till.getId()).blockingGet();
                payIn = tillDetails.getTotalPayIns();
                payOut = tillDetails.getTotalPayOuts();
                bankDrop = tillDetails.getTotalBankDrops();
                payToVendor = tillDetails.getTotalPayToVendors();
                incomeDebt = tillDetails.getTotalDebtIncome();
                cashTransactions = tillDetails.getTotalStartingCash();
                tips = tillDetails.getTips();
                totalStartingCash = tillDetails.getTotalStartingCash();
            }
            double expectedCash = payIn - payOut - payToVendor + incomeDebt - bankDrop + cashTransactions + totalStartingCash;
            view.fillTillDetails(totalStartingCash, payOut, payIn, payToVendor, incomeDebt, bankDrop, expectedCash, tips, cashTransactions);
        }

    }

    @Override
    public void updateDetails() {
        calculateCashDetails(account, till);
    }
}
