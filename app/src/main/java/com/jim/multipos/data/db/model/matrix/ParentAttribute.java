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
@Entity(active = true, nameInDb = "PARENT_ATTRIBUTE")
public class ParentAttribute {
    @Id
    private String id;
    private String parentProductId;
    @ToOne(joinProperty = "parentProductId")
    private Product product;
    private String firstAttributeTypeId;
    @ToOne(joinProperty = "firstAttributeTypeId")
    private AttributeType firstAttributeType;
    private String secondAttributeTypeId;
    @ToOne(joinProperty = "secondAttributeTypeId")
    private AttributeType secondAttributeType;

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
    @Generated(hash = 1205791049)
    public void setSecondAttributeType(AttributeType secondAttributeType) {
        synchronized (this) {
            this.secondAttributeType = secondAttributeType;
            secondAttributeTypeId = secondAttributeType == null ? null
                    : secondAttributeType.getId();
            secondAttributeType__resolvedKey = secondAttributeTypeId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1396762954)
    public AttributeType getSecondAttributeType() {
        String __key = this.secondAttributeTypeId;
        if (secondAttributeType__resolvedKey == null
                || secondAttributeType__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AttributeTypeDao targetDao = daoSession.getAttributeTypeDao();
            AttributeType secondAttributeTypeNew = targetDao.load(__key);
            synchronized (this) {
                secondAttributeType = secondAttributeTypeNew;
                secondAttributeType__resolvedKey = __key;
            }
        }
        return secondAttributeType;
    }

    @Generated(hash = 1055460241)
    private transient String secondAttributeType__resolvedKey;

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 128172970)
    public void setFirstAttributeType(AttributeType firstAttributeType) {
        synchronized (this) {
            this.firstAttributeType = firstAttributeType;
            firstAttributeTypeId = firstAttributeType == null ? null
                    : firstAttributeType.getId();
            firstAttributeType__resolvedKey = firstAttributeTypeId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 440685228)
    public AttributeType getFirstAttributeType() {
        String __key = this.firstAttributeTypeId;
        if (firstAttributeType__resolvedKey == null
                || firstAttributeType__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AttributeTypeDao targetDao = daoSession.getAttributeTypeDao();
            AttributeType firstAttributeTypeNew = targetDao.load(__key);
            synchronized (this) {
                firstAttributeType = firstAttributeTypeNew;
                firstAttributeType__resolvedKey = __key;
            }
        }
        return firstAttributeType;
    }

    @Generated(hash = 193755761)
    private transient String firstAttributeType__resolvedKey;

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 958886133)
    public void setProduct(Product product) {
        synchronized (this) {
            this.product = product;
            parentProductId = product == null ? null : product.getId();
            product__resolvedKey = parentProductId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1635382557)
    public Product getProduct() {
        String __key = this.parentProductId;
        if (product__resolvedKey == null || product__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProductDao targetDao = daoSession.getProductDao();
            Product productNew = targetDao.load(__key);
            synchronized (this) {
                product = productNew;
                product__resolvedKey = __key;
            }
        }
        return product;
    }

    @Generated(hash = 1996061979)
    private transient String product__resolvedKey;

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1985893322)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getParentAttributeDao() : null;
    }

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1948409092)
    private transient ParentAttributeDao myDao;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public String getSecondAttributeTypeId() {
        return this.secondAttributeTypeId;
    }

    public void setSecondAttributeTypeId(String secondAttributeTypeId) {
        this.secondAttributeTypeId = secondAttributeTypeId;
    }

    public String getFirstAttributeTypeId() {
        return this.firstAttributeTypeId;
    }

    public void setFirstAttributeTypeId(String firstAttributeTypeId) {
        this.firstAttributeTypeId = firstAttributeTypeId;
    }

    public String getParentProductId() {
        return this.parentProductId;
    }

    public void setParentProductId(String parentProductId) {
        this.parentProductId = parentProductId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Generated(hash = 1492395909)
    public ParentAttribute(String id, String parentProductId,
                           String firstAttributeTypeId, String secondAttributeTypeId) {
        this.id = id;
        this.parentProductId = parentProductId;
        this.firstAttributeTypeId = firstAttributeTypeId;
        this.secondAttributeTypeId = secondAttributeTypeId;
    }

    @Keep
    public ParentAttribute() {
        id = UUID.randomUUID().toString();
    }
}
