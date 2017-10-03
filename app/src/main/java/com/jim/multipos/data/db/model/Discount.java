package com.jim.multipos.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "DISCOUNT", active = true)
public class Discount {
    @Id
    private String id;
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
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1583145616)
    private transient DiscountDao myDao;

    public Discount(String name, String amountType, double amount, String applyType, long barcode, double maxAmount, double minAmount, boolean isTaxed, boolean isActive, boolean isWholesale,
                    boolean hasDiscountCode, String startDate, String endDate, long requireNimber, String qualificationType) {
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

    @Generated(hash = 1851300924)
    public Discount(String id, String name, String amountType, double amount, String applyType, long barcode, double maxAmount, double minAmount, boolean isTaxed, boolean isActive,
            boolean isWholesale, boolean hasDiscountCode, String startDate, String endDate, long requireNimber, String qualificationType) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public boolean isTaxed() {
        return isTaxed;
    }

    public void setTaxed(boolean taxed) {
        isTaxed = taxed;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isWholesale() {
        return isWholesale;
    }

    public void setWholesale(boolean wholesale) {
        isWholesale = wholesale;
    }

    public boolean isHasDiscountCode() {
        return hasDiscountCode;
    }

    public void setHasDiscountCode(boolean hasDiscountCode) {
        this.hasDiscountCode = hasDiscountCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public long getRequireNimber() {
        return requireNimber;
    }

    public void setRequireNimber(long requireNimber) {
        this.requireNimber = requireNimber;
    }

    public String getQualificationType() {
        return qualificationType;
    }

    public void setQualificationType(String qualificationType) {
        this.qualificationType = qualificationType;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getIsTaxed() {
        return this.isTaxed;
    }

    public void setIsTaxed(boolean isTaxed) {
        this.isTaxed = isTaxed;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsWholesale() {
        return this.isWholesale;
    }

    public void setIsWholesale(boolean isWholesale) {
        this.isWholesale = isWholesale;
    }

    public boolean getHasDiscountCode() {
        return this.hasDiscountCode;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 915725086)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDiscountDao() : null;
    }
}
