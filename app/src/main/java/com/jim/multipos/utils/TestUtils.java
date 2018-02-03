package com.jim.multipos.utils;

import android.content.Context;

import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.order.Order;
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

            Account debt = new Account();
            debt.setName("DebtAccount");
            debt.setCirculation(0);
            debt.setType(0);
            debt.setIsVisible(false);
            debt.setTypeStaticAccountType(Account.DEBT_ACCOUNT);
            databaseManager.addAccount(debt).blockingSingle();
            Account account = new Account();
            account.setName("Bank");
            account.setCirculation(1);
            account.setType(1);
            databaseManager.addAccount(account).subscribe();
            Account account1 = new Account();
            account1.setName("Cashbox");
            account1.setCirculation(0);
            account1.setType(0);
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
                paymentType.setAccount(account);
                paymentType.setCurrency(databaseManager.getMainCurrency());
                databaseManager.addPaymentType(paymentType1).subscribe();
            }
        }
    }

//    public static void createCustomersWithDebt(DatabaseManager databaseManager){
////        if (databaseManager.getAllCustomers().blockingSingle().isEmpty()){
////            Customer customer = new Customer();
////            customer.setAddress("Tashkent, 5A");
////            customer.setClientId(1L);
////            customer.setPhoneNumber("998901532653");
////            customer.setCreatedDate(System.currentTimeMillis());
////            customer.setName("Ketmonbek Toshpulatov");
////            customer.setQrCode("48454846454");
////            databaseManager.addCustomer(customer).subscribe(aLong -> {
////                Order order = new Order();
////                order.setCustomer(customer);
////                order.setCreateAt(System.currentTimeMillis());
////                order.setSubTotalValue(30000);
////                databaseManager.addOrder(order).blockingGet();
////                Debt debt = new Debt();
////                debt.setCustomer(customer);
////                debt.setOrder(order);
////                debt.setDebtAmount(order.getSubTotalValue() + 5 * order.getSubTotalValue()/100);
////                debt.setFee(5);
////                debt.setDebtType(Debt.ALL);
////                debt.setTakenDate(System.currentTimeMillis());
////                GregorianCalendar calendar = new GregorianCalendar(2018, 1, 15);
////                debt.setEndDate(calendar.getTimeInMillis());
////                debt.setStatus(Debt.ACTIVE);
////                databaseManager.addDebt(debt).subscribe();
////                Order order1 = new Order();
////                order1.setCustomer(customer);
////                order1.setCreateAt(System.currentTimeMillis());
////                order1.setSubTotalValue(25000);
////                databaseManager.addOrder(order1).blockingGet();
////                Debt debt1 = new Debt();
////                debt1.setCustomer(customer);
////                debt1.setDebtAmount(order1.getSubTotalValue() + 15 * order1.getSubTotalValue()/100);
////                debt1.setFee(15);
////                debt1.setDebtType(Debt.PARTICIPLE);
////                debt1.setOrder(order1);
////                debt1.setStatus(Debt.ACTIVE);
////                debt1.setTakenDate(System.currentTimeMillis());
////                GregorianCalendar calendar1 = new GregorianCalendar(2018, 0, 15);
////                debt1.setEndDate(calendar1.getTimeInMillis());
////                databaseManager.addDebt(debt1).subscribe();
////            });
////            Customer customer1 = new Customer();
////            customer1.setAddress("Tashkent, QoraSuv 2 - A");
////            customer1.setClientId(2L);
////            customer1.setPhoneNumber("99890154453");
////            customer1.setCreatedDate(System.currentTimeMillis());
////            customer1.setName("Avaz Oxun");
////            customer1.setQrCode("168432135");
////            databaseManager.addCustomer(customer1).subscribe(aLong -> {
////                Order order = new Order();
////                order.setCustomer(customer1);
////                order.setCreateAt(System.currentTimeMillis());
////                order.setSubTotalValue(30000);
////                databaseManager.addOrder(order).blockingGet();
////                Debt debt = new Debt();
////                debt.setCustomer(customer1);
////                debt.setOrder(order);
////                debt.setDebtAmount(order.getSubTotalValue() + 5 * order.getSubTotalValue()/100);
////                debt.setFee(5);
////                debt.setTakenDate(System.currentTimeMillis());
////                GregorianCalendar calendar = new GregorianCalendar(2018, 0, 3);
////                debt.setEndDate(calendar.getTimeInMillis());
////                debt.setStatus(Debt.ACTIVE);
////                databaseManager.addDebt(debt).subscribe();
////                Order order1 = new Order();
////                order1.setCustomer(customer1);
////                order1.setCreateAt(System.currentTimeMillis());
////                order1.setSubTotalValue(25000);
////                databaseManager.addOrder(order1).blockingGet();
////                Debt debt1 = new Debt();
////                debt1.setCustomer(customer1);
////                debt1.setDebtAmount(order1.getSubTotalValue() + 15 * order1.getSubTotalValue()/100);
////                debt1.setFee(15);
////                debt1.setOrder(order1);
////                debt1.setStatus(Debt.ACTIVE);
////                debt1.setTakenDate(System.currentTimeMillis());
////                GregorianCalendar calendar1 = new GregorianCalendar(2018, 0, 15);
////                debt1.setEndDate(calendar1.getTimeInMillis());
////                databaseManager.addDebt(debt1).subscribe();
////            });
////
////        }
//    }
}
