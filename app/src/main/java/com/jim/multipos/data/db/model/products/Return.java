package com.jim.multipos.data.db.model.products;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.PaymentTypeDao;

/**
 * Created by Sirojiddin on 06.01.2018.
 */
@Entity(active = true, nameInDb = "RETURN")
public class Return {
    @Id(autoincrement = true)
    private Long id;
    private Long productId;
    @ToOne(joinProperty = "productId")
    private Product product;
    private Long vendorId;
    @ToOne(joinProperty = "vendorId")
    private Vendor vendor;
    private double quantity;
    private double returnAmount;
    private String description = "";
    private Long paymentTypeId;
    @ToOne(joinProperty = "paymentTypeId")
    private PaymentType paymentType;
    private Long createAt;
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 558738496)
    public void setProduct(Product product) {
        synchronized (this) {
            this.product = product;
            productId = product == null ? null : product.getId();
            product__resolvedKey = productId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1198864293)
    public Product getProduct() {
        Long __key = this.productId;
        if (product__resolvedKey == null || !product__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProductDao targetDao = daoSession.getProductDao();
            Product productNew = targetDao.load(__key);
            synchronized (this) {
                product = productNew;
                product__resolvedKey = __key;
            }
        }
        return product;
    }
    @Generated(hash = 587652864)
    private transient Long product__resolvedKey;
    /** Used for active entity operations. */
    @Generated(hash = 1408294100)
    private transient ReturnDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    @Generated(hash = 1022035388)
    private transient Long vendor__resolvedKey;
    @Generated(hash = 1438380965)
    private transient Long paymentType__resolvedKey;
    public Double getReturnAmount() {
        return this.returnAmount;
    }
    public void setReturnAmount(double returnAmount) {
        this.returnAmount = returnAmount;
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
    public double getQuantity() {
        return this.quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 606511807)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getReturnDao() : null;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 332557200)
    public void setVendor(Vendor vendor) {
        synchronized (this) {
            this.vendor = vendor;
            vendorId = vendor == null ? null : vendor.getId();
            vendor__resolvedKey = vendorId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1192552702)
    public Vendor getVendor() {
        Long __key = this.vendorId;
        if (vendor__resolvedKey == null || !vendor__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VendorDao targetDao = daoSession.getVendorDao();
            Vendor vendorNew = targetDao.load(__key);
            synchronized (this) {
                vendor = vendorNew;
                vendor__resolvedKey = __key;
            }
        }
        return vendor;
    }
    public Long getVendorId() {
        return this.vendorId;
    }
    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getCreateAt() {
        return this.createAt;
    }
    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1351411560)
    public void setPaymentType(PaymentType paymentType) {
        synchronized (this) {
            this.paymentType = paymentType;
            paymentTypeId = paymentType == null ? null : paymentType.getId();
            paymentType__resolvedKey = paymentTypeId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2041238771)
    public PaymentType getPaymentType() {
        Long __key = this.paymentTypeId;
        if (paymentType__resolvedKey == null || !paymentType__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PaymentTypeDao targetDao = daoSession.getPaymentTypeDao();
            PaymentType paymentTypeNew = targetDao.load(__key);
            synchronized (this) {
                paymentType = paymentTypeNew;
                paymentType__resolvedKey = __key;
            }
        }
        return paymentType;
    }
    public Long getPaymentTypeId() {
        return this.paymentTypeId;
    }
    public void setPaymentTypeId(Long paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }
    @Generated(hash = 221520427)
    public Return() {
    }
    @Generated(hash = 1355928296)
    public Return(Long id, Long productId, Long vendorId, double quantity, double returnAmount,
            String description, Long paymentTypeId, Long createAt) {
        this.id = id;
        this.productId = productId;
        this.vendorId = vendorId;
        this.quantity = quantity;
        this.returnAmount = returnAmount;
        this.description = description;
        this.paymentTypeId = paymentTypeId;
        this.createAt = createAt;
    }
}
