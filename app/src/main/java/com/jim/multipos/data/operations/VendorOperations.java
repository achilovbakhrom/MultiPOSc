package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.Vendor;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by bakhrom on 10/23/17.
 */

public interface VendorOperations {
    Observable<Long> addVendor(Vendor vendor);
    Observable<Boolean> addVendors(List<Vendor> vendors);
    Observable<Boolean> isVendorNameExist(String name);
    Observable<Boolean> updateContacts(Long vendorId, List<Contact> contacts);
    Observable<Boolean> deleteVendor(Long vendorId);
    Observable<Vendor> getVendorById(Long vendorId);
    Observable<List<Vendor>> getVendors();
}
