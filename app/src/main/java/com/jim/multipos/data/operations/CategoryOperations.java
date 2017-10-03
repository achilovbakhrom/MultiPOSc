package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.products.Category;

import java.util.List;

import io.reactivex.Observable;
/**
 * Created by DEV on 16.08.2017.
 */

public interface CategoryOperations {
    Observable<Long> addCategory(Category category);
    Observable<Boolean> addCategory(List<Category> categoryList);
    Observable<Long> replaceCategory(Category category);
    Observable<List<Category>> getAllCategories();
    Observable<Category> getCategoryByName(Category category);
    Observable<Category> getMatchCategories(Category category, String temp);
}
