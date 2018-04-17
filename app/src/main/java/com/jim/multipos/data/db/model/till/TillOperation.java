package com.jim.multipos.data.db.model.till;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.ToOne;

import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.PaymentTypeDao;

/**
 * Created by Sirojiddin on 02.02.2018.
 */
@Entity(active = true, nameInDb = "TILL_OPERATIONS")
public class TillOperation {

    public static final int PAY_IN = 0;
    public static final int PAY_OUT = 1;
    public static final int BANK_DROP = 2;

    @Id(autoincrement = true)
    private Long id;
    private int type;
    private Long createAt;
    private double amount;
    private String description;
    private Long tillId;
    @ToOne(joinProperty = "tillId")
    private Till till;
    private Long paymentTypeId;
    @ToOne(joinProperty = "paymentTypeId")
    private PaymentType paymentType;
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
    @Generated(hash = 1351411560)
    public void setPaymentType(PaymentType paymentType) {
        synchronized (this) {
            this.paymentType = paymentType;
            paymentTypeId = paymentType == null ? null : paymentType.getId();
            paymentType__resolvedKey = paymentTypeId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2041238771)
    public PaymentType getPaymentType() {
        Long __key = this.paymentTypeId;
        if (paymentType__resolvedKey == null
                || !paymentType__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PaymentTypeDao targetDao = daoSession.getPaymentTypeDao();
            PaymentType paymentTypeNew = targetDao.load(__key);
            synchronized (this) {
                paymentType = paymentTypeNew;
                paymentType__resolvedKey = __key;
            }
        }
        return paymentType;
    }
    @Generated(hash = 1438380965)
    private transient Long paymentType__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 728189389)
    public void setTill(Till till) {
        synchronized (this) {
            this.till = till;
            tillId = till == null ? null : till.getId();
            till__resolvedKey = tillId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 954679648)
    public Till getTill() {
        Long __key = this.tillId;
        if (till__resolvedKey == null || !till__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TillDao targetDao = daoSession.getTillDao();
            Till tillNew = targetDao.load(__key);
            synchronized (this) {
                till = tillNew;
                till__resolvedKey = __key;
            }
        }
        return till;
    }
    @Generated(hash = 1584262592)
    private transient Long till__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1948681009)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTillOperationDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 2118061679)
    private transient TillOperationDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getPaymentTypeId() {
        return this.paymentTypeId;
    }
    public void setPaymentTypeId(Long paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }
    public Long getTillId() {
        return this.tillId;
    }
    public void setTillId(Long tillId) {
        this.tillId = tillId;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getAmount() {
        return this.amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getCreateAt() {
        return this.createAt;
    }
    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }
    @Generated(hash = 1557109747)
    public TillOperation(Long id, int type, Long createAt, double amount, String description,
            Long tillId, Long paymentTypeId) {
        this.id = id;
        this.type = type;
        this.createAt = createAt;
        this.amount = amount;
        this.description = description;
        this.tillId = tillId;
        this.paymentTypeId = paymentTypeId;
    }
    @Generated(hash = 1733148244)
    public TillOperation() {
    }
}
