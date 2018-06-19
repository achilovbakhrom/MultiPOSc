package com.jim.multipos.ui.service_fee_new;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.ServiceFeeLog;
import com.jim.multipos.ui.service_fee_new.model.ServiceFeeAdapterDetails;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Portable-Acer on 28.10.2017.
 */

public class ServiceFeePresenterImpl extends BasePresenterImpl<ServiceFeeView> implements ServiceFeePresenter {
    private DatabaseManager databaseManager;
    private List<ServiceFeeAdapterDetails> items;

    public enum ServiceFeeSortTypes {Amount, Type, Description, AppType, Active, Default}

    @Inject
    public ServiceFeePresenterImpl(ServiceFeeView serviceFeeView, DatabaseManager databaseManager) {
        super(serviceFeeView);
        this.databaseManager = databaseManager;
    }

    @Override
    public void initDataToServiceFee() {
        items = new ArrayList<>();
        databaseManager.getServiceFeeOperations().getAllServiceFees().subscribe(serviceFees -> {
            items.add(null);
            for (int i = 0; i < serviceFees.size(); i++) {
                if (!serviceFees.get(i).getIsManual()) {
                    ServiceFeeAdapterDetails details = new ServiceFeeAdapterDetails();
                    details.setObject(serviceFees.get(i));
                    items.add(details);
                }
            }
            view.refreshList(items);
        });
    }

    @Override
    public void addServiceFee(double amount, int type, String reason, int appType, boolean checked) {
        ServiceFee serviceFee = new ServiceFee();
        serviceFee.setAmount(amount);
        serviceFee.setType(type);
        serviceFee.setName(reason);
        serviceFee.setApplyingType(appType);
        serviceFee.setIsActive(checked);
        serviceFee.setCreatedDate(System.currentTimeMillis());
        serviceFee.setDeleted(false);
        databaseManager.addServiceFee(serviceFee).subscribe(serviceFee1 -> {
            ServiceFeeAdapterDetails serviceFeeAdapterDetails = new ServiceFeeAdapterDetails();
            serviceFeeAdapterDetails.setObject(serviceFee);
            ServiceFeeLog serviceFeeLog = new ServiceFeeLog();
            serviceFeeLog.setChangeDate(System.currentTimeMillis());
            serviceFeeLog.setServiceFee(serviceFee1);
            serviceFeeLog.setStatus(ServiceFeeLog.SERVICE_FEE_ADDED);
            databaseManager.insertServiceFeeLog(serviceFeeLog).subscribe();
            items.add(1, serviceFeeAdapterDetails);
            view.notifyItemAdd(1);
            view.sendEvent(GlobalEventConstants.ADD, serviceFee);
        });
    }

    @Override
    public void onSave(double amount, int type, String description, int appType, boolean active, ServiceFee serviceFee) {
        ServiceFeeLog serviceFeeLog = new ServiceFeeLog();
        serviceFeeLog.setChangeDate(System.currentTimeMillis());
        serviceFeeLog.setServiceFee(serviceFee);
        serviceFeeLog.setStatus(ServiceFeeLog.SERVICE_FEE_UPDATED);
        databaseManager.insertServiceFeeLog(serviceFeeLog).subscribe();
        serviceFee.keepToHistory();
        serviceFee.setAmount(amount);
        serviceFee.setType(type);
        serviceFee.setName(description);
        serviceFee.setApplyingType(appType);
        serviceFee.setIsActive(active);
        serviceFee.setDeleted(false);
        databaseManager.addServiceFee(serviceFee).subscribe(serviceFee2 -> {
            for (int i = 1; i < items.size(); i++) {
                if (items.get(i) != null) {
                    if (items.get(i).getObject().getId().equals(serviceFee.getId())) {
                        ServiceFeeAdapterDetails serviceFeeAdapterDetails = new ServiceFeeAdapterDetails();
                        serviceFeeAdapterDetails.setObject(serviceFee);
                        items.set(i, serviceFeeAdapterDetails);
                        view.notifyItemChanged(i);
                        return;
                    }
                }
            }
            view.sendChangeEvent(GlobalEventConstants.UPDATE, serviceFee);
        });
    }

    @Override
    public void deleteServiceFee(ServiceFee serviceFee) {
        serviceFee.setDeleted(true);
        databaseManager.addServiceFee(serviceFee).subscribe(serviceFee1 -> {
            for (int i = 1; i < items.size(); i++) {
                if (items.get(i).getObject().getId().equals(serviceFee.getId())) {
                    items.remove(i);
                    view.notifyItemRemove(i);
                    break;
                }
            }
            ServiceFeeLog serviceFeeLog = new ServiceFeeLog();
            serviceFeeLog.setChangeDate(System.currentTimeMillis());
            serviceFeeLog.setServiceFee(serviceFee1);
            serviceFeeLog.setStatus(ServiceFeeLog.SERVICE_FEE_DELETED);
            databaseManager.insertServiceFeeLog(serviceFeeLog).subscribe();
            view.sendEvent(GlobalEventConstants.DELETE, serviceFee);
        });
    }


    @Override
    public void sortList(ServiceFeeSortTypes serviceFeeSortTypes) {
        items.remove(0);
        switch (serviceFeeSortTypes) {
            case Amount:
                Collections.sort(items, (services, t1) -> t1.getObject().getAmount().compareTo(services.getObject().getAmount()));
                break;
            case Type:
                Collections.sort(items, (services, t1) -> t1.getObject().getType().compareTo(services.getObject().getType()));
                break;
            case AppType:
                Collections.sort(items, (services, t1) -> t1.getObject().getApplyingType().compareTo(services.getObject().getApplyingType()));
                break;
            case Active:
                Collections.sort(items, (services, t1) -> t1.getObject().getIsActive().compareTo(services.getObject().getIsActive()));
                break;
            case Description:
                Collections.sort(items, (services, t1) -> t1.getObject().getName().compareTo(services.getObject().getName()));
                break;
            case Default:
                Collections.sort(items, (services, t1) -> t1.getObject().getCreatedDate().compareTo(services.getObject().getCreatedDate()));
                break;
        }
        items.add(0, null);
        view.refreshList();
    }

    @Override
    public void onClose() {
        boolean weCanClose = true;
        for (int i = 1; i < items.size(); i++) {
            if (items.get(i).isChanged())
                weCanClose = false;
        }
        if (weCanClose) {
            view.closeActivity();
        } else {
            view.openWarning();
        }
    }

}
