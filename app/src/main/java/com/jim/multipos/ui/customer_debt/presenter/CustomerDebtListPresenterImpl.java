package com.jim.multipos.ui.customer_debt.presenter;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.ui.customer_debt.view.CustomerDebtListView;
import com.jim.multipos.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Inject
    protected CustomerDebtListPresenterImpl(CustomerDebtListView view, DatabaseManager databaseManager, Context context) {
        super(view);
        this.databaseManager = databaseManager;
        this.context = context;
        debtList = new ArrayList<>();
    }

    @Override
    public void initData(Customer customer) {
        currency = databaseManager.getMainCurrency();
        databaseManager.getDebtsByCustomerId(customer.getId()).subscribe((debts, throwable) -> {
            debtList = debts;
            view.fillRecyclerView(debtList, currency);
        });
    }

    @Override
    public void initDebtDetails(Debt item, int position) {
        this.debt = item;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date today = new Date(System.currentTimeMillis());
        Date endDate = new Date(item.getEndDate());
        String leftDate = "";
        if (item.getStatus() == Debt.CLOSED)
            leftDate = "Closed";
        else {
            if (today.getTime() > endDate.getTime())
                leftDate = "Overdue";
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
        String status;
        if (item.getStatus() == Debt.ACTIVE) {
            status = "Active";
        } else status = "Closed";

        String debtType;
        if (item.getDebtType() == Debt.PARTICIPLE)
            debtType = "Can participle";
        else debtType = "All in once";
//        double feeAmount = item.getFee() * item.getOrder().getSubTotalValue()/100;
//        double total = item.getOrder().getSubTotalValue() + feeAmount;
        double feeAmount = item.getFee() * 50000 / 100;
        double total = 50000 + feeAmount;
        double paidAmount = 0;
        double dueAmount = 0;
        if (item.getCustomerPayments().size() > 0){
            for (int i = 0; i < item.getCustomerPayments().size(); i++) {
                dueAmount += item.getCustomerPayments().get(i).getDebtDue();
                paidAmount += item.getCustomerPayments().get(i).getPaymentAmount();
            }
        }

        if (dueAmount == 0)
            dueAmount = total;

//        view.fillDebtInfo(item.getOrder().getId(), simpleDateFormat.format(item.getTakenDate()), simpleDateFormat.format(item.getEndDate()),
//                leftDate, debtType, item.getFee(), status, item.getOrder().getSubTotalValue(), feeAmount, total, paidAmount, feeAmount, dueAmount, databaseManager.getMainCurrency());

        view.fillDebtInfo(5462L, simpleDateFormat.format(item.getTakenDate()), simpleDateFormat.format(item.getEndDate()),
                leftDate, debtType, item.getFee(), status, 50000, feeAmount, total, paidAmount, feeAmount, dueAmount, databaseManager.getMainCurrency());
    }

    @Override
    public void onPayToDebt() {
        view.openPayToDebt(debt, databaseManager);
    }
}
