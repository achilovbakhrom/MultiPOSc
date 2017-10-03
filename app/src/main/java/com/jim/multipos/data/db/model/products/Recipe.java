package com.jim.multipos.data.db.model.products;

import com.jim.multipos.data.db.model.unit.Unit;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import java.util.UUID;

import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.unit.UnitDao;

/**
 * Created by DEV on 13.09.2017.
 */

@Entity(nameInDb = "RECIPE", active = true)
public class Recipe {
    @Id
    private String id;
    @Property
    private String recipeId;
    private String ingredientId;
    @ToOne(joinProperty = "ingredientId")
    private Product ingredient;
    private float factorRoot;
    private String unitId;
    @ToOne(joinProperty = "unitId")
    private Unit ingredientUnit;
    @Generated(hash = 634718215)
    private transient String ingredientUnit__resolvedKey;
    @Generated(hash = 1618559533)
    private transient String ingredient__resolvedKey;
    /** Used for active entity operations. */
    @Generated(hash = 1947830398)
    private transient RecipeDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;


    @Keep
    public Recipe() {
        id = UUID.randomUUID().toString();
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
    @Generated(hash = 110911862)
    public void setIngredientUnit(Unit ingredientUnit) {
        synchronized (this) {
            this.ingredientUnit = ingredientUnit;
            unitId = ingredientUnit == null ? null : ingredientUnit.getId();
            ingredientUnit__resolvedKey = unitId;
        }
    }


    /** To-one relationship, resolved on first access. */
    @Generated(hash = 680742690)
    public Unit getIngredientUnit() {
        String __key = this.unitId;
        if (ingredientUnit__resolvedKey == null
                || ingredientUnit__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UnitDao targetDao = daoSession.getUnitDao();
            Unit ingredientUnitNew = targetDao.load(__key);
            synchronized (this) {
                ingredientUnit = ingredientUnitNew;
                ingredientUnit__resolvedKey = __key;
            }
        }
        return ingredientUnit;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 724540630)
    public void setIngredient(Product ingredient) {
        synchronized (this) {
            this.ingredient = ingredient;
            ingredientId = ingredient == null ? null : ingredient.getId();
            ingredient__resolvedKey = ingredientId;
        }
    }


    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1106644384)
    public Product getIngredient() {
        String __key = this.ingredientId;
        if (ingredient__resolvedKey == null || ingredient__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ProductDao targetDao = daoSession.getProductDao();
            Product ingredientNew = targetDao.load(__key);
            synchronized (this) {
                ingredient = ingredientNew;
                ingredient__resolvedKey = __key;
            }
        }
        return ingredient;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1484851246)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecipeDao() : null;
    }


    public String getUnitId() {
        return this.unitId;
    }


    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }


    public float getFactorRoot() {
        return this.factorRoot;
    }


    public void setFactorRoot(float factorRoot) {
        this.factorRoot = factorRoot;
    }


    public String getIngredientId() {
        return this.ingredientId;
    }


    public void setIngredientId(String ingredientId) {
        this.ingredientId = ingredientId;
    }


    public String getRecipeId() {
        return this.recipeId;
    }


    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }


    public String getId() {
        return this.id;
    }


    public void setId(String id) {
        this.id = id;
    }


    @Generated(hash = 2019123484)
    public Recipe(String id, String recipeId, String ingredientId,
            float factorRoot, String unitId) {
        this.id = id;
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.factorRoot = factorRoot;
        this.unitId = unitId;
    }

}
