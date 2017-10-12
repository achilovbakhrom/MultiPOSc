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
    @Id(autoincrement = true)
    private Long id;
    @Property
    private String name;
    @Property
    private Boolean active;
    @Property
    private Long parentId;
    private boolean deleted;
    private boolean notModifyted;
    private Long rootId;
    private Long createdDate;
    @ToOne(joinProperty = "id")
    @NotNull
    private ProductClass productClass;
    @Generated(hash = 1979699144)
    private transient Long productClass__resolvedKey;
    /** Used for active entity operations. */
    @Generated(hash = 358612705)
    private transient ProductClassDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    @Generated(hash = 101617438)
    public ProductClass(Long id, String name, Boolean active, Long parentId,
            boolean deleted, boolean notModifyted, Long rootId, Long createdDate) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.parentId = parentId;
        this.deleted = deleted;
        this.notModifyted = notModifyted;
        this.rootId = rootId;
        this.createdDate = createdDate;
    }

    @Generated(hash = 965538416)
    public ProductClass() {
    }

    @Override
    public void setId(Long id) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setActive(boolean isActive) {

    }

    @Override
    public boolean isDeleted() {
        return false;
    }

    @Override
    public void setDeleted(boolean deleted) {

    }

    @Override
    public boolean isNotModifyted() {
        return false;
    }

    @Override
    public void setNotModifyted(boolean notModifyted) {

    }

    @Override
    public Long getRootId() {
        return null;
    }

    @Override
    public void setRootId(Long rootId) {

    }

    @Override
    public Long getCreatedDate() {
        return null;
    }

    @Override
    public void setCreatedDate(long createdDate) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Long getId() {
        return null;
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
    @Generated(hash = 1118378209)
    public void setProductClass(ProductClass productClass) {
        synchronized (this) {
            this.productClass = productClass;
            id = productClass == null ? null : productClass.getId();
            productClass__resolvedKey = id;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2135018260)
    public ProductClass getProductClass() {
        Long __key = this.id;
        if (productClass__resolvedKey == null
                || !productClass__resolvedKey.equals(__key)) {
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
    @Generated(hash = 234641496)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getProductClassDao() : null;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public boolean getNotModifyted() {
        return this.notModifyted;
    }

    public boolean getDeleted() {
        return this.deleted;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setName(String name) {
        this.name = name;
    }
}
