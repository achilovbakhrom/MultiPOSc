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
public class Product implements NamePhotoPathId, Editable {
    @Id
    private String id;
    private String name;
    private double price;
    private double cost;
    private String barcode;
    private String sku;
    private String photoPath;
    private boolean isActive;
    private boolean isRecipe;
    private boolean isTaxed;
    private String priceCurrencyId;
    @ToOne(joinProperty = "priceCurrencyId")
    private Currency priceCurrency;
    private String costCurrencyId;
    @ToOne(joinProperty = "costCurrencyId")
    private Currency costCurrency;
    private String classId;
    @ToOne(joinProperty = "classId")
    private ProductClass productClass;
    private String mainUnitId;
    @ToOne(joinProperty = "mainUnitId")
    private Unit mainUnit;
    @ToMany
    @JoinEntity(entity = SubUnitsList.class,
            sourceProperty = "productId",
            targetProperty = "unitId")
    private List<Unit> subUnits;
    private String subCategoryId;
    @Property
    private String vendorId;
    @ToOne(joinProperty = "vendorId")
    private Vendor vendor;
    private String description;
    private String newVersionId;
    private boolean isNewVersion;
    @ToMany(joinProperties = {
            @JoinProperty(
                    name = "id", referencedName = "recipeId"
            )
    })
    private List<Recipe> ingredients;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 694336451)
    private transient ProductDao myDao;
    @Generated(hash = 1476600865)
    private transient String productClass__resolvedKey;
    @Generated(hash = 1502344423)
    private transient String mainUnit__resolvedKey;
    @Generated(hash = 224193883)
    private transient String vendor__resolvedKey;
    @Generated(hash = 1771668663)
    private transient String priceCurrency__resolvedKey;
    @Generated(hash = 1126972930)
    private transient String costCurrency__resolvedKey;


    public Product() {
        id = UUID.randomUUID().toString();
    }

    public Product(String name, double price, double cost, String barcode, String sku, String photoPath, boolean isActive, boolean hasRecipe,
                   String priceCurrencyId, String costCurrencyId, String classId, String mainUnitId, String subCategoryId, String vendorId, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.barcode = barcode;
        this.sku = sku;
        this.photoPath = photoPath;
        this.isActive = isActive;
        this.isRecipe = hasRecipe;
        this.priceCurrencyId = priceCurrencyId;
        this.costCurrencyId = costCurrencyId;
        this.classId = classId;
        this.mainUnitId = mainUnitId;
        this.subCategoryId = subCategoryId;
        this.vendorId = vendorId;
        this.description = description;
    }

    @Generated(hash = 1977935556)
    public Product(String id, String name, double price, double cost, String barcode, String sku, String photoPath, boolean isActive, boolean isRecipe,
            boolean isTaxed, String priceCurrencyId, String costCurrencyId, String classId, String mainUnitId, String subCategoryId, String vendorId,
            String description, String newVersionId, boolean isNewVersion) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.barcode = barcode;
        this.sku = sku;
        this.photoPath = photoPath;
        this.isActive = isActive;
        this.isRecipe = isRecipe;
        this.isTaxed = isTaxed;
        this.priceCurrencyId = priceCurrencyId;
        this.costCurrencyId = costCurrencyId;
        this.classId = classId;
        this.mainUnitId = mainUnitId;
        this.subCategoryId = subCategoryId;
        this.vendorId = vendorId;
        this.description = description;
        this.newVersionId = newVersionId;
        this.isNewVersion = isNewVersion;
    }

    @Override
    public String getNewVersionId() {
        return newVersionId;
    }

    @Override
    public void setNewVersionId(String newVersionId) {
        this.newVersionId = newVersionId;
    }

    @Override
    public boolean isNewVersion() {
        return isNewVersion;
    }

    @Override
    public void isNewVersion(boolean isNewVersion) {
        this.isNewVersion = isNewVersion;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getBarcode() {
        return this.barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSku() {
        return this.sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Override
    public String getPhotoPath() {
        return this.photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public boolean getActive() {
        return this.isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getRecipe() {
        return this.isRecipe;
    }

    public void setRecipe(boolean recipe) {
        this.isRecipe = recipe;
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

    public String getClassId() {
        return this.classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getMainUnitId() {
        return this.mainUnitId;
    }

    public void setMainUnitId(String mainUnitId) {
        this.mainUnitId = mainUnitId;
    }

    public String getSubCategoryId() {
        return this.subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getVendorId() {
        return this.vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 768610995)
    public ProductClass getProductClass() {
        String __key = this.classId;
        if (productClass__resolvedKey == null || productClass__resolvedKey != __key) {
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 587064025)
    public void setProductClass(ProductClass productClass) {
        synchronized (this) {
            this.productClass = productClass;
            classId = productClass == null ? null : productClass.getId();
            productClass__resolvedKey = classId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 603098074)
    public Unit getMainUnit() {
        String __key = this.mainUnitId;
        if (mainUnit__resolvedKey == null || mainUnit__resolvedKey != __key) {
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1689706463)
    public void setMainUnit(Unit mainUnit) {
        synchronized (this) {
            this.mainUnit = mainUnit;
            mainUnitId = mainUnit == null ? null : mainUnit.getId();
            mainUnit__resolvedKey = mainUnitId;
        }
    }


    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 2069317337)
    public Vendor getVendor() {
        String __key = this.vendorId;
        if (vendor__resolvedKey == null || vendor__resolvedKey != __key) {
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 332557200)
    public void setVendor(Vendor vendor) {
        synchronized (this) {
            this.vendor = vendor;
            vendorId = vendor == null ? null : vendor.getId();
            vendor__resolvedKey = vendorId;
        }
    }

    public String getPriceCurrencyId() {
        return this.priceCurrencyId;
    }

    public void setPriceCurrencyId(String priceCurrencyId) {
        this.priceCurrencyId = priceCurrencyId;
    }

    public String getCostCurrencyId() {
        return this.costCurrencyId;
    }

    public void setCostCurrencyId(String costCurrencyId) {
        this.costCurrencyId = costCurrencyId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1586819081)
    public Currency getPriceCurrency() {
        String __key = this.priceCurrencyId;
        if (priceCurrency__resolvedKey == null || priceCurrency__resolvedKey != __key) {
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 2052200050)
    public void setPriceCurrency(Currency priceCurrency) {
        synchronized (this) {
            this.priceCurrency = priceCurrency;
            priceCurrencyId = priceCurrency == null ? null : priceCurrency.getId();
            priceCurrency__resolvedKey = priceCurrencyId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1578724695)
    public Currency getCostCurrency() {
        String __key = this.costCurrencyId;
        if (costCurrency__resolvedKey == null || costCurrency__resolvedKey != __key) {
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1963206481)
    public void setCostCurrency(Currency costCurrency) {
        synchronized (this) {
            this.costCurrency = costCurrency;
            costCurrencyId = costCurrency == null ? null : costCurrency.getId();
            costCurrency__resolvedKey = costCurrencyId;
        }
    }

    public boolean getTaxed() {
        return this.isTaxed;
    }

    public void setTaxed(boolean isTaxed) {
        this.isTaxed = isTaxed;
    }

    public boolean getIsTaxed() {
        return this.isTaxed;
    }

    public void setIsTaxed(boolean isTaxed) {
        this.isTaxed = isTaxed;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1171535257)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getProductDao() : null;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
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
                if (subUnits == null) {
                    subUnits = subUnitsNew;
                }
            }
        }
        return subUnits;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 183837919)
    public synchronized void resetIngredients() {
        ingredients = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 99871902)
    public List<Recipe> getIngredients() {
        if (ingredients == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RecipeDao targetDao = daoSession.getRecipeDao();
            List<Recipe> ingredientsNew = targetDao._queryProduct_Ingredients(id);
            synchronized (this) {
                if(ingredients == null) {
                    ingredients = ingredientsNew;
                }
            }
        }
        return ingredients;
    }

    public boolean getIsRecipe() {
        return this.isRecipe;
    }

    public void setIsRecipe(boolean isRecipe) {
        this.isRecipe = isRecipe;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsNewVersion() {
        return this.isNewVersion;
    }

    public void setIsNewVersion(boolean isNewVersion) {
        this.isNewVersion = isNewVersion;
    }

}
