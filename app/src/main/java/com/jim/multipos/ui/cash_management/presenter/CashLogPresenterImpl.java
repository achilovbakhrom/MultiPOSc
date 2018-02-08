package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.ui.cash_management.view.CashLogView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 11.01.2018.
 */

public class CashLogPresenterImpl extends BasePresenterImpl<CashLogView> implements CashLogPresenter {

    private DatabaseManager databaseManager;
    private List<Account> accountList;
    private Till till;

    @Inject
    protected CashLogPresenterImpl(CashLogView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
        accountList = new ArrayList<>();
    }

    @Override
    public void initData() {
        boolean isHaveOpenTill = databaseManager.hasOpenTill().blockingGet();
        if (isHaveOpenTill) {
            till = databaseManager.getOpenTill().blockingGet();
        } else {
            boolean isNoTills = databaseManager.isNoTills().blockingGet();
            if (isNoTills) {
                till = null;
            } else till = databaseManager.getLastClosedTill().blockingGet();
        }


        accountList = databaseManager.getAllAccounts().blockingSingle();
        List<String> accounts = new ArrayList<>();
        for (Account accName : accountList) {
            accounts.add(accName.getName());
        }
        if (till != null) {
            view.setDataToAccountSpinner(accounts);
            view.setTillOpenDateTime(till.getOpenDate());
        }
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
}
