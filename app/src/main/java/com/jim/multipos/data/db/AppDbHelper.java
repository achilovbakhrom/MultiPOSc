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
import android.util.Log;

import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.AccountDao;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.DaoMaster;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.PaymentTypeDao;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.customer.JoinCustomerGroupsWithCustomers;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.CategoryDao;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.stock.Stock;
import com.jim.multipos.data.db.model.unit.SubUnitsList;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.data.db.model.unit.UnitDao;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;


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
    public Observable<Boolean> deleteJoinCustomerGroupWithCustomer(String customerGroupId, String customerId) {
        return Observable.fromCallable(() -> {
            mDaoSession.getDatabase().execSQL("DELETE FROM JOIN_CUSTOMER_GROUPS_WITH_CUSTOMERS WHERE CUSTOMER_ID=? AND CUSTOMER_GROUP_ID=?", new String[]{customerId, customerGroupId});

            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteJoinCustomerGroupWithCustomer(String customerId) {
        return Observable.fromCallable(() -> {
            mDaoSession.getDatabase().execSQL("DELETE FROM JOIN_CUSTOMER_GROUPS_WITH_CUSTOMERS WHERE CUSTOMER_ID=?", new String[]{customerId});

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
        return Observable.fromCallable(() -> mDaoSession.getContactDao().insert(contact));
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
        return Observable.create(subscriber -> {
            Cursor cursor = mDaoSession.getDatabase().rawQuery("SELECT MAX(position) FROM POSITION", null);
            cursor.moveToFirst();
            int max = cursor.getInt(0);
            long insert = mDaoSession.getCategoryDao().insert(category);
            /*CategoryPosition categoryPosition = new CategoryPosition();
            categoryPosition.setParentId(category.getId());
            categoryPosition.setCategory(category);
            categoryPosition.setPosition(max + 1);
            mDaoSession.getCategoryPositionDao().insert(categoryPosition);*/
            subscriber.onNext(insert);
            subscriber.onComplete();
        });
    }

    @Override
    public Observable<Boolean> insertCategories(List<Category> categories) {
        return Observable.fromCallable(() ->
        {
            mDaoSession.getCategoryDao().insertOrReplaceInTx(categories);
            return true;
        });
    }

    @Override
    public Observable<List<Category>> getAllCategories() {
        return Observable.fromCallable(() -> mDaoSession.getCategoryDao().loadAll());
    }

    @Override
    public Observable<Long> insertOrReplaceCategory(Category category) {
        return Observable.fromCallable(() -> mDaoSession.getCategoryDao().insertOrReplace(category));
    }

    @Override
    public Observable<Long> insertOrReplaceCategoryByPosition(Category category) {
        return Observable.create(subscriber -> {
            String rootId;
            /*if (category.getRootId() == null) {
                rootId = category.getId();
            } else rootId = category.getRootId();
            Query<CategoryPosition> categoryPositionQuery = mDaoSession.getCategoryPositionDao().queryBuilder()
                    .where(CategoryPositionDao.Properties.ParentId.eq(rootId)).build();
            if (!categoryPositionQuery.list().isEmpty()) {
                CategoryPosition categoryPosition = categoryPositionQuery.list().get(0);
                categoryPosition.setParentId(category.getId());
                mDaoSession.getCategoryPositionDao().insertOrReplace(categoryPosition);
            }*/
            long insert = mDaoSession.getCategoryDao().insertOrReplace(category);
            subscriber.onNext(insert);
            subscriber.onComplete();
        });
    }

    @Override
    public Observable<Long> insertProduct(Product product) {
        return Observable.create(subscriber -> {
            Cursor cursor = mDaoSession.getDatabase().rawQuery("SELECT MAX(position) FROM PRODUCT_POSITION", null);
            cursor.moveToFirst();
            int max = cursor.getInt(0);
            long insert = mDaoSession.getProductDao().insertOrReplace(product);
            /*ProductPosition productPosition = new ProductPosition();
            productPosition.setProduct(product);
            productPosition.setProductId(product.getId());
            productPosition.setSubCategoryId(product.getSubCategoryId());
            productPosition.setPosition(max + 1);
            mDaoSession.getProductPositionDao().insert(productPosition);*/
            subscriber.onNext(insert);
            subscriber.onComplete();
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
    public Observable<Long> insertOrReplaceProduct(Product product) {
        return Observable.fromCallable(() -> mDaoSession.getProductDao().insertOrReplace(product));
    }

    /*@Override
    public Observable<Long> insertSubCategory(SubCategory subCategory) {
        return Observable.create(subscriber -> {
            Cursor cursor = mDaoSession.getDatabase().rawQuery("SELECT MAX(position) FROM SUB_CAT_POSITION", null);
            cursor.moveToFirst();
            int max = cursor.getInt(0);
            long insert = mDaoSession.getSubCategoryDao().insert(subCategory);
            SubCategoryPosition subCategoryPosition = new SubCategoryPosition();
            subCategoryPosition.setSubCategory(subCategory);
            subCategoryPosition.setSubCategoryId(subCategory.getId());
            subCategoryPosition.setCategoryId(subCategory.getCategoryId());
            subCategoryPosition.setPosition(max + 1);
            mDaoSession.getSubCategoryPositionDao().insert(subCategoryPosition);
            subscriber.onNext(insert);
            subscriber.onComplete();
        });
    }

    @Override
    public Observable<Boolean> insertSubCategories(List<SubCategory> subCategories) {
        return Observable.fromCallable(() ->
        {
            mDaoSession.getSubCategoryDao().insertOrReplaceInTx(subCategories);
            return true;
        });
    }

    @Override
    public Observable<List<SubCategory>> getAllSubCategories() {
        return Observable.fromCallable(() -> mDaoSession.getSubCategoryDao().loadAll());
    }

    @Override
    public Observable<Long> insertOrReplaceSubCategory(SubCategory subCategory) {
        return Observable.fromCallable(() -> mDaoSession.getSubCategoryDao().insertOrReplace(subCategory));
    }

    @Override
    public Observable<Boolean> insertCategoryPositions(List<CategoryPosition> positionList) {
        return Observable.fromCallable(() ->
        {
            mDaoSession.getCategoryPositionDao().deleteAll();
            mDaoSession.getCategoryPositionDao().insertOrReplaceInTx(positionList);
            return true;
        });
    }*/

    /*@Override
    public Observable<List<Category>> getAllCategoryPositions() {
        return Observable.create(subscriber -> {
            List<CategoryPosition> categoryPositions = mDaoSession.getCategoryPositionDao().loadAll();
            if (categoryPositions.size() == 0) {
                subscriber.onNext(mDaoSession.getCategoryDao().loadAll());
                subscriber.onComplete();
                return;
            }
            Collections.sort(categoryPositions, (categoryPosition, t1) -> categoryPosition.getPosition().compareTo(t1.getPosition()));
            List<Category> categoryList = new ArrayList<>();
            for (CategoryPosition categoryPosition : categoryPositions) {
                categoryList.add(categoryPosition.getCategory());
            }
            subscriber.onNext(categoryList);
            subscriber.onComplete();
        });
    }

    @Override
    public Observable<Boolean> insertProductPositions(List<ProductPosition> positionList, SubCategory subCategory) {
        return Observable.fromCallable(() ->
        {
            DeleteQuery<ProductPosition> deleteQuery = mDaoSession.getProductPositionDao().queryBuilder().
                    where(ProductPositionDao.Properties.SubCategoryId.eq(subCategory.getId())).buildDelete();
            deleteQuery.executeDeleteWithoutDetachingEntities();
            mDaoSession.clear();
            mDaoSession.getProductPositionDao().insertOrReplaceInTx(positionList);
            return true;
        });
    }

    @Override
    public Observable<List<Product>> getAllProductPositions(SubCategory subCategory) {
        return Observable.create(subscriber -> {
            Query<ProductPosition> productPositionQuery = mDaoSession.getProductPositionDao().queryBuilder()
                    .where(ProductPositionDao.Properties.SubCategoryId.eq(subCategory.getId())).build();
            List<ProductPosition> productPositions = productPositionQuery.list();
            if (productPositions.size() == 0) {
                subscriber.onNext(subCategory.getProducts());
                subscriber.onComplete();
                return;
            }
            Collections.sort(productPositions, (productPosition, t1) -> productPosition.getPosition().compareTo(t1.getPosition()));
            List<Product> productList = new ArrayList<>();
            for (ProductPosition productPosition : productPositions) {
                productList.add(productPosition.getProduct());
            }
            subscriber.onNext(productList);
            subscriber.onComplete();
        });
    }


    @Override
    public Observable<Long> insertOrReplaceCategoryPosition(CategoryPosition position) {
        return Observable.fromCallable(() -> mDaoSession.getCategoryPositionDao().insertOrReplace(position));
    }*/

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
        return Observable.fromCallable(() -> mDaoSession.getCurrencyDao().insertOrReplace(currency));
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
    public Boolean isPaymentTypeNameExists(String name) {
        return !mDaoSession.getPaymentTypeDao().queryBuilder().where(PaymentTypeDao.Properties.Name.eq(name)).build().list().isEmpty();
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

    /*@Override
    public Observable<Boolean> insertSubCategoryPositions(List<SubCategoryPosition> positionList, Category category) {
        return Observable.fromCallable(() ->
        {
            DeleteQuery<SubCategoryPosition> deleteQuery = mDaoSession.getSubCategoryPositionDao().queryBuilder().
                    where(SubCategoryPositionDao.Properties.CategoryId.eq(category.getId())).buildDelete();
            deleteQuery.executeDeleteWithoutDetachingEntities();
            mDaoSession.clear();
            mDaoSession.getSubCategoryPositionDao().insertOrReplaceInTx(positionList);
            return true;
        });
    }

    @Override
    public Observable<List<SubCategory>> getAllSubCategoryPositions(Category category) {
        return Observable.create(subscriber -> {
            Query<SubCategoryPosition> subCategoryPositionQuery = mDaoSession.getSubCategoryPositionDao().queryBuilder()
                    .where(SubCategoryPositionDao.Properties.CategoryId.eq(category.getId())).build();
            List<SubCategoryPosition> subCategoryPositions = subCategoryPositionQuery.list();
            if (subCategoryPositions.size() == 0) {
                subscriber.onNext(category.getSubCategories());
                subscriber.onComplete();
                return;
            }
            Collections.sort(subCategoryPositions, (subCategoryPosition, t1) -> subCategoryPosition.getPosition().compareTo(t1.getPosition()));
            List<SubCategory> subCategoryList = new ArrayList<>();
            for (SubCategoryPosition subCategoryPosition : subCategoryPositions) {
                subCategoryList.add(subCategoryPosition.getSubCategory());
            }
            subscriber.onNext(subCategoryList);
            subscriber.onComplete();
        });
    }*/


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

    /*@Override
    public Observable<Long> insertOrReplaceRecipe(Recipe recipe) {
        return Observable.fromCallable(() -> mDaoSession.getRecipeDao().insertOrReplace(recipe));
    }

    @Override
    public Observable<List<Recipe>> getAllRecipe() {
        return Observable.fromCallable(() -> mDaoSession.getRecipeDao().loadAll());
    }

    @Override
    public Observable<Boolean> deleteRecipe(Recipe recipe) {
        return Observable.fromCallable(() -> {
            mDaoSession.getRecipeDao().delete(recipe);
            return true;
        });
    }

    @Override
    public Observable<Long> insertAttribute(Attribute attribute) {
        return Observable.fromCallable(() -> mDaoSession.getAttributeDao().insertOrReplace(attribute));
    }

    @Override
    public Observable<Boolean> insertAttributes(List<Attribute> attributes) {
        return Observable.fromCallable(() -> {
            mDaoSession.getAttributeDao().insertOrReplaceInTx(attributes);

            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteAttribute(Attribute attribute) {
        return Observable.fromCallable(() -> {
            mDaoSession.getAttributeDao().delete(attribute);
            return true;
        });
    }

    @Override
    public Observable<List<Attribute>> getAllAttributes() {
        return Observable.fromCallable(() -> mDaoSession.getAttributeDao().loadAll());
    }

    @Override
    public Observable<Long> insertAttributeType(AttributeType attributeType) {
        return Observable.fromCallable(() -> mDaoSession.getAttributeTypeDao().insertOrReplace(attributeType));
    }

    @Override
    public Observable<Boolean> insertAttributeTypes(List<AttributeType> attributeTypes) {
        return Observable.fromCallable(() -> {
            mDaoSession.getAttributeTypeDao().insertOrReplaceInTx(attributeTypes);
            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteAttributeType(AttributeType attributeType) {
        return Observable.fromCallable(() -> {
            mDaoSession.getAttributeTypeDao().delete(attributeType);
            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteAttributeTypeByName(String name) {
        return Observable.fromCallable(() -> {
            DeleteQuery<AttributeType> deleteQuery = mDaoSession.getAttributeTypeDao().queryBuilder().
                    where(AttributeTypeDao.Properties.Name.eq(name)).buildDelete();
            deleteQuery.executeDeleteWithoutDetachingEntities();
            mDaoSession.clear();
            return true;
        });
    }

    @Override
    public Observable<List<AttributeType>> getAllAttributeTypes() {
        return Observable.fromCallable(() -> mDaoSession.getAttributeTypeDao().loadAll());
    }

    @Override
    public Observable<Long> insertChildAttribute(ChildAttribute childAttribute) {
        return Observable.fromCallable(() -> mDaoSession.getChildAttributeDao().insertOrReplace(childAttribute));
    }

    @Override
    public Observable<Boolean> insertChildAttributes(List<ChildAttribute> childAttributes) {
        return Observable.fromCallable(() -> {
            mDaoSession.getChildAttributeDao().insertOrReplaceInTx(childAttributes);
            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteChildAttribute(ChildAttribute childAttribute) {
        return Observable.fromCallable(() -> {
            mDaoSession.getChildAttributeDao().delete(childAttribute);
            return true;
        });
    }

    @Override
    public Observable<List<ChildAttribute>> getAllChildAttributes() {
        return Observable.fromCallable(() -> mDaoSession.getChildAttributeDao().loadAll());
    }

    @Override
    public Observable<Long> insertParentAttribute(ParentAttribute parentAttribute) {
        return Observable.fromCallable(() -> mDaoSession.getParentAttributeDao().insertOrReplace(parentAttribute));
    }

    @Override
    public Observable<Boolean> insertParentAttributes(List<ParentAttribute> parentAttributes) {
        return Observable.fromCallable(() -> {
            mDaoSession.getParentAttributeDao().insertOrReplaceInTx(parentAttributes);
            return true;
        });
    }

    @Override
    public Observable<Boolean> deleteParentAttribute(ParentAttribute parentAttribute) {
        return Observable.fromCallable(() -> {
            mDaoSession.getParentAttributeDao().delete(parentAttribute);
            return true;
        });
    }

    @Override
    public Observable<List<ParentAttribute>> getAllParentAttributes() {
        return Observable.fromCallable(() -> mDaoSession.getParentAttributeDao().loadAll());
    }*/

    @Override
    public Boolean isAccountNameExists(String name) {
        return !mDaoSession.getAccountDao()
                .queryBuilder()
                .where(AccountDao.Properties.Name.eq(name))
                .build()
                .list()
                .isEmpty();
    }


    @Override
    public Observable<Integer> getCategoryByName(Category category) {
        return Observable.fromCallable(() -> {
            Query<Category> categoryQuery = mDaoSession.getCategoryDao().queryBuilder()
                    .where(CategoryDao.Properties.Name.eq(category.getName()), CategoryDao.Properties.IsDeleted.eq(false)).build();
            if (categoryQuery.list().isEmpty()) {
                return 0;
            } else if (categoryQuery.list().get(0).getId().equals(category.getId())) {
                return 1;
            } else return 2;
        });
    }

    @Override
    public Observable<Boolean> getMatchCategory(Category category) {
        return Observable.fromCallable(() -> {
            Query<Category> categoryQuery = mDaoSession.getCategoryDao().queryBuilder()
                    .where(CategoryDao.Properties.Name.eq(category.getName()), CategoryDao.Properties.IsDeleted.eq(false)).build();
            return categoryQuery.list().isEmpty();
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

    /*@Override
    public Observable<List<ServiceFee>> getAllServiceFees() {
        return Observable.fromCallable(() -> {
            List<ServiceFee> serviceFees = mDaoSession.getServiceFeeDao().loadAll();
            Collections.sort(serviceFees, (o1, o2) -> o2.getIsActive().compareTo(o1.getIsActive()));

            return serviceFees;
        });
    }*/

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
        return Observable.fromCallable(() -> {
            List<Customer> customers = mDaoSession.getCustomerDao().loadAll();
            Collections.reverse(customers);

            return customers;
        });
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
        return Observable.fromCallable(() -> {
            List<CustomerGroup> customerGroups = mDaoSession.getCustomerGroupDao().loadAll();
            Collections.reverse(customerGroups);

            return customerGroups;
        });
    }

    /*@Override
    public Observable<List<CustomerGroup>> getCustomerGroups(Customer customer) {
        return Observable.fromCallable(() -> {
            //String s = "SELECT a.* FROM CUSTOMER_GROUP a JOIN JoinCustomerGroupsWithCustomersOperations b ON a.ID = b.CUSTOMER_GROUP_ID WHERE b.CUSTOMER_ID = ?";
            String query = "SELECT * FROM CUSTOMER_GROUP JOIN (SELECT * FROM JOIN_CUSTOMER_GROUPS_WITH_CUSTOMERS WHERE CUSTOMER_ID = ?) AS TEMP ON (CUSTOMER_GROUP.ID = TEMP.CUSTOMER_GROUP_ID)";

            Cursor cursor = mDaoSession.getDatabase().rawQuery(query, new String[]{customer.getId()});

            List<CustomerGroup> customerGroups = new ArrayList<>();

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                while (!cursor.isAfterLast()) {
                    CustomerGroup customerGroup = new CustomerGroup();
                    customerGroup.setId(cursor.getString(cursor.getColumnIndex("CUSTOMER_GROUP_ID")));
                    customerGroup.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                    customerGroup.setServiceFeeId(cursor.getString(cursor.getColumnIndex("SERVICE_FEE_ID")));
                    customerGroup.setDiscountId(cursor.getString(cursor.getColumnIndex("DISCOUNT_ID")));

                    int iActive = cursor.getInt(cursor.getColumnIndex("IS_ACTIVE"));
                    boolean isActive = iActive != 0;
                    customerGroup.setIsActive(isActive);

                    customerGroups.add(customerGroup);

                    cursor.moveToNext();
                }
            }
            return customerGroups;
        });
    }*/

    //TODO FIX
    @Override
    public Observable<List<ServiceFee>> getAllServiceFees() {
        return null;
    }

    @Override
    public Observable<List<CustomerGroup>> getCustomerGroups(Customer customer) {
        return null;
    }
}
