package com.jim.multipos.data.db.model.inventory;

import com.jim.multipos.data.db.model.products.Product;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import lombok.Data;

/**
 * Created by developer on 27.11.2017.
 */
@Entity(nameInDb = "CONSIGMENTPRODUCT", active = true)
@Data
public class ConsigmentProduct {
    @Id(autoincrement = true)
    private Long id;
    private Long productId;
    @ToOne(joinProperty = "productId")
    private Product product;
    private Long warehouseId;
    @ToOne(joinProperty = "warehouseId")
    private WarehouseOperations warehouse;
    private Long consigmentId;
    @ToOne(joinProperty = "consigmentId")
    private Consigment consigment;
    private double cost;
    private double count;
}
