package com.jim.multipos.data.db.model.intosystem;

import com.jim.multipos.data.db.model.products.SubCategory;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.UUID;

import com.jim.multipos.data.db.model.products.SubCategoryDao;
import com.jim.multipos.data.db.model.DaoSession;

/**
 * Created by DEV on 22.08.2017.
 */
@Entity(nameInDb = "SUB_CAT_POSITION", active = true)
public class SubCategoryPosition {
    @Id
    private String id;
    @Property
    private int position;
    @Property
    private String categoryId;
    @Property
    private String subCategoryId;
    @ToOne(joinProperty = "subCategoryId")
    private SubCategory subCategory;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1580172776)
    private transient SubCategoryPositionDao myDao;
    @Generated(hash = 861346724)
    private transient String subCategory__resolvedKey;

    /**
     * Used to resolve relations
     */

    public SubCategoryPosition() {
        id = UUID.randomUUID().toString();
    }

    @Keep
    public SubCategoryPosition(String parentId, int position, String categoryId) {
        this.categoryId = categoryId;
        this.id = UUID.randomUUID().toString();
        this.position = position;
        this.subCategoryId = parentId;
    }

    @Generated(hash = 843778318)
    public SubCategoryPosition(String id, int position, String categoryId,
                               String subCategoryId) {
        this.id = id;
        this.position = position;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getSubCategoryId() {
        return this.subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
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


    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 183596225)
    public SubCategory getSubCategory() {
        String __key = this.subCategoryId;
        if (subCategory__resolvedKey == null || subCategory__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SubCategoryDao targetDao = daoSession.getSubCategoryDao();
            SubCategory subCategoryNew = targetDao.load(__key);
            synchronized (this) {
                subCategory = subCategoryNew;
                subCategory__resolvedKey = __key;
            }
        }
        return subCategory;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 612508343)
    public void setSubCategory(SubCategory subCategory) {
        synchronized (this) {
            this.subCategory = subCategory;
            subCategoryId = subCategory == null ? null : subCategory.getId();
            subCategory__resolvedKey = subCategoryId;
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 930202240)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSubCategoryPositionDao() : null;
    }

}
