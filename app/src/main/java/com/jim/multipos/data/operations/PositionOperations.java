package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.intosystem.CategoryPosition;
import com.jim.multipos.data.db.model.intosystem.ProductPosition;
import com.jim.multipos.data.db.model.intosystem.SubCategoryPosition;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.SubCategory;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by DEV on 22.08.2017.
 */

public interface PositionOperations {

    Observable<Boolean> addCategoryPositions(List<CategoryPosition> positionList );
    Observable<List<Category>> getAllCategoryPositions();
    Observable<Long> replaceCategoryPosition(CategoryPosition position);
    Observable<Boolean> addSubCategoryPositions(List<SubCategoryPosition> positionList, Category category);
    Observable<List<SubCategory>> getAllSubCategoryPositions(Category category);
    Observable<Boolean> addProductPositions(List<ProductPosition> positionList, SubCategory subCategory);
    Observable<List<Product>> getAllProductPositions(SubCategory subCategory);
}
