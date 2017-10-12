package com.jim.multipos.data.db.model.customer;

import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import java.util.UUID;

import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.ServiceFeeDao;
import com.jim.multipos.data.db.model.DiscountDao;

@Entity(nameInDb = "CUSTOMER_GROUP", active = true)
public class CustomerGroup {
    @Id
    private Long id;
    private String name;
    private boolean isActive;
    private boolean isTaxFree;
    private Long serviceFeeId;
    @ToOne(joinProperty = "serviceFeeId")
    private ServiceFee serviceFee;
    private Long discountId;
    @ToOne(joinProperty = "discountId")
    private Discount discount;
    @ToMany
    @JoinEntity(entity = JoinCustomerGroupsWithCustomers.class,
            sourceProperty = "customerGroupId",
            targetProperty = "customerId")
    private List<Customer> customers;
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
    @Generated(hash = 608715293)
    public synchronized void resetCustomers() {
        customers = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 494719775)
    public List<Customer> getCustomers() {
        if (customers == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CustomerDao targetDao = daoSession.getCustomerDao();
            List<Customer> customersNew = targetDao._queryCustomerGroup_Customers(id);
            synchronized (this) {
                if(customers == null) {
                    customers = customersNew;
                }
            }
        }
        return customers;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 761713010)
    public void setDiscount(Discount discount) {
        synchronized (this) {
            this.discount = discount;
            discountId = discount == null ? null : discount.getId();
            discount__resolvedKey = discountId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1770890420)
    public Discount getDiscount() {
        Long __key = this.discountId;
        if (discount__resolvedKey == null || !discount__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DiscountDao targetDao = daoSession.getDiscountDao();
            Discount discountNew = targetDao.load(__key);
            synchronized (this) {
                discount = discountNew;
                discount__resolvedKey = __key;
            }
        }
        return discount;
    }
    @Generated(hash = 480750264)
    private transient Long discount__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 233696324)
    public void setServiceFee(ServiceFee serviceFee) {
        synchronized (this) {
            this.serviceFee = serviceFee;
            serviceFeeId = serviceFee == null ? null : serviceFee.getId();
            serviceFee__resolvedKey = serviceFeeId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 416102326)
    public ServiceFee getServiceFee() {
        Long __key = this.serviceFeeId;
        if (serviceFee__resolvedKey == null
                || !serviceFee__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ServiceFeeDao targetDao = daoSession.getServiceFeeDao();
            ServiceFee serviceFeeNew = targetDao.load(__key);
            synchronized (this) {
                serviceFee = serviceFeeNew;
                serviceFee__resolvedKey = __key;
            }
        }
        return serviceFee;
    }
    @Generated(hash = 1123616640)
    private transient Long serviceFee__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 310228965)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCustomerGroupDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 1785080120)
    private transient CustomerGroupDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getDiscountId() {
        return this.discountId;
    }
    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }
    public Long getServiceFeeId() {
        return this.serviceFeeId;
    }
    public void setServiceFeeId(Long serviceFeeId) {
        this.serviceFeeId = serviceFeeId;
    }
    public boolean getIsTaxFree() {
        return this.isTaxFree;
    }
    public void setIsTaxFree(boolean isTaxFree) {
        this.isTaxFree = isTaxFree;
    }
    public boolean getIsActive() {
        return this.isActive;
    }
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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
    @Generated(hash = 1199827531)
    public CustomerGroup(Long id, String name, boolean isActive, boolean isTaxFree,
            Long serviceFeeId, Long discountId) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
        this.isTaxFree = isTaxFree;
        this.serviceFeeId = serviceFeeId;
        this.discountId = discountId;
    }
    @Generated(hash = 662460451)
    public CustomerGroup() {
    }
}