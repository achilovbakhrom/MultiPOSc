package com.jim.multipos.data.db.model.consignment;

import com.jim.multipos.data.db.model.inventory.WarehouseOperations;
import com.jim.multipos.data.db.model.products.Product;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.products.ProductDao;
import com.jim.multipos.data.db.model.inventory.WarehouseOperationsDao;


/**
 * Created by Sirojiddin on 10.11.2017.
 */
@Entity(nameInDb = "CONSIGNMENT_PRODUCTS", active = true)
public class ConsignmentProduct {
    @Id(autoincrement = true)
    private Long id;
    private Double costValue;
    private Double countValue;
    private Long productId;
    @ToOne(joinProperty = "productId")
    private Product product;
    private Long consignmentId;
    @ToOne(joinProperty = "consignmentId")
    private Consignment consignment;
    private Long warehouseId;
    @ToOne(joinProperty = "warehouseId")
    private WarehouseOperations warehouse;
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
    @Generated(hash = 1229596798)
    private transient Long warehouse__resolvedKey;
    @Generated(hash = 1986436088)
    private transient Long consignment__resolvedKey;
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
            WarehouseOperationsDao targetDao = daoSession.getWarehouseOperationsDao();
            WarehouseOperations warehouseNew = targetDao.load(__key);
            synchronized (this) {
                warehouse = warehouseNew;
                warehouse__resolvedKey = __key;
            }
        }
        return warehouse;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 929152524)
    public void setConsignment(Consignment consignment) {
        synchronized (this) {
            this.consignment = consignment;
            consignmentId = consignment == null ? null : consignment.getId();
            consignment__resolvedKey = consignmentId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 330853767)
    public Consignment getConsignment() {
        Long __key = this.consignmentId;
        if (consignment__resolvedKey == null || !consignment__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ConsignmentDao targetDao = daoSession.getConsignmentDao();
            Consignment consignmentNew = targetDao.load(__key);
            synchronized (this) {
                consignment = consignmentNew;
                consignment__resolvedKey = __key;
            }
        }
        return consignment;
    }
    public Long getWarehouseId() {
        return this.warehouseId;
    }
    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }
    @Generated(hash = 383883924)
    public ConsignmentProduct(Long id, Double costValue, Double countValue, Long productId,
            Long consignmentId, Long warehouseId) {
        this.id = id;
        this.costValue = costValue;
        this.countValue = countValue;
        this.productId = productId;
        this.consignmentId = consignmentId;
        this.warehouseId = warehouseId;
    }
    @Generated(hash = 724649454)
    public ConsignmentProduct() {
    }

}
