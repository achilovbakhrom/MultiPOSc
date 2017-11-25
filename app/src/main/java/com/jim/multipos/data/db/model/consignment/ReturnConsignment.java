package com.jim.multipos.data.db.model.consignment;

import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.intosystem.Editable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.currency.CurrencyDao;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorDao;

/**
 * Created by Sirojiddin on 24.11.2017.
 */
@Entity(nameInDb = "RETURN_CONSIGNMENT", active = true)
public class ReturnConsignment implements Editable {

    @Id(autoincrement = true)
    Long id;
    private String returnNumber;
    private String description;
    private Double totalReturnAmount;
    private Boolean isActive = true;
    private Boolean isDeleted = false;
    private Boolean isNotModified = true;
    private Long createdDate;
    private Long rootId;
    private Long vendorId;
    @ToOne(joinProperty = "vendorId")
    private Vendor vendor;
    private Long currencyId;
    @ToOne(joinProperty = "currencyId")
    private Currency currency;
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "consignmentId")})
    private List<ConsignmentProduct> consignmentProducts;
    @Generated(hash = 1489923924)
    private transient Long currency__resolvedKey;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1831394786)
    private transient ReturnConsignmentDao myDao;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    @Generated(hash = 1022035388)
    private transient Long vendor__resolvedKey;


    @Generated(hash = 1648958820)
    public ReturnConsignment(Long id, String returnNumber, String description, Double totalReturnAmount, Boolean isActive,
            Boolean isDeleted, Boolean isNotModified, Long createdDate, Long rootId, Long vendorId, Long currencyId) {
        this.id = id;
        this.returnNumber = returnNumber;
        this.description = description;
        this.totalReturnAmount = totalReturnAmount;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.isNotModified = isNotModified;
        this.createdDate = createdDate;
        this.rootId = rootId;
        this.vendorId = vendorId;
        this.currencyId = currencyId;
    }

    @Generated(hash = 1040655140)
    public ReturnConsignment() {
    }


    @Override
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

    @Override
    public Long getId() {
        return this.id;
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

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 307869910)
    public synchronized void resetConsignmentProducts() {
        consignmentProducts = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1130291441)
    public List<ConsignmentProduct> getConsignmentProducts() {
        if (consignmentProducts == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ConsignmentProductDao targetDao = daoSession.getConsignmentProductDao();
            List<ConsignmentProduct> consignmentProductsNew = targetDao._queryReturnConsignment_ConsignmentProducts(id);
            synchronized (this) {
                if (consignmentProducts == null) {
                    consignmentProducts = consignmentProductsNew;
                }
            }
        }
        return consignmentProducts;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1889019422)
    public void setCurrency(Currency currency) {
        synchronized (this) {
            this.currency = currency;
            currencyId = currency == null ? null : currency.getId();
            currency__resolvedKey = currencyId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 434384135)
    public Currency getCurrency() {
        Long __key = this.currencyId;
        if (currency__resolvedKey == null || !currency__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CurrencyDao targetDao = daoSession.getCurrencyDao();
            Currency currencyNew = targetDao.load(__key);
            synchronized (this) {
                currency = currencyNew;
                currency__resolvedKey = __key;
            }
        }
        return currency;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 183238007)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getReturnConsignmentDao() : null;
    }

    public Long getCurrencyId() {
        return this.currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
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

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Double getTotalReturnAmount() {
        return this.totalReturnAmount;
    }

    public void setTotalReturnAmount(Double totalReturnAmount) {
        this.totalReturnAmount = totalReturnAmount;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReturnNumber() {
        return this.returnNumber;
    }

    public void setReturnNumber(String returnNumber) {
        this.returnNumber = returnNumber;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 332557200)
    public void setVendor(Vendor vendor) {
        synchronized (this) {
            this.vendor = vendor;
            vendorId = vendor == null ? null : vendor.getId();
            vendor__resolvedKey = vendorId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1192552702)
    public Vendor getVendor() {
        Long __key = this.vendorId;
        if (vendor__resolvedKey == null || !vendor__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VendorDao targetDao = daoSession.getVendorDao();
            Vendor vendorNew = targetDao.load(__key);
            synchronized (this) {
                vendor = vendorNew;
                vendor__resolvedKey = __key;
            }
        }
        return vendor;
    }

    public Long getVendorId() {
        return this.vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
}
