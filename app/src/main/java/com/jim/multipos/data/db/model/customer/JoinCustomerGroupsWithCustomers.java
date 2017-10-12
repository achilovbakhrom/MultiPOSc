package com.jim.multipos.data.db.model.customer;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.UUID;

/**
 * Created by user on 05.09.17.
 */

@Entity(nameInDb = "JOIN_CUSTOMER_GROUPS_WITH_CUSTOMERS")
public class JoinCustomerGroupsWithCustomers {
    @Id(autoincrement = true)
    private Long id;
    private Long customerId;
    private Long customerGroupId;
    public Long getCustomerGroupId() {
        return this.customerGroupId;
    }
    public void setCustomerGroupId(Long customerGroupId) {
        this.customerGroupId = customerGroupId;
    }
    public Long getCustomerId() {
        return this.customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 601694094)
    public JoinCustomerGroupsWithCustomers(Long id, Long customerId,
            Long customerGroupId) {
        this.id = id;
        this.customerId = customerId;
        this.customerGroupId = customerGroupId;
    }
    @Generated(hash = 1309216389)
    public JoinCustomerGroupsWithCustomers() {
    }

}
