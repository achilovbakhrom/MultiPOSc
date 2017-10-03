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
    private String id;
    private String customerId;
    private String customerGroupId;

    @Generated(hash = 461838062)
    public JoinCustomerGroupsWithCustomers(String id, String customerId,
            String customerGroupId) {
        this.id = id;
        this.customerId = customerId;
        this.customerGroupId = customerGroupId;
    }

    public JoinCustomerGroupsWithCustomers() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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
}
