package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.intosystem.OutcomeWithDetials;
import com.jim.multipos.data.db.model.intosystem.StockQueueItem;
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderProduct;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.ui.reports.stock_operations.model.OperationSummaryItem;
import com.jim.multipos.ui.reports.stock_state.module.InventoryItemReport;
import com.jim.multipos.ui.vendor_products_view.model.ProductState;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
    OutcomeProduct insertAndFillOutcomeProduct(OutcomeWithDetials outcomeWithDetials); //+

    Single<List<ProductState>> getVendorProductsWithStates(Long vendorId);

    //ORDER EDIT QILINGANI TASDIQLANSA ESKI ORDERNI OUTCOMELARI OTMENA BO'LISHI KERAK
    Single<Integer> cancelOutcomeProductWhenOrderProductCanceled(List<OrderProduct> orderProducts); //+

    Single<List<OutcomeWithDetials>> checkPositionAvailablity(List<OutcomeProduct> outcomeProducts); //+
    Single<List<OutcomeWithDetials>> checkPositionAvailablityWithoutSomeOutcomes(List<OutcomeProduct> outcomeProducts,List<OutcomeProduct> withoutOutcomeProducts); //+

    Single<OutcomeWithDetials> checkPositionAvailablity(OutcomeProduct outcomeProduct);

    Single<IncomeProduct> insertIncomeProduct(IncomeProduct incomeProduct);
    Single<StockQueue> insertStockQueue(StockQueue stockQueue);
    //TODO VOQTINCHALI
    Single<Double> getProductInvenotry(Long productId); //+

    Single<List<InventoryItem>> getProductInventoryStatesForNow();

    Single<List<InventoryItemReport>> getInventoryWithSummaryCost();
    Single<List<InventoryItemReport>> getInventoryVendorWithSummaryCost();

    Single<InventoryItem> setLowStockAlert(InventoryItem inventoryItem,double newAlertCount);

    Single<List<StockQueue>> getStockQueuesByProductId(Long id);
    Single<List<StockQueue>> getAllStockQueuesByProductId(Long productId);

    Single<Double> getAvailableCountForProduct(Long id);
    Single<List<StockQueueItem>> getStockQueueItemForOutcomeProduct(OutcomeProduct outcomeProduct, List<OutcomeProduct> outcomeProductList, List<OutcomeProduct> exceptionList);
    Single<List<StockQueue>> getAllStockQueuesByVendorId(Long vendorId);
    Single<List<StockQueue>> getAllStockQueuesByProductIdInInterval(Long productId, Calendar fromDate, Calendar toDate);
    Single<List<StockQueue>> getAllStockQueuesByVendorIdInInterval(Long vendorId, Calendar fromDate, Calendar toDate);
    Single<List<StockQueue>> getExpiredStockQueue();
    Single<List<OutcomeProduct>> updateOutcomeProduct(List<OutcomeProduct> outcomeProducts);
    Single<List<OperationSummaryItem>> getOperationsSummary(Date fromDate, Date toDate);
    Single<List<OutcomeProduct>> getOutcomeProductsForPeriod(Calendar fromDate,Calendar toDate);
    Single<List<IncomeProduct>> getIncomeProductsForPeriod(Calendar fromDate, Calendar toDate);
    Single<List<StockQueue>> getStockQueueForPeriod(Calendar fromDate, Calendar toDate);
    Single<List<StockQueue>> getStockQueueUsedForPeriod(Calendar fromDate, Calendar toDate);

}
