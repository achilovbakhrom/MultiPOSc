package com.jim.multipos.data;

import android.content.Context;

import com.jim.multipos.data.db.DbHelper;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.Vendor;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.customer.JoinCustomerGroupsWithCustomers;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.stock.Stock;
import com.jim.multipos.data.db.model.unit.SubUnitsList;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.data.operations.AccountOperations;
import com.jim.multipos.data.operations.CategoryOperations;
import com.jim.multipos.data.operations.ContactOperations;
import com.jim.multipos.data.operations.CurrencyOperations;
import com.jim.multipos.data.operations.CustomerGroupOperations;
import com.jim.multipos.data.operations.CustomerOperations;
import com.jim.multipos.data.operations.DiscountOperations;
import com.jim.multipos.data.operations.JoinCustomerGroupWithCustomerOperations;
import com.jim.multipos.data.operations.PaymentTypeOperations;
import com.jim.multipos.data.operations.ProductClassOperations;
import com.jim.multipos.data.operations.ProductOperations;
import com.jim.multipos.data.operations.ServiceFeeOperations;
import com.jim.multipos.data.operations.StockOperations;
import com.jim.multipos.data.operations.SubUnitOperations;
import com.jim.multipos.data.operations.UnitCategoryOperations;
import com.jim.multipos.data.operations.UnitOperations;
import com.jim.multipos.data.operations.VendorOperations;
import com.jim.multipos.data.prefs.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Developer on 5/13/17.
 */

