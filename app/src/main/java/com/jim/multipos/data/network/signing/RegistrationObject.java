package com.jim.multipos.data.network.signing;

import com.google.gson.annotations.SerializedName;
import com.jim.multipos.data.db.model.Contact;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by bakhrom on 10/5/17.
 */

@Parcel
public class RegistrationObject implements Serializable {
    @Getter @Setter
    @SerializedName("organization_name")
    private String organizationName;
    @Getter @Setter
    @SerializedName("e_mail")
    private String eMail;
    private List<Contact> contactList;
}
