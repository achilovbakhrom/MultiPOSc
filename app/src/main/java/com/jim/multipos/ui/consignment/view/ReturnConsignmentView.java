package com.jim.multipos.ui.consignment.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.List;

/**
 * Created by Sirojiddin on 24.11.2017.
 */

public interface ReturnConsignmentView extends BaseView {
    void setTotalProductsSum(double sum);
    void fillDialogItems(List<Product> productList, List<Product> vendorProducts);
    void setError(String string);
    void setVendorName(String name);
    void openDiscardDialog();
    void closeFragment(Vendor vendor);
    void setConsignmentNumberError();
    void setConsignmentNumber(int number);
    void setCurrency(String abbr);
    void fillReturnList(List<OutcomeProduct> outcomeProduct);
    void openCustomStockPositionsDialog(OutcomeProduct outcomeProduct, List<OutcomeProduct> outcomeProductList, List<OutcomeProduct> exceptionList, int position);
    void updateChangedPosition(int position);
    void closeFragment();
}
