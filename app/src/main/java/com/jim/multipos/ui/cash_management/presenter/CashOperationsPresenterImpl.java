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
        if (currentPaymentType.getAccount().getCirculation() == 0)
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
        if (till != null){
            view.getTillStatus(till.getStatus());
        }
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
            view.openCashOperationDialog(till, currentPaymentType, TillOperation.PAY_IN, payInAmount);
        } else view.showWarningDialog("Please, open till");
    }

    @Override
    public void doPayOut(double payOutAmount) {
        if (till != null) {
            view.openCashOperationDialog(till, currentPaymentType, TillOperation.PAY_OUT, payOutAmount);
        } else view.showWarningDialog("Please, open till");
    }

    @Override
    public void doBankDrop(double bankDropAmount) {
        if (till != null) {
            if (ifEnoughCash(bankDropAmount))
                view.openCashOperationDialog(till, currentPaymentType, TillOperation.BANK_DROP, bankDropAmount);
            else view.showWarningDialog("Not enough cash in cash drawer");
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
        view.showCloseTillDialog();
    }

    @Override
    public void showOpenTillDialog() {
        view.showOpenTillDialog();
    }

    public boolean ifEnoughCash(double bankDropAmount) {
        double payIn = 0, payOut = 0, bankDrop = 0, payToVendor = 0, incomeDebt = 0, cashTransactions = 0, tips = 0;

        Account account = currentPaymentType.getAccount();
        List<TillOperation> allOperations = databaseManager.getTillOperationsByAccountId(account.getId(), till.getId()).blockingGet();
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

        bankDrop += bankDropAmount;

        double cash = payIn - payOut - payToVendor + incomeDebt - bankDrop + cashTransactions;

        return cash >= 0;
    }
}
