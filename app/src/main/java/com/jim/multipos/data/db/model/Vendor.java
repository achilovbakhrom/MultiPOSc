package com.jim.multipos.data.db.model;


import com.jim.multipos.data.db.model.intosystem.Editable;
import com.jim.multipos.data.db.model.products.Product;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Keep;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.products.ProductDao;

@Entity(nameInDb = "VENDOR", active = true)
public class Vendor implements Editable, Serializable {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String contactName;
    private String address;
    private Boolean isActive;
    private Boolean isDeleted;
    private Boolean isNotModified;
    private Long globalId;
    private Long rootId;
    private Long createdDate;
    private Long productId;


    @ToMany
    @JoinEntity(entity = VendorProductConnection.class, sourceProperty = "vendorId", targetProperty = "productId")
    private List<Product> products;



    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "vendorId")})
    private List<Contact> contacts;
    /** Used for active entity operations. */
    @Generated(hash = 957720129)
    private transient VendorDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;


    @Generated(hash = 1854852822)
    public Vendor(Long id, String name, String contactName, String address, Boolean isActive, Boolean isDeleted,
            Boolean isNotModified, Long globalId, Long rootId, Long createdDate, Long productId) {
        this.id = id;
        this.name = name;
        this.contactName = contactName;
        this.address = address;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.isNotModified = isNotModified;
        this.globalId = globalId;
        this.rootId = rootId;
        this.createdDate = createdDate;
        this.productId = productId;
    }

    @Generated(hash = 530746692)
    public Vendor() {
    }


    @Override
    @Keep
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    @Keep
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    @Keep
    public void setActive(boolean active) {
        this.isActive = active;
    }

    @Override
    @Keep
    public Long getId() {
        return id;
    }

    @Override
    @Keep
    public boolean isDeleted() {
        return this.isDeleted;
    }

    @Override
    @Keep
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    @Override
    @Keep
    public boolean isNotModifyted() {
        return this.isNotModified;
    }

    @Override
    @Keep
    public void setNotModifyted(boolean notModifyted) {
        this.isNotModified = notModifyted;
    }

    @Override
    @Keep
    public Long getRootId() {
        return this.rootId;
    }

    @Override
    @Keep
    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    @Override
    @Keep
    public Long getCreatedDate() {
        return this.createdDate;
    }

    @Override
    @Keep
    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
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

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1818154294)
    public synchronized void resetContacts() {
        contacts = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 934241905)
    public List<Contact> getContacts() {
        if (contacts == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ContactDao targetDao = daoSession.getContactDao();
            List<Contact> contactsNew = targetDao._queryVendor_Contacts(id);
            synchronized (this) {
                if(contacts == null) {
                    contacts = contactsNew;
                }
            }
        }
        return contacts;
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
    @Generated(hash = 878649066)
    public List<Product> getProducts() {
        if (products == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProductDao targetDao = daoSession.getProductDao();
            List<Product> productsNew = targetDao._queryVendor_Products(id);
            synchronized (this) {
                if(products == null) {
                    products = productsNew;
                }
            }
        }
        return products;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1079169342)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getVendorDao() : null;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getGlobalId() {
        return this.globalId;
    }

    public void setGlobalId(Long globalId) {
        this.globalId = globalId;
    }

    public Boolean getIsNotModified() {
        return this.isNotModified;
    }

    public void setIsNotModified(Boolean isNotModified) {
        this.isNotModified = isNotModified;
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

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

 }