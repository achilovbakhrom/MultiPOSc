package com.jim.multipos.data.db.model.order;

import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.products.Vendor;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import lombok.Data;

/**
 * Created by developer on 20.12.2017.
 */
@Entity(nameInDb = "PAYEDPARTITIONS", active = true)
@Data
public class PayedPartitions {
    @Id(autoincrement = true)
    private Long id;
    private long orderId;

    private long paymentId;
    @ToOne(joinProperty = "paymentId")
    private PaymentType paymentType;

    private double value;
}
