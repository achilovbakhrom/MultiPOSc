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
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.PaymentTypeDao;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.ProductClassDao;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.ServiceFeeDao;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentDao;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.consignment.ConsignmentProductDao;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.currency.CurrencyDao;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerDao;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.customer.CustomerGroupDao;
import com.jim.multipos.data.db.model.customer.JoinCustomerGroupsWithCustomers;
import com.jim.multipos.data.db.model.customer.JoinCustomerGroupsWithCustomersDao;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.inventory.BillingOperationsDao;
import com.jim.multipos.data.db.model.inventory.InventoryState;
import com.jim.multipos.data.db.model.inventory.InventoryStateDao;
import com.jim.multipos.data.db.model.inventory.WarehouseOperations;
import com.jim.multipos.data.db.model.inventory.WarehouseOperationsDao;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.CategoryDao;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.ProductDao;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorDao;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.data.db.model.products.VendorProductConDao;
import com.jim.multipos.data.db.model.stock.Stock;
import com.jim.multipos.data.db.model.unit.SubUnitsList;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.data.db.model.unit.UnitDao;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.ui.vendor_item_managment.model.VendorWithDebt;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;

import static com.jim.multipos.data.db.model.products.Category.WITHOUT_PARENT;
import static com.jim.multipos.ui.service_fee_new.Constants.APP_TYPE_ALL;
import static com.jim.multipos.ui.service_fee_new.Constants.APP_TYPE_ITEM;


/**
 * Created by janisharali on 08/12/16.
 */

@Singleton
public class AppDbHelper implements DbHelper {
    private final DaoSession mDaoSession;

    @Inject
    public AppDbHelper(DbOpenHelper dbOpenHelper) {
        mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }

    /*@Override
    public Observable<Long> insertJoinCustomerGroupWithCustomer(JoinCustomerGroupsWithCustomers joinCustomerGroupWithCustomer) {
        return Observable.fromCallable(() -> mDaoSession.getJoinCustomerGroupsWithCustomersDao().insertOrReplace(joinCustomerGroupWithCustomer));
    }

    @Override
    public Observable<Boolean> insertJoinCustomerGroupWithCustomers(List<JoinCustomerGroupsWithCustomers> joinCustomerGroupsWithCustomers) {
        return Observable.fromCallable(() -> {
            mDaoSession.getJoinCustomerGroupsWithCustomersDao().insertOrReplaceInTx(joinCustomerGroupsWithCustomers);
            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteJoinCustomerGroupWithCustomer(Long customerGroupId, Long customerId) {
        return Observable.fromCallable(() -> {
            mDaoSession.getDatabase().execSQL("DELETE FROM JOIN_CUSTOMER_GROUPS_WITH_CUSTOMERS WHERE CUSTOMER_ID=? AND CUSTOMER_GROUP_ID=?", new String[]{String.valueOf(customerId), String.valueOf(customerGroupId)});

            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteJoinCustomerGroupWithCustomer(Long customerId) {
        return Observable.fromCallable(() -> {
            mDaoSession.getDatabase().execSQL("DELETE FROM JOIN_CUSTOMER_GROUPS_WITH_CUSTOMERS WHERE CUSTOMER_ID=?", new String[]{String.valueOf(customerId)});

            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteAllJoinCustomerGroupWithCustomer() {
        return Observable.fromCallable(() -> {
            mDaoSession.getJoinCustomerGroupsWithCustomersDao().deleteAll();

            return true;
        });
    }

    @Override
    public Observable<List<JoinCustomerGroupsWithCustomers>> getAllJoinCustomerGroupsWithCustomers() {
        return Observable.fromCallable(() -> mDaoSession.getJoinCustomerGroupsWithCustomersDao().loadAll());
    }*/

