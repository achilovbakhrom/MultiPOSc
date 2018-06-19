package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.intosystem.ProductWithCount;
import com.jim.multipos.data.db.model.intosystem.StockQueueItem;
import com.jim.multipos.data.db.model.intosystem.StockResult;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderProduct;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
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

    Single<StockResult> getProductWithRuleRegister(Long productId, double count);
    Single<StockResult> getProductWithCustomQueueRegister(Long productId, double count,Long stockQueueId);

    Single<StockResult> updateOutcomeRegistredProductCount(OutcomeProduct outcomeProduct,double newCount);

    Single<OutcomeProduct> cancelOutcomeProductWhenHoldedProductReturn(OutcomeProduct outcomeProduct);
    Single<OutcomeProduct> cancelOutcomeProductWhenOrderProductCleared(OutcomeProduct outcomeProduct);
    Single<OutcomeProduct> cancelOutcomeProductWhenOrderProductCanceled(OutcomeProduct outcomeProduct);



    //AFTER CREATING DETIALS STOCK QUEUE SET COUNTS ALSO TO ORDER_PRODUCT AND RETURN IT
    Single<OutcomeProduct> confirmOutcomeProductWhenSold(OutcomeProduct outcomeProduct, OrderProduct orderProduct);
    Single<List<OrderProduct>> confirmOutcomeProductWhenSold(List<OrderProduct> orderProductItems);
    Single<Integer> checkProductHaveInStock(Long productId, double count);
    Single<Double> getProductInvenotry(Long productId);
    Single<Double> getProductInvenotryFromVendor(Long productId,Long vendorId);
    Single<Integer> getAllProductsCountVendor(Long vendorId);

    Single<List<StockQueueItem>> getActualStockQueue(Long productId);
    Single<List<StockQueueItem>> getActualStockQueueWithoutMe(OutcomeProduct outcomeProduct);
    Single<List<StockQueueItem>> getActualyStockQueueList(Calendar from, Calendar to);
    Single<List<StockQueueItem>> getActualyStockQueueListForVendorAndProduct(Long vendorId,Long productId);
    Single<List<StockQueueItem>> getActualyStockQueueListForVendor(Long vendorId);

    Single<List<InventoryItem>> getProductInventoryStatesForNow();
    Single<List<InventoryItem>> getProductInventoryStatesForNowForVendor(Vendor vendor);

    Single<InventoryItem> setLowStockAlert(InventoryItem inventoryItem,double newAlertCount);

    Single<List<Product>> getAllProductsEverGotFromVendor(Long vendorId);
    Single<List<ProductWithCount>> getVendorStateInventory(Long vendorId);

}
