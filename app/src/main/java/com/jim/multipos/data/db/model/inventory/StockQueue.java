package com.jim.multipos.data.db.model.inventory;

import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.products.VendorDao;
import com.jim.multipos.data.db.model.products.ProductDao;

@Entity(active = true, nameInDb = "STOCK_QUEUE")
public class StockQueue {
    @Id(autoincrement = true)
    private Long id;
    private double incomeCount;
    private double available;
    private boolean closed;
    private String stockId;
    private double cost;
    private long incomeProductDate;
    private long createdProductDate;
    private long expiredProductDate;

    private Long incomeId;
    @ToOne(joinProperty = "incomeId")
    private IncomeProduct incomeProduct;

    private Long vendorId;
    @ToOne(joinProperty = "vendorId")
    private Vendor vendor;

    private Long productId;
    @ToOne(joinProperty = "productId")
    private Product product;

    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "stockId")})
    private List<DetialCount> detialCounts;

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
    @Generated(hash = 450053932)
    public synchronized void resetDetialCounts() {
        detialCounts = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1739065611)
    public List<DetialCount> getDetialCounts() {
        if (detialCounts == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DetialCountDao targetDao = daoSession.getDetialCountDao();
            List<DetialCount> detialCountsNew = targetDao._queryStockQueue_DetialCounts(id);
            synchronized (this) {
                if(detialCounts == null) {
                    detialCounts = detialCountsNew;
                }
            }
        }
        return detialCounts;
    }

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
    @Generated(hash = 332557200)
    public void setVendor(Vendor vendor) {
        synchronized (this) {
            this.vendor = vendor;
            vendorId = vendor == null ? null : vendor.getId();
            vendor__resolvedKey = vendorId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1192552702)
    public Vendor getVendor() {
        Long __key = this.vendorId;
        if (vendor__resolvedKey == null || !vendor__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VendorDao targetDao = daoSession.getVendorDao();
            Vendor vendorNew = targetDao.load(__key);
            synchronized (this) {
                vendor = vendorNew;
                vendor__resolvedKey = __key;
            }
        }
        return vendor;
    }

    @Generated(hash = 1022035388)
    private transient Long vendor__resolvedKey;

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 85916759)
    public void setIncomeProduct(IncomeProduct incomeProduct) {
        synchronized (this) {
            this.incomeProduct = incomeProduct;
            incomeId = incomeProduct == null ? null : incomeProduct.getId();
            incomeProduct__resolvedKey = incomeId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1764607933)
    public IncomeProduct getIncomeProduct() {
        Long __key = this.incomeId;
        if (incomeProduct__resolvedKey == null || !incomeProduct__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IncomeProductDao targetDao = daoSession.getIncomeProductDao();
            IncomeProduct incomeProductNew = targetDao.load(__key);
            synchronized (this) {
                incomeProduct = incomeProductNew;
                incomeProduct__resolvedKey = __key;
            }
        }
        return incomeProduct;
    }

    @Generated(hash = 704898610)
    private transient Long incomeProduct__resolvedKey;

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1760029467)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStockQueueDao() : null;
    }

    /** Used for active entity operations. */
    @Generated(hash = 1355301848)
    private transient StockQueueDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getVendorId() {
        return this.vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getIncomeId() {
        return this.incomeId;
    }

    public void setIncomeId(Long incomeId) {
        this.incomeId = incomeId;
    }

    public Long getExpiredProductDate() {
        return this.expiredProductDate;
    }

    public void setExpiredProductDate(long expiredProductDate) {
        this.expiredProductDate = expiredProductDate;
    }

    public Long getCreatedProductDate() {
        return this.createdProductDate;
    }

    public void setCreatedProductDate(long createdProductDate) {
        this.createdProductDate = createdProductDate;
    }

    public Long getIncomeProductDate() {
        return this.incomeProductDate;
    }

    public void setIncomeProductDate(long incomeProductDate) {
        this.incomeProductDate = incomeProductDate;
    }

    public double getCost() {
        return this.cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getStockId() {
        return this.stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public boolean getClosed() {
        return this.closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public double getAvailable() {
        return this.available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public double getIncomeCount() {
        return this.incomeCount;
    }

    public void setIncomeCount(double incomeCount) {
        this.incomeCount = incomeCount;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 339598944)
    public StockQueue(Long id, double incomeCount, double available, boolean closed,
            String stockId, double cost, long incomeProductDate, long createdProductDate,
            long expiredProductDate, Long incomeId, Long vendorId, Long productId) {
        this.id = id;
        this.incomeCount = incomeCount;
        this.available = available;
        this.closed = closed;
        this.stockId = stockId;
        this.cost = cost;
        this.incomeProductDate = incomeProductDate;
        this.createdProductDate = createdProductDate;
        this.expiredProductDate = expiredProductDate;
        this.incomeId = incomeId;
        this.vendorId = vendorId;
        this.productId = productId;
    }

    @Generated(hash = 523114700)
    public StockQueue() {
    }
}
