package com.jim.multipos.data.operations;

import com.jim.multipos.ui.vendor_item_managment.model.VendorManagmentItem;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by bakhrom on 10/23/17.
 */

public interface VendorItemManagmentOperations {
    Single<List<VendorManagmentItem>> getVendorItemManagmentItem();

}
