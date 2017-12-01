package com.jim.multipos.ui.consignment.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.products.Product;

import java.util.List;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public interface IncomeConsignmentView extends BaseView {
    void fillDialogItems(List<Product> productList);
    void fillConsignmentProductList(List<ConsignmentProduct> consignmentProductList);
    void setVendorName(String name);
    void fillAccountsList(List<String> accountList);
    void setConsignmentSumValue(double sum);
    void setError();
    void openDiscardDialog();
    void closeFragment();
    void fillConsignmentData(String consignmentNumber, String description, Boolean isFromAccount, double amount);
    void setAccountSpinnerSelection(int selectedAccount);
}
