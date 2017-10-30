package com.jim.multipos.data.db.model;

import com.jim.multipos.data.db.model.intosystem.Editable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "DISCOUNT", active = true)
public class Discount implements Editable {
    @Id(autoincrement = true)
    private Long id;
    private String discription;
    private double amount;
    private String amountType;
    private String usedType;
    private boolean active;
    private boolean delete;
    private boolean notModifyted;
    private Long rootId;
    private Long createdDate;
    /** Used for active entity operations. */
    @Generated(hash = 1583145616)
    private transient DiscountDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;


    @Generated(hash = 923781430)
    public Discount(Long id, String discription, double amount, String amountType,
            String usedType, boolean active, boolean delete, boolean notModifyted,
            Long rootId, Long createdDate) {
        this.id = id;
        this.discription = discription;
        this.amount = amount;
        this.amountType = amountType;
        this.usedType = usedType;
        this.active = active;
        this.delete = delete;
        this.notModifyted = notModifyted;
        this.rootId = rootId;
        this.createdDate = createdDate;
    }

    @Generated(hash = 1777606421)
    public Discount() {
    }


    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getUsedType() {
        return usedType;
    }

    public void setUsedType(String usedType) {
        this.usedType = usedType;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isDeleted() {
        return delete;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.delete = deleted;
    }

    @Override
    public boolean isNotModifyted() {
        return notModifyted;
    }

    @Override
    public void setNotModifyted(boolean notModifyted) {
        this.notModifyted  =notModifyted;
    }

    @Override
    public Long getRootId() {
        return rootId;
    }

    @Override
    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    @Override
    public Long getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public Long getId() {
        return id;
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
    @Generated(hash = 915725086)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDiscountDao() : null;
    }

    public boolean getNotModifyted() {
        return this.notModifyted;
    }

    public boolean getDelete() {
        return this.delete;
    }

    public Boolean getActive() {
        return this.active;
    }
    public Discount copy(){
        Discount discount = new Discount();
        discount.setId(id);
        discount.setAmount(amount);
        discount.setDiscription(discription);
        discount.setAmountType(amountType);
        discount.setUsedType(usedType);
        discount.setActive(active);
        discount.setNotModifyted(notModifyted);
        discount.setRootId(rootId);
        discount.setCreatedDate(createdDate);
        return discount;
    }
}
