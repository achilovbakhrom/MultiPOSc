package com.jim.multipos.ui.discount.model;

import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ProductClass;

/**
 * Created by developer on 26.10.2017.
 */

public class DiscountApaterDetials {
    Discount object;
    Discount changedObject;

    public boolean setNewDiscription(String newDiscription){
        if(!object.getDiscription().equals(newDiscription)|| changedObject !=null){
            getChangedObject().setDiscription(newDiscription);
        }
        return changedObject !=null;
    }
    public boolean setNewAmmount(double newAmmount){
        if(object.getAmount() !=newAmmount || changedObject != null){
            getChangedObject().setAmount(newAmmount);
        }
        return changedObject !=null;
    }
    public boolean setNewAmmountType(String amountType){
        if(!object.getAmountType().equals(amountType) || changedObject !=null){
            getChangedObject().setAmountType(amountType);
        }
        return changedObject !=null;
    }
    public boolean setNewUsedType(String usedType){
        if(!object.getUsedType().equals(usedType) || changedObject !=null){
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
        return (changedObject != null) ? changedObject.getDiscription() : object.getDiscription();
    }

    public String getActualyAmmountType(){
        return (changedObject !=null) ? changedObject.getAmountType() : object.getAmountType();
    }
    public String getActualyUsedType(){
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
