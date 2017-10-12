package com.jim.multipos.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "DISCOUNT", active = true)
public class Discount {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String amountType;
    private double amount;
    private String applyType;
    private long barcode;
    private double maxAmount;
    private double minAmount;
    private boolean isTaxed;
    private boolean isActive;
    private boolean isWholesale;
    private boolean hasDiscountCode;
    private String startDate;
    private String endDate;
    private long requireNimber;
    private String qualificationType;
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
    @Generated(hash = 915725086)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDiscountDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 1583145616)
    private transient DiscountDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public String getQualificationType() {
        return this.qualificationType;
    }
    public void setQualificationType(String qualificationType) {
        this.qualificationType = qualificationType;
    }
    public long getRequireNimber() {
        return this.requireNimber;
    }
    public void setRequireNimber(long requireNimber) {
        this.requireNimber = requireNimber;
    }
    public String getEndDate() {
        return this.endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getStartDate() {
        return this.startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public boolean getHasDiscountCode() {
        return this.hasDiscountCode;
    }
    public void setHasDiscountCode(boolean hasDiscountCode) {
        this.hasDiscountCode = hasDiscountCode;
    }
    public boolean getIsWholesale() {
        return this.isWholesale;
    }
    public void setIsWholesale(boolean isWholesale) {
        this.isWholesale = isWholesale;
    }
    public boolean getIsActive() {
        return this.isActive;
    }
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    public boolean getIsTaxed() {
        return this.isTaxed;
    }
    public void setIsTaxed(boolean isTaxed) {
        this.isTaxed = isTaxed;
    }
    public double getMinAmount() {
        return this.minAmount;
    }
    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }
    public double getMaxAmount() {
        return this.maxAmount;
    }
    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }
    public long getBarcode() {
        return this.barcode;
    }
    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }
    public String getApplyType() {
        return this.applyType;
    }
    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }
    public double getAmount() {
        return this.amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getAmountType() {
        return this.amountType;
    }
    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1218280432)
    public Discount(Long id, String name, String amountType, double amount,
            String applyType, long barcode, double maxAmount, double minAmount,
            boolean isTaxed, boolean isActive, boolean isWholesale,
            boolean hasDiscountCode, String startDate, String endDate,
            long requireNimber, String qualificationType) {
        this.id = id;
        this.name = name;
        this.amountType = amountType;
        this.amount = amount;
        this.applyType = applyType;
        this.barcode = barcode;
        this.maxAmount = maxAmount;
        this.minAmount = minAmount;
        this.isTaxed = isTaxed;
        this.isActive = isActive;
        this.isWholesale = isWholesale;
        this.hasDiscountCode = hasDiscountCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requireNimber = requireNimber;
        this.qualificationType = qualificationType;
    }
    @Generated(hash = 1777606421)
    public Discount() {
    }
}
