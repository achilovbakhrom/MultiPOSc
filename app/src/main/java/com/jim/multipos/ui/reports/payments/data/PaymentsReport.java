package com.jim.multipos.ui.reports.payments.data;
import lombok.Data;

@Data
public class PaymentsReport {
    long filterId;
    String paymentName;
    String accountName;
    String reason;
    String description = "";
    long orderId;
    long tillId;
    long date;
    double amount;
    public Long getDate(){
        return date;
    }
}
