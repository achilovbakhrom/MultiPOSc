/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.jim.multipos.data.db;

import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.Account;

import com.jim.multipos.data.db.model.Vendor;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.customer.JoinCustomerGroupsWithCustomers;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.stock.Stock;
import com.jim.multipos.data.db.model.unit.SubUnitsList;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;

import java.util.List;

import io.reactivex.Observable;

import io.reactivex.Single;

/**
 * Created by developer on 08/12/16.
 */

public interface DbHelper {
    List<Account> getAccounts();
    Observable<Long> insertContact(Contact contact);
    Observable<Boolean> insertContacts(List<Contact> contact);
    Observable<List<Contact>> getAllContacts();
    DaoSession getDaoSession();
    Observable<Integer> getCategoryByName(Category category);
    Observable<Boolean> isCategoryNameExists(String name);
    Observable<Integer> getSubCategoryByName(Category category);
    Observable<Boolean> isSubCategoryNameExists(String parentName, String name);
    Observable<Long> insertCategory(Category category);
    Observable<Long> insertSubCategory(Category subcategory);
    Observable<Boolean> insertCategories(List<Category> categories);
    Observable<List<Category>> getAllCategories();
    Observable<Integer> getAllProductsCount(Category category);
    Single<List<Category>> getAllActiveCategories();
    Single<List<Category>> getAllActiveSubCategories(Category parent);
    Observable<List<Category>> getSubCategories(Category category);
    Observable<Long> insertOrReplaceCategory(Category category);
    Observable<Long> insertProduct(Product product);
    Observable<Boolean> insertProducts(List<Product> products);
    Observable<List<Product>> getAllProducts();
    Single<List<Product>> getAllActiveProducts(Category parent);
    Observable<Long> insertOrReplaceProduct(Product product);
    Observable<Account> insertAccount(Account account);
    Observable<Boolean> insertAccounts(List<Account> accounts);
    Observable<List<Account>> getAllAccounts();
    Observable<Boolean> deleteAccount(Account account);
    Observable<Boolean> deleteAllAccounts();
    Observable<Long> insertStock(Stock stock);
    Observable<Boolean> insertStocks(List<Stock> stocks);
    Observable<List<Stock>> getAllStocks();
    Observable<Boolean> deleteStock(Stock stock);
    Observable<Boolean> deleteAllStocks();
    Observable<Long> addCurrency(Currency currency);
    Observable<Boolean> insertCurrencies(List<Currency> currencies);
    Observable<Boolean> deleteCurrency(Currency currency);
    Observable<Boolean> deleteAllCurrencies();
    Observable<List<Currency>> getAllCurrencies();
    Observable<Long> insertUnitCategory(UnitCategory category);
    Observable<Boolean> insertUnitCategories(List<UnitCategory> categoryList);
    Observable<List<UnitCategory>> getAllUnitCategories();
    Observable<Long> insertPaymentType(PaymentType paymentType);
    Observable<Boolean> insertPaymentTypes(List<PaymentType> paymentTypes);
    Observable<Boolean> deletePaymentType(PaymentType paymentType);
    Observable<Boolean> deleteAllPaymentTypes();
    Observable<List<PaymentType>> getAllPaymentTypes();
    Observable<Long> insertUnit(Unit unit);
    Observable<Boolean> insertUnits(List<Unit> units);
    Observable<Boolean> deleteUnit(Unit unit);
    Observable<Boolean> deleteAllUnits();
    Observable<List<Unit>> getAllStaticUnits();
    Observable<List<Unit>> getUnits(Long rootId, String name);
    Observable<Unit> updateUnit(Unit unit);
    Observable<Boolean> insertServiceFees(List<ServiceFee> serviceFees);
    Observable<Long> insertServiceFee(ServiceFee serviceFee);
    Observable<List<ServiceFee>> getAllServiceFees();
    Observable<Boolean> deleteAllServiceFees();
    Observable<Boolean> deleteServiceFee(ServiceFee serviceFee);
    Observable<Boolean> isCustomerExists(String name);
    Observable<Long> insertCustomer(Customer customer);
    Observable<Boolean> insertCustomers(List<Customer> customers);
    Observable<Boolean> deleteCustomer(Customer customer);
    Observable<Boolean> deleteAllCustomers();
    Observable<List<Customer>> getAllCustomers();
    Observable<Long> insertCustomerGroup(CustomerGroup customerGroup);
    Observable<Boolean> insertCustomerGroups(List<CustomerGroup> customerGroups);
    Observable<Boolean> deleteCustomerGroup(CustomerGroup customerGroup);
    Observable<Boolean> deleteAllCustomerGroups();
    Observable<Boolean> isCustomerGroupExists(String name);
    Observable<List<CustomerGroup>> getAllCustomerGroups();
    Single<List<ProductClass>> getAllProductClass();
    Single<Long> insertProductClass(ProductClass productClass);
    Observable<List<SubUnitsList>> getSubUnits();
    Observable<Long> insertSubUnits(SubUnitsList subUnitsList);
    Observable<Long> insertJoinCustomerGroupWithCustomer(JoinCustomerGroupsWithCustomers joinCustomerGroupWithCustomer);
    Observable<Boolean> insertJoinCustomerGroupWithCustomers(List<JoinCustomerGroupsWithCustomers> joinCustomerGroupsWithCustomers);
    Observable<Boolean> deleteJoinCustomerGroupWithCustomer(Long customerGroupId, Long customerId);
    Observable<Boolean> deleteJoinCustomerGroupWithCustomer(Long customerId);
    Observable<Boolean> deleteAllJoinCustomerGroupWithCustomer();
    Observable<List<JoinCustomerGroupsWithCustomers>> getAllJoinCustomerGroupsWithCustomers();
    Observable<List<CustomerGroup>> getCustomerGroups(Customer customer);
    List<Currency> getCurrencies();
    Observable<Boolean> deleteSubUnits(SubUnitsList subUnitsList);
    Observable<Boolean> isAccountNameExists(String name);
    Observable<Boolean> isPaymentTypeExists(String name);
    List<PaymentType> getPaymentTypes();
    Single<List<Discount>> getAllDiscounts();
    Single<Long> insertDiscount(Discount discount);

    //Vendor operations
    Observable<Long> addVendor(Vendor vendor);
    Observable<Boolean> addVendors(List<Vendor> vendors);
    Observable<Boolean> isVendorNameExist(String name);
    Observable<Boolean> updateContacts(Long vendorId, List<Contact> contacts);
    Observable<Boolean> deleteVendor(Long vendorId);
    Observable<Vendor> getVendorById(Long vendorId);
    Observable<List<Vendor>> getVendors();
    Observable<Boolean> removeAllContacts(Long vendorId);
    Single<List<Product>> getSearchProducts(String searchText, boolean skuMode, boolean barcodeMode,boolean nameMode);

    //Category
    Observable<Category> getCategoryById(Long id);
    Observable<Boolean> removeCategory(Category category);

    //Product
    Observable<Boolean> isProductNameExists(String productName, Long categoryId);
    Observable<Boolean> removeProduct(Product product);
}
