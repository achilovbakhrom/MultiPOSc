package com.jim.multipos.data.db.model.matrix;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import java.util.UUID;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.jim.multipos.data.db.model.DaoSession;

/**
 * Created by DEV on 17.09.2017.
 */
@Entity(nameInDb = "ATTRIBUTE_TYPE", active = true)
public class AttributeType {
    @Id
    private String id;
    private String name;
    private boolean isActive;
    @ToMany(joinProperties = {
            @JoinProperty(
                    name = "id", referencedName = "attributeTypeId"
            )
    })
    private List<Attribute> attributes;

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
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1697487056)
    public synchronized void resetAttributes() {
        attributes = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1568538966)
    public List<Attribute> getAttributes() {
        if (attributes == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AttributeDao targetDao = daoSession.getAttributeDao();
            List<Attribute> attributesNew = targetDao._queryAttributeType_Attributes(id);
            synchronized (this) {
                if (attributes == null) {
                    attributes = attributesNew;
                }
            }
        }
        return attributes;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1318639038)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAttributeTypeDao() : null;
    }

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1626059312)
    private transient AttributeTypeDao myDao;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Generated(hash = 1737583036)
    public AttributeType(String id, String name, boolean isActive) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
    }

    @Keep
    public AttributeType() {
        id = UUID.randomUUID().toString();
    }
}
