package com.jim.multipos.data.db.model;


import com.jim.multipos.data.db.model.currency.Currency;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.UUID;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;

import com.jim.multipos.data.db.model.currency.CurrencyDao;


@Entity(nameInDb = "SERVICE_FEE", active = true)
public class ServiceFee {
    @Id
    private Long id;
    private String name;
    private double amount;
    private String type;
    private String usedFor;
    private String applyingType;
    private boolean isTaxed;
    private boolean isActive;
    private Long currencyId;
    @ToOne(joinProperty = "currencyId")
    Currency currency;
    private Long paymentTypeId;
    @ToOne(joinProperty = "paymentTypeId")
    PaymentType paymentType;
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
    @Generated(hash = 1351411560)
    public void setPaymentType(PaymentType paymentType) {
        synchronized (this) {
            this.paymentType = paymentType;
            paymentTypeId = paymentType == null ? null : paymentType.getId();
            paymentType__resolvedKey = paymentTypeId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2041238771)
    public PaymentType getPaymentType() {
        Long __key = this.paymentTypeId;
        if (paymentType__resolvedKey == null
                || !paymentType__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PaymentTypeDao targetDao = daoSession.getPaymentTypeDao();
            PaymentType paymentTypeNew = targetDao.load(__key);
            synchronized (this) {
                paymentType = paymentTypeNew;
                paymentType__resolvedKey = __key;
            }
        }
        return paymentType;
    }
    @Generated(hash = 1438380965)
    private transient Long paymentType__resolvedKey;
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
    @Generated(hash = 2038307626)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getServiceFeeDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 1592290701)
    private transient ServiceFeeDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getPaymentTypeId() {
        return this.paymentTypeId;
    }
    public void setPaymentTypeId(Long paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }
    public Long getCurrencyId() {
        return this.currencyId;
    }
    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }
    public boolean getIsActive() {
        return this.isActive;
    }
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    public boolean getIsTaxed() {
        return this.isTaxed;
    }
    public void setIsTaxed(boolean isTaxed) {
        this.isTaxed = isTaxed;
    }
    public String getApplyingType() {
        return this.applyingType;
    }
    public void setApplyingType(String applyingType) {
        this.applyingType = applyingType;
    }
    public String getUsedFor() {
        return this.usedFor;
    }
    public void setUsedFor(String usedFor) {
        this.usedFor = usedFor;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public double getAmount() {
        return this.amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
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
    @Generated(hash = 1388886159)
    public ServiceFee(Long id, String name, double amount, String type,
            String usedFor, String applyingType, boolean isTaxed, boolean isActive,
            Long currencyId, Long paymentTypeId) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.type = type;
        this.usedFor = usedFor;
        this.applyingType = applyingType;
        this.isTaxed = isTaxed;
        this.isActive = isActive;
        this.currencyId = currencyId;
        this.paymentTypeId = paymentTypeId;
    }
    @Generated(hash = 1970278224)
    public ServiceFee() {
    }
}
