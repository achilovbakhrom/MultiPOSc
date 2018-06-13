package com.jim.multipos.data.db.model.inventory;

import com.jim.multipos.data.db.model.consignment.Invoice;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.consignment.InvoiceDao;
import com.jim.multipos.data.db.model.products.ProductDao;
import com.jim.multipos.data.db.model.products.VendorDao;

@Entity(active = true, nameInDb = "INCOMEPRODUCT")
public class IncomeProduct {
    //INCOME_TYPES
    public static int INVOICE_PRODUCT = 1;
    public static int SURPLUS_PRODUCT = 2;
    public static int RETURNED_PRODUCT = 3;


    @Id(autoincrement = true)
    private Long id;
    private Double costValue;
    private Double countValue;
    private long incomeDate;
    private String description;
    private int incomeType;


    //REFERENCE FOR GETTING STOCK QUEUE
    private Long stockQueueId;
    @ToOne(joinProperty = "stockQueueId")
    private StockQueue stockQueue;

    //IF IT INCOMED FROM INVOICE
    private Long invoiceId;
    @ToOne(joinProperty = "invoiceId")
    private Invoice invoice;

    //IF IT INCOMED FROM SURPLUS
    @Unique
    private Long surplusId;
    @ToOne(joinProperty = "surplusId")
    private SurplusOperation surplusOperation;

    //IF IT INCOMED FROM INVOICE
    private Long productId;
    @ToOne(joinProperty = "productId")
    private Product product;

    private Long vendorId;
    @ToOne(joinProperty = "vendorId")
    private Vendor vendor;
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
    @Generated(hash = 1346756260)
    public void setSurplusOperation(SurplusOperation surplusOperation) {
        synchronized (this) {
            this.surplusOperation = surplusOperation;
            surplusId = surplusOperation == null ? null : surplusOperation.getId();
            surplusOperation__resolvedKey = surplusId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 541638173)
    public SurplusOperation getSurplusOperation() {
        Long __key = this.surplusId;
        if (surplusOperation__resolvedKey == null
                || !surplusOperation__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SurplusOperationDao targetDao = daoSession.getSurplusOperationDao();
            SurplusOperation surplusOperationNew = targetDao.load(__key);
            synchronized (this) {
                surplusOperation = surplusOperationNew;
                surplusOperation__resolvedKey = __key;
            }
        }
        return surplusOperation;
    }
    @Generated(hash = 1049919068)
    private transient Long surplusOperation__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 749725237)
    public void setInvoice(Invoice invoice) {
        synchronized (this) {
            this.invoice = invoice;
            invoiceId = invoice == null ? null : invoice.getId();
            invoice__resolvedKey = invoiceId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 710097085)
    public Invoice getInvoice() {
        Long __key = this.invoiceId;
        if (invoice__resolvedKey == null || !invoice__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InvoiceDao targetDao = daoSession.getInvoiceDao();
            Invoice invoiceNew = targetDao.load(__key);
            synchronized (this) {
                invoice = invoiceNew;
                invoice__resolvedKey = __key;
            }
        }
        return invoice;
    }
    @Generated(hash = 694408149)
    private transient Long invoice__resolvedKey;
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1506102758)
    public void setStockQueue(StockQueue stockQueue) {
        synchronized (this) {
            this.stockQueue = stockQueue;
            stockQueueId = stockQueue == null ? null : stockQueue.getId();
            stockQueue__resolvedKey = stockQueueId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 876058326)
    public StockQueue getStockQueue() {
        Long __key = this.stockQueueId;
        if (stockQueue__resolvedKey == null
                || !stockQueue__resolvedKey.equals(__key)) {
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
    @Generated(hash = 616965070)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getIncomeProductDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 1438266101)
    private transient IncomeProductDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getVendorId() {
        return this.vendorId;
    }
    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }
    public Long getProductId() {
        return this.productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Long getSurplusId() {
        return this.surplusId;
    }
    public void setSurplusId(Long surplusId) {
        this.surplusId = surplusId;
    }
    public Long getInvoiceId() {
        return this.invoiceId;
    }
    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
    public Long getStockQueueId() {
        return this.stockQueueId;
    }
    public void setStockQueueId(Long stockQueueId) {
        this.stockQueueId = stockQueueId;
    }
    public int getIncomeType() {
        return this.incomeType;
    }
    public void setIncomeType(int incomeType) {
        this.incomeType = incomeType;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public long getIncomeDate() {
        return this.incomeDate;
    }
    public void setIncomeDate(long incomeDate) {
        this.incomeDate = incomeDate;
    }
    public Double getCountValue() {
        return this.countValue;
    }
    public void setCountValue(Double countValue) {
        this.countValue = countValue;
    }
    public Double getCostValue() {
        return this.costValue;
    }
    public void setCostValue(Double costValue) {
        this.costValue = costValue;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 506452214)
    public IncomeProduct(Long id, Double costValue, Double countValue,
            long incomeDate, String description, int incomeType, Long stockQueueId,
            Long invoiceId, Long surplusId, Long productId, Long vendorId) {
        this.id = id;
        this.costValue = costValue;
        this.countValue = countValue;
        this.incomeDate = incomeDate;
        this.description = description;
        this.incomeType = incomeType;
        this.stockQueueId = stockQueueId;
        this.invoiceId = invoiceId;
        this.surplusId = surplusId;
        this.productId = productId;
        this.vendorId = vendorId;
    }
    @Generated(hash = 1411256302)
    public IncomeProduct() {
    }



}
