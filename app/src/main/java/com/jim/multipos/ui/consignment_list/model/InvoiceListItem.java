package com.jim.multipos.ui.consignment_list.model;

import com.jim.multipos.data.db.model.consignment.Invoice;
import com.jim.multipos.data.db.model.consignment.Outvoice;

public class InvoiceListItem {

    private Invoice invoice;
    private Outvoice outvoice;
    private String  number;
    private double totalAmount;
    private int type;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    private long createdDate;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Outvoice getOutvoice() {
        return outvoice;
    }

    public void setOutvoice(Outvoice outvoice) {
        this.outvoice = outvoice;
    }

}
