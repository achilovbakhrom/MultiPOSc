package com.jim.multipos.ui.service_fee_new.model;

import com.jim.multipos.data.db.model.ServiceFee;

/**
 * Created by developer on 26.10.2017.
 */

public class ServiceFeeAdapterDetails {
    ServiceFee object;
    ServiceFee changedObject;

    public boolean setNewDiscription(String newDiscription) {
        if (!object.getName().equals(newDiscription) || changedObject != null) {
            getChangedObject().setName(newDiscription);
        }
        return changedObject != null;
    }

    public boolean setNewAmount(double newAmmount) {
        if (object.getAmount() != newAmmount || changedObject != null) {
            getChangedObject().setAmount(newAmmount);
        }
        return changedObject != null;
    }

    public boolean setNewApplyingType(int appType) {
        if (object.getApplyingType() != appType || changedObject != null) {
            getChangedObject().setApplyingType(appType);
        }
        return changedObject != null;
    }

    public boolean setNewType(int type) {
        if (object.getType() != (type) || changedObject != null) {
            getChangedObject().setType(type);
        }
        return changedObject != null;
    }

    public boolean setNewActive(boolean active) {
        if (object.getActive() != active || changedObject != null) {
            getChangedObject().setActive(active);
        }
        return changedObject != null;
    }

    public String getActualDescription() {
        return (changedObject != null) ? changedObject.getName() : object.getName();
    }

    public int getActualApplyingType() {
        return (changedObject != null) ? changedObject.getApplyingType() : object.getApplyingType();
    }

    public int getActualType() {
        return (changedObject != null) ? changedObject.getType() : object.getType();
    }

    public double getActualAmount() {
        return (changedObject != null) ? changedObject.getAmount() : object.getAmount();
    }

    public boolean getActualActiveStatus() {
        return (changedObject != null) ? changedObject.getActive() : object.getActive();
    }

    public void setObject(ServiceFee object) {
        this.object = object;
    }

    public ServiceFee getChangedObject() {
        if (changedObject == null && object != null)
            changedObject = object.copy();
        return changedObject;
    }

    public ServiceFee getObject() {
        return object;
    }

    public void setChangedObject(ServiceFee changedObject) {
        this.changedObject = changedObject;
    }

    public boolean isChanged() {
        return changedObject != null;
    }

}
