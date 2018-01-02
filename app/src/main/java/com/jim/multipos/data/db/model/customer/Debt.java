package com.jim.multipos.data.db.model.customer;

import com.jim.multipos.data.db.model.order.Order;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.order.OrderDao;

import java.util.List;

/**
 * Created by Sirojiddin on 29.12.2017.
 */
@Entity(active = true)
public class Debt {
    public static final int PARTICIPLE = 0;
    public static final int ALL = 1;
    public static final int CLOSED = 0;
    public static final int ACTIVE = 1;

    @Id(autoincrement = true)
    private Long id;
    private long takenDate;
    private long endDate;
    private Long customerId;
    @ToOne(joinProperty = "customerId")
    private Customer customer;
    private Long orderId;
    @ToOne(joinProperty = "orderId")
    private Order order;
    private int debtType;
    private double fee;
    private double debtAmount;
    private int status;
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "debtId")})
    private List<CustomerPayment> customerPayments;
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
    @Generated(hash = 1275275852)
    public void setOrder(Order order) {
        synchronized (this) {
            this.order = order;
            orderId = order == null ? null : order.getId();
            order__resolvedKey = orderId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 352034325)
    public Order getOrder() {
        Long __key = this.orderId;
        if (order__resolvedKey == null || !order__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OrderDao targetDao = daoSession.getOrderDao();
            Order orderNew = targetDao.load(__key);
            synchronized (this) {
                order = orderNew;
                order__resolvedKey = __key;
            }
        }
        return order;
    }
    @Generated(hash = 219913283)
    private transient Long order__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1357896984)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDebtDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 615885837)
    private transient DebtDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    @Generated(hash = 8592637)
    private transient Long customer__resolvedKey;
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public double getDebtAmount() {
        return this.debtAmount;
    }
    public void setDebtAmount(double debtAmount) {
        this.debtAmount = debtAmount;
    }
    public double getFee() {
        return this.fee;
    }
    public void setFee(double fee) {
        this.fee = fee;
    }
    public int getDebtType() {
        return this.debtType;
    }
    public void setDebtType(int debtType) {
        this.debtType = debtType;
    }
    public Long getOrderId() {
        return this.orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public Long getCustomerId() {
        return this.customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public long getEndDate() {
        return this.endDate;
    }
    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
    public long getTakenDate() {
        return this.takenDate;
    }
    public void setTakenDate(long takenDate) {
        this.takenDate = takenDate;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1082213564)
    public void setCustomer(Customer customer) {
        synchronized (this) {
            this.customer = customer;
            customerId = customer == null ? null : customer.getId();
            customer__resolvedKey = customerId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 149066257)
    public Customer getCustomer() {
        Long __key = this.customerId;
        if (customer__resolvedKey == null || !customer__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CustomerDao targetDao = daoSession.getCustomerDao();
            Customer customerNew = targetDao.load(__key);
            synchronized (this) {
                customer = customerNew;
                customer__resolvedKey = __key;
            }
        }
        return customer;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 2053807316)
    public synchronized void resetCustomerPayments() {
        customerPayments = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 37905221)
    public List<CustomerPayment> getCustomerPayments() {
        if (customerPayments == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CustomerPaymentDao targetDao = daoSession.getCustomerPaymentDao();
            List<CustomerPayment> customerPaymentsNew = targetDao._queryDebt_CustomerPayments(id);
            synchronized (this) {
                if(customerPayments == null) {
                    customerPayments = customerPaymentsNew;
                }
            }
        }
        return customerPayments;
    }
    @Keep
    public Debt(Long id, long takenDate, long endDate, Long customerId,
            Long orderId, int debtType, double fee, double debtAmount, int status) {
        this.id = id;
        this.takenDate = takenDate;
        this.endDate = endDate;
        this.customerId = customerId;
        this.orderId = orderId;
        this.debtType = debtType;
        this.fee = fee;
        this.debtAmount = debtAmount;
        this.status = status;
    }
    @Generated(hash = 488411483)
    public Debt() {
    }
}
