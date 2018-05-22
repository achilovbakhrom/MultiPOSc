package com.jim.multipos.ui.start_configuration;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;

import javax.inject.Inject;
import javax.inject.Named;

public class StartConfigurationPresenterImpl extends BasePresenterImpl<StartConfigurationView> implements StartConfigurationPresenter {

    private DatabaseManager databaseManager;
    private String[] currencyName;
    private String[] currencyAbbr;
    private String accountName;
    private String paymentTypeName;
    private String debtName;
    private String debtAccountName;

    @Inject
    protected StartConfigurationPresenterImpl(StartConfigurationView startConfigurationView, DatabaseManager databaseManager, @Named(value = "currency_name") String[] currencyName,
                                              @Named(value = "currency_abbr") String[] currencyAbbr, @Named(value = "till") String accountName, @Named(value = "cash") String paymentTypeName,
                                              @Named(value = "debt") String debtName, @Named(value = "debtAccount") String debtAccountName) {
        super(startConfigurationView);
        this.databaseManager = databaseManager;
        this.currencyName = currencyName;
        this.currencyAbbr = currencyAbbr;
        this.accountName = accountName;
        this.paymentTypeName = paymentTypeName;
        this.debtName = debtName;
        this.debtAccountName = debtAccountName;
    }

    @Override
    public void initDefaultData() {
        if (databaseManager.getMainCurrency() == null) {
            Currency currency = new Currency();
            currency.setIsMain(true);
            currency.setName(currencyName[0]);
            currency.setAbbr(currencyAbbr[0]);
            currency.setActive(true);
            databaseManager.addCurrency(currency).subscribe();
        }
        if (databaseManager.getAccounts().isEmpty()) {
            Account account1 = new Account();
            account1.setName(accountName);
            account1.setIsActive(true);
            account1.setStaticAccountType(Account.CASH_ACCOUNT);
            account1.setIsNotSystemAccount(true);
            databaseManager.addAccount(account1).subscribe();
            Account account = new Account();
            account.setName(debtAccountName);
            account.setStaticAccountType(Account.DEBT_ACCOUNT);
            account.setIsActive(true);
            account.setIsNotSystemAccount(false);
            databaseManager.addAccount(account).subscribe();
            PaymentType paymentType = new PaymentType();
            paymentType.setAccount(account);
            paymentType.setName(debtName);
            paymentType.setTypeStaticPaymentType(PaymentType.DEBT_PAYMENT_TYPE);
            paymentType.setCurrency(databaseManager.getMainCurrency());
            paymentType.setIsNotSystem(false);
            databaseManager.addPaymentType(paymentType).subscribe();
            PaymentType cashPaymentType = new PaymentType();
            cashPaymentType.setAccount(account1);
            cashPaymentType.setName(paymentTypeName);
            cashPaymentType.setTypeStaticPaymentType(PaymentType.CASH_PAYMENT_TYPE);
            cashPaymentType.setCurrency(databaseManager.getMainCurrency());
            databaseManager.addPaymentType(cashPaymentType).subscribe();
        }
        view.initViews();
    }

    @Override
    public void clearData() {
        databaseManager.removeAllAccounts().subscribe();
        databaseManager.removeAllPaymentTypes().subscribe();
        databaseManager.removeAllCurrencies().subscribe();
        view.restart();
    }
}
