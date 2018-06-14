package com.jim.multipos.data.db.model.inventory;


import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.history.BillingOperationsHistory;
import com.jim.multipos.data.db.model.products.Vendor;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.AccountDao;
import com.jim.multipos.data.db.model.products.VendorDao;
import com.jim.multipos.data.db.model.consignment.ConsignmentDao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.jim.multipos.data.db.model.history.BillingOperationsHistoryDao;

/**
 * Created by developer on 27.11.2017.
 */
@Entity(nameInDb = "BILLING_OPERATION", active = true)
public class BillingOperations  {

    public static final int DEBT_CONSIGNMENT = 100;
    public static final int PAID_TO_CONSIGNMENT = 101;
    public static final int RETURN_TO_VENDOR = 102;

    @Id(autoincrement = true)
    private Long id;
    private Long accountId;
    @ToOne(joinProperty = "accountId")
    private Account account;
    private Long vendorId;
    @ToOne(joinProperty = "vendorId")
    private Vendor vendor;
    private Long consignmentId;
    @ToOne(joinProperty = "consignmentId")
    private Consignment consignment;
    private double amount;
    private long createAt;
    private int operationType;
    private String description = "";
    private boolean isActive = true;
    private boolean isDeleted = false;
    private long paymentDate;

    @ToMany(joinProperties = {
            @JoinProperty(
                    name = "id", referencedName = "rootId"
            )
    })
    private List<BillingOperationsHistory> operationsHistoryList;


    @Keep
    public void keepToHistory(){
        if(daoSession!=null){
            BillingOperationsHistory billingOperationsHistory = new BillingOperationsHistory();
            billingOperationsHistory.setAccountId(accountId);
            billingOperationsHistory.setVendorId(vendorId);
            billingOperationsHistory.setConsignmentId(consignmentId);
            billingOperationsHistory.setAmount(amount);
            billingOperationsHistory.setCreateAt(createAt);
            billingOperationsHistory.setOperationType(operationType);
            billingOperationsHistory.setDescription(description);
            billingOperationsHistory.setIsActive(isActive);
            billingOperationsHistory.setIsDeleted(isDeleted);
            billingOperationsHistory.setPaymentDate(paymentDate);
            billingOperationsHistory.setRootId(id);
            billingOperationsHistory.setEditedAt(System.currentTimeMillis());
            daoSession.getBillingOperationsHistoryDao().insertOrReplace(billingOperationsHistory);
        }
    }
    @Keep
    public BillingOperationsHistory getBillingOperationHistoryForDate(Long date){
        if(daoSession!=null){
            List<BillingOperationsHistory> billingOperationsHistories = getOperationsHistoryList();
            Collections.sort(billingOperationsHistories, new Comparator<BillingOperationsHistory>() {
                @Override
                public int compare(BillingOperationsHistory billingOperationsHistory, BillingOperationsHistory t1) {
                    return billingOperationsHistory.getEditedAt().compareTo(t1.getEditedAt());
                }
            });
            for(BillingOperationsHistory billingOperationsHistory:billingOperationsHistories){
                if(date > billingOperationsHistory.getEditedAt()){
                    return billingOperationsHistory;
                }
            }
            return null;
        }else {
            new Exception("Gettting History for not saved object exeption").printStackTrace();
            return null;
        }
    }

    @Generated(hash = 1986436088)
    private transient Long consignment__resolvedKey;
    @Generated(hash = 1022035388)
    private transient Long vendor__resolvedKey;
    @Generated(hash = 1501133588)
    private transient Long account__resolvedKey;
    /** Used for active entity operations. */
    @Generated(hash = 862952958)
    private transient BillingOperationsDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public Long getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(long paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public long getCreateAt() {
        return this.createAt;
    }
    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }
    public Double getAmount() {
        return this.amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public Long getConsignmentId() {
        return this.consignmentId;
    }
    public void setConsignmentId(Long consignmentId) {
        this.consignmentId = consignmentId;
    }
    public Long getVendorId() {
        return this.vendorId;
    }
    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
    public Long getAccountId() {
        return this.accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public Long getId() {
        return this.id;
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

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getCreatedDate() {
        return this.createAt;
    }

    public void setCreatedDate(long createdDate) {
        this.createAt = createdDate;
    }

    public Integer getOperationType() {
        return this.operationType;
    }
    public void setOperationType(int operationType) {
        this.operationType = operationType;
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
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 929152524)
    public void setConsignment(Consignment consignment) {
        synchronized (this) {
            this.consignment = consignment;
            consignmentId = consignment == null ? null : consignment.getId();
            consignment__resolvedKey = consignmentId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 330853767)
    public Consignment getConsignment() {
        Long __key = this.consignmentId;
        if (consignment__resolvedKey == null || !consignment__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ConsignmentDao targetDao = daoSession.getConsignmentDao();
            Consignment consignmentNew = targetDao.load(__key);
            synchronized (this) {
                consignment = consignmentNew;
                consignment__resolvedKey = __key;
            }
        }
        return consignment;
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
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1910176546)
    public void setAccount(Account account) {
        synchronized (this) {
            this.account = account;
            accountId = account == null ? null : account.getId();
            account__resolvedKey = accountId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 531730087)
    public Account getAccount() {
        Long __key = this.accountId;
        if (account__resolvedKey == null || !account__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AccountDao targetDao = daoSession.getAccountDao();
            Account accountNew = targetDao.load(__key);
            synchronized (this) {
                account = accountNew;
                account__resolvedKey = __key;
            }
        }
        return account;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1885474104)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBillingOperationsDao() : null;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1017625180)
    public synchronized void resetOperationsHistoryList() {
        operationsHistoryList = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 877471280)
    public List<BillingOperationsHistory> getOperationsHistoryList() {
        if (operationsHistoryList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BillingOperationsHistoryDao targetDao = daoSession.getBillingOperationsHistoryDao();
            List<BillingOperationsHistory> operationsHistoryListNew = targetDao._queryBillingOperations_OperationsHistoryList(id);
            synchronized (this) {
                if(operationsHistoryList == null) {
                    operationsHistoryList = operationsHistoryListNew;
                }
            }
        }
        return operationsHistoryList;
    }


    @Generated(hash = 1782284890)
    public BillingOperations(Long id, Long accountId, Long vendorId, Long consignmentId, double amount,
            long createAt, int operationType, String description, boolean isActive, boolean isDeleted,
            long paymentDate) {
        this.id = id;
        this.accountId = accountId;
        this.vendorId = vendorId;
        this.consignmentId = consignmentId;
        this.amount = amount;
        this.createAt = createAt;
        this.operationType = operationType;
        this.description = description;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.paymentDate = paymentDate;
    }

    @Generated(hash = 1327934834)
    public BillingOperations() {
    }

}

