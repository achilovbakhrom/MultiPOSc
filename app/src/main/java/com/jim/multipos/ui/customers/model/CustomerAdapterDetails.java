package com.jim.multipos.ui.customers.model;

import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

public class CustomerAdapterDetails {

    Customer object;
    Customer changedObject;

    public boolean setNewName(String name) {
        if (!object.getName().equals(name) || changedObject != null)
            getChangedObject().setName(name);
        return changedObject != null;
    }

    public boolean setNewAddress(String address) {
        if (!object.getAddress().equals(address) || changedObject != null)
            getChangedObject().setAddress(address);
        return changedObject != null;
    }

    public boolean setNewPhoneNumber(String number) {
        if (!object.getPhoneNumber().equals(number) || changedObject != null)
            getChangedObject().setPhoneNumber(number);
        return changedObject != null;
    }

    public boolean setNewQRCode(String code) {
        if (!object.getQrCode().equals(code) || changedObject != null)
            getChangedObject().setQrCode(code);
        return changedObject != null;
    }

    public boolean setNewGroup(List<CustomerGroup> groups) {
        if (!groups.isEmpty() || changedObject != null)
            getChangedObject().setCustomerGroups(groups);
        return changedObject != null;
    }

    public String getActualName(){
        return (changedObject != null) ? changedObject.getName() : object.getName();
    }

    public String getActualAddress(){
        return (changedObject != null) ? changedObject.getAddress() : object.getAddress();
    }

    public String getActualPhoneNumber(){
        return (changedObject != null) ? changedObject.getPhoneNumber() : object.getPhoneNumber();
    }

    public String getActualQRCode(){
        return (changedObject != null) ? changedObject.getQrCode() : object.getQrCode();
    }

    public List<CustomerGroup> getActualCustomerGroup(){
        return (changedObject != null) ? changedObject.getCustomerGroups() : object.getCustomerGroups();
    }

    public Customer getChangedObject() {
        if (changedObject == null && object != null)
            changedObject = object.clone();
        return changedObject;
    }

    public void setObject(Customer object) {
        this.object = object;
    }

    public Customer getObject() {
        return object;
    }

    public void setChangedObject(Customer changedObject) {
        this.changedObject = changedObject;
    }

    public boolean isChanged() {
        return changedObject != null;
    }
}
