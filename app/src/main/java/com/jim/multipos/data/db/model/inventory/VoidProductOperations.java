package com.jim.multipos.data.db.model.inventory;

import com.jim.multipos.data.db.model.products.Product;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import lombok.Data;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.products.ProductDao;

/**
 * Created by developer on 27.11.2017.
 */
@Entity(nameInDb = "VOIDOPERATIONS", active = true)
@Data
public class VoidProductOperations {
    @Id(autoincrement = true)
    private Long id;
    private Long productId;
    @ToOne(joinProperty = "productId")
    private Product product;
    private Long warehouseId;
    @ToOne(joinProperty = "warehouseId")
    private WarehouseOperations warehouse;
    private double count;
    private double beforeCount;
    private Long createAt;
    private String discription;
    private int type;
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
    @Generated(hash = 33462667)
    public void setWarehouse(WarehouseOperations warehouse) {
        synchronized (this) {
            this.warehouse = warehouse;
            warehouseId = warehouse == null ? null : warehouse.getId();
            warehouse__resolvedKey = warehouseId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1560568949)
    public WarehouseOperations getWarehouse() {
        Long __key = this.warehouseId;
        if (warehouse__resolvedKey == null || !warehouse__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WarehouseOperationsDao targetDao = daoSession
                    .getWarehouseOperationsDao();
            WarehouseOperations warehouseNew = targetDao.load(__key);
            synchronized (this) {
                warehouse = warehouseNew;
                warehouse__resolvedKey = __key;
            }
        }
        return warehouse;
    }
    @Generated(hash = 1229596798)
    private transient Long warehouse__resolvedKey;
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
    @Generated(hash = 1777898829)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getVoidProductOperationsDao()
                : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 793588533)
    private transient VoidProductOperationsDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getCreateAt() {
        return this.createAt;
    }
    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }
    public double getBeforeCount() {
        return this.beforeCount;
    }
    public void setBeforeCount(double beforeCount) {
        this.beforeCount = beforeCount;
    }
    public double getCount() {
        return this.count;
    }
    public void setCount(double count) {
        this.count = count;
    }
    public Long getWarehouseId() {
        return this.warehouseId;
    }
    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
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
    @Generated(hash = 1594571685)
    public VoidProductOperations(Long id, Long productId, Long warehouseId,
            double count, double beforeCount, Long createAt) {
        this.id = id;
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.count = count;
        this.beforeCount = beforeCount;
        this.createAt = createAt;
    }
    @Generated(hash = 1051827895)
    public VoidProductOperations() {
    }
}
