package com.jim.multipos.ui.consignment.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.products.Product;

import java.util.List;

/**
 * Created by Sirojiddin on 24.11.2017.
 */

public interface ReturnConsignmentView extends BaseView {
    void fillReturnList(List<ConsignmentProduct> consignmentProductList);
    void setTotalProductsSum(double sum);
    void fillDialogItems(List<Product> productList);
    void setError();
    void setVendorName(String name);
}
