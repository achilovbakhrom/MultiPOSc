package com.jim.multipos.data.db.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import java.util.UUID;

import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import com.jim.multipos.data.db.model.intosystem.Editable;
import com.jim.multipos.data.db.model.products.Product;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.products.ProductDao;

@Entity(nameInDb = "VENDOR", active = true)
public class Vendor implements Editable{
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String contactName;
    private String email;
    private String phoneNumber;
    private String address;
    private Double taxRate;
    private Boolean isActive;
    private Boolean isDeleted;
    private Boolean isNotModified;
    private Long globalId;
    private Long rootId;
    private Long createdDate;
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "vendorId")})
    private List<Product> products;
    /** Used for active entity operations. */
    @Generated(hash = 957720129)
    private transient VendorDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    @Generated(hash = 1150672768)
    public Vendor(Long id, String name, String contactName, String email, String phoneNumber,
            String address, Double taxRate, Boolean isActive, Boolean isDeleted,
            Boolean isNotModified, Long globalId, Long rootId, Long createdDate) {
        this.id = id;
        this.name = name;
        this.contactName = contactName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.taxRate = taxRate;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.isNotModified = isNotModified;
        this.globalId = globalId;
        this.rootId = rootId;
        this.createdDate = createdDate;
    }

    @Generated(hash = 530746692)
    public Vendor() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public void setActive(boolean active) {
        this.isActive = active;
    }

    @Override
    public boolean isDeleted() {
        return this.isDeleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    @Override
    public boolean isNotModifyted() {
        return this.isNotModified;
    }

    @Override
    public void setNotModifyted(boolean notModifyted) {
        this.isNotModified = notModifyted;
    }

    @Override
    public Long getRootId() {
        return this.rootId;
    }

    @Override
    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    @Override
    public Long getCreatedDate() {
        return this.createdDate;
    }

    @Override
    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTaxRate() {
        return this.taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Long getGlobalId() {
        return this.globalId;
    }

    public void setGlobalId(Long globalId) {
        this.globalId = globalId;
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

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
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

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

 }