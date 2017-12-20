package com.jim.multipos.data.db.model.order;

import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import lombok.Data;

/**
 * Created by developer on 20.12.2017.
 */
@Entity(nameInDb = "ORDERPRODUCT", active = true)
@Data
public class OrderProduct {
    @Id(autoincrement = true)
    private Long id;
    private long orderId;

    private long productId;
    @ToOne(joinProperty = "productId")
    private Product product;

    private long vendorId;
    @ToOne(joinProperty = "vendorId")
    private Vendor vendor;

    private double cost;
    private double price;
    private int count;
    private double sum;
    private double discountValue;
    private double serviceValue;
    private String discription;

    private long discountId;
    @ToOne(joinProperty = "discountId")
    private Discount discount;

    private long serviceFeeId;
    @ToOne(joinProperty = "serviceFeeId")
    private ServiceFee serviceFee;

}
