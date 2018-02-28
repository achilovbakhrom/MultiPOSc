package com.jim.multipos.data.db.model.customer;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;

import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.intosystem.Editable;

import java.util.List;
import java.util.UUID;


@Entity(nameInDb = "CUSTOMER", active = true)
public class Customer implements Editable {
    @Id(autoincrement = true)
    private Long id;
    private Long clientId;
    private String name = "";
    private String phoneNumber = "";
    private String address = "";
    private String qrCode = "";
    private Long createdDate;
    private Long modifiedDate;
    private boolean isActive = true;
    private boolean isDeleted = false;
    private boolean isNotModifyted = true;
    private Long rootId;
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "customerId")})
    private List<Debt> debtList;

    @ToMany
    @JoinEntity(entity = JoinCustomerGroupsWithCustomers.class,
            sourceProperty = "customerId",
            targetProperty = "customerGroupId")
    private List<CustomerGroup> customerGroups;
    /** Used for active entity operations. */
    @Generated(hash = 1697251196)
    private transient CustomerDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    @Generated(hash = 1362279701)
    public Customer(Long id, Long clientId, String name, String phoneNumber, String address,
            String qrCode, Long createdDate, Long modifiedDate, boolean isActive, boolean isDeleted,
            boolean isNotModifyted, Long rootId) {
        this.id = id;
        this.clientId = clientId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.qrCode = qrCode;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.isNotModifyted = isNotModifyted;
        this.rootId = rootId;
    }

    @Generated(hash = 60841032)
    public Customer() {
    }

    public void setCustomerGroups(List<CustomerGroup> customerGroups) {
        this.customerGroups = customerGroups;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean isDeleted() {
        return isDeleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean isNotModifyted() {
        return isNotModifyted;
    }

    @Override
    public void setNotModifyted(boolean notModifyted) {
        isNotModifyted = notModifyted;
    }

    @Override
    public Long getRootId() {
        return rootId;
    }

    @Override
    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    @Override
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

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 48278981)
    public synchronized void resetCustomerGroups() {
        customerGroups = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1512030343)
    public List<CustomerGroup> getCustomerGroups() {
        if (customerGroups == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CustomerGroupDao targetDao = daoSession.getCustomerGroupDao();
            List<CustomerGroup> customerGroupsNew = targetDao._queryCustomer_CustomerGroups(id);
            synchronized (this) {
                if (customerGroups == null) {
                    customerGroups = customerGroupsNew;
                }
            }
        }
        return customerGroups;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 462117449)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCustomerDao() : null;
    }

    public Long getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getCreatedDate() {
        return this.createdDate;
    }

    public String getQrCode() {
        return this.qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsNotModifyted() {
        return this.isNotModifyted;
    }

    public void setIsNotModifyted(boolean isNotModifyted) {
        this.isNotModifyted = isNotModifyted;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 2018636599)
    public synchronized void resetDebtList() {
        debtList = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1349978810)
    public List<Debt> getDebtList() {
        if (debtList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DebtDao targetDao = daoSession.getDebtDao();
            List<Debt> debtListNew = targetDao._queryCustomer_DebtList(id);
            synchronized (this) {
                if(debtList == null) {
                    debtList = debtListNew;
                }
            }
        }
        return debtList;
    }

    @Keep
    public List<Debt> getActiveDebts() {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        return daoSession
                .queryBuilder(Debt.class)
                .where(
                        DebtDao.Properties.CustomerId.eq(id),
                        DebtDao.Properties.Status.eq(Debt.ACTIVE),
                        DebtDao.Properties.IsDeleted.eq(false)
                )
                .build()
                .list();
    }
}
