package com.jim.multipos.ui.cash_management.presenter;

import android.view.View;

import com.jim.mpviews.model.PaymentTypeWithService;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.customer.CustomerPayment;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillDetails;
import com.jim.multipos.data.db.model.till.TillManagementOperation;
import com.jim.multipos.data.db.model.till.TillOperation;
import com.jim.multipos.ui.cash_management.view.CashOperationsView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 11.01.2018.
 */

public class CashOperationsPresenterImpl extends BasePresenterImpl<CashOperationsView> implements CashOperationsPresenter {

    private List<PaymentType> paymentTypes;
    private DatabaseManager databaseManager;
    private PaymentType currentPaymentType;
    private Till till;

    @Inject
    protected CashOperationsPresenterImpl(CashOperationsView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
        paymentTypes = new ArrayList<>();
    }

    @Override
    public void changePayment(int position) {
        currentPaymentType = paymentTypes.get(position);
        view.changeAccount(currentPaymentType.getAccount().getId());
        if (currentPaymentType.getAccount().getStaticAccountType() == 1)
            view.setBankDropVisibility(View.VISIBLE);
        else view.setBankDropVisibility(View.GONE);
    }

    @Override
    public void initData() {
        paymentTypes = databaseManager.getPaymentTypes();
        boolean isHaveOpenTill = databaseManager.hasOpenTill().blockingGet();
        if (isHaveOpenTill) {
            till = databaseManager.getOpenTill().blockingGet();
        } else {
            boolean isNoTills = databaseManager.isNoTills().blockingGet();
            if (!isNoTills) {
                till = null;
            } else till = databaseManager.getLastClosedTill().blockingGet();
        }
        if (till != null) {
            view.applyTillStatus(till.getStatus());
        } else view.applyTillStatus(Till.CLOSED);

        ArrayList<PaymentTypeWithService> paymentTypeWithServices = new ArrayList<>();
        for (int i = 0; i < paymentTypes.size(); i++) {
            PaymentTypeWithService paymentTypeWithService = new PaymentTypeWithService(paymentTypes.get(i).getName(), "");
            paymentTypeWithServices.add(paymentTypeWithService);
        }
        view.initPaymentTypes(paymentTypeWithServices);
    }

    @Override
    public void doPayIn(double payInAmount) {
        if (till != null) {
            if (till.getStatus() == Till.OPEN)
                view.openCashOperationDialog(till, currentPaymentType, TillOperation.PAY_IN, payInAmount);
            else view.showWarningDialog("Please, open till");
        } else view.showWarningDialog("Please, open till");
    }

    @Override
    public void doPayOut(double payOutAmount) {
        if (till != null) {
            if (till.getStatus() == Till.OPEN)
                view.openCashOperationDialog(till, currentPaymentType, TillOperation.PAY_OUT, payOutAmount);
            else view.showWarningDialog("Please, open till");
        } else view.showWarningDialog("Please, open till");
    }

    @Override
    public void doBankDrop(double bankDropAmount) {
        if (till != null) {
            if (till.getStatus() == Till.OPEN) {
                if (ifEnoughCash(bankDropAmount))
                    view.openCashOperationDialog(till, currentPaymentType, TillOperation.BANK_DROP, bankDropAmount);
                else view.showWarningDialog("Not enough cash in cash drawer");
            } else view.showWarningDialog("Please, open till");
        } else view.showWarningDialog("Please, open till");
    }

    @Override
    public void executeOperation(double amount, int operationType, String description) {
        TillOperation tillOperation = new TillOperation();
        tillOperation.setAmount(amount);
        tillOperation.setType(operationType);
        tillOperation.setPaymentType(currentPaymentType);
        tillOperation.setTill(till);
        tillOperation.setDescription(description);
        databaseManager.insertTillOperation(tillOperation).subscribe();
        view.updateDetails();
    }

    @Override
    public void showCloseTillDialog() {
        view.showCloseTillDialog(till.getId());
    }

    @Override
    public void showOpenTillDialog() {
        view.showOpenTillDialog();
    }

    private boolean ifEnoughCash(double bankDropAmount) {
        double payIn, payOut, bankDrop, payToVendor, incomeDebt, cashTransactions = 0, tips = 0, totalStartingCash;

        Account account = currentPaymentType.getAccount();
        totalStartingCash = databaseManager.getTotalTillManagementOperationsAmount(account.getId(), till.getId(), TillManagementOperation.OPENED_WITH).blockingGet();
        payIn = databaseManager.getTotalTillOperationsAmount(account.getId(), till.getId(), TillOperation.PAY_IN).blockingGet();
        payOut = databaseManager.getTotalTillOperationsAmount(account.getId(), till.getId(), TillOperation.PAY_OUT).blockingGet();
        bankDrop = databaseManager.getTotalTillOperationsAmount(account.getId(), till.getId(), TillOperation.BANK_DROP).blockingGet() + bankDropAmount;

        Calendar fromDate = new GregorianCalendar(), toDate = new GregorianCalendar();
        if (till.getStatus() == Till.OPEN) {
            fromDate.setTimeInMillis(till.getOpenDate());
            toDate.setTimeInMillis(System.currentTimeMillis());
            payToVendor = databaseManager.getBillingOperationsAmountInInterval(account.getId(), fromDate, toDate).blockingGet();
            incomeDebt = databaseManager.getCustomerPaymentsInInterval(account.getId(), fromDate, toDate).blockingGet();
        } else {
            fromDate.setTimeInMillis(till.getOpenDate());
            toDate.setTimeInMillis(till.getCloseDate());
            payToVendor = databaseManager.getBillingOperationsAmountInInterval(account.getId(), fromDate, toDate).blockingGet();
            incomeDebt = databaseManager.getCustomerPaymentsInInterval(account.getId(), fromDate, toDate).blockingGet();
        }

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

        double cash = payIn - payOut - payToVendor + incomeDebt - bankDrop + cashTransactions + totalStartingCash;

        return cash >= 0;
    }
}
