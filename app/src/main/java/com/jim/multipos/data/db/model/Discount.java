package com.jim.multipos.data.db.model;

import android.support.annotation.Keep;

import com.jim.multipos.data.db.model.history.DiscountHistory;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.jim.multipos.data.db.model.history.DiscountHistoryDao;
import com.jim.multipos.data.db.model.history.VendorHistory;


@Entity(nameInDb = "DISCOUNT", active = true)
public class Discount  {
    public static final int PERCENT = 0;
    public static final int VALUE = 1;
    public static final int ITEM = 0;
    public static final int ORDER = 1;
    public static final int ALL = 2;

    @Id(autoincrement = true)
    private Long id;
    private String name;
    private double amount;
    private int amountType;
    private int usedType;
    private boolean active = true;
    private boolean delete = false;
    private boolean isManual = false;
    private Long createdDate;

    @ToMany(joinProperties = {
            @JoinProperty(
                    name = "id", referencedName = "rootId"
            )
    })
    private List<DiscountHistory> discountHistories;
    /** Used for active entity operations. */
    @Generated(hash = 1583145616)
    private transient DiscountDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    @Generated(hash = 1789882092)
    public Discount(Long id, String name, double amount, int amountType, int usedType,
            boolean active, boolean delete, boolean isManual, Long createdDate) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.amountType = amountType;
        this.usedType = usedType;
        this.active = active;
        this.delete = delete;
        this.isManual = isManual;
        this.createdDate = createdDate;
    }

    @Generated(hash = 1777606421)
    public Discount() {
    }

    @Keep
    public void keepToHistory(){
        if(daoSession!=null){
            DiscountHistory discountHistory = new DiscountHistory();
            discountHistory.setName(name);
            discountHistory.setAmount(amount);
            discountHistory.setAmountType(amountType);
            discountHistory.setUsedType(usedType);
            discountHistory.setActive(active);
            discountHistory.setDelete(delete);
            discountHistory.setIsManual(isManual);
            discountHistory.setCreatedDate(createdDate);
            discountHistory.setRootId(id);
            discountHistory.setEditedAt(System.currentTimeMillis());
            daoSession.getDiscountHistoryDao().insertOrReplace(discountHistory);
        }
    }

    @Keep
    public DiscountHistory getDiscountHistoryForDate(Long date){
        if(daoSession!=null){
            List<DiscountHistory> discountHistories = getDiscountHistories();
            Collections.sort(discountHistories, new Comparator<DiscountHistory>() {
                @Override
                public int compare(DiscountHistory discountHistory, DiscountHistory t1) {
                    return discountHistory.getEditedAt().compareTo(t1.getEditedAt());
                }
            });
            for(DiscountHistory discountHistory:discountHistories){
                if(date > discountHistory.getEditedAt()){
                    return discountHistory;
                }
            }
            return null;
        }else {
            new Exception("Gettting History for not saved object exeption").printStackTrace();
            return null;
        }
    }

    @Keep
    public Discount copy(){
        Discount discount = new Discount();
        discount.setId(id);
        discount.setAmount(amount);
        discount.setName(name);
        discount.setAmountType(amountType);
        discount.setUsedType(usedType);
        discount.setActive(active);
        discount.setCreatedDate(createdDate);
        return discount;
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
    @Generated(hash = 697535990)
    public synchronized void resetDiscountHistories() {
        discountHistories = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 106062254)
    public List<DiscountHistory> getDiscountHistories() {
        if (discountHistories == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DiscountHistoryDao targetDao = daoSession.getDiscountHistoryDao();
            List<DiscountHistory> discountHistoriesNew = targetDao._queryDiscount_DiscountHistories(id);
            synchronized (this) {
                if(discountHistories == null) {
                    discountHistories = discountHistoriesNew;
                }
            }
        }
        return discountHistories;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 915725086)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDiscountDao() : null;
    }

    public Long getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public boolean getIsManual() {
        return this.isManual;
    }

    public void setIsManual(boolean isManual) {
        this.isManual = isManual;
    }

    public boolean getDelete() {
        return this.delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getUsedType() {
        return this.usedType;
    }

    public void setUsedType(int usedType) {
        this.usedType = usedType;
    }

    public Integer getAmountType() {
        return this.amountType;
    }

    public void setAmountType(int amountType) {
        this.amountType = amountType;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

}
