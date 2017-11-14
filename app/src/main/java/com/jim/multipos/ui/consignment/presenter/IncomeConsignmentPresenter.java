package com.jim.multipos.ui.consignment.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.products.Product;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public interface IncomeConsignmentPresenter extends Presenter {
    void loadVendorProducts();
    void setData();
    void setConsignmentItem(Product product);
    void saveConsignment(String number, String description, String totalAmount, boolean checked, int selectedPosition);
    void deleteFromList(ConsignmentProduct consignmentProduct);
    void getAccounts();
    void calculateConsignmentSum();
}
