package com.jim.multipos.utils;

import android.content.Context;

import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.GregorianCalendar;
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
            Account account = new Account();
            account.setName("Bank");
            account.setCirculation(1);
            account.setType(1);
            databaseManager.addAccount(account).subscribe();
            Account account1 = new Account();
            account1.setName("Kassa");
            account1.setCirculation(0);
            account1.setType(0);
            databaseManager.addAccount(account1).subscribe();
            if(databaseManager.getPaymentTypes().isEmpty()){
                PaymentType paymentType = new PaymentType();
                paymentType.setName("Naqt Pul");
                paymentType.setAccount(account1);
                paymentType.setCurrency(databaseManager.getMainCurrency());
                databaseManager.addPaymentType(paymentType).subscribe();
                PaymentType paymentType1 = new PaymentType();
                paymentType1.setName("Plastik");
                paymentType.setAccount(account);
                paymentType.setCurrency(databaseManager.getMainCurrency());
                databaseManager.addPaymentType(paymentType1).subscribe();
            }
        }
    }

    public static void createCustomersWithDebt(DatabaseManager databaseManager){
        if (databaseManager.getAllCustomers().blockingSingle().isEmpty()){
            Customer customer = new Customer();
            customer.setAddress("Tashkent, 5A");
            customer.setClientId(1L);
            customer.setPhoneNumber("998901532653");
            customer.setCreatedDate(System.currentTimeMillis());
            customer.setName("Ketmonbek Toshpulatov");
            customer.setQrCode("48454846454");
            databaseManager.addCustomer(customer).subscribe(aLong -> {
                Debt debt = new Debt();
                debt.setCustomer(customer);
                debt.setDebtAmount(500);
                debt.setFee(5);
                debt.setTakenDate(System.currentTimeMillis());
                GregorianCalendar calendar = new GregorianCalendar(2017, 11, 30);
                debt.setEndDate(calendar.getTimeInMillis());
                databaseManager.addDebt(debt).subscribe();
                Debt debt1 = new Debt();
                debt1.setCustomer(customer);
                debt1.setDebtAmount(500);
                debt1.setFee(5);
                debt1.setTakenDate(System.currentTimeMillis());
                GregorianCalendar calendar1 = new GregorianCalendar(2017, 11, 30);
                debt1.setEndDate(calendar1.getTimeInMillis());
                databaseManager.addDebt(debt1).subscribe();
            });
            Customer customer1 = new Customer();
            customer1.setAddress("Tashkent, QoraSuv 2 - A");
            customer1.setClientId(2L);
            customer1.setPhoneNumber("99890154453");
            customer1.setCreatedDate(System.currentTimeMillis());
            customer1.setName("Avaz Oxun");
            customer1.setQrCode("168432135");
            databaseManager.addCustomer(customer1).subscribe(aLong -> {
                Debt debt = new Debt();
                debt.setCustomer(customer1);
                debt.setDebtAmount(500);
                debt.setFee(5);
                debt.setTakenDate(System.currentTimeMillis());
                GregorianCalendar calendar = new GregorianCalendar(2017, 11, 31);
                debt.setEndDate(calendar.getTimeInMillis());
                databaseManager.addDebt(debt).subscribe();
                Debt debt1 = new Debt();
                debt1.setCustomer(customer);
                debt1.setDebtAmount(500);
                debt1.setFee(5);
                debt1.setTakenDate(System.currentTimeMillis());
                GregorianCalendar calendar1 = new GregorianCalendar(2017, 11, 30);
                debt1.setEndDate(calendar1.getTimeInMillis());
                databaseManager.addDebt(debt1).subscribe();
            });

        }
    }
}
