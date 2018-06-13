package com.jim.multipos.data.db.model.inventory;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;

@Entity(active = true, nameInDb = "WASTEOPERATION")
public class WasteOperation {
    @Id(autoincrement = true)
    private Long id;
    private Long wasteDate;
    private Long discription;

    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "wasteId")})
    private List<OutcomeProduct> outcomeProduct;

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
    @Generated(hash = 1635309850)
    public synchronized void resetOutcomeProduct() {
        outcomeProduct = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1220878319)
    public List<OutcomeProduct> getOutcomeProduct() {
        if (outcomeProduct == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OutcomeProductDao targetDao = daoSession.getOutcomeProductDao();
            List<OutcomeProduct> outcomeProductNew = targetDao._queryWasteOperation_OutcomeProduct(id);
            synchronized (this) {
                if(outcomeProduct == null) {
                    outcomeProduct = outcomeProductNew;
                }
            }
        }
        return outcomeProduct;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 470202280)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getWasteOperationDao() : null;
    }

    /** Used for active entity operations. */
    @Generated(hash = 683038573)
    private transient WasteOperationDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public Long getDiscription() {
        return this.discription;
    }

    public void setDiscription(Long discription) {
        this.discription = discription;
    }

    public Long getWasteDate() {
        return this.wasteDate;
    }

    public void setWasteDate(Long wasteDate) {
        this.wasteDate = wasteDate;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1721824987)
    public WasteOperation(Long id, Long wasteDate, Long discription) {
        this.id = id;
        this.wasteDate = wasteDate;
        this.discription = discription;
    }

    @Generated(hash = 866329219)
    public WasteOperation() {
    }

}
