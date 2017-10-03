package com.jim.multipos.data.db.model.employee;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;

@Entity(nameInDb = "EMPLOYEE", active = true)
public class Employee {
    @Id
    private String id;
    @Property
    private String name;
    @Property
    private String phone;
    @Property
    private String startDate;
    @Property
    private String photoID;
    @Property
    private String photoPath;
    private String positionId;
    @ToOne(joinProperty = "positionId")
    private PositionEmployee position;
    @Property
    private long password;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 694547204)
    private transient EmployeeDao myDao;
    @Generated(hash = 1473771710)
    private transient String position__resolvedKey;

    public Employee() {
        id = UUID.randomUUID().toString();
    }

    public Employee(String name, String phone, String startDate, String photoID, PositionEmployee position, long password, String photoPath) {
        this.name = name;
        this.phone = phone;
        this.startDate = startDate;
        this.photoID = photoID;
        this.position = position;
        this.password = password;
        this.photoPath = photoPath;
    }

    @Generated(hash = 812416363)
    public Employee(String id, String name, String phone, String startDate, String photoID, String photoPath, String positionId,
            long password) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.startDate = startDate;
        this.photoID = photoID;
        this.photoPath = photoPath;
        this.positionId = positionId;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public long getPassword() {
        return password;
    }

    public void setPassword(long password) {
        this.password = password;
    }

    public String getPositionId() {
        return this.positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1262660520)
    public PositionEmployee getPosition() {
        String __key = this.positionId;
        if (position__resolvedKey == null || position__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PositionEmployeeDao targetDao = daoSession.getPositionEmployeeDao();
            PositionEmployee positionNew = targetDao.load(__key);
            synchronized (this) {
                position = positionNew;
                position__resolvedKey = __key;
            }
        }
        return position;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 408472671)
    public void setPosition(PositionEmployee position) {
        synchronized (this) {
            this.position = position;
            positionId = position == null ? null : position.getId();
            position__resolvedKey = positionId;
        }
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
    @Generated(hash = 671679171)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getEmployeeDao() : null;
    }
}
