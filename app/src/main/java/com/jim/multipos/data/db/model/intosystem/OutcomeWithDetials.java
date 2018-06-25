package com.jim.multipos.data.db.model.intosystem;

import com.jim.multipos.data.db.model.inventory.DetialCount;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;

import java.util.List;

public class OutcomeWithDetials {
    OutcomeProduct outcomeProduct;
    List<DetialCount> detialCountList;

    public OutcomeWithDetials(OutcomeProduct outcomeProduct, List<DetialCount> detialCountList) {
        this.outcomeProduct = outcomeProduct;
        this.detialCountList = detialCountList;
    }

    public OutcomeProduct getOutcomeProduct() {
        return outcomeProduct;
    }

    public void setOutcomeProduct(OutcomeProduct outcomeProduct) {
        this.outcomeProduct = outcomeProduct;
    }

    public List<DetialCount> getDetialCountList() {
        return detialCountList;
    }

    public void setDetialCountList(List<DetialCount> detialCountList) {
        this.detialCountList = detialCountList;
    }
}
