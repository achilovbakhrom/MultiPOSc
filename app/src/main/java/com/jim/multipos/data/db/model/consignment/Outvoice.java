package com.jim.multipos.data.db.model.consignment;

import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.products.Vendor;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.products.VendorDao;
import com.jim.multipos.data.db.model.inventory.OutcomeProductDao;

@Entity(active = true, nameInDb = "OUTVOICE")
public class Outvoice {
    @Id(autoincrement = true)
    private Long id;
    private String consigmentNumber;
    private String description = "";
    private Double totalAmount;
    private Boolean isDeleted = false;
    private Long createdDate;
    private Long vendorId;
    @ToOne(joinProperty = "vendorId")
    private Vendor vendor;
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "outvoiceId")})
    private List<OutcomeProduct> outcomeProducts;
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
    @Generated(hash = 2065867856)
    public synchronized void resetOutcomeProducts() {
        outcomeProducts = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1016989479)
    public List<OutcomeProduct> getOutcomeProducts() {
        if (outcomeProducts == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OutcomeProductDao targetDao = daoSession.getOutcomeProductDao();
            List<OutcomeProduct> outcomeProductsNew = targetDao._queryOutvoice_OutcomeProducts(id);
            synchronized (this) {
                if(outcomeProducts == null) {
                    outcomeProducts = outcomeProductsNew;
                }
            }
        }
        return outcomeProducts;
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
    @Generated(hash = 1022035388)
    private transient Long vendor__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1956848419)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOutvoiceDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 1061755352)
    private transient OutvoiceDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getVendorId() {
        return this.vendorId;
    }
    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
    public Long getCreatedDate() {
        return this.createdDate;
    }
    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }
    public Boolean getIsDeleted() {
        return this.isDeleted;
    }
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    public Double getTotalAmount() {
        return this.totalAmount;
    }
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getConsigmentNumber() {
        return this.consigmentNumber;
    }
    public void setConsigmentNumber(String consigmentNumber) {
        this.consigmentNumber = consigmentNumber;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 260440144)
    public Outvoice(Long id, String consigmentNumber, String description, Double totalAmount,
            Boolean isDeleted, Long createdDate, Long vendorId) {
        this.id = id;
        this.consigmentNumber = consigmentNumber;
        this.description = description;
        this.totalAmount = totalAmount;
        this.isDeleted = isDeleted;
        this.createdDate = createdDate;
        this.vendorId = vendorId;
    }
    @Generated(hash = 18030553)
    public Outvoice() {
    }
}
