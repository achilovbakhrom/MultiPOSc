package com.jim.multipos.data.db.model.unit;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.UUID;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.jim.multipos.data.db.model.DaoSession;


@Entity(nameInDb = "UNIT", active = true)
public class Unit {
    @Id
    private Long id;
    private String name;
    private String abbr;
    private float factorRoot;
    private boolean isActive;
    private boolean isStaticUnit;
    private String subUnitAbbr;
    private Long rootId;
    private Long unitCategoryId;
    private Long productId;
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
    @Generated(hash = 342985017)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUnitDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 1283912641)
    private transient UnitDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getUnitCategoryId() {
        return this.unitCategoryId;
    }
    public void setUnitCategoryId(Long unitCategoryId) {
        this.unitCategoryId = unitCategoryId;
    }
    public Long getRootId() {
        return this.rootId;
    }
    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }
    public String getSubUnitAbbr() {
        return this.subUnitAbbr;
    }
    public void setSubUnitAbbr(String subUnitAbbr) {
        this.subUnitAbbr = subUnitAbbr;
    }
    public boolean getIsStaticUnit() {
        return this.isStaticUnit;
    }
    public void setIsStaticUnit(boolean isStaticUnit) {
        this.isStaticUnit = isStaticUnit;
    }
    public boolean getIsActive() {
        return this.isActive;
    }
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    public float getFactorRoot() {
        return this.factorRoot;
    }
    public void setFactorRoot(float factorRoot) {
        this.factorRoot = factorRoot;
    }
    public String getAbbr() {
        return this.abbr;
    }
    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getProductId() {
        return this.productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    @Generated(hash = 1570457876)
    public Unit(Long id, String name, String abbr, float factorRoot, boolean isActive,
            boolean isStaticUnit, String subUnitAbbr, Long rootId, Long unitCategoryId,
            Long productId) {
        this.id = id;
        this.name = name;
        this.abbr = abbr;
        this.factorRoot = factorRoot;
        this.isActive = isActive;
        this.isStaticUnit = isStaticUnit;
        this.subUnitAbbr = subUnitAbbr;
        this.rootId = rootId;
        this.unitCategoryId = unitCategoryId;
        this.productId = productId;
    }
    @Generated(hash = 1236212320)
    public Unit() {
    }

}
