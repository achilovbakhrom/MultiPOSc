package com.jim.multipos.ui.vendor_products_view;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendor_products_view.model.ProductState;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.data.db.model.consignment.Consignment.INCOME_CONSIGNMENT;
import static com.jim.multipos.data.db.model.consignment.Consignment.RETURN_CONSIGNMENT;
import static com.jim.multipos.data.db.model.inventory.BillingOperations.PAID_TO_CONSIGNMENT;

/**
 * Created by Portable-Acer on 17.11.2017.
 */

public class VendorProductsViewPresenterImpl extends BasePresenterImpl<VendorProductsView> implements VendorProductsViewPresenter {

    @Inject
    public VendorProductsViewPresenterImpl(VendorProductsView vendorProductsView, DatabaseManager databaseManager) {
        super(vendorProductsView);
    }

}
