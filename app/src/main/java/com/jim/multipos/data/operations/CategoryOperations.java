package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.products.Category;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by DEV on 16.08.2017.
 */

public interface CategoryOperations {
    Observable<Long> addCategory(Category category);
    Observable<Long> addSubCategory(Category subcategory);
    Observable<Boolean> addCategory(List<Category> categoryList);
    Observable<Long> replaceCategory(Category category);
    Observable<List<Category>> getAllCategories();
    Single<List<Category>> getAllActiveCategories();
    Single<List<Category>> getAllActiveSubCategories(Category parent);
    Observable<List<Category>> getSubCategories(Category category);
    Observable<Category> getCategoryByName(String name);
    Observable<Boolean> isCategoryNameExists(String name);
    Observable<Category> getSubCategoryByName(String category, Long id);
    Observable<Boolean> isSubCategoryNameExists(String parentName, String name);
    Observable<Category> getCategoryById(Long id);
    Observable<Boolean> removeCategory(Category category);
    Observable<List<Category>> getActiveCategories();
}
