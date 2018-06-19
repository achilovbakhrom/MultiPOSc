package com.jim.multipos.ui.consignment.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.data.db.model.products.Product;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public interface IncomeConsignmentPresenter extends Presenter {
    void loadVendorProducts();
    void setData(Long productId, Long vendorId);
    void setInvoiceItem(Product product);
    void saveInvoice(String number, String description, String totalAmount, String paidSum, boolean checked, int selectedPosition);
    void deleteFromList(int position);
    void getAccounts();
    void calculateInvoiceSum();
    void checkChanges(String number, String description, String totalPaid, boolean checked, int selectedPosition);
    void saveChanges();
    void onBarcodeScanned(String barcode);
    void openSettingsDialogForProduct(IncomeProduct incomeProduct, int position);
    void setConfigsToProduct(IncomeProduct incomeProduct, StockQueue stockQueue, int position);
}
