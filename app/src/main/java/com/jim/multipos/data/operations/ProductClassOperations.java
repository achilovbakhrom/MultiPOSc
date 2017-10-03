package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.ProductClass;

import java.util.List;

import io.reactivex.Single;


/**
 * Created by developer on 29.08.2017.
 */

public interface ProductClassOperations {
    Single<List<ProductClass>> getAllProductClass();
    Single<Long> insertProductClass(ProductClass productClass);
}
