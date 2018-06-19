package com.jim.multipos.ui.service_fee_new;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.ui.service_fee_new.model.ServiceFeeAdapterDetails;

import java.util.List;

/**
 * Created by Portable-Acer on 28.10.2017.
 */

public interface ServiceFeeView extends BaseView {
    void refreshList(List<ServiceFeeAdapterDetails> detailsList);
    void notifyItemAdd(int position);
    void sendEvent(int type, ServiceFee serviceFee);
    void sendChangeEvent(int type, ServiceFee updateServiceFee);
    void notifyItemRemove(int position);
    void refreshList();
    void closeActivity();
    void openWarning();
    void notifyItemChanged(int position);
}
