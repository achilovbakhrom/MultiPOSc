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
    private String id;
    private String name;
    private boolean isActive;
    private boolean isTaxFree;
    private String serviceFeeId;
    @ToOne(joinProperty = "serviceFeeId")
    private ServiceFee serviceFee;
    private String discountId;
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

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
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
                if (customers == null) {
                    customers = customersNew;
                }
            }
        }
        return customers;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 761713010)
    public void setDiscount(Discount discount) {
        synchronized (this) {
            this.discount = discount;
            discountId = discount == null ? null : discount.getId();
            discount__resolvedKey = discountId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 970654932)
    public Discount getDiscount() {
        String __key = this.discountId;
        if (discount__resolvedKey == null || discount__resolvedKey != __key) {
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

    @Generated(hash = 1996692038)
    private transient String discount__resolvedKey;

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 233696324)
    public void setServiceFee(ServiceFee serviceFee) {
        synchronized (this) {
            this.serviceFee = serviceFee;
            serviceFeeId = serviceFee == null ? null : serviceFee.getId();
            serviceFee__resolvedKey = serviceFeeId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1728431386)
    public ServiceFee getServiceFee() {
        String __key = this.serviceFeeId;
        if (serviceFee__resolvedKey == null || serviceFee__resolvedKey != __key) {
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

    @Generated(hash = 291058008)
    private transient String serviceFee__resolvedKey;

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 310228965)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCustomerGroupDao() : null;
    }

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1785080120)
    private transient CustomerGroupDao myDao;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public String getDiscountId() {
        return this.discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getServiceFeeId() {
        return this.serviceFeeId;
    }

    public void setServiceFeeId(String serviceFeeId) {
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

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Generated(hash = 743434636)
    public CustomerGroup(String id, String name, boolean isActive,
                         boolean isTaxFree, String serviceFeeId, String discountId) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
        this.isTaxFree = isTaxFree;
        this.serviceFeeId = serviceFeeId;
        this.discountId = discountId;
    }

    public CustomerGroup() {
        id = UUID.randomUUID().toString();
    }
}