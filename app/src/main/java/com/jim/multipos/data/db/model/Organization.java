package com.jim.multipos.data.db.model;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;


/**
 * Created by DEV on 26.08.2017.
 */
@Entity(active = true, nameInDb = "ORGANIZATION")
public class Organization implements Serializable {
    @Id(autoincrement = true)
    @SerializedName("id")
    private Long id;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("zip_code")
    private String zipCode;
    @SerializedName("e_mail")
    private String email;
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "organizationId")})
    @SerializedName("contacts")
    private List<Contact> contacts;
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
    @Generated(hash = 1818154294)
    public synchronized void resetContacts() {
        contacts = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2067743840)
    public List<Contact> getContacts() {
        if (contacts == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ContactDao targetDao = daoSession.getContactDao();
            List<Contact> contactsNew = targetDao._queryOrganization_Contacts(id);
            synchronized (this) {
                if(contacts == null) {
                    contacts = contactsNew;
                }
            }
        }
        return contacts;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1328120336)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOrganizationDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 49964946)
    private transient OrganizationDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getZipCode() {
        return this.zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
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
    @Generated(hash = 1769081081)
    public Organization(Long id, String name, String address, String zipCode, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.zipCode = zipCode;
        this.email = email;
    }
    @Generated(hash = 27039612)
    public Organization() {
    }
}
