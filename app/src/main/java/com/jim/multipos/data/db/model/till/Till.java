package com.jim.multipos.data.db.model.till;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;

/**
 * Created by Sirojiddin on 02.02.2018.
 */
@Entity(nameInDb = "TILL", active = true)
public class Till {
    public static final int OPEN = 1000;
    public static final int CLOSED = 1001;

    @Id(autoincrement = true)
    private Long id;
    private Long closeDate;
    private Long openDate;
    private double debtSales;
    private int status;
    @ToMany(joinProperties = {
            @JoinProperty(
                    name = "id", referencedName = "tillId"
            )
    })
    private List<TillDetails> tillDetails;
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
@Generated(hash = 1826916925)
public synchronized void resetTillDetails() {
        tillDetails = null;
}
/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 545440960)
public List<TillDetails> getTillDetails() {
    if (tillDetails == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        TillDetailsDao targetDao = daoSession.getTillDetailsDao();
        List<TillDetails> tillDetailsNew = targetDao._queryTill_TillDetails(id);
        synchronized (this) {
            if(tillDetails == null) {
                tillDetails = tillDetailsNew;
            }
        }
    }
    return tillDetails;
}
/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 196448767)
public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTillDao() : null;
}
/** Used for active entity operations. */
@Generated(hash = 1933225571)
private transient TillDao myDao;
/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;
public int getStatus() {
        return this.status;
}
public void setStatus(int status) {
        this.status = status;
}
public double getDebtSales() {
        return this.debtSales;
}
public void setDebtSales(double debtSales) {
        this.debtSales = debtSales;
}
public Long getOpenDate() {
        return this.openDate;
}
public void setOpenDate(Long openDate) {
        this.openDate = openDate;
}
public Long getCloseDate() {
        return this.closeDate;
}
public void setCloseDate(Long closeDate) {
        this.closeDate = closeDate;
}
public Long getId() {
        return this.id;
}
public void setId(Long id) {
        this.id = id;
}
@Generated(hash = 469774718)
public Till(Long id, Long closeDate, Long openDate, double debtSales, int status) {
        this.id = id;
        this.closeDate = closeDate;
        this.openDate = openDate;
        this.debtSales = debtSales;
        this.status = status;
}
@Generated(hash = 694839459)
public Till() {
}
}
