package com.jim.multipos.data.db.model.inventory;

import com.jim.multipos.data.db.model.consignment.Outvoice;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderProduct;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.order.OrderProductDao;
import com.jim.multipos.data.db.model.consignment.OutvoiceDao;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.ProductDao;

@Entity(active = true, nameInDb = "OUT_PRODUCT")
public class OutcomeProduct {
    //OUTCOME_TYPES
    public static final int ORDER_SALES = 1;
    public static final int OUTVOICE_TO_VENDOR = 2;
    public static final int WASTE = 3;
    public static final int TO_HOLD = 4;

    //PRODUCT

    @Id(autoincrement = true)
    private Long id;
    private int outcomeType;
    private Double sumCountValue;
    private Double sumCostValue;
    private Long outcomeDate;
    private boolean closed;
    private boolean customPickSock;
    private Long pickedStockQueueId;

    private Long productId;
    @ToOne(joinProperty = "productId")
    private Product product;
    @ToOne(joinProperty = "pickedStockQueueId")
    private StockQueue stockQueue;

    //REFERENCE FOR GETTING STOCK QUEUE
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "outcomeProductId")})
    private List<DetialCount> detialCount;

    //IF IT IS PART OUTVOICE
    private Long outvoiceId;
    @ToOne(joinProperty = "outvoiceId")
    private Outvoice outvoice;


    //IF IT IS PART ORDER
    //IF IT IS PART HOLD SORDER
    private Long orderProductId;
    @ToOne(joinProperty = "orderProductId")
    private OrderProduct orderProduct;

    //IF IT IS WASTE
    @Unique
    private Long wasteId;
    @ToOne(joinProperty = "wasteId")
    private WasteOperation wasteOperation;
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
    @Generated(hash = 708779842)
    public synchronized void resetDetialCount() {
        detialCount = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1760130597)
    public List<DetialCount> getDetialCount() {
        if (detialCount == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DetialCountDao targetDao = daoSession.getDetialCountDao();
            List<DetialCount> detialCountNew = targetDao._queryOutcomeProduct_DetialCount(id);
            synchronized (this) {
                if(detialCount == null) {
                    detialCount = detialCountNew;
                }
            }
        }
        return detialCount;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1705135128)
    public void setWasteOperation(WasteOperation wasteOperation) {
        synchronized (this) {
            this.wasteOperation = wasteOperation;
            wasteId = wasteOperation == null ? null : wasteOperation.getId();
            wasteOperation__resolvedKey = wasteId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1631909874)
    public WasteOperation getWasteOperation() {
        Long __key = this.wasteId;
        if (wasteOperation__resolvedKey == null || !wasteOperation__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WasteOperationDao targetDao = daoSession.getWasteOperationDao();
            WasteOperation wasteOperationNew = targetDao.load(__key);
            synchronized (this) {
                wasteOperation = wasteOperationNew;
                wasteOperation__resolvedKey = __key;
            }
        }
        return wasteOperation;
    }
    @Generated(hash = 1832502798)
    private transient Long wasteOperation__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1032769298)
    public void setOrderProduct(OrderProduct orderProduct) {
        synchronized (this) {
            this.orderProduct = orderProduct;
            orderProductId = orderProduct == null ? null : orderProduct.getId();
            orderProduct__resolvedKey = orderProductId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 43086935)
    public OrderProduct getOrderProduct() {
        Long __key = this.orderProductId;
        if (orderProduct__resolvedKey == null || !orderProduct__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OrderProductDao targetDao = daoSession.getOrderProductDao();
            OrderProduct orderProductNew = targetDao.load(__key);
            synchronized (this) {
                orderProduct = orderProductNew;
                orderProduct__resolvedKey = __key;
            }
        }
        return orderProduct;
    }
    @Generated(hash = 1588167718)
    private transient Long orderProduct__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 736685258)
    public void setOutvoice(Outvoice outvoice) {
        synchronized (this) {
            this.outvoice = outvoice;
            outvoiceId = outvoice == null ? null : outvoice.getId();
            outvoice__resolvedKey = outvoiceId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1409481578)
    public Outvoice getOutvoice() {
        Long __key = this.outvoiceId;
        if (outvoice__resolvedKey == null || !outvoice__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OutvoiceDao targetDao = daoSession.getOutvoiceDao();
            Outvoice outvoiceNew = targetDao.load(__key);
            synchronized (this) {
                outvoice = outvoiceNew;
                outvoice__resolvedKey = __key;
            }
        }
        return outvoice;
    }
    @Generated(hash = 1779439357)
    private transient Long outvoice__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1669178786)
    public void setStockQueue(StockQueue stockQueue) {
        synchronized (this) {
            this.stockQueue = stockQueue;
            pickedStockQueueId = stockQueue == null ? null : stockQueue.getId();
            stockQueue__resolvedKey = pickedStockQueueId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 475781676)
    public StockQueue getStockQueue() {
        Long __key = this.pickedStockQueueId;
        if (stockQueue__resolvedKey == null || !stockQueue__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StockQueueDao targetDao = daoSession.getStockQueueDao();
            StockQueue stockQueueNew = targetDao.load(__key);
            synchronized (this) {
                stockQueue = stockQueueNew;
                stockQueue__resolvedKey = __key;
            }
        }
        return stockQueue;
    }
    @Generated(hash = 1029195753)
    private transient Long stockQueue__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 558738496)
    public void setProduct(Product product) {
        synchronized (this) {
            this.product = product;
            productId = product == null ? null : product.getId();
            product__resolvedKey = productId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1198864293)
    public Product getProduct() {
        Long __key = this.productId;
        if (product__resolvedKey == null || !product__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProductDao targetDao = daoSession.getProductDao();
            Product productNew = targetDao.load(__key);
            synchronized (this) {
                product = productNew;
                product__resolvedKey = __key;
            }
        }
        return product;
    }
    @Generated(hash = 587652864)
    private transient Long product__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1910995361)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOutcomeProductDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 2098808205)
    private transient OutcomeProductDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getWasteId() {
        return this.wasteId;
    }
    public void setWasteId(Long wasteId) {
        this.wasteId = wasteId;
    }
    public Long getOrderProductId() {
        return this.orderProductId;
    }
    public void setOrderProductId(Long orderProductId) {
        this.orderProductId = orderProductId;
    }
    public Long getOutvoiceId() {
        return this.outvoiceId;
    }
    public void setOutvoiceId(Long outvoiceId) {
        this.outvoiceId = outvoiceId;
    }
    public Long getProductId() {
        return this.productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Long getPickedStockQueueId() {
        return this.pickedStockQueueId;
    }
    public void setPickedStockQueueId(Long pickedStockQueueId) {
        this.pickedStockQueueId = pickedStockQueueId;
    }
    public boolean getCustomPickSock() {
        return this.customPickSock;
    }
    public void setCustomPickSock(boolean customPickSock) {
        this.customPickSock = customPickSock;
    }
    public boolean getClosed() {
        return this.closed;
    }
    public void setClosed(boolean closed) {
        this.closed = closed;
    }
    public Long getOutcomeDate() {
        return this.outcomeDate;
    }
    public void setOutcomeDate(Long outcomeDate) {
        this.outcomeDate = outcomeDate;
    }
    public Double getSumCostValue() {
        return this.sumCostValue;
    }
    public void setSumCostValue(Double sumCostValue) {
        this.sumCostValue = sumCostValue;
    }
    public Double getSumCountValue() {
        return this.sumCountValue;
    }
    public void setSumCountValue(Double sumCountValue) {
        this.sumCountValue = sumCountValue;
    }
    public int getOutcomeType() {
        return this.outcomeType;
    }
    public void setOutcomeType(int outcomeType) {
        this.outcomeType = outcomeType;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1410550842)
    public OutcomeProduct(Long id, int outcomeType, Double sumCountValue, Double sumCostValue,
            Long outcomeDate, boolean closed, boolean customPickSock, Long pickedStockQueueId,
            Long productId, Long outvoiceId, Long orderProductId, Long wasteId) {
        this.id = id;
        this.outcomeType = outcomeType;
        this.sumCountValue = sumCountValue;
        this.sumCostValue = sumCostValue;
        this.outcomeDate = outcomeDate;
        this.closed = closed;
        this.customPickSock = customPickSock;
        this.pickedStockQueueId = pickedStockQueueId;
        this.productId = productId;
        this.outvoiceId = outvoiceId;
        this.orderProductId = orderProductId;
        this.wasteId = wasteId;
    }
    @Generated(hash = 1888728274)
    public OutcomeProduct() {
    }


}
