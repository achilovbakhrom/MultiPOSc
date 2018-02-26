package com.jim.multipos.utils;

import android.content.Context;

import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.List;

/**
 * Created by Achilov Bakhrom on 11/3/17.
 */

public class TestUtils {

    public static void createUnits(DatabaseManager databaseManager, Context context) {
        if (databaseManager.getAllUnitCategories().blockingSingle().isEmpty()) {
            UnitUtils.generateUnitCategories(context, databaseManager);
        }
    }

    public static void createCurrencies(DatabaseManager databaseManager, Context context) {
        if (databaseManager.getCurrencies().isEmpty()) {
            List<Currency> currencies = CurrencyUtils.generateCurrencies(context);
            if (!currencies.isEmpty()) {
                Currency currency = currencies.get(0);
                databaseManager.addCurrency(currency).subscribe();
            }
        }
    }

    public static void createProductClasses(DatabaseManager databaseManager) {
        if (databaseManager.getAllProductClass().blockingGet().isEmpty()) {
            for (int i = 0; i < 10; i++) {
                ProductClass productClass = new ProductClass();
                productClass.setName("Product class " + i);
                databaseManager.insertProductClass(productClass).subscribe( aLong -> {
                    for (int j = 0; j < 2; j++){
                        ProductClass childClass = new ProductClass();
                        childClass.setName("Child class " + j);
                        childClass.setParentId(productClass.getId());
                        databaseManager.insertProductClass(childClass).subscribe();
                    }
                });
            }
        }
    }

    public static void createVendord(DatabaseManager databaseManager) {
        if (databaseManager.getVendors().blockingSingle().isEmpty()) {
            for (int i = 0; i < 10; i++) {
                Vendor vendor = new Vendor();
                vendor.setName("Vendor " + i);
                vendor.setContactName("Vendor contact name " + i);
                vendor.setAddress("Vendor address" + i);
                databaseManager.addVendor(vendor).subscribe();
            }
        }
    }
    public static void createAccount(DatabaseManager databaseManager){
        if(databaseManager.getAccounts().isEmpty()){

            Account debt = new Account();
            debt.setName("DebtAccount");
            debt.setIsVisible(false);
            debt.setStaticAccountType(Account.DEBT_ACCOUNT);
            databaseManager.addAccount(debt).blockingSingle();
            Account account = new Account();
            account.setName("Bank");
            account.setStaticAccountType(Account.CUSTOM_ACCOUNT);
            databaseManager.addAccount(account).subscribe();
            Account account1 = new Account();
            account1.setName("Cashbox");
            account1.setStaticAccountType(Account.CASH_ACCOUNT);
            databaseManager.addAccount(account1).subscribe();
            if(databaseManager.getPaymentTypes().isEmpty()){
                PaymentType debtPayment = new PaymentType();
                debtPayment.setAccount(debt);
                debtPayment.setName("To Debt");
                debtPayment.setTypeStaticPaymentType(PaymentType.DEBT_PAYMENT_TYPE);
                debtPayment.setCurrency(databaseManager.getMainCurrency());
                debtPayment.setIsVisible(false);
                databaseManager.addPaymentType(debtPayment).blockingSingle();
                PaymentType paymentType = new PaymentType();
                paymentType.setName("Naqt");
                paymentType.setAccount(account1);
                paymentType.setCurrency(databaseManager.getMainCurrency());
                databaseManager.addPaymentType(paymentType).subscribe();
                PaymentType paymentType1 = new PaymentType();
                paymentType1.setName("Plastik");
                paymentType1.setAccount(account);
                paymentType1.setCurrency(databaseManager.getMainCurrency());
                databaseManager.addPaymentType(paymentType1).subscribe();
            }
        }
    }

}
