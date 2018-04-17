package com.jim.multipos.ui.reports.payments.data;

import lombok.Data;

@Data
public class SummaryPayments {
    long id;
    String name;
    double gotToOrder;
    double percentage;
    public void plusGoToOrder(double v){
        gotToOrder += v;
    }

}
