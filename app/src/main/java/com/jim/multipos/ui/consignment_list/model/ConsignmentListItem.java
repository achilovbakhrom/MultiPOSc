package com.jim.multipos.ui.consignment_list.model;

import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;

import java.util.List;

/**
 * Created by Sirojiddin on 30.11.2017.
 */

public class ConsignmentListItem {
    private Consignment consignment;
    private List<ConsignmentProduct> consignmentProductList;

    public Consignment getConsignment() {
        return consignment;
    }

    public void setConsignment(Consignment consignment) {
        this.consignment = consignment;
    }

    public List<ConsignmentProduct> getConsignmentProductList() {
        return consignmentProductList;
    }

    public void setConsignmentProductList(List<ConsignmentProduct> consignmentProductList) {
        this.consignmentProductList = consignmentProductList;
    }
}
