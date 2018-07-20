package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.unit.UnitCategory;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by user on 18.08.17.
 */

public interface UnitCategoryOperations {
    Observable<Long> addUnitCategory(UnitCategory category);

    Observable<Boolean> addUnitCategories(List<UnitCategory> categoryList);

    Observable<List<UnitCategory>> getAllUnitCategories();
}
