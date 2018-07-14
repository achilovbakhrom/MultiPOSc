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

import android.database.Cursor;

import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.AccountDao;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.ContactDao;
import com.jim.multipos.data.db.model.DaoMaster;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.DiscountDao;
import com.jim.multipos.data.db.model.DiscountLog;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.PaymentTypeDao;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.ProductClassDao;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.ServiceFeeDao;
import com.jim.multipos.data.db.model.ServiceFeeLog;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentDao;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.consignment.ConsignmentProductDao;
import com.jim.multipos.data.db.model.consignment.Invoice;
import com.jim.multipos.data.db.model.consignment.InvoiceDao;
import com.jim.multipos.data.db.model.consignment.Outvoice;
import com.jim.multipos.data.db.model.consignment.OutvoiceDao;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.currency.CurrencyDao;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerDao;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.customer.CustomerGroupDao;
import com.jim.multipos.data.db.model.customer.CustomerPayment;
import com.jim.multipos.data.db.model.customer.CustomerPaymentDao;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.customer.DebtDao;
import com.jim.multipos.data.db.model.customer.JoinCustomerGroupsWithCustomers;
import com.jim.multipos.data.db.model.customer.JoinCustomerGroupsWithCustomersDao;
import com.jim.multipos.data.db.model.intosystem.OutcomeWithDetials;
import com.jim.multipos.data.db.model.intosystem.StockQueueItem;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.inventory.BillingOperationsDao;
import com.jim.multipos.data.db.model.inventory.DetialCount;
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.IncomeProductDao;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.inventory.OutcomeProductDao;
import com.jim.multipos.data.db.model.inventory.StockPerfomanceWithProduct;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.data.db.model.inventory.StockQueueDao;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderChangesLog;
import com.jim.multipos.data.db.model.order.OrderDao;
import com.jim.multipos.data.db.model.order.OrderProduct;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.CategoryDao;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.ProductDao;
import com.jim.multipos.data.db.model.products.Return;
import com.jim.multipos.data.db.model.products.ReturnDao;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorDao;
import com.jim.multipos.data.db.model.stock.Stock;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillDao;
import com.jim.multipos.data.db.model.till.TillDetails;
import com.jim.multipos.data.db.model.till.TillDetailsDao;
import com.jim.multipos.data.db.model.till.TillManagementOperation;
import com.jim.multipos.data.db.model.till.TillManagementOperationDao;
import com.jim.multipos.data.db.model.till.TillOperation;
import com.jim.multipos.data.db.model.till.TillOperationDao;
import com.jim.multipos.data.db.model.unit.SubUnitsList;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.data.db.model.unit.UnitDao;
import com.jim.multipos.ui.consignment_list.model.InvoiceListItem;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.ui.reports.stock_operations.model.OperationSummaryItem;
import com.jim.multipos.ui.reports.stock_state.module.InventoryItemReport;
import com.jim.multipos.ui.reports.vendor.model.InvoiceProduct;
import com.jim.multipos.ui.vendor_item_managment.model.VendorManagmentItem;
import com.jim.multipos.ui.vendor_products_view.model.ProductState;
import com.jim.multipos.utils.BundleConstants;

import org.greenrobot.greendao.query.LazyList;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;

import static com.jim.multipos.data.db.model.products.Category.WITHOUT_PARENT;


/**
 * Created by janisharali on 08/12/16.
 */

@Singleton
public class AppDbHelper implements DbHelper {
    private final DaoSession mDaoSession;
    private Comparator<StockPerfomanceWithProduct> comparatorFIFO;
    private Comparator<StockPerfomanceWithProduct> comparatorLIFO;
    private Comparator<StockPerfomanceWithProduct> comparatorFEFO;
    @Inject
    public AppDbHelper(DbOpenHelper dbOpenHelper) {
        mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
        //CHECK COMPARATORS
        comparatorFIFO = (stockQueue, t1) -> stockQueue.getStockId().compareTo(t1.getStockId());
        comparatorLIFO = (stockQueue, t1) -> t1.getStockId().compareTo(stockQueue.getStockId());
        comparatorFEFO = (stockQueue, t1) -> stockQueue.getExpiredDate().compareTo(t1.getExpiredDate());

    }



    @Override
    public Observable<Long> insertCustomerToCustomerGroup(Long customerId, Long customerGroupId) {
        return Observable.fromCallable(() -> {
            JoinCustomerGroupsWithCustomers entity = new JoinCustomerGroupsWithCustomers(customerId, customerGroupId);
            return mDaoSession.getJoinCustomerGroupsWithCustomersDao().insertOrReplace(entity);
        });
    }

