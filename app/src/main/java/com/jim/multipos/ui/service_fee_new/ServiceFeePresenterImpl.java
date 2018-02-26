package com.jim.multipos.ui.service_fee_new;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.ui.service_fee_new.model.ServiceFeeAdapterDetails;

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
    public static final String SERVICE_FEE_ADD = "service fee is added";
    public static final String SERVICE_FEE_UPDATE = "service fee is updated";
    public static final String SERVICE_FEE_DELETE = "service fee is deleted";

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
                ServiceFeeAdapterDetails details = new ServiceFeeAdapterDetails();
                details.setObject(serviceFees.get(i));
                items.add(details);
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
        serviceFee.setNotModifyted(true);
        serviceFee.setDeleted(false);
        databaseManager.addServiceFee(serviceFee).subscribe(aLong -> {
            ServiceFeeAdapterDetails serviceFeeAdapterDetails = new ServiceFeeAdapterDetails();
            serviceFeeAdapterDetails.setObject(serviceFee);
            items.add(1, serviceFeeAdapterDetails);
            view.notifyItemAdd(1);
            view.sendEvent(SERVICE_FEE_ADD, serviceFee);
        });
    }

    @Override
    public void onSave(double amount, int type, String description, int appType, boolean active, ServiceFee serviceFee) {
        serviceFee.setNotModifyted(false);
        databaseManager.addServiceFee(serviceFee).subscribe(aLong -> {
            ServiceFee newServiceFee = new ServiceFee();
            newServiceFee.setAmount(amount);
            newServiceFee.setType(type);
            newServiceFee.setName(description);
            newServiceFee.setApplyingType(appType);
            newServiceFee.setIsActive(active);
            newServiceFee.setCreatedDate(System.currentTimeMillis());
            newServiceFee.setNotModifyted(true);
            newServiceFee.setDeleted(false);
            if (serviceFee.getRootId() != null)
                newServiceFee.setRootId(serviceFee.getId());
            else newServiceFee.setRootId(serviceFee.getRootId());
            databaseManager.addServiceFee(serviceFee).subscribe(aLong1 -> {
                for (int i = 1; i < items.size(); i++) {
                    if (items.get(i).getObject().getId().equals(serviceFee.getId())) {
                        ServiceFeeAdapterDetails serviceFeeAdapterDetails = new ServiceFeeAdapterDetails();
                        serviceFeeAdapterDetails.setObject(newServiceFee);
                        items.set(i, serviceFeeAdapterDetails);
                        view.notifyItemChanged(i);
                        return;
                    }
                }
                view.sendChangeEvent(SERVICE_FEE_UPDATE, serviceFee.getId(), newServiceFee.getId());
            });
        });
    }

    @Override
    public void deleteServiceFee(ServiceFee serviceFee) {
        serviceFee.setDeleted(true);
        databaseManager.addServiceFee(serviceFee).subscribe(aLong -> {
            for (int i = 1; i < items.size(); i++) {
                if (items.get(i).getObject().getId().equals(serviceFee.getId())) {
                    items.remove(i);
                    view.notifyItemRemove(i);
                    break;
                }
            }
            view.sendEvent(SERVICE_FEE_DELETE, serviceFee);
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
