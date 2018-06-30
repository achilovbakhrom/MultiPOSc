package com.jim.multipos.data.db.model.products;


import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.ContactDao;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.history.VendorHistory;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.jim.multipos.data.db.model.history.VendorHistoryDao;

@Entity(nameInDb = "VENDOR", active = true)
public class Vendor implements  Serializable {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String contactName;
    private String address;
    private String photoPath;
    private Boolean active = true;
    private Boolean deleted = false;
    private Long globalId;
    private Long createdDate;
    @ToMany(joinProperties = {
            @JoinProperty(
                    name = "id", referencedName = "rootId"
            )
    })
    private List<VendorHistory> vendorHistories;


    @Keep
    public void keepToHistory(){
        if(daoSession!=null){
            VendorHistory vendorHistory = new VendorHistory();
            vendorHistory.setName(name);
            vendorHistory.setContactName(contactName);
            vendorHistory.setAddress(address);
            vendorHistory.setPhotoPath(photoPath);
            vendorHistory.setIsActive(active);
            vendorHistory.setIsDeleted(deleted);
            vendorHistory.setGlobalId(globalId);
            vendorHistory.setCreatedDate(createdDate);
            vendorHistory.setRootId(id);
            vendorHistory.setEditedAt(System.currentTimeMillis());
            daoSession.getVendorHistoryDao().insertOrReplace(vendorHistory);
        }
    }
    @Keep
    public VendorHistory getVendorHistoryForDate(Long date){
        if(daoSession!=null){
            List<VendorHistory> vendorHistories = getVendorHistories();
            Collections.sort(vendorHistories, new Comparator<VendorHistory>() {
                @Override
                public int compare(VendorHistory vendorHistory, VendorHistory t1) {
                    return vendorHistory.getEditedAt().compareTo(t1.getEditedAt());
                }
            });
            for(VendorHistory vendorHistory:vendorHistories){
                if(date > vendorHistory.getEditedAt()){
                    return vendorHistory;
                }
            }
            return null;
        }else {
            new Exception("Gettting History for not saved object exeption").printStackTrace();
            return null;
        }
    }


    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "vendorId")})
    private List<Contact> contacts;
    /** Used for active entity operations. */
    @Generated(hash = 957720129)
    private transient VendorDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;


    @Generated(hash = 245491538)
    public Vendor(Long id, String name, String contactName, String address, String photoPath, Boolean active,
            Boolean deleted, Long globalId, Long createdDate) {
        this.id = id;
        this.name = name;
        this.contactName = contactName;
        this.address = address;
        this.photoPath = photoPath;
        this.active = active;
        this.deleted = deleted;
        this.globalId = globalId;
        this.createdDate = createdDate;
    }
    @Generated(hash = 530746692)
    public Vendor() {
    }


    @Keep
    public void setId(Long id) {
        this.id = id;
    }

    @Keep
    public Long getId() {
        return id;
    }


    @Keep
    public Long getCreatedDate() {
        return this.createdDate;
    }

    @Keep
    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
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
    @Generated(hash = 1818154294)
    public synchronized void resetContacts() {
        contacts = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 934241905)
    public List<Contact> getContacts() {
        if (contacts == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ContactDao targetDao = daoSession.getContactDao();
            List<Contact> contactsNew = targetDao._queryVendor_Contacts(id);
            synchronized (this) {
                if(contacts == null) {
                    contacts = contactsNew;
                }
            }
        }
        return contacts;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1079169342)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getVendorDao() : null;
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

    public String getPhotoPath() {
        return this.photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1656033905)
    public synchronized void resetVendorHistories() {
        vendorHistories = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 568284908)
    public List<VendorHistory> getVendorHistories() {
        if (vendorHistories == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VendorHistoryDao targetDao = daoSession.getVendorHistoryDao();
            List<VendorHistory> vendorHistoriesNew = targetDao._queryVendor_VendorHistories(id);
            synchronized (this) {
                if(vendorHistories == null) {
                    vendorHistories = vendorHistoriesNew;
                }
            }
        }
        return vendorHistories;
    }
    public Boolean getDeleted() {
        return this.deleted;
    }
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    public Boolean getActive() {
        return this.active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }

 }