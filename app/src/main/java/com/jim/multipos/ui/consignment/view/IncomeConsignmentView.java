package com.jim.multipos.ui.consignment.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.List;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public interface IncomeConsignmentView extends BaseView {
    void fillDialogItems(List<Product> productList, List<Product> vendorProducts);
    void setVendorName(String name);
    void fillAccountsList(List<String> accountList);
    void setConsignmentSumValue(double sum);
    void setError(String s);
    void openDiscardDialog();
    void closeFragment(Vendor vendor);
    void setInvoiceNumberError();
    void setInvoiceNumber(int number);
    void setCurrency(String abbr);
    void fillInvoiceProductList(List<IncomeProduct> incomeProductList);
    void openSettingsDialog(IncomeProduct incomeProduct, StockQueue stockQueue, int position);
}