    @Override
    public Observable<Long> insertCustomerToCustomerGroup(Long customerGroupId, Long customerId) {
        JoinCustomerGroupsWithCustomers entity = new JoinCustomerGroupsWithCustomers(customerId, customerGroupId);

        return Observable.fromCallable(() -> mDaoSession.getJoinCustomerGroupsWithCustomersDao().insertOrReplace(entity));
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
            mDaoSession.getDatabase().execSQL("DELETE FROM JOIN_CUSTOMER_GROUPS_WITH_CUSTOMERS WHERE CUSTOMER_ID=?", new String[]{String.valueOf(customerId)});

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
                                CategoryDao.Properties.IsDeleted.eq(false),
                                CategoryDao.Properties.IsNotModified.eq(true))
                        .build().list();
                category.setPosition((double) (categories.size() + 1));
            }
            Long result = mDaoSession.getCategoryDao().insertOrReplace(category);
            mDaoSession.getCategoryDao().detachAll();
            return result;
        });
    }

    @Override
    public Observable<Long> insertSubCategory(Category subcategory) {
        return Observable.fromCallable(() -> {
            mDaoSession.clear();
            List<Category> categories = mDaoSession.getCategoryDao().queryBuilder()
                    .where(CategoryDao.Properties.ParentId.eq(subcategory.getParentId()),
                            CategoryDao.Properties.IsDeleted.eq(false),
                            CategoryDao.Properties.IsNotModified.eq(true))
                    .build().list();
            if (categories.isEmpty()) {
                subcategory.setPosition(1d);
            } else {
                subcategory.setPosition((double) (categories.size() + 1));
            }
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
                .where(CategoryDao.Properties.ParentId.eq(WITHOUT_PARENT), CategoryDao.Properties.IsDeleted.eq(false), CategoryDao.Properties.IsNotModified.eq(true))
                .orderAsc(CategoryDao.Properties.Position)
                .build().list());
    }

    @Override
    public Observable<Integer> getAllProductsCount(Category category) {
        return Observable.fromCallable(() -> {
            List<Category> subCategories = mDaoSession.getCategoryDao().queryBuilder()
                    .where(CategoryDao.Properties.ParentId.eq(category.getId()), CategoryDao.Properties.IsDeleted.eq(false))
                    .where(CategoryDao.Properties.IsNotModified.eq(true), CategoryDao.Properties.IsActive.eq(true))
                    .orderAsc(CategoryDao.Properties.Position)
                    .build().list();
            int sum = 0;
            for (Category subcategory : subCategories) {
                sum += mDaoSession.getProductDao().queryBuilder()
                        .where(ProductDao.Properties.ParentId.eq(subcategory.getId()), ProductDao.Properties.IsDeleted.eq(false))
                        .where(ProductDao.Properties.IsNotModified.eq(true), ProductDao.Properties.IsActive.eq(true))
                        .build().list().size();
            }
            return sum;
        });
    }

    @Override
    public Single<List<Category>> getAllActiveCategories() {
        Observable<List<Category>> objectObservable = Observable.create(singleSubscriber -> {
            try {
                List<Category> categories = mDaoSession.getCategoryDao().loadAll();
                singleSubscriber.onNext(categories);
                singleSubscriber.onComplete();
            } catch (Exception o) {
                singleSubscriber.onError(o);
            }
        });
        return objectObservable.flatMap(Observable::fromIterable)
                .filter(Category::isNotModifyted)
                .filter(category -> !category.isDeleted())
                .filter(Category::getIsActive)
                .filter(category -> category.getParentId().equals(WITHOUT_PARENT))
                .sorted((category, t1) -> category.getPosition().compareTo(t1.getPosition()))
                .toList();
    }

    @Override
    public Single<List<Category>> getAllActiveSubCategories(Category parent) {
        Observable<List<Category>> objectObservable = Observable.create(singleSubscriber -> {
            try {
                List<Category> categories = mDaoSession.getCategoryDao().loadAll();
                singleSubscriber.onNext(categories);
                singleSubscriber.onComplete();
            } catch (Exception o) {
                singleSubscriber.onError(o);
            }
        });
        return objectObservable.flatMap(Observable::fromIterable)
                .filter(Category::isNotModifyted)
                .filter(category -> !category.isDeleted())
                .filter(Category::getIsActive)
                .filter(category -> category.getParentId().equals(parent.getId()))
                .sorted((category, t1) -> category.getPosition().compareTo(t1.getPosition()))
                .toList();
    }

    @Override
    public Observable<List<Category>> getSubCategories(Category category) {
        return Observable.fromCallable(() -> mDaoSession.getCategoryDao().queryBuilder()
                .where(CategoryDao.Properties.ParentId.eq(category.getId()), CategoryDao.Properties.IsDeleted.eq(false))
                .orderAsc(CategoryDao.Properties.Position)
                .build().list());
    }

    @Override
    public Observable<Long> insertOrReplaceCategory(Category category) {
        return Observable.fromCallable(() -> mDaoSession.getCategoryDao().insertOrReplace(category));
    }

    @Override
    public Observable<Long> insertProduct(Product product) {
        return Observable.fromCallable(() -> {
            if (product.getId() == null) {
                List<Product> products = mDaoSession.getProductDao().queryBuilder()
                        .where(ProductDao.Properties.IsDeleted.eq(false),
                                ProductDao.Properties.IsNotModified.eq(true))
                        .list();
                product.setPosition((double) (products.size() + 1));
            }
            return mDaoSession.getProductDao().insertOrReplace(product);
        });
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
        return Observable.fromCallable(() -> mDaoSession.getProductDao().loadAll());
    }

    @Override
    public Single<List<Product>> getAllActiveProducts(Category parent) {
        Observable<List<Product>> objectObservable = Observable.create(singleSubscriber -> {
            try {
                List<Product> products = mDaoSession.getProductDao().loadAll();
                singleSubscriber.onNext(products);
                singleSubscriber.onComplete();
            } catch (Exception o) {
                singleSubscriber.onError(o);
            }
        });
        return objectObservable.flatMap(Observable::fromIterable)
                .filter(Product::isNotModifyted)
                .filter(product -> !product.isDeleted())
                .filter(Product::getIsActive)
                .filter(product -> product.getCategoryId().equals(parent.getId()))
                .sorted((product, t1) -> t1.getCreatedDate().compareTo(product.getCreatedDate()))
                .sorted((product, t1) -> product.getPosition().compareTo(t1.getPosition()))
                .toList();
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
        return Observable.fromCallable(() -> {
                    List<Account> accounts = mDaoSession.getAccountDao().loadAll();
                    Collections.reverse(accounts);

                    return accounts;
                }
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
        return mDaoSession.getPaymentTypeDao().loadAll();
    }

    @Override
    public Single<List<Discount>> getAllDiscounts() {
        Observable<List<Discount>> objectObservable = Observable.create(singleSubscriber -> {
            try {
                List<Discount> discounts = mDaoSession.getDiscountDao().queryBuilder().where(DiscountDao.Properties.Delete.eq(false)).list();
                singleSubscriber.onNext(discounts);
                singleSubscriber.onComplete();
            } catch (Exception o) {
                singleSubscriber.onError(o);
            }
        });
        return objectObservable.flatMap(Observable::fromIterable)
                .filter(discounts -> discounts.isNotModifyted())
                .filter(discounts -> !discounts.isDeleted())
                .sorted((discounts, t1) -> t1.getCreatedDate().compareTo(discounts.getCreatedDate()))
                .toList();
    }

    @Override
    public Single<Long> insertDiscount(Discount discount) {
        return Single.create(singleSubscriber -> {
            try {
                long result = mDaoSession.getDiscountDao().insertOrReplace(discount);
                singleSubscriber.onSuccess(result);
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
        return Observable.fromCallable(() -> {
            List<PaymentType> paymentTypes = mDaoSession.getPaymentTypeDao().loadAll();
            Collections.reverse(paymentTypes);

            return paymentTypes;
        });
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
        Observable<List<ProductClass>> objectObservable = Observable.create(singleSubscriber -> {
            try {
                List<ProductClass> productClasses = mDaoSession.getProductClassDao().loadAll();
                singleSubscriber.onNext(productClasses);
                singleSubscriber.onComplete();
            } catch (Exception o) {
                singleSubscriber.onError(o);
            }
        });
        return objectObservable.flatMap(Observable::fromIterable)
                .filter(productClass -> productClass.isNotModifyted())
                .filter(productClass -> !productClass.isDeleted())
                .sorted((productClass, t1) -> t1.getCreatedDate().compareTo(productClass.getCreatedDate()))
                .sorted((productClass, t1) -> t1.getActive().compareTo(productClass.getActive()))
                .toList();
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
    public Observable<Integer> getCategoryByName(Category category) {
        return Observable.fromCallable(() -> {
            Query<Category> categoryQuery = mDaoSession.getCategoryDao().queryBuilder()
                    .where(CategoryDao.Properties.Name.eq(category.getName()), CategoryDao.Properties.IsDeleted.eq(false), CategoryDao.Properties.ParentId.eq(WITHOUT_PARENT))
                    .build();
            if (categoryQuery.list().isEmpty()) {
                return 0;
            } else if (categoryQuery.list().get(0).getId().equals(category.getId())) {
                return 1;
            } else return 2;
        });
    }

    @Override
    public Observable<Boolean> isCategoryNameExists(String name) {
        return Observable.fromCallable(() -> !mDaoSession.getCategoryDao().queryBuilder()
                .where(
                        CategoryDao.Properties.ParentId.eq(Category.WITHOUT_PARENT),
                        CategoryDao.Properties.Name.eq(name),
                        CategoryDao.Properties.IsDeleted.eq(false),
                        CategoryDao.Properties.ParentId.eq(WITHOUT_PARENT)
                )
                .build()
                .list()
                .isEmpty());
    }

    @Override
    public Observable<Integer> getSubCategoryByName(Category category) {
        return Observable.fromCallable(() -> {
            Query<Category> subCategoryQuery = mDaoSession.getCategoryDao().queryBuilder()
                    .where(CategoryDao.Properties.Name.eq(category.getName()), CategoryDao.Properties.IsDeleted.eq(false), CategoryDao.Properties.ParentId.eq(category.getParentId()))
                    .build();
            if (subCategoryQuery.list().isEmpty()) {
                return 0;
            } else if (subCategoryQuery.list().get(0).getId().equals(category.getId())) {
                return 1;
            } else return 2;
        });
    }

    @Override
    public Observable<Boolean> isSubCategoryNameExists(String parentName, String name) {
        return Observable.fromCallable(() -> {
            List<Category> categories = mDaoSession.getCategoryDao().queryBuilder()
                    .where(CategoryDao.Properties.Name.eq(parentName), CategoryDao.Properties.ParentId.eq(Category.WITHOUT_PARENT),
                            CategoryDao.Properties.IsDeleted.eq(false))
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
                    .where(CategoryDao.Properties.ParentId.eq(parentId), CategoryDao.Properties.Name.eq(name))
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
    public Observable<Long> insertServiceFee(ServiceFee serviceFee) {
        return Observable.fromCallable(() -> mDaoSession.getServiceFeeDao().insertOrReplace(serviceFee));
    }

    @Override
    public Observable<List<ServiceFee>> getAllServiceFees() {
        return Observable.fromCallable(() -> mDaoSession.getServiceFeeDao().queryBuilder().where(ServiceFeeDao.Properties.IsDeleted.eq(false)).orderDesc(ServiceFeeDao.Properties.CreatedDate).build().list());
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
        return Observable.fromCallable(() -> mDaoSession.getServiceFeeDao().queryBuilder().whereOr(ServiceFeeDao.Properties.ApplyingType.eq(APP_TYPE_ITEM), ServiceFeeDao.Properties.ApplyingType.eq(APP_TYPE_ALL)).orderDesc(ServiceFeeDao.Properties.CreatedDate).build().list());
    }

    @Override
    public Observable<Boolean> isCustomerExists(String name) {
        return Observable.fromCallable(() -> !mDaoSession.getCustomerDao().queryBuilder().where(CustomerDao.Properties.Name.eq(name)).build().list().isEmpty());
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
        return Observable.fromCallable(() -> mDaoSession.getCustomerDao().queryBuilder().where(CustomerDao.Properties.IsNotModifyted.notEq(false)).orderDesc(CustomerDao.Properties.CreatedDate).build().list());
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
        return Observable.fromCallable(() -> !mDaoSession.getCustomerGroupDao().queryBuilder().where(CustomerGroupDao.Properties.Name.eq(name)).build().list().isEmpty());
    }

    @Override
    public List<Account> getAccounts() {
        return mDaoSession.getAccountDao().loadAll();
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
        return Observable.fromCallable(() -> !mDaoSession.getVendorDao().queryBuilder().where(VendorDao.Properties.Name.eq(name)).list().isEmpty());
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
            if (vendor == null || vendor.getIsActive()) return false;
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
        return Observable.fromCallable(() -> mDaoSession.getVendorDao().loadAll());
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
        if (mDaoSession.getProductDao().loadAll().size() == 0) {
            List<Product> products = new ArrayList<>();

            Product product = new Product();
            product.setName("Coca Cola");
            product.setBarcode("123456789");
            product.setSku("cc777");
            product.setCreatedDate(System.currentTimeMillis());
            products.add(product);

            Product product1 = new Product();
            product1.setName("Колбаса");
            product1.setBarcode("777444888");
            product1.setSku("колБ123");
            product1.setCreatedDate(System.currentTimeMillis());
            products.add(product1);


            Product product2 = new Product();
            product2.setName("Яицо");
            product2.setBarcode("7887878787");
            product2.setSku("Тт15");
            product2.setCreatedDate(System.currentTimeMillis());
            products.add(product2);


            Product product3 = new Product();
            product3.setName("Анти Хайп");
            product3.setBarcode("");
            product3.setSku("");
            product3.setCreatedDate(System.currentTimeMillis());
            products.add(product3);


            Product product4 = new Product();
            product4.setName("58йй2");
            product4.setBarcode("фывйа");
            product4.setSku("фывййаыа");
            product4.setCreatedDate(System.currentTimeMillis());
            products.add(product4);


            Product product5 = new Product();
            product5.setName("5884878613");
            product5.setBarcode("777889961");
            product5.setSku("cc777");
            product5.setCreatedDate(System.currentTimeMillis());
            products.add(product5);


            Product product6 = new Product();
            product6.setName("Сардор");
            product6.setBarcode("234023");
            product6.setSku("сс144458");
            product6.setCreatedDate(System.currentTimeMillis());
            products.add(product6);
            mDaoSession.getProductDao().insertOrReplaceInTx(products);
        }
        return Single.create(e -> {

            QueryBuilder<Product> queryBuilderCred = mDaoSession.getProductDao().queryBuilder();
            queryBuilderCred.whereOr(
                    ProductDao.Properties.Name.like("%" + searchText.toLowerCase() + "%"),
                    ProductDao.Properties.Name.like("%" + searchText.toUpperCase() + "%"),
                    ProductDao.Properties.Barcode.like("%" + searchText.toUpperCase() + "%"),
                    ProductDao.Properties.Barcode.like("%" + searchText.toUpperCase() + "%"),
                    ProductDao.Properties.Sku.like("%" + searchText.toUpperCase() + "%"),
                    ProductDao.Properties.Sku.like("%" + searchText.toUpperCase() + "%"));
            List<Product> list = queryBuilderCred.build().list();
            for (int i = list.size() - 1; i >= 0; i--) {
                if (list.get(i).getIsDeleted()) list.remove(i);
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
                .where(ProductDao.Properties.CategoryId.eq(categoryId), ProductDao.Properties.Name.eq(productName))
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
//        if(mDaoSession.getProductDao().loadAll().size()==0){
//
//        Account account = new Account();
//        account.setType(1);
//        account.setCirculation(0);
//        account.setName("Cash");
//        mDaoSession.getAccountDao().insertOrReplace(account);
//
//        Currency currency = new Currency();
//        currency.setAbbr("uzs");
//        currency.setName("Sum");
//        currency.setIsMain(true);
//        mDaoSession.getCurrencyDao().insertOrReplace(currency);
//
//        Unit unit = new Unit();
//        unit.setAbbr("kg");
//        unit.setFactorRoot(1);
//        unit.setIsActive(true);
//
//        mDaoSession.getUnitDao().insertOrReplace(unit);
//
//        Unit unit1 = new Unit();
//        unit1.setAbbr("pcs");
//        unit1.setFactorRoot(1);
//        unit1.setIsActive(true);
//
//        mDaoSession.getUnitDao().insertOrReplace(unit1);
//
//
//        Vendor vendor = new Vendor();
//        vendor.setName("Huawei Design");
//        vendor.setContactName("Islomov Sardor");
//        vendor.setDeleted(false);
//        vendor.setActive(true);
//        vendor.setCreatedDate(System.currentTimeMillis());
//        mDaoSession.getVendorDao().insertOrReplace(vendor);
//
//        Vendor vendor1 = new Vendor();
//        vendor1.setName("Motorola");
//        vendor1.setContactName("Islomov Sardor");
//        vendor1.setDeleted(false);
//        vendor1.setActive(true);
//        vendor1.setCreatedDate(System.currentTimeMillis());
//        mDaoSession.getVendorDao().insertOrReplace(vendor1);
//
//
//        Vendor vendor2 = new Vendor();
//        vendor2.setName("Samsung");
//        vendor2.setContactName("Islomov Sardor");
//        vendor2.setDeleted(false);
//        vendor2.setActive(true);
//        vendor2.setCreatedDate(System.currentTimeMillis());
//        mDaoSession.getVendorDao().insertOrReplace(vendor2);
//
//        Vendor vendor3 = new Vendor();
//        vendor3.setName("Google");
//        vendor3.setContactName("Islomov Sardor");
//        vendor3.setDeleted(false);
//        vendor3.setActive(true);
//        vendor3.setCreatedDate(System.currentTimeMillis());
//        mDaoSession.getVendorDao().insertOrReplace(vendor3);
//
//
//        ProductClass productClass = new ProductClass();
//        productClass.setName("T-shirt");
//        productClass.setCreatedDate(System.currentTimeMillis());
//        productClass.setActive(true);
//        productClass.setDeleted(false);
//
//        mDaoSession.getProductClassDao().insertOrReplace(productClass);
//
//        Category category = new Category();
//        category.setName("MoneyIsGood");
//        mDaoSession.getCategoryDao().insertOrReplace(category);
//
//        Category subcategory = new Category();
//        subcategory.setParentId(category.getId());
//        subcategory.setName("GirlsAreGood");
//        mDaoSession.getCategoryDao().insertOrReplace(subcategory);
//
//        Product product = new Product();
//        product.setName("Taxima");
//        product.setBarcode("88974");
//        product.setSku("12312421");
//        product.setPrice(6500d);
//        product.setCreatedDate(System.currentTimeMillis());
//        product.setMainUnitId(unit.getId());
//        product.setCostCurrencyId(currency.getId());
//        product.setPriceCurrencyId(currency.getId());
//        product.setClassId(productClass.getId());
//        product.setCategoryId(subcategory.getId());
//        mDaoSession.getProductDao().insertOrReplace(product);
//
//        VendorProductCon vendorProductCon = new VendorProductCon();
//        vendorProductCon.setProductId(product.getId());
//        vendorProductCon.setVendorId(vendor.getId());
//        vendorProductCon.setCost(6300d);
//        mDaoSession.getVendorProductConDao().insertOrReplace(vendorProductCon);
//
//        VendorProductCon vendorProductCon0 = new VendorProductCon();
//        vendorProductCon0.setProductId(product.getId());
//        vendorProductCon0.setVendorId(vendor1.getId());
//        vendorProductCon.setCost(6100d);
//        mDaoSession.getVendorProductConDao().insertOrReplace(vendorProductCon0);
//
//            InventoryState inventoryState = new InventoryState();
//            inventoryState.setValue(0d);
//            inventoryState.setVendor(vendor);
//            inventoryState.setProduct(product);
//            mDaoSession.getInventoryStateDao().insertOrReplace(inventoryState);
//
//            InventoryState inventoryState1 = new InventoryState();
//            inventoryState1.setValue(0d);
//            inventoryState1.setVendor(vendor1);
//            inventoryState1.setProduct(product);
//            mDaoSession.getInventoryStateDao().insertOrReplace(inventoryState1);
//
//            Product product5 = new Product();
//            product5.setName("Bono Chocolate");
//            product5.setBarcode("3414124");
//            product5.setSku("8732344");
//            product5.setPrice(6500d);
//            product5.setCreatedDate(System.currentTimeMillis());
//            product5.setMainUnitId(unit1.getId());
//            product5.setCostCurrencyId(currency.getId());
//            product5.setPriceCurrencyId(currency.getId());
//            product5.setClassId(productClass.getId());
//            product5.setCategoryId(subcategory.getId());
//            mDaoSession.getProductDao().insertOrReplace(product5);
//
//            VendorProductCon vendorProductCon01 = new VendorProductCon();
//            vendorProductCon01.setProductId(product5.getId());
//            vendorProductCon01.setVendorId(vendor2.getId());
//            vendorProductCon01.setCost(6200d);
//            mDaoSession.getVendorProductConDao().insertOrReplace(vendorProductCon01);
//
//            InventoryState inventoryState2 = new InventoryState();
//            inventoryState2.setValue(0d);
//            inventoryState2.setVendor(vendor2);
//            inventoryState2.setProduct(product5);
//            mDaoSession.getInventoryStateDao().insertOrReplace(inventoryState2);
//
//            Product product6 = new Product();
//            product6.setName("Sugar 1000gr");
//            product6.setBarcode("8875154");
//            product6.setSku("px1412");
//            product6.setPrice(6500d);
//            product6.setCreatedDate(System.currentTimeMillis());
//            product6.setMainUnitId(unit1.getId());
//            product6.setCostCurrencyId(currency.getId());
//            product6.setPriceCurrencyId(currency.getId());
//            product6.setClassId(productClass.getId());
//            product6.setCategoryId(subcategory.getId());
//            mDaoSession.getProductDao().insertOrReplace(product6);
//
//            VendorProductCon vendorProductCon02 = new VendorProductCon();
//            vendorProductCon02.setProductId(product6.getId());
//            vendorProductCon02.setVendorId(vendor3.getId());
//            vendorProductCon02.setCost(6500d);
//            mDaoSession.getVendorProductConDao().insertOrReplace(vendorProductCon02);
//
//            InventoryState inventoryState3 = new InventoryState();
//            inventoryState3.setValue(0d);
//            inventoryState3.setVendor(vendor3);
//            inventoryState3.setProduct(product6);
//            mDaoSession.getInventoryStateDao().insertOrReplace(inventoryState3);
//
//            Product product1 = new Product();
//        product1.setName("To'xtaniyoz ota kapchonniy");
//        product1.setBarcode("sadq");
//        product1.setSku("qweqs");
//        product1.setPrice(5000d);
//        product1.setCreatedDate(System.currentTimeMillis());
//        product1.setMainUnitId(unit1.getId());
//        product1.setCostCurrencyId(currency.getId());
//        product1.setClassId(productClass.getId());
//        product1.setPriceCurrencyId(currency.getId());
//        product1.setCategoryId(subcategory.getId());
//        mDaoSession.getProductDao().insertOrReplace(product1);
//
//
//        VendorProductCon vendorProductCon1 = new VendorProductCon();
//        vendorProductCon1.setProductId(product1.getId());
//        vendorProductCon1.setVendorId(vendor.getId());
//        mDaoSession.getVendorProductConDao().insertOrReplace(vendorProductCon1);
//
//            InventoryState inventoryState4 = new InventoryState();
//            inventoryState4.setValue(0d);
//            inventoryState4.setVendor(vendor);
//            inventoryState4.setProduct(product1);
//            mDaoSession.getInventoryStateDao().insertOrReplace(inventoryState4);
//    }
        return Single.create(e -> {

            String query = "SELECT _id, PRODUCT_ID, SUM(VALUE) AS INVENTORY, LOW_STOCK_ALERT, VENDOR_ID FROM INVENTORYSTATE GROUP BY PRODUCT_ID";
            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, null);
            List<InventoryItem> inventoryItems = new ArrayList<>();
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                while (!cursor.isAfterLast()) {
                    InventoryItem inventoryItem = new InventoryItem();
                    inventoryItem.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                    inventoryItem.setProduct(mDaoSession.getProductDao().load(cursor.getLong(cursor.getColumnIndex("PRODUCT_ID"))));
                    inventoryItem.setVendor(mDaoSession.getVendorDao().load(cursor.getLong(cursor.getColumnIndex("VENDOR_ID"))));
                    inventoryItem.setInventory(cursor.getDouble(cursor.getColumnIndex("INVENTORY")));
                    inventoryItem.setLowStockAlert(cursor.getDouble(cursor.getColumnIndex("LOW_STOCK_ALERT")));
                    inventoryItems.add(inventoryItem);

                    cursor.moveToNext();
                }
            }
            e.onSuccess(inventoryItems);
        });
    }

    @Override
    public Observable<Long> addProductVendorConn(VendorProductCon vendorProductCon) {
        return Observable.fromCallable(() -> mDaoSession.getVendorProductConDao().insertOrReplace(vendorProductCon));
    }

    @Override
    public Observable<Boolean> removeVendorProductConnection(VendorProductCon vendorProductCon) {
        return Observable.fromCallable(() -> {
            mDaoSession.getVendorProductConDao().delete(vendorProductCon);
            return true;
        });
    }

    @Override
    public Observable<Boolean> removeVendorProductConnectionByVendorId(Long vendorid) {
        return Observable.fromCallable(() -> {
            mDaoSession.queryBuilder(VendorProductCon.class)
                    .where(VendorProductConDao.Properties.VendorId.eq(vendorid))
                    .buildDelete().executeDeleteWithoutDetachingEntities();
            return true;
        });
    }

    @Override
    public Observable<Boolean> removeVendorProductConnectionByProductId(Long productId) {
        return Observable.fromCallable(() -> {
            mDaoSession.queryBuilder(VendorProductCon.class)
                    .where(VendorProductConDao.Properties.ProductId.eq(productId))
                    .buildDelete().executeDeleteWithoutDetachingEntities();
            return true;
        });
    }

    @Override
    public Observable<List<VendorProductCon>> getVendorProductConnectionByProductId(Long productId) {
        return Observable.fromCallable(() -> mDaoSession.queryBuilder(VendorProductCon.class)
                .where(VendorProductConDao.Properties.ProductId.eq(productId)).build().list());
    }

    @Override
    public Observable<VendorProductCon> getVendorProductConnectionById(Long productId, Long vendorId) {
        return Observable.fromCallable(() -> mDaoSession.queryBuilder(VendorProductCon.class)
                .where(VendorProductConDao.Properties.ProductId.eq(productId), VendorProductConDao.Properties.VendorId.eq(vendorId)).build().list().get(0));
    }

    @Override
    public Observable<List<Category>> getActiveCategories() {
        return Observable.fromCallable(() -> mDaoSession.queryBuilder(Category.class)
                .where(CategoryDao.Properties.ParentId.eq(WITHOUT_PARENT),
                        CategoryDao.Properties.IsActive.eq(true),
                        CategoryDao.Properties.IsDeleted.eq(false),
                        CategoryDao.Properties.IsNotModified.eq(true))
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
        return Observable.fromCallable(() -> mDaoSession.getConsignmentDao().loadAll());
    }

    @Override
    public Single<List<Consignment>> getConsignmentsByVendorId(Long vendorId) {
        return Single.create(e -> {
            List<Consignment> consignmentList = mDaoSession.queryBuilder(Consignment.class)
                    .where(ConsignmentDao.Properties.VendorId.eq(vendorId),
                           ConsignmentDao.Properties.IsNotModified.eq(true),
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
    public Single<List<VendorWithDebt>> getVendorWirhDebt() {

        return Single.create(e -> {
            List<Vendor> vendors = mDaoSession.getVendorDao().loadAll();
            List<VendorWithDebt> vendorWithDebts = new ArrayList<>();
            for (Vendor vendor : vendors) {
                VendorWithDebt vendorWithDebt = new VendorWithDebt();
                vendorWithDebt.setVendor(vendor);
                vendor.resetProducts();
                String query = "SELECT  SUM(AMOUNT) AS AMOUNT FROM BILLING_OPERATION WHERE IS_NOT_MODIFIED == " + 1 + " AND IS_DELETED == " + 0 +  " GROUP BY VENDOR_ID HAVING VENDOR_ID=?";
                Cursor cursor = mDaoSession.getDatabase().rawQuery(query, new String[]{String.valueOf(vendor.getId())});

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    vendorWithDebt.setDebt(cursor.getDouble(cursor.getColumnIndex("AMOUNT")));
                } else {
                    vendorWithDebt.setDebt(0);
                }
                vendorWithDebts.add(vendorWithDebt);
            }
            for (VendorWithDebt vendorWithDebt : vendorWithDebts) {
                Collections.sort(vendorWithDebt.getVendor().getProducts(), (product, t1) -> product.getName().compareTo(t1.getName()));
            }
            e.onSuccess(vendorWithDebts);
        });
    }

    @Override
    public Single<Double> getVendorDebt(Long vendorId) {
        return Single.create(e -> {
            double debt=0;
            String query = "SELECT  SUM(AMOUNT) AS AMOUNT FROM BILLING_OPERATION WHERE IS_NOT_MODIFIED == " + 1 + " AND IS_DELETED == " + 0 + " GROUP BY VENDOR_ID HAVING VENDOR_ID=?";
            Cursor cursor = mDaoSession.getDatabase().rawQuery(query,  new String[]{String.valueOf(vendorId)});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                debt = cursor.getDouble(cursor.getColumnIndex("AMOUNT"));
            }else {
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
                .where(BillingOperationsDao.Properties.IsDeleted.eq(false),
                       BillingOperationsDao.Properties.IsNotModified.eq(true))
                .build()
                .list());
    }

    @Override
    public Single<BillingOperations> getBillingOperationsByConsignmentId(Long consignmentId) {
        return Single.create(e -> {
            List<BillingOperations> billingOperations = mDaoSession.queryBuilder(BillingOperations.class)
                    .where(BillingOperationsDao.Properties.ConsignmentId.eq(consignmentId),
                           BillingOperationsDao.Properties.IsNotModified.eq(true),
                           BillingOperationsDao.Properties.IsDeleted.eq(false))
                    .build()
                    .list();
            e.onSuccess(billingOperations.get(0));
        });
    }

    @Override
    public Single<BillingOperations> getBillingOperationsById(Long firstPayId) {
        return Single.create(e -> {
            List<BillingOperations> billingOperations = mDaoSession.queryBuilder(BillingOperations.class)
                    .where(BillingOperationsDao.Properties.Id.eq(firstPayId),
                           BillingOperationsDao.Properties.IsNotModified.eq(true),
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
                    .whereOr(BillingOperationsDao.Properties.RootId.eq(rootId), BillingOperationsDao.Properties.Id.eq(rootId))
                    .build()
                    .list();
            e.onSuccess(billingOperations);
        });
    }

    @Override
    public Observable<Long> insertInventoryState(InventoryState inventoryState) {
        return Observable.fromCallable(() -> mDaoSession.getInventoryStateDao().insertOrReplace(inventoryState));
    }

    @Override
    public Observable<List<InventoryState>> getInventoryStates() {
        return Observable.fromCallable(() -> mDaoSession.getInventoryStateDao().loadAll());
    }

    @Override
    public Observable<List<InventoryState>> getInventoryStatesByProductId(Long productId) {
        return Observable.fromCallable(() -> mDaoSession.queryBuilder(InventoryState.class)
                .where(InventoryStateDao.Properties.ProductId.eq(productId)).build().list());
    }

    @Override
    public Single<List<BillingOperations>> getBillingOperationInteval(Long vendorId, Calendar fromDate, Calendar toDate) {
        return Single.create(e -> {
            fromDate.set(Calendar.HOUR_OF_DAY,0);
            fromDate.set(Calendar.MINUTE,0);
            fromDate.set(Calendar.SECOND,0);
            fromDate.set(Calendar.MILLISECOND,0);

            toDate.set(Calendar.HOUR_OF_DAY,23);
            toDate.set(Calendar.MINUTE,59);
            toDate.set(Calendar.SECOND,59);
            toDate.set(Calendar.MILLISECOND,9999);

            List<BillingOperations> billingOperations = mDaoSession.getBillingOperationsDao().queryBuilder()
                    .where(BillingOperationsDao.Properties.PaymentDate.ge(fromDate.getTimeInMillis()),
                            BillingOperationsDao.Properties.PaymentDate.le(toDate.getTimeInMillis()),
                            BillingOperationsDao.Properties.VendorId.eq(vendorId),
                            BillingOperationsDao.Properties.IsNotModified.eq(true))
                    .build().list();

            e.onSuccess(billingOperations);
        });
    }

    @Override
    public Single<Long> insertWarehouseOperation(WarehouseOperations warehouseOperations) {
        return Single.create(e -> {
            Database database = mDaoSession.getDatabase();
            database.beginTransaction();
            try {
                mDaoSession.getWarehouseOperationsDao().insertOrReplace(warehouseOperations);

                List<InventoryState> inventoryStates = mDaoSession.getInventoryStateDao().queryBuilder()
                        .where(InventoryStateDao.Properties.ProductId.eq(warehouseOperations.getProductId()), InventoryStateDao.Properties.VendorId.eq(warehouseOperations.getVendorId()))
                        .build().list();

                if (inventoryStates.size() != 1) {
                    e.onError(new Throwable("Inventory state invalid data (ONE PRODUCT FROM VENDOR IN DATABASE NOT UNIQUE OR NOT HAVE)"));
                    return;
                }

                InventoryState inventoryState = inventoryStates.get(0);
                inventoryState.setValue(inventoryState.getValue() + warehouseOperations.getValue());
                inventoryState.update();

                database.setTransactionSuccessful();
                database.endTransaction();
                e.onSuccess(warehouseOperations.getId());
            } catch (Exception o) {
                e.onError(o);
            }
        });
    }

    @Override
    public Single<WarehouseOperations> getWarehouseOperationById(Long warehouseId) {
        return Single.create(e -> {
            try {
                WarehouseOperations warehouseOperations = mDaoSession.getWarehouseOperationsDao().queryBuilder()
                        .where(WarehouseOperationsDao.Properties.Id.eq(warehouseId))
                        .build().list().get(0);
                e.onSuccess(warehouseOperations);
            } catch (Exception o) {
                e.onError(o);
            }
        });
    }

    @Override
    public Single<Long> replaceWarehouseOperation(WarehouseOperations warehouseOperations) {
        return Single.create(e -> {
            Database database = mDaoSession.getDatabase();
            database.beginTransaction();
            try {
                mDaoSession.getWarehouseOperationsDao().insertOrReplace(warehouseOperations);

                List<InventoryState> inventoryStates = mDaoSession.getInventoryStateDao().queryBuilder()
                        .where(InventoryStateDao.Properties.ProductId.eq(warehouseOperations.getProductId()), InventoryStateDao.Properties.VendorId.eq(warehouseOperations.getVendorId()))
                        .build().list();

                if (inventoryStates.size() != 1) {
                    e.onError(new Throwable("Inventory state invalid data (ONE PRODUCT FROM VENDOR IN DATABASE NOT UNIQUE OR NOT HAVE)"));
                    return;
                }

                InventoryState inventoryState = inventoryStates.get(0);
                inventoryState.setValue(inventoryState.getValue() + warehouseOperations.getValue() * -1);
                inventoryState.update();

                database.setTransactionSuccessful();
                database.endTransaction();
                e.onSuccess(warehouseOperations.getId());
            } catch (Exception o) {
                e.onError(o);
            }
        });
    }

    @Override
    public Observable<Product> getProductById(Long productId) {
        return Observable.fromCallable(() -> mDaoSession.getProductDao().load(productId));
    }

    @Override
    public Observable<Boolean> removeProductFromInventoryState(Long productId) {
        return Observable.fromCallable(() -> {
            mDaoSession.queryBuilder(InventoryStateDao.class)
                    .where(InventoryStateDao.Properties.ProductId.eq(productId))
                    .buildDelete().executeDeleteWithoutDetachingEntities();
            return true;
        });
    }

    @Override
    public Observable<List<InventoryState>> getInventoryStatesByVendorId(Long vendorId) {
        return Observable.fromCallable(() -> mDaoSession.getInventoryStateDao().queryBuilder().where(InventoryStateDao.Properties.VendorId.eq(vendorId)).build().list());
    }

    @Override
    public Currency getMainCurrency() {
        List<Currency> currencies = mDaoSession.queryBuilder(Currency.class)
                .where(CurrencyDao.Properties.IsMain.eq(true))
                .build().list();
        return currencies.get(0);
    }

    @Override
    public Single<List<BillingOperations>> getBillingOperationForVendor(Long vendorId) {
        return Single.create(e -> {
            List<BillingOperations> billingOperations = mDaoSession.queryBuilder(BillingOperations.class)
                    .where(BillingOperationsDao.Properties.VendorId.eq(vendorId),
                            BillingOperationsDao.Properties.IsNotModified.eq(true),
                            BillingOperationsDao.Properties.IsDeleted.eq(false))
                    .build().list();
            e.onSuccess(billingOperations);
        });
    }

    @Override
    public Single<Boolean> deleteInventoryState(InventoryState inventoryState) {
        return Single.create(e -> {
            mDaoSession
                    .queryBuilder(InventoryState.class)
                    .where(InventoryStateDao.Properties.Id.eq(inventoryState.getId()))
                    .buildDelete()
                    .executeDeleteWithoutDetachingEntities();
            e.onSuccess(true);
        });
    }
}
