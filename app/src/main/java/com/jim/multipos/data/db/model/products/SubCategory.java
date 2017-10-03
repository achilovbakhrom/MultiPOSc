package com.jim.multipos.data.db.model.products;



import com.jim.multipos.data.db.model.intosystem.NamePhotoPathId;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import java.util.UUID;
import com.jim.multipos.data.db.model.DaoSession;


@Entity(nameInDb = "SUB_CATEGORY", active = true)
public class SubCategory implements NamePhotoPathId {
    @Id
    private String id;
    @Property
    private String name;
    @Property
    private String photoPath;
    @Property
    private String description;
    @Property
    private String categoryId;
    @Property
    private boolean active;
    @ToMany(joinProperties = {
            @JoinProperty(
                    name = "id", referencedName = "subCategoryId"
            )
    })
    @NotNull
    private List<Product> products;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1371187557)
    private transient SubCategoryDao myDao;

    public SubCategory() {
        id = UUID.randomUUID().toString();
    }

    public SubCategory(String name, String photoPath, String description, boolean active) {
        this.name = name;
        this.photoPath = photoPath;
        this.description = description;
        this.active = active;
        this.id = UUID.randomUUID().toString();
    }

    @Generated(hash = 730874306)
    public SubCategory(String id, String name, String photoPath, String description,
            String categoryId, boolean active) {
        this.id = id;
        this.name = name;
        this.photoPath = photoPath;
        this.description = description;
        this.categoryId = categoryId;
        this.active = active;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPhotoPath() {
        return photoPath;
    }

    @Override
    public String getId() {
        return id;
    }

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

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 527415705)
    public List<Product> getProducts() {
        if (products == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProductDao targetDao = daoSession.getProductDao();
            List<Product> productsNew = targetDao._querySubCategory_Products(id);
            synchronized (this) {
                if (products == null) {
                    products = productsNew;
                }
            }
        }
        return products;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 513498032)
    public synchronized void resetProducts() {
        products = null;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1297250478)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSubCategoryDao() : null;
    }
}
