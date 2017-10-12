package com.jim.multipos.data.db.model.stock;

import com.jim.multipos.data.db.model.PointOfSale;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.UUID;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.jim.multipos.data.db.model.PointOfSaleDao;
import com.jim.multipos.data.db.model.DaoSession;

/**
 * Created by DEV on 17.08.2017.
 */
@Entity(nameInDb = "STOCK_COMMUNICATION")
public class StockComminication {
    @Id
    private Long id;
    private String type;
    private Long stockId;
    @ToOne(joinProperty = "stockId")
    private Stock stock;
    private Long posId;
    @ToOne(joinProperty = "posId")
    private PointOfSale pointOfSale;
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
    @Generated(hash = 1155147220)
    public void setPointOfSale(PointOfSale pointOfSale) {
        synchronized (this) {
            this.pointOfSale = pointOfSale;
            posId = pointOfSale == null ? null : pointOfSale.getId();
            pointOfSale__resolvedKey = posId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1593810690)
    public PointOfSale getPointOfSale() {
        Long __key = this.posId;
        if (pointOfSale__resolvedKey == null
                || !pointOfSale__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PointOfSaleDao targetDao = daoSession.getPointOfSaleDao();
            PointOfSale pointOfSaleNew = targetDao.load(__key);
            synchronized (this) {
                pointOfSale = pointOfSaleNew;
                pointOfSale__resolvedKey = __key;
            }
        }
        return pointOfSale;
    }
    @Generated(hash = 1902655142)
    private transient Long pointOfSale__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1252106200)
    public void setStock(Stock stock) {
        synchronized (this) {
            this.stock = stock;
            stockId = stock == null ? null : stock.getId();
            stock__resolvedKey = stockId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1815663589)
    public Stock getStock() {
        Long __key = this.stockId;
        if (stock__resolvedKey == null || !stock__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StockDao targetDao = daoSession.getStockDao();
            Stock stockNew = targetDao.load(__key);
            synchronized (this) {
                stock = stockNew;
                stock__resolvedKey = __key;
            }
        }
        return stock;
    }
    @Generated(hash = 1574703934)
    private transient Long stock__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1867718117)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStockComminicationDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 1122044645)
    private transient StockComminicationDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getPosId() {
        return this.posId;
    }
    public void setPosId(Long posId) {
        this.posId = posId;
    }
    public Long getStockId() {
        return this.stockId;
    }
    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 2108589090)
    public StockComminication(Long id, String type, Long stockId, Long posId) {
        this.id = id;
        this.type = type;
        this.stockId = stockId;
        this.posId = posId;
    }
    @Generated(hash = 1411809505)
    public StockComminication() {
    }
}
