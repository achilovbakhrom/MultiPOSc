package com.jim.multipos.data.db.model.currency;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;

/**
 * Created by DEV on 17.08.2017.
 */
@Entity(nameInDb = "CURRENCY_RATE", active = true)
public class CurrencyRate {
    @Id(autoincrement = true)
    private Long id;
    private String date;
    private Long firstCurrencyID;
    @ToOne(joinProperty = "firstCurrencyID")
    private Currency firstCurrency;
    private Long secondCurrencyID;
    @ToOne(joinProperty = "secondCurrencyID")
    private Currency secondCurrency;
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
    @Generated(hash = 8125419)
    public void setSecondCurrency(Currency secondCurrency) {
        synchronized (this) {
            this.secondCurrency = secondCurrency;
            secondCurrencyID = secondCurrency == null ? null : secondCurrency
                    .getId();
            secondCurrency__resolvedKey = secondCurrencyID;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1763066065)
    public Currency getSecondCurrency() {
        Long __key = this.secondCurrencyID;
        if (secondCurrency__resolvedKey == null
                || !secondCurrency__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CurrencyDao targetDao = daoSession.getCurrencyDao();
            Currency secondCurrencyNew = targetDao.load(__key);
            synchronized (this) {
                secondCurrency = secondCurrencyNew;
                secondCurrency__resolvedKey = __key;
            }
        }
        return secondCurrency;
    }
    @Generated(hash = 725792019)
    private transient Long secondCurrency__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 81474669)
    public void setFirstCurrency(Currency firstCurrency) {
        synchronized (this) {
            this.firstCurrency = firstCurrency;
            firstCurrencyID = firstCurrency == null ? null : firstCurrency.getId();
            firstCurrency__resolvedKey = firstCurrencyID;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 98769029)
    public Currency getFirstCurrency() {
        Long __key = this.firstCurrencyID;
        if (firstCurrency__resolvedKey == null
                || !firstCurrency__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CurrencyDao targetDao = daoSession.getCurrencyDao();
            Currency firstCurrencyNew = targetDao.load(__key);
            synchronized (this) {
                firstCurrency = firstCurrencyNew;
                firstCurrency__resolvedKey = __key;
            }
        }
        return firstCurrency;
    }
    @Generated(hash = 1855973419)
    private transient Long firstCurrency__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 148368970)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCurrencyRateDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 1178607874)
    private transient CurrencyRateDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getSecondCurrencyID() {
        return this.secondCurrencyID;
    }
    public void setSecondCurrencyID(Long secondCurrencyID) {
        this.secondCurrencyID = secondCurrencyID;
    }
    public Long getFirstCurrencyID() {
        return this.firstCurrencyID;
    }
    public void setFirstCurrencyID(Long firstCurrencyID) {
        this.firstCurrencyID = firstCurrencyID;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1293979143)
    public CurrencyRate(Long id, String date, Long firstCurrencyID,
            Long secondCurrencyID) {
        this.id = id;
        this.date = date;
        this.firstCurrencyID = firstCurrencyID;
        this.secondCurrencyID = secondCurrencyID;
    }
    @Generated(hash = 989774535)
    public CurrencyRate() {
    }
}
