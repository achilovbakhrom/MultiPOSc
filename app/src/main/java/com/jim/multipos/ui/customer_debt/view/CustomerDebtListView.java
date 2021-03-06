package com.jim.multipos.ui.customer_debt.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;

import java.util.List;

/**
 * Created by Sirojiddin on 30.12.2017.
 */

public interface CustomerDebtListView extends BaseView {
    void initCustomerWithDebt(Customer customer);
    void fillRecyclerView(List<Debt> debtList, Currency currency);
    void fillDebtInfo(Long orderNumber, String takenDate, String endDate, String leftDate, int debtType, double fee, double feeAmount, double total, double paidAmount, double dueAmount, Currency mainCurrency, Double debtAmount);
    void openPayToDebt(Debt debt, DatabaseManager databaseManager, boolean closeDebt, boolean payToAll, Customer customer);
    void openPaymentHistoryDialog(Debt debt, DatabaseManager databaseManager);
    void openCustomerDebtsHistoryDialog(Customer customer, DatabaseManager databaseManager);
    void setCustomerDebtListVisibility(int visibility);
    void notifyList();
    void fillTotalInfo(double total, Currency currency);
    void openWarningDialog();
}
