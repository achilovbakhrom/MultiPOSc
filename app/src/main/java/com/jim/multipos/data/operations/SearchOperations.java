package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.Vendor;
import com.jim.multipos.data.db.model.products.Product;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by bakhrom on 10/23/17.
 */

public interface SearchOperations {
    Single<List<Product>> getSearchProducts(String searchText, boolean skuMode, boolean barcodeMode,boolean nameMode);

}
