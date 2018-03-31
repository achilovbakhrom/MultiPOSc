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
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.products.Category;
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
import com.jim.multipos.data.operations.AccountOperations;
import com.jim.multipos.data.operations.BillingTransactionOperations;
import com.jim.multipos.data.operations.OrderOperations;
import com.jim.multipos.data.operations.PayedPartitionOperations;
import com.jim.multipos.data.operations.PaymentOperations;
import com.jim.multipos.data.operations.CategoryOperations;
import com.jim.multipos.data.operations.ConsignmentOperations;
import com.jim.multipos.data.operations.ContactOperations;
import com.jim.multipos.data.operations.CurrencyOperations;
import com.jim.multipos.data.operations.CustomerGroupOperations;
import com.jim.multipos.data.operations.CustomerOperations;
import com.jim.multipos.data.operations.DiscountOperations;
import com.jim.multipos.data.operations.InventoryOperations;
import com.jim.multipos.data.operations.JoinCustomerGroupWithCustomerOperations;
import com.jim.multipos.data.operations.PaymentTypeOperations;
import com.jim.multipos.data.operations.ProductClassOperations;
import com.jim.multipos.data.operations.ProductOperations;
import com.jim.multipos.data.operations.SearchOperations;
import com.jim.multipos.data.operations.ServiceFeeOperations;
import com.jim.multipos.data.operations.StockOperations;
import com.jim.multipos.data.operations.SubUnitOperations;
import com.jim.multipos.data.operations.TillOperations;
import com.jim.multipos.data.operations.UnitCategoryOperations;
import com.jim.multipos.data.operations.UnitOperations;
import com.jim.multipos.data.operations.VendorItemManagmentOperations;
import com.jim.multipos.data.operations.VendorOperations;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.ui.vendor_item_managment.model.VendorWithDebt;

import org.greenrobot.greendao.query.LazyList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Developer on 5/13/17.
 */

