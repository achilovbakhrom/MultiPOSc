package com.jim.multipos.ui.consignment.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.intosystem.StockQueueItem;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.products.Product;

import java.util.List;

/**
 * Created by Sirojiddin on 24.11.2017.
 */

public interface ReturnConsignmentPresenter extends Presenter{
    void setData(Long productId, Long vendorId);
    void setReturnItem(Product product);
    void deleteFromList(int position);
    void calculateConsignmentSum();
    void loadVendorProducts();
    void saveReturnConsignment(String number, String description);
    void checkChanges(String number, String des);
    void onBarcodeScanned(String barcode);
    void openCustomStockPositionsDialog(OutcomeProduct outcomeProduct, int position);
    void setOutcomeProduct(OutcomeProduct outcomeProduct);
}
