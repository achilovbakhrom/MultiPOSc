package com.jim.multipos.utils.rxevents.main_order_events;

import com.jim.multipos.data.db.model.customer.Debt;

import lombok.Data;

/**
 * Created by developer on 27.02.2018.
 */
@Data
public class DebtEvent {
    private Debt debt;
    private Debt newDebt;
    private int type;

    public DebtEvent(Debt debt, int type) {
        this.debt = debt;
        this.type = type;
    }
}