    @Override
    public Observable<Boolean> deleteCustomerFromCustomerGroup(Long customerGroupId, Long customerId) {
        return Observable.fromCallable(() -> {
            mDaoSession.getJoinCustomerGroupsWithCustomersDao().queryBuilder().where(JoinCustomerGroupsWithCustomersDao.Properties.CustomerId.eq(customerId), JoinCustomerGroupsWithCustomersDao.Properties.CustomerGroupId.eq(customerGroupId)).buildDelete().executeDeleteWithoutDetachingEntities();

            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteJoinCustomerGroupWithCustomer(Long customerId) {
        return Observable.fromCallable(() -> {
            mDaoSession.getJoinCustomerGroupsWithCustomersDao().queryBuilder().where(JoinCustomerGroupsWithCustomersDao.Properties.CustomerId.eq(customerId)).buildDelete().executeDeleteWithoutDetachingEntities();
            return true;
        });
    }

    @Override
    public Observable<Long> insertContact(Contact contact) {
        return Observable.fromCallable(() -> mDaoSession.getContactDao().insertOrReplace(contact));
    }

    @Override
    public Observable<Boolean> insertContacts(List<Contact> contact) {
        return Observable.fromCallable(() ->
        {
            mDaoSession.getContactDao().insertOrReplaceInTx(contact);
            return true;
        });
    }

    @Override
    public Observable<List<Contact>> getAllContacts() {
        return Observable.fromCallable(() -> mDaoSession.getContactDao().loadAll());
    }

    @Override
    public Observable<Long> insertCategory(Category category) {
        return Observable.fromCallable(() -> {
            if (category.getId() == null) {
                List<Category> categories = mDaoSession.getCategoryDao().queryBuilder()
                        .where(CategoryDao.Properties.ParentId.eq(WITHOUT_PARENT),
                                CategoryDao.Properties.Deleted.eq(false))
                        .build().list();
            }
            Long result = mDaoSession.getCategoryDao().insertOrReplace(category);
            mDaoSession.getCategoryDao().detachAll();
            return result;
        });
    }

    @Override
    public Observable<Long> insertSubCategory(Category subcategory) {
        return Observable.fromCallable(() -> {
            return mDaoSession.getCategoryDao().insertOrReplace(subcategory);
        });
    }

    @Override
    public Observable<Boolean> insertCategories(List<Category> categories) {
        return Observable.fromCallable(() -> {
            mDaoSession.getCategoryDao().insertOrReplaceInTx(categories);
            return true;
        });
    }

    @Override
    public Observable<List<Category>> getAllCategories() {
        return Observable.fromCallable(() -> mDaoSession.getCategoryDao().queryBuilder()
                .where(CategoryDao.Properties.ParentId.eq(WITHOUT_PARENT), CategoryDao.Properties.Deleted.eq(false))
                .build().list());
    }

    @Override
    public Observable<Integer> getAllProductsCount(Category category) {
        return Observable.fromCallable(() -> {
            List<Category> subCategories = mDaoSession.getCategoryDao().queryBuilder()
                    .where(CategoryDao.Properties.ParentId.eq(category.getId()), CategoryDao.Properties.Deleted.eq(false))
                    .where(CategoryDao.Properties.Active.eq(true))
                    .build().list();
            int sum = 0;
            for (Category subcategory : subCategories) {
                sum += mDaoSession.getProductDao().queryBuilder()
                        .where(ProductDao.Properties.CategoryId.eq(subcategory.getId()), ProductDao.Properties.Deleted.eq(false))
                        .where(ProductDao.Properties.Active.eq(true))
                        .build().list().size();
            }
            return sum;
        });
    }

    @Override
    public Single<List<Category>> getAllActiveCategories() {
        return Single.create(e -> e.onSuccess(mDaoSession.getCategoryDao().queryBuilder()
                .where(
                        CategoryDao.Properties.Deleted.eq(false),
                        CategoryDao.Properties.Active.eq(true),
                        CategoryDao.Properties.ParentId.eq(WITHOUT_PARENT))
                .build()
                .list()));
    }

    @Override
    public Single<List<Category>> getAllActiveSubCategories(Category parent) {
        return Single.create(e -> e.onSuccess(mDaoSession.getCategoryDao().queryBuilder()
                .where(
                        CategoryDao.Properties.Deleted.eq(false),
                        CategoryDao.Properties.Active.eq(true),
                        CategoryDao.Properties.ParentId.eq(parent.getId()))
                .build()
                .list()));
    }

    @Override
    public Observable<List<Category>> getSubCategories(Category category) {
        return Observable.fromCallable(() -> mDaoSession.getCategoryDao().queryBuilder()
                .where(CategoryDao.Properties.ParentId.eq(category.getId()), CategoryDao.Properties.Deleted.eq(false))
                .build().list());
    }

    @Override
    public Observable<Long> insertOrReplaceCategory(Category category) {
        return Observable.fromCallable(() -> mDaoSession.getCategoryDao().insertOrReplace(category));
    }

    @Override
    public Observable<Long> insertProduct(Product product) {
        return Observable.fromCallable(() -> mDaoSession.getProductDao().insertOrReplace(product));
    }

    @Override
    public Observable<Boolean> insertProducts(List<Product> products) {
        return Observable.fromCallable(() -> {
            mDaoSession.getProductDao().insertOrReplaceInTx(products);
            return true;
        });
    }

    @Override
    public Observable<List<Product>> getAllProducts() {
        return Observable.fromCallable(() ->
                mDaoSession.getProductDao().queryBuilder()
                        .where(ProductDao.Properties.Deleted.eq(false),
                                ProductDao.Properties.Active.eq(true))
                        .list());
    }

    @Override
    public Single<List<Product>> getAllActiveProducts(Category parent) {
        return Single.create(e -> e.onSuccess(mDaoSession.getProductDao().queryBuilder()
                .where(
                        ProductDao.Properties.Deleted.eq(false),
                        ProductDao.Properties.Active.eq(true),
                        ProductDao.Properties.CategoryId.eq(parent.getId()))
                .build()
                .list()));
    }

    @Override
    public Observable<Long> insertOrReplaceProduct(Product product) {
        return Observable.fromCallable(() -> mDaoSession.getProductDao().insertOrReplace(product));
    }

    @Override
    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    @Override
    public Observable<Account> insertAccount(Account account) {
        return Observable.fromCallable(() -> {
            mDaoSession.getAccountDao().insertOrReplace(account);

            return account;
        });
    }

    @Override
    public Observable<Boolean> insertAccounts(List<Account> accounts) {
        return Observable.fromCallable(() -> {
            mDaoSession.getAccountDao().insertOrReplaceInTx(accounts);
            return true;
        });
    }

    @Override
    public Observable<List<Account>> getAllAccounts() {
        return Observable.fromCallable(() -> mDaoSession.getAccountDao().queryBuilder()
                .where(AccountDao.Properties.IsNotSystemAccount.eq(true))
                .build().list()
        );
    }


    @Override
    public Observable<Boolean> deleteAccount(Account account) {
        return Observable.fromCallable(() -> {
            mDaoSession
                    .queryBuilder(PaymentType.class)
                    .where(PaymentTypeDao.Properties.AccountId.eq(account.getId()))
                    .buildDelete()
                    .executeDeleteWithoutDetachingEntities();
            mDaoSession.getAccountDao().delete(account);
            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteAllAccounts() {
        return Observable.fromCallable(() -> {
            mDaoSession.getAccountDao().deleteAll();

            return true;
        });
    }

    @Override
    public Observable<Long> insertStock(Stock stock) {
        return Observable.fromCallable(() -> mDaoSession.getStockDao().insertOrReplace(stock));
    }

    @Override
    public Observable<Boolean> insertStocks(List<Stock> stocks) {
        return Observable.fromCallable(() -> {
            mDaoSession.getStockDao().insertOrReplaceInTx(stocks);

            return true;
        });
    }

    @Override
    public Observable<List<Stock>> getAllStocks() {
        return Observable.fromCallable(() -> {
            List<Stock> stocks = mDaoSession.getStockDao().loadAll();
            Collections.reverse(stocks);

            return stocks;
        });
    }

    @Override
    public Observable<Boolean> deleteStock(Stock stock) {
        return Observable.fromCallable(() -> {
            mDaoSession.getStockDao().delete(stock);

            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteAllStocks() {
        return Observable.fromCallable(() -> {
            mDaoSession.getStockDao().deleteAll();

            return true;
        });
    }

    @Override
    public Observable<Long> addCurrency(Currency currency) {
        return Observable.fromCallable(() -> {
            Long id = mDaoSession.getCurrencyDao().insertOrReplace(currency);
            mDaoSession.getCurrencyDao().detachAll();
            return id;
        });
    }

    @Override
    public Observable<Boolean> insertCurrencies(List<Currency> currencies) {
        return Observable.fromCallable(() -> {
            mDaoSession.getCurrencyDao().insertInTx(currencies);

            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteAllCurrencies() {
        return Observable.fromCallable(() -> {
            mDaoSession.getCurrencyDao().deleteAll();

            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteCurrency(Currency currency) {
        return Observable.fromCallable(() -> {
            mDaoSession.getCurrencyDao().delete(currency);

            return true;
        });
    }

    @Override
    public Observable<List<Currency>> getAllCurrencies() {
        return Observable.fromCallable(() -> mDaoSession.getCurrencyDao().loadAll());
    }


    @Override
    public List<Currency> getCurrencies() {
        return mDaoSession.getCurrencyDao().loadAll();
    }

    @Override
    public Observable<Long> insertUnitCategory(UnitCategory category) {
        return Observable.fromCallable(() -> mDaoSession.getUnitCategoryDao().insertOrReplace(category));
    }

    @Override
    public Observable<Boolean> insertUnitCategories(List<UnitCategory> categoryList) {
        return Observable.fromCallable(() -> {
            mDaoSession.getUnitCategoryDao().insertOrReplaceInTx(categoryList);

            return true;
        });
    }

    @Override
    public Observable<List<UnitCategory>> getAllUnitCategories() {
        return Observable.fromCallable(() -> mDaoSession.getUnitCategoryDao().loadAll());
    }

    @Override
    public Observable<Long> insertPaymentType(PaymentType paymentType) {
        return Observable.fromCallable(() -> mDaoSession.getPaymentTypeDao().insertOrReplace(paymentType));
    }

    @Override
    public Observable<Boolean> deletePaymentType(PaymentType paymentType) {
        return Observable.fromCallable(() -> {
            mDaoSession.getPaymentTypeDao().delete(paymentType);

            return true;
        });
    }

    @Override
    public Observable<Boolean> isPaymentTypeExists(String name) {
        return Observable.fromCallable(() -> !mDaoSession.getPaymentTypeDao()
                .queryBuilder()
                .where(PaymentTypeDao.Properties.Name.eq(name))
                .build()
                .list()
                .isEmpty());
    }

    @Override
    public List<PaymentType> getPaymentTypes() {
        return mDaoSession.getPaymentTypeDao().queryBuilder()
                .where(PaymentTypeDao.Properties.IsNotSystem.eq(true), PaymentTypeDao.Properties.IsActive.eq(true)).build().list();
    }

    @Override
    public Single<List<Discount>> getAllDiscounts() {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getDiscountDao().queryBuilder()
                    .where(
                            DiscountDao.Properties.Delete.eq(false))
                    .build()
                    .list());
        });
    }

    @Override
    public Single<Discount> insertDiscount(Discount discount) {
        return Single.create(singleSubscriber -> {
            try {
                mDaoSession.getDiscountDao().insertOrReplace(discount);
                singleSubscriber.onSuccess(discount);
            } catch (Exception o) {
                singleSubscriber.onError(o);
            }
        });
    }

    @Override
    public Observable<List<Discount>> getDiscountsWithAllItemTypes(String[] discountTypes) {
        return Observable.fromCallable(() -> mDaoSession.getDiscountDao().queryBuilder().whereOr(DiscountDao.Properties.UsedType.eq(discountTypes[0]), DiscountDao.Properties.UsedType.eq(discountTypes[2])).orderDesc(DiscountDao.Properties.CreatedDate).build().list());
    }

    @Override
    public Observable<Boolean> deleteAllPaymentTypes() {
        return Observable.fromCallable(() -> {
            mDaoSession.getPaymentTypeDao().deleteAll();

            return true;
        });
    }

    @Override
    public Observable<Boolean> insertPaymentTypes(List<PaymentType> paymentTypes) {
        return Observable.fromCallable(() -> {
            mDaoSession.getPaymentTypeDao().insertOrReplaceInTx(paymentTypes);

            return true;
        });
    }

    @Override
    public Observable<List<PaymentType>> getAllPaymentTypes() {
        return Observable.fromCallable(() -> mDaoSession.getPaymentTypeDao().queryBuilder()
                .where(PaymentTypeDao.Properties.IsNotSystem.eq(true))
                .build().list());
    }

    @Override
    public Observable<Long> insertUnit(Unit unit) {
        return Observable.fromCallable(() -> mDaoSession.getUnitDao().insertOrReplace(unit));
    }

    @Override
    public Observable<Unit> updateUnit(Unit unit) {
        return Observable.fromCallable(() -> {
            mDaoSession.getUnitDao().insertOrReplace(unit);

            return unit;
        });
    }

    @Override
    public Observable<Boolean> insertUnits(List<Unit> units) {
        return Observable.fromCallable(() -> {
            mDaoSession.getUnitDao().insertOrReplaceInTx(units);

            return true;
        });
    }

    @Override
    public Observable<List<Unit>> getUnits(Long rootId, String name) {
        return Observable.fromCallable(() -> mDaoSession.getUnitDao().queryBuilder().where(UnitDao.Properties.RootId.eq(rootId), UnitDao.Properties.Name.notEq(name)).list());
    }

    @Override
    public Observable<Boolean> deleteUnit(Unit unit) {
        return Observable.fromCallable(() -> {
            mDaoSession.getUnitDao().delete(unit);

            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteAllUnits() {
        return Observable.fromCallable(() -> {
            mDaoSession.getUnitDao().deleteAll();
            mDaoSession.getUnitCategoryDao().deleteAll();
            return true;
        });
    }

    @Override
    public Observable<List<Unit>> getAllStaticUnits() {
        return Observable.fromCallable(() -> {
            Query<Unit> unitQuery = mDaoSession.getUnitDao().queryBuilder().
                    where(UnitDao.Properties.IsStaticUnit.eq(true)).build();
            return unitQuery.list();
        });
    }

    @Override
    public Single<List<ProductClass>> getAllProductClass() {
        return Single.create(e -> {
            List<ProductClass> list = mDaoSession.getProductClassDao().queryBuilder()
                    .where(
                            ProductClassDao.Properties.Deleted.eq(false))
                    .build()
                    .list();
            Collections.sort(list, (o1, o2) -> o1.getActive().compareTo(o2.getActive()));
            e.onSuccess(list);
        });
    }

    @Override
    public Observable<ProductClass> getProductClass(Long id) {
        return Observable.fromCallable(() -> mDaoSession.getProductClassDao().queryBuilder().where(ProductClassDao.Properties.Id.eq(id)).build().unique());
    }

    @Override
    public Single<Long> insertProductClass(ProductClass productClass) {
        return Single.create(singleSubscriber -> {
            try {
                long result = mDaoSession.getProductClassDao().insertOrReplace(productClass);
                singleSubscriber.onSuccess(result);
            } catch (Exception o) {
                singleSubscriber.onError(o);
            }
        });
    }

    @Override
    public Observable<List<SubUnitsList>> getSubUnits() {
        return Observable.fromCallable(() -> mDaoSession.getSubUnitsListDao().loadAll());
    }

    @Override
    public Observable<Long> insertSubUnits(SubUnitsList subUnitsList) {
        return Observable.fromCallable(() -> mDaoSession.getSubUnitsListDao().insertOrReplace(subUnitsList));
    }

    @Override
    public Observable<Boolean> deleteSubUnits(SubUnitsList subUnitsList) {
        return Observable.fromCallable(() -> {
            mDaoSession.getSubUnitsListDao().delete(subUnitsList);
            return true;
        });
    }


    @Override
    public Observable<Boolean> isAccountNameExists(String name) {
        return Observable.fromCallable(() -> !mDaoSession.getAccountDao()
                .queryBuilder()
                .where(AccountDao.Properties.Name.eq(name))
                .build()
                .list()
                .isEmpty());
    }


    @Override
    public Observable<Category> getCategoryByName(String name) {
        return Observable.fromCallable(() -> mDaoSession.getCategoryDao().queryBuilder()
                .where(CategoryDao.Properties.Name.eq(name), CategoryDao.Properties.Deleted.eq(false), CategoryDao.Properties.ParentId.eq(WITHOUT_PARENT))
                .build().unique());
    }

    @Override
    public Observable<Boolean> isCategoryNameExists(String name) {
        return Observable.fromCallable(() -> !mDaoSession.getCategoryDao().queryBuilder()
                .where(
                        CategoryDao.Properties.Name.eq(name),
                        CategoryDao.Properties.Deleted.eq(false),
                        CategoryDao.Properties.ParentId.eq(WITHOUT_PARENT)
                )
                .build()
                .list()
                .isEmpty());
    }

    @Override
    public Observable<Category> getSubCategoryByName(String name, Long id) {
        return Observable.fromCallable(() -> mDaoSession.getCategoryDao().queryBuilder()
                .where(CategoryDao.Properties.Name.eq(name), CategoryDao.Properties.Deleted.eq(false), CategoryDao.Properties.ParentId.eq(id))
                .build().unique());
    }

    @Override
    public Observable<Boolean> isSubCategoryNameExists(String parentName, String name) {
        return Observable.fromCallable(() -> {
            List<Category> categories = mDaoSession.getCategoryDao().queryBuilder()
                    .where(CategoryDao.Properties.Name.eq(parentName), CategoryDao.Properties.ParentId.eq(Category.WITHOUT_PARENT),
                            CategoryDao.Properties.Deleted.eq(false))
                    .build()
                    .list();
            Long parentId = Category.WITHOUT_PARENT;
            if (!categories.isEmpty()) {
                parentId = categories.get(0).getId();
            }
            if (Objects.equals(parentId, Category.WITHOUT_PARENT)) {
                return false;
            }
            return !mDaoSession.getCategoryDao().queryBuilder()
                    .where(CategoryDao.Properties.ParentId.eq(parentId), CategoryDao.Properties.Name.eq(name), CategoryDao.Properties.Deleted.eq(false))
                    .list()
                    .isEmpty();
        });
    }

    @Override
    public Observable<Boolean> insertServiceFees(List<ServiceFee> serviceFees) {
        return Observable.fromCallable(() -> {
            mDaoSession.getServiceFeeDao().insertOrReplaceInTx(serviceFees);

            return true;
        });
    }

    @Override
    public Observable<ServiceFee> insertServiceFee(ServiceFee serviceFee) {
        return Observable.fromCallable(() -> {
            mDaoSession.getServiceFeeDao().insertOrReplace(serviceFee);
            return serviceFee;
        });
    }

    @Override
    public Observable<List<ServiceFee>> getAllServiceFees() {
        return Observable.fromCallable(() -> mDaoSession.getServiceFeeDao().queryBuilder().where(ServiceFeeDao.Properties.Deleted.eq(false)).orderDesc(ServiceFeeDao.Properties.CreatedDate).build().list());
    }

    @Override
    public Observable<Boolean> deleteAllServiceFees() {
        return Observable.fromCallable(() -> {
            mDaoSession.getServiceFeeDao().deleteAll();

            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteServiceFee(ServiceFee serviceFee) {
        return Observable.fromCallable(() -> {
            mDaoSession.getServiceFeeDao().delete(serviceFee);

            return true;
        });
    }

    @Override
    public Observable<List<ServiceFee>> getServiceFeesWithAllItemTypes() {
        return Observable.fromCallable(() -> mDaoSession.getServiceFeeDao().queryBuilder().whereOr(ServiceFeeDao.Properties.ApplyingType.eq(ServiceFee.ITEM), ServiceFeeDao.Properties.ApplyingType.eq(ServiceFee.ALL)).orderDesc(ServiceFeeDao.Properties.CreatedDate).build().list());
    }

    @Override
    public Observable<Boolean> isCustomerExists(String name) {
        return Observable.fromCallable(() -> !mDaoSession.getCustomerDao().queryBuilder()
                .where(CustomerDao.Properties.Name.eq(name), CustomerDao.Properties.IsDeleted.eq(false))
                .build()
                .list()
                .isEmpty());
    }

    @Override
    public Observable<Long> insertCustomer(Customer customer) {
        return Observable.fromCallable(() -> mDaoSession.getCustomerDao().insertOrReplace(customer));
    }

    @Override
    public Observable<Boolean> insertCustomers(List<Customer> customers) {
        return Observable.fromCallable(() -> {
            mDaoSession.getCustomerDao().insertOrReplaceInTx(customers);

            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteCustomer(Customer customer) {
        return Observable.fromCallable(() -> {
            mDaoSession.getCustomerDao().delete(customer);

            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteAllCustomers() {
        return Observable.fromCallable(() -> {
            mDaoSession.getCustomerDao().deleteAll();

            return true;
        });
    }

    @Override
    public Observable<CustomerGroup> getCustomerGroupByName(String name) {
        return Observable.fromCallable(() -> mDaoSession.getCustomerGroupDao().queryBuilder().where(CustomerGroupDao.Properties.Name.eq(name)).build().unique());
    }

    @Override
    public Observable<CustomerGroup> getCustomerGroupById(long id) {
        return Observable.fromCallable(() -> mDaoSession.getCustomerGroupDao().queryBuilder().where(CustomerGroupDao.Properties.Id.eq(id)).build().unique());
    }

    @Override
    public Observable<List<Customer>> getAllCustomers() {
        return Observable.fromCallable(() -> mDaoSession.getCustomerDao().queryBuilder()
                .where(
                        CustomerDao.Properties.IsDeleted.eq(false),
                        CustomerDao.Properties.IsActive.eq(true))
                .orderDesc(CustomerDao.Properties.ClientId)
                .build()
                .list());
    }

    @Override
    public Observable<Long> insertCustomerGroup(CustomerGroup customerGroup) {
        return Observable.fromCallable(() -> mDaoSession.getCustomerGroupDao().insertOrReplace(customerGroup));
    }

    @Override
    public Observable<Boolean> insertCustomerGroups(List<CustomerGroup> customerGroups) {
        return Observable.fromCallable(() -> {
            mDaoSession.getCustomerGroupDao().insertOrReplaceInTx(customerGroups);

            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteCustomerGroup(CustomerGroup customerGroup) {
        return Observable.fromCallable(() -> {
            mDaoSession.getCustomerGroupDao().delete(customerGroup);

            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteAllCustomerGroups() {
        return Observable.fromCallable(() -> {
            mDaoSession.getCustomerGroupDao().deleteAll();

            return true;
        });
    }

    @Override
    public Observable<List<CustomerGroup>> getAllCustomerGroups() {
        return Observable.fromCallable(() -> mDaoSession.getCustomerGroupDao().queryBuilder().orderDesc(CustomerGroupDao.Properties.CreatedDate).build().list());
    }

    @Override
    public Observable<Boolean> isCustomerGroupExists(String name) {
        return Observable.fromCallable(() -> !mDaoSession.getCustomerGroupDao().queryBuilder()
                .where(CustomerGroupDao.Properties.Name.eq(name))
                .build()
                .list()
                .isEmpty());
    }

    @Override
    public List<Account> getAccounts() {
        return mDaoSession.getAccountDao().queryBuilder()
                .where(AccountDao.Properties.IsNotSystemAccount.eq(true), AccountDao.Properties.IsActive.eq(true))
                .build().list();
    }

    @Override
    public Observable<List<CustomerGroup>> getCustomerGroups(Customer customer) {
        return Observable.fromCallable(() -> {
            String query = "SELECT * FROM CUSTOMER_GROUP JOIN (SELECT * FROM JOIN_CUSTOMER_GROUPS_WITH_CUSTOMERS WHERE CUSTOMER_ID = ?) AS TEMP ON (CUSTOMER_GROUP._ID = TEMP.CUSTOMER_GROUP_ID)";

            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, new String[]{String.valueOf(customer.getId())});

            List<CustomerGroup> customerGroups = new ArrayList<>();

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                while (!cursor.isAfterLast()) {
                    CustomerGroup customerGroup = new CustomerGroup();
                    customerGroup.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex("CUSTOMER_GROUP_ID"))));
                    customerGroup.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                    //customerGroup.setServiceFeeId(Long.parseLong(cursor.getString(cursor.getColumnIndex("SERVICE_FEE_ID"))));
                    //customerGroup.setDiscountId(Long.parseLong(cursor.getString(cursor.getColumnIndex("DISCOUNT_ID"))));

                    int iActive = cursor.getInt(cursor.getColumnIndex("IS_ACTIVE"));
                    boolean isActive = iActive != 0;
                    customerGroup.setIsActive(isActive);

                    customerGroups.add(customerGroup);

                    cursor.moveToNext();
                }
            }
            return customerGroups;
        });
    }

    //Vendor operations
    @Override
    public Observable<Long> addVendor(Vendor vendor) {
        return Observable.fromCallable(() -> mDaoSession.getVendorDao().insertOrReplace(vendor));
    }

    @Override
    public Observable<Boolean> addVendors(List<Vendor> vendors) {
        return Observable.fromCallable(() -> {
            mDaoSession.getVendorDao().insertOrReplaceInTx(vendors);
            return true;
        });
    }

    @Override
    public Observable<Boolean> isVendorNameExist(String name) {
        return Observable.fromCallable(() -> !mDaoSession.getVendorDao().queryBuilder().where(VendorDao.Properties.Name.eq(name), VendorDao.Properties.Deleted.eq(false)).list().isEmpty());
    }

    @Override
    public Observable<Boolean> updateContacts(Long vendorId, List<Contact> contacts) {
        return Observable.fromCallable(() -> {
            mDaoSession.getContactDao().queryBuilder().where(ContactDao.Properties.VendorId.eq(vendorId)).buildDelete().executeDeleteWithoutDetachingEntities();
            mDaoSession.getContactDao().insertOrReplaceInTx(contacts);
            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteVendor(Long vendorId) {
        return Observable.fromCallable(() -> {
            Vendor vendor = mDaoSession.getVendorDao().load(vendorId);
            if (vendor == null || vendor.getActive()) return false;
            mDaoSession.getVendorDao().delete(vendor);
            return true;
        });
    }

    @Override
    public Observable<Vendor> getVendorById(Long vendorId) {
        return Observable.fromCallable(() -> mDaoSession.getVendorDao().load(vendorId));
    }

    @Override
    public Observable<List<Vendor>> getVendors() {
        return Observable.fromCallable(() ->
                mDaoSession.getVendorDao().queryBuilder()
                        .where(VendorDao.Properties.Deleted.eq(false))
                        .list());
    }

    @Override
    public Observable<Boolean> removeAllContacts(Long vendorId) {
        return Observable.fromCallable(() -> {
            mDaoSession.getContactDao().queryBuilder().where(ContactDao.Properties.VendorId.eq(vendorId)).buildDelete().executeDeleteWithoutDetachingEntities();
            return true;
        });
    }

    @Override
    public Single<List<Product>> getSearchProducts(String searchText, boolean skuMode, boolean barcodeMode, boolean nameMode) {

        return Single.create(e -> {

            QueryBuilder<Product> queryBuilderCred = mDaoSession.getProductDao().queryBuilder();
            queryBuilderCred.whereOr(
                    ProductDao.Properties.Name.like("%" + searchText.toLowerCase() + "%"),
                    ProductDao.Properties.Name.like("%" + searchText.toUpperCase() + "%"),
                    ProductDao.Properties.Barcode.like("%" + searchText.toUpperCase() + "%"),
                    ProductDao.Properties.Barcode.like("%" + searchText.toUpperCase() + "%"),
                    ProductDao.Properties.Sku.like("%" + searchText.toUpperCase() + "%"),
                    ProductDao.Properties.Sku.like("%" + searchText.toUpperCase() + "%")).where(ProductDao.Properties.Deleted.eq(false));
            List<Product> list = queryBuilderCred.build().list();
            for (int i = list.size() - 1; i >= 0; i--) {
                if (list.get(i).getDeleted()) list.remove(i);
                boolean keepMe = false;
                if (skuMode && list.get(i).getSku().toUpperCase().contains(searchText.toUpperCase())) {
                    keepMe = true;
                }
                if (barcodeMode && list.get(i).getBarcode().toUpperCase().contains(searchText.toUpperCase())) {
                    keepMe = true;
                }
                if (nameMode && list.get(i).getName().toUpperCase().contains(searchText.toUpperCase())) {
                    keepMe = true;
                }
                if (!keepMe) {
                    list.remove(i);
                }
            }

            e.onSuccess(list);
        });
    }

    //Category
    @Override
    public Observable<Category> getCategoryById(Long id) {
        return Observable.fromCallable(() -> mDaoSession.getCategoryDao().load(id));
    }

    @Override
    public Observable<Boolean> removeCategory(Category category) {
        return Observable.fromCallable(() -> {
//            if (category.getParentId().equals(Category.WITHOUT_PARENT)) {
//
//            }

            mDaoSession.getCategoryDao().delete(category);
            return true;
        });
    }

    @Override
    public Observable<Boolean> isProductNameExists(String productName, Long categoryId) {
        return Observable.fromCallable(() -> !mDaoSession
                .queryBuilder(Product.class)
                .where(ProductDao.Properties.CategoryId.eq(categoryId), ProductDao.Properties.Name.eq(productName), ProductDao.Properties.Deleted.eq(false))
                .list()
                .isEmpty());
    }

    @Override
    public Observable<Boolean> removeProduct(Product product) {
        return Observable.fromCallable(() -> {
            mDaoSession.getProductDao().delete(product);
            return true;
        });

    }

    @Override
    public Single<List<InventoryItem>> getInventoryItems() {
        //TODO
        return null;
    }


    @Override
    public Observable<List<Category>> getActiveCategories() {
        return Observable.fromCallable(() -> mDaoSession.queryBuilder(Category.class)
                .where(CategoryDao.Properties.ParentId.eq(WITHOUT_PARENT),
                        CategoryDao.Properties.Active.eq(true),
                        CategoryDao.Properties.Deleted.eq(false))
                .build()
                .list());
    }

    @Override
    public Single<Consignment> insertConsignment(Consignment consignment) {
        return Single.create(singleSubscriber -> {
            try {
                mDaoSession.getConsignmentDao().insertOrReplace(consignment);
                singleSubscriber.onSuccess(consignment);
            } catch (Exception o) {
                singleSubscriber.onError(o);
            }
        });
    }

    @Override
    public Observable<Long> insertConsignmentProduct(ConsignmentProduct consignmentProduct) {
        return Observable.fromCallable(() -> mDaoSession.getConsignmentProductDao().insertOrReplace(consignmentProduct));
    }

    @Override
    public Observable<List<Consignment>> getConsignments() {
        return Observable.fromCallable(() -> mDaoSession.getConsignmentDao().queryBuilder().where(ConsignmentDao.Properties.IsDeleted.eq(false)).build().list());
    }

    @Override
    public Single<List<Consignment>> getConsignmentsByVendorId(Long vendorId) {
        return Single.create(e -> {
            List<Consignment> consignmentList = mDaoSession.queryBuilder(Consignment.class)
                    .where(ConsignmentDao.Properties.VendorId.eq(vendorId),
                            ConsignmentDao.Properties.IsDeleted.eq(false))
                    .build()
                    .list();
            e.onSuccess(consignmentList);
        });
    }

    @Override
    public Single<List<ConsignmentProduct>> getConsignmentProductsByConsignmentId(Long consignmentId) {
        return Single.create(e -> {
            List<ConsignmentProduct> productList = mDaoSession.queryBuilder(ConsignmentProduct.class)
                    .where(ConsignmentProductDao.Properties.ConsignmentId.eq(consignmentId))
                    .build()
                    .list();
            e.onSuccess(productList);
        });
    }

    @Override
    public Observable<Boolean> insertConsignmentProduct(List<ConsignmentProduct> consignmentProducts) {
        return Observable.fromCallable(() ->
        {
            mDaoSession.getConsignmentProductDao().insertOrReplaceInTx(consignmentProducts);
            return true;
        });
    }

    @Override
    public Single<Consignment> getConsignmentById(Long consignmentId) {
        return Single.create(e -> {
            Consignment consignment = mDaoSession.queryBuilder(Consignment.class)
                    .where(ConsignmentDao.Properties.Id.eq(consignmentId))
                    .build()
                    .list().get(0);
            e.onSuccess(consignment);
        });
    }

    @Override
    public Single<List<VendorManagmentItem>> getVendorItemManagmentItem() {
        return Single.create(e -> {
            List<Vendor> vendors = mDaoSession.getVendorDao().queryBuilder().where(VendorDao.Properties.Deleted.eq(false)).build().list();
            List<VendorManagmentItem> vendorManagementItems = new ArrayList<>();
            for (Vendor vendor : vendors) {
                VendorManagmentItem item = new VendorManagmentItem();
                item.setVendor(vendor);
                String stockQuery = "SELECT * FROM STOCK_QUEUE WHERE VENDOR_ID == " + vendor.getId() + " GROUP BY PRODUCT_ID";
                Cursor stockCursor = mDaoSession.getDatabase().rawQuery(stockQuery, null);
                List<Product> products = new ArrayList<>();
                if (stockCursor.getCount() > 0) {
                    stockCursor.moveToFirst();
                    while (!stockCursor.isAfterLast()){
                    products.add(mDaoSession.getProductDao().load(stockCursor.getLong(stockCursor.getColumnIndex("PRODUCT_ID"))));
                    stockCursor.moveToNext();
                    }
                }
                item.setProducts(products);
                String query = "SELECT  SUM(AMOUNT) AS AMOUNT FROM BILLING_OPERATION WHERE IS_DELETED == " + 0 + " GROUP BY VENDOR_ID HAVING VENDOR_ID=?";
                Cursor cursor = mDaoSession.getDatabase().rawQuery(query, new String[]{String.valueOf(vendor.getId())});
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    item.setDebt(cursor.getDouble(cursor.getColumnIndex("AMOUNT")));
                } else {
                    item.setDebt(0);
                }
                vendorManagementItems.add(item);
            }
            e.onSuccess(vendorManagementItems);
        });
    }

    @Override
    public Single<List<StockQueue>> getStockQueueByVendorId(Long id) {
        return Single.create(e -> e.onSuccess(mDaoSession.getStockQueueDao().queryBuilder().where(StockQueueDao.Properties.VendorId.eq(id)).build().list()));
    }



    @Override
    public Single<Double> getVendorDebt(Long vendorId) {
        return Single.create(e -> {
            double debt = 0;
            String query = "SELECT  SUM(AMOUNT) AS AMOUNT FROM BILLING_OPERATION WHERE  IS_DELETED == " + 0 + " GROUP BY VENDOR_ID HAVING VENDOR_ID=?";
            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, new String[]{String.valueOf(vendorId)});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                debt = cursor.getDouble(cursor.getColumnIndex("AMOUNT"));
            } else {
                debt = 0;
            }
            e.onSuccess(debt);
        });
    }

    @Override
    public Single<BillingOperations> insertBillingOperation(BillingOperations billingOperations) {
        return Single.create(singleSubscriber -> {
            try {
                mDaoSession.getBillingOperationsDao().insertOrReplace(billingOperations);
                singleSubscriber.onSuccess(billingOperations);
            } catch (Exception o) {
                singleSubscriber.onError(o);
            }
        });
    }

    @Override
    public Observable<List<BillingOperations>> getBillingOperations() {
        return Observable.fromCallable(() -> mDaoSession.queryBuilder(BillingOperations.class)
                .where(BillingOperationsDao.Properties.IsDeleted.eq(false))
                .build()
                .list());
    }

    @Override
    public Single<BillingOperations> getBillingOperationsByConsignmentId(Long consignmentId) {
        return Single.create(e -> {
            //TODO
//            List<BillingOperations> billingOperations = mDaoSession.queryBuilder(BillingOperations.class)
//                    .where(BillingOperationsDao.Properties.ConsignmentId.eq(consignmentId),
//                            BillingOperationsDao.Properties.IsDeleted.eq(false))
//                    .build()
//                    .list();
//            e.onSuccess(billingOperations.get(0));
        });
    }

    @Override
    public Single<BillingOperations> getBillingOperationsById(Long firstPayId) {
        return Single.create(e -> {
            List<BillingOperations> billingOperations = mDaoSession.queryBuilder(BillingOperations.class)
                    .where(BillingOperationsDao.Properties.Id.eq(firstPayId),
                            BillingOperationsDao.Properties.IsDeleted.eq(false))
                    .build()
                    .list();
            e.onSuccess(billingOperations.get(0));
        });
    }

    @Override
    public Single<List<BillingOperations>> getBillingOperationByRootId(Long rootId) {
        return Single.create(e -> {
            List<BillingOperations> billingOperations = mDaoSession.queryBuilder(BillingOperations.class)
                    .build()
                    .list();
            e.onSuccess(billingOperations);
        });
    }



    @Override
    public Single<List<BillingOperations>> getBillingOperationInteval(Long vendorId, Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
//            fromDate.set(Calendar.HOUR_OF_DAY, 0);
//            fromDate.set(Calendar.MINUTE, 0);
//            fromDate.set(Calendar.SECOND, 0);
//            fromDate.set(Calendar.MILLISECOND, 0);
//
//            toDate.set(Calendar.HOUR_OF_DAY, 23);
//            toDate.set(Calendar.MINUTE, 59);
//            toDate.set(Calendar.SECOND, 59);
//            toDate.set(Calendar.MILLISECOND, 9999);

            List<BillingOperations> billingOperations = mDaoSession.getBillingOperationsDao().queryBuilder()
                    .where(BillingOperationsDao.Properties.PaymentDate.ge(fromDate.getTimeInMillis()),
                            BillingOperationsDao.Properties.PaymentDate.le(toDate.getTimeInMillis()),
                            BillingOperationsDao.Properties.VendorId.eq(vendorId))
                    .build().list();

            e.onSuccess(billingOperations);
        });
    }

    @Override
    public Single<List<BillingOperations>> getBillingOperationsByInterval(Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
//            fromDate.set(Calendar.HOUR_OF_DAY, 0);
//            fromDate.set(Calendar.MINUTE, 0);
//            fromDate.set(Calendar.SECOND, 0);
//            fromDate.set(Calendar.MILLISECOND, 0);
//
//            toDate.set(Calendar.HOUR_OF_DAY, 23);
//            toDate.set(Calendar.MINUTE, 59);
//            toDate.set(Calendar.SECOND, 59);
//            toDate.set(Calendar.MILLISECOND, 9999);

            List<BillingOperations> billingOperations = mDaoSession.getBillingOperationsDao().queryBuilder()
                    .where(BillingOperationsDao.Properties.PaymentDate.ge(fromDate.getTimeInMillis()),
                            BillingOperationsDao.Properties.PaymentDate.le(toDate.getTimeInMillis()),
                            BillingOperationsDao.Properties.OperationType.eq(BillingOperations.PAID_TO_CONSIGNMENT))
                    .build().list();

            e.onSuccess(billingOperations);
        });
    }



    @Override
    public Single<Product> getProductById(Long productId) {
        return Single.create(e-> e.onSuccess(mDaoSession.getProductDao().load(productId)) );
    }

    @Override
    public Observable<Boolean> removeProductFromInventoryState(Long productId) {
        return Observable.fromCallable(() -> {
//            mDaoSession.queryBuilder(InventoryStateDao.class)
//                    .where(InventoryStateDao.Properties.ProductId.eq(productId))
//                    .buildDelete().executeDeleteWithoutDetachingEntities();
            return true;
        });
    }


    @Override
    public Currency getMainCurrency() {
        return mDaoSession.queryBuilder(Currency.class)
                .where(CurrencyDao.Properties.IsMain.eq(true))
                .build().unique();
//        if(currencies.get(0) == null){
//            Currency currency = new Currency();
//            currency.setAbbr("uzs");
//            currency.setIsMain(true);
//            currency.setActive(true);
//            currency.setName("Uzb");
//            return currency;
//        }
//            return currencies.get(0);
    }

    @Override
    public Single<List<BillingOperations>> getBillingOperationForVendor(Long vendorId) {
        return Single.create(e -> {
            List<BillingOperations> billingOperations = mDaoSession.queryBuilder(BillingOperations.class)
                    .where(BillingOperationsDao.Properties.VendorId.eq(vendorId),
                            BillingOperationsDao.Properties.IsDeleted.eq(false))
                    .build().list();
            e.onSuccess(billingOperations);
        });
    }


    @Override
    public Single<List<Consignment>> getConsignmentsInInterval(Long vendorId, Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
//            fromDate.set(Calendar.HOUR_OF_DAY, 0);
//            fromDate.set(Calendar.MINUTE, 0);
//            fromDate.set(Calendar.SECOND, 0);
//            fromDate.set(Calendar.MILLISECOND, 0);
//
//            toDate.set(Calendar.HOUR_OF_DAY, 23);
//            toDate.set(Calendar.MINUTE, 59);
//            toDate.set(Calendar.SECOND, 59);
//            toDate.set(Calendar.MILLISECOND, 9999);

            List<Consignment> consignments = mDaoSession.getConsignmentDao().queryBuilder()
                    .where(ConsignmentDao.Properties.CreatedDate.ge(fromDate.getTimeInMillis()),
                            ConsignmentDao.Properties.CreatedDate.le(toDate.getTimeInMillis()),
                            ConsignmentDao.Properties.VendorId.eq(vendorId))
                    .build().list();

            e.onSuccess(consignments);
        });
    }

    @Override
    public Single<Customer> getCustomerById(Long customerId) {
        return Single.create(e -> {
            List<Customer> customerList = mDaoSession
                    .queryBuilder(Customer.class)
                    .where(CustomerDao.Properties.Id.eq(customerId))
                    .build().list();
            e.onSuccess(customerList.get(0));
        });
    }

    @Override
    public Single<List<Customer>> getCustomersWithDebt() {
        return Single.create(e -> {
            List<Customer> customerList = mDaoSession
                    .queryBuilder(Customer.class)
                    .where(
                            CustomerDao.Properties.IsDeleted.eq(false),
                            CustomerDao.Properties.IsActive.eq(true))
                    .build().list();
            List<Customer> newCustomerList = new ArrayList<>();
            for (int i = 0; i < customerList.size(); i++) {
                if (customerList.get(i).getActiveDebts().size() > 0)
                    newCustomerList.add(customerList.get(i));
            }
            e.onSuccess(newCustomerList);
        });
    }

    @Override
    public Single<Boolean> insertDebt(Debt debt) {
        return Single.create(singleSubscriber -> {
            mDaoSession.getDebtDao().insertOrReplace(debt);
            singleSubscriber.onSuccess(true);
        });
    }

    @Override
    public Single<List<Debt>> getDebtsByCustomerId(Long id) {
        return Single.create(e -> {
            List<Debt> debts = mDaoSession
                    .queryBuilder(Debt.class)
                    .where(DebtDao.Properties.CustomerId.eq(id),
                            DebtDao.Properties.Status.eq(Debt.ACTIVE),
                            DebtDao.Properties.IsDeleted.eq(false))
                    .build().list();
            e.onSuccess(debts);
        });
    }

    @Override
    public Single<List<Debt>> getAllActiveDebts() {
        return Single.create(e -> {
            List<Debt> debts = mDaoSession
                    .queryBuilder(Debt.class)
                    .where(DebtDao.Properties.Status.eq(Debt.ACTIVE))
                    .build().list();
            e.onSuccess(debts);
        });
    }

    @Override
    public Single<Order> insertOrder(Order order) {
        return Single.create(singleSubscriber -> {
            mDaoSession.getOrderDao().insertOrReplace(order);
            singleSubscriber.onSuccess(order);
        });
    }

    @Override
    public Single<CustomerPayment> insertCustomerPayment(CustomerPayment payment) {
        return Single.create(singleSubscriber -> {
            mDaoSession.getCustomerPaymentDao().insertOrReplace(payment);
            singleSubscriber.onSuccess(payment);
        });
    }

    @Override
    public Single<List<CustomerPayment>> getCustomerPaymentsByInterval(Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
//            fromDate.set(Calendar.HOUR_OF_DAY, 0);
//            fromDate.set(Calendar.MINUTE, 0);
//            fromDate.set(Calendar.SECOND, 0);
//            fromDate.set(Calendar.MILLISECOND, 0);
//
//            toDate.set(Calendar.HOUR_OF_DAY, 23);
//            toDate.set(Calendar.MINUTE, 59);
//            toDate.set(Calendar.SECOND, 59);
//            toDate.set(Calendar.MILLISECOND, 9999);

            List<CustomerPayment> customerPayments = mDaoSession.getCustomerPaymentDao().queryBuilder()
                    .where(CustomerPaymentDao.Properties.PaymentDate.ge(fromDate.getTimeInMillis()),
                            CustomerPaymentDao.Properties.PaymentDate.le(toDate.getTimeInMillis()))
                    .build().list();
            e.onSuccess(customerPayments);
        });
    }

    @Override
    public Single<Boolean> insertReturns(List<Return> returnsList) {
        return Single.create(singleSubscriber -> {
            mDaoSession.getReturnDao().insertOrReplaceInTx(returnsList);
            singleSubscriber.onSuccess(true);
        });
    }


    @Override
    public Single<PaymentType> getDebtPaymentType() {
        return Single.create(e -> {
            List<PaymentType> paymentTypes = mDaoSession.getPaymentTypeDao().queryBuilder()
                    .where(PaymentTypeDao.Properties.IsNotSystem.eq(false))
                    .build().list();
            e.onSuccess(paymentTypes.get(0));
        });
    }

    @Override
    public Single<List<PayedPartitions>> insertPayedPartitions(List<PayedPartitions> payedPartitions) {
        return Single.create(e -> {
            mDaoSession.getPayedPartitionsDao().insertOrReplaceInTx(payedPartitions);
            e.onSuccess(payedPartitions);
        });
    }

    @Override
    public Single<List<OrderProduct>> insertOrderProducts(List<OrderProduct> orderProducts) {
        return Single.create(e -> {
            mDaoSession.getOrderProductDao().insertOrReplaceInTx(orderProducts);
            e.onSuccess(orderProducts);
        });
    }

    @Override
    public Single<List<Order>> getAllTillOrders() {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getOrderDao().queryBuilder()
                    .where(OrderDao.Properties.IsArchive.eq(false))
                    .build().list());
        });
    }

    @Override
    public Single<LazyList<Order>> getAllTillLazzyOrders() {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getOrderDao().queryBuilder().where(OrderDao.Properties.IsArchive.eq(false)).listLazy());
        });
    }


    @Override
    public Single<TillOperation> insertTillOperation(TillOperation tillOperation) {
        return Single.create(singleSubscriber -> {
            mDaoSession.getTillOperationDao().insertOrReplace(tillOperation);
            singleSubscriber.onSuccess(tillOperation);
        });
    }

    @Override
    public Single<TillDetails> insertTillDetails(TillDetails tillDetails) {
        return Single.create(singleSubscriber -> {
            mDaoSession.getTillDetailsDao().insertOrReplace(tillDetails);
            singleSubscriber.onSuccess(tillDetails);
        });
    }

    @Override
    public Single<Till> insertTill(Till till) {
        return Single.create(singleSubscriber -> {
            mDaoSession.getTillDao().insertOrReplace(till);
            singleSubscriber.onSuccess(till);
        });
    }

    @Override
    public Single<List<TillOperation>> getTillOperationsByAccountId(Long id, Long tillId) {
        return Single.create(e -> {
            List<TillOperation> tillOperationList = new ArrayList<>();
            String query = "SELECT OnItemClickListener.* FROM TILL_OPERATIONS OnItemClickListener INNER JOIN PAYMENT_TYPE b ON OnItemClickListener.PAYMENT_TYPE_ID = b._id WHERE b.ACCOUNT_ID = " + id + " AND OnItemClickListener.TILL_ID = " + tillId;
            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                TillOperation tillOperation = new TillOperation();
                tillOperation.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                tillOperation.setPaymentType(mDaoSession.getPaymentTypeDao().load(cursor.getLong(cursor.getColumnIndex("PAYMENT_TYPE_ID"))));
                tillOperation.setTill(mDaoSession.getTillDao().load(cursor.getLong(cursor.getColumnIndex("TILL_ID"))));
                tillOperation.setAmount(cursor.getDouble(cursor.getColumnIndex("AMOUNT")));
                tillOperation.setType(cursor.getInt(cursor.getColumnIndex("TYPE")));
                tillOperation.setPaymentTypeId(mDaoSession.getPaymentTypeDao().load(cursor.getLong(cursor.getColumnIndex("PAYMENT_TYPE_ID"))).getId());
                tillOperation.setTillId(mDaoSession.getTillDao().load(cursor.getLong(cursor.getColumnIndex("TILL_ID"))).getId());
                tillOperationList.add(tillOperation);
                cursor.moveToNext();
            }
            e.onSuccess(tillOperationList);
        });
    }

    @Override
    public Single<Till> getOpenTill() {
        return Single.create(e -> {
            Till till;
            String query = "SELECT * FROM TILL WHERE STATUS = " + Till.OPEN;
            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, null);
            cursor.moveToFirst();
            till = new Till();
            till.setId(cursor.getLong(cursor.getColumnIndex("_id")));
            till.setStatus(Till.OPEN);
            till.setOpenDate(cursor.getLong(cursor.getColumnIndex("OPEN_DATE")));
            till.setCloseDate(cursor.getLong(cursor.getColumnIndex("CLOSE_DATE")));
            till.setDebtSales(cursor.getDouble(cursor.getColumnIndex("DEBT_SALES")));
            e.onSuccess(till);
        });
    }

    @Override
    public Single<Long> getCurrentOpenTillId() {
        return Single.create(e -> {
            Till till;
            String query = "SELECT * FROM TILL WHERE STATUS = " + Till.OPEN;
            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, null);
            cursor.moveToFirst();
            till = new Till();
            till.setId(cursor.getLong(cursor.getColumnIndex("_id")));
            till.setStatus(Till.OPEN);
            till.setOpenDate(cursor.getLong(cursor.getColumnIndex("OPEN_DATE")));
            till.setCloseDate(cursor.getLong(cursor.getColumnIndex("CLOSE_DATE")));
            till.setDebtSales(cursor.getDouble(cursor.getColumnIndex("DEBT_SALES")));
            e.onSuccess(till.getId());
        });
    }

    @Override
    public Single<Boolean> isHaveOpenTill() {
        return Single.create(e -> {
            String query = "SELECT * FROM TILL WHERE STATUS = " + Till.OPEN;
            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, null);
            if (cursor.getCount() == 1) {
                e.onSuccess(true);
            } else if (cursor.getCount() == 0) {
                e.onSuccess(false);
            } else {
                e.onError(new Throwable("MORE THAN ONE TILLS WERE OPENED"));
            }
        });
    }

    @Override
    public Single<Boolean> isNoTills() {
        return Single.create(e -> {
            String query = "SELECT * FROM TILL WHERE STATUS = " + Till.CLOSED;
            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, null);
            e.onSuccess(cursor.getCount() > 0);
        });
    }

    @Override
    public Single<Till> getLastClosedTill() {
        return Single.create(e -> {
            Till till;
            String newQuery = "SELECT * FROM TILL WHERE STATUS = " + Till.CLOSED;
            Cursor newCursor = mDaoSession.getDatabase().rawQuery(newQuery, null);
            newCursor.moveToLast();
            till = new Till();
            till.setId(newCursor.getLong(newCursor.getColumnIndex("_id")));
            till.setStatus(Till.CLOSED);
            till.setOpenDate(newCursor.getLong(newCursor.getColumnIndex("OPEN_DATE")));
            till.setCloseDate(newCursor.getLong(newCursor.getColumnIndex("CLOSE_DATE")));
            till.setDebtSales(newCursor.getDouble(newCursor.getColumnIndex("DEBT_SALES")));
            e.onSuccess(till);
        });
    }

    @Override
    public Single<TillManagementOperation> insertTillCloseOperation(TillManagementOperation tillCloseOperation) {
        return Single.create(singleSubscriber -> {
            mDaoSession.getTillManagementOperationDao().insertOrReplace(tillCloseOperation);
            singleSubscriber.onSuccess(tillCloseOperation);
        });
    }

    @Override
    public Single<Till> getTillById(Long tillId) {
        return Single.create(singleSubscriber -> {
            Till till = mDaoSession.getTillDao().load(tillId);
            singleSubscriber.onSuccess(till);
        });
    }

    @Override
    public Single<List<TillManagementOperation>> insertTillCloseOperationList(List<TillManagementOperation> tillCloseOperations) {
        return Single.create(singleSubscriber -> {
            mDaoSession.getTillManagementOperationDao().insertOrReplaceInTx(tillCloseOperations);
            singleSubscriber.onSuccess(tillCloseOperations);
        });
    }

    @Override
    public Single<List<TillManagementOperation>> getTillManagementOperationsByTillId(Long id) {
        return Single.create(e -> {
            List<TillManagementOperation> operations = mDaoSession.getTillManagementOperationDao().queryBuilder()
                    .where(TillManagementOperationDao.Properties.TillId.eq(id))
                    .build()
                    .list();
            e.onSuccess(operations);
        });
    }

    @Override
    public Single<Double> getTotalTillOperationsAmount(Long accountId, Long tillId, int type) {
        return Single.create(singleSubscriber -> {
            String query = "SELECT SUM(OnItemClickListener.AMOUNT) AS TOTAL FROM TILL_OPERATIONS OnItemClickListener INNER JOIN PAYMENT_TYPE b ON OnItemClickListener.PAYMENT_TYPE_ID = b._id" +
                    " WHERE b.ACCOUNT_ID = " + accountId + " AND OnItemClickListener.TILL_ID = " + tillId + " AND OnItemClickListener.TYPE =" + type;
            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, null);
            cursor.moveToFirst();
            double operationAmount = 0;
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    operationAmount += cursor.getDouble(cursor.getColumnIndex("TOTAL"));
                    cursor.moveToNext();
                }
            }
            singleSubscriber.onSuccess(operationAmount);
        });
    }

    @Override
    public Single<Double> getTotalTillManagementOperationsAmount(Long accountId, Long tillId, int type) {
        return Single.create(singleSubscriber -> {
            String query = "SELECT SUM(AMOUNT) AS TOTAL FROM TILL_MANAGEMENT_OPERATION  WHERE ACCOUNT_ID = " + accountId + " AND TILL_ID = " + tillId + " AND TYPE =" + type;
            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, null);
            cursor.moveToFirst();
            double operationAmount = 0;
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    operationAmount += cursor.getDouble(cursor.getColumnIndex("TOTAL"));
                    cursor.moveToNext();
                }
            }
            singleSubscriber.onSuccess(operationAmount);
        });
    }

