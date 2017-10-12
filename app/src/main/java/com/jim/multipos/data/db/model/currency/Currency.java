package com.jim.multipos.data.db.model.currency;

import org.greenrobot.greendao.annotation.Entity;
import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Id;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.intosystem.Activatable;

/**
 * Created by DEV on 17.08.2017.
 */
@Entity(nameInDb = "CURRENCY", active = true)
public class Currency {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String abbr;
    private boolean isMain;
    private boolean active;
    /** Used for active entity operations. */
    @Generated(hash = 1033120508)
    private transient CurrencyDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    @Generated(hash = 1923982902)
    public Currency(Long id, String name, String abbr, boolean isMain,
            boolean active) {
        this.id = id;
        this.name = name;
        this.abbr = abbr;
        this.isMain = isMain;
        this.active = active;
    }

    @Generated(hash = 1387171739)
    public Currency() {
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public Boolean getIsMain() {
        return this.isMain;
    }

    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
    }

    public boolean getActive() {
        return this.active;
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
    @Generated(hash = 869658167)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCurrencyDao() : null;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
