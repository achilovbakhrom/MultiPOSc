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
    private String id;
    private String name;
    private double amount;
    private String type;
    private String usedFor;
    private String applyingType;
    private boolean isTaxed;
    private boolean isActive;
    private String currencyId;
    @ToOne(joinProperty = "currencyId")
    Currency currency;
    private String paymentTypeId;
    @ToOne(joinProperty = "paymentTypeId")
    PaymentType paymentType;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1592290701)
    private transient ServiceFeeDao myDao;
    @Generated(hash = 1170963677)
    private transient String currency__resolvedKey;
    @Generated(hash = 1916944099)
    private transient String paymentType__resolvedKey;

    @Generated(hash = 807881543)
    public ServiceFee(String id, String name, double amount, String type, String usedFor,
            String applyingType, boolean isTaxed, boolean isActive, String currencyId,
            String paymentTypeId) {
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

    public ServiceFee() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsedFor() {
        return this.usedFor;
    }

    public void setUsedFor(String usedFor) {
        this.usedFor = usedFor;
    }

    public String getApplyingType() {
        return this.applyingType;
    }

    public void setApplyingType(String applyingType) {
        this.applyingType = applyingType;
    }

    public boolean getIsTaxed() {
        return this.isTaxed;
    }

    public void setIsTaxed(boolean isTaxed) {
        this.isTaxed = isTaxed;
    }
    @Keep
    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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

    public String getCurrencyId() {
        return this.currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 376477166)
    public Currency getCurrency() {
        String __key = this.currencyId;
        if (currency__resolvedKey == null || currency__resolvedKey != __key) {
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1889019422)
    public void setCurrency(Currency currency) {
        synchronized (this) {
            this.currency = currency;
            currencyId = currency == null ? null : currency.getId();
            currency__resolvedKey = currencyId;
        }
    }

    public String getPaymentTypeId() {
        return this.paymentTypeId;
    }

    public void setPaymentTypeId(String paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 225318847)
    public PaymentType getPaymentType() {
        String __key = this.paymentTypeId;
        if (paymentType__resolvedKey == null || paymentType__resolvedKey != __key) {
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1351411560)
    public void setPaymentType(PaymentType paymentType) {
        synchronized (this) {
            this.paymentType = paymentType;
            paymentTypeId = paymentType == null ? null : paymentType.getId();
            paymentType__resolvedKey = paymentTypeId;
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2038307626)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getServiceFeeDao() : null;
    }
}
