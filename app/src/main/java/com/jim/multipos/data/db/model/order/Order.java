package com.jim.multipos.data.db.model.order;

import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.products.Vendor;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;

import lombok.Data;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.customer.CustomerDao;
import org.greenrobot.greendao.annotation.NotNull;
import com.jim.multipos.data.db.model.ServiceFeeDao;

/**
 * Created by developer on 20.12.2017.
 */
@Entity(nameInDb = "ORDER", active = true)
@Data
public class Order {
    @Id(autoincrement = true)
    private Long id;
    private long createAt;
    private int status;
    private double subTotalValue;
    private double serviceTotalValue;
    private double discountTotalValue;
    private double orderServiceValue;
    private double orderDiscountValue;
    private double tips;
    private double totalPayed;
    private double toDebtValue;

    private long customer_id;
    @ToOne(joinProperty = "customer_id")
    private Customer customer;
    private long service_id;
    @ToOne(joinProperty = "service_id")
    private ServiceFee serviceFee;

    @ToMany(joinProperties = {
            @JoinProperty(
                    name = "id", referencedName = "orderId"
            )
    })
    private List<OrderProduct> orderProducts;

    @ToMany(joinProperties = {
            @JoinProperty(
                    name = "id", referencedName = "orderId"
            )
    })
    private List<PayedPartitions> payedPartitions;

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

/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 544073052)
public synchronized void resetPayedPartitions() {
        payedPartitions = null;
}

/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 1637679996)
public List<PayedPartitions> getPayedPartitions() {
    if (payedPartitions == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        PayedPartitionsDao targetDao = daoSession.getPayedPartitionsDao();
        List<PayedPartitions> payedPartitionsNew = targetDao._queryOrder_PayedPartitions(id);
        synchronized (this) {
            if(payedPartitions == null) {
                payedPartitions = payedPartitionsNew;
            }
        }
    }
    return payedPartitions;
}

/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 797759809)
public synchronized void resetOrderProducts() {
        orderProducts = null;
}

/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 11940788)
public List<OrderProduct> getOrderProducts() {
    if (orderProducts == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        OrderProductDao targetDao = daoSession.getOrderProductDao();
        List<OrderProduct> orderProductsNew = targetDao._queryOrder_OrderProducts(id);
        synchronized (this) {
            if(orderProducts == null) {
                orderProducts = orderProductsNew;
            }
        }
    }
    return orderProducts;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1626393540)
public void setServiceFee(@NotNull ServiceFee serviceFee) {
        if (serviceFee == null) {
                throw new DaoException(
                                "To-one property 'service_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
                this.serviceFee = serviceFee;
                service_id = serviceFee.getId();
                serviceFee__resolvedKey = service_id;
        }
}

/** To-one relationship, resolved on first access. */
@Generated(hash = 1564879830)
public ServiceFee getServiceFee() {
        long __key = this.service_id;
        if (serviceFee__resolvedKey == null
                        || !serviceFee__resolvedKey.equals(__key)) {
                final DaoSession daoSession = this.daoSession;
                if (daoSession == null) {
                        throw new DaoException(
                                        "Entity is detached from DAO context");
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
@Generated(hash = 1138688112)
public void setCustomer(@NotNull Customer customer) {
        if (customer == null) {
                throw new DaoException(
                                "To-one property 'customer_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
                this.customer = customer;
                customer_id = customer.getId();
                customer__resolvedKey = customer_id;
        }
}

/** To-one relationship, resolved on first access. */
@Generated(hash = 1342790599)
public Customer getCustomer() {
        long __key = this.customer_id;
        if (customer__resolvedKey == null
                        || !customer__resolvedKey.equals(__key)) {
                final DaoSession daoSession = this.daoSession;
                if (daoSession == null) {
                        throw new DaoException(
                                        "Entity is detached from DAO context");
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

@Generated(hash = 8592637)
private transient Long customer__resolvedKey;

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 965731666)
public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOrderDao() : null;
}

/** Used for active entity operations. */
@Generated(hash = 949219203)
private transient OrderDao myDao;
/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

public long getService_id() {
        return this.service_id;
}

public void setService_id(long service_id) {
        this.service_id = service_id;
}

public long getCustomer_id() {
        return this.customer_id;
}

public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
}

public double getToDebtValue() {
        return this.toDebtValue;
}

public void setToDebtValue(double toDebtValue) {
        this.toDebtValue = toDebtValue;
}

public double getTotalPayed() {
        return this.totalPayed;
}

public void setTotalPayed(double totalPayed) {
        this.totalPayed = totalPayed;
}

public double getTips() {
        return this.tips;
}

public void setTips(double tips) {
        this.tips = tips;
}

public double getOrderDiscountValue() {
        return this.orderDiscountValue;
}

public void setOrderDiscountValue(double orderDiscountValue) {
        this.orderDiscountValue = orderDiscountValue;
}

public double getOrderServiceValue() {
        return this.orderServiceValue;
}

public void setOrderServiceValue(double orderServiceValue) {
        this.orderServiceValue = orderServiceValue;
}

public double getDiscountTotalValue() {
        return this.discountTotalValue;
}

public void setDiscountTotalValue(double discountTotalValue) {
        this.discountTotalValue = discountTotalValue;
}

public double getServiceTotalValue() {
        return this.serviceTotalValue;
}

public void setServiceTotalValue(double serviceTotalValue) {
        this.serviceTotalValue = serviceTotalValue;
}

public double getSubTotalValue() {
        return this.subTotalValue;
}

public void setSubTotalValue(double subTotalValue) {
        this.subTotalValue = subTotalValue;
}

public int getStatus() {
        return this.status;
}

public void setStatus(int status) {
        this.status = status;
}

public long getCreateAt() {
        return this.createAt;
}

public void setCreateAt(long createAt) {
        this.createAt = createAt;
}

public Long getId() {
        return this.id;
}

public void setId(Long id) {
        this.id = id;
}

@Generated(hash = 981881482)
public Order(Long id, long createAt, int status, double subTotalValue,
                double serviceTotalValue, double discountTotalValue,
                double orderServiceValue, double orderDiscountValue,
                double tips, double totalPayed, double toDebtValue,
                long customer_id, long service_id) {
        this.id = id;
        this.createAt = createAt;
        this.status = status;
        this.subTotalValue = subTotalValue;
        this.serviceTotalValue = serviceTotalValue;
        this.discountTotalValue = discountTotalValue;
        this.orderServiceValue = orderServiceValue;
        this.orderDiscountValue = orderDiscountValue;
        this.tips = tips;
        this.totalPayed = totalPayed;
        this.toDebtValue = toDebtValue;
        this.customer_id = customer_id;
        this.service_id = service_id;
}

@Generated(hash = 1105174599)
public Order() {
}



}
