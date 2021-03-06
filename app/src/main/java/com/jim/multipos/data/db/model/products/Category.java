package com.jim.multipos.data.db.model.products;


import com.jim.multipos.data.db.model.DaoSession;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;
import java.util.List;

@Entity(nameInDb = "CATEGORY", active = true)
public class Category implements  Serializable{
    public static final Long WITHOUT_PARENT = -1L;
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String description = "";
    private Boolean active = true;
    private Boolean deleted = false;
    private Long createdDate;
    private Long parentId = WITHOUT_PARENT;
    @ToOne(joinProperty = "parentId")
    private Category parentCategory;
    @ToMany(joinProperties = {
            @JoinProperty(
                    name = "id", referencedName = "parentId"
            )
    })
    private List<Category> subCategories;
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "categoryId")})
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

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
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
                if (products == null) {
                    products = productsNew;
                }
            }
        }
        return products;
    }

    @Keep
    public List<Product> getActiveProducts() {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        return daoSession
                .queryBuilder(Product.class)
                .where(
                        ProductDao.Properties.CategoryId.eq(id),
                        ProductDao.Properties.Active.eq(true),
                        ProductDao.Properties.Deleted.eq(false)
                )
                .build()
                .list();
    }

    @Keep
    public List<Product> getAllProducts() {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        return daoSession
                .queryBuilder(Product.class)
                .where(
                        ProductDao.Properties.CategoryId.eq(id),
                        ProductDao.Properties.Deleted.eq(false)
                )
                .build()
                .list();
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
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
                if (subCategories == null) {
                    subCategories = subCategoriesNew;
                }
            }
        }
        return subCategories;
    }

    @Keep
    public List<Category> getActiveSubCategories() {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        return daoSession
                .queryBuilder(Category.class)
                .where(CategoryDao.Properties.ParentId.eq(id),
                        CategoryDao.Properties.Active.eq(true),
                        CategoryDao.Properties.Deleted.eq(false))
                .build()
                .list();
    }

    @Keep
    public List<Category> getAllSubCategories() {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        return daoSession
                .queryBuilder(Category.class)
                .where(CategoryDao.Properties.ParentId.eq(id),
                        CategoryDao.Properties.Deleted.eq(false))
                .build()
                .list();
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 503476761)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCategoryDao() : null;
    }

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 40161530)
    private transient CategoryDao myDao;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    @Generated(hash = 1138982838)
    private transient Long parentCategory__resolvedKey;

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 607733206)
    public void setParentCategory(Category parentCategory) {
        synchronized (this) {
            this.parentCategory = parentCategory;
            parentId = parentCategory == null ? null : parentCategory.getId();
            parentCategory__resolvedKey = parentId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 881343150)
    public Category getParentCategory() {
        Long __key = this.parentId;
        if (parentCategory__resolvedKey == null || !parentCategory__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CategoryDao targetDao = daoSession.getCategoryDao();
            Category parentCategoryNew = targetDao.load(__key);
            synchronized (this) {
                parentCategory = parentCategoryNew;
                parentCategory__resolvedKey = __key;
            }
        }
        return parentCategory;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Generated(hash = 1150634039)
    public Category() {
    }

    @Generated(hash = 1675323139)
    public Category(Long id, String name, String description, Boolean active, Boolean deleted,
            Long createdDate, Long parentId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.deleted = deleted;
        this.createdDate = createdDate;
        this.parentId = parentId;
    }

}
