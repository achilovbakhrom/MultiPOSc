package com.jim.multipos.data.db.model.till;

import com.jim.multipos.data.db.model.Account;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.AccountDao;

/**
 * Created by Sirojiddin on 08.02.2018.
 */

@Entity(active = true, nameInDb = "TILL_MANAGEMENT_OPERATION")
public class TillManagementOperation {

    public static final int CLOSED_WITH = 100;
    public static final int TO_NEW_TILL = 101;
    public static final int OPENED_WITH = 102;

    @Id(autoincrement = true)
    private Long id;
    private int type;
    private Double amount;
    private String description = "";
    private Long accountId;
    @ToOne(joinProperty = "accountId")
    private Account account;
    private Long tillId;
    @ToOne(joinProperty = "tillId")
    private Till till;
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
    @Generated(hash = 728189389)
    public void setTill(Till till) {
        synchronized (this) {
            this.till = till;
            tillId = till == null ? null : till.getId();
            till__resolvedKey = tillId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 954679648)
    public Till getTill() {
        Long __key = this.tillId;
        if (till__resolvedKey == null || !till__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TillDao targetDao = daoSession.getTillDao();
            Till tillNew = targetDao.load(__key);
            synchronized (this) {
                till = tillNew;
                till__resolvedKey = __key;
            }
        }
        return till;
    }
    @Generated(hash = 1584262592)
    private transient Long till__resolvedKey;
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
    @Generated(hash = 1053157310)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTillManagementOperationDao()
                : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 186493684)
    private transient TillManagementOperationDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getTillId() {
        return this.tillId;
    }
    public void setTillId(Long tillId) {
        this.tillId = tillId;
    }
    public Long getAccountId() {
        return this.accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Double getAmount() {
        return this.amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1986922026)
    public TillManagementOperation(Long id, int type, Double amount,
            String description, Long accountId, Long tillId) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.accountId = accountId;
        this.tillId = tillId;
    }
    @Generated(hash = 100095027)
    public TillManagementOperation() {
    }
}
