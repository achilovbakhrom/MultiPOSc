package com.jim.multipos.data.db.model.products;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import java.util.UUID;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.Vendor;

import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.intosystem.Editable;
import com.jim.multipos.data.db.model.intosystem.NamePhotoPathId;
import com.jim.multipos.data.db.model.unit.SubUnitsList;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.VendorDao;
import com.jim.multipos.data.db.model.unit.UnitDao;
import com.jim.multipos.data.db.model.ProductClassDao;
import com.jim.multipos.data.db.model.currency.CurrencyDao;
import com.jim.multipos.data.db.model.DaoSession;

/**
 * Created by DEV on 26.08.2017.
 */
@Entity(nameInDb = "PRODUCT", active = true)
public class Product implements Editable {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private Double price;
    private Double cost;
    private Long createdDate;
    private String barcode;
    private String sku;
    private String photoPath;
    private Boolean isActive;
    private Boolean isNotModified;
    private Boolean isDeleted;
    private Long priceCurrencyId;
    @ToOne(joinProperty = "priceCurrencyId")
    private Currency priceCurrency;
    private Long costCurrencyId;
    @ToOne(joinProperty = "costCurrencyId")
    private Currency costCurrency;
    private Long classId;
    @ToOne(joinProperty = "classId")
    private ProductClass productClass;
    private Long mainUnitId;
    @ToOne(joinProperty = "mainUnitId")
    private Unit mainUnit;
    @ToMany
    @JoinEntity(entity = SubUnitsList.class,
            sourceProperty = "productId",
            targetProperty = "unitId")
    private List<Unit> subUnits;
    private Long subCategoryId;
    @Property
    private Long vendorId;
    @ToOne(joinProperty = "vendorId")
    private Vendor vendor;
    private String description;
    private Long rootId;
    private Long parentId;
    @Generated(hash = 1022035388)
    private transient Long vendor__resolvedKey;
    @Generated(hash = 1037669877)
    private transient Long mainUnit__resolvedKey;
    @Generated(hash = 1979699144)
    private transient Long productClass__resolvedKey;
    @Generated(hash = 2076466863)
    private transient Long costCurrency__resolvedKey;
    @Generated(hash = 348405744)
    private transient Long priceCurrency__resolvedKey;
    /** Used for active entity operations. */
    @Generated(hash = 694336451)
    private transient ProductDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    @Generated(hash = 1890278724)
    public Product() {
    }

    @Generated(hash = 2040294256)
    public Product(Long id, String name, Double price, Double cost, Long createdDate, String barcode,
            String sku, String photoPath, Boolean isActive, Boolean isNotModified, Boolean isDeleted,
            Long priceCurrencyId, Long costCurrencyId, Long classId, Long mainUnitId,
            Long subCategoryId, Long vendorId, String description, Long rootId, Long parentId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.createdDate = createdDate;
        this.barcode = barcode;
        this.sku = sku;
        this.photoPath = photoPath;
        this.isActive = isActive;
        this.isNotModified = isNotModified;
        this.isDeleted = isDeleted;
        this.priceCurrencyId = priceCurrencyId;
        this.costCurrencyId = costCurrencyId;
        this.classId = classId;
        this.mainUnitId = mainUnitId;
        this.subCategoryId = subCategoryId;
        this.vendorId = vendorId;
        this.description = description;
        this.rootId = rootId;
        this.parentId = parentId;
    }

