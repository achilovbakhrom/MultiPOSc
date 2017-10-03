package com.jim.multipos.ui.first_configure.presenters;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.operations.AccountOperations;
import com.jim.multipos.data.operations.CurrencyOperations;
import com.jim.multipos.data.operations.PaymentTypeOperations;
import com.jim.multipos.ui.first_configure.fragments.PaymentTypeFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 05.08.17.
 */

public class PaymentTypeFragmentPresenterImpl implements PaymentTypeFragmentPresenter {
    private PaymentTypeFragmentView view;
    private List<PaymentType> systemPaymentTypes;
    private CurrencyFragmentPresenter currencyFragmentPresenter;
    private AccountFragmentPresenter accountFragmentPresenter;
    private List<Account> systemAccounts;
    private List<Currency> systemCurrencies;
    private Context context;
    private PaymentTypeOperations paymentTypeoperations;
    private AccountOperations accountOperations;
    private CurrencyOperations currencyOperations;


    public PaymentTypeFragmentPresenterImpl(Context context, PaymentTypeOperations operations, AccountOperations accountOperations, CurrencyOperations currencyOperations) {
        this.context = context;
        this.paymentTypeoperations = operations;
        this.accountOperations = accountOperations;
        this.currencyOperations = currencyOperations;
        systemPaymentTypes = new ArrayList<>();
        systemAccounts = new ArrayList<>();
        systemCurrencies = new ArrayList<>();

        operations.getAllPaymentTypes().subscribe(paymentTypes -> {
            systemPaymentTypes = paymentTypes;
        });
    }

    @Override
    public void init(PaymentTypeFragmentView view) {
        this.view = view;
    }

    @Override
    public boolean isCompleteData() {
        if (!systemPaymentTypes.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setData() {
        if (systemCurrencies.size() == 1) {
            currencyOperations.getAllCurrencies().subscribe(currencies -> {
                if (!currencies.isEmpty())
                    this.systemCurrencies = currencies;
            });
        }

        view.showAccount(systemAccounts);
        view.showCurrencies(systemCurrencies);
        view.showRecyclerView(systemPaymentTypes);
    }

    @Override
    public void addData(String paymentTypeName, int accountPosition, int currencyPosition) {
        boolean hasError = checkData(paymentTypeName);

        if (!hasError) {
            PaymentType paymentType = new PaymentType();
            paymentType.setName(paymentTypeName);
            paymentType.setAccount(systemAccounts.get(accountPosition));
            paymentType.setCurrency(systemCurrencies.get(currencyPosition));

            systemPaymentTypes.add(0, paymentType);

            view.paymentTypeAdded();
            view.clearViews();
        }
    }

    private boolean checkData(String paymentTypeName) {
        boolean hasError = false;

        if (paymentTypeName.isEmpty()) {
            hasError = true;
            view.showPaymentTypeNameError(getString(R.string.enter_payment_type_name));
        }

        if (systemAccounts.isEmpty()) {
            hasError = true;
            view.showAccountError(getString(R.string.create_least_one_account));
        }

        return hasError;
    }

    @Override
    public void openNextFragment() {
        if (!systemPaymentTypes.isEmpty()) {
            view.clearViews();
            view.openNextFragment();
        } else {
            view.showPaymentTypeListEmpty(getString(R.string.create_least_one_payment_type));
        }
    }

    private String getString(int resId) {
        return context.getString(resId);
    }

    @Override
    public void updatePaymentTypeCurrency() {
        List<Currency> removedCurrencies = currencyFragmentPresenter.getRemovedList();

        for (int i = 0; i < removedCurrencies.size(); i++) {
            for (int j = 0; j < systemPaymentTypes.size(); j++) {
                if (systemPaymentTypes.get(j).getCurrency().equals(removedCurrencies.get(i))) {
                    systemPaymentTypes.remove(j);
                }
            }
        }

        view.updateRecyclerView();
        removedCurrencies.clear();
    }

    @Override
    public void saveData() {
        paymentTypeoperations.removeAllPaymentTypes().subscribe(aBoolean -> {
            paymentTypeoperations.addPaymentTypes(systemPaymentTypes).subscribe();
        });
    }

    @Override
    public void removeItem(int position) {
        systemPaymentTypes.remove(position);
        view.paymentTypeRemoved();
    }

    @Override
    public void updateAccountData(List<Account> accounts) {
        List<PaymentType> payment = new ArrayList<>();
        systemAccounts = accounts;

        for (Account account : systemAccounts) {
            for (int i = 0; i < systemPaymentTypes.size(); i++) {
                Account acc = systemPaymentTypes.get(i).getAccount();
                if (acc.getId().equals(account.getId())) {
                    payment.add(systemPaymentTypes.get(i));
                }
            }
        }

        systemPaymentTypes = payment;
        if (view != null)
            view.showAccount(accounts);
    }

    @Override
    public void updateCurrencyData(List<Currency> currencies) {
        List<PaymentType> payment = new ArrayList<>();
        systemCurrencies = currencies;

        for (Currency currency : systemCurrencies) {
            for (int i = 0; i < systemPaymentTypes.size(); i++) {
                Currency curr = systemPaymentTypes.get(i).getCurrency();
                if (currency.getId().equals(curr.getId())) {
                    payment.add(systemPaymentTypes.get(i));
                }
            }
        }

        systemPaymentTypes = payment;

        if (view !=null)
            view.showCurrencies(currencies);
    }
}
