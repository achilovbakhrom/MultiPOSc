package com.jim.multipos.ui.customer_debt.presenter;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.ui.customer_debt.view.CustomerDebtListFragment;
import com.jim.multipos.ui.customer_debt.view.CustomerDebtListView;
import com.jim.multipos.utils.CommonUtils;
import com.jim.multipos.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 30.12.2017.
 */

public class CustomerDebtListPresenterImpl extends BasePresenterImpl<CustomerDebtListView> implements CustomerDebtListPresenter {

    private List<Debt> debtList;
    private Currency currency;
    private DatabaseManager databaseManager;
    private Context context;
    private Debt debt;
    private CustomerDebtListFragment.DebtSortingStates sortMode = CustomerDebtListFragment.DebtSortingStates.SORTED_BY_TAKEN_DATE;
    private int sorting = 1;
    private Customer customer;

    @Inject
    protected CustomerDebtListPresenterImpl(CustomerDebtListView view, DatabaseManager databaseManager, Context context) {
        super(view);
        this.databaseManager = databaseManager;
        this.context = context;
        debtList = new ArrayList<>();
    }

    @Override
    public void initData(Customer customer) {
        this.customer = customer;
        currency = databaseManager.getMainCurrency();
        databaseManager.getDebtsByCustomerId(customer.getId()).subscribe((debts, throwable) -> {
            debtList.clear();
            debtList.addAll(debts);
            sortList();
            view.fillRecyclerView(debtList, currency);
        });
        initTotalDataOfCustomer();
    }

    @Override
    public void initDebtDetails(Debt item, int position) {
        this.debt = item;
        double feeAmount = item.getFee() * item.getDebtAmount() / 100;
        double total = item.getDebtAmount() + feeAmount;
        double paidAmount = 0;
        double dueAmount = total;
        if (item.getCustomerPayments().size() > 0) {
            for (int i = 0; i < item.getCustomerPayments().size(); i++) {
                dueAmount -= item.getCustomerPayments().get(i).getPaymentAmount();
                paidAmount += item.getCustomerPayments().get(i).getPaymentAmount();
            }
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date today = new Date(System.currentTimeMillis());
        Date endDate = new Date(item.getEndDate());
        String leftDate = "";
        if (item.getStatus() == Debt.CLOSED)
            leftDate = context.getString(R.string.closed);
        else {
            if (today.getTime() > endDate.getTime())
                leftDate = context.getString(R.string.overdue);
            else {
                int[] leftTime = CommonUtils.getDateDifferenceInDDMMYYYY(today, endDate);
                if (leftTime[0] != 0) {
                    if (leftTime[0] > 1) {
                        leftDate += Integer.toString(leftTime[0]) + " " + context.getString(R.string.years);
                    } else {
                        leftDate += Integer.toString(leftTime[0]) + " " + context.getString(R.string.year);
                    }
                }
                if (leftTime[1] != 0) {
                    if (!leftDate.matches("")) {
                        leftDate += " ";
                    }
                    if (leftTime[1] > 1) {
                        leftDate += Integer.toString(leftTime[1]) + " " + context.getString(R.string.months);
                    } else {
                        leftDate += Integer.toString(leftTime[1]) + " " + context.getString(R.string.month);
                    }
                }
                if (leftTime[2] != 0) {
                    if (!leftDate.matches("")) {
                        leftDate += " ";
                    }
                    if (leftTime[2] > 1) {
                        leftDate += Integer.toString(leftTime[2]) + " " + context.getString(R.string.days);
                    } else {
                        leftDate += Integer.toString(leftTime[2]) + " " + context.getString(R.string.day);
                    }
                }

            }
        }

        view.fillDebtInfo(item.getOrder().getId(), simpleDateFormat.format(item.getTakenDate()), simpleDateFormat.format(item.getEndDate()),
                leftDate, item.getDebtType(), item.getFee(), feeAmount, total, paidAmount,
                dueAmount, currency, item.getDebtAmount());
    }

    @Override
    public void onPayToDebt() {
        view.openPayToDebt(debt, databaseManager, false, false, customer);
    }

    @Override
    public void onPaymentHistoryClicked() {
        view.openPaymentHistoryDialog(debt, databaseManager);
    }

    @Override
    public void onCustomerDebtsHistoryClicked() {
        view.openCustomerDebtsHistoryDialog(customer, databaseManager);
    }

    @Override
    public void closeDebtWithPayingAllAmount(Debt item) {
        view.openPayToDebt(item, databaseManager, true, false, customer);
    }

    @Override
    public void filterBy(CustomerDebtListFragment.DebtSortingStates sortMode) {
        this.sortMode = sortMode;
        sorting = 1;
        sortList();
        view.notifyList();
    }

    @Override
    public void filterInvert() {
        sorting *= -1;
        sortList();
        view.notifyList();
    }

    @Override
    public void initTotalDataOfCustomer() {
        double total = 0;
        for (Debt debt : debtList) {
            double feeAmount = debt.getFee() * debt.getDebtAmount() / 100;
            total += debt.getDebtAmount() + feeAmount;
            if (debt.getCustomerPayments().size() > 0)
                for (int i = 0; i < debt.getCustomerPayments().size(); i++) {
                    total -= debt.getCustomerPayments().get(i).getPaymentAmount();
                }
        }
        view.fillTotalInfo(total, currency);

    }

    @Override
    public void openPayToAllDialog() {
        customer.resetDebtList();
        if (customer.getDebtList() != null && customer.getDebtList().size() > 0)
            view.openPayToDebt(null, databaseManager, false, true, customer);
        else view.openWarningDialog();
    }

    private void sortList() {
        switch (sortMode) {
            case SORTED_BY_TAKEN_DATE:
                Collections.sort(debtList, (debt, t1) -> debt.getTakenDate().compareTo(t1.getTakenDate()) * sorting);
                break;
            case SORTED_BY_ORDER_NUMBER:
                Collections.sort(debtList, (debt, t1) -> debt.getOrderId().compareTo(t1.getOrderId()) * sorting);
                break;
            case SORTED_BY_TOTAL_DEBT:
                Collections.sort(debtList, (debt, t1) -> debt.getDebtAmount().compareTo(t1.getDebtAmount()) * sorting);
                break;
            case SORTED_BY_DUE_DATE:
                Collections.sort(debtList, (debt, t1) -> debt.getEndDate().compareTo(t1.getEndDate()) * sorting);
                break;
        }
    }
}
