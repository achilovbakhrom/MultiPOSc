package com.jim.multipos.data.db.model.order;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;

import lombok.Data;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by developer on 15.02.2018.
 */
@Entity(nameInDb = "ORDER_CHANGE_LOG", active = true)
@Data
public class OrderChangesLog {
    public static final int HAND = 1;
    public static final int EDITED = 2;
    public static final int PAYED = 3;
    public static final int CONTINUE = 4;
    public static final int HAND_AT_CLOSE_TILL = 5;

    @Id(autoincrement = true)
    private Long id;
    long changedAt;
    int toStatus;
    int changedCauseType;
    String reason;

    long orderId;
    @ToOne(joinProperty = "orderId")
    private Order order;

    long relationshipOrderId;
    @ToOne(joinProperty = "relationshipOrderId")
    private Order relationOrder;



    /** Used for active entity operations. */
    @Generated(hash = 2018495084)
    private transient OrderChangesLogDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    @Generated(hash = 644928069)
    private transient Long relationOrder__resolvedKey;
    @Generated(hash = 219913283)
    private transient Long order__resolvedKey;

    @Generated(hash = 1663214038)
    public OrderChangesLog(Long id, long changedAt, int toStatus, int changedCauseType,
            String reason, long orderId, long relationshipOrderId) {
        this.id = id;
        this.changedAt = changedAt;
        this.toStatus = toStatus;
        this.changedCauseType = changedCauseType;
        this.reason = reason;
        this.orderId = orderId;
        this.relationshipOrderId = relationshipOrderId;
    }
    @Generated(hash = 2107833076)
    public OrderChangesLog() {
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1095268188)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOrderChangesLogDao() : null;
    }
    public long getRelationshipOrderId() {
        return this.relationshipOrderId;
    }
    public void setRelationshipOrderId(long relationshipOrderId) {
        this.relationshipOrderId = relationshipOrderId;
    }
    public String getReason() {
        return this.reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public int getChangedCauseType() {
        return this.changedCauseType;
    }
    public void setChangedCauseType(int changedCauseType) {
        this.changedCauseType = changedCauseType;
    }
    public int getToStatus() {
        return this.toStatus;
    }
    public void setToStatus(int toStatus) {
        this.toStatus = toStatus;
    }
    public long getChangedAt() {
        return this.changedAt;
    }
    public void setChangedAt(long changedAt) {
        this.changedAt = changedAt;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2014746222)
    public void setRelationOrder(@NotNull Order relationOrder) {
        if (relationOrder == null) {
            throw new DaoException(
                    "To-one property 'relationshipOrderId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.relationOrder = relationOrder;
            relationshipOrderId = relationOrder.getId();
            relationOrder__resolvedKey = relationshipOrderId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 362678479)
    public Order getRelationOrder() {
        long __key = this.relationshipOrderId;
        if (relationOrder__resolvedKey == null || !relationOrder__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OrderDao targetDao = daoSession.getOrderDao();
            Order relationOrderNew = targetDao.load(__key);
            synchronized (this) {
                relationOrder = relationOrderNew;
                relationOrder__resolvedKey = __key;
            }
        }
        return relationOrder;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 678796274)
    public void setOrder(@NotNull Order order) {
        if (order == null) {
            throw new DaoException(
                    "To-one property 'orderId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.order = order;
            orderId = order.getId();
            order__resolvedKey = orderId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1307901010)
    public Order getOrder() {
        long __key = this.orderId;
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
    public long getOrderId() {
        return this.orderId;
    }
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
