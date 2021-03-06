package com.jim.multipos.data.db.model.consignment;

import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;

/**
 * Created by Sirojiddin on 09.11.2017.
 */
@Entity(active = true, nameInDb = "CONSIGNMENT")
public class Consignment  {

    public static final int INCOME_CONSIGNMENT = 1000;
    public static final int RETURN_CONSIGNMENT = 1001;

    @Id(autoincrement = true)
    private Long id;
    private String consignmentNumber;
    private String description = "";
    private Double totalAmount;
    private Boolean isActive = true;
    private Boolean isDeleted = false;
    private Boolean isFromAccount;
    private Long createdDate;
    private int consignmentType;
    private Long vendorId;
    @ToOne(joinProperty = "vendorId")
    private Vendor vendor;
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "consignmentId")})
    private List<ConsignmentProduct> consignmentProducts;
    private Long firstPayId;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 265222024)
    private transient ConsignmentDao myDao;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    @Generated(hash = 1022035388)
    private transient Long vendor__resolvedKey;

    @Generated(hash = 407427411)
    public Consignment(Long id, String consignmentNumber, String description, Double totalAmount, Boolean isActive,
            Boolean isDeleted, Boolean isFromAccount, Long createdDate, int consignmentType, Long vendorId, Long firstPayId) {
        this.id = id;
        this.consignmentNumber = consignmentNumber;
        this.description = description;
        this.totalAmount = totalAmount;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.isFromAccount = isFromAccount;
        this.createdDate = createdDate;
        this.consignmentType = consignmentType;
        this.vendorId = vendorId;
        this.firstPayId = firstPayId;
    }

    @Generated(hash = 1791287112)
    public Consignment() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public Long getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

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
    @Generated(hash = 578339266)
    public List<ConsignmentProduct> getConsignmentProducts() {
        if (consignmentProducts == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ConsignmentProductDao targetDao = daoSession.getConsignmentProductDao();
            List<ConsignmentProduct> consignmentProductsNew = targetDao._queryConsignment_ConsignmentProducts(id);
            synchronized (this) {
                if (consignmentProducts == null) {
                    consignmentProducts = consignmentProductsNew;
                }
            }
        }
        return consignmentProducts;
    }

    @Keep
    public List<ConsignmentProduct> getAllConsignmentProducts() {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        return daoSession
                .queryBuilder(ConsignmentProduct.class)
                .where(
                        ConsignmentProductDao.Properties.ConsignmentId.eq(id),
                        ConsignmentProductDao.Properties.IsDeleted.eq(false)
                )
                .build()
                .list();
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1181206235)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getConsignmentDao() : null;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getIsFromAccount() {
        return this.isFromAccount;
    }

    public void setIsFromAccount(Boolean isFromAccount) {
        this.isFromAccount = isFromAccount;
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

    public String getConsignmentNumber() {
        return this.consignmentNumber;
    }

    public void setConsignmentNumber(String consignmentNumber) {
        this.consignmentNumber = consignmentNumber;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 332557200)
    public void setVendor(Vendor vendor) {
        synchronized (this) {
            this.vendor = vendor;
            vendorId = vendor == null ? null : vendor.getId();
            vendor__resolvedKey = vendorId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
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

    public Integer getConsignmentType() {
        return this.consignmentType;
    }

    public void setConsignmentType(Integer consignmentType) {
        this.consignmentType = consignmentType;
    }

    public void setConsignmentType(int consignmentType) {
        this.consignmentType = consignmentType;
    }

    public Long getFirstPayId() {
        return this.firstPayId;
    }

    public void setFirstPayId(Long firstPayId) {
        this.firstPayId = firstPayId;
    }
}
