package com.jim.multipos.data.db.model.intosystem;

import com.jim.multipos.data.db.model.products.Category;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;

import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.ToOne;

import com.jim.multipos.data.db.model.products.CategoryDao;
import com.jim.multipos.data.db.model.DaoSession;

/**
 * Created by DEV on 22.08.2017.
 */
@Entity(nameInDb = "POSITION", active = true)
public class CategoryPosition {
    @Id
    private String id;
    @Property
    private int position;
    @Property
    private String parentId;
    @ToOne(joinProperty = "parentId")
    private Category category;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 616485334)
    private transient CategoryPositionDao myDao;
    @Generated(hash = 646829400)
    private transient String category__resolvedKey;

    public CategoryPosition() {
        id = UUID.randomUUID().toString();
    }


    @Keep
    public CategoryPosition( String parentId,int position) {
        this.id =  UUID.randomUUID().toString();
        this.position = position;
        this.parentId = parentId;
    }


    @Generated(hash = 1757524887)
    public CategoryPosition(String id, int position, String parentId) {
        this.id = id;
        this.position = position;
        this.parentId = parentId;
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

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1244600378)
    public Category getCategory() {
        String __key = this.parentId;
        if (category__resolvedKey == null || category__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CategoryDao targetDao = daoSession.getCategoryDao();
            Category categoryNew = targetDao.load(__key);
            synchronized (this) {
                category = categoryNew;
                category__resolvedKey = __key;
            }
        }
        return category;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1044017476)
    public void setCategory(Category category) {
        synchronized (this) {
            this.category = category;
            parentId = category == null ? null : category.getId();
            category__resolvedKey = parentId;
        }
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2101544183)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCategoryPositionDao() : null;
    }
}
