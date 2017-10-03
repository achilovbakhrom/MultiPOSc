package com.jim.multipos.data.db.model.products;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.util.List;
import java.util.UUID;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.ToMany;

import com.jim.multipos.data.db.model.intosystem.NamePhotoPathId;
import com.jim.multipos.data.db.model.DaoSession;

@Entity(nameInDb = "CATEGORY", active = true)
public class Category implements NamePhotoPathId {
    @Id
    private String id;
    @Property
    private String name;
    @Property
    private String photoPath;
    @Property
    private String description;
    @Property
    private boolean active;
    @ToMany(joinProperties = {
            @JoinProperty(
                    name = "id", referencedName = "categoryId"
            )
    })
    @NotNull
    private List<SubCategory> subCategories;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 40161530)
    private transient CategoryDao myDao;

    @Keep
    public Category() {
        id = UUID.randomUUID().toString();
    }

    @Keep
    public Category(String name, String photoPath, String description, boolean active) {
        this.name = name;
        this.photoPath = photoPath;
        this.description = description;
        this.active = active;
        this.id = UUID.randomUUID().toString();
    }

    @Generated(hash = 1604348298)
    public Category(String id, String name, String photoPath, String description,
            boolean active) {
        this.id = id;
        this.name = name;
        this.photoPath = photoPath;
        this.description = description;
        this.active = active;
    }

    @Keep
    @Override
    public String getName() {
        return name;
    }

    @Keep
    @Override
    public String getPhotoPath() {
        return photoPath;
    }

    @Keep
    @Override
    public String getId() {
        return id;
    }

    @Keep
    @Override
    public boolean isActive() {
        return active;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 239672464)
    public List<SubCategory> getSubCategories() {
        if (subCategories == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SubCategoryDao targetDao = daoSession.getSubCategoryDao();
            List<SubCategory> subCategoriesNew = targetDao
                    ._queryCategory_SubCategories(id);
            synchronized (this) {
                if (subCategories == null) {
                    subCategories = subCategoriesNew;
                }
            }
        }
        return subCategories;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 832942771)
    public synchronized void resetSubCategories() {
        subCategories = null;
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

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getActive() {
        return this.active;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 503476761)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCategoryDao() : null;
    }
}
