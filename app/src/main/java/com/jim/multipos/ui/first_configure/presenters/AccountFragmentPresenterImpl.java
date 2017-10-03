package com.jim.multipos.ui.first_configure.presenters;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.operations.AccountOperations;
import com.jim.multipos.ui.first_configure.Constants;
import com.jim.multipos.ui.first_configure.fragments.AccountFragmentView;

import java.util.HashMap;
import java.util.List;



/**
 * Created by user on 05.08.17.
 */

public class AccountFragmentPresenterImpl implements AccountFragmentPresenter {
    private AccountFragmentView view;
    private List<Account> systemAccounts;
    private String[] accountTypes;
    private String[] accountCirculation;
    private Context context;
    private AccountOperations dbManager;
    private PaymentTypeFragmentPresenter paymentTypeFragmentPresenter;

    public AccountFragmentPresenterImpl(Context context, PaymentTypeFragmentPresenter paymentTypeFragmentPresenter, AccountOperations accountOperations) {
        this.context = context;
        this.paymentTypeFragmentPresenter = paymentTypeFragmentPresenter;
        dbManager = accountOperations;
        accountTypes = getStringArray(R.array.first_configure_account_type);
        accountCirculation = getStringArray(R.array.first_configure_account_circulation);
        dbManager.getAllAccounts().subscribe(accounts -> {
            systemAccounts = accounts;
        });
    }

    @Override
    public void init(AccountFragmentView view) {
        this.view = view;
    }

    @Override
    public boolean isCompleteData() {
        if (systemAccounts.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<Account> getData() {
        return systemAccounts;
    }


    @Override
    public void addAccount(String name, int type, int circulation) {
        if (name.isEmpty()) {
            view.showAccountNameError(getString(R.string.enter_account_name));
        } else {
            Account account = new Account();
            account.setName(name);
            account.setType(getType(type));
            account.setCirculation(getCirculation(circulation));

            systemAccounts.add(0, account);
            paymentTypeFragmentPresenter.updateAccountData(systemAccounts);

            view.showAddedAccount();
            view.clearViews();
        }
    }

    private String[] getStringArray(int resId) {
        return context.getResources().getStringArray(resId);
    }

    @Override
    public void showAccountType() {
        view.showAccountType(accountTypes);
    }

    @Override
    public void showAccountCirculation() {
        view.showAccountCirculation(accountCirculation);
    }

    @Override
    public void setAdapterData() {
        paymentTypeFragmentPresenter.updateAccountData(systemAccounts);
        view.showAdapter(systemAccounts, accountTypes, accountCirculation);
    }

    private String getType(int type) {
        String accountType;

        if (type == 0) {
            accountType = Constants.TYPE_STANDART;
        } else {
            accountType = Constants.TYPE_NON_MINUS;
        }

        return accountType;
    }

    private String getCirculation(int circulation) {
        String circulationType;

        if (circulation == 0) {
            circulationType = Constants.CIRCULATION_TO_TILL;
        } else {
            circulationType = Constants.CIRCULATION_BANK_ACCOUNT;
        }

        return circulationType;
    }

    @Override
    public void openNextFragment(HashMap<String, String> data) {
        if (systemAccounts.isEmpty()) {
            view.showAccountEmptyError(getString(R.string.create_least_one_account));
        } else {
            view.openNextFragment();
        }
    }

    private String getString(int resId) {
        return context.getResources().getString(resId);
    }

    @Override
    public void removeAccount(int position) {
        systemAccounts.remove(position);
        paymentTypeFragmentPresenter.updateAccountData(systemAccounts);
        view.accountRemoved(position);
    }

    @Override
    public void saveData() {
        dbManager.removeAllAccounts().subscribe(aBoolean -> dbManager.addAccounts(systemAccounts).subscribe());
    }
}
