package com.jim.multipos.data.db.model.history;

import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.DiscountDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


@Entity(nameInDb = "DISCOUNT_HISTORY", active = true)
public class DiscountHistory {
    public static final int PERCENT = 0;
    public static final int VALUE = 1;
    public static final int ITEM = 0;
    public static final int ORDER = 1;
    public static final int ALL = 2;

    @Id(autoincrement = true)
    private Long id;
    private String name;
    private double amount;
    private int amountType;
    private int usedType;
    private boolean active = true;
    private boolean delete = false;
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
    @Generated(hash = 979003107)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDiscountHistoryDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 96174768)
    private transient DiscountHistoryDao myDao;
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
    public boolean getDelete() {
        return this.delete;
    }
    public void setDelete(boolean delete) {
        this.delete = delete;
    }
    public boolean getActive() {
        return this.active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public int getUsedType() {
        return this.usedType;
    }
    public void setUsedType(int usedType) {
        this.usedType = usedType;
    }
    public int getAmountType() {
        return this.amountType;
    }
    public void setAmountType(int amountType) {
        this.amountType = amountType;
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
    @Generated(hash = 1410626923)
    public DiscountHistory(Long id, String name, double amount, int amountType,
            int usedType, boolean active, boolean delete, boolean isManual,
            Long createdDate, long editedAt, Long rootId) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.amountType = amountType;
        this.usedType = usedType;
        this.active = active;
        this.delete = delete;
        this.isManual = isManual;
        this.createdDate = createdDate;
        this.editedAt = editedAt;
        this.rootId = rootId;
    }
    @Generated(hash = 1918437972)
    public DiscountHistory() {
    }

}
