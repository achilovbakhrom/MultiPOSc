package com.jim.multipos.data.db.model.products;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import com.jim.multipos.data.db.model.ProductClass;

import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.history.ProductHistory;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitDao;
import com.jim.multipos.data.db.model.ProductClassDao;
import com.jim.multipos.data.db.model.currency.CurrencyDao;
import com.jim.multipos.data.db.model.DaoSession;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.jim.multipos.data.db.model.history.ProductHistoryDao;

/**
 * Created by DEV on 26.08.2017.
 */
@Entity(nameInDb = "PRODUCT", active = true)
public class Product  {
    //STOCK KEEP TYPE
    public static final int FIFO = 0; //First In First Out
    public static final int LIFO = 1; //Last In Last Out
    public static final int FEFO = 2; //First Expired First Out

    @Id(autoincrement = true)
    private Long id;
    private String name;
    private Long createdDate;
    private Double price;
    private String barcode;
    private String sku;
    private String photoPath = "";
    private Boolean isActive = true;
    private Boolean isDeleted = false;
    private Long priceCurrencyId;
    private int stockKeepType;
    @ToOne(joinProperty = "priceCurrencyId")
    private Currency priceCurrency;
    private Long classId;
    @ToOne(joinProperty = "classId")
    private ProductClass productClass;
    private Long mainUnitId;
    @ToOne(joinProperty = "mainUnitId")
    private Unit mainUnit;
    private String description;
    private Long categoryId;
    @ToOne(joinProperty = "categoryId")
    private Category category;
    @ToMany(joinProperties = {
            @JoinProperty(
                    name = "id", referencedName = "rootId"
            )
    })
    private List<ProductHistory> productHistoryList;


    @Keep
    public void keepToHistory(){
        if(daoSession!=null){
            ProductHistory productHistory = new ProductHistory();
            productHistory.setName(name);
            productHistory.setCreatedDate(createdDate);
            productHistory.setPrice(price);
            productHistory.setBarcode(barcode);
            productHistory.setSku(sku);
            productHistory.setPhotoPath(photoPath);
            productHistory.setIsActive(isActive);
            productHistory.setIsDeleted(isDeleted);
            productHistory.setPriceCurrencyId(priceCurrencyId);
            productHistory.setClassId(classId);
            productHistory.setMainUnitId(mainUnitId);
            productHistory.setDescription(description);
            productHistory.setCategoryId(categoryId);
            productHistory.setRootId(id);
            productHistory.setEditedAt(System.currentTimeMillis());
            daoSession.getProductHistoryDao().insertOrReplace(productHistory);
        }
    }

     @Keep
    public ProductHistory getProductHistoryForDate(Long date){
        if(daoSession!=null){
            List<ProductHistory> productHistories = getProductHistoryList();
            Collections.sort(productHistories, new Comparator<ProductHistory>() {
                @Override
                public int compare(ProductHistory productHistory, ProductHistory t1) {
                    return productHistory.getEditedAt().compareTo(t1.getEditedAt());
                }
            });
            for(ProductHistory productHistory:productHistories){
                if(date > productHistory.getEditedAt()){
                    return productHistory;
                }
            }
            return null;
        }else {
            new Exception("Gettting History for not saved object exeption").printStackTrace();
            return null;
        }
    }


    @Generated(hash = 1372501278)
    private transient Long category__resolvedKey;
    @Generated(hash = 1037669877)
    private transient Long mainUnit__resolvedKey;
    @Generated(hash = 1979699144)
    private transient Long productClass__resolvedKey;
    @Generated(hash = 348405744)
    private transient Long priceCurrency__resolvedKey;
    /** Used for active entity operations. */
    @Generated(hash = 694336451)
    private transient ProductDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    @Generated(hash = 101822717)
    public Product(Long id, String name, Long createdDate, Double price, String barcode, String sku,
            String photoPath, Boolean isActive, Boolean isDeleted, Long priceCurrencyId,
            int stockKeepType, Long classId, Long mainUnitId, String description, Long categoryId) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.price = price;
        this.barcode = barcode;
        this.sku = sku;
        this.photoPath = photoPath;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.priceCurrencyId = priceCurrencyId;
        this.stockKeepType = stockKeepType;
        this.classId = classId;
        this.mainUnitId = mainUnitId;
        this.description = description;
        this.categoryId = categoryId;
    }

    @Generated(hash = 1890278724)
    public Product() {
    }
    
    @Keep
    public void setId(Long id) {
        this.id = id;
    }

    @Keep
    public boolean isActive() {
        return this.isActive;
    }

    @Keep
    public void setActive(boolean active) {
        this.isActive = active;
    }

    @Keep
    public boolean isDeleted() {
        return this.isDeleted;
    }

    @Keep
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    @Keep
    public Long getCreatedDate() {
        return this.createdDate;
    }

    @Keep
    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }
    @Keep
    public Long getId() {
        return this.id;
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
    @Generated(hash = 1132018243)
    public void setCategory(Category category) {
        synchronized (this) {
            this.category = category;
            categoryId = category == null ? null : category.getId();
            category__resolvedKey = categoryId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 728129201)
    public Category getCategory() {
        Long __key = this.categoryId;
        if (category__resolvedKey == null || !category__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CategoryDao targetDao = daoSession.getCategoryDao();
            Category categoryNew = targetDao.load(__key);
            synchronized (this) {
                category = categoryNew;
                category__resolvedKey = __key;
            }
        }
        return category;
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

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getPriceCurrencyId() {
        return this.priceCurrencyId;
    }

    public void setPriceCurrencyId(Long priceCurrencyId) {
        this.priceCurrencyId = priceCurrencyId;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
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

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStockKeepType() {
        return stockKeepType;
    }

    public void setStockKeepType(int stockKeepType) {
        this.stockKeepType = stockKeepType;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1095827929)
    public synchronized void resetProductHistoryList() {
        productHistoryList = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1791702241)
    public List<ProductHistory> getProductHistoryList() {
        if (productHistoryList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProductHistoryDao targetDao = daoSession.getProductHistoryDao();
            List<ProductHistory> productHistoryListNew = targetDao._queryProduct_ProductHistoryList(id);
            synchronized (this) {
                if(productHistoryList == null) {
                    productHistoryList = productHistoryListNew;
                }
            }
        }
        return productHistoryList;
    }
}
