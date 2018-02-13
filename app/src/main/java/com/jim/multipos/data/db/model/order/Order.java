package com.jim.multipos.data.db.model.order;

import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.DiscountDao;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.ServiceFeeDao;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerDao;
import com.jim.multipos.data.db.model.customer.Debt;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import com.jim.multipos.data.db.model.customer.DebtDao;
import com.jim.multipos.ui.mainpospage.model.DiscountItem;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;
import com.jim.multipos.ui.mainpospage.model.ServiceFeeItem;

/**
 * Created by developer on 20.12.2017.
 */
@Entity(nameInDb = "ORDER", active = true)
@Data
public class Order {
    public static final int EDITED_ORDER = 1;
    public static final int SIMPLE_ORDER = 0;

    @Id(autoincrement = true)
    private Long id;
    private long createAt;
    private int status;
    private double subTotalValue;
    private double serviceTotalValue;
    private double discountTotalValue;
    private double tips;
    private double totalPayed;
    private double toDebtValue;
    private double discountAmount;
    private double serviceAmount;
    private boolean isDeleted;
    private String deleteCause;
    private long customer_id;
    @ToOne(joinProperty = "customer_id")
    private Customer customer;
    private long deleteAt;
    private long editAt;
    private long serviceFeeId;
    @ToOne(joinProperty = "serviceFeeId")
    private ServiceFee serviceFee;

    private long debtId;
    @ToOne(joinProperty = "debtId")
    private Debt debt;

    private long discountId;
    @ToOne(joinProperty = "discountId")
    private Discount discount;

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


    public double getForPayAmmount(){
        return subTotalValue + discountTotalValue+ serviceTotalValue + tips;
    }

    public double getChange(){
        return totalPayed - getForPayAmmount();
    }

    public Order clone(){
        Order order = new Order();
        order.setTips(tips);
        order.setTotalPayed(totalPayed);
        return order;
    }
    public List<Object> getListObject(){
        List<Object> list = new ArrayList<>();
        getOrderProducts();
        for (int i = 0; i < orderProducts.size(); i++) {
            OrderProductItem orderProductItem = new OrderProductItem();
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setCost(orderProducts.get(i).getCost());
            orderProduct.setPrice(orderProducts.get(i).getPrice());
            orderProduct.setCount(orderProducts.get(i).getCount());
            orderProduct.setProduct(orderProducts.get(i).getProduct());
            orderProduct.setVendor(orderProducts.get(i).getVendor());
            orderProductItem.setOrderProduct(orderProduct);
            if(orderProducts.get(i).getServiceFee()!=null) {
                orderProductItem.setServiceFee(orderProducts.get(i).getServiceFee());
                orderProductItem.setServiceFeeAmmount(orderProducts.get(i).getServiceAmount());
            }
            if(orderProducts.get(i).getDiscount()!=null) {
                orderProductItem.setDiscount(orderProducts.get(i).getDiscount());
                orderProductItem.setDiscountAmmount(orderProducts.get(i).getDiscountAmount());
            }
            list.add(orderProductItem);
        }
        if(getDiscount() != null){
            DiscountItem discountItem = new DiscountItem();
            discountItem.setDiscount(getDiscount());
            discountItem.setAmmount(getDiscountAmount());
            list.add(discountItem);
        }
        if(getServiceFee() != null){
            ServiceFeeItem serviceFeeItem = new ServiceFeeItem();
            serviceFeeItem.setServiceFee(getServiceFee());
            serviceFeeItem.setAmmount(getServiceAmount());
            list.add(serviceFeeItem);
        }
       return list;
    }
    public DiscountItem getDiscountItem(){
        if(getDiscount()!=null) {
            DiscountItem discountItem = new DiscountItem();
            discountItem.setDiscount(getDiscount());
            discountItem.setAmmount(getDiscountAmount());
            return discountItem;
        }
        return null;
    }
    public ServiceFeeItem getServiceFeeItem(){
        if(getServiceFee()!=null){
            ServiceFeeItem serviceFeeItem = new ServiceFeeItem();
            serviceFeeItem.setServiceFee(getServiceFee());
            serviceFeeItem.setAmmount(getServiceAmount());
            return serviceFeeItem;
        }
        return null;
    }
    public Customer getOrderCustomer(){
        return getCustomer();
    }

