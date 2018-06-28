package com.jim.multipos.data.db.model.inventory;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;

@Entity(active = true, nameInDb = "SURPLUSOPERATION")
public class SurplusOperation {
    @Id(autoincrement = true)
    private Long id;
    private Long surplusDate;
    private Long discription;

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
    @Generated(hash = 83630438)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSurplusOperationDao() : null;
    }

    /** Used for active entity operations. */
    @Generated(hash = 1011589419)
    private transient SurplusOperationDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public Long getDiscription() {
        return this.discription;
    }

    public void setDiscription(Long discription) {
        this.discription = discription;
    }

    public Long getSurplusDate() {
        return this.surplusDate;
    }

    public void setSurplusDate(Long surplusDate) {
        this.surplusDate = surplusDate;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 570389318)
    public SurplusOperation(Long id, Long surplusDate, Long discription) {
        this.id = id;
        this.surplusDate = surplusDate;
        this.discription = discription;
    }

    @Generated(hash = 549281480)
    public SurplusOperation() {
    }
}
