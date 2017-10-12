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
    @Id
    private Long id;
    private String customerId;
    private String customerGroupId;

    @Generated(hash = 1817902033)
    public JoinCustomerGroupsWithCustomers(Long id, String customerId,
            String customerGroupId) {
        this.id = id;
        this.customerId = customerId;
        this.customerGroupId = customerGroupId;
    }

    @Generated(hash = 1309216389)
    public JoinCustomerGroupsWithCustomers() {
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerGroupId() {
        return this.customerGroupId;
    }

    public void setCustomerGroupId(String customerGroupId) {
        this.customerGroupId = customerGroupId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
