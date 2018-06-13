package com.jim.multipos.ui.product_class_new.model;

import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.ui.product_class_new.adapters.ProductsClassListAdapter;

/**
 * Created by developer on 01.11.2017.
 */

public class ProductsClassAdapterDetials {
    ProductClass object;
    ProductsClassListAdapter.ProductClassItemTypes type;
    ProductClass changedObject;

    public boolean setNewName(String newName){
        if(!object.getName().equals(newName) || changedObject!=null){
            getChangedObject().setName(newName);
        }
        return isChanged();
    }
    public boolean setNewActive(boolean newActive){
        if(object.getActive()!=newActive || changedObject !=null){
            getChangedObject().setActive(newActive);
        }
        return isChanged();
    }

    public String getActualName(){
        return (isChanged())?changedObject.getName():object.getName();
    }
    public boolean getActualActive(){
        return (isChanged())?changedObject.getActive():object.getActive();
    }
    public void setType(ProductsClassListAdapter.ProductClassItemTypes type){
        this.type = type;
    }

    public ProductsClassListAdapter.ProductClassItemTypes getType(){
        return type;
    }
    public ProductClass getObject(){
        return object;
    }
    public ProductClass getChangedObject(){
        if(changedObject==null && object!=null)
            changedObject = object.copy();
        return changedObject;
    }
    public boolean isChanged(){
        return changedObject!=null;
    }
    public void setChangedObject(ProductClass changedObject){
        this.changedObject = changedObject;
    }
    public void setObject(ProductClass object){
        this.object = object;
    }
    public void contanierMode(){
        object = new ProductClass();
        object.setActive(true);
        object.setName("");
    }
}
