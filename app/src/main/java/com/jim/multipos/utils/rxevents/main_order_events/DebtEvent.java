package com.jim.multipos.utils.rxevents.main_order_events;

import com.jim.multipos.data.db.model.customer.Debt;


/**
 * Created by developer on 27.02.2018.
 */
public class DebtEvent {
    private Debt debt;
    private Debt newDebt;
    private int type;

    public DebtEvent(Debt debt, int type) {
        this.debt = debt;
        this.type = type;
    }

    public Debt getDebt() {
        return debt;
    }

    public void setDebt(Debt debt) {
        this.debt = debt;
    }

    public Debt getNewDebt() {
        return newDebt;
    }

    public void setNewDebt(Debt newDebt) {
        this.newDebt = newDebt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
