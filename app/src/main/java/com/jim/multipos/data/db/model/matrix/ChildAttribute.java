package com.jim.multipos.data.db.model.matrix;

import com.jim.multipos.data.db.model.products.Product;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.products.ProductDao;

import java.util.UUID;

/**
 * Created by DEV on 17.09.2017.
 */
@Entity(nameInDb = "CHILD_ATTRIBUTE", active = true)
public class ChildAttribute {
    @Id
    private String id;
    private String parentAttributeId;
    @ToOne(joinProperty = "parentAttributeId")
    private ParentAttribute parentAttribute;
    private String childProductId;
    @ToOne(joinProperty = "childProductId")
    private Product childProduct;
    private String firstAttributeId;
    @ToOne(joinProperty = "firstAttributeId")
    private Attribute firstAttribute;
    private String secondAttributeId;
    @ToOne(joinProperty = "secondAttributeId")
    private Attribute secondAttribute;

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
    @Generated(hash = 1031859150)
    public void setSecondAttribute(Attribute secondAttribute) {
        synchronized (this) {
            this.secondAttribute = secondAttribute;
            secondAttributeId = secondAttribute == null ? null : secondAttribute
                    .getId();
            secondAttribute__resolvedKey = secondAttributeId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1840812819)
    public Attribute getSecondAttribute() {
        String __key = this.secondAttributeId;
        if (secondAttribute__resolvedKey == null
                || secondAttribute__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AttributeDao targetDao = daoSession.getAttributeDao();
            Attribute secondAttributeNew = targetDao.load(__key);
            synchronized (this) {
                secondAttribute = secondAttributeNew;
                secondAttribute__resolvedKey = __key;
            }
        }
        return secondAttribute;
    }

    @Generated(hash = 1751368008)
    private transient String secondAttribute__resolvedKey;

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 190017287)
    public void setFirstAttribute(Attribute firstAttribute) {
        synchronized (this) {
            this.firstAttribute = firstAttribute;
            firstAttributeId = firstAttribute == null ? null : firstAttribute
                    .getId();
            firstAttribute__resolvedKey = firstAttributeId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 296556080)
    public Attribute getFirstAttribute() {
        String __key = this.firstAttributeId;
        if (firstAttribute__resolvedKey == null
                || firstAttribute__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AttributeDao targetDao = daoSession.getAttributeDao();
            Attribute firstAttributeNew = targetDao.load(__key);
            synchronized (this) {
                firstAttribute = firstAttributeNew;
                firstAttribute__resolvedKey = __key;
            }
        }
        return firstAttribute;
    }

    @Generated(hash = 1469126029)
    private transient String firstAttribute__resolvedKey;

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1044507642)
    public void setChildProduct(Product childProduct) {
        synchronized (this) {
            this.childProduct = childProduct;
            childProductId = childProduct == null ? null : childProduct.getId();
            childProduct__resolvedKey = childProductId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 237754408)
    public Product getChildProduct() {
        String __key = this.childProductId;
        if (childProduct__resolvedKey == null || childProduct__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProductDao targetDao = daoSession.getProductDao();
            Product childProductNew = targetDao.load(__key);
            synchronized (this) {
                childProduct = childProductNew;
                childProduct__resolvedKey = __key;
            }
        }
        return childProduct;
    }

    @Generated(hash = 534726648)
    private transient String childProduct__resolvedKey;

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 801771248)
    public void setParentAttribute(ParentAttribute parentAttribute) {
        synchronized (this) {
            this.parentAttribute = parentAttribute;
            parentAttributeId = parentAttribute == null ? null : parentAttribute
                    .getId();
            parentAttribute__resolvedKey = parentAttributeId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 803935578)
    public ParentAttribute getParentAttribute() {
        String __key = this.parentAttributeId;
        if (parentAttribute__resolvedKey == null
                || parentAttribute__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ParentAttributeDao targetDao = daoSession.getParentAttributeDao();
            ParentAttribute parentAttributeNew = targetDao.load(__key);
            synchronized (this) {
                parentAttribute = parentAttributeNew;
                parentAttribute__resolvedKey = __key;
            }
        }
        return parentAttribute;
    }

    @Generated(hash = 778412301)
    private transient String parentAttribute__resolvedKey;

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1074468442)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getChildAttributeDao() : null;
    }

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 437446168)
    private transient ChildAttributeDao myDao;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public String getSecondAttributeId() {
        return this.secondAttributeId;
    }

    public void setSecondAttributeId(String secondAttributeId) {
        this.secondAttributeId = secondAttributeId;
    }

    public String getFirstAttributeId() {
        return this.firstAttributeId;
    }

    public void setFirstAttributeId(String firstAttributeId) {
        this.firstAttributeId = firstAttributeId;
    }

    public String getChildProductId() {
        return this.childProductId;
    }

    public void setChildProductId(String childProductId) {
        this.childProductId = childProductId;
    }

    public String getParentAttributeId() {
        return this.parentAttributeId;
    }

    public void setParentAttributeId(String parentAttributeId) {
        this.parentAttributeId = parentAttributeId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Generated(hash = 281717043)
    public ChildAttribute(String id, String parentAttributeId,
                          String childProductId, String firstAttributeId, String secondAttributeId) {
        this.id = id;
        this.parentAttributeId = parentAttributeId;
        this.childProductId = childProductId;
        this.firstAttributeId = firstAttributeId;
        this.secondAttributeId = secondAttributeId;
    }

    @Keep
    public ChildAttribute() {
        id = UUID.randomUUID().toString();
    }
}
