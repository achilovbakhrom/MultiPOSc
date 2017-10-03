package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.SubCategory;

import java.util.List;

import io.reactivex.Observable;
/**
 * Created by DEV on 18.08.2017.
 */

public interface SubCategoryOperations {
    Observable<Long> addSubCategory(SubCategory subCategory);
    Observable<Boolean> addSubCategory(List<SubCategory> subCategoryList);
    Observable<Long> replaceSubCategory(SubCategory subCategory);
    Observable<List<SubCategory>> getAllSubCategories();
    Observable<SubCategory> getSubCategoryByName(SubCategory subCategory);
}
