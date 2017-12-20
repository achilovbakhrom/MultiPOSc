package com.jim.multipos.data.db.model.order;

import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.products.Vendor;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import lombok.Data;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.PaymentTypeDao;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by developer on 20.12.2017.
 */
@Entity(nameInDb = "PAYEDPARTITIONS", active = true)
@Data
public class PayedPartitions {
    @Id(autoincrement = true)
    private Long id;
    private long orderId;

    private long paymentId;
    @ToOne(joinProperty = "paymentId")
    private PaymentType paymentType;

    private double value;

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
    @Generated(hash = 1477086135)
    public void setPaymentType(@NotNull PaymentType paymentType) {
        if (paymentType == null) {
            throw new DaoException(
                    "To-one property 'paymentId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.paymentType = paymentType;
            paymentId = paymentType.getId();
            paymentType__resolvedKey = paymentId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 760654797)
    public PaymentType getPaymentType() {
        long __key = this.paymentId;
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
    @Generated(hash = 4724972)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPayedPartitionsDao() : null;
    }

    /** Used for active entity operations. */
    @Generated(hash = 959290302)
    private transient PayedPartitionsDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getPaymentId() {
        return this.paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1777274461)
    public PayedPartitions(Long id, long orderId, long paymentId, double value) {
        this.id = id;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.value = value;
    }

    @Generated(hash = 2134620745)
    public PayedPartitions() {
    }
}
