package com.jim.multipos.utils.rxevents.main_order_events;

import com.jim.multipos.data.db.model.customer.Debt;

import lombok.Data;

/**
 * Created by developer on 27.02.2018.
 */
@Data
public class DebtEvent {
    private Debt debt;
    private int type;
}
