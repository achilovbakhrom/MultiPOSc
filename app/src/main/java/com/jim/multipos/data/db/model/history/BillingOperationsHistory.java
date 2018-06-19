package com.jim.multipos.data.db.model.history;

import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.AccountDao;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentDao;
import com.jim.multipos.data.db.model.consignment.Invoice;
import com.jim.multipos.data.db.model.inventory.BillingOperationsDao;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import com.jim.multipos.data.db.model.consignment.InvoiceDao;

/**
 * Created by developer on 27.11.2017.
 */
@Entity(nameInDb = "BILLING_OPERATION_HISTORY", active = true)
public class BillingOperationsHistory {

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
    private Long invoiceId;
    @ToOne(joinProperty = "invoiceId")
    private Invoice invoice;
    private double amount;
    private long createAt;
    private int operationType;
    private String description = "";
    private boolean isActive = true;
    private boolean isDeleted = false;
    private Long rootId;
    private long paymentDate;
    private long editedAt;
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
    @Generated(hash = 749725237)
    public void setInvoice(Invoice invoice) {
        synchronized (this) {
            this.invoice = invoice;
            invoiceId = invoice == null ? null : invoice.getId();
            invoice__resolvedKey = invoiceId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 710097085)
    public Invoice getInvoice() {
        Long __key = this.invoiceId;
        if (invoice__resolvedKey == null || !invoice__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InvoiceDao targetDao = daoSession.getInvoiceDao();
            Invoice invoiceNew = targetDao.load(__key);
            synchronized (this) {
                invoice = invoiceNew;
                invoice__resolvedKey = __key;
            }
        }
        return invoice;
    }
    @Generated(hash = 694408149)
    private transient Long invoice__resolvedKey;
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
    @Generated(hash = 1689257587)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBillingOperationsHistoryDao()
                : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 848373507)
    private transient BillingOperationsHistoryDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getEditedAt() {
        return this.editedAt;
    }
    public void setEditedAt(long editedAt) {
        this.editedAt = editedAt;
    }
    public long getPaymentDate() {
        return this.paymentDate;
    }
    public void setPaymentDate(long paymentDate) {
        this.paymentDate = paymentDate;
    }
    public Long getRootId() {
        return this.rootId;
    }
    public void setRootId(Long rootId) {
        this.rootId = rootId;
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
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getOperationType() {
        return this.operationType;
    }
    public void setOperationType(int operationType) {
        this.operationType = operationType;
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
    public Long getInvoiceId() {
        return this.invoiceId;
    }
    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
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
    @Generated(hash = 507995)
    public BillingOperationsHistory(Long id, Long accountId, Long vendorId,
            Long invoiceId, double amount, long createAt, int operationType,
            String description, boolean isActive, boolean isDeleted, Long rootId,
            long paymentDate, long editedAt) {
        this.id = id;
        this.accountId = accountId;
        this.vendorId = vendorId;
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.createAt = createAt;
        this.operationType = operationType;
        this.description = description;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.rootId = rootId;
        this.paymentDate = paymentDate;
        this.editedAt = editedAt;
    }
    @Generated(hash = 987940575)
    public BillingOperationsHistory() {
    }


}

