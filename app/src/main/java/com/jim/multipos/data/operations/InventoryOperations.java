package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.inventory.HistoryInventoryState;
import com.jim.multipos.data.db.model.inventory.InventoryState;
import com.jim.multipos.data.db.model.inventory.WarehouseOperations;
import com.jim.multipos.ui.inventory.model.InventoryItem;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by bakhrom on 10/23/17.
 */

public interface InventoryOperations {
    Single<List<InventoryItem>> getInventoryItems();
    Observable<Long> insertInventoryState(InventoryState inventoryState);
    Observable<List<InventoryState>> getInventoryStates();
    Single<Boolean> deleteInventoryState(InventoryState inventoryState);
    Observable<List<InventoryState>> getInventoryStatesByProductId(Long productId);
    Observable<List<InventoryState>> getInventoryStatesByVendorId(Long vendorId);
    Single<Long> insertWarehouseOperation(WarehouseOperations warehouseOperations);
    Single<WarehouseOperations> getWarehouseOperationById(Long warehouseId);
    Single<Long> replaceWarehouseOperation(WarehouseOperations warehouseOperations);
    Single<HistoryInventoryState> insertHistoryInventoryState(HistoryInventoryState state);
    Single<List<WarehouseOperations>> getWarehouseOperationsInInterval(Calendar fromDate, Calendar toDate);
}
