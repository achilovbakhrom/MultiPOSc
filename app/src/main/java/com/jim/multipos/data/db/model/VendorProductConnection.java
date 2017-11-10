package com.jim.multipos.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Achilov Bakhrom on 11/10/17.
 */
@Entity(nameInDb = "VENDOR_PRODUCT_CONNECTION")
public class VendorProductConnection implements Serializable {
    @Id(autoincrement = true)
    Long id;
    private Long productId;
    private Long vendorId;
    @Generated(hash = 1813949627)
    public VendorProductConnection(Long id, Long productId, Long vendorId) {
        this.id = id;
        this.productId = productId;
        this.vendorId = vendorId;
    }
    @Generated(hash = 2042696388)
    public VendorProductConnection() {
    }
    public Long getVendorId() {
        return this.vendorId;
    }
    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
    public Long getProductId() {
        return this.productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
