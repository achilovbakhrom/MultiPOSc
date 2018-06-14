package com.jim.multipos.data.db.model.history;


import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.ContactDao;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.products.VendorDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.List;

@Entity(nameInDb = "VENDOR_HISTORY", active = true)
public class VendorHistory implements  Serializable {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String contactName;
    private String address;
    private String photoPath;
    private Boolean isActive = true;
    private Boolean isDeleted = false;
    private Long globalId;
    private Long createdDate;
    private Long productId;
    private Long rootId;
    private long editedAt;
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "vendorId")})
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
    @Generated(hash = 490483008)
    public List<Contact> getContacts() {
        if (contacts == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ContactDao targetDao = daoSession.getContactDao();
            List<Contact> contactsNew = targetDao._queryVendorHistory_Contacts(id);
            synchronized (this) {
                if(contacts == null) {
                    contacts = contactsNew;
                }
            }
        }
        return contacts;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2083483797)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getVendorHistoryDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 902734643)
    private transient VendorHistoryDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public Long getEditedAt() {
        return this.editedAt;
    }
    public void setEditedAt(long editedAt) {
        this.editedAt = editedAt;
    }
    public Long getRootId() {
        return this.rootId;
    }
    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }
    public Long getProductId() {
        return this.productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Long getCreatedDate() {
        return this.createdDate;
    }
    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }
    public Long getGlobalId() {
        return this.globalId;
    }
    public void setGlobalId(Long globalId) {
        this.globalId = globalId;
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
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getContactName() {
        return this.contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
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
    @Generated(hash = 2082712573)
    public VendorHistory(Long id, String name, String contactName, String address,
            String photoPath, Boolean isActive, Boolean isDeleted, Long globalId,
            Long createdDate, Long productId, Long rootId, long editedAt) {
        this.id = id;
        this.name = name;
        this.contactName = contactName;
        this.address = address;
        this.photoPath = photoPath;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.globalId = globalId;
        this.createdDate = createdDate;
        this.productId = productId;
        this.rootId = rootId;
        this.editedAt = editedAt;
    }
    @Generated(hash = 1650969759)
    public VendorHistory() {
    }

 }