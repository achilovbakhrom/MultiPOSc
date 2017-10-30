package com.jim.multipos.ui.discount.model;

import com.jim.multipos.data.db.model.Discount;

/**
 * Created by developer on 26.10.2017.
 */

public class DiscountApaterDetials {
    Discount object;
    Discount changedObject;

    public Discount getObject() {
        return object;
    }

    public void setObject(Discount object) {
        this.object = object;
    }

    public Discount getChangedObject() {
        if(changedObject==null && object!=null)
            changedObject = object.copy();
        else if(changedObject==null && object==null){
            changedObject = new Discount();
            changedObject.setActive(true);
        }
        return changedObject;
    }

    public void setChangedObject(Discount changedObject) {
        this.changedObject = changedObject;
    }
    public boolean isChanged(){
        return changedObject!=null;
    }
}
