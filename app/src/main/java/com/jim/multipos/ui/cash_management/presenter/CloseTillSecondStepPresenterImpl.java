package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillManagementOperation;
import com.jim.multipos.ui.cash_management.view.CloseTillSecondStepView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 13.01.2018.
 */

public class CloseTillSecondStepPresenterImpl extends BasePresenterImpl<CloseTillSecondStepView> implements CloseTillSecondStepPresenter {

    private DatabaseManager databaseManager;
    private List<TillManagementOperation> operations;

    @Inject
    protected CloseTillSecondStepPresenterImpl(CloseTillSecondStepView view, DatabaseManager databaseManager) {
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
            operation.setType(TillManagementOperation.CLOSED_WITH);
            operations.add(operation);
        }
        view.setDataToRecyclerView(operations, currency);
    }

    @Override
    public void checkCompletion() {
        int count = 0;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAmount() == null){
                view.sendSecondStepCompletionStatus(false);
                break;
            } else count++;
        }

        if (count == operations.size())
            view.sendSecondStepCompletionStatus(true);
    }

    @Override
    public void collectData() {
        view.sendAllDataToParent(operations);
    }
}
