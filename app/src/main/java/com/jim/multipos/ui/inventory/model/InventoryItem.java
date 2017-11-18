package com.jim.multipos.ui.inventory.model;

import com.jim.multipos.data.db.model.products.Product;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by developer on 10.11.2017.
 */
@Data
public class InventoryItem implements Serializable {
    long id;
    Product product;
    double inventory;
    double lowStockAlert;

    public Double getLowStockAlert() {
        return lowStockAlert;
    }

    public Double getInventory() {
        return inventory;
    }

}
