package com.jim.multipos.data.db.model.order;

import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.inventory.WarehouseOperations;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.products.ProductDao;
import com.jim.multipos.data.db.model.products.VendorDao;
import com.jim.multipos.data.db.model.DiscountDao;
import com.jim.multipos.data.db.model.ServiceFeeDao;
import com.jim.multipos.data.db.model.inventory.WarehouseOperationsDao;

/**
 * Created by developer on 20.12.2017.
 */
@Entity(nameInDb = "ORDERPRODUCT", active = true)
public class OrderProduct {
    @Id(autoincrement = true)
    private Long id;
    private long orderId;

    private Long productId;
    @ToOne(joinProperty = "productId")
    private Product product;

    private Long vendorId;
    @ToOne(joinProperty = "vendorId")
    private Vendor vendor;

    private double cost;
    private double price;
    private double count;
    private double sum;
    private double discountAmount;
    private double serviceAmount;
    private String discription;

    private Long discountId;
    @ToOne(joinProperty = "discountId")
    private Discount discount;

    private Long serviceFeeId;
    @ToOne(joinProperty = "serviceFeeId")
    private ServiceFee serviceFee;

    private Long warehouseGetId;
    @ToOne(joinProperty = "warehouseGetId")
    WarehouseOperations warehouseGet;

    private Long warehouseReturnId;
    @ToOne(joinProperty = "warehouseReturnId")
    WarehouseOperations warehouseReturn;


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
    @Generated(hash = 233696324)
    public void setServiceFee(ServiceFee serviceFee) {
        synchronized (this) {
            this.serviceFee = serviceFee;
            serviceFeeId = serviceFee == null ? null : serviceFee.getId();
            serviceFee__resolvedKey = serviceFeeId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 416102326)
    public ServiceFee getServiceFee() {
        Long __key = this.serviceFeeId;
        if (serviceFee__resolvedKey == null
                || !serviceFee__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
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
    @Generated(hash = 761713010)
    public void setDiscount(Discount discount) {
        synchronized (this) {
            this.discount = discount;
            discountId = discount == null ? null : discount.getId();
            discount__resolvedKey = discountId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1770890420)
    public Discount getDiscount() {
        Long __key = this.discountId;
        if (discount__resolvedKey == null || !discount__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
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
    @Generated(hash = 1412736234)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOrderProductDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 884699745)
    private transient OrderProductDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    @Generated(hash = 667520458)
    private transient Long warehouseReturn__resolvedKey;
    @Generated(hash = 1872662047)
    private transient Long warehouseGet__resolvedKey;
    public Long getServiceFeeId() {
        return this.serviceFeeId;
    }
    public void setServiceFeeId(Long serviceFeeId) {
        this.serviceFeeId = serviceFeeId;
    }
    public Long getDiscountId() {
        return this.discountId;
    }
    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }
    public String getDiscription() {
        return this.discription;
    }
    public void setDiscription(String discription) {
        this.discription = discription;
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
    public double getSum() {
        return this.sum;
    }
    public void setSum(double sum) {
        this.sum = sum;
    }
    public double getCount() {
        return this.count;
    }
    public void setCount(double count) {
        this.count = count;
    }
    public double getPrice() {
        return this.price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public double getCost() {
        return this.cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
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
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 218199277)
    public void setWarehouseReturn(WarehouseOperations warehouseReturn) {
        synchronized (this) {
            this.warehouseReturn = warehouseReturn;
            warehouseReturnId = warehouseReturn == null ? null : warehouseReturn.getId();
            warehouseReturn__resolvedKey = warehouseReturnId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1937446373)
    public WarehouseOperations getWarehouseReturn() {
        Long __key = this.warehouseReturnId;
        if (warehouseReturn__resolvedKey == null
                || !warehouseReturn__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WarehouseOperationsDao targetDao = daoSession.getWarehouseOperationsDao();
            WarehouseOperations warehouseReturnNew = targetDao.load(__key);
            synchronized (this) {
                warehouseReturn = warehouseReturnNew;
                warehouseReturn__resolvedKey = __key;
            }
        }
        return warehouseReturn;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 96141712)
    public void setWarehouseGet(WarehouseOperations warehouseGet) {
        synchronized (this) {
            this.warehouseGet = warehouseGet;
            warehouseGetId = warehouseGet == null ? null : warehouseGet.getId();
            warehouseGet__resolvedKey = warehouseGetId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1393798737)
    public WarehouseOperations getWarehouseGet() {
        Long __key = this.warehouseGetId;
        if (warehouseGet__resolvedKey == null || !warehouseGet__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WarehouseOperationsDao targetDao = daoSession.getWarehouseOperationsDao();
            WarehouseOperations warehouseGetNew = targetDao.load(__key);
            synchronized (this) {
                warehouseGet = warehouseGetNew;
                warehouseGet__resolvedKey = __key;
            }
        }
        return warehouseGet;
    }
    public Long getWarehouseReturnId() {
        return this.warehouseReturnId;
    }
    public void setWarehouseReturnId(Long warehouseReturnId) {
        this.warehouseReturnId = warehouseReturnId;
    }
    public Long getWarehouseGetId() {
        return this.warehouseGetId;
    }
    public void setWarehouseGetId(Long warehouseGetId) {
        this.warehouseGetId = warehouseGetId;
    }
    @Generated(hash = 1853748004)
    public OrderProduct(Long id, long orderId, Long productId, Long vendorId, double cost,
            double price, double count, double sum, double discountAmount,
            double serviceAmount, String discription, Long discountId, Long serviceFeeId,
            Long warehouseGetId, Long warehouseReturnId) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.vendorId = vendorId;
        this.cost = cost;
        this.price = price;
        this.count = count;
        this.sum = sum;
        this.discountAmount = discountAmount;
        this.serviceAmount = serviceAmount;
        this.discription = discription;
        this.discountId = discountId;
        this.serviceFeeId = serviceFeeId;
        this.warehouseGetId = warehouseGetId;
        this.warehouseReturnId = warehouseReturnId;
    }
    @Generated(hash = 1818552344)
    public OrderProduct() {
    }

}
