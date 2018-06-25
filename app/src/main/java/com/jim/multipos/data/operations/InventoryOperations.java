package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.intosystem.OutcomeWithDetials;
import com.jim.multipos.data.db.model.intosystem.ProductWithCount;
import com.jim.multipos.data.db.model.intosystem.StockQueueItem;
import com.jim.multipos.data.db.model.intosystem.StockResult;
import com.jim.multipos.data.db.model.inventory.DetialCount;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderProduct;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.stock.Stock;
import com.jim.multipos.ui.inventory.model.InventoryItem;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Single;

/**
 * Created by bakhrom on 10/23/17.
 */

public interface InventoryOperations {
//    Single<List<InventoryItem>> getInventoryItems();
//    Observable<Long> insertInventoryState(InventoryState inventoryState);
//    Observable<List<InventoryState>> getInventoryStates();
//    Single<Boolean> deleteInventoryState(InventoryState inventoryState);
//    Observable<List<InventoryState>> getInventoryStatesByProductId(Long productId);
//    Observable<List<InventoryState>> getInventoryStatesByVendorId(Long vendorId);
//    Single<Long> insertWarehouseOperation(WarehouseOperations warehouseOperations);
//    Single<WarehouseOperations> getWarehouseOperationById(Long warehouseId);
//    Single<Long> replaceWarehouseOperation(WarehouseOperations warehouseOperations);
//    Single<HistoryInventoryState> insertHistoryInventoryState(HistoryInventoryState state);
//    Single<List<WarehouseOperations>> getWarehouseOperationsInInterval(Calendar fromDate, Calendar toDate);
//    Single<List<HistoryInventoryState>> getHistoryInventoryStatesByTillId(Long id);

    Integer checkProductAvailable(Long productId, double summaryCount,Order ifHaveOldOrder); //+
    List<OutcomeProduct> insertAndFillOutcomeProducts(List<OutcomeWithDetials> outcomeWithDetials); //+

    //ORDER EDIT QILINGANI TASDIQLANSA ESKI ORDERNI OUTCOMELARI OTMENA BO'LISHI KERAK
    Single<Integer> cancelOutcomeProductWhenOrderProductCanceled(List<OrderProduct> orderProducts); //+

    Single<List<OutcomeWithDetials>> checkPositionAvailablity(List<OutcomeProduct> outcomeProducts); //+
    Single<List<OutcomeWithDetials>> checkPositionAvailablityWithoutSomeOutcomes(List<OutcomeProduct> outcomeProducts,List<OutcomeProduct> withoutOutcomeProducts);


    //TODO VOQTINCHALI
    Single<Double> getProductInvenotry(Long productId); //+

    Single<List<InventoryItem>> getProductInventoryStatesForNow();
    Single<InventoryItem> setLowStockAlert(InventoryItem inventoryItem,double newAlertCount);


}
