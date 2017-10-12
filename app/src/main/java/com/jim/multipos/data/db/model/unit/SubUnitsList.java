package com.jim.multipos.data.db.model.unit;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Keep;

import com.jim.multipos.data.db.model.DaoSession;

import java.util.UUID;

/**
 * Created by DEV on 05.09.2017.
 */
@Entity(active = true, nameInDb = "LIST_OF_UNITS")
public class SubUnitsList {
    @Id
    private Long id;
    private Long productId;
    private Long unitId;
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
    @Generated(hash = 655087947)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSubUnitsListDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 2054501514)
    private transient SubUnitsListDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getUnitId() {
        return this.unitId;
    }
    public void setUnitId(Long unitId) {
        this.unitId = unitId;
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
    @Generated(hash = 624667326)
    public SubUnitsList(Long id, Long productId, Long unitId) {
        this.id = id;
        this.productId = productId;
        this.unitId = unitId;
    }
    @Generated(hash = 1904010677)
    public SubUnitsList() {
    }
}
