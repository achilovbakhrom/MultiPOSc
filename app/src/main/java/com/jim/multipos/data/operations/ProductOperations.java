package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.products.Product;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by DEV on 30.08.2017.
 */

public interface ProductOperations {
    Observable<Long> addProduct(Product product);
    Observable<Boolean> addProduct(List<Product> productList);
    Observable<Long> replaceProduct(Product product);
    Observable<List<Product>> getAllProducts();

}
