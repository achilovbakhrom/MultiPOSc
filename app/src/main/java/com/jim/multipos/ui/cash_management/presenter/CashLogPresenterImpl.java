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
import com.jim.multipos.ui.cash_management.view.CashLogView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 11.01.2018.
 */

public class CashLogPresenterImpl extends BasePresenterImpl<CashLogView> implements CashLogPresenter {

    private DatabaseManager databaseManager;
    private DecimalFormat decimalFormat;
    private List<Account> accountList;
    private Till till;

    @Inject
    protected CashLogPresenterImpl(CashLogView view, DatabaseManager databaseManager, DecimalFormat decimalFormat) {
        super(view);
        this.databaseManager = databaseManager;
        this.decimalFormat = decimalFormat;
        accountList = new ArrayList<>();
    }

    @Override
    public void initData() {
        boolean isHaveOpenTill = databaseManager.hasOpenTill().blockingGet();
        if (isHaveOpenTill) {
            till = databaseManager.getOpenTill().blockingGet();
            view.setTillOpenDateTime(till.getOpenDate());
        } else {
            boolean isNoTills = databaseManager.isNoTills().blockingGet();
            if (!isNoTills) {
                till = null;
                view.setNoTillDate();
            } else {
                till = databaseManager.getLastClosedTill().blockingGet();
                view.setTillOpenDateTime(till.getOpenDate());
                view.setTillClosedDateTime(till.getCloseDate());
            }
        }
        if (till != null){
            countDebtSales();
        }
        accountList = databaseManager.getAllAccounts().blockingSingle();
        initAccounts();
    }

    @Override
    public void setDataToDetailsFragment(int position) {
        view.setDataToDetailsFragment(accountList.get(position), till);
    }

    @Override
    public void changeAccount(Long accountId) {
        for (int i = 0; i < accountList.size(); i++) {
            if (accountList.get(i).getId().equals(accountId))
                view.setAccountSelection(i);
        }
    }

    private void initAccounts() {
        List<String> accounts = new ArrayList<>();
        for (Account accName : accountList) {
            accounts.add(accName.getName());
        }
        view.setDataToAccountSpinner(accounts);
    }

    private void countDebtSales() {
        double debtSales = 0;
        List<Order> ordersList = databaseManager.getOrdersByTillId(till.getId()).blockingGet();
        for (int i = 0; i < ordersList.size(); i++) {
            Order order = ordersList.get(i);
            debtSales += order.getToDebtValue();
        }
        till.setDebtSales(debtSales);
        view.setDebtSales(decimalFormat.format(debtSales));
    }

    @Override
    public void setTillStatus(int status) {
        if (status == Till.CLOSED) {
            till = databaseManager.getLastClosedTill().blockingGet();
            view.setTillOpenDateTime(till.getOpenDate());
            view.setTillClosedDateTime(till.getCloseDate());
            saveTillDetails();
            view.sendEvent();
        } else if (status == Till.OPEN) {
            till = databaseManager.getOpenTill().blockingGet();
            view.setNoTillDate();
            view.setTillOpenDateTime(till.getOpenDate());
        }
        initAccounts();
        countDebtSales();
    }


    private void saveTillDetails() {
        for (Account account : accountList) {
            double payIn, payOut, bankDrop, payToVendor, incomeDebt, cashTransactions = 0, tips = 0, totalStartingCash;
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
                if (order.getTipsAccount() != null && account.getStaticAccountType() == order.getTipsAccount().getStaticAccountType()){
                    tips += order.getTips();
                }
            }
            TillDetails details = new TillDetails();
            details.setAccount(account);
            details.setTillId(till.getId());
            details.setTotalBankDrops(bankDrop);
            details.setTotalDebtIncome(incomeDebt);
            details.setTotalPayIns(payIn);
            details.setTotalPayOuts(payOut);
            details.setTotalPayToVendors(payToVendor);
            details.setTotalSales(cashTransactions);
            details.setTips(tips);
            details.setTotalStartingCash(totalStartingCash);
            databaseManager.insertTillDetails(details).subscribe();
        }
    }
}
