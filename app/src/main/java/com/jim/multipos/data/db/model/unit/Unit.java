package com.jim.multipos.data.db.model.unit;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;


@Entity(nameInDb = "UNIT_ADDED", active = true)
public class Unit {
    @Id
    private String id;
    private String name;
    private String abbr;
    private float factorRoot;
    private boolean isActive;
    private boolean isStaticUnit;
    private String subUnitAbbr;
    private Long rootId;
    @ToOne(joinProperty = "rootId")
    private UnitCategory unitCategory;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1283912641)
    private transient UnitDao myDao;

    public Unit() {
        id = UUID.randomUUID().toString();
    }


    @Generated(hash = 1581094907)
    public Unit(String id, String name, String abbr, float factorRoot, boolean isActive,
            boolean isStaticUnit, String subUnitAbbr, Long rootId) {
        this.id = id;
        this.name = name;
        this.abbr = abbr;
        this.factorRoot = factorRoot;
        this.isActive = isActive;
        this.isStaticUnit = isStaticUnit;
        this.subUnitAbbr = subUnitAbbr;
        this.rootId = rootId;
    }
    

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getFactorRoot() {
        return this.factorRoot;
    }
    public void setFactorRoot(float factorRoot) {
        this.factorRoot = factorRoot;
    }
    public boolean getIsActive() {
        return this.isActive;
    }
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    public Boolean getIsStaticUnit() {
        return this.isStaticUnit;
    }
    public void setIsStaticUnit(boolean isStaticUnit) {
        this.isStaticUnit = isStaticUnit;
    }
    public Long getRootId() {
        return this.rootId;
    }
    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }
    @Generated(hash = 269968744)
    private transient Long unitCategory__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1383996926)
    public UnitCategory getUnitCategory() {
        Long __key = this.rootId;
        if (unitCategory__resolvedKey == null
                || !unitCategory__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UnitCategoryDao targetDao = daoSession.getUnitCategoryDao();
            UnitCategory unitCategoryNew = targetDao.load(__key);
            synchronized (this) {
                unitCategory = unitCategoryNew;
                unitCategory__resolvedKey = __key;
            }
        }
        return unitCategory;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 112824339)
    public void setUnitCategory(UnitCategory unitCategory) {
        synchronized (this) {
            this.unitCategory = unitCategory;
            rootId = unitCategory == null ? null : unitCategory.getId();
            unitCategory__resolvedKey = rootId;
        }
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
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 342985017)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUnitDao() : null;
    }

    public String getAbbr() {
        return this.abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }


    public String getSubUnitAbbr() {
        return this.subUnitAbbr;
    }


    public void setSubUnitAbbr(String subUnitAbbr) {
        this.subUnitAbbr = subUnitAbbr;
    }
}
