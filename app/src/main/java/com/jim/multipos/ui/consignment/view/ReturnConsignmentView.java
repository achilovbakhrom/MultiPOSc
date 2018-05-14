package com.jim.multipos.ui.consignment.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.List;

/**
 * Created by Sirojiddin on 24.11.2017.
 */

public interface ReturnConsignmentView extends BaseView {
    void fillReturnList(List<ConsignmentProduct> consignmentProductList, int type);
    void setTotalProductsSum(double sum);
    void fillDialogItems(List<Product> productList);
    void setError(String string);
    void setVendorName(String name);
    void openDiscardDialog();
    void closeFragment(Vendor vendor);
    void fillConsignmentData(String consignmentNumber, String description);
    void openSaveChangesDialog();
    void setConsignmentNumberError();
    void setConsignmentNumber(int number);
    void setCurrency(String abbr);
}