    public List<PayedPartitions> getOrderPayedPartitionsClone(){
        List<PayedPartitions> payedPartitions = new ArrayList<>();
        if(getPayedPartitions().size() >=0){
            for (int i = 0; i < getPayedPartitions().size(); i++) {
                PayedPartitions payedPartition = new PayedPartitions();
                payedPartition.setPaymentType(getPayedPartitions().get(i).getPaymentType());
                payedPartition.setValue(getPayedPartitions().get(i).getValue());
                payedPartitions.add(payedPartition);
            }
        }
        return payedPartitions;
    }

    public Debt getDebtClone(){
        Debt debt = getDebt();
        if(debt == null)
            return null;
        else
        return debt.clone();
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
@Generated(hash = 1061119129)
public void setDiscount(@NotNull Discount discount) {
        if (discount == null) {
                throw new DaoException(
                                "To-one property 'discountId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
                this.discount = discount;
                discountId = discount.getId();
                discount__resolvedKey = discountId;
        }
}

/** To-one relationship, resolved on first access. */
@Generated(hash = 1929929130)
public Discount getDiscount() {
        long __key = this.discountId;
        if (discount__resolvedKey == null
                        || !discount__resolvedKey.equals(__key)) {
                final DaoSession daoSession = this.daoSession;
                if (daoSession == null) {
                        throw new DaoException(
                                        "Entity is detached from DAO context");
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
@Generated(hash = 868197846)
public void setServiceFee(@NotNull ServiceFee serviceFee) {
        if (serviceFee == null) {
                throw new DaoException(
                                "To-one property 'serviceFeeId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
                this.serviceFee = serviceFee;
                serviceFeeId = serviceFee.getId();
                serviceFee__resolvedKey = serviceFeeId;
        }
}

/** To-one relationship, resolved on first access. */
@Generated(hash = 1189758955)
public ServiceFee getServiceFee() {
        long __key = this.serviceFeeId;
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
@Generated(hash = 1989546530)
private transient Long debt__resolvedKey;

public long getDiscountId() {
        return this.discountId;
}

public void setDiscountId(long discountId) {
        this.discountId = discountId;
}

public long getServiceFeeId() {
        return this.serviceFeeId;
}

public void setServiceFeeId(long serviceFeeId) {
        this.serviceFeeId = serviceFeeId;
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


public double getServiceAmount() {
        return this.serviceAmount;
}


public void setServiceAmount(double serviceAmount) {
        this.serviceAmount = serviceAmount;
}


public double getDiscountAmount() {
        return this.discountAmount;
}


public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
}


public boolean getIsDeleted() {
        return this.isDeleted;
}


public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
}


public String getDeleteCause() {
        return this.deleteCause;
}


public void setDeleteCause(String deleteCause) {
        this.deleteCause = deleteCause;
}


/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 936904513)
public void setDebt(@NotNull Debt debt) {
        if (debt == null) {
                throw new DaoException("To-one property 'debtId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
                this.debt = debt;
                debtId = debt.getId();
                debt__resolvedKey = debtId;
        }
}


/** To-one relationship, resolved on first access. */
@Generated(hash = 280466444)
public Debt getDebt() {
        long __key = this.debtId;
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


public long getDebtId() {
        return this.debtId;
}


public void setDebtId(long debtId) {
        this.debtId = debtId;
}


public long getEditAt() {
        return this.editAt;
}


public void setEditAt(long editAt) {
        this.editAt = editAt;
}


public long getDeleteAt() {
        return this.deleteAt;
}


public void setDeleteAt(long deleteAt) {
        this.deleteAt = deleteAt;
}

@Generated(hash = 1039407432)
public Order(Long id, long createAt, int status, double subTotalValue, double serviceTotalValue,
                double discountTotalValue, double tips, double totalPayed, double toDebtValue, double discountAmount,
                double serviceAmount, boolean isDeleted, String deleteCause, long customer_id, long deleteAt,
                long editAt, long serviceFeeId, long debtId, long discountId) {
        this.id = id;
        this.createAt = createAt;
        this.status = status;
        this.subTotalValue = subTotalValue;
        this.serviceTotalValue = serviceTotalValue;
        this.discountTotalValue = discountTotalValue;
        this.tips = tips;
        this.totalPayed = totalPayed;
        this.toDebtValue = toDebtValue;
        this.discountAmount = discountAmount;
        this.serviceAmount = serviceAmount;
        this.isDeleted = isDeleted;
        this.deleteCause = deleteCause;
        this.customer_id = customer_id;
        this.deleteAt = deleteAt;
        this.editAt = editAt;
        this.serviceFeeId = serviceFeeId;
        this.debtId = debtId;
        this.discountId = discountId;
}


@Generated(hash = 1105174599)
public Order() {
}




}
