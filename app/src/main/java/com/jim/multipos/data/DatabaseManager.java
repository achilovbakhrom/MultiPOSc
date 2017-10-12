package com.jim.multipos.data;

import android.content.Context;

import com.jim.multipos.data.db.DbHelper;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.customer.JoinCustomerGroupsWithCustomers;
import com.jim.multipos.data.db.model.intosystem.CategoryPosition;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.intosystem.ProductPosition;
import com.jim.multipos.data.db.model.intosystem.SubCategoryPosition;
import com.jim.multipos.data.db.model.matrix.Attribute;
import com.jim.multipos.data.db.model.matrix.AttributeType;
import com.jim.multipos.data.db.model.matrix.ChildAttribute;
import com.jim.multipos.data.db.model.matrix.ParentAttribute;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Recipe;
import com.jim.multipos.data.db.model.products.SubCategory;
import com.jim.multipos.data.db.model.stock.Stock;
import com.jim.multipos.data.db.model.unit.SubUnitsList;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.data.operations.*;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.prefs.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Developer on 5/13/17.
 */

public class DatabaseManager implements ContactOperations, CategoryOperations, SubCategoryOperations, ProductOperations, PositionOperations, AccountOperations, CurrencyOperations, StockOperations, UnitCategoryOperations, UnitOperations, PaymentTypeOperations, ServiceFeeOperations, ProductClassOperations, CustomerOperations, CustomerGroupOperations, SubUnitOperations, JoinCustomerGroupWithCustomerOperations, RecipeOperations, MatrixOptions {
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

    public SubCategoryOperations getSubCategoryOperations() {
        return this;
    }

    public PositionOperations getPositionOperations() {
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
    public Observable<Boolean> removeJoinCustomerGroupWithCustomer(String customerGroupId, String customerId) {
        return dbHelper.deleteJoinCustomerGroupWithCustomer(customerGroupId, customerId);
    }

    @Override
    public Observable<Boolean> removeJoinCustomerGroupWithCustomer(String customerId) {
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
    public Observable<Boolean> addCategory(List<Category> categoryList) {
        return dbHelper.insertCategories(categoryList);
    }

    @Override
    public Observable<Long> replaceCategory(Category category) {
        return dbHelper.insertOrReplaceCategory(category);
    }

    @Override
    public Observable<Long> replaceCategoryByPosition(Category category) {
        return dbHelper.insertOrReplaceCategoryByPosition(category);
    }

    @Override
    public Observable<List<Category>> getAllCategories() {
        return dbHelper.getAllCategories();
    }

    @Override
    public Observable<Integer> getCategoryByName(Category category) {
        return dbHelper.getCategoryByName(category);
    }

    @Override
    public Observable<Boolean> getMatchCategory(Category category) {
        return dbHelper.getMatchCategory(category);
    }

    @Override
    public Observable<Long> addSubCategory(SubCategory subCategory) {
        return dbHelper.insertSubCategory(subCategory);
    }


    @Override
    public Observable<Boolean> addSubCategory(List<SubCategory> subCategoryList) {
        return dbHelper.insertSubCategories(subCategoryList);
    }

    @Override
    public Observable<Long> replaceSubCategory(SubCategory subCategory) {
        return dbHelper.insertOrReplaceSubCategory(subCategory);
    }

    @Override
    public Observable<List<SubCategory>> getAllSubCategories() {
        return dbHelper.getAllSubCategories();
    }

    @Override
    public Observable<Boolean> addCategoryPositions(List<CategoryPosition> positionList) {
        return dbHelper.insertCategoryPositions(positionList);
    }

    @Override
    public Observable<List<Category>> getAllCategoryPositions() {
        return dbHelper.getAllCategoryPositions();
    }

    @Override
    public Observable<Long> replaceCategoryPosition(CategoryPosition position) {
        return dbHelper.insertOrReplaceCategoryPosition(position);
    }

    @Override
    public Observable<Boolean> addSubCategoryPositions(List<SubCategoryPosition> positionList, Category category) {
        return dbHelper.insertSubCategoryPositions(positionList, category);
    }

    @Override
    public Observable<List<SubCategory>> getAllSubCategoryPositions(Category category) {
        return dbHelper.getAllSubCategoryPositions(category);
    }

    @Override
    public Observable<Boolean> addProductPositions(List<ProductPosition> positionList, SubCategory subCategory) {
        return dbHelper.insertProductPositions(positionList, subCategory);
    }

    @Override
    public Observable<List<Product>> getAllProductPositions(SubCategory subCategory) {
        return dbHelper.getAllProductPositions(subCategory);
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
    public Boolean isAccountNameExists(String name) {
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
    public Observable<Long> addRecipe(Recipe recipe) {
        return dbHelper.insertOrReplaceRecipe(recipe);
    }

    @Override
    public Observable<List<Recipe>> getAllRecipes() {
        return dbHelper.getAllRecipe();
    }

    @Override
    public Observable<Boolean> deleteRecipes(Recipe recipe) {
        return dbHelper.deleteRecipe(recipe);
    }

    @Override
    public Observable<Long> addAttribute(Attribute attribute) {
        return dbHelper.insertAttribute(attribute);
    }

    @Override
    public Observable<Boolean> addAttributes(List<Attribute> attributes) {
        return dbHelper.insertAttributes(attributes);
    }

    @Override
    public Observable<List<Attribute>> getAllAttributes() {
        return dbHelper.getAllAttributes();
    }

    @Override
    public Observable<Boolean> removeAttribute(Attribute attribute) {
        return dbHelper.deleteAttribute(attribute);
    }

    @Override
    public Observable<Long> addAttributeType(AttributeType attributeType) {
        return dbHelper.insertAttributeType(attributeType);
    }

    @Override
    public Observable<Boolean> addAttributeTypes(List<AttributeType> attributeTypes) {
        return dbHelper.insertAttributeTypes(attributeTypes);
    }

    @Override
    public Observable<List<AttributeType>> getAllAttributeTypes() {
        return dbHelper.getAllAttributeTypes();
    }

    @Override
    public Observable<Boolean> removeAttributeTypes(AttributeType attributeType) {
        return dbHelper.deleteAttributeType(attributeType);
    }

    @Override
    public Observable<Boolean> removeAttributeTypesByName(String name) {
        return dbHelper.deleteAttributeTypeByName(name);
    }

    @Override
    public Observable<Long> addChildAttribute(ChildAttribute childAttribute) {
        return dbHelper.insertChildAttribute(childAttribute);
    }

    @Override
    public Observable<Boolean> addChildAttributes(List<ChildAttribute> childAttributes) {
        return dbHelper.insertChildAttributes(childAttributes);
    }

    @Override
    public Observable<List<ChildAttribute>> getAllChildAttributes() {
        return dbHelper.getAllChildAttributes();
    }

    @Override
    public Observable<Boolean> removeChildAttribute(ChildAttribute childAttribute) {
        return dbHelper.deleteChildAttribute(childAttribute);
    }

    @Override
    public Observable<Long> addParentAttribute(ParentAttribute parentAttribute) {
        return dbHelper.insertParentAttribute(parentAttribute);
    }

    @Override
    public Observable<Boolean> addParentAttributes(List<ParentAttribute> parentAttributes) {
        return dbHelper.insertParentAttributes(parentAttributes);
    }

    @Override
    public Observable<List<ParentAttribute>> getAllParentAttributes() {
        return dbHelper.getAllParentAttributes();
    }

    @Override
    public Observable<Boolean> removeParentAttribute(ParentAttribute parentAttribute) {
        return dbHelper.deleteParentAttribute(parentAttribute);
    }
}
