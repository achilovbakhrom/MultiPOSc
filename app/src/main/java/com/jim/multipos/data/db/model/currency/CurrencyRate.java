package com.jim.multipos.data.db.model.currency;

import com.jim.multipos.data.db.model.employee.Employee;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.employee.EmployeeDao;
import com.jim.multipos.data.db.model.DaoSession;

/**
 * Created by DEV on 17.08.2017.
 */
@Entity(nameInDb = "CURRENCY_RATE", active = true)
public class CurrencyRate {
    @Id
    private String id;
    private String date;
    private String firstCurrencyID;
    @ToOne(joinProperty = "firstCurrencyID")
    private Currency firstCurrency;
    private String secondCurrencyID;
    @ToOne(joinProperty = "secondCurrencyID")
    private Currency secondCurrency;
    private String employerId;
    @ToOne(joinProperty = "employerId")
    private Employee employee;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1178607874)
    private transient CurrencyRateDao myDao;
    @Generated(hash = 1043429636)
    private transient String firstCurrency__resolvedKey;
    @Generated(hash = 1833615753)
    private transient String secondCurrency__resolvedKey;
    @Generated(hash = 1937818519)
    private transient String employee__resolvedKey;

    public CurrencyRate() {
        id = UUID.randomUUID().toString();
    }

    @Generated(hash = 1565237581)
    public CurrencyRate(String id, String date, String firstCurrencyID,
            String secondCurrencyID, String employerId) {
        this.id = id;
        this.date = date;
        this.firstCurrencyID = firstCurrencyID;
        this.secondCurrencyID = secondCurrencyID;
        this.employerId = employerId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFirstCurrencyID() {
        return this.firstCurrencyID;
    }

    public void setFirstCurrencyID(String firstCurrencyID) {
        this.firstCurrencyID = firstCurrencyID;
    }

    public String getSecondCurrencyID() {
        return this.secondCurrencyID;
    }

    public void setSecondCurrencyID(String secondCurrencyID) {
        this.secondCurrencyID = secondCurrencyID;
    }

    public String getEmployerId() {
        return this.employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 597145213)
    public Currency getFirstCurrency() {
        String __key = this.firstCurrencyID;
        if (firstCurrency__resolvedKey == null
                || firstCurrency__resolvedKey != __key) {
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
    @Generated(hash = 1186040251)
    public Currency getSecondCurrency() {
        String __key = this.secondCurrencyID;
        if (secondCurrency__resolvedKey == null
                || secondCurrency__resolvedKey != __key) {
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 8125419)
    public void setSecondCurrency(Currency secondCurrency) {
        synchronized (this) {
            this.secondCurrency = secondCurrency;
            secondCurrencyID = secondCurrency == null ? null
                    : secondCurrency.getId();
            secondCurrency__resolvedKey = secondCurrencyID;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 194499120)
    public Employee getEmployee() {
        String __key = this.employerId;
        if (employee__resolvedKey == null || employee__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EmployeeDao targetDao = daoSession.getEmployeeDao();
            Employee employeeNew = targetDao.load(__key);
            synchronized (this) {
                employee = employeeNew;
                employee__resolvedKey = __key;
            }
        }
        return employee;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1901998477)
    public void setEmployee(Employee employee) {
        synchronized (this) {
            this.employee = employee;
            employerId = employee == null ? null : employee.getId();
            employee__resolvedKey = employerId;
        }
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 148368970)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCurrencyRateDao() : null;
    }


}