public class DatabaseManager implements ContactOperations, CategoryOperations, ProductOperations, AccountOperations, CurrencyOperations, StockOperations, UnitCategoryOperations, UnitOperations, PaymentTypeOperations, ServiceFeeOperations, ProductClassOperations, CustomerOperations, CustomerGroupOperations, SubUnitOperations, JoinCustomerGroupWithCustomerOperations, DiscountOperations,
        VendorOperations, SearchOperations, ConsignmentOperations, InventoryOperations, VendorItemManagmentOperations, PaymentOperations, BillingTransactionOperations, OrderOperations, PayedPartitionOperations, TillOperations {
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

    public JoinCustomerGroupWithCustomerOperations getJoinCustomerGroupWithCustomerOperations() {
        return this;
    }

    @Override
    public Observable<Long> addCustomerToCustomerGroup(Long customerId, Long customerGroupId) {
        return dbHelper.insertCustomerToCustomerGroup(customerId, customerGroupId);
    }

    @Override
    public Observable<Boolean> removeCustomerFromCustomerGroup(Long customerGroupId, Long customerId) {
        return dbHelper.deleteCustomerFromCustomerGroup(customerGroupId, customerId);
    }

    @Override
    public Observable<Boolean> removeJoinCustomerGroupWithCustomer(Long customerId) {
        return dbHelper.deleteJoinCustomerGroupWithCustomer(customerId);
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
    public Currency getMainCurrency() {
        return dbHelper.getMainCurrency();
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
        return dbHelper.getPaymentTypes();
    }

    @Override
    public Single<PaymentType> getDebtPaymentType() {
        return dbHelper.getDebtPaymentType();
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
    public Observable<List<ServiceFee>> getServiceFeesWithAllItemTypes() {
        return dbHelper.getServiceFeesWithAllItemTypes();
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
    public Single<Customer> getCustomerById(Long customerId) {
        return dbHelper.getCustomerById(customerId);
    }

    @Override
    public Single<List<Customer>> getCustomersWithDebt() {
        return dbHelper.getCustomersWithDebt();
    }

    @Override
    public Single<Boolean> addDebt(Debt debt) {
        return dbHelper.insertDebt(debt);
    }

    @Override
    public Single<Boolean> deleteDebt(Debt debt) {
        return dbHelper.deleteDebt(debt);
    }

    @Override
    public Single<List<Debt>> getDebtsByCustomerId(Long id) {
        return dbHelper.getDebtsByCustomerId(id);
    }

    @Override
    public Single<List<Debt>> getAllActiveDebts() {
        return dbHelper.getAllActiveDebts();
    }

    @Override
    public Single<CustomerPayment> addCustomerPayment(CustomerPayment payment) {
        return dbHelper.insertCustomerPayment(payment);
    }

    @Override
    public Single<List<CustomerPayment>> getCustomerPaymentsByInterval(Calendar fromDate, Calendar toDate) {
        return dbHelper.getCustomerPaymentsByInterval(fromDate, toDate);
    }

    @Override
    public Single<Double> getCustomerPaymentsInInterval(Long id, Calendar fromDate, Calendar toDate) {
        return dbHelper.getCustomerPaymentsInInterval(id, fromDate, toDate);
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
    public Observable<ProductClass> getProductClass(Long id) {
        return dbHelper.getProductClass(id);
    }

    @Override
    public Observable<Boolean> removeProduct(Product product) {
        return dbHelper.removeProduct(product);
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
    public Observable<Boolean> isProductNameExists(String productName, Long categoryId) {
        return dbHelper.isProductNameExists(productName, categoryId);
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
    public Observable<Integer> getAllProductCount(Category category) {
        return dbHelper.getAllProductsCount(category);
    }

    @Override
    public Observable<Product> getProductById(Long productId) {
        return dbHelper.getProductById(productId);
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
    public Observable<CustomerGroup> getCustomerGroupByName(String name) {
        return dbHelper.getCustomerGroupByName(name);
    }

    @Override
    public Observable<CustomerGroup> getCustomerGroupById(long id) {
        return dbHelper.getCustomerGroupById(id);
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
    public Single<Vendor> detachVendor(Vendor vendor) {
        return dbHelper.detachVendor(vendor);
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

    @Override
    public Observable<List<Discount>> getDiscountsWithAllItemTypes(String[] discountTypes) {
        return dbHelper.getDiscountsWithAllItemTypes(discountTypes);
    }

    @Override
    public Single<List<Product>> getSearchProducts(String searchText, boolean skuMode, boolean barcodeMode, boolean nameMode) {
        return dbHelper.getSearchProducts(searchText, skuMode, barcodeMode, nameMode);
    }


    @Override
    public Single<Consignment> insertConsignment(Consignment consignment, List<BillingOperations> operations, List<ConsignmentProduct> consignmentProductList, List<WarehouseOperations> warehouseOperationsList) {
        return dbHelper.insertConsignment(consignment)
                .flatMap(consignment1 -> setBillingOperation(operations, consignment1)
                        .flatMap(consignment2 -> setConsignmentProductId(consignmentProductList, warehouseOperationsList, consignment2)));
    }

    private Single<Consignment> setBillingOperation(List<BillingOperations> billingOperations, Consignment consignment) {
        return Single.create(singleSubscriber -> {
            try {
                if (billingOperations != null)
                    for (int i = 0; i < billingOperations.size(); i++) {
                        if (billingOperations.get(i).getOperationType().equals(BillingOperations.DEBT_CONSIGNMENT)) {
                            billingOperations.get(i).setConsignment(consignment);
                            billingOperations.get(i).setConsignmentId(consignment.getId());
                            insertBillingOperation(billingOperations.get(i)).subscribe();
                        }
                        if (billingOperations.get(i).getOperationType().equals(BillingOperations.RETURN_TO_VENDOR)) {
                            billingOperations.get(i).setConsignment(consignment);
                            billingOperations.get(i).setConsignmentId(consignment.getId());
                            insertBillingOperation(billingOperations.get(i)).subscribe();
                        } else if (billingOperations.get(i).getOperationType().equals(BillingOperations.PAID_TO_CONSIGNMENT)) {
                            billingOperations.get(i).setConsignment(consignment);
                            billingOperations.get(i).setConsignmentId(consignment.getId());
                            insertBillingOperation(billingOperations.get(i)).subscribe(operations -> {
                                consignment.setFirstPayId(operations.getId());
                                dbHelper.insertConsignment(consignment).blockingGet();
                            });
                        }
                    }
                singleSubscriber.onSuccess(consignment);
            } catch (Exception o) {
                singleSubscriber.onError(o);
            }
        });
    }

    private Single<Consignment> setConsignmentProductId(List<ConsignmentProduct> consignmentProductList, List<WarehouseOperations> warehouseOperations, Consignment consignment) {
        return Single.create(singleSubscriber -> {
            try {
                if (consignmentProductList != null)
                    for (int i = 0; i < consignmentProductList.size(); i++) {
                        consignmentProductList.get(i).setConsignmentId(consignment.getId());
                        if (warehouseOperations.get(i) != null) {
                            insertWarehouseOperation(warehouseOperations.get(i)).blockingGet();
                            consignmentProductList.get(i).setWarehouse(warehouseOperations.get(i));
                            consignmentProductList.get(i).setWarehouseId(warehouseOperations.get(i).getId());
                        }
                        insertConsignmentProduct(consignmentProductList.get(i)).blockingSingle();
                    }
                singleSubscriber.onSuccess(consignment);
            } catch (Exception o) {
                singleSubscriber.onError(o);
            }
        });
    }

    @Override
    public Observable<Long> insertConsignmentProduct(ConsignmentProduct consignment) {
        return dbHelper.insertConsignmentProduct(consignment);
    }

    @Override
    public Observable<List<Consignment>> getConsignments() {
        return dbHelper.getConsignments();
    }

    @Override
    public Single<List<Consignment>> getConsignmentsByVendorId(Long vendorId) {
        return dbHelper.getConsignmentsByVendorId(vendorId);
    }

    @Override
    public Observable<Boolean> insertConsignmentProducts(List<ConsignmentProduct> consignmentProducts) {
        return dbHelper.insertConsignmentProduct(consignmentProducts);
    }

    @Override
    public Single<List<ConsignmentProduct>> getConsignmentProductsByConsignmentId(Long consignmentId) {
        return dbHelper.getConsignmentProductsByConsignmentId(consignmentId);
    }

    @Override
    public Single<Consignment> getConsignmentById(Long consignmentId) {
        return dbHelper.getConsignmentById(consignmentId);
    }

    @Override
    public Single<List<Consignment>> getConsignmentsInInterval(Long vendorId, Calendar fromDate, Calendar toDate) {
        return dbHelper.getConsignmentsInInterval(vendorId, fromDate, toDate);
    }

    @Override
    public Single<Boolean> isConsignmentNumberExists(String number) {
        return dbHelper.isConsignmentNumberExists(number);
    }

    @Override
    public Single<List<InventoryItem>> getInventoryItems() {
        return dbHelper.getInventoryItems().map(inventoryItems -> {
            Collections.sort(inventoryItems, (inventoryItem, t1) -> inventoryItem.getProduct().getCreatedDate().compareTo(t1.getProduct().getCreatedDate()));
            return inventoryItems;
        });
    }

    @Override
    public Single<Long> insertWarehouseOperation(WarehouseOperations warehouseOperations) {
        return dbHelper.insertWarehouseOperation(warehouseOperations);
    }

    @Override
    public Single<WarehouseOperations> getWarehouseOperationById(Long warehouseId) {
        return dbHelper.getWarehouseOperationById(warehouseId);
    }

    @Override
    public Single<Long> replaceWarehouseOperation(WarehouseOperations warehouseOperations) {
        return dbHelper.replaceWarehouseOperation(warehouseOperations);
    }

    @Override
    public Single<HistoryInventoryState> insertHistoryInventoryState(HistoryInventoryState state) {
        return dbHelper.insertHistoryInventoryState(state);
    }

    @Override
    public Observable<Long> insertInventoryState(InventoryState inventoryState) {
        return dbHelper.insertInventoryState(inventoryState);
    }

    @Override
    public Observable<List<InventoryState>> getInventoryStates() {
        return dbHelper.getInventoryStates();
    }

    @Override
    public Observable<List<InventoryState>> getInventoryStatesByProductId(Long productId) {
        return dbHelper.getInventoryStatesByProductId(productId);
    }

    @Override
    public Observable<List<InventoryState>> getInventoryStatesByVendorId(Long vendorId) {
        return dbHelper.getInventoryStatesByVendorId(vendorId);
    }

    @Override
    public Observable<Long> addVendorProductConnection(VendorProductCon vendorProductCon) {
        return dbHelper.addProductVendorConn(vendorProductCon);
    }

    @Override
    public Observable<Boolean> removeVendorProductConnection(VendorProductCon vendorProductCon) {
        return null;
    }

    @Override
    public Observable<Boolean> removeVendorProductConnectionByVendorId(Long vendorId) {
        return dbHelper.removeVendorProductConnectionByVendorId(vendorId);
    }

    @Override
    public Observable<Boolean> removeVendorProductConnectionByProductId(Long productId) {
        return dbHelper.removeVendorProductConnectionByProductId(productId);
    }

    @Override
    public Observable<List<VendorProductCon>> getVendorProductConnectionByProductId(Long productId) {
        return dbHelper.getVendorProductConnectionByProductId(productId);
    }

    @Override
    public Observable<VendorProductCon> getVendorProductConnectionById(Long productId, Long vendorId) {
        return dbHelper.getVendorProductConnectionById(productId, vendorId);
    }

    @Override
    public Single<Boolean> insertReturns(List<Return> returnsList) {
        return dbHelper.insertReturns(returnsList);
    }

    @Override
    public Single<List<VendorProductCon>> getVendorProductConnectionByVendorId(Long vendorId) {
        return dbHelper.getVendorProductConnectionByVendorId(vendorId);
    }

    @Override
    public Single<Product> getProductByRootId(Long rootId) {
        return dbHelper.getProductByRootId(rootId);
    }

    @Override
    public Single<Boolean> isProductSkuExists(String sku, Long subcategoryId) {
        return dbHelper.isProductSkuExists(sku, subcategoryId);
    }

    @Override
    public Observable<List<Category>> getActiveCategories() {
        return dbHelper.getActiveCategories();
    }

    @Override
    public Single<List<VendorWithDebt>> getVendorWirhDebt() {
        return dbHelper.getVendorWirhDebt();
    }

    @Override
    public Single<BillingOperations> insertBillingOperation(BillingOperations billingOperations) {
        return dbHelper.insertBillingOperation(billingOperations);
    }

    @Override
    public Observable<List<com.jim.multipos.data.db.model.inventory.BillingOperations>> getBillingOperations() {
        return dbHelper.getBillingOperations();
    }

    @Override
    public Single<BillingOperations> getBillingOperationsByConsignmentId(Long consignmentId) {
        return dbHelper.getBillingOperationsByConsignmentId(consignmentId);
    }

    @Override
    public Single<BillingOperations> getBillingOperationsById(Long firstPayId) {
        return dbHelper.getBillingOperationsById(firstPayId);
    }

    @Override
    public Single<List<BillingOperations>> getBillingOperationByRootId(Long rootId) {
        return dbHelper.getBillingOperationByRootId(rootId).map(billingOperations -> {
            Collections.sort(billingOperations, (billingOperation, t1) -> t1.getCreatedDate().compareTo(billingOperation.getCreatedDate()));
            return billingOperations;
        });
    }

    @Override
    public Single<List<BillingOperations>> getBillingOperationForVendor(Long vendorId) {
        return dbHelper.getBillingOperationForVendor(vendorId);
    }

    @Override
    public Single<List<BillingOperations>> getBillingOperationInteval(Long vendorId, Calendar fromDate, Calendar toDate) {
        return dbHelper.getBillingOperationInteval(vendorId,fromDate,toDate);
    }

    @Override
    public Single<Double> getVendorDebt(Long vendorId) {
        return dbHelper.getVendorDebt(vendorId);
    }

    @Override
    public Single<List<BillingOperations>> getBillingOperationsInInterval(Calendar fromDate, Calendar toDate) {
        return dbHelper.getBillingOperationsByInterval(fromDate, toDate);
    }

    @Override
    public Single<Double> getBillingOperationsAmountInInterval(Long accountId, Calendar fromDate, Calendar toDate) {
        return dbHelper.getBillingOperationsAmountInInterval(accountId, fromDate, toDate);
    }

    @Override
    public Single<Boolean> deleteInventoryState(InventoryState inventoryState) {
        return dbHelper.deleteInventoryState(inventoryState);
    }
    @Override
    public Single<Order> insertOrder(Order order) {
        return dbHelper.insertOrder(order);
    }

    @Override
    public Single<List<OrderProduct>> insertOrderProducts(List<OrderProduct> orderProducts) {
        return dbHelper.insertOrderProducts(orderProducts);
    }

    @Override
    public Single<List<Order>> getAllTillOrders() {
        return dbHelper.getAllTillOrders();
    }

    @Override
    public Single<LazyList<Order>> getAllTillLazyOrders() {
        return dbHelper.getAllTillLazzyOrders();
    }

    @Override
    public Single<Integer> removeAllOrders() {
        return dbHelper.removeAllOrders();
    }

    @Override
    public Single<Long> insertOrderChangeLog(OrderChangesLog orderChangesLog) {
        return dbHelper.insertOrderChangeLog(orderChangesLog);
    }

    @Override
    public Single<List<Order>> getAllHoldOrders() {
        return dbHelper.getAllHoldOrders();
    }

    @Override
    public Single<List<Order>> getOrdersByTillId(Long id) {
        return dbHelper.getOrdersByTillId(id);
    }

    @Override
    public Single<List<Order>> getAllTillClosedOrders() {
        return dbHelper.getAllTillClosedOrders();
    }

    @Override
    public Single<Long> getLastOrderId() {
        return dbHelper.getLastOrderId();
    }

    @Override
    public Single<Long> getLastArchiveOrderId() {
        return dbHelper.getLastArchiveOrderId();
    }

    @Override
    public Single<Order> getOrder(Long orderId) {
        return dbHelper.getOrder(orderId);
    }

    @Override
    public Single<Long> deleteOrderProductsOnHold(List<OrderProduct> orderProducts) {
        return dbHelper.deleteOrderProductsOnHold(orderProducts);

    }


    @Override
    public Single<List<PayedPartitions>> insertPayedPartitions(List<PayedPartitions> payedPartitions) {
        return dbHelper.insertPayedPartitions(payedPartitions);
    }

    @Override
    public Single<Long> deletePayedPartitions(List<PayedPartitions> payedPartitions) {
        return dbHelper.deletePayedPartitions(payedPartitions);
    }

    @Override
    public Single<TillOperation> insertTillOperation(TillOperation tillOperation) {
        return dbHelper.insertTillOperation(tillOperation);
    }

    @Override
    public Single<TillDetails> insertTillDetails(TillDetails tillDetails) {
        return dbHelper.insertTillDetails(tillDetails);
    }

    @Override
    public Single<Double> getTotalTillOperationsAmount(Long accountId, Long tillId, int type) {
        return dbHelper.getTotalTillOperationsAmount(accountId, tillId, type);
    }

    @Override
    public Single<Double> getTotalTillManagementOperationsAmount(Long accountId, Long tillId, int type) {
        return dbHelper.getTotalTillManagementOperationsAmount(accountId, tillId, type);
    }

    @Override
    public Single<Till> insertTill(Till till) {
        return dbHelper.insertTill(till);
    }

    @Override
    public Single<List<TillOperation>> getTillOperationsByAccountId(Long accountId, Long tillId) {
        return dbHelper.getTillOperationsByAccountId(accountId, tillId);
    }

    @Override
    public Single<Till> getOpenTill() {
        return dbHelper.getOpenTill();
    }

    @Override
    public Single<Boolean> hasOpenTill() {
        return dbHelper.isHaveOpenTill();
    }

    @Override
    public Single<Boolean> isNoTills() {
        return dbHelper.isNoTills();
    }

    @Override
    public Single<Till> getLastClosedTill() {
        return dbHelper.getLastClosedTill();
    }

    @Override
    public Single<TillManagementOperation> insertTillManagementOperation(TillManagementOperation tillCloseOperation) {
        return dbHelper.insertTillCloseOperation(tillCloseOperation);
    }

    @Override
    public Single<Till> getTillById(Long tillId) {
        return dbHelper.getTillById(tillId);
    }

    @Override
    public Single<List<TillManagementOperation>> insertTillCloseOperationList(List<TillManagementOperation> tillCloseOperations) {
        return dbHelper.insertTillCloseOperationList(tillCloseOperations);
    }

    @Override
    public Single<Long> getCurrentOpenTillId() {
        return dbHelper.getCurrentOpenTillId();
    }

    @Override
    public Single<List<TillManagementOperation>> getTillManagementOperationsByTillId(Long id) {
        return dbHelper.getTillManagementOperationsByTillId(id);
    }

    @Override
    public Single<TillDetails> getTillDetailsByAccountId(Long accountId, Long tillId) {
        return dbHelper.getTillDetailsByAccountId(accountId, tillId);
    }

    @Override
    public Single<List<Till>> getAllTills() {
        return dbHelper.getAllTills();
    }

    @Override
    public Single<List<TillDetails>> getTillDetailsByTillId(Long tillId) {
        return dbHelper.getTillDetailsByTillId(tillId);
    }

    @Override
    public Single<List<Till>> getAllClosedTills() {
        return dbHelper.getAllClosedTills();
    }
}

