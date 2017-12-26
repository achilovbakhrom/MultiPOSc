package com.jim.multipos.data.db.model;


import com.jim.multipos.data.db.model.intosystem.Editable;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


@Entity(nameInDb = "SERVICE_FEE", active = true)
public class ServiceFee implements Editable {

    public static final int TYPE_PERCENT = 100;
    public static final int TYPE_VALUE = 101;
    public static final int TYPE_REPRICE = 102;
    public static final int APP_TYPE_ITEM = 1000;
    public static final int APP_TYPE_ORDER = 1001;
    public static final int APP_TYPE_ALL = 1002;

    @Id(autoincrement = true)
    private Long id;
    private Double amount;
    private int type;
    private String name;
    private int applyingType;
    private boolean isActive;
    private boolean isDeleted;
    private boolean notModifyted = true;
    private Long rootId;
    private Long createdDate;
    /** Used for active entity operations. */
    @Generated(hash = 1592290701)
    private transient ServiceFeeDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;


    @Generated(hash = 1970278224)
    public ServiceFee() {
    }


    @Generated(hash = 1989109301)
    public ServiceFee(Long id, Double amount, int type, String name, int applyingType,
            boolean isActive, boolean isDeleted, boolean notModifyted, Long rootId,
            Long createdDate) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.name = name;
        this.applyingType = applyingType;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.notModifyted = notModifyted;
        this.rootId = rootId;
        this.createdDate = createdDate;
    }
    

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }
    
    @Override
    protected ServiceFee clone() throws CloneNotSupportedException {
        return (ServiceFee) super.clone();
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        this.isActive = active;
    }

    @Override
    public boolean isDeleted() {
        return isDeleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    @Override
    public boolean isNotModifyted() {
        return notModifyted;
    }

    @Override
    public void setNotModifyted(boolean notModifyted) {
        this.notModifyted = notModifyted;
    }

    @Override
    public Long getRootId() {
        return rootId;
    }

    @Override
    public void setRootId(Long rootId) {
        this.rootId = rootId;
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
    @Generated(hash = 2038307626)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getServiceFeeDao() : null;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public boolean getNotModifyted() {
        return this.notModifyted;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getApplyingType() {
        return this.applyingType;
    }

    public void setApplyingType(int applyingType) {
        this.applyingType = applyingType;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
