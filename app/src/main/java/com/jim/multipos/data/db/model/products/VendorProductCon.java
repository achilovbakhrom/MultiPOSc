package com.jim.multipos.data.db.model.products;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Achilov Bakhrom on 11/10/17.
 */
@Entity(nameInDb = "VENDOR_PRODUCT_CONNECTION")
public class VendorProductCon {
    @Id(autoincrement = true)
    Long id;
    private Long productId;
    private Long vendorId;
    private Double cost;

    @Generated(hash = 1743435100)
    public VendorProductCon(Long id, Long productId, Long vendorId, Double cost) {
        this.id = id;
        this.productId = productId;
        this.vendorId = vendorId;
        this.cost = cost;
    }
    @Generated(hash = 1840350928)
    public VendorProductCon() {
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
    public void setCost(Double cost) {
        this.cost = cost;
    }
    public Double getCost() {
        return this.cost;
    }
}
