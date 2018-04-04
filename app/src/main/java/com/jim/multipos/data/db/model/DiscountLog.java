package com.jim.multipos.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(active = true, nameInDb = "DISCOUNT_LOG")
public class DiscountLog {

    public static final int DISCOUNT_ADDED= 0;
    public static final int DISCOUNT_UPDATED = 1;
    public static final int DISCOUNT_CANCELED = 2;
    public static final int DISCOUNT_DELETED = 3;

    @Id(autoincrement = true)
    private Long id;
    private Long changeDate;
    private int status;
    private Long discountId;
    @ToOne(joinProperty = "discountId")
    private Discount discount;
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
    @Generated(hash = 761713010)
    public void setDiscount(Discount discount) {
        synchronized (this) {
            this.discount = discount;
            discountId = discount == null ? null : discount.getId();
            discount__resolvedKey = discountId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1770890420)
    public Discount getDiscount() {
        Long __key = this.discountId;
        if (discount__resolvedKey == null || !discount__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DiscountDao targetDao = daoSession.getDiscountDao();
            Discount discountNew = targetDao.load(__key);
            synchronized (this) {
                discount = discountNew;
                discount__resolvedKey = __key;
            }
        }
        return discount;
    }
    @Generated(hash = 480750264)
    private transient Long discount__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2037704985)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDiscountLogDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 1527073785)
    private transient DiscountLogDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getDiscountId() {
        return this.discountId;
    }
    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
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
    @Generated(hash = 1700419737)
    public DiscountLog(Long id, Long changeDate, int status, Long discountId) {
        this.id = id;
        this.changeDate = changeDate;
        this.status = status;
        this.discountId = discountId;
    }
    @Generated(hash = 1443662439)
    public DiscountLog() {
    }
}
