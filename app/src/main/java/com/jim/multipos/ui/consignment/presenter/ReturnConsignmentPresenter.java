package com.jim.multipos.ui.consignment.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.products.Product;

/**
 * Created by Sirojiddin on 24.11.2017.
 */

public interface ReturnConsignmentPresenter extends Presenter{
    void setData(Long productId, Long vendorId, Long consignmentId);
    void setReturnItem(Product product);
    void deleteFromList(ConsignmentProduct consignmentProduct);
    void calculateConsignmentSum();
    void loadVendorProducts();
    void saveReturnConsignment(String number, String description);
    void checkChanges(String number, String des);
    void saveChanges();
}
