package com.jim.multipos.data.db.model.inventory;

import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.products.Vendor;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import lombok.Data;

/**
 * Created by developer on 27.11.2017.
 */
@Entity(nameInDb = "BILLINGOPERATION", active = true)
@Data
public class BillingOperations {
    @Id(autoincrement = true)
    private Long id;
    private Long accountId;
    @ToOne(joinProperty = "accountId")
    private Account account;
    private Long vendorId;
    @ToOne(joinProperty = "vendorId")
    private Vendor vendor;
    private Long consigmentId;
    @ToOne(joinProperty = "consigmentId")
    private Consigment consigment;
    private double ammount;
    private long createAt;
    private String operationType;
    private String description;
}

