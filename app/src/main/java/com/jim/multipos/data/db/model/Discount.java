package com.jim.multipos.data.db.model;

import android.content.Context;

import com.jim.multipos.data.db.model.intosystem.Editable;
import com.jim.multipos.utils.CommonUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import lombok.Data;

@Entity(nameInDb = "DISCOUNT", active = true)
@Data
public class Discount implements Editable {
    public static final int PERCENT = 0;
    public static final int VALUE = 1;
    public static final int REPRICE = 2;
    public static final int ITEM = 0;
    public static final int ORDER = 1;
    public static final int ALL = 2;

    @Id(autoincrement = true)
    private Long id;
    private String name;
    private double amount;
    private int amountType;
    private int usedType;
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






    @Generated(hash = 1777606421)
    public Discount() {
    }




    @Generated(hash = 48568515)
    public Discount(Long id, String name, double amount, int amountType, int usedType,
            boolean active, boolean delete, boolean notModifyted, Long rootId,
            Long createdDate) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.amountType = amountType;
        this.usedType = usedType;
        this.active = active;
        this.delete = delete;
        this.notModifyted = notModifyted;
        this.rootId = rootId;
        this.createdDate = createdDate;
    }




    public Double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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
        discount.setName(name);
        discount.setAmountType(amountType);
        discount.setUsedType(usedType);
        discount.setActive(active);
        discount.setNotModifyted(notModifyted);
        discount.setRootId(rootId);
        discount.setCreatedDate(createdDate);
        return discount;
    }
    public String getDiscountTypeName(Context context){
        String discountTypeName = CommonUtils.getDiscountTypeName(context, amountType);
        if(amountType == Discount.PERCENT){
            DecimalFormat formatter;
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            formatter = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            formatter.setDecimalFormatSymbols(symbols);
            discountTypeName += " ";
            discountTypeName += formatter.format(amount);
            discountTypeName += "%";
        }
        return discountTypeName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

}
