package com.jim.multipos.data.db.model.intosystem;

import com.jim.multipos.data.db.model.inventory.OutcomeProduct;

public class StockResult {
    public static final int STOCK_OUT = -1;
    public static final int STOCK_OK = 1;

    OutcomeProduct outcomeProduct;
    int result;

    public OutcomeProduct getOutcomeProduct() {
        return outcomeProduct;
    }

    public void setOutcomeProduct(OutcomeProduct outcomeProduct) {
        this.outcomeProduct = outcomeProduct;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
