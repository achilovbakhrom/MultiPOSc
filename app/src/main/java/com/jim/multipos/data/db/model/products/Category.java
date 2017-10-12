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

import com.jim.multipos.data.db.model.intosystem.Editable;
import com.jim.multipos.data.db.model.intosystem.NamePhotoPathId;
import com.jim.multipos.data.db.model.DaoSession;

@Entity(nameInDb = "CATEGORY", active = true)
public class Category {
    public static final Long WITHOUT_PARENT = -1L;
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String description;
    private Boolean isActive = true;
    private Boolean isDeleted = false;
    private Boolean isModified = false;
    private Long createdDate;
    private Integer position;
    private Long parentId = WITHOUT_PARENT;
    @ToMany(joinProperties = {
            @JoinProperty(
                    name = "id", referencedName = "parentId"
            )
    })
    private List<Category> subCategories;
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "parentId")})
    private List<Product> products;
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
/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 513498032)
public synchronized void resetProducts() {
        products = null;
}
/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 1201024546)
public List<Product> getProducts() {
    if (products == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        ProductDao targetDao = daoSession.getProductDao();
        List<Product> productsNew = targetDao._queryCategory_Products(id);
        synchronized (this) {
            if(products == null) {
                products = productsNew;
            }
        }
    }
    return products;
}
/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 832942771)
public synchronized void resetSubCategories() {
        subCategories = null;
}
/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 1926233482)
public List<Category> getSubCategories() {
    if (subCategories == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        CategoryDao targetDao = daoSession.getCategoryDao();
        List<Category> subCategoriesNew = targetDao._queryCategory_SubCategories(id);
        synchronized (this) {
            if(subCategories == null) {
                subCategories = subCategoriesNew;
            }
        }
    }
    return subCategories;
}
/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 503476761)
public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCategoryDao() : null;
}
/** Used for active entity operations. */
@Generated(hash = 40161530)
private transient CategoryDao myDao;
/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;
public Long getParentId() {
        return this.parentId;
}
public void setParentId(Long parentId) {
        this.parentId = parentId;
}
public Integer getPosition() {
        return this.position;
}
public void setPosition(Integer position) {
        this.position = position;
}
public Long getCreatedDate() {
        return this.createdDate;
}
public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
}
public Boolean getIsModified() {
        return this.isModified;
}
public void setIsModified(Boolean isModified) {
        this.isModified = isModified;
}
public Boolean getIsDeleted() {
        return this.isDeleted;
}
public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
}
public Boolean getIsActive() {
        return this.isActive;
}
public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
}
public String getDescription() {
        return this.description;
}
public void setDescription(String description) {
        this.description = description;
}
public String getName() {
        return this.name;
}
public void setName(String name) {
        this.name = name;
}
public Long getId() {
        return this.id;
}
public void setId(Long id) {
        this.id = id;
}
@Generated(hash = 10094700)
public Category(Long id, String name, String description, Boolean isActive,
                Boolean isDeleted, Boolean isModified, Long createdDate,
                Integer position, Long parentId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.isModified = isModified;
        this.createdDate = createdDate;
        this.position = position;
        this.parentId = parentId;
}
@Generated(hash = 1150634039)
public Category() {
}

}
