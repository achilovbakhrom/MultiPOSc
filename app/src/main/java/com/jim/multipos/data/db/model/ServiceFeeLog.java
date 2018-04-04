package com.jim.multipos.data.db.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

@Entity(active = true, nameInDb = "SERVICE_FEE_LOG")
public class ServiceFeeLog {

    public static final int SERVICE_FEE_ADDED= 0;
    public static final int SERVICE_FEE_UPDATED = 1;
    public static final int SERVICE_FEE_CANCELED = 2;
    public static final int SERVICE_FEE_DELETED = 3;

    @Id(autoincrement = true)
    private Long id;
    private Long changeDate;
    private int status;
    private Long serviceFeeId;
    @ToOne(joinProperty = "serviceFeeId")
    private ServiceFee serviceFee;
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
    @Generated(hash = 233696324)
    public void setServiceFee(ServiceFee serviceFee) {
        synchronized (this) {
            this.serviceFee = serviceFee;
            serviceFeeId = serviceFee == null ? null : serviceFee.getId();
            serviceFee__resolvedKey = serviceFeeId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 416102326)
    public ServiceFee getServiceFee() {
        Long __key = this.serviceFeeId;
        if (serviceFee__resolvedKey == null
                || !serviceFee__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ServiceFeeDao targetDao = daoSession.getServiceFeeDao();
            ServiceFee serviceFeeNew = targetDao.load(__key);
            synchronized (this) {
                serviceFee = serviceFeeNew;
                serviceFee__resolvedKey = __key;
            }
        }
        return serviceFee;
    }
    @Generated(hash = 1123616640)
    private transient Long serviceFee__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 923193706)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getServiceFeeLogDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 1779912441)
    private transient ServiceFeeLogDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getServiceFeeId() {
        return this.serviceFeeId;
    }
    public void setServiceFeeId(Long serviceFeeId) {
        this.serviceFeeId = serviceFeeId;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public Long getChangeDate() {
        return this.changeDate;
    }
    public void setChangeDate(Long changeDate) {
        this.changeDate = changeDate;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1620574701)
    public ServiceFeeLog(Long id, Long changeDate, int status, Long serviceFeeId) {
        this.id = id;
        this.changeDate = changeDate;
        this.status = status;
        this.serviceFeeId = serviceFeeId;
    }
    @Generated(hash = 391511144)
    public ServiceFeeLog() {
    }
}
