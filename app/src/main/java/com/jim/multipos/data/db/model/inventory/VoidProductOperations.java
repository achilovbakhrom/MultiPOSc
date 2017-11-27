package com.jim.multipos.data.db.model.inventory;

import com.jim.multipos.data.db.model.products.Product;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import lombok.Data;

/**
 * Created by developer on 27.11.2017.
 */
@Entity(nameInDb = "VOIDOPERATIONS", active = true)
@Data
public class VoidProductOperations {
    @Id(autoincrement = true)
    private Long id;
    private Long productId;
    @ToOne(joinProperty = "productId")
    private Product product;
    private Long warehouseId;
    @ToOne(joinProperty = "warehouseId")
    private WarehouseOperations warehouse;
    private double count;
    private double beforeCount;
    private Long createAt;
}
