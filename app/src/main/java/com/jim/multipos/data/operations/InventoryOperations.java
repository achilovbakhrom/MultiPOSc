package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.inventory.WarehouseOperations;
import com.jim.multipos.ui.inventory.model.InventoryItem;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by bakhrom on 10/23/17.
 */

public interface InventoryOperations {
    Single<List<InventoryItem>> getInventoryItems();
    Single<Long> insertWarehouseOperation(WarehouseOperations warehouseOperations);
}
