package com.jim.multipos.data.db.model;


import com.jim.multipos.data.db.model.currency.Currency;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.currency.CurrencyDao;

import lombok.Data;

/**
 * Created by DEV on 17.08.2017.
 */
@Data
@Entity(nameInDb = "PAYMENT_TYPE", active = true)
public class PaymentType {

    public static final int DEBT_PAYMENT_TYPE = 7;
    public static final int CUSTOM_PAYMENT_TYPE = 0;
    public static final int CASH_PAYMENT_TYPE = 1;

    @Id(autoincrement = true)
    private Long id;
    private int typeStaticPaymentType = CUSTOM_PAYMENT_TYPE;
    private String name;
    private Long currencyId;
    @ToOne(joinProperty = "currencyId")
    private Currency currency;
    private boolean isActive = true;
    private boolean isNotSystem = true;
    private Long accountId;
    @ToOne(joinProperty = "accountId")
    private Account account;
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
    @Generated(hash = 1889019422)
    public void setCurrency(Currency currency) {
        synchronized (this) {
            this.currency = currency;
            currencyId = currency == null ? null : currency.getId();
            currency__resolvedKey = currencyId;
        }
    }
    /** To-one relationship, resolved on first access. */
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
    @Generated(hash = 1489923924)
    private transient Long currency__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 289857265)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPaymentTypeDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 1468171215)
    private transient PaymentTypeDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getAccountId() {
        return this.accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public boolean getIsNotSystem() {
        return this.isNotSystem;
    }
    public void setIsNotSystem(boolean isNotSystem) {
        this.isNotSystem = isNotSystem;
    }
    public boolean getIsActive() {
        return this.isActive;
    }
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    public Long getCurrencyId() {
        return this.currencyId;
    }
    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getTypeStaticPaymentType() {
        return this.typeStaticPaymentType;
    }
    public void setTypeStaticPaymentType(int typeStaticPaymentType) {
        this.typeStaticPaymentType = typeStaticPaymentType;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 15766687)
    public PaymentType(Long id, int typeStaticPaymentType, String name,
            Long currencyId, boolean isActive, boolean isNotSystem, Long accountId) {
        this.id = id;
        this.typeStaticPaymentType = typeStaticPaymentType;
        this.name = name;
        this.currencyId = currencyId;
        this.isActive = isActive;
        this.isNotSystem = isNotSystem;
        this.accountId = accountId;
    }
    @Generated(hash = 479868900)
    public PaymentType() {
    }
}
