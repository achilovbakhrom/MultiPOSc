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
    @Id(autoincrement = true)
    private Long id;
    private Double amount;
    private String type;
    private String reason;
    private String applyingType;
    private boolean isActive;
    private Long createdDate;

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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 2038307626)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getServiceFeeDao() : null;
    }

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1592290701)
    private transient ServiceFeeDao myDao;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getApplyingType() {
        return this.applyingType;
    }

    public void setApplyingType(String applyingType) {
        this.applyingType = applyingType;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    @Generated(hash = 840624729)
    public ServiceFee(Long id, Double amount, String type, String reason,
            String applyingType, boolean isActive, Long createdDate) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.reason = reason;
        this.applyingType = applyingType;
        this.isActive = isActive;
        this.createdDate = createdDate;
    }

    @Generated(hash = 1970278224)
    public ServiceFee() {
    }

    @Override
    protected ServiceFee clone() throws CloneNotSupportedException {
        return (ServiceFee) super.clone();
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
