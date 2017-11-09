package com.jim.multipos.ui.service_fee_new;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ServiceFee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Portable-Acer on 28.10.2017.
 */

public class ServiceFeePresenterImpl extends BasePresenterImpl<ServiceFeeView> implements ServiceFeePresenter {
    private DatabaseManager databaseManager;

    @Inject
    public ServiceFeePresenterImpl(ServiceFeeView serviceFeeView, DatabaseManager databaseManager) {
        super(serviceFeeView);
        this.databaseManager = databaseManager;
    }

    @Override
    public List<ServiceFee> getServiceFees() {
        return new ArrayList<>(databaseManager.getServiceFeeOperations().getAllServiceFees().blockingSingle());


        /*for (ServiceFee sf : serviceFees) {
            ServiceFee serviceFee = new ServiceFee();
            serviceFee.setIsActive(sf.getIsActive());
            serviceFee.setApplyingType(sf.getApplyingType());
            serviceFee.setType(sf.getType());
            serviceFee.setReason(sf.getReason());
            serviceFee.setAmount(sf.getAmount());
            serviceFee.setCreatedDate(sf.getCreatedDate());
            serviceFee.setId(sf.getId());

            temp.add(serviceFee);
        }*/
    }

    @Override
    public void addServiceFee(ServiceFee serviceFee) {
        databaseManager.getServiceFeeOperations().addServiceFee(serviceFee).subscribe(aLong -> {
            view.addServiceFee(serviceFee);
        });
    }

    @Override
    public void saveServiceFee(ServiceFee serviceFee) {
        databaseManager.getServiceFeeOperations().removeServiceFee(serviceFee).subscribe();
    }

    @Override
    public void updateServiceFee(ServiceFee serviceFee) {
        databaseManager.getServiceFeeOperations().addServiceFee(serviceFee).subscribe();
    }

    @Override
    public void deleteServiceFee(ServiceFee serviceFee) {
        databaseManager.getServiceFeeOperations().removeServiceFee(serviceFee).subscribe(aBoolean -> {
            view.removeServiceFee(serviceFee);
        });
    }

    @Override
    public void sortByAmount(List<ServiceFee> items) {
        Collections.sort(items, (serviceFee, t1) -> t1.getAmount().compareTo(serviceFee.getAmount()));
        view.updateRecyclerView(items);
    }

    @Override
    public void sortByType(List<ServiceFee> items) {
        Collections.sort(items, (serviceFee, t1) -> t1.getType().compareTo(serviceFee.getType()));
        view.updateRecyclerView(items);
    }

    @Override
    public void sortByReason(List<ServiceFee> items) {
        Collections.sort(items, (serviceFee, t1) -> serviceFee.getReason().compareTo(t1.getReason()));
        view.updateRecyclerView(items);
    }

    @Override
    public void sortByAppType(List<ServiceFee> items) {
        Collections.sort(items, (serviceFee, t1) -> t1.getApplyingType().compareTo(serviceFee.getApplyingType()));
        view.updateRecyclerView(items);
    }

    @Override
    public void sortByActive(List<ServiceFee> items) {
        Collections.sort(items, (serviceFee, t1) -> t1.getIsActive().compareTo(serviceFee.getIsActive()));
        view.updateRecyclerView(items);
    }

    @Override
    public void sortByDefault(List<ServiceFee> items) {
        Collections.sort(items, (serviceFee, t1) -> t1.getCreatedDate().compareTo(serviceFee.getCreatedDate()));
        view.updateRecyclerView(items);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        databaseManager.getDaoSession().clear();
    }
}
