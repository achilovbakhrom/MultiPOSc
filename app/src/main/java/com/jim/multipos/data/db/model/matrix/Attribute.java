package com.jim.multipos.data.db.model.matrix;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.jim.multipos.data.db.model.DaoSession;

import java.util.UUID;

/**
 * Created by DEV on 17.09.2017.
 */
@Entity(active = true, nameInDb = "ATTRIBUTE")
public class Attribute {
    @Id
    private String id;
    private boolean isActive;
    private String attributeTypeId;

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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 701260076)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAttributeDao() : null;
    }

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 2114943461)
    private transient AttributeDao myDao;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public String getAttributeTypeId() {
        return this.attributeTypeId;
    }

    public void setAttributeTypeId(String attributeTypeId) {
        this.attributeTypeId = attributeTypeId;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Generated(hash = 2016743204)
    public Attribute(String id, boolean isActive, String attributeTypeId) {
        this.id = id;
        this.isActive = isActive;
        this.attributeTypeId = attributeTypeId;
    }

    @Keep
    public Attribute() {
        id = UUID.randomUUID().toString();

    }
}
