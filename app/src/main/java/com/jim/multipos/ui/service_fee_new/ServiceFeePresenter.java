package com.jim.multipos.ui.service_fee_new;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.ServiceFee;

/**
 * Created by Portable-Acer on 28.10.2017.
 */

public interface ServiceFeePresenter extends Presenter {
    void initDataToServiceFee();
    void deleteServiceFee(ServiceFee serviceFee);
    void addServiceFee(double amount, int type, String reason, int appType, boolean checked);
    void onSave(double amount, int type, String description, int appType, boolean active, ServiceFee serviceFee);
    void sortList(ServiceFeePresenterImpl.ServiceFeeSortTypes serviceFeeSortTypes);
    void onClose();
}
