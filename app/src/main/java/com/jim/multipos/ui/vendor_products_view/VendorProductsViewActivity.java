package com.jim.multipos.ui.vendor_products_view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.billing_vendor.BillingOperationsActivity;
import com.jim.multipos.ui.consignment.ConsignmentActivity;
import com.jim.multipos.ui.consignment_list.ConsignmentListActivity;
import com.jim.multipos.ui.vendor.add_edit.VendorAddEditActivity;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorDetailsFragment;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorDetailsList;
import com.jim.multipos.ui.vendor_products_view.model.ProductState;
import com.jim.multipos.utils.PaymentToVendorDialog;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.inventory_events.InventoryStateEvent;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;


import static com.jim.multipos.ui.consignment.ConsignmentActivity.PRODUCT_ID;
import static com.jim.multipos.ui.inventory.InventoryActivity.CONSIGNMENT_TYPE;
import static com.jim.multipos.ui.inventory.InventoryActivity.VENDOR_ID;

public class VendorProductsViewActivity extends DoubleSideActivity implements VendorProductsView {
    @Inject
    VendorProductsViewPresenter presenter;
    @Inject
    DecimalFormat decimalFormat;
    @Inject
    RxBus rxBus;

    public VendorProductsViewPresenter getPresenter() {
        return presenter;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public RxBus getRxBus() {
        return rxBus;
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Long vendorId = bundle.getLong(VENDOR_ID);
            presenter.setVendorId(vendorId);
        }
        addFragmentToLeft(new VendorDetailsFragment());
        addFragmentToRight(new VendorDetailsList());
    }

    @Override
    public void sendDataToConsignment(int consignmentType, long vendorId) {
        Intent intent = new Intent(this, ConsignmentActivity.class);
        intent.putExtra(VENDOR_ID, vendorId);
        intent.putExtra(CONSIGNMENT_TYPE, consignmentType);
        startActivity(intent);
    }

    @Override
    public void sendDataToConsignmentList(long vendorId) {
        Intent intent = new Intent(this, ConsignmentListActivity.class);
        intent.putExtra(VENDOR_ID, vendorId);
        startActivity(intent);
    }

    @Override
    public void openIncomeConsignmentToProduct(int consignmentType, long vendorId, Long productId) {
        Intent intent = new Intent(this, ConsignmentActivity.class);
        intent.putExtra(VENDOR_ID, vendorId);
        intent.putExtra(PRODUCT_ID, productId);
        intent.putExtra(CONSIGNMENT_TYPE, consignmentType);
        startActivity(intent);
    }

    @Override
    public void openVendorEditing(long vendorId) {
        Intent intent = new Intent(this, VendorAddEditActivity.class);
        intent.putExtra(VENDOR_ID, vendorId);
        startActivity(intent);
    }

    @Override
    public void updateAdapterItems(List<ProductState> inventoryStates) {
        VendorDetailsList fragment = (VendorDetailsList) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.updateAdapterItems(inventoryStates);
        }
    }

    @Override
    public void initVendorDetails(String name, String photoPath, String address, String contactName, List<Contact> contacts, Double debt, double paid, String abbr) {
        VendorDetailsFragment fragment = (VendorDetailsFragment) getCurrentFragmentLeft();
        if (fragment != null) {
            fragment.initVendor(name, photoPath, address, contactName, contacts, debt, paid, abbr);
        }
    }

    @Override
    public void openPaymentsList(long vendorId, Double debt) {
        Intent intent = new Intent(this, BillingOperationsActivity.class);
        intent.putExtra(BillingOperationsActivity.VENDOR_EXTRA_ID, vendorId);
        intent.putExtra(BillingOperationsActivity.VENDOR_DEBT, debt);
        startActivity(intent);
    }

    @Override
    public void openPayDialog(Vendor vendor, DatabaseManager databaseManager) {
        PaymentToVendorDialog paymentToVendorDialog = new PaymentToVendorDialog(this, vendor, new PaymentToVendorDialog.PaymentToVendorCallback() {
            @Override
            public void onChanged() {
                presenter.updateBillings();
            }
            @Override
            public void onCancel() {

            }
        }, databaseManager, null);
        paymentToVendorDialog.show();
    }

    @Override
    public void updateVendorBillings(Double debt, double paid, String abbr) {
        VendorDetailsFragment fragment = (VendorDetailsFragment) getCurrentFragmentLeft();
        if (fragment != null) {
            fragment.updateBillings(debt, paid, abbr);
        }
    }

    @Override
    public void sendEvent(int event) {
        rxBus.send(new InventoryStateEvent(event));
    }
}
