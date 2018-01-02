package com.jim.multipos.data.db.model.customer;

import com.jim.multipos.data.db.model.PaymentType;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import lombok.Data;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.PaymentTypeDao;

/**
 * Created by Sirojiddin on 29.12.2017.
 */
@Entity(nameInDb = "CUSTOMER_PAYMENT", active = true)
public class CustomerPayment {
    @Id(autoincrement = true)
    private Long id;
    private long paymentDate;
    private Long paymentTypeId;
    @ToOne(joinProperty = "paymentTypeId")
    private PaymentType paymentType;
    private Long debtId;
    @ToOne(joinProperty = "debtId")
    private Debt debt;
    private double paymentAmount;
    private double debtDue;
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
    @Generated(hash = 1073352067)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCustomerPaymentDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 1924253160)
    private transient CustomerPaymentDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    @Generated(hash = 1989546530)
    private transient Long debt__resolvedKey;
    public double getDebtDue() {
        return this.debtDue;
    }
    public void setDebtDue(double debtDue) {
        this.debtDue = debtDue;
    }
    public double getPaymentAmount() {
        return this.paymentAmount;
    }
    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
    public Long getPaymentTypeId() {
        return this.paymentTypeId;
    }
    public void setPaymentTypeId(Long paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }
    public long getPaymentDate() {
        return this.paymentDate;
    }
    public void setPaymentDate(long paymentDate) {
        this.paymentDate = paymentDate;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1368585146)
    public void setDebt(Debt debt) {
        synchronized (this) {
            this.debt = debt;
            debtId = debt == null ? null : debt.getId();
            debt__resolvedKey = debtId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 71464854)
    public Debt getDebt() {
        Long __key = this.debtId;
        if (debt__resolvedKey == null || !debt__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DebtDao targetDao = daoSession.getDebtDao();
            Debt debtNew = targetDao.load(__key);
            synchronized (this) {
                debt = debtNew;
                debt__resolvedKey = __key;
            }
        }
        return debt;
    }
    public Long getDebtId() {
        return this.debtId;
    }
    public void setDebtId(Long debtId) {
        this.debtId = debtId;
    }
    @Generated(hash = 1744334294)
    public CustomerPayment(Long id, long paymentDate, Long paymentTypeId, Long debtId,
            double paymentAmount, double debtDue) {
        this.id = id;
        this.paymentDate = paymentDate;
        this.paymentTypeId = paymentTypeId;
        this.debtId = debtId;
        this.paymentAmount = paymentAmount;
        this.debtDue = debtDue;
    }
    @Generated(hash = 1322280799)
    public CustomerPayment() {
    }
}