    @Override
    public void setId(Long id) {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setActive(boolean active) {

    }

    @Override
    public boolean isDeleted() {
        return false;
    }

    @Override
    public void setDeleted(boolean deleted) {

    }

    @Override
    public boolean isNotModifyted() {
        return false;
    }

    @Override
    public void setNotModifyted(boolean notModifyted) {

    }

    @Override
    public Long getRootId() {
        return this.rootId;
    }

    @Override
    public void setRootId(Long rootId) {

        this.rootId = rootId;
    }

    @Override
    public Long getCreatedDate() {
        return this.createdDate;
    }

    @Override
    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public Long getId() {
        return null;
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
    @Generated(hash = 622239219)
    public synchronized void resetSubUnits() {
        subUnits = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1537469223)
    public List<Unit> getSubUnits() {
        if (subUnits == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UnitDao targetDao = daoSession.getUnitDao();
            List<Unit> subUnitsNew = targetDao._queryProduct_SubUnits(id);
            synchronized (this) {
                if(subUnits == null) {
                    subUnits = subUnitsNew;
                }
            }
        }
        return subUnits;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1689706463)
    public void setMainUnit(Unit mainUnit) {
        synchronized (this) {
            this.mainUnit = mainUnit;
            mainUnitId = mainUnit == null ? null : mainUnit.getId();
            mainUnit__resolvedKey = mainUnitId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 63527950)
    public Unit getMainUnit() {
        Long __key = this.mainUnitId;
        if (mainUnit__resolvedKey == null || !mainUnit__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UnitDao targetDao = daoSession.getUnitDao();
            Unit mainUnitNew = targetDao.load(__key);
            synchronized (this) {
                mainUnit = mainUnitNew;
                mainUnit__resolvedKey = __key;
            }
        }
        return mainUnit;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 587064025)
    public void setProductClass(ProductClass productClass) {
        synchronized (this) {
            this.productClass = productClass;
            classId = productClass == null ? null : productClass.getId();
            productClass__resolvedKey = classId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 977787601)
    public ProductClass getProductClass() {
        Long __key = this.classId;
        if (productClass__resolvedKey == null
                || !productClass__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProductClassDao targetDao = daoSession.getProductClassDao();
            ProductClass productClassNew = targetDao.load(__key);
            synchronized (this) {
                productClass = productClassNew;
                productClass__resolvedKey = __key;
            }
        }
        return productClass;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1963206481)
    public void setCostCurrency(Currency costCurrency) {
        synchronized (this) {
            this.costCurrency = costCurrency;
            costCurrencyId = costCurrency == null ? null : costCurrency.getId();
            costCurrency__resolvedKey = costCurrencyId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 741901330)
    public Currency getCostCurrency() {
        Long __key = this.costCurrencyId;
        if (costCurrency__resolvedKey == null
                || !costCurrency__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CurrencyDao targetDao = daoSession.getCurrencyDao();
            Currency costCurrencyNew = targetDao.load(__key);
            synchronized (this) {
                costCurrency = costCurrencyNew;
                costCurrency__resolvedKey = __key;
            }
        }
        return costCurrency;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2052200050)
    public void setPriceCurrency(Currency priceCurrency) {
        synchronized (this) {
            this.priceCurrency = priceCurrency;
            priceCurrencyId = priceCurrency == null ? null : priceCurrency.getId();
            priceCurrency__resolvedKey = priceCurrencyId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1063663284)
    public Currency getPriceCurrency() {
        Long __key = this.priceCurrencyId;
        if (priceCurrency__resolvedKey == null
                || !priceCurrency__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CurrencyDao targetDao = daoSession.getCurrencyDao();
            Currency priceCurrencyNew = targetDao.load(__key);
            synchronized (this) {
                priceCurrency = priceCurrencyNew;
                priceCurrency__resolvedKey = __key;
            }
        }
        return priceCurrency;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1171535257)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getProductDao() : null;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getVendorId() {
        return this.vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getSubCategoryId() {
        return this.subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Long getMainUnitId() {
        return this.mainUnitId;
    }

    public void setMainUnitId(Long mainUnitId) {
        this.mainUnitId = mainUnitId;
    }

    public Long getClassId() {
        return this.classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getCostCurrencyId() {
        return this.costCurrencyId;
    }

    public void setCostCurrencyId(Long costCurrencyId) {
        this.costCurrencyId = costCurrencyId;
    }

    public Long getPriceCurrencyId() {
        return this.priceCurrencyId;
    }

    public void setPriceCurrencyId(Long priceCurrencyId) {
        this.priceCurrencyId = priceCurrencyId;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean getIsNotModifyted() {
        return this.isNotModified;
    }

    public void setIsNotModifyted(boolean isNotModifyted) {
        this.isNotModified = isNotModifyted;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getPhotoPath() {
        return this.photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getSku() {
        return this.sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getCost() {
        return this.cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getIsNotModified() {
        return this.isNotModified;
    }

    public void setIsNotModified(Boolean isNotModified) {
        this.isNotModified = isNotModified;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
