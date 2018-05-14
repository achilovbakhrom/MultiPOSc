package com.jim.multipos.ui.consignment.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.List;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public interface IncomeConsignmentView extends BaseView {
    void fillDialogItems(List<Product> productList);
    void fillConsignmentProductList(List<ConsignmentProduct> consignmentProductList, int type);
    void setVendorName(String name);
    void fillAccountsList(List<String> accountList);
    void setConsignmentSumValue(double sum);
    void setError(String s);
    void openDiscardDialog();
    void openSaveChangesDialog();
    void closeFragment(Vendor vendor);
    void fillConsignmentData(String consignmentNumber, String description, Boolean isFromAccount, double amount);
    void setAccountSpinnerSelection(int selectedAccount);
    void setConsignmentNumberError();
    void setConsignmentNumber(int number);
    void setCurrency(String abbr);
}
