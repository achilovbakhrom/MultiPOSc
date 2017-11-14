package com.jim.multipos.data.db.model.consignment;

import com.jim.multipos.data.db.model.products.Product;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.products.ProductDao;


/**
 * Created by Sirojiddin on 10.11.2017.
 */
@Entity(nameInDb = "CONSIGNMENT_PRODUCTS", active = true)
public class ConsignmentProduct {
    @Id
    private Long id;
    private Double costValue;
    private Double countValue;
    private Long productId;
    @ToOne(joinProperty = "productId")
    private Product product;
    private Long consignmentId;
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
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 321610097)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getConsignmentProductDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 2018828875)
    private transient ConsignmentProductDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getConsignmentId() {
        return this.consignmentId;
    }
    public void setConsignmentId(Long consignmentId) {
        this.consignmentId = consignmentId;
    }
    public Long getProductId() {
        return this.productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Double getCountValue() {
        return this.countValue;
    }
    public void setCountValue(Double countValue) {
        this.countValue = countValue;
    }
    public Double getCostValue() {
        return this.costValue;
    }
    public void setCostValue(Double costValue) {
        this.costValue = costValue;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1515128430)
    public ConsignmentProduct(Long id, Double costValue, Double countValue,
            Long productId, Long consignmentId) {
        this.id = id;
        this.costValue = costValue;
        this.countValue = countValue;
        this.productId = productId;
        this.consignmentId = consignmentId;
    }
    @Generated(hash = 724649454)
    public ConsignmentProduct() {
    }

}
