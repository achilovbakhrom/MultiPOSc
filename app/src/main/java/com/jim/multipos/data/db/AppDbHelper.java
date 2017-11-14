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
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.PaymentTypeDao;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.ServiceFeeDao;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerDao;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.customer.CustomerGroupDao;
import com.jim.multipos.data.db.model.customer.JoinCustomerGroupsWithCustomers;
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

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;

import static com.jim.multipos.data.db.model.products.Category.WITHOUT_PARENT;
import static com.jim.multipos.utils.CategoryUtils.isSubcategory;


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

    @Override
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
                .where(CategoryDao.Properties.ParentId.eq(WITHOUT_PARENT), CategoryDao.Properties.IsDeleted.eq(false))
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
                .filter(product -> product.getParentId().equals(parent.getId()))
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
                List<Discount> discounts = mDaoSession.getDiscountDao().loadAll();
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
        return Observable.fromCallable(() -> mDaoSession.getServiceFeeDao().queryBuilder().orderDesc(ServiceFeeDao.Properties.CreatedDate).build().list());
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
    public Single<List<Product>> getSearchProducts(String searchText, boolean skuMode, boolean barcodeMode,boolean nameMode) {
        if(mDaoSession.getProductDao().loadAll().size()==0){
            List<Product> products = new ArrayList<>();

            Product product = new Product();
            product.setName("Coca Cola");
            product.setBarcode("123456789");
            product.setSku("cc777");
            product.setPrice(1d);
            product.setCost(1d);
            product.setCreatedDate(System.currentTimeMillis());
            products.add(product);

            Product product1 = new Product();
            product1.setName("Колбаса");
            product1.setBarcode("777444888");
            product1.setSku("колБ123");
            product1.setPrice(1d);
            product1.setCost(1d);
            product1.setCreatedDate(System.currentTimeMillis());
            products.add(product1);


            Product product2 = new Product();
            product2.setName("Яицо");
            product2.setBarcode("7887878787");
            product2.setSku("Тт15");
            product2.setPrice(1d);
            product2.setCost(1d);
            product2.setCreatedDate(System.currentTimeMillis());
            products.add(product2);


            Product product3 = new Product();
            product3.setName("Анти Хайп");
            product3.setBarcode("");
            product3.setSku("");
            product3.setPrice(1d);
            product3.setCost(1d);
            product3.setCreatedDate(System.currentTimeMillis());
            products.add(product3);


            Product product4 = new Product();
            product4.setName("58йй2");
            product4.setBarcode("фывйа");
            product4.setSku("фывййаыа");
            product4.setPrice(1d);
            product4.setCost(1d);
            product4.setCreatedDate(System.currentTimeMillis());
            products.add(product4);


            Product product5 = new Product();
            product5.setName("5884878613");
            product5.setBarcode("777889961");
            product5.setSku("cc777");
            product5.setPrice(1d);
            product5.setCost(1d);
            product5.setCreatedDate(System.currentTimeMillis());
            products.add(product5);


            Product product6 = new Product();
            product6.setName("Сардор");
            product6.setBarcode("234023");
            product6.setSku("сс144458");
            product6.setPrice(1d);
            product6.setCost(1d);
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
            for (int i = list.size()-1; i >=0; i--) {
                if(list.get(i).getIsDeleted()) list.remove(i);
                boolean keepMe = false;
                if(skuMode && list.get(i).getSku().toUpperCase().contains(searchText.toUpperCase())){
                    keepMe = true;
                }
                if(barcodeMode && list.get(i).getBarcode().toUpperCase().contains(searchText.toUpperCase())){
                    keepMe = true;
                }
                if(nameMode && list.get(i).getName().toUpperCase().contains(searchText.toUpperCase())){
                    keepMe = true;
                }
                if(!keepMe){
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
        if(mDaoSession.getProductDao().loadAll().size()==0){

            List<Product> products = new ArrayList<>();



            Product product = new Product();
            product.setName("Coca Cola");
            product.setBarcode("123456789");
            product.setSku("cc777");
            product.setPrice(1d);
            product.setCost(1d);
            product.setCreatedDate(System.currentTimeMillis());
            products.add(product);

            Product product1 = new Product();
            product1.setName("Колбаса");
            product1.setBarcode("777444888");
            product1.setSku("колБ123");
            product1.setPrice(1d);
            product1.setCost(1d);
            product1.setCreatedDate(System.currentTimeMillis());
            products.add(product1);


            Product product2 = new Product();
            product2.setName("Яицо");
            product2.setBarcode("7887878787");
            product2.setSku("Тт15");
            product2.setPrice(1d);
            product2.setCost(1d);
            product2.setCreatedDate(System.currentTimeMillis());
            products.add(product2);


            Product product3 = new Product();
            product3.setName("Анти Хайп");
            product3.setBarcode("");
            product3.setSku("");
            product3.setPrice(1d);
            product3.setCost(1d);
            product3.setCreatedDate(System.currentTimeMillis());
            products.add(product3);


            Product product4 = new Product();
            product4.setName("58йй2");
            product4.setBarcode("фывйа");
            product4.setSku("фывййаыа");
            product4.setPrice(1d);
            product4.setCost(1d);
            product4.setCreatedDate(System.currentTimeMillis());
            products.add(product4);


            Product product5 = new Product();
            product5.setName("5884878613");
            product5.setBarcode("777889961");
            product5.setSku("cc777");
            product5.setPrice(1d);
            product5.setCost(1d);
            product5.setCreatedDate(System.currentTimeMillis());
            products.add(product5);


            Product product6 = new Product();
            product6.setName("Сардор");
            product6.setBarcode("234023");
            product6.setSku("сс144458");
            product6.setPrice(1d);
            product6.setCost(1d);
            product6.setCreatedDate(System.currentTimeMillis());
            products.add(product6);
            mDaoSession.getProductDao().insertOrReplaceInTx(products);


            Vendor vendor = new Vendor();
            vendor.setName("Orifjon");
            vendor.setActive(true);
            vendor.setCreatedDate(System.currentTimeMillis());
            mDaoSession.getVendorDao().insertOrReplace(vendor);

            Vendor vendor1 = new Vendor();
            vendor1.setName("Anvarjon");
            vendor1.setActive(true);
            vendor1.setCreatedDate(System.currentTimeMillis());
            mDaoSession.getVendorDao().insertOrReplace(vendor1);

        }

        return Single.create(e -> {
            List<InventoryItem> inventoryItems = new ArrayList<>();
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
    public Observable<List<Category>> getActiveCategories() {
        return Observable.fromCallable(() -> mDaoSession.queryBuilder(Category.class)
                .where(CategoryDao.Properties.ParentId.eq(WITHOUT_PARENT),
                        CategoryDao.Properties.IsActive.eq(true))
                .build()
                .list());
    }

    @Override
    public Observable<Long> insertConsignment(Consignment consignment) {
        return Observable.fromCallable(() -> mDaoSession.getConsignmentDao().insertOrReplace(consignment));
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
    public Observable<Boolean> insertConsignmentProduct(List<ConsignmentProduct> consignmentProducts) {
        return Observable.fromCallable(() ->
        {
            mDaoSession.getConsignmentProductDao().insertOrReplaceInTx(consignmentProducts);
            return true;
        });
    }

    @Override
    public Observable<Product> getProductById(Long productId) {
        return Observable.fromCallable(() -> mDaoSession.getProductDao().load(productId));
    }

    @Override
    public Observable<List<Product>> getAllActiveProductsFromVendor(Long vendorId) {
//        return Observable.fromCallable(() -> mDaoSession.getProductDao().queryBuilder()
//                 .where(ProductDao.Properties.VendorId.eq(vendorId), ProductDao.Properties.IsDeleted.eq(false))
//                 .where(ProductDao.Properties.IsNotModified.eq(true), ProductDao.Properties.IsActive.eq(true))
//                 .build().list());
        return null;
    }
}
