package com.jim.multipos.ui.vendor.add_edit;

import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendor.AddingMode;

/**
 * Created by bakhrom on 10/21/17.
 */

public interface ContentChangeable {
    void setMode(AddingMode mode, Vendor vendor);
}
