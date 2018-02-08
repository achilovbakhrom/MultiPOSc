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
 * Created by Sirojiddin on 03.02.2018.
 */
@Entity(nameInDb = "TILL_DETAILS", active = true)
public class TillDetails {
    @Id(autoincrement = true)
    private Long id;
    private double totalStartingCash;
    private double totalPayOuts;
    private double totalPayIns;
    private double totalPayToVendors;
    private double totalDebtIncome;
    private double totalBankDrops;
    private double totalSales;
    private Long accountId;
    @ToOne(joinProperty = "accountId")
    private Account account;
    private Long tillId;
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
    @Generated(hash = 1610597325)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTillDetailsDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 1002185708)
    private transient TillDetailsDao myDao;
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
    public double getTotalSales() {
        return this.totalSales;
    }
    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }
    public double getTotalBankDrops() {
        return this.totalBankDrops;
    }
    public void setTotalBankDrops(double totalBankDrops) {
        this.totalBankDrops = totalBankDrops;
    }
    public double getTotalDebtIncome() {
        return this.totalDebtIncome;
    }
    public void setTotalDebtIncome(double totalDebtIncome) {
        this.totalDebtIncome = totalDebtIncome;
    }
    public double getTotalPayToVendors() {
        return this.totalPayToVendors;
    }
    public void setTotalPayToVendors(double totalPayToVendors) {
        this.totalPayToVendors = totalPayToVendors;
    }
    public double getTotalPayIns() {
        return this.totalPayIns;
    }
    public void setTotalPayIns(double totalPayIns) {
        this.totalPayIns = totalPayIns;
    }
    public double getTotalPayOuts() {
        return this.totalPayOuts;
    }
    public void setTotalPayOuts(double totalPayOuts) {
        this.totalPayOuts = totalPayOuts;
    }
    public double getTotalStartingCash() {
        return this.totalStartingCash;
    }
    public void setTotalStartingCash(double totalStartingCash) {
        this.totalStartingCash = totalStartingCash;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 2051134310)
    public TillDetails(Long id, double totalStartingCash, double totalPayOuts,
            double totalPayIns, double totalPayToVendors, double totalDebtIncome,
            double totalBankDrops, double totalSales, Long accountId, Long tillId) {
        this.id = id;
        this.totalStartingCash = totalStartingCash;
        this.totalPayOuts = totalPayOuts;
        this.totalPayIns = totalPayIns;
        this.totalPayToVendors = totalPayToVendors;
        this.totalDebtIncome = totalDebtIncome;
        this.totalBankDrops = totalBankDrops;
        this.totalSales = totalSales;
        this.accountId = accountId;
        this.tillId = tillId;
    }
    @Generated(hash = 1725934361)
    public TillDetails() {
    }
}