public class DatabaseManager implements ContactOperations, CategoryOperations, ProductOperations, AccountOperations, CurrencyOperations, StockOperations, UnitCategoryOperations, UnitOperations, PaymentTypeOperations, ServiceFeeOperations, ProductClassOperations, CustomerOperations, CustomerGroupOperations, SubUnitOperations, JoinCustomerGroupWithCustomerOperations, DiscountOperations,
        VendorOperations {
    private Context context;
    private PreferencesHelper preferencesHelper;
    private DbHelper dbHelper;

    public DatabaseManager(Context context, PreferencesHelper preferencesHelper, DbHelper dbHelper) {
        this.context = context;
        this.preferencesHelper = preferencesHelper;
        this.dbHelper = dbHelper;
    }

    public CustomerGroupOperations getCustomerGroupOperations() {
        return this;
    }

    public CustomerOperations getCustomerOperations() {
        return this;
    }

    public ServiceFeeOperations getServiceFeeOperations() {
        return this;
    }

    public ContactOperations getContactOperations() {
        return this;
    }

    public CategoryOperations getCategoryOperations() {
        return this;
    }

    public ProductOperations getProductOperations() {
        return this;
    }

    public DaoSession getDaoSession() {
        return dbHelper.getDaoSession();
    }

    public AccountOperations getAccountOperations() {
        return this;
    }

    public UnitCategoryOperations getUnitCategoryOperations() {
        return this;
    }

    public UnitOperations getUnitOperations() {
        return this;
    }

    public CurrencyOperations getCurrencyOperations() {
        return this;
    }

    public StockOperations getStockOperations() {
        return this;
    }

    public PaymentTypeOperations getPaymentTypeOperations() {
        return this;
    }

    public JoinCustomerGroupWithCustomerOperations getJoinCustomerGroupWithCustomerOperations () {
        return this;
    }

    @Override
    public Observable<Long> addJoinCustomerGroupWithCustomer(JoinCustomerGroupsWithCustomers joinCustomerGroupWithCustomer) {
        return dbHelper.insertJoinCustomerGroupWithCustomer(joinCustomerGroupWithCustomer);
    }

    @Override
    public Observable<Boolean> addJoinCustomerGroupWithCustomers(List<JoinCustomerGroupsWithCustomers> joinCustomerGroupsWithCustomers) {
        return dbHelper.insertJoinCustomerGroupWithCustomers(joinCustomerGroupsWithCustomers);
    }

    @Override
    public Observable<Boolean> removeJoinCustomerGroupWithCustomer(Long customerGroupId, Long customerId) {
        return dbHelper.deleteJoinCustomerGroupWithCustomer(customerGroupId, customerId);
    }

    @Override
    public Observable<Boolean> removeJoinCustomerGroupWithCustomer(Long customerId) {
        return dbHelper.deleteJoinCustomerGroupWithCustomer(customerId);
    }

    @Override
    public Observable<Boolean> removeAllJoinCustomerGroupWithCustomer() {
        return dbHelper.deleteAllJoinCustomerGroupWithCustomer();
    }

    @Override
    public Observable<List<JoinCustomerGroupsWithCustomers>> getAllJoinCustomerGroupsWithCustomers() {
        return dbHelper.getAllJoinCustomerGroupsWithCustomers();
    }

    @Override
    public ArrayList<Contact> getContactsOrganization() {
        return null;
    }

    @Override
    public Observable<Long> addContact(Contact contact) {
        return dbHelper.insertContact(contact);
    }

    @Override
    public Observable<Boolean> addContact(List<Contact> contacts) {
        return dbHelper.insertContacts(contacts);
    }

    @Override
    public Observable<List<Contact>> getAllContacts() {
        return dbHelper.getAllContacts();
    }

    @Override
    public Observable<Long> addCategory(Category category) {
        return dbHelper.insertCategory(category);
    }

    @Override
    public Observable<Long> addSubCategory(Category subcategory) {
        return dbHelper.insertSubCategory(subcategory);
    }

    @Override
    public Observable<Boolean> addCategory(List<Category> categoryList) {
        return dbHelper.insertCategories(categoryList);
    }

    @Override
    public Observable<Long> replaceCategory(Category category) {
        return dbHelper.insertOrReplaceCategory(category);
    }

    @Override
    public Observable<List<Category>> getAllCategories() {
        return dbHelper.getAllCategories();
    }

    @Override
    public Single<List<Category>> getAllActiveCategories() {
        return dbHelper.getAllActiveCategories();
    }

    @Override
    public Single<List<Category>> getAllActiveSubCategories(Category parent) {
        return dbHelper.getAllActiveSubCategories(parent);
    }

    @Override
    public Observable<List<Category>> getSubCategories(Category category) {
        return dbHelper.getSubCategories(category);
    }

    @Override
    public Observable<Integer> getCategoryByName(Category category) {
        return dbHelper.getCategoryByName(category);
    }

    @Override
    public Observable<Boolean> isCategoryNameExists(String name) {
        return dbHelper.isCategoryNameExists(name);
    }

    @Override
    public List<Account> getAccounts() {
        return dbHelper.getAccounts();
    }

    @Override
    public Observable<Integer> getSubCategoryByName(Category category) {
        return dbHelper.getSubCategoryByName(category);
    }

    @Override
    public Observable<Boolean> isSubCategoryNameExists(String parentName, String name) {
        return dbHelper.isSubCategoryNameExists(parentName, name);
    }

    @Override
    public Observable<Account> addAccount(Account account) {
        return dbHelper.insertAccount(account);
    }

    @Override
    public Observable<Boolean> addAccounts(List<Account> accounts) {
        return dbHelper.insertAccounts(accounts);
    }

    @Override
    public Observable<List<Account>> getAllAccounts() {
        return dbHelper.getAllAccounts();
    }



    @Override
    public Observable<Boolean> removeAccount(Account account) {
        return dbHelper.deleteAccount(account);
    }

    @Override
    public Observable<Boolean> removeAllAccounts() {
        return dbHelper.deleteAllAccounts();
    }

    @Override
    public Observable<Boolean> isAccountNameExists(String name) {
        return dbHelper.isAccountNameExists(name);
    }

    @Override
    public Observable<Long> addStock(Stock stock) {
        return dbHelper.insertStock(stock);
    }

    @Override
    public Observable<Boolean> addStocks(List<Stock> stocks) {
        return dbHelper.insertStocks(stocks);
    }

    @Override
    public Observable<List<Stock>> getAllStocks() {
        return dbHelper.getAllStocks();
    }

    @Override
    public Observable<Boolean> removeStock(Stock stock) {
        return dbHelper.deleteStock(stock);
    }

    @Override
    public Observable<Boolean> removeAllStocks() {
        return dbHelper.deleteAllStocks();
    }

    @Override
    public Observable<Long> addUnitCategory(UnitCategory unitCategory) {
        return dbHelper.insertUnitCategory(unitCategory);
    }

    @Override
    public Observable<Boolean> addUnitCategories(List<UnitCategory> categories) {
        return dbHelper.insertUnitCategories(categories);
    }

    @Override
    public Observable<List<UnitCategory>> getAllUnitCategories() {
        return dbHelper.getAllUnitCategories();
    }

    @Override
    public Observable<Long> addUnit(Unit unit) {
        return dbHelper.insertUnit(unit);
    }

    @Override
    public Observable<Boolean> addUnits(List<Unit> units) {
        return dbHelper.insertUnits(units);
    }

    @Override
    public Observable<List<Unit>> getUnits(Long rootId, String name) {
        return dbHelper.getUnits(rootId, name);
    }

    @Override
    public Observable<Unit> updateUnit(Unit unit) {
        return dbHelper.updateUnit(unit);
    }

    @Override
    public Observable<List<Unit>> getAllStaticUnits() {
        return dbHelper.getAllStaticUnits();
    }

    @Override
    public Observable<Boolean> removeAllUnits() {
        return dbHelper.deleteAllUnits();
    }

    @Override
    public Observable<Boolean> removeUnit(Unit unit) {
        return dbHelper.deleteUnit(unit);
    }

    @Override
    public Observable<Long> addCurrency(Currency currency) {
        return dbHelper.addCurrency(currency);
    }

    @Override
    public List<Currency> getCurrencies() {
        return dbHelper.getCurrencies();
    }

    @Override
    public Observable<Boolean> removeCurrency(Currency currency) {
        return dbHelper.deleteCurrency(currency);
    }

    @Override
    public Observable<List<Currency>> getAllCurrencies() {
        return dbHelper.getAllCurrencies();
    }

    @Override
    public Observable<Boolean> addCurrencies(List<Currency> currencies) {
        return dbHelper.insertCurrencies(currencies);
    }

    @Override
    public Observable<Boolean> removeAllCurrencies() {
        return dbHelper.deleteAllCurrencies();
    }

    @Override
    public Observable<Long> addPaymentType(PaymentType paymentType) {
        return dbHelper.insertPaymentType(paymentType);
    }

    @Override
    public List<PaymentType> getPaymentTypes() {
        return null;
    }

    @Override
    public Observable<Boolean> addPaymentTypes(List<PaymentType> paymentTypes) {
        return dbHelper.insertPaymentTypes(paymentTypes);
    }

    @Override
    public Observable<Boolean> removePaymentType(PaymentType paymentType) {
        return dbHelper.deletePaymentType(paymentType);
    }

    @Override
    public Observable<Boolean> removeAllPaymentTypes() {
        return dbHelper.deleteAllPaymentTypes();
    }

    @Override
    public Observable<List<PaymentType>> getAllPaymentTypes() {
        return dbHelper.getAllPaymentTypes();
    }

    @Override
    public Observable<Boolean> isPaymentTypeNameExists(String name) {
        return dbHelper.isPaymentTypeExists(name);
    }

    @Override
    public Observable<Long> addServiceFee(ServiceFee serviceFee) {
        return dbHelper.insertServiceFee(serviceFee);
    }

    @Override
    public Observable<Boolean> addServiceFees(List<ServiceFee> serviceFeeList) {
        return dbHelper.insertServiceFees(serviceFeeList);
    }

    @Override
    public Observable<List<ServiceFee>> getAllServiceFees() {
        return dbHelper.getAllServiceFees();
    }

    @Override
    public Observable<Boolean> removeAllServiceFees() {
        return dbHelper.deleteAllServiceFees();
    }

    @Override
    public Observable<Boolean> removeServiceFee(ServiceFee serviceFee) {
        return dbHelper.deleteServiceFee(serviceFee);
    }

    @Override
    public Observable<Long> addCustomer(Customer customer) {
        return dbHelper.insertCustomer(customer);
    }

    @Override
    public Observable<Boolean> addCustomers(List<Customer> customers) {
        return dbHelper.insertCustomers(customers);
    }

    @Override
    public Observable<Boolean> removeCustomer(Customer customer) {
        return dbHelper.deleteCustomer(customer);
    }

    @Override
    public Observable<Boolean> removeAllCustomers() {
        return dbHelper.deleteAllCustomers();
    }

    @Override
    public Observable<List<Customer>> getAllCustomers() {
        return dbHelper.getAllCustomers();
    }

    @Override
    public Observable<Boolean> isCustomerExists(String name) {
        return dbHelper.isCustomerExists(name);
    }

    @Override
    public Single<List<ProductClass>> getAllProductClass() {
        return dbHelper.getAllProductClass();
    }

    @Override
    public Single<Long> insertProductClass(ProductClass productClass) {
        return dbHelper.insertProductClass(productClass);
    }

    @Override
    public Observable<Long> addProduct(Product product) {
        return dbHelper.insertProduct(product);
    }

    @Override
    public Observable<Boolean> addProduct(List<Product> productList) {
        return dbHelper.insertProducts(productList);
    }

    @Override
    public Observable<Long> replaceProduct(Product product) {
        return dbHelper.insertOrReplaceProduct(product);
    }

    @Override
    public Observable<List<Product>> getAllProducts() {
        return dbHelper.getAllProducts();
    }

    @Override
    public Single<List<Product>> getAllActiveProducts(Category category) {
        return dbHelper.getAllActiveProducts(category);
    }

    @Override
    public Observable<Long> addSubUnitList(SubUnitsList subUnitsList) {
        return dbHelper.insertSubUnits(subUnitsList);
    }

    @Override
    public Observable<List<SubUnitsList>> getSubUnitList() {
        return dbHelper.getSubUnits();
    }

    @Override
    public Observable<Boolean> deleteSubUnitList(SubUnitsList subUnitsList) {
        return dbHelper.deleteSubUnits(subUnitsList);
    }

    @Override
    public Observable<Long> addCustomerGroup(CustomerGroup customerGroup) {
        return dbHelper.insertCustomerGroup(customerGroup);
    }

    @Override
    public Observable<Boolean> addCustomerGroups(List<CustomerGroup> customerGroups) {
        return dbHelper.insertCustomerGroups(customerGroups);
    }

    @Override
    public Observable<Boolean> removeCustomerGroup(CustomerGroup customerGroup) {
        return dbHelper.deleteCustomerGroup(customerGroup);
    }

    @Override
    public Observable<Boolean> removeAllCustomerGroups() {
        return dbHelper.deleteAllCustomers();
    }

    @Override
    public Observable<List<CustomerGroup>> getAllCustomerGroups() {
        return dbHelper.getAllCustomerGroups();
    }

    @Override
    public Observable<List<CustomerGroup>> getCustomerGroups(Customer customer) {
        return dbHelper.getCustomerGroups(customer);
    }

    @Override
    public Observable<Boolean> isCustomerGroupExists(String name) {
        return dbHelper.isCustomerGroupExists(name);
    }

    @Override
    public Observable<Long> addVendor(Vendor vendor) {
        return dbHelper.addVendor(vendor);
    }

    @Override
    public Observable<Boolean> addVendors(List<Vendor> vendors) {
        return dbHelper.addVendors(vendors);
    }

    @Override
    public Observable<Boolean> isVendorNameExist(String name) {
        return dbHelper.isVendorNameExist(name);
    }

    @Override
    public Observable<Boolean> updateContacts(Long vendorId, List<Contact> contacts) {
        return dbHelper.updateContacts(vendorId, contacts);
    }


    @Override
    public Observable<Boolean> deleteVendor(Long vendorId) {
        return dbHelper.deleteVendor(vendorId);
    }

    @Override
    public Observable<Vendor> getVendorById(Long vendorId) {
        return dbHelper.getVendorById(vendorId);
    }

    @Override
    public Observable<List<Vendor>> getVendors() {
        return dbHelper.getVendors();
    }

    @Override
    public Observable<Boolean> removeAllContacts(Long vendorId) {
        return dbHelper.removeAllContacts(vendorId);
    }

    @Override
    public Observable<Category> getCategoryById(Long id) {
        return dbHelper.getCategoryById(id);
    }

    @Override
    public Observable<Boolean> removeCategory(Category category) {
        return dbHelper.removeCategory(category);
    }

    @Override
    public Single<List<Discount>> getAllDiscounts() {
        return dbHelper.getAllDiscounts();
    }

    @Override
    public Single<Long> insertDiscount(Discount discount) {
        return dbHelper.insertDiscount(discount);
    }
}

