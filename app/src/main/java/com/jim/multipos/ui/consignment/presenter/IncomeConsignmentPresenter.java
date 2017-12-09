package com.jim.multipos.ui.consignment.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.products.Product;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public interface IncomeConsignmentPresenter extends Presenter {
    void loadVendorProducts();
    void setData(Long productId, Long vendorId, Long consignmentId);
    void setConsignmentItem(Product product);
    void saveConsignment(String number, String description, String totalAmount, String paidSum, boolean checked, int selectedPosition);
    void deleteFromList(ConsignmentProduct consignmentProduct);
    void getAccounts();
    void calculateConsignmentSum();
    void checkChanges(String number, String description, String totalPaid, boolean checked, int selectedPosition);
    void saveChanges();
}
