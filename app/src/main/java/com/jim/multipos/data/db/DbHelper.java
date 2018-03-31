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

import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.customer.CustomerPayment;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.inventory.HistoryInventoryState;
import com.jim.multipos.data.db.model.inventory.InventoryState;
import com.jim.multipos.data.db.model.inventory.WarehouseOperations;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderChangesLog;
import com.jim.multipos.data.db.model.order.OrderProduct;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.db.model.products.Return;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.data.db.model.stock.Stock;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillManagementOperation;
import com.jim.multipos.data.db.model.till.TillDetails;
import com.jim.multipos.data.db.model.till.TillOperation;
import com.jim.multipos.data.db.model.unit.SubUnitsList;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.ui.vendor_item_managment.model.VendorWithDebt;

import org.greenrobot.greendao.query.LazyList;

import java.util.Calendar;
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
    Observable<Product> getProductById(Long productId);
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
    Observable<List<ServiceFee>> getServiceFeesWithAllItemTypes();
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
    Observable<Long> insertCustomerToCustomerGroup(Long customerGroupId, Long customerId);
    Observable<Boolean> deleteCustomerFromCustomerGroup(Long customerGroupId, Long customerId);
    Observable<Boolean> deleteJoinCustomerGroupWithCustomer(Long customerId);
    Observable<Boolean> isCustomerGroupExists(String name);
    Observable<List<CustomerGroup>> getAllCustomerGroups();
    Observable<CustomerGroup> getCustomerGroupByName(String name);
    Observable<CustomerGroup> getCustomerGroupById(long id);
    Single<List<ProductClass>> getAllProductClass();
    Single<Long> insertProductClass(ProductClass productClass);
    Observable<ProductClass> getProductClass(Long id);
    Observable<List<SubUnitsList>> getSubUnits();
    Observable<Long> insertSubUnits(SubUnitsList subUnitsList);
    Observable<List<CustomerGroup>> getCustomerGroups(Customer customer);
    List<Currency> getCurrencies();
    Observable<Boolean> deleteSubUnits(SubUnitsList subUnitsList);
    Observable<Boolean> isAccountNameExists(String name);
    Observable<Boolean> isPaymentTypeExists(String name);
    List<PaymentType> getPaymentTypes();
    Single<List<Discount>> getAllDiscounts();
    Single<Long> insertDiscount(Discount discount);
    Observable<List<Discount>> getDiscountsWithAllItemTypes(String[] discountTypes);

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
    Observable<List<Category>> getActiveCategories();

    //Product
    Observable<Boolean> isProductNameExists(String productName, Long categoryId);
    Observable<Boolean> removeProduct(Product product);
    //Inventory
    Single<List<InventoryItem>> getInventoryItems();
    Observable<Long> addProductVendorConn(VendorProductCon vendorProductCon);
    Observable<Boolean> removeVendorProductConnection(VendorProductCon vendorProductCon);
    Observable<Boolean> removeVendorProductConnectionByVendorId(Long vendorId);
    Observable<Boolean> removeVendorProductConnectionByProductId(Long productId);
    Observable<List<VendorProductCon>> getVendorProductConnectionByProductId(Long productId);
    Observable<VendorProductCon> getVendorProductConnectionById(Long productId, Long vendorId);

    Single<Consignment> insertConsignment(Consignment consignment);
    Observable<Long> insertConsignmentProduct(ConsignmentProduct consignmentProduct);
    Observable<List<Consignment>> getConsignments();
    Single<List<Consignment>> getConsignmentsByVendorId(Long vendorId);
    Single<List<ConsignmentProduct>> getConsignmentProductsByConsignmentId(Long consignmentId);
    Observable<Boolean> insertConsignmentProduct(List<ConsignmentProduct> consignmentProducts);
    Single<Consignment> getConsignmentById(Long consignmentId);

    Single<List<VendorWithDebt>> getVendorWirhDebt();
    Single<Double> getVendorDebt(Long vendorId);

    Single<Long> insertWarehouseOperation(WarehouseOperations warehouseOperations);
    Single<WarehouseOperations> getWarehouseOperationById(Long warehouseId);
    Single<Long> replaceWarehouseOperation(WarehouseOperations warehouseOperations);

    Single<BillingOperations> insertBillingOperation(BillingOperations billingOperations);
    Observable<List<BillingOperations>> getBillingOperations();
    Single<BillingOperations> getBillingOperationsByConsignmentId(Long consignmentId);
    Single<BillingOperations> getBillingOperationsById(Long firstPayId);
    Single<List<BillingOperations>> getBillingOperationByRootId(Long rootId);

    Observable<Long> insertInventoryState(InventoryState inventoryState);
    Observable<List<InventoryState>> getInventoryStates();
    Observable<List<InventoryState>> getInventoryStatesByProductId(Long productId);
    Single<List<BillingOperations>> getBillingOperationInteval(Long vendorId, Calendar fromDate, Calendar toDate);
    Single<List<BillingOperations>> getBillingOperationsByInterval(Calendar fromDate, Calendar toDate);

    Observable<Boolean> removeProductFromInventoryState(Long productId);
    Observable<List<InventoryState>> getInventoryStatesByVendorId(Long vendorId);
    Currency getMainCurrency();
    Single<List<BillingOperations>> getBillingOperationForVendor(Long vendorId);

    Single<Boolean> deleteInventoryState(InventoryState inventoryState);

    Single<List<Consignment>> getConsignmentsInInterval(Long vendorId, Calendar fromDate, Calendar toDate);

    Single<Customer> getCustomerById(Long customerId);
    Single<List<Customer>> getCustomersWithDebt();
    Single<Boolean> insertDebt(Debt debt);
    Single<List<Debt>> getDebtsByCustomerId(Long id);
    Single<List<Debt>> getAllActiveDebts();
    Single<CustomerPayment> insertCustomerPayment(CustomerPayment payment);
    Single<List<CustomerPayment>> getCustomerPaymentsByInterval(Calendar fromDate, Calendar toDate);
    Single<Order> insertOrder(Order order);

    Single<Boolean> insertReturns(List<Return> returnsList);

    Single<List<VendorProductCon>> getVendorProductConnectionByVendorId(Long vendorId);
    Single<PaymentType> getDebtPaymentType();
    Single<List<PayedPartitions>> insertPayedPartitions(List<PayedPartitions> payedPartitions);
    Single<List<OrderProduct>> insertOrderProducts(List<OrderProduct> orderProducts);
    Single<List<Order>> getAllTillOrders();
    Single<LazyList<Order>> getAllTillLazzyOrders();
    //till management operations
    Single<TillOperation> insertTillOperation(TillOperation tillOperation);
    Single<TillDetails> insertTillDetails(TillDetails tillDetails);
    Single<Till> insertTill(Till till);
    Single<List<TillOperation>> getTillOperationsByAccountId(Long id, Long tillId);
    Single<Till> getOpenTill();
    Single<Long> getCurrentOpenTillId();
    Single<Boolean> isHaveOpenTill();
    Single<Boolean> isNoTills();
    Single<Till> getLastClosedTill();
    Single<Integer> removeAllOrders();
    Single<TillManagementOperation> insertTillCloseOperation(TillManagementOperation tillCloseOperation);
    Single<Till> getTillById(Long tillId);
    Single<List<TillManagementOperation>> insertTillCloseOperationList(List<TillManagementOperation> tillCloseOperations);
    Single<List<TillManagementOperation>> getTillManagementOperationsByTillId(Long id);
    Single<Double> getTotalTillOperationsAmount(Long accountId, Long tillId, int type);
    Single<Double> getTotalTillManagementOperationsAmount(Long accountId, Long tillId, int type);
    Single<Double> getBillingOperationsAmountInInterval(Long accountId, Calendar fromDate, Calendar toDate);
    Single<Double> getCustomerPaymentsInInterval(Long id, Calendar fromDate, Calendar toDate);
    Single<List<Order>> getOrdersByTillId(Long id);
    Single<TillDetails> getTillDetailsByAccountId(Long accountId, Long tillId);

    Single<List<Order>> getAllHoldOrders();
    Single<Long> insertOrderChangeLog(OrderChangesLog orderChangesLog);
    Single<Long> getLastOrderId();
    Single<Long> getLastArchiveOrderId();
    Single<Order> getOrder(Long orderId);
    Single<Boolean> deleteDebt(Debt debt);
    Single<Long> deleteOrderProductsOnHold(List<OrderProduct> orderProducts);
    Single<Long> deletePayedPartitions(List<PayedPartitions> payedPartitions);
    Single<List<Order>> getAllTillClosedOrders();
    Single<Product> getProductByRootId(Long rootId);
    Single<HistoryInventoryState> insertHistoryInventoryState(HistoryInventoryState state);
    Single<Boolean> isProductSkuExists(String sku, Long subcategoryId);
    Single<Boolean> isConsignmentNumberExists(String number);
    Single<Vendor> detachVendor(Vendor vendor);
    Single<List<Till>> getAllTills();
    Single<List<TillDetails>> getTillDetailsByTillId(Long tillId);
    Single<List<Till>> getAllClosedTills();
}
