package com.jim.multipos.data.db.model;


import android.content.Context;
import android.support.annotation.Keep;

import com.jim.multipos.data.db.model.history.DiscountHistory;
import com.jim.multipos.data.db.model.history.ServiceFeeHistory;
import com.jim.multipos.utils.CommonUtils;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.jim.multipos.data.db.model.history.ServiceFeeHistoryDao;


@Entity(nameInDb = "SERVICE_FEE", active = true)
public class ServiceFee  {

    public static final int PERCENT = 0;
    public static final int VALUE = 1;
    public static final int ITEM = 0;
    public static final int ORDER = 1;
    public static final int ALL = 2;

    @Id(autoincrement = true)
    private Long id;
    private Double amount;
    private int type;
    private String name;
    private int applyingType;
    private boolean isActive = true;
    private boolean isDeleted = false;
    private boolean isManual = false;
    private Long createdDate;

    @ToMany(joinProperties = {
            @JoinProperty(
                    name = "id", referencedName = "rootId"
            )
    })
    private List<ServiceFeeHistory> serviceFeeHistories;

    @Keep
    public void keepToHistory(){
        if(daoSession!=null){
            ServiceFeeHistory serviceFeeHistory = new ServiceFeeHistory();
            serviceFeeHistory.setAmount(amount);
            serviceFeeHistory.setType(type);
            serviceFeeHistory.setName(name);
            serviceFeeHistory.setApplyingType(applyingType);
            serviceFeeHistory.setIsActive(isActive);
            serviceFeeHistory.setIsDeleted(isDeleted);
            serviceFeeHistory.setIsManual(isManual);
            serviceFeeHistory.setCreatedDate(createdDate);
            serviceFeeHistory.setRootId(id);
            serviceFeeHistory.setEditedAt(System.currentTimeMillis());
            daoSession.getServiceFeeHistoryDao().insertOrReplace(serviceFeeHistory);
        }
    }
    @Keep
    public ServiceFeeHistory getServiceFeeHistoryForDate(Long date){
        if(daoSession!=null){
            List<ServiceFeeHistory> serviceFeeHistories = getServiceFeeHistories();
            Collections.sort(serviceFeeHistories, new Comparator<ServiceFeeHistory>() {
                @Override
                public int compare(ServiceFeeHistory serviceFeeHistory, ServiceFeeHistory t1) {
                    return serviceFeeHistory.getEditedAt().compareTo(t1.getEditedAt());
                }
            });
            for(ServiceFeeHistory serviceFeeHistory:serviceFeeHistories){
                if(date > serviceFeeHistory.getEditedAt()){
                    return serviceFeeHistory;
                }
            }
            return null;
        }else {
            new Exception("Gettting History for not saved object exeption").printStackTrace();
            return null;
        }
    }

    /** Used for active entity operations. */
    @Generated(hash = 1592290701)
    private transient ServiceFeeDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    @Generated(hash = 1970278224)
    public ServiceFee() {
    }

    @Generated(hash = 832403407)
    public ServiceFee(Long id, Double amount, int type, String name, int applyingType,
            boolean isActive, boolean isDeleted, boolean isManual, Long createdDate) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.name = name;
        this.applyingType = applyingType;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.isManual = isManual;
        this.createdDate = createdDate;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }
    
    @Override
    protected ServiceFee clone() throws CloneNotSupportedException {
        return (ServiceFee) super.clone();
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
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
    @Generated(hash = 2038307626)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getServiceFeeDao() : null;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getApplyingType() {
        return this.applyingType;
    }

    public void setApplyingType(int applyingType) {
        this.applyingType = applyingType;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public String getServiceFeeTypeName(Context context){
        String serviceFeeTypeName = CommonUtils.getServiceTypeName(context, type);
        if(type == ServiceFee.PERCENT){
            DecimalFormat formatter;
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMaximumFractionDigits(2);
            formatter = (DecimalFormat) numberFormat;
            DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            formatter.setDecimalFormatSymbols(symbols);
            serviceFeeTypeName += " ";
            serviceFeeTypeName += formatter.format(amount);
            serviceFeeTypeName += "%";
        }
        return serviceFeeTypeName;
    }

    public boolean getIsManual() {
        return this.isManual;
    }

    public void setIsManual(boolean isManual) {
        this.isManual = isManual;
    }

    public ServiceFee copy(){
        ServiceFee serviceFee = new ServiceFee();
        serviceFee.setId(id);
        serviceFee.setAmount(amount);
        serviceFee.setName(name);
        serviceFee.setType(type);
        serviceFee.setApplyingType(applyingType);
        serviceFee.setActive(isActive);
        serviceFee.setCreatedDate(createdDate);
        return serviceFee;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1492452982)
    public synchronized void resetServiceFeeHistories() {
        serviceFeeHistories = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 566565551)
    public List<ServiceFeeHistory> getServiceFeeHistories() {
        if (serviceFeeHistories == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ServiceFeeHistoryDao targetDao = daoSession.getServiceFeeHistoryDao();
            List<ServiceFeeHistory> serviceFeeHistoriesNew = targetDao._queryServiceFee_ServiceFeeHistories(id);
            synchronized (this) {
                if(serviceFeeHistories == null) {
                    serviceFeeHistories = serviceFeeHistoriesNew;
                }
            }
        }
        return serviceFeeHistories;
    }
}
