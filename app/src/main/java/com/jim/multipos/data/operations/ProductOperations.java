package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.VendorProductCon;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;


/**
 * Created by DEV on 30.08.2017.
 */

public interface ProductOperations {
    Observable<Long> addProduct(Product product);
    Observable<Boolean> addProduct(List<Product> productList);
    Observable<Long> replaceProduct(Product product);
    Observable<List<Product>> getAllProducts();
    Single<List<Product>> getAllActiveProducts(Category category);
    Observable<Integer> getAllProductCount(Category category);
    Observable<Boolean> isProductNameExists(String productName, Long categoryId);
    Observable<Boolean> removeProduct(Product product);
    Observable<Product> getProductById(Long productId);
    Observable<Long> addVendorProductConnection(VendorProductCon vendorProductCon);
    Observable<Boolean> removeVendorProductConnection(VendorProductCon vendorProductCon);
    Observable<Boolean> removeVendorProductConnectionByVendorId(Long vendorId);
    Observable<Boolean> removeVendorProductConnectionByProductId(Long productId);
    Observable<List<VendorProductCon>> getVendorProductConnectionByProductId(Long productId);
    Observable<VendorProductCon> getVendorProductConnectionById(Long productId, Long vendorId);
}
