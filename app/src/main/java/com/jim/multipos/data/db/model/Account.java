package com.jim.multipos.data.db.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;


/**
 * Created by DEV on 17.08.2017.
 */
@Entity(nameInDb = "ACCOUNTS", active = true)
public class Account {
    public static final int DEBT_ACCOUNT = 7;
    public static final int CASH_ACCOUNT = 1;
    public static final int CUSTOM_ACCOUNT = 0;

    @Id(autoincrement = true)
    private Long id;
    private String name;
    private int staticAccountType = CUSTOM_ACCOUNT;
    private boolean isActive = true;
    private boolean isNotSystemAccount = true;
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
    @Generated(hash = 1812283172)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAccountDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 335469827)
    private transient AccountDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public boolean getIsNotSystemAccount() {
        return this.isNotSystemAccount;
    }
    public void setIsNotSystemAccount(boolean isNotSystemAccount) {
        this.isNotSystemAccount = isNotSystemAccount;
    }
    public boolean getIsActive() {
        return this.isActive;
    }
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    public int getStaticAccountType() {
        return this.staticAccountType;
    }
    public void setStaticAccountType(int staticAccountType) {
        this.staticAccountType = staticAccountType;
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
    @Generated(hash = 1030309385)
    public Account(Long id, String name, int staticAccountType, boolean isActive,
            boolean isNotSystemAccount) {
        this.id = id;
        this.name = name;
        this.staticAccountType = staticAccountType;
        this.isActive = isActive;
        this.isNotSystemAccount = isNotSystemAccount;
    }
    @Generated(hash = 882125521)
    public Account() {
    }
}
