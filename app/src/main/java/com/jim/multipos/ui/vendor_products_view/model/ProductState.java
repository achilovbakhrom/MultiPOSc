package com.jim.multipos.ui.vendor_products_view.model;

import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

import lombok.Data;

/**
 * Created by Sirojiddin on 01.03.2018.
 */
@Data
public class ProductState {
    private Product product;
    private Vendor vendor;
    private Double value;
}
