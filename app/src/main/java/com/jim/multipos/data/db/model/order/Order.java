package com.jim.multipos.data.db.model.order;

import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.products.Vendor;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;

import lombok.Data;

/**
 * Created by developer on 20.12.2017.
 */
@Entity(nameInDb = "ORDER", active = true)
@Data
public class Order {
    @Id(autoincrement = true)
    private Long id;
    private long createAt;
    private int status;
    private double subTotalValue;
    private double serviceTotalValue;
    private double discountTotalValue;
    private double orderServiceValue;
    private double orderDiscountValue;
    private double tips;
    private double totalPayed;
    private double toDebtValue;

    private long customer_id;
    @ToOne(joinProperty = "customer_id")
    private Customer customer;
    private long service_id;
    @ToOne(joinProperty = "service_id")
    private ServiceFee serviceFee;

    @ToMany(joinProperties = {
            @JoinProperty(
                    name = "id", referencedName = "orderId"
            )
    })
    private List<OrderProduct> orderProducts;

    @ToMany(joinProperties = {
            @JoinProperty(
                    name = "id", referencedName = "orderId"
            )
    })
    private List<PayedPartitions> payedPartitions;



}
