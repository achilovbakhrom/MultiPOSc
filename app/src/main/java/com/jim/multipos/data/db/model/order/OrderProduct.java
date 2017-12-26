package com.jim.multipos.data.db.model.order;

import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import lombok.Data;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.products.ProductDao;
import org.greenrobot.greendao.annotation.NotNull;
import com.jim.multipos.data.db.model.products.VendorDao;
import com.jim.multipos.data.db.model.DiscountDao;
import com.jim.multipos.data.db.model.ServiceFeeDao;

/**
 * Created by developer on 20.12.2017.
 */
@Entity(nameInDb = "ORDERPRODUCT", active = true)
@Data
public class OrderProduct {
    @Id(autoincrement = true)
    private Long id;
    private long orderId;

    private long productId;
    @ToOne(joinProperty = "productId")
    private Product product;

    private long vendorId;
    @ToOne(joinProperty = "vendorId")
    private Vendor vendor;

    private double cost;
    private double price;
    private int count;
    private double sum;
    private double firstValueChanger;
    private double secondValueChanger;
    private String discription;

    private long discountId;
    @ToOne(joinProperty = "discountId")
    private Discount discount;

    private long serviceFeeId;
    @ToOne(joinProperty = "serviceFeeId")
    private ServiceFee serviceFee;
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
    @Generated(hash = 225933291)
    public void setVendor(@NotNull Vendor vendor) {
        if (vendor == null) {
            throw new DaoException(
                    "To-one property 'vendorId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.vendor = vendor;
            vendorId = vendor.getId();
            vendor__resolvedKey = vendorId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1377213603)
    public Vendor getVendor() {
        long __key = this.vendorId;
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
    @Generated(hash = 1202438341)
    public void setProduct(@NotNull Product product) {
        if (product == null) {
            throw new DaoException(
                    "To-one property 'productId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.product = product;
            productId = product.getId();
            product__resolvedKey = productId;
        }
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 926515087)
    public Product getProduct() {
        long __key = this.productId;
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
    public long getServiceFeeId() {
        return this.serviceFeeId;
    }
    public void setServiceFeeId(long serviceFeeId) {
        this.serviceFeeId = serviceFeeId;
    }
    public long getDiscountId() {
        return this.discountId;
    }
    public void setDiscountId(long discountId) {
        this.discountId = discountId;
    }
    public String getDiscription() {
        return this.discription;
    }
    public void setDiscription(String discription) {
        this.discription = discription;
    }
    public double getSum() {
        return this.sum;
    }
    public void setSum(double sum) {
        this.sum = sum;
    }
    public int getCount() {
        return this.count;
    }
    public void setCount(int count) {
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
    public long getVendorId() {
        return this.vendorId;
    }
    public void setVendorId(long vendorId) {
        this.vendorId = vendorId;
    }
    public long getProductId() {
        return this.productId;
    }
    public void setProductId(long productId) {
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
    public double getSecondValueChanger() {
        return this.secondValueChanger;
    }
    public void setSecondValueChanger(double secondValueChanger) {
        this.secondValueChanger = secondValueChanger;
    }
    public double getFirstValueChanger() {
        return this.firstValueChanger;
    }
    public void setFirstValueChanger(double firstValueChanger) {
        this.firstValueChanger = firstValueChanger;
    }

    @Generated(hash = 1818552344)
    public OrderProduct() {
    }
    @Generated(hash = 414917454)
    public OrderProduct(Long id, long orderId, long productId, long vendorId, double cost, double price,
            int count, double sum, double firstValueChanger, double secondValueChanger, String discription,
            long discountId, long serviceFeeId) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.vendorId = vendorId;
        this.cost = cost;
        this.price = price;
        this.count = count;
        this.sum = sum;
        this.firstValueChanger = firstValueChanger;
        this.secondValueChanger = secondValueChanger;
        this.discription = discription;
        this.discountId = discountId;
        this.serviceFeeId = serviceFeeId;
    }

}
