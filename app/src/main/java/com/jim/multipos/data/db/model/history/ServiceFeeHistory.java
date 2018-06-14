package com.jim.multipos.data.db.model.history;


import android.content.Context;

import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.ServiceFeeDao;
import com.jim.multipos.utils.CommonUtils;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;


@Entity(nameInDb = "SERVICE_FEE_HISTORY", active = true)
public class ServiceFeeHistory {

    public static final int PERCENT = 0;
    public static final int VALUE = 1;
    public static final int ITEM = 0;
    public static final int ORDER = 1;
    public static final int ALL = 2;

    @Id(autoincrement = true)
    private Long id;
    private Double amount;
    private int type;
    private String name;
    private int applyingType;
    private boolean isActive = true;
    private boolean isDeleted = false;
    private boolean isManual = false;
    private Long createdDate;
    private long editedAt;
    private Long rootId;
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
    @Generated(hash = 360273267)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getServiceFeeHistoryDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 221406508)
    private transient ServiceFeeHistoryDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getRootId() {
        return this.rootId;
    }
    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }
    public Long getEditedAt() {
        return this.editedAt;
    }
    public void setEditedAt(long editedAt) {
        this.editedAt = editedAt;
    }
    public Long getCreatedDate() {
        return this.createdDate;
    }
    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }
    public boolean getIsManual() {
        return this.isManual;
    }
    public void setIsManual(boolean isManual) {
        this.isManual = isManual;
    }
    public boolean getIsDeleted() {
        return this.isDeleted;
    }
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    public boolean getIsActive() {
        return this.isActive;
    }
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    public int getApplyingType() {
        return this.applyingType;
    }
    public void setApplyingType(int applyingType) {
        this.applyingType = applyingType;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public Double getAmount() {
        return this.amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1799286386)
    public ServiceFeeHistory(Long id, Double amount, int type, String name,
            int applyingType, boolean isActive, boolean isDeleted,
            boolean isManual, Long createdDate, long editedAt, Long rootId) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.name = name;
        this.applyingType = applyingType;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.isManual = isManual;
        this.createdDate = createdDate;
        this.editedAt = editedAt;
        this.rootId = rootId;
    }
    @Generated(hash = 1770177564)
    public ServiceFeeHistory() {
    }


}
