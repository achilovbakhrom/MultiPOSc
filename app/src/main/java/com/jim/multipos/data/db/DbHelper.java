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
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.Account;

import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.customer.JoinCustomerGroupsWithCustomers;
import com.jim.multipos.data.db.model.intosystem.CategoryPosition;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.intosystem.ProductPosition;
import com.jim.multipos.data.db.model.intosystem.SubCategoryPosition;
import com.jim.multipos.data.db.model.matrix.Attribute;
import com.jim.multipos.data.db.model.matrix.AttributeType;
import com.jim.multipos.data.db.model.matrix.ChildAttribute;
import com.jim.multipos.data.db.model.matrix.ParentAttribute;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Recipe;
import com.jim.multipos.data.db.model.products.SubCategory;
import com.jim.multipos.data.db.model.stock.Stock;
import com.jim.multipos.data.db.model.unit.SubUnitsList;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;

import java.util.List;

import io.reactivex.Observable;

import io.reactivex.Single;

/**
 * Created by janisharali on 08/12/16.
 */

public interface DbHelper {
    Observable<Long> insertContact(Contact contact);
    Observable<Boolean> insertContacts(List<Contact> contact);
    Observable<List<Contact>> getAllContacts();
    DaoSession getDaoSession();
    Observable<Boolean> getCategoryByName(Category category);
    Observable<Boolean> getMatchCategory(Category category);
    Observable<Long> insertCategory(Category category);
    Observable<Boolean> insertCategories(List<Category> categories);
    Observable<List<Category>> getAllCategories();
    Observable<Long> insertOrReplaceCategory(Category category);
    Observable<Long> insertProduct(Product product);
    Observable<Boolean> insertProducts(List<Product> products);
    Observable<List<Product>> getAllProducts();
    Observable<Long> insertOrReplaceProduct(Product product);
    Observable<Long> insertSubCategory(SubCategory subCategory);
    Observable<Boolean> insertSubCategories(List<SubCategory> subCategories);
    Observable<List<SubCategory>> getAllSubCategories();
    Observable<Long> insertOrReplaceSubCategory(SubCategory subCategory);
    Observable<Boolean> insertCategoryPositions(List<CategoryPosition> positionList);
    Observable<List<Category>> getAllCategoryPositions();
    Observable<Boolean> insertProductPositions(List<ProductPosition> positionList, SubCategory subCategory);
    Observable<List<Product>> getAllProductPositions(SubCategory subCategory);
    Observable<Long> insertOrReplaceCategoryPosition(CategoryPosition position);
    Observable<Long> insertAccount(Account account);
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
    Observable<Boolean> insertServiceFees(List<ServiceFee> serviceFees);
    Observable<Long> insertServiceFee(ServiceFee serviceFee);
    Observable<List<ServiceFee>> getAllServiceFees();
    Observable<Boolean> deleteAllServiceFees();
    Observable<Boolean> deleteServiceFee(ServiceFee serviceFee);
    Observable<Boolean> insertSubCategoryPositions(List<SubCategoryPosition> positionList, Category category);
    Observable<List<SubCategory>> getAllSubCategoryPositions(Category category);
    Observable<Long> insertCustomer(Customer customer);
    Observable<Boolean> insertCustomers(List<Customer> customers);
    Observable<Boolean> deleteCustomer(Customer customer);
    Observable<Boolean> deleteAllCustomers();
    Observable<List<Customer>> getAllCustomers();
    Observable<Long> insertCustomerGroup(CustomerGroup customerGroup);
    Observable<Boolean> insertCustomerGroups(List<CustomerGroup> customerGroups);
    Observable<Boolean> deleteCustomerGroup(CustomerGroup customerGroup);
    Observable<Boolean> deleteAllCustomerGroups();
    Observable<List<CustomerGroup>> getAllCustomerGroups();
    Single<List<ProductClass>> getAllProductClass();
    Single<Long> insertProductClass(ProductClass productClass);
    Observable<List<SubUnitsList>> getSubUnits();
    Observable<Long> insertSubUnits(SubUnitsList subUnitsList);
    Observable<Long> insertJoinCustomerGroupWithCustomer(JoinCustomerGroupsWithCustomers joinCustomerGroupWithCustomer);
    Observable<Boolean> insertJoinCustomerGroupWithCustomers(List<JoinCustomerGroupsWithCustomers> joinCustomerGroupsWithCustomers);
    Observable<Boolean> deleteJoinCustomerGroupWithCustomer(String customerGroupId, String customerId);
    Observable<Boolean> deleteJoinCustomerGroupWithCustomer(String customerId);
    Observable<Boolean> deleteAllJoinCustomerGroupWithCustomer();
    Observable<List<JoinCustomerGroupsWithCustomers>> getAllJoinCustomerGroupsWithCustomers();
    Observable<List<CustomerGroup>> getCustomerGroups(Customer customer);

    Observable<Boolean> deleteSubUnits(SubUnitsList subUnitsList);
    Observable<Long> insertOrReplaceRecipe(Recipe recipe);
    Observable<List<Recipe>> getAllRecipe();
    Observable<Boolean> deleteRecipe(Recipe recipe);
    Observable<Long> insertAttribute(Attribute attribute);
    Observable<Boolean> insertAttributes(List<Attribute> attributes);
    Observable<Boolean> deleteAttribute(Attribute attribute);
    Observable<List<Attribute>> getAllAttributes();
    Observable<Long> insertAttributeType(AttributeType attributeType);
    Observable<Boolean> insertAttributeTypes(List<AttributeType> attributeTypes);
    Observable<Boolean> deleteAttributeType(AttributeType attributeType);
    Observable<Boolean> deleteAttributeTypeByName(String name);
    Observable<List<AttributeType>> getAllAttributeTypes();
    Observable<Long> insertChildAttribute(ChildAttribute childAttribute);
    Observable<Boolean> insertChildAttributes(List<ChildAttribute> childAttributes);
    Observable<Boolean> deleteChildAttribute(ChildAttribute childAttribute);
    Observable<List<ChildAttribute>> getAllChildAttributes();
    Observable<Long> insertParentAttribute(ParentAttribute parentAttribute);
    Observable<Boolean> insertParentAttributes(List<ParentAttribute> parentAttributes);
    Observable<Boolean> deleteParentAttribute(ParentAttribute parentAttribute);
    Observable<List<ParentAttribute>> getAllParentAttributes();

}
