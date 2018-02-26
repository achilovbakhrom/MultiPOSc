package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillManagementOperation;
import com.jim.multipos.ui.cash_management.view.CloseTillThirdStepView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 13.01.2018.
 */

public class CloseTillThirdStepPresenterImpl extends BasePresenterImpl<CloseTillThirdStepView> implements CloseTillThirdStepPresenter {

    private DatabaseManager databaseManager;
    private List<TillManagementOperation> operations;
    private boolean left;

    @Inject
    protected CloseTillThirdStepPresenterImpl(CloseTillThirdStepView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
        operations = new ArrayList<>();
    }

    @Override
    public void initData() {
        Currency currency = databaseManager.getMainCurrency();
        List<Account> accountList = databaseManager.getAccounts();
        Till till = databaseManager.getOpenTill().blockingGet();
        for (int i = 0; i < accountList.size(); i++) {
            TillManagementOperation operation = new TillManagementOperation();
            operation.setAccount(accountList.get(i));
            operation.setTill(till);
            operation.setType(TillManagementOperation.TO_NEW_TILL);
            operations.add(operation);
        }
        view.setDataToRecyclerView(operations, currency);
    }

    @Override
    public void checkCompletion(boolean left) {
        this.left = left;
        if (!left) {
            int count = 0;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAmount() == null) {
                    view.sendThirdStepCompletionStatus(false);
                    break;
                } else count++;
            }
            if (count == operations.size())
                view.sendThirdStepCompletionStatus(true);
        } else {
            view.sendThirdStepCompletionStatus(true);
        }
    }

    @Override
    public void collectData() {
        if (!left)
            view.sendAllDataToParent(operations);
        else {
            List<Account> accountList = databaseManager.getAccounts();
            Till till = databaseManager.getOpenTill().blockingGet();
            List<TillManagementOperation> list = new ArrayList<>();
            for (int i = 0; i < accountList.size(); i++) {
                TillManagementOperation operation = new TillManagementOperation();
                operation.setAccount(accountList.get(i));
                operation.setTill(till);
                operation.setAmount(0d);
                operation.setType(TillManagementOperation.TO_NEW_TILL);
                list.add(operation);
            }
            view.sendAllDataToParent(list);
        }
    }
}
