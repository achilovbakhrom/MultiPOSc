package com.jim.multipos.ui.service_fee_new;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.ServiceFee;

import java.util.List;

/**
 * Created by Portable-Acer on 28.10.2017.
 */

public interface ServiceFeeView extends BaseView {
    void addServiceFee(ServiceFee serviceFee);
    void removeServiceFee(ServiceFee serviceFee);
    void updateRecyclerView(List<ServiceFee> items);
}