    @Override
    public Single<Double> getBillingOperationsAmountInInterval(Long accountId, Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            String query = "SELECT SUM(AMOUNT) AS TOTAL FROM BILLING_OPERATION " +
                    "WHERE ACCOUNT_ID IS NOT NULL AND ACCOUNT_ID = " + accountId + " AND IS_DELETED = " + 0 + " AND PAYMENT_DATE BETWEEN " + fromDate.getTimeInMillis() + " AND " + toDate.getTimeInMillis();
            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, null);
            cursor.moveToFirst();
            double operationAmount = 0;
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    operationAmount += cursor.getDouble(cursor.getColumnIndex("TOTAL"));
                    cursor.moveToNext();
                }
            }
            e.onSuccess(operationAmount);
        });
    }

    @Override
    public Single<Double> getCustomerPaymentsInInterval(Long id, Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            String query = "SELECT SUM(OnItemClickListener.PAYMENT_AMOUNT) AS TOTAL FROM CUSTOMER_PAYMENT OnItemClickListener INNER JOIN PAYMENT_TYPE b ON OnItemClickListener.PAYMENT_TYPE_ID = b._id" +
                    " WHERE b.ACCOUNT_ID = " + id + " AND PAYMENT_DATE BETWEEN " + fromDate.getTimeInMillis() + " AND " + toDate.getTimeInMillis();
            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, null);
            cursor.moveToFirst();
            double operationAmount = 0;
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    operationAmount += cursor.getDouble(cursor.getColumnIndex("TOTAL"));
                    cursor.moveToNext();
                }
            }
            e.onSuccess(operationAmount);
        });
    }

    @Override
    public Single<List<Order>> getOrdersByTillId(Long id) {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getOrderDao().queryBuilder()
                    .where(OrderDao.Properties.TillId.eq(id))
                    .build().list());
        });
    }

    @Override
    public Single<TillDetails> getTillDetailsByAccountId(Long accountId, Long tillId) {
        return Single.create(e -> {
            String query = "SELECT * FROM TILL_DETAILS WHERE ACCOUNT_ID = " + accountId + " AND TILL_ID = " + tillId;
            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                TillDetails details = new TillDetails();
                details.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                details.setTips(cursor.getDouble(cursor.getColumnIndex("TIPS")));
                details.setTotalStartingCash(cursor.getDouble(cursor.getColumnIndex("TOTAL_STARTING_CASH")));
                details.setTotalSales(cursor.getDouble(cursor.getColumnIndex("TOTAL_SALES")));
                details.setTotalPayToVendors(cursor.getDouble(cursor.getColumnIndex("TOTAL_PAY_TO_VENDORS")));
                details.setTotalPayIns(cursor.getDouble(cursor.getColumnIndex("TOTAL_PAY_INS")));
                details.setTotalPayOuts(cursor.getDouble(cursor.getColumnIndex("TOTAL_PAY_OUTS")));
                details.setTotalDebtIncome(cursor.getDouble(cursor.getColumnIndex("TOTAL_DEBT_INCOME")));
                details.setTotalBankDrops(cursor.getDouble(cursor.getColumnIndex("TOTAL_BANK_DROPS")));
                details.setAccount(mDaoSession.getAccountDao().load(cursor.getLong(cursor.getColumnIndex("ACCOUNT_ID"))));
                details.setTillId(cursor.getLong(cursor.getColumnIndex("TILL_ID")));
                e.onSuccess(details);
            }
        });
    }

    @Override
    public Single<List<Order>> getAllHoldOrders() {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getOrderDao().queryBuilder()
                    .where(OrderDao.Properties.Status.eq(Order.HOLD_ORDER),
                            OrderDao.Properties.IsArchive.eq(false))
                    .build().list());
        });
    }

    @Override
    public Single<Integer> removeAllOrders() {
        return Single.create(e -> {
            mDaoSession.getOrderDao().deleteAll();
            e.onSuccess(1);
        });
    }

    @Override
    public Single<Long> insertOrderChangeLog(OrderChangesLog orderChangesLog) {
        return Single.create(e -> {
            long l = mDaoSession.insertOrReplace(orderChangesLog);
            e.onSuccess(l);
        });
    }

    @Override
    public Single<Long> getLastOrderId() {
        return Single.create(e -> {
            String query = "SELECT MAX(_id) as MAX FROM ALL_ORDER";
            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, null);
            cursor.moveToFirst();
            long max = cursor.getLong(cursor.getColumnIndex("MAX"));
            e.onSuccess(max);
        });
    }

    @Override
    public Single<Long> getLastArchiveOrderId() {
        return Single.create(e -> {
            String query = "SELECT MAX(_id) as MAX FROM ALL_ORDER WHERE IS_ARCHIVE";
            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, null);
            cursor.moveToFirst();
            long max = cursor.getLong(cursor.getColumnIndex("MAX"));
            e.onSuccess(max);
        });
    }

    @Override
    public Single<Order> getOrder(Long orderId) {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getOrderDao().load(orderId));
        });
    }

    @Override
    public Single<Boolean> deleteDebt(Debt debt) {
        return Single.create(e -> {
            mDaoSession.getDebtDao().delete(debt);
            e.onSuccess(true);
        });
    }

    @Override
    public Single<Long> deleteOrderProductsOnHold(List<OrderProduct> orderProducts) {
        return Single.create(e -> {
            //TODO IT CAN BE OPTIMIZED IF YOU WANT IT
            List<DetialCount> detialCounts = new ArrayList<>();
                for (OrderProduct orderProduct:orderProducts){
                    detialCounts.addAll(orderProduct.getOutcomeProduct().getDetialCount());
                    mDaoSession.getOutcomeProductDao().delete(orderProduct.getOutcomeProduct());
                }
            for(DetialCount detialCount: detialCounts) {
                detialCount.getStockQueue().setAvailable(detialCount.getStockQueue().getAvailable() + detialCount.getCount());
                mDaoSession.getStockQueueDao().insertOrReplace(detialCount.getStockQueue());
            }
            mDaoSession.getDetialCountDao().deleteInTx(detialCounts);
            mDaoSession.getOrderProductDao().deleteInTx(orderProducts);
            e.onSuccess(1l);
        });
    }

    @Override
    public Single<Long> deletePayedPartitions(List<PayedPartitions> payedPartitions) {
        return Single.create(e -> {
            mDaoSession.getPayedPartitionsDao().deleteInTx(payedPartitions);
            e.onSuccess(1l);
        });
    }

    @Override
    public Single<List<Order>> getAllTillClosedOrders() {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getOrderDao().queryBuilder()
                    .where(OrderDao.Properties.Status.eq(Order.CLOSED_ORDER),
                            OrderDao.Properties.IsArchive.eq(false))
                    .build().list());
        });
    }




    @Override
    public Single<Boolean> isProductSkuExists(String sku, Long subcategoryId) {
        return Single.create(e -> {
            e.onSuccess(!mDaoSession
                    .queryBuilder(Product.class)
                    .where(ProductDao.Properties.Sku.eq(sku), ProductDao.Properties.Deleted.eq(false))
                    .list()
                    .isEmpty());
        });
    }

    @Override
    public Single<Boolean> isInvoiceNumberExists(String number) {
        return Single.create(e -> {
            e.onSuccess(!mDaoSession
                    .queryBuilder(Invoice.class)
                    .where(InvoiceDao.Properties.ConsigmentNumber.eq(number), InvoiceDao.Properties.IsDeleted.eq(false))
                    .list()
                    .isEmpty());
        });
    }

    @Override
    public Single<Vendor> detachVendor(Vendor vendor) {
        return Single.create(e -> {
            mDaoSession.getVendorDao().detachAll();
            e.onSuccess(vendor);
        });
    }

    @Override
    public Single<List<Till>> getAllTills() {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getTillDao().loadAll());
        });
    }

    @Override
    public Single<List<TillDetails>> getTillDetailsByTillId(Long tillId) {
        return Single.create(e -> e.onSuccess(mDaoSession.getTillDetailsDao().queryBuilder().where(TillDetailsDao.Properties.TillId.eq(tillId)).build().list()));
    }

    @Override
    public Single<List<Till>> getAllClosedTills() {
        return Single.create(e -> e.onSuccess(mDaoSession.getTillDao().queryBuilder().where(TillDao.Properties.Status.eq(Till.CLOSED)).build().list()));
    }

    @Override
    public Single<List<Till>> getClosedTillsInInterval(Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            List<Till> tills = mDaoSession.getTillDao().queryBuilder()
                    .where(TillDao.Properties.CloseDate.ge(fromDate.getTimeInMillis()),
                            TillDao.Properties.CloseDate.le(toDate.getTimeInMillis()),
                            TillDao.Properties.Status.eq(Till.CLOSED))
                    .build().list();

            e.onSuccess(tills);
        });
    }

    @Override
    public Single<List<Order>> getClosedOrdersInIntervalForReport(Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            List<Order> orderList = mDaoSession.getOrderDao().queryBuilder()
                    .where(OrderDao.Properties.CreateAt.ge(fromDate.getTimeInMillis()),
                            OrderDao.Properties.CreateAt.le(toDate.getTimeInMillis()),
                            OrderDao.Properties.Status.eq(Order.CLOSED_ORDER))
                    .build().list();
            e.onSuccess(orderList);
        });
    }

    @Override
    public Single<List<Discount>> getAllDiscountsWithoutFiltering() {
        return Single.create(e -> e.onSuccess(mDaoSession.getDiscountDao().loadAll()));
    }

    @Override
    public Single<DiscountLog> insertDiscountLog(DiscountLog discountLog) {
        return Single.create(e -> {
            mDaoSession.getDiscountLogDao().insertOrReplace(discountLog);
            e.onSuccess(discountLog);
        });
    }

    @Override
    public Single<ServiceFeeLog> insertServiceFeeLog(ServiceFeeLog serviceFeeLog) {
        return Single.create(e -> {
            mDaoSession.getServiceFeeLogDao().insertOrReplace(serviceFeeLog);
            e.onSuccess(serviceFeeLog);
        });
    }

    @Override
    public Single<List<DiscountLog>> getDiscountLogs() {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getDiscountLogDao().loadAll());
        });
    }

    @Override
    public Single<List<ServiceFeeLog>> getServiceFeeLogs() {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getServiceFeeLogDao().loadAll());
        });
    }

    @Override
    public Single<Order> getLastOrderWithCustomer(Long customerId) {
        return Single.create(e -> {
            List<Order> orders = mDaoSession.getOrderDao().queryBuilder()
                    .where(OrderDao.Properties.Customer_id.eq(customerId), OrderDao.Properties.Status.eq(Order.CLOSED_ORDER))
                    .build()
                    .list();
            e.onSuccess(orders.get(orders.size() - 1));
        });
    }

    @Override
    public Single<List<Debt>> getAllCustomerDebtsInInterval(Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            List<Debt> debts = mDaoSession.getDebtDao().queryBuilder()
                    .where(DebtDao.Properties.TakenDate.ge(fromDate.getTimeInMillis()),
                            DebtDao.Properties.TakenDate.le(toDate.getTimeInMillis()))
                    .build().list();
            e.onSuccess(debts);
        });
    }

    @Override
    public Single<List<Order>> getOrdersWithCustomerInInterval(Long id, Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            List<Order> orders = mDaoSession.getOrderDao().queryBuilder()
                    .where(OrderDao.Properties.Customer_id.eq(id),
                            OrderDao.Properties.CreateAt.ge(fromDate.getTimeInMillis()),
                            OrderDao.Properties.CreateAt.le(toDate.getTimeInMillis()))
                    .build()
                    .list();
            e.onSuccess(orders);
        });
    }






    @Override
    public Single<List<Return>> getReturnList(Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            List<Return> returnList = mDaoSession.getReturnDao().queryBuilder()
                    .where(ReturnDao.Properties.CreateAt.ge(fromDate.getTimeInMillis()),
                            ReturnDao.Properties.CreateAt.le(toDate.getTimeInMillis()))
                    .build().list();
            e.onSuccess(returnList);
        });
    }

    @Override
    public Single<List<TillOperation>> getTillOperationsInterval(Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            List<TillOperation> orderList = mDaoSession.getTillOperationDao().queryBuilder()
                    .where(TillOperationDao.Properties.CreateAt.ge(fromDate.getTimeInMillis()),
                            TillOperationDao.Properties.CreateAt.le(toDate.getTimeInMillis()))
                    .build().list();
            e.onSuccess(orderList);
        });
    }

    @Override
    public PaymentType getCashPaymentType() {
        List<PaymentType> paymentTypes = mDaoSession.getPaymentTypeDao().queryBuilder().where(PaymentTypeDao.Properties.TypeStaticPaymentType.eq(PaymentType.CASH_PAYMENT_TYPE)).build().list();
        if (paymentTypes.size() != 1) {
            new Exception("Cash payment type not equals ONE!!! Some Think wrong with").printStackTrace();
        }
        return paymentTypes.get(0);
    }

    @Override
    public PaymentType getPaymentTypeById(long id) {
        return mDaoSession.getPaymentTypeDao().load(id);
    }

    @Override
    public Single<List<Consignment>> getConsignmentsInInterval(Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            List<Consignment> consignments = mDaoSession.getConsignmentDao().queryBuilder()
                    .where(ConsignmentDao.Properties.CreatedDate.ge(fromDate.getTimeInMillis()),
                            ConsignmentDao.Properties.CreatedDate.le(toDate.getTimeInMillis()))
                    .build().list();

            e.onSuccess(consignments);
        });
    }

    @Override
    public Single<List<ConsignmentProduct>> getConsignmentProductsInterval(Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            List<ConsignmentProduct> consignments = mDaoSession.getConsignmentProductDao().queryBuilder()
                    .where(ConsignmentProductDao.Properties.CreatedDate.ge(fromDate.getTimeInMillis()),
                            ConsignmentProductDao.Properties.CreatedDate.le(toDate.getTimeInMillis()),
                            ConsignmentProductDao.Properties.IsDeleted.eq(false))
                    .build().list();

            e.onSuccess(consignments);
        });
    }

    @Override
    public Single<List<BillingOperations>> getAllBillingOperationsInInterval(Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            List<BillingOperations> billingOperations = mDaoSession.getBillingOperationsDao().queryBuilder()
                    .where(BillingOperationsDao.Properties.PaymentDate.ge(fromDate.getTimeInMillis()),
                            BillingOperationsDao.Properties.PaymentDate.le(toDate.getTimeInMillis()))
                    .build().list();

            e.onSuccess(billingOperations);
        });
    }

    @Override
    public Single<Vendor> getVendorByName(String vendorName) {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getVendorDao().queryBuilder().where(VendorDao.Properties.Name.eq(vendorName), VendorDao.Properties.Deleted.eq(false)).build().unique());
        });
    }

    @Override
    public Single<Account> getSystemAccount() {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getAccountDao().queryBuilder().where(AccountDao.Properties.IsNotSystemAccount.eq(false)).build().unique());
        });
    }

    @Override
    public Single<PaymentType> getSystemPaymentType() {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getPaymentTypeDao().queryBuilder().where(PaymentTypeDao.Properties.IsNotSystem.eq(false)).build().unique());
        });
    }

    @Override
    public Single<List<Discount>> getStaticDiscounts() {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getDiscountDao().queryBuilder()
                    .where(DiscountDao.Properties.IsManual.eq(false),
                            DiscountDao.Properties.Delete.eq(false))
                    .build()
                    .list());
        });
    }

    @Override
    public Single<List<Discount>> getDiscountsByType(int discountApplyType) {
        return Single.create(e -> {
            QueryBuilder<Discount> queryBuilder = mDaoSession.getDiscountDao().queryBuilder();
            queryBuilder.whereOr(DiscountDao.Properties.UsedType.eq(discountApplyType), DiscountDao.Properties.UsedType.eq(Discount.ALL));
            queryBuilder.where(DiscountDao.Properties.IsManual.eq(false),
                    DiscountDao.Properties.Active.eq(true),
                    DiscountDao.Properties.Delete.eq(false));
            e.onSuccess(queryBuilder.build().list());
        });
    }

    @Override
    public Single<List<Customer>> getCustomersWithoutSorting() {
        return Single.create(e -> e.onSuccess(mDaoSession.getCustomerDao().loadAll()));
    }

    @Override
    public Single<List<Order>> getOrdersInIntervalForReport(Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            List<Order> orderList = mDaoSession.getOrderDao().queryBuilder()
                    .where(OrderDao.Properties.CreateAt.ge(fromDate.getTimeInMillis()),
                            OrderDao.Properties.CreateAt.le(toDate.getTimeInMillis()))
                    .build().list();
            e.onSuccess(orderList);
        });
    }

    @Override
    public Single<Invoice> insertInvoice(Invoice invoice) {
        return Single.create(e -> mDaoSession.getInvoiceDao().insertOrReplace(invoice));
    }

    @Override
    public Single<IncomeProduct> insertIncomeProduct(IncomeProduct incomeProduct) {
        return Single.create(e -> {
            mDaoSession.getIncomeProductDao().insertOrReplace(incomeProduct);
            e.onSuccess(incomeProduct);
        });
    }

    @Override
    public Single<StockQueue> insertStockQueue(StockQueue stockQueue) {
        return Single.create(e -> {
            mDaoSession.getStockQueueDao().insertOrReplace(stockQueue);
            e.onSuccess(stockQueue);
        });
    }

    @Override
    public Single<Invoice> insertInvoiceWithBillingAndIncomeProduct(Invoice invoice, List<IncomeProduct> incomeProductList, List<StockQueue> stockQueueList, List<BillingOperations> billingOperationsList) {
        return Single.create(e -> {
            mDaoSession.getInvoiceDao().insertOrReplace(invoice);
            for (int i = 0; i < billingOperationsList.size(); i++) {
                billingOperationsList.get(i).setInvoiceId(invoice.getId());
                insertBillingOperation(billingOperationsList.get(i)).subscribe();
                if (billingOperationsList.get(i).getOperationType() == BillingOperations.PAID_TO_CONSIGNMENT) {
                    invoice.setFirstPayId(billingOperationsList.get(i).getId());
                    mDaoSession.getInvoiceDao().insertOrReplace(invoice);
                }
            }
            for (int i = 0; i < incomeProductList.size(); i++) {
                IncomeProduct incomeProduct = incomeProductList.get(i);
                StockQueue stockQueue = stockQueueList.get(i);
                incomeProduct.setInvoiceId(invoice.getId());
                incomeProduct.setIncomeDate(System.currentTimeMillis());
                mDaoSession.getIncomeProductDao().insertOrReplace(incomeProduct);
                stockQueue.setIncomeCount(incomeProduct.getCountValue());
                stockQueue.setAvailable(incomeProduct.getCountValue());
                stockQueue.setIncomeProduct(incomeProduct);
                stockQueue.setCost(incomeProduct.getCostValue());
                mDaoSession.getStockQueueDao().insertOrReplace(stockQueue);
                incomeProduct.setStockQueue(stockQueue);
                mDaoSession.getIncomeProductDao().insertOrReplace(incomeProduct);
            }
            e.onSuccess(invoice);
        });
    }

    @Override
    public Single<List<Invoice>> getAllInvoices() {
        return Single.create(e -> e.onSuccess(mDaoSession.getInvoiceDao().queryBuilder().build().list()));
    }



    //TODO <----

    @Override
    public Single<List<Product>> getVendorProductsByVendorId(Long id) {
        return Single.create(e -> {
            String stockQuery = "SELECT * FROM STOCK_QUEUE WHERE VENDOR_ID == " + id + " GROUP BY PRODUCT_ID";
            Cursor stockCursor = mDaoSession.getDatabase().rawQuery(stockQuery, null);
            List<Product> products = new ArrayList<>();
            if (stockCursor.getCount() > 0) {
                stockCursor.moveToFirst();
                while (!stockCursor.isAfterLast()){
                    products.add(mDaoSession.getProductDao().load(stockCursor.getLong(stockCursor.getColumnIndex("PRODUCT_ID"))));
                    stockCursor.moveToNext();
                }
            }
            e.onSuccess(products);
        });
    }
    @Override
    public Single<Double> getLastCostForProduct(Long productId) {
        return Single.create(e -> {
            double cost = 0;
            String query = "SELECT * FROM STOCK_QUEUE WHERE PRODUCT_ID == " + productId;
            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, null);
            if (cursor.getCount() > 0){
                cursor.moveToLast();
                cost = cursor.getLong(cursor.getColumnIndex("COST"));
            }
            e.onSuccess(cost);
        });
    }

    @Override
    public Single<List<StockQueue>> getAvailableStockQueuesByProductId(Long id) {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getStockQueueDao().queryBuilder().where(StockQueueDao.Properties.ProductId.eq(id), StockQueueDao.Properties.Available.ge(0.0009)).build().list());
        });
    }

    @Override
    public Single<Double> getAvailableCountForProduct(Long id) {
        return Single.create(e ->{
                    String query = "SELECT  SUM(AVAILABLE) AS AMOUNT FROM STOCK_QUEUE WHERE AVAILABLE > 0.0009 AND PRODUCT_ID == " + id;
                    Cursor cursor = mDaoSession.getDatabase().rawQuery(query, null);
                    double count = 0;
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        count = cursor.getDouble(cursor.getColumnIndex("AMOUNT"));
                    }
                    e.onSuccess(count);
                }
        );
    }


    @Override
    public Single<Double> getAvailableCount(Long productId) {
        return Single.create(e -> {
            String sumStockAvailableQuery = "SELECT SUM(AVAILABLE) FROM STOCK_QUEUE WHERE PRODUCT_ID = " + productId + " AND AVAILABLE > 0.0009";
            Cursor cursorStockAvailable = mDaoSession.getDatabase().rawQuery(sumStockAvailableQuery, null);
            cursorStockAvailable.moveToFirst();
            double summaryAvailable = 0;
            if (cursorStockAvailable.getCount() > 0) {
                cursorStockAvailable.moveToFirst();
                while (!cursorStockAvailable.isAfterLast()) {
                    summaryAvailable = cursorStockAvailable.getDouble(0);
                    cursorStockAvailable.moveToNext();
                }
            }
            e.onSuccess(summaryAvailable);
        });
    }

    @Override
    public OutcomeProduct insertOutcome(OutcomeProduct outcomeProduct) {
        mDaoSession.getOutcomeProductDao().insertOrReplace(outcomeProduct);
        return outcomeProduct;
    }

    @Override
    public Long insetDetialCounts(List<DetialCount> detialCounts) {
        mDaoSession.getDetialCountDao().insertInTx(detialCounts);
        for(DetialCount detialCount:detialCounts){
            detialCount.getStockQueue().setAvailable(detialCount.getStockQueue().getAvailable()-detialCount.getCount());
            mDaoSession.getStockQueueDao().insertOrReplace(detialCount.getStockQueue());
        }
        return 1l;
    }

    @Override
    public Single<Integer> cancelOutcomeProductWhenOrderProductCanceled(List<OrderProduct> orderProducts) {
        return Single.create(e -> {
            List<DetialCount> detialCounts = new ArrayList<>();

            for (OrderProduct orderProduct:orderProducts){
                detialCounts.addAll(orderProduct.getOutcomeProduct().getDetialCount());
                mDaoSession.getOutcomeProductDao().delete(orderProduct.getOutcomeProduct());
                orderProduct.setOutcomeProductId(null);
                mDaoSession.getOrderProductDao().insertOrReplace(orderProduct);
            }
            for(DetialCount detialCount: detialCounts) {
                detialCount.getStockQueue().setAvailable(detialCount.getStockQueue().getAvailable() + detialCount.getCount());
                mDaoSession.getStockQueueDao().insertOrReplace(detialCount.getStockQueue());
            }
            mDaoSession.getDetialCountDao().deleteInTx(detialCounts);
            e.onSuccess(1);
        });
    }

    @Override
    public Single<List<OutcomeWithDetials>> checkPositionAvailablity(List<OutcomeProduct> outcomeProducts) {
        return Single.create(e -> {
            //TODO CAN BE OPTIMIZED
            String queryForStock = "SELECT _ID, AVAILABLE, COST, PRODUCT_ID, EXPIRED_PRODUCT_DATE  FROM STOCK_QUEUE WHERE AVAILABLE > 0.0009 AND PRODUCT_ID IN (";
            for (int i = 0; i < outcomeProducts.size(); i++) {
                queryForStock +=  " " + outcomeProducts.get(i).getProductId() ;
                if(i+1!=outcomeProducts.size())
                    queryForStock += ",";
                else queryForStock += ")";
            }
            Cursor cursorForStock = mDaoSession.getDatabase().rawQuery(queryForStock, null);

            HashMap<Long,List<StockPerfomanceWithProduct>> stockPerHashMap = new HashMap<>();
            if (cursorForStock.getCount() > 0) {
                cursorForStock.moveToFirst();
                while (!cursorForStock.isAfterLast()) {
                    StockPerfomanceWithProduct stockPerfomanceWithProduct = new StockPerfomanceWithProduct(cursorForStock.getLong(0), cursorForStock.getDouble(1), cursorForStock.getDouble(2), cursorForStock.getLong(3), cursorForStock.getLong(4));
                    if(stockPerHashMap.get(stockPerfomanceWithProduct.getProductId())!=null){
                        stockPerHashMap.get(stockPerfomanceWithProduct.getProductId()).add(stockPerfomanceWithProduct);
                    }else{
                        ArrayList<StockPerfomanceWithProduct> stockPerfomanceWithProducts = new ArrayList<>();
                        stockPerfomanceWithProducts.add(stockPerfomanceWithProduct);
                        stockPerHashMap.put(stockPerfomanceWithProduct.getProductId(),stockPerfomanceWithProducts);
                    }
                    cursorForStock.moveToNext();
                }
            }else new Exception("In Stock Not Have Queue? Some think is wrong, because when Outcome registered stock queue was!!!").printStackTrace();

            for(OutcomeProduct outcomeProduct: outcomeProducts){
                if(outcomeProduct.getProduct().getStockKeepType()==Product.LIFO) Collections.sort(stockPerHashMap.get(outcomeProduct.getProductId()),comparatorLIFO);
                if(outcomeProduct.getProduct().getStockKeepType()==Product.FEFO) Collections.sort(stockPerHashMap.get(outcomeProduct.getProductId()),comparatorFEFO);
            }

            List<OutcomeWithDetials> outcomeWithDetials = new ArrayList<>();
            for (int i = 0; i < outcomeProducts.size(); i++) {
                outcomeWithDetials.add(new OutcomeWithDetials(outcomeProducts.get(i),null));
            }

            //CUSTOM PICK
            for (int i = 0; i < outcomeProducts.size(); i++) {
                if(outcomeProducts.get(i).getCustomPickSock()){
                    for (StockPerfomanceWithProduct stockPerfomanceWithProduct:stockPerHashMap.get(outcomeProducts.get(i).getProductId())) {
                        if(stockPerfomanceWithProduct.getStockId()== outcomeProducts.get(i).getPickedStockQueueId()){
                            if(stockPerfomanceWithProduct.getAvailable()>=outcomeProducts.get(i).getSumCountValue()){
                                stockPerfomanceWithProduct.setAvailable(stockPerfomanceWithProduct.getAvailable()-outcomeProducts.get(i).getSumCountValue());
                                DetialCount detialCount = new DetialCount();
                                detialCount.setStockId(stockPerfomanceWithProduct.getStockId());
                                detialCount.setCount(outcomeProducts.get(i).getSumCountValue());
                                detialCount.setCost(stockPerfomanceWithProduct.getCost());
                                List<DetialCount> detialCounts = new ArrayList<>();
                                detialCounts.add(detialCount);
                                outcomeWithDetials.get(i).setDetialCountList(detialCounts);
                            }else{
                                //STOCK OUT, CUSTOM PICK
                                e.onSuccess(new ArrayList<>());
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < outcomeProducts.size(); i++) {
                if(!outcomeProducts.get(i).getCustomPickSock()){
                    List<DetialCount> detialCounts = new ArrayList<>();
                    double accumelate = outcomeProducts.get(i).getSumCountValue();
                    for (StockPerfomanceWithProduct stockPerfomanceWithProduct:stockPerHashMap.get(outcomeProducts.get(i).getProductId())){
                        if(stockPerfomanceWithProduct.getAvailable()>0.0001){
                            double available = stockPerfomanceWithProduct.getAvailable() - accumelate;
                            if (available >= 0) {
                                stockPerfomanceWithProduct.setAvailable(available);
                                DetialCount detialCount = new DetialCount();
                                detialCount.setStockId(stockPerfomanceWithProduct.getStockId());
                                detialCount.setCount(accumelate);
                                detialCount.setCost(stockPerfomanceWithProduct.getCost());
                                detialCounts.add(detialCount);
                                accumelate = 0;
                            } else {
                                DetialCount detialCount = new DetialCount();
                                detialCount.setStockId(stockPerfomanceWithProduct.getStockId());
                                detialCount.setCount(stockPerfomanceWithProduct.getAvailable());
                                detialCount.setCost(stockPerfomanceWithProduct.getCost());
                                detialCounts.add(detialCount);
                                stockPerfomanceWithProduct.setAvailable(0);
                                accumelate = available * -1;
                            }
                        }
                        if(accumelate<0.0001){
                            break;
                        }
                    }
                    if(accumelate>0.001) e.onSuccess(new ArrayList<>());

                    outcomeWithDetials.get(i).setDetialCountList(detialCounts);
                }
            }
            e.onSuccess(outcomeWithDetials);
        });
    }

    @Override
    public Single<List<OutcomeWithDetials>> checkPositionAvailablityWithoutSomeOutcomes(List<OutcomeProduct> outcomeProducts, List<OutcomeProduct> withoutOutcomeProducts) {
        return Single.create(e -> {
            //TODO CAN BE OPTIMIZED
            String queryForStock = "SELECT _ID, AVAILABLE, COST, PRODUCT_ID, EXPIRED_PRODUCT_DATE  FROM STOCK_QUEUE WHERE AVAILABLE > 0.0009 AND PRODUCT_ID IN (";
            for (int i = 0; i < outcomeProducts.size(); i++) {
                queryForStock +=  " " + outcomeProducts.get(i).getProductId() ;
                if(i+1!=outcomeProducts.size())
                    queryForStock += ",";
                else queryForStock += ")";
            }
            Cursor cursorForStock = mDaoSession.getDatabase().rawQuery(queryForStock, null);

            HashMap<Long,List<StockPerfomanceWithProduct>> stockPerHashMap = new HashMap<>();
            if (cursorForStock.getCount() > 0) {
                cursorForStock.moveToFirst();
                while (!cursorForStock.isAfterLast()) {
                    StockPerfomanceWithProduct stockPerfomanceWithProduct = new StockPerfomanceWithProduct(cursorForStock.getLong(0), cursorForStock.getDouble(1), cursorForStock.getDouble(2), cursorForStock.getLong(3), cursorForStock.getLong(4));
                    if(stockPerHashMap.get(stockPerfomanceWithProduct.getProductId())!=null){
                        stockPerHashMap.get(stockPerfomanceWithProduct.getProductId()).add(stockPerfomanceWithProduct);
                    }else{
                        ArrayList<StockPerfomanceWithProduct> stockPerfomanceWithProducts = new ArrayList<>();
                        stockPerfomanceWithProducts.add(stockPerfomanceWithProduct);
                        stockPerHashMap.put(stockPerfomanceWithProduct.getProductId(),stockPerfomanceWithProducts);
                    }
                    cursorForStock.moveToNext();
                }
            }else new Exception("In Stock Not Have Queue? Something went wrong, because when Outcome registered stock queue was!!!").printStackTrace();

            for(OutcomeProduct outcomeProduct: outcomeProducts){
                if(outcomeProduct.getProduct().getStockKeepType()==Product.LIFO) Collections.sort(stockPerHashMap.get(outcomeProduct.getId()),comparatorLIFO);
                if(outcomeProduct.getProduct().getStockKeepType()==Product.FEFO) Collections.sort(stockPerHashMap.get(outcomeProduct.getId()),comparatorFEFO);
            }


            for (OutcomeProduct outcomeProduct:withoutOutcomeProducts) {
                if(stockPerHashMap.get(outcomeProduct.getProductId())!=null){
                    for (DetialCount detialCount:outcomeProduct.getDetialCount()){
                        for (StockPerfomanceWithProduct stockPerfomanceWithProduct:stockPerHashMap.get(outcomeProduct.getProductId())) {
                            if(stockPerfomanceWithProduct.getStockId() == detialCount.getStockId()){
                                stockPerfomanceWithProduct.setAvailable(stockPerfomanceWithProduct.getAvailable()+detialCount.getCount());
                            }
                        }
                    }
                }

            }

            List<OutcomeWithDetials> outcomeWithDetials = new ArrayList<>();
            for (int i = 0; i < outcomeProducts.size(); i++) {
                outcomeWithDetials.add(new OutcomeWithDetials(outcomeProducts.get(i),null));
            }

            //CUSTOM PICK
            for (int i = 0; i < outcomeProducts.size(); i++) {
                if(outcomeProducts.get(i).getCustomPickSock()){
                    for (StockPerfomanceWithProduct stockPerfomanceWithProduct:stockPerHashMap.get(outcomeProducts.get(i).getProductId())) {
                        if(stockPerfomanceWithProduct.getStockId()== outcomeProducts.get(i).getPickedStockQueueId()){
                            if(stockPerfomanceWithProduct.getAvailable()>=outcomeProducts.get(i).getSumCountValue()){
                                stockPerfomanceWithProduct.setAvailable(stockPerfomanceWithProduct.getAvailable()-outcomeProducts.get(i).getSumCountValue());
                                DetialCount detialCount = new DetialCount();
                                detialCount.setStockId(stockPerfomanceWithProduct.getStockId());
                                detialCount.setCount(outcomeProducts.get(i).getSumCountValue());
                                detialCount.setCost(stockPerfomanceWithProduct.getCost());
                                List<DetialCount> detialCounts = new ArrayList<>();
                                detialCounts.add(detialCount);
                                outcomeWithDetials.get(i).setDetialCountList(detialCounts);
                            }else{
                                //STOCK OUT, CUSTOM PICK
                                e.onSuccess(new ArrayList<>());
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < outcomeProducts.size(); i++) {
                if(!outcomeProducts.get(i).getCustomPickSock()){
                    List<DetialCount> detialCounts = new ArrayList<>();
                    double accumelate = outcomeProducts.get(i).getSumCountValue();
                    for (StockPerfomanceWithProduct stockPerfomanceWithProduct:stockPerHashMap.get(outcomeProducts.get(i).getProductId())){
                        if(stockPerfomanceWithProduct.getAvailable()>0.0001){
                            double available = stockPerfomanceWithProduct.getAvailable() - accumelate;
                            if (available >= 0) {
                                stockPerfomanceWithProduct.setAvailable(available);
                                DetialCount detialCount = new DetialCount();
                                detialCount.setStockId(stockPerfomanceWithProduct.getStockId());
                                detialCount.setCount(accumelate);
                                detialCount.setCost(stockPerfomanceWithProduct.getCost());
                                detialCounts.add(detialCount);
                                accumelate = 0;
                            } else {
                                DetialCount detialCount = new DetialCount();
                                detialCount.setStockId(stockPerfomanceWithProduct.getStockId());
                                detialCount.setCount(stockPerfomanceWithProduct.getAvailable());
                                detialCount.setCost(stockPerfomanceWithProduct.getCost());
                                detialCounts.add(detialCount);
                                stockPerfomanceWithProduct.setAvailable(0);
                                accumelate = available * -1;
                            }
                        }
                        if(accumelate<0.0001){
                            break;
                        }
                    }
                    if(accumelate>0.001) e.onSuccess(new ArrayList<>());

                    outcomeWithDetials.get(i).setDetialCountList(detialCounts);
                }
            }
            e.onSuccess(outcomeWithDetials);
        });
    }

    @Override
    public Single<List<InventoryItem>> getProductInventoryStatesForNow() {
        return Single.create(e -> {
            List<Product> products = mDaoSession.getProductDao().loadAll();
            String inventoryQuery = "SELECT PRODUCT_ID, SUM(AVAILABLE), GROUP_CONCAT(DISTINCT(VENDOR_ID)) FROM STOCK_QUEUE GROUP BY PRODUCT_ID";
            Cursor cursorInventory = mDaoSession.getDatabase().rawQuery(inventoryQuery, null);
            cursorInventory.moveToFirst();
            List<InventoryItem> inventoryItems = new ArrayList<>();
            if (cursorInventory.getCount() > 0) {
                cursorInventory.moveToFirst();
                while (!cursorInventory.isAfterLast()) {
                    InventoryItem inventoryItem = new InventoryItem();
                    inventoryItem.setProduct(mDaoSession.getProductDao().load(cursorInventory.getLong(0)));
                    inventoryItem.setLowStockAlert(0);
                    inventoryItem.setInventory(cursorInventory.getDouble(1));
                    String vendorsIdSingleLine = cursorInventory.getString(2);
                    String[] vendorsId;
                    List<Vendor> vendorList = new ArrayList<>();
                    if(vendorsIdSingleLine != null) {
                        vendorsId = vendorsIdSingleLine.split(",");
                        for(String strId:vendorsId) {
                            vendorList.add(mDaoSession.getVendorDao().load(Long.parseLong(strId)));
                        }
                    }
                    inventoryItem.setVendors(vendorList);
                    inventoryItems.add(inventoryItem);
                    cursorInventory.moveToNext();
                }
            }
            mark: for (Product product : products) {
                for (InventoryItem inventoryItem : inventoryItems) {
                    if(product.getId() == inventoryItem.getProduct().getId())
                        continue mark;
                }
                InventoryItem inventoryItem = new InventoryItem();
                inventoryItem.setProduct(product);
                inventoryItem.setLowStockAlert(0);
                inventoryItem.setInventory(0);
                List<Vendor> vendorList = new ArrayList<>();
                inventoryItem.setVendors(vendorList);
                inventoryItems.add(inventoryItem);
            }
            e.onSuccess(inventoryItems);
        });
    }

    @Override
    public Single<List<StockQueueItem>> getStockQueueItemForOutcomeProduct(OutcomeProduct outcomeProduct, List<OutcomeProduct> outcomeProductList, List<OutcomeProduct> exceptionList) {
        return Single.create(e -> {
            List<StockQueue> stockQueues = mDaoSession.getStockQueueDao().queryBuilder().where(StockQueueDao.Properties.ProductId.eq(outcomeProduct.getProduct().getId()), StockQueueDao.Properties.Available.ge(0.0009)).build().list();

            if(outcomeProduct.getProduct().getStockKeepType()==Product.LIFO) Collections.sort(stockQueues,(stockQueue, t1) -> t1.getId().compareTo(stockQueue.getId()));
            if(outcomeProduct.getProduct().getStockKeepType()==Product.FEFO) Collections.sort(stockQueues,(stockQueue, t1) -> stockQueue.getExpiredProductDate().compareTo(t1.getExpiredProductDate()));

            List<StockQueueItem> stockQueueItems = new ArrayList<>();
            for (int i = 0; i < stockQueues.size(); i++) {
                StockQueueItem stockQueueItem = new StockQueueItem();
                stockQueueItem.setStockQueue(stockQueues.get(i));
                stockQueueItem.setAvailable(stockQueues.get(i).getAvailable());
                stockQueueItems.add(stockQueueItem);
            }

            if (exceptionList != null){
                for (int i = 0; i < exceptionList.size(); i++) {
                    for (DetialCount detialCount: exceptionList.get(i).getDetialCount()){
                        for (StockQueueItem stockQueueItem: stockQueueItems) {
                            if(stockQueueItem.getStockQueue().getId().equals(detialCount.getStockId())){
                                stockQueueItem.setAvailable(stockQueueItem.getAvailable() + detialCount.getCount());
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < outcomeProductList.size(); i++) {
                if (outcomeProductList.get(i).getCustomPickSock()){
                    for (int j = 0; j < stockQueueItems.size(); j++) {
                        if (stockQueueItems.get(j).getStockQueue().getId().equals(outcomeProductList.get(i).getStockQueue().getId()))
                            stockQueueItems.get(j).setAvailable(stockQueueItems.get(j).getAvailable() - outcomeProductList.get(i).getSumCountValue());
                    }
                }
            }

            for (int i = 0; i < outcomeProductList.size(); i++) {
                if (!outcomeProductList.get(i).getCustomPickSock()){
                    double sumCount = outcomeProductList.get(i).getSumCountValue();
                    for (int j = 0; j < stockQueueItems.size(); j++) {
                        if (outcomeProduct.getCustomPickSock() && stockQueueItems.get(j).getStockQueue().getId().equals(outcomeProduct.getStockQueue().getId())){
                            if ((stockQueueItems.get(j).getAvailable()) > 0.0001){
                                if (stockQueueItems.get(j).getAvailable() > outcomeProduct.getSumCountValue()){
                                    if (sumCount != 0){
                                        if (sumCount >= stockQueueItems.get(j).getAvailable() - outcomeProduct.getSumCountValue()){
                                            sumCount -= stockQueueItems.get(j).getAvailable() - outcomeProduct.getSumCountValue();
                                            double count =  stockQueueItems.get(j).getAvailable() - outcomeProduct.getSumCountValue();
                                            stockQueueItems.get(j).setAvailable(stockQueueItems.get(j).getAvailable() - count);
                                        } else {
                                            stockQueueItems.get(j).setAvailable(stockQueueItems.get(j).getAvailable() - sumCount);
                                            sumCount = 0;
                                        }
                                    }
                                }
                            }
                        } else {
                            if ((stockQueueItems.get(j).getAvailable()) > 0.0001){
                                if (sumCount != 0){
                                    if (sumCount >= stockQueueItems.get(j).getAvailable()){
                                        sumCount -= stockQueueItems.get(j).getAvailable();
                                        stockQueueItems.get(j).setAvailable(0);
                                    } else {
                                        stockQueueItems.get(j).setAvailable(stockQueueItems.get(j).getAvailable() - sumCount);
                                        sumCount = 0;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            e.onSuccess(stockQueueItems);
        });
    }

    @Override
    public Single<OutcomeWithDetials> checkPositionAvailablity(OutcomeProduct outcomeProduct) {
        return Single.create(e -> {
            //TODO CAN BE OPTIMIZED
            String queryForStock = "SELECT _ID, AVAILABLE, COST, PRODUCT_ID, EXPIRED_PRODUCT_DATE  FROM STOCK_QUEUE WHERE AVAILABLE > 0.0009 AND PRODUCT_ID = "+outcomeProduct.getProductId();
            Cursor cursorForStock = mDaoSession.getDatabase().rawQuery(queryForStock, null);

            List<StockPerfomanceWithProduct> stockPerfomanceWithProducts = new ArrayList<>();

            if (cursorForStock.getCount() > 0) {
                cursorForStock.moveToFirst();
                while (!cursorForStock.isAfterLast()) {
                    StockPerfomanceWithProduct stockPerfomanceWithProduct = new StockPerfomanceWithProduct(cursorForStock.getLong(0), cursorForStock.getDouble(1), cursorForStock.getDouble(2), cursorForStock.getLong(3), cursorForStock.getLong(4));
                    stockPerfomanceWithProducts.add(stockPerfomanceWithProduct);
                    cursorForStock.moveToNext();
                }
            }else new Exception("In Stock Not Have Queue? Some think is wrong, because when Outcome registered stock queue was!!!").printStackTrace();


           OutcomeWithDetials outcomeWithDetials = new OutcomeWithDetials(outcomeProduct,null);
            //CUSTOM PICK
            if (outcomeProduct.getCustomPickSock()) {
                for (StockPerfomanceWithProduct stockPerfomanceWithProduct : stockPerfomanceWithProducts) {
                    if (stockPerfomanceWithProduct.getStockId() == outcomeProduct.getPickedStockQueueId()) {
                        if (stockPerfomanceWithProduct.getAvailable() >= outcomeProduct.getSumCountValue()) {
                            stockPerfomanceWithProduct.setAvailable(stockPerfomanceWithProduct.getAvailable() - outcomeProduct.getSumCountValue());
                            DetialCount detialCount = new DetialCount();
                            detialCount.setStockId(stockPerfomanceWithProduct.getStockId());
                            detialCount.setCount(outcomeProduct.getSumCountValue());
                            detialCount.setCost(stockPerfomanceWithProduct.getCost());
                            List<DetialCount> detialCounts = new ArrayList<>();
                            detialCounts.add(detialCount);
                            outcomeWithDetials.setDetialCountList(detialCounts);
                            e.onSuccess(outcomeWithDetials);
                        } else {
                            //STOCK OUT, CUSTOM PICK
                            e.onSuccess(new OutcomeWithDetials(null, null));
                        }
                    }
                }
            }else{
                List<DetialCount> detialCounts = new ArrayList<>();
                double accumelate = outcomeProduct.getSumCountValue();
                for (StockPerfomanceWithProduct stockPerfomanceWithProduct:stockPerfomanceWithProducts){
                    if(stockPerfomanceWithProduct.getAvailable()>0.0001){
                        double available = stockPerfomanceWithProduct.getAvailable() - accumelate;
                        if (available >= 0) {
                            stockPerfomanceWithProduct.setAvailable(available);
                            DetialCount detialCount = new DetialCount();
                            detialCount.setStockId(stockPerfomanceWithProduct.getStockId());
                            detialCount.setCount(accumelate);
                            detialCount.setCost(stockPerfomanceWithProduct.getCost());
                            detialCounts.add(detialCount);
                            accumelate = 0;
                        } else {
                            DetialCount detialCount = new DetialCount();
                            detialCount.setStockId(stockPerfomanceWithProduct.getStockId());
                            detialCount.setCount(stockPerfomanceWithProduct.getAvailable());
                            detialCount.setCost(stockPerfomanceWithProduct.getCost());
                            detialCounts.add(detialCount);
                            stockPerfomanceWithProduct.setAvailable(0);
                            accumelate = available * -1;
                        }
                    }
                    if(accumelate<0.0001){
                        break;
                    }
                }
                if(accumelate>0.001) e.onSuccess(new OutcomeWithDetials(null, null));

                outcomeWithDetials.setDetialCountList(detialCounts);

                e.onSuccess(outcomeWithDetials);
            }


        });
    }

    @Override
    public Single<Outvoice> insertOutvoiceWithBillingAndOutcomeProducts(Outvoice outvoice, List<OutcomeProduct> outcomeProducts, BillingOperations operationDebt) {
        return Single.create(e -> {
            mDaoSession.getOutvoiceDao().insertOrReplace(outvoice);
            operationDebt.setOutvoiceId(outvoice.getId());
            mDaoSession.getBillingOperationsDao().insertOrReplace(operationDebt);
            for (int i = 0; i < outcomeProducts.size(); i++) {
                OutcomeProduct outcomeProduct = outcomeProducts.get(i);
                outcomeProduct.setOutcomeDate(System.currentTimeMillis());
                outcomeProduct.setOutvoiceId(outvoice.getId());
                mDaoSession.getOutcomeProductDao().insertOrReplace(outcomeProduct);
            }
            for (OutcomeProduct outcomeProduct: outcomeProducts) {
                List<StockQueue> stockQueues = mDaoSession.getStockQueueDao().queryBuilder().where(StockQueueDao.Properties.ProductId.eq(outcomeProduct.getProduct().getId()), StockQueueDao.Properties.Available.ge(0.0009)).build().list();
                double count = outcomeProduct.getSumCountValue();
                if (outcomeProduct.getCustomPickSock()){
                    for (int j = 0; j < stockQueues.size(); j++) {
                        if (stockQueues.get(j).getId().equals(outcomeProduct.getStockQueue().getId())) {
                            stockQueues.get(j).setAvailable(stockQueues.get(j).getAvailable() - count);
                            mDaoSession.getStockQueueDao().insertOrReplace(stockQueues.get(j));
                            DetialCount detialCount = new DetialCount();
                            detialCount.setStockId(stockQueues.get(j).getId());
                            detialCount.setStockQueue(stockQueues.get(j));
                            detialCount.setOutcomeProduct(outcomeProduct);
                            detialCount.setOutcomeProductId(outcomeProduct.getId());
                            detialCount.setCost(outcomeProduct.getSumCostValue());
                            detialCount.setCount(count);
                            mDaoSession.getDetialCountDao().insertOrReplace(detialCount);
                         }
                    }
                }
            }
            for (OutcomeProduct outcomeProduct: outcomeProducts) {
                List<StockQueue> stockQueues = mDaoSession.getStockQueueDao().queryBuilder().where(StockQueueDao.Properties.ProductId.eq(outcomeProduct.getProduct().getId()), StockQueueDao.Properties.Available.ge(0.0009)).build().list();
                double count = outcomeProduct.getSumCountValue();
                if(outcomeProduct.getProduct().getStockKeepType()==Product.LIFO)
                    Collections.sort(stockQueues,(stockQueue, t1) -> t1.getId().compareTo(stockQueue.getId()));
                if(outcomeProduct.getProduct().getStockKeepType()==Product.FEFO)
                    Collections.sort(stockQueues,(stockQueue, t1) -> stockQueue.getExpiredProductDate().compareTo(t1.getExpiredProductDate()));
                if (!outcomeProduct.getCustomPickSock()){
                    for (int j = 0; j < stockQueues.size(); j++) {
                        if (count != 0){
                            if (count >= stockQueues.get(j).getAvailable()){
                                count -= stockQueues.get(j).getAvailable();
                                stockQueues.get(j).setAvailable(0);
                                DetialCount detialCount = new DetialCount();
                                detialCount.setStockId(stockQueues.get(j).getId());
                                detialCount.setStockQueue(stockQueues.get(j));
                                detialCount.setOutcomeProduct(outcomeProduct);
                                detialCount.setOutcomeProductId(outcomeProduct.getId());
                                detialCount.setCost(outcomeProduct.getSumCostValue());
                                detialCount.setCount(count);
                                mDaoSession.getDetialCountDao().insertOrReplace(detialCount);
                            } else {
                                DetialCount detialCount = new DetialCount();
                                detialCount.setStockId(stockQueues.get(j).getId());
                                detialCount.setStockQueue(stockQueues.get(j));
                                detialCount.setOutcomeProduct(outcomeProduct);
                                detialCount.setOutcomeProductId(outcomeProduct.getId());
                                detialCount.setCost(outcomeProduct.getSumCostValue());
                                detialCount.setCount(count);
                                mDaoSession.getDetialCountDao().insertOrReplace(detialCount);
                                stockQueues.get(j).setAvailable(stockQueues.get(j).getAvailable() - count);
                                count = 0;
                            }
                        }
                        mDaoSession.getStockQueueDao().insertOrReplace(stockQueues.get(j));
                    }
                }
            }
            e.onSuccess(outvoice);
        });
    }

    @Override
    public Single<Boolean> isOutvoiceNumberExists(String number) {
        return Single.create(e -> {
            e.onSuccess(!mDaoSession
                    .queryBuilder(Outvoice.class)
                    .where(OutvoiceDao.Properties.ConsigmentNumber.eq(number))
                    .list()
                    .isEmpty());
        });
    }

    @Override
    public Single<List<Vendor>> getVendorsByProductId(Long productId) {
        return Single.create(e -> {
            String stockQuery = "SELECT * FROM STOCK_QUEUE WHERE PRODUCT_ID == " + productId + " AND VENDOR_ID != 0 GROUP BY VENDOR_ID";
            Cursor stockCursor = mDaoSession.getDatabase().rawQuery(stockQuery, null);
            List<Vendor> vendors = new ArrayList<>();
            if (stockCursor.getCount() > 0) {
                stockCursor.moveToFirst();
                while (!stockCursor.isAfterLast()){
                    vendors.add(mDaoSession.getVendorDao().load(stockCursor.getLong(stockCursor.getColumnIndex("VENDOR_ID"))));
                    stockCursor.moveToNext();
                }
            }
            e.onSuccess(vendors);
        });
    }

    @Override
    public Single<List<ProductState>> getVendorProductsWithStates(Long vendorId) {
        return Single.create(e -> {
            String inventoryQuery = "SELECT PRODUCT_ID, SUM(AVAILABLE) FROM STOCK_QUEUE WHERE VENDOR_ID = "+vendorId +" GROUP BY PRODUCT_ID" ;
            Cursor cursorStockQueue = mDaoSession.getDatabase().rawQuery(inventoryQuery, null);
            cursorStockQueue.moveToFirst();
            List<ProductState> productStates = new ArrayList<>();
            if (cursorStockQueue.getCount() > 0) {
                cursorStockQueue.moveToFirst();
                while (!cursorStockQueue.isAfterLast()) {
                    ProductState productState = new ProductState();
                    productState.setProduct(mDaoSession.getProductDao().load(cursorStockQueue.getLong(0)));
                    productState.setValue(cursorStockQueue.getDouble(1));
                    productStates.add(productState);
                    cursorStockQueue.moveToNext();
                }
            }
            e.onSuccess(productStates);
            });
    }

    @Override
    public Single<List<InvoiceListItem>> getInvoiceListItemByVendorId(Long vendorId) {
        return Single.create(e -> {
            List<Invoice> invoiceList = mDaoSession.getInvoiceDao().queryBuilder().where(InvoiceDao.Properties.VendorId.eq(vendorId)).build().list();
            List<Outvoice> outvoiceList = mDaoSession.getOutvoiceDao().queryBuilder().where(OutvoiceDao.Properties.VendorId.eq(vendorId)).build().list();
            List<InvoiceListItem> invoiceListItems = new ArrayList<>();
            for(Invoice invoice: invoiceList){
                InvoiceListItem item = new InvoiceListItem();
                item.setInvoice(invoice);
                item.setCreatedDate(invoice.getCreatedDate());
                item.setNumber(invoice.getConsigmentNumber());
                item.setTotalAmount(invoice.getTotalAmount());
                item.setType(BundleConstants.INVOICE);
                invoiceListItems.add(item);
            }
            for(Outvoice outvoice: outvoiceList){
                InvoiceListItem item = new InvoiceListItem();
                item.setOutvoice(outvoice);
                item.setCreatedDate(outvoice.getCreatedDate());
                item.setNumber(outvoice.getConsigmentNumber());
                item.setTotalAmount(outvoice.getTotalAmount());
                item.setType(BundleConstants.OUTVOICE);
                invoiceListItems.add(item);
            }
            Collections.sort(invoiceListItems, (o1, o2) -> o1.getCreatedDate().compareTo(o2.getCreatedDate()));
            e.onSuccess(invoiceListItems);
        });
    }

    @Override
    public Single<List<InvoiceListItem>> getInvoiceListItemsInIntervalByVendor(Long vendorId, Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            List<Invoice> invoiceList = mDaoSession.getInvoiceDao().queryBuilder()
                    .where(InvoiceDao.Properties.VendorId.eq(vendorId), InvoiceDao.Properties.CreatedDate.ge(fromDate.getTimeInMillis()),
                           InvoiceDao.Properties.CreatedDate.le(toDate.getTimeInMillis()))
                    .build()
                    .list();
            List<Outvoice> outvoiceList = mDaoSession.getOutvoiceDao().queryBuilder()
                    .where(OutvoiceDao.Properties.VendorId.eq(vendorId), OutvoiceDao.Properties.CreatedDate.ge(fromDate.getTimeInMillis()),
                           OutvoiceDao.Properties.CreatedDate.le(toDate.getTimeInMillis()))
                    .build()
                    .list();
            List<InvoiceListItem> invoiceListItems = new ArrayList<>();
            for(Invoice invoice: invoiceList){
                InvoiceListItem item = new InvoiceListItem();
                item.setInvoice(invoice);
                item.setCreatedDate(invoice.getCreatedDate());
                item.setNumber(invoice.getConsigmentNumber());
                item.setTotalAmount(invoice.getTotalAmount());
                item.setType(BundleConstants.INVOICE);
                invoiceListItems.add(item);
            }
            for(Outvoice outvoice: outvoiceList){
                InvoiceListItem item = new InvoiceListItem();
                item.setOutvoice(outvoice);
                item.setCreatedDate(outvoice.getCreatedDate());
                item.setNumber(outvoice.getConsigmentNumber());
                item.setTotalAmount(outvoice.getTotalAmount());
                item.setType(BundleConstants.OUTVOICE);
                invoiceListItems.add(item);
            }
            Collections.sort(invoiceListItems, (o1, o2) -> o1.getCreatedDate().compareTo(o2.getCreatedDate()));
            e.onSuccess(invoiceListItems);
        });
    }

    @Override
    public Single<List<StockQueue>> getAllStockQueuesByProductId(Long productId) {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getStockQueueDao().queryBuilder().where(StockQueueDao.Properties.ProductId.eq(productId)).build().list());
        });
    }

    @Override
    public Single<List<StockQueue>> getAllStockQueuesByVendorId(Long vendorId) {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getStockQueueDao().queryBuilder().where(StockQueueDao.Properties.VendorId.eq(vendorId)).build().list());
        });
    }

    @Override
    public Single<List<StockQueue>> getAllStockQueuesByProductIdInInterval(Long productId, Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getStockQueueDao().queryBuilder().where(StockQueueDao.Properties.ProductId.eq(productId),
                    StockQueueDao.Properties.IncomeProductDate.ge(fromDate.getTimeInMillis()),
                    StockQueueDao.Properties.IncomeProductDate.le(toDate.getTimeInMillis())).build().list());
        });
    }

    @Override
    public Single<List<StockQueue>> getAllStockQueuesByVendorIdInInterval(Long vendorId, Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getStockQueueDao().queryBuilder().where(StockQueueDao.Properties.VendorId.eq(vendorId),
                    StockQueueDao.Properties.IncomeProductDate.ge(fromDate.getTimeInMillis()),
                    StockQueueDao.Properties.IncomeProductDate.le(toDate.getTimeInMillis())).build().list());
        });
    }

    @Override
    public Single<List<StockQueue>> getExpiredStockQueue() {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getStockQueueDao().queryBuilder().where(StockQueueDao.Properties.ExpiredProductDate.gt(0), StockQueueDao.Properties.Available.ge(0.0009)).build().list());
        });
    }

    @Override
    public List<OperationSummaryItem> getIncomeProductOperationsSummary(Date fromDate, Date toDate,List<OperationSummaryItem> operationSummaryItems) {
        String summary = "SELECT  PRODUCT_ID, INCOME_TYPE, SUM(COUNT_VALUE) FROM INCOMEPRODUCT WHERE INCOME_DATE BETWEEN " + fromDate.getTime() + " AND " + toDate.getTime()+ " GROUP BY INCOME_TYPE, PRODUCT_ID";
        Cursor cursorSummary = mDaoSession.getDatabase().rawQuery(summary, null);
        cursorSummary.moveToFirst();
        if (cursorSummary.getCount() > 0) {
            cursorSummary.moveToFirst();
            mark: while (!cursorSummary.isAfterLast()) {
                for (OperationSummaryItem operationSummaryItem:operationSummaryItems) {
                    if(operationSummaryItem.getProduct().getId() == cursorSummary.getLong(0)){
                        if(cursorSummary.getLong(1) == IncomeProduct.INVOICE_PRODUCT){
                            operationSummaryItem.setReciveFromVendor(cursorSummary.getDouble(2));
                        }else if(cursorSummary.getLong(1) == IncomeProduct.RETURNED_PRODUCT){
                            operationSummaryItem.setReturnFromCustomer(cursorSummary.getDouble(2));
                        }else {
                            operationSummaryItem.setSurplus(cursorSummary.getDouble(2));
                        }
                        cursorSummary.moveToNext();
                        continue mark;
                    }
                }
                OperationSummaryItem operationSummaryItem = new OperationSummaryItem();
                operationSummaryItem.setProduct(mDaoSession.getProductDao().load(cursorSummary.getLong(0)));
                if(cursorSummary.getLong(1) == IncomeProduct.INVOICE_PRODUCT){
                    operationSummaryItem.setReciveFromVendor(cursorSummary.getDouble(2));
                }else if(cursorSummary.getLong(1) == IncomeProduct.RETURNED_PRODUCT){
                    operationSummaryItem.setReturnFromCustomer(cursorSummary.getDouble(2));
                }else {
                    operationSummaryItem.setSurplus(cursorSummary.getDouble(2));
                }
                operationSummaryItems.add(operationSummaryItem);
                cursorSummary.moveToNext();
            }
        }
        return operationSummaryItems;
    }

    @Override
    public List<OperationSummaryItem> getOutcomeProductOperationsSummary(Date fromDate, Date toDate, List<OperationSummaryItem> operationSummaryItems) {
        String summary = "SELECT  PRODUCT_ID, OUTCOME_TYPE, SUM(SUM_COUNT_VALUE) FROM OUT_PRODUCT WHERE  OUTCOME_DATE BETWEEN " + fromDate.getTime() + " AND " + toDate.getTime()+ " GROUP BY OUTCOME_TYPE, PRODUCT_ID";
        Cursor cursorSummary = mDaoSession.getDatabase().rawQuery(summary, null);
        cursorSummary.moveToFirst();
        if (cursorSummary.getCount() > 0) {
            cursorSummary.moveToFirst();
            mark: while (!cursorSummary.isAfterLast()) {
                for (OperationSummaryItem operationSummaryItem:operationSummaryItems) {
                    if(operationSummaryItem.getProduct().getId() == cursorSummary.getLong(0)){
                        if(cursorSummary.getLong(1) == OutcomeProduct.ORDER_SALES){
                            operationSummaryItem.setSales(cursorSummary.getDouble(2));
                        }else if(cursorSummary.getLong(1) == OutcomeProduct.OUTVOICE_TO_VENDOR){
                            operationSummaryItem.setReturnToVendor(cursorSummary.getDouble(2));
                        }else {
                            operationSummaryItem.setWaste(cursorSummary.getDouble(2));
                        }
                        cursorSummary.moveToNext();
                        continue mark;
                    }
                }
                OperationSummaryItem operationSummaryItem = new OperationSummaryItem();
                operationSummaryItem.setProduct(mDaoSession.getProductDao().load(cursorSummary.getLong(0)));
                if(cursorSummary.getLong(1) == OutcomeProduct.ORDER_SALES){
                    operationSummaryItem.setSales(cursorSummary.getDouble(2));
                }else if(cursorSummary.getLong(1) == OutcomeProduct.OUTVOICE_TO_VENDOR){
                    operationSummaryItem.setReturnToVendor(cursorSummary.getDouble(2));
                }else {
                    operationSummaryItem.setWaste(cursorSummary.getDouble(2));
                }
                operationSummaryItems.add(operationSummaryItem);
                cursorSummary.moveToNext();
            }
        }
        return operationSummaryItems;
    }

    @Override
    public Single<List<OutcomeProduct>> getOutcomeProductsForPeriod(Calendar fromDate, Calendar toDate) {
        return Single.create(e->{
            e.onSuccess(mDaoSession.getOutcomeProductDao().queryBuilder().where(OutcomeProductDao.Properties.OutcomeDate.ge(fromDate.getTimeInMillis()),
                    OutcomeProductDao.Properties.OutcomeDate.le(toDate.getTimeInMillis())).build().list());
        });
    }

    @Override
    public Single<List<OutcomeProduct>> updateOutcomeProduct(List<OutcomeProduct> outcomeProducts) {
        return Single.create(e -> {
            mDaoSession.getOutcomeProductDao().insertOrReplaceInTx(outcomeProducts);
            e.onSuccess(outcomeProducts);
        });
    }

    @Override
    public Single<List<IncomeProduct>> getIncomeProductsForPeriod(Calendar fromDate, Calendar toDate) {
        return Single.create(e->{
            e.onSuccess(mDaoSession.getIncomeProductDao().queryBuilder().where(IncomeProductDao.Properties.IncomeDate.ge(fromDate.getTimeInMillis()),
                    IncomeProductDao.Properties.IncomeDate.le(toDate.getTimeInMillis())).build().list());
        });
    }

    @Override
    public Single<List<StockQueue>> getStockQueueForPeriod(Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getStockQueueDao().queryBuilder().where(StockQueueDao.Properties.IncomeProductDate.ge(fromDate.getTimeInMillis()),
                    StockQueueDao.Properties.IncomeProductDate.le(toDate.getTimeInMillis())).build().list());
        });
    }

    @Override
    public Single<List<StockQueue>> getStockQueueUsedForPeriod(Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getStockQueueDao().queryBuilder().where(StockQueueDao.Properties.IncomeProductDate.ge(fromDate.getTimeInMillis()),
                    StockQueueDao.Properties.IncomeProductDate.le(toDate.getTimeInMillis()),StockQueueDao.Properties.Available.notEq(StockQueueDao.Properties.IncomeCount)).build().list());
        });
    }

    @Override
    public Single<List<InvoiceListItem>> getInvoiceListItemsInInterval(Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            List<Invoice> invoiceList = mDaoSession.getInvoiceDao().queryBuilder()
                    .where(InvoiceDao.Properties.CreatedDate.ge(fromDate.getTimeInMillis()),
                            InvoiceDao.Properties.CreatedDate.le(toDate.getTimeInMillis()))
                    .build()
                    .list();
            List<Outvoice> outvoiceList = mDaoSession.getOutvoiceDao().queryBuilder()
                    .where(OutvoiceDao.Properties.CreatedDate.ge(fromDate.getTimeInMillis()),
                            OutvoiceDao.Properties.CreatedDate.le(toDate.getTimeInMillis()))
                    .build()
                    .list();
            List<InvoiceListItem> invoiceListItems = new ArrayList<>();
            for(Invoice invoice: invoiceList){
                InvoiceListItem item = new InvoiceListItem();
                item.setInvoice(invoice);
                item.setCreatedDate(invoice.getCreatedDate());
                item.setNumber(invoice.getConsigmentNumber());
                item.setTotalAmount(invoice.getTotalAmount());
                item.setType(BundleConstants.INVOICE);
                invoiceListItems.add(item);
            }
            for(Outvoice outvoice: outvoiceList){
                InvoiceListItem item = new InvoiceListItem();
                item.setOutvoice(outvoice);
                item.setCreatedDate(outvoice.getCreatedDate());
                item.setNumber(outvoice.getConsigmentNumber());
                item.setTotalAmount(outvoice.getTotalAmount());
                item.setType(BundleConstants.OUTVOICE);
                invoiceListItems.add(item);
            }
            Collections.sort(invoiceListItems, (o1, o2) -> o1.getCreatedDate().compareTo(o2.getCreatedDate()));
            e.onSuccess(invoiceListItems);
        });
    }

    @Override
    public Single<List<InvoiceProduct>> getInvoiceProductsInInterval(Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            List<IncomeProduct> incomeProducts = mDaoSession.getIncomeProductDao().queryBuilder()
                    .where(IncomeProductDao.Properties.InvoiceId.isNotNull(), IncomeProductDao.Properties.IncomeDate.ge(fromDate.getTimeInMillis()),
                            IncomeProductDao.Properties.IncomeDate.le(toDate.getTimeInMillis()))
                    .build()
                    .list();
            List<OutcomeProduct> outcomeProducts = mDaoSession.getOutcomeProductDao().queryBuilder()
                    .where(OutcomeProductDao.Properties.OutvoiceId.isNotNull(), OutcomeProductDao.Properties.OutcomeDate.ge(fromDate.getTimeInMillis()),
                            OutcomeProductDao.Properties.OutcomeDate.le(toDate.getTimeInMillis()))
                    .build()
                    .list();
            List<InvoiceProduct> invoiceProducts = new ArrayList<>();
            for (IncomeProduct incomeProduct: incomeProducts){
                InvoiceProduct product = new InvoiceProduct();
                product.setId(incomeProduct.getInvoiceId());
                product.setCost(incomeProduct.getCostValue());
                product.setCountValue(incomeProduct.getCountValue() + " " + incomeProduct.getProduct().getMainUnit().getAbbr());
                product.setCreatedDate(incomeProduct.getIncomeDate());
                product.setName(incomeProduct.getProduct().getName());
                product.setType(BundleConstants.INVOICE);
                product.setTotal(incomeProduct.getCostValue() * incomeProduct.getCountValue());
                product.setVendorName(incomeProduct.getVendor().getName());
                invoiceProducts.add(product);
            }
            for (OutcomeProduct outcomeProduct: outcomeProducts){
                InvoiceProduct product = new InvoiceProduct();
                product.setId(outcomeProduct.getOutvoiceId());
                product.setCost(outcomeProduct.getSumCostValue());
                product.setCountValue(outcomeProduct.getSumCountValue() + " " + outcomeProduct.getProduct().getMainUnit().getAbbr());
                product.setCreatedDate(outcomeProduct.getOutcomeDate());
                product.setName(outcomeProduct.getProduct().getName());
                product.setType(BundleConstants.OUTVOICE);
                product.setTotal(outcomeProduct.getSumCostValue() * outcomeProduct.getSumCountValue());
                product.setVendorName(outcomeProduct.getOutvoice().getVendor().getName());
                invoiceProducts.add(product);
            }
            Collections.sort(invoiceProducts, (o1, o2) -> o1.getCreatedDate().compareTo(o2.getCreatedDate()));
            e.onSuccess(invoiceProducts);
        });
    }

    @Override
    public Single<Outvoice> getOutvoice(Long id) {
        return Single.create(e -> e.onSuccess(mDaoSession.getOutvoiceDao().load(id)));
    }

    @Override
    public Single<Invoice> getInvoiceById(Long id) {
        return Single.create(e -> {e.onSuccess(mDaoSession.getInvoiceDao().load(id));});
    }

    @Override
    public Single<List<Vendor>> getActiveVendors() {
        return Single.create(e -> e.onSuccess(mDaoSession.getVendorDao().queryBuilder().where(VendorDao.Properties.Active.eq(true), VendorDao.Properties.Deleted.eq(false)).build().list()));
    }

    @Override
    public Single<StockQueue> getStockQueueById(Long stockQueueId) {
        return Single.create(e -> e.onSuccess(mDaoSession.getStockQueueDao().load(stockQueueId)));
    }

    @Override
    public Single<List<InventoryItemReport>> getInventoryWithSummaryCost() {
        return Single.create(e -> {
            List<Product> products = mDaoSession.getProductDao().loadAll();
            String inventoryQuery = "SELECT PRODUCT_ID, SUM(AVAILABLE), SUM(AVAILABLE*COST) FROM STOCK_QUEUE GROUP BY PRODUCT_ID";
            Cursor cursorInventory = mDaoSession.getDatabase().rawQuery(inventoryQuery, null);
            cursorInventory.moveToFirst();
            List<InventoryItemReport> inventoryItems = new ArrayList<>();
            if (cursorInventory.getCount() > 0) {
                cursorInventory.moveToFirst();
                while (!cursorInventory.isAfterLast()) {
                    InventoryItemReport inventoryItemReport = new InventoryItemReport();
                    inventoryItemReport.setProduct(mDaoSession.getProductDao().load(cursorInventory.getLong(0)));
                    inventoryItemReport.setAvailable(cursorInventory.getDouble(1));
                    inventoryItemReport.setSumCost(cursorInventory.getDouble(2));
                    inventoryItems.add(inventoryItemReport);
                    cursorInventory.moveToNext();
                }
            }
            mark: for (Product product : products) {
                for (InventoryItemReport inventoryItemReport : inventoryItems) {
                    if(product.getId() == inventoryItemReport.getProduct().getId())
                        continue mark;
                }
                InventoryItemReport inventoryItemReport = new InventoryItemReport();
                inventoryItemReport.setProduct(product);
                inventoryItemReport.setAvailable(0);
                inventoryItemReport.setSumCost(0);
                inventoryItems.add(inventoryItemReport);
            }
            e.onSuccess(inventoryItems);
        });
    }

    @Override
    public Single<List<InventoryItemReport>> getInventoryVendorWithSummaryCost() {
        return Single.create(e -> {
            List<Product> products = mDaoSession.getProductDao().loadAll();
            String inventoryQuery = "SELECT PRODUCT_ID, VENDOR_ID, SUM(AVAILABLE), SUM(AVAILABLE*COST) FROM STOCK_QUEUE GROUP BY PRODUCT_ID, VENDOR_ID";
            Cursor cursorInventory = mDaoSession.getDatabase().rawQuery(inventoryQuery, null);
            cursorInventory.moveToFirst();
            List<InventoryItemReport> inventoryItems = new ArrayList<>();
            if (cursorInventory.getCount() > 0) {
                cursorInventory.moveToFirst();
                while (!cursorInventory.isAfterLast()) {
                    InventoryItemReport inventoryItemReport = new InventoryItemReport();
                    inventoryItemReport.setProduct(mDaoSession.getProductDao().load(cursorInventory.getLong(0)));
                    inventoryItemReport.setVendor(mDaoSession.getVendorDao().load(cursorInventory.getLong(1)));
                    inventoryItemReport.setAvailable(cursorInventory.getDouble(2));
                    inventoryItemReport.setSumCost(cursorInventory.getDouble(3));
                    inventoryItems.add(inventoryItemReport);
                    cursorInventory.moveToNext();
                }
            }
            e.onSuccess(inventoryItems);
        });
    }

    @Override
    public Single<List<Contact>> getContactsByVendorId(Long id) {
        return Single.create(e -> {
            e.onSuccess(mDaoSession.getContactDao().queryBuilder().where(ContactDao.Properties.VendorId.eq(id)).build().list());
        });
    }


}
