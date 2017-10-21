package com.jim.multipos.data.db.model.unit;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.ToMany;

import com.jim.multipos.data.db.model.DaoSession;

import java.util.List;


@Entity(nameInDb = "UNIT_CATEGORY", active = true)
public class UnitCategory {
    @Id
    private Long id;
    private String name;
    private String abbr;
    @ToMany(referencedJoinProperty = "unitCategoryId")
    private List<Unit> units;
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
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 121816020)
    public synchronized void resetUnits() {
        units = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 248139458)
    public List<Unit> getUnits() {
        if (units == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UnitDao targetDao = daoSession.getUnitDao();
            List<Unit> unitsNew = targetDao._queryUnitCategory_Units(id);
            synchronized (this) {
                if(units == null) {
                    units = unitsNew;
                }
            }
        }
        return units;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1867336421)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUnitCategoryDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 787361558)
    private transient UnitCategoryDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
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
    @Generated(hash = 129417560)
    public UnitCategory(Long id, String name, String abbr) {
        this.id = id;
        this.name = name;
        this.abbr = abbr;
    }
    @Generated(hash = 1454197395)
    public UnitCategory() {
    }
}
