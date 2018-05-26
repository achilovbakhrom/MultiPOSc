package com.jim.multipos.data.network.signing;

import com.google.gson.annotations.SerializedName;
import com.jim.multipos.data.db.model.Contact;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;


/**
 * Created by bakhrom on 10/5/17.
 */

@Parcel
public class RegistrationObject implements Serializable {
    @SerializedName("organization_name")
    String organizationName;
    @SerializedName("e_mail")
    String eMail;
    List<Contact> contactList;
    
    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }
}
