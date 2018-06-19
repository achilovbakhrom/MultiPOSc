package com.jim.multipos.data.db.model.inventory;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;

@Entity(active = true, nameInDb = "DETIALCOUNT")
public class DetialCount {

    @Id(autoincrement = true)
    private Long id;
    private Long outcomeProductId;
    private Long stockId;
    private double count;
    private double cost;
    @ToOne(joinProperty = "outcomeProductId")
    OutcomeProduct outcomeProduct;
    @ToOne(joinProperty = "stockId")
    StockQueue stockQueue;
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
    @Generated(hash = 1809648493)
    public void setStockQueue(StockQueue stockQueue) {
        synchronized (this) {
            this.stockQueue = stockQueue;
            stockId = stockQueue == null ? null : stockQueue.getId();
            stockQueue__resolvedKey = stockId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 650734403)
    public StockQueue getStockQueue() {
        Long __key = this.stockId;
        if (stockQueue__resolvedKey == null
                || !stockQueue__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StockQueueDao targetDao = daoSession.getStockQueueDao();
            StockQueue stockQueueNew = targetDao.load(__key);
            synchronized (this) {
                stockQueue = stockQueueNew;
                stockQueue__resolvedKey = __key;
            }
        }
        return stockQueue;
    }
    @Generated(hash = 1029195753)
    private transient Long stockQueue__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 455714)
    public void setOutcomeProduct(OutcomeProduct outcomeProduct) {
        synchronized (this) {
            this.outcomeProduct = outcomeProduct;
            outcomeProductId = outcomeProduct == null ? null : outcomeProduct
                    .getId();
            outcomeProduct__resolvedKey = outcomeProductId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 185308137)
    public OutcomeProduct getOutcomeProduct() {
        Long __key = this.outcomeProductId;
        if (outcomeProduct__resolvedKey == null
                || !outcomeProduct__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OutcomeProductDao targetDao = daoSession.getOutcomeProductDao();
            OutcomeProduct outcomeProductNew = targetDao.load(__key);
            synchronized (this) {
                outcomeProduct = outcomeProductNew;
                outcomeProduct__resolvedKey = __key;
            }
        }
        return outcomeProduct;
    }
    @Generated(hash = 1110532525)
    private transient Long outcomeProduct__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 317630791)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDetialCountDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 189212740)
    private transient DetialCountDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public double getCost() {
        return this.cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public double getCount() {
        return this.count;
    }
    public void setCount(double count) {
        this.count = count;
    }
    public Long getStockId() {
        return this.stockId;
    }
    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }
    public Long getOutcomeProductId() {
        return this.outcomeProductId;
    }
    public void setOutcomeProductId(Long outcomeProductId) {
        this.outcomeProductId = outcomeProductId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 2024604752)
    public DetialCount(Long id, Long outcomeProductId, Long stockId, double count, double cost) {
        this.id = id;
        this.outcomeProductId = outcomeProductId;
        this.stockId = stockId;
        this.count = count;
        this.cost = cost;
    }
    @Generated(hash = 796092273)
    public DetialCount() {
    }

}
