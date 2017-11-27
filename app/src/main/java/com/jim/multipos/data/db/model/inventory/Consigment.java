package com.jim.multipos.data.db.model.inventory;

import com.jim.multipos.data.db.model.products.Vendor;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import lombok.Data;

/**
 * Created by developer on 27.11.2017.
 */
@Entity(nameInDb = "CONSIGMENT", active = true)
@Data
public class Consigment {
    @Id(autoincrement = true)
    private Long id;
    private Long vendorId;
    @ToOne(joinProperty = "vendorId")
    private Vendor vendor;
    private String type;
    private String consigmentNumber;
    private double totalAmmount;
    private String discription;
    private long createAt;
}
