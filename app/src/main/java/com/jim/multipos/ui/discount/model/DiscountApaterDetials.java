package com.jim.multipos.ui.discount.model;

import com.jim.multipos.data.db.model.Discount;

/**
 * Created by developer on 26.10.2017.
 */

public class DiscountApaterDetials {
    Discount object;
    Discount changedObject;

    public boolean setNewDiscription(String newDiscription){
        if(!object.getName().equals(newDiscription)|| changedObject !=null){
            getChangedObject().setName(newDiscription);
        }
        return changedObject !=null;
    }
    public boolean setNewAmmount(double newAmmount){
        if(object.getAmount() !=newAmmount || changedObject != null){
            getChangedObject().setAmount(newAmmount);
        }
        return changedObject !=null;
    }
    public boolean setNewAmmountType(int amountType){
        if(object.getAmountType() != amountType || changedObject !=null){
            getChangedObject().setAmountType(amountType);
        }
        return changedObject !=null;
    }
    public boolean setNewUsedType(int usedType){
        if(object.getUsedType() !=(usedType) || changedObject !=null){
            getChangedObject().setUsedType(usedType);
        }
        return changedObject !=null;
    }
    public boolean setNewActive(boolean active){
        if(object.getActive() != active || changedObject !=null){
            getChangedObject().setActive(active);
        }
        return changedObject !=null;
    }

    public String getActualDiscription(){
        return (changedObject != null) ? changedObject.getName() : object.getName();
    }

    public int getActualyAmmountType(){
        return (changedObject !=null) ? changedObject.getAmountType() : object.getAmountType();
    }
    public int getActualyUsedType(){
        return (changedObject !=null) ? changedObject.getUsedType() : object.getUsedType();
    }
    public double getActualyAmmount(){
        return (changedObject!=null) ? changedObject.getAmount() : object.getAmount();
    }

    public boolean getActualyActive(){
        return (changedObject!=null) ? changedObject.getActive() : object.getActive();
    }
    public void setObject(Discount object) {
        this.object = object;
    }

    public Discount getChangedObject() {
        if(changedObject==null && object!=null)
            changedObject = object.copy();
        return changedObject;
    }

    public Discount getObject() {
        return object;
    }

    public void setChangedObject(Discount changedObject) {
        this.changedObject = changedObject;
    }

    public boolean isChanged(){
        return changedObject!=null;
    }

}
