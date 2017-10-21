package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.products.Category;

import java.util.List;

import io.reactivex.Observable;
/**
 * Created by DEV on 16.08.2017.
 */

public interface CategoryOperations {
    Observable<Long> addCategory(Category category);
    Observable<Long> addSubCategory(Category subcategory);
    Observable<Boolean> addCategory(List<Category> categoryList);
    Observable<Long> replaceCategory(Category category);
    Observable<List<Category>> getAllCategories();
    Observable<List<Category>> getSubCategories(Category category);
    Observable<Integer> getCategoryByName(Category category);
    Observable<Boolean> isCategoryNameExists(String name);
    Observable<Integer> getSubCategoryByName(Category category);
    Observable<Boolean> isSubCategoryNameExists(Category parent);
}
