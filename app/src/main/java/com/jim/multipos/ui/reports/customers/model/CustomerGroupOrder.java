package com.jim.multipos.ui.reports.customers.model;

import lombok.Data;

@Data
public class CustomerGroupOrder {
    private Long id;
    private String name;
    private Long date;
    private Long orderId;
    private int orderStatus;
    private Double totalAmount;
}
