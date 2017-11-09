package com.jim.multipos.ui.service_fee_new;

import android.support.v7.widget.RecyclerView;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.ServiceFee;

import java.util.List;

/**
 * Created by Portable-Acer on 28.10.2017.
 */

public interface ServiceFeePresenter extends Presenter {
    List<ServiceFee> getServiceFees();
    void addServiceFee(ServiceFee serviceFee);
    void saveServiceFee(ServiceFee serviceFee);
    void updateServiceFee(ServiceFee serviceFee);
    void deleteServiceFee(ServiceFee serviceFee);
    void sortByAmount(List<ServiceFee> items);
    void sortByType(List<ServiceFee> items);
    void sortByReason(List<ServiceFee> items);
    void sortByAppType(List<ServiceFee> items);
    void sortByActive(List<ServiceFee> items);
    void sortByDefault(List<ServiceFee> items);
}
