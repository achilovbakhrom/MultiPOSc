package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.customer.CustomerPayment;
import com.jim.multipos.data.db.model.customer.Debt;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


/**
 * Created by user on 02.09.17.
 */

public interface CustomerOperations {
    Observable<Long> addCustomer(Customer customer);
    Observable<Boolean> addCustomers(List<Customer> customers);
    Observable<Boolean> removeCustomer(Customer customer);
    Observable<Boolean> removeAllCustomers();
    Observable<List<Customer>> getCustomers();
    Observable<List<CustomerGroup>> getCustomerGroups(Customer customer);
    Observable<Boolean> isCustomerExists(String name);
    Single<Customer> getCustomerById(Long customerId);
    Single<List<Customer>> getCustomersWithDebt();
    //Debts
    Single<Boolean> addDebt(Debt debt);
    Single<Boolean> deleteDebt(Debt debt);
    Single<List<Debt>> getDebtsByCustomerId(Long id);
    Single<List<Debt>> getAllActiveDebts();
    Single<CustomerPayment> addCustomerPayment(CustomerPayment payment);
    Single<List<CustomerPayment>> getCustomerPaymentsByInterval(Calendar fromDate, Calendar toDate);
    Single<Double> getCustomerPaymentsInInterval(Long id, Calendar fromDate, Calendar toDate);
    Single<List<Debt>> getAllCustomerDebtsInInterval(Calendar fromDate, Calendar toDate);
    Single<List<Customer>> getAllCustomers();
}
