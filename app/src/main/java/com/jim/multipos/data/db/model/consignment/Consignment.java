package com.jim.multipos.data.db.model.consignment;

import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.intosystem.Editable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.jim.multipos.data.db.model.DaoSession;
import com.jim.multipos.data.db.model.currency.CurrencyDao;
import com.jim.multipos.data.db.model.AccountDao;

/**
 * Created by Sirojiddin on 09.11.2017.
 */
@Entity(active = true, nameInDb = "CONSIGNMENT")
public class Consignment implements Editable {
    @Id(autoincrement = true)
    private Long id;
    private String consignmentNumber;
    private String description;
    private Double totalAmount;
    private Boolean isActive = true;
    private Boolean isDeleted = false;
    private Boolean isNotModified = true;
    private Boolean isFromAccount;
    private Long createdDate;
    private Long rootId;
    private Long accountId;
    @ToOne(joinProperty = "accountId")
    private Account account;
    private Long currencyId;
    @ToOne(joinProperty = "currencyId")
    private Currency currency;
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "consignmentId")})
    private List<ConsignmentProduct> consignmentProducts;
    @Generated(hash = 1489923924)
    private transient Long currency__resolvedKey;
    /** Used for active entity operations. */
    @Generated(hash = 265222024)
    private transient ConsignmentDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    @Generated(hash = 1501133588)
    private transient Long account__resolvedKey;

    @Generated(hash = 514956270)
    public Consignment(Long id, String consignmentNumber, String description, Double totalAmount,
            Boolean isActive, Boolean isDeleted, Boolean isNotModified, Boolean isFromAccount, Long createdDate,
            Long rootId, Long accountId, Long currencyId) {
        this.id = id;
        this.consignmentNumber = consignmentNumber;
        this.description = description;
        this.totalAmount = totalAmount;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.isNotModified = isNotModified;
        this.isFromAccount = isFromAccount;
        this.createdDate = createdDate;
        this.rootId = rootId;
        this.accountId = accountId;
        this.currencyId = currencyId;
    }

    @Generated(hash = 1791287112)
    public Consignment() {
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public void setActive(boolean active) {
        this.isActive = active;
    }

    @Override
    public boolean isDeleted() {
        return this.isDeleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    @Override
    public boolean isNotModifyted() {
        return this.isNotModified;
    }

    @Override
    public void setNotModifyted(boolean notModifyted) {
        this.isNotModified = notModifyted;
    }

    @Override
    public Long getRootId() {
        return this.rootId;
    }

    @Override
    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    @Override
    public Long getCreatedDate() {
        return this.createdDate;
    }

    @Override
    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public Long getId() {
        return this.id;
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
    @Generated(hash = 307869910)
    public synchronized void resetConsignmentProducts() {
        consignmentProducts = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 578339266)
    public List<ConsignmentProduct> getConsignmentProducts() {
        if (consignmentProducts == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ConsignmentProductDao targetDao = daoSession.getConsignmentProductDao();
            List<ConsignmentProduct> consignmentProductsNew = targetDao._queryConsignment_ConsignmentProducts(id);
            synchronized (this) {
                if(consignmentProducts == null) {
                    consignmentProducts = consignmentProductsNew;
                }
            }
        }
        return consignmentProducts;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1889019422)
    public void setCurrency(Currency currency) {
        synchronized (this) {
            this.currency = currency;
            currencyId = currency == null ? null : currency.getId();
            currency__resolvedKey = currencyId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 434384135)
    public Currency getCurrency() {
        Long __key = this.currencyId;
        if (currency__resolvedKey == null || !currency__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CurrencyDao targetDao = daoSession.getCurrencyDao();
            Currency currencyNew = targetDao.load(__key);
            synchronized (this) {
                currency = currencyNew;
                currency__resolvedKey = __key;
            }
        }
        return currency;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1181206235)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getConsignmentDao() : null;
    }

    public Long getCurrencyId() {
        return this.currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getIsFromAccount() {
        return this.isFromAccount;
    }

    public void setIsFromAccount(Boolean isFromAccount) {
        this.isFromAccount = isFromAccount;
    }

    public Boolean getIsNotModified() {
        return this.isNotModified;
    }

    public void setIsNotModified(Boolean isNotModified) {
        this.isNotModified = isNotModified;
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

    public Double getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConsignmentNumber() {
        return this.consignmentNumber;
    }

    public void setConsignmentNumber(String consignmentNumber) {
        this.consignmentNumber = consignmentNumber;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1910176546)
    public void setAccount(Account account) {
        synchronized (this) {
            this.account = account;
            accountId = account == null ? null : account.getId();
            account__resolvedKey = accountId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 531730087)
    public Account getAccount() {
        Long __key = this.accountId;
        if (account__resolvedKey == null || !account__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AccountDao targetDao = daoSession.getAccountDao();
            Account accountNew = targetDao.load(__key);
            synchronized (this) {
                account = accountNew;
                account__resolvedKey = __key;
            }
        }
        return account;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
