package com.jim.multipos.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.jim.multipos.data.db.model.intosystem.Editable;
import com.jim.multipos.data.db.model.intosystem.NameId;

import java.util.UUID;



/**
 * Created by developer on 29.08.2017.
 */
@Entity(nameInDb = "PRODUCT_CLASS", active = true)
public class ProductClass implements NameId,Editable {
    @Id
    private String id;
    @Property
    private String name;
    @Property
    private boolean active;
    @Property
    private String parentId;
    private boolean deleted;
    private boolean notModifyted;
    String rootId;
    long createdDate;
    @ToOne(joinProperty = "id")
    @NotNull
    private ProductClass productClass;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 358612705)
    private transient ProductClassDao myDao;
    @Generated(hash = 2037836123)
    public ProductClass(String id, String name, boolean active, String parentId,
            boolean deleted, boolean notModifyted, String rootId, long createdDate) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.parentId = parentId;
        this.deleted = deleted;
        this.notModifyted = notModifyted;
        this.rootId = rootId;
        this.createdDate = createdDate;
    }
    @Keep
    public ProductClass() {
        id= UUID.randomUUID().toString();
    }
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isActive() {
        return active;
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
    public Boolean getActive() {
        return this.active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean isNotModifyted() {
        return notModifyted;
    }

    @Override
    public void setNotModifyted(boolean notModifyted) {
        this.notModifyted = notModifyted;
    }

    @Override
    public String getRootId() {
        return rootId;
    }

    @Override
    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    @Override
    public Long getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getParentId() {
        return this.parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    @Generated(hash = 1476600865)
    private transient String productClass__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 152537493)
    public ProductClass getProductClass() {
        String __key = this.id;
        if (productClass__resolvedKey == null
                || productClass__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProductClassDao targetDao = daoSession.getProductClassDao();
            ProductClass productClassNew = targetDao.load(__key);
            synchronized (this) {
                productClass = productClassNew;
                productClass__resolvedKey = __key;
            }
        }
        return productClass;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1118378209)
    public void setProductClass(ProductClass productClass) {
        synchronized (this) {
            this.productClass = productClass;
            id = productClass == null ? null : productClass.getId();
            productClass__resolvedKey = id;
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
    @Generated(hash = 234641496)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getProductClassDao() : null;
    }
    public boolean getNotModifyted() {
        return this.notModifyted;
    }
    public boolean getDeleted() {
        return this.deleted;
    }
}
