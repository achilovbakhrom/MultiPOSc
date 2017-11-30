package com.jim.multipos.data.db.model.inventory;

import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.products.Vendor;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import lombok.Data;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.AccountDao;
import com.jim.multipos.data.db.model.products.VendorDao;
import com.jim.multipos.data.db.model.consignment.ConsignmentDao;

/**
 * Created by developer on 27.11.2017.
 */
@Entity(nameInDb = "BILLING_OPERATION", active = true)
@Data
public class BillingOperations {

    public static final int DEBT = 100;
    public static final int PAID = 101;

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
    private String description;
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
        if (consignment__resolvedKey == null
                || !consignment__resolvedKey.equals(__key)) {
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
    @Generated(hash = 1986436088)
    private transient Long consignment__resolvedKey;
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
    @Generated(hash = 1501133588)
    private transient Long account__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1885474104)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBillingOperationsDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 862952958)
    private transient BillingOperationsDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
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
    public double getAmount() {
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
    public int getOperationType() {
        return this.operationType;
    }
    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }
    @Generated(hash = 926237327)
    public BillingOperations(Long id, Long accountId, Long vendorId, Long consignmentId,
            double amount, long createAt, int operationType, String description) {
        this.id = id;
        this.accountId = accountId;
        this.vendorId = vendorId;
        this.consignmentId = consignmentId;
        this.amount = amount;
        this.createAt = createAt;
        this.operationType = operationType;
        this.description = description;
    }
    @Generated(hash = 1327934834)
    public BillingOperations() {
    }
}

