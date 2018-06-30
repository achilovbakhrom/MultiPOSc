package com.jim.multipos.ui.vendor_products_view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorBelongProductsList;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorDetailsFragment;
import com.jim.multipos.utils.RxBus;

import java.text.DecimalFormat;

import javax.inject.Inject;

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
            VendorDetailsFragment vendorDetailsFragment = new VendorDetailsFragment();
            vendorDetailsFragment.setArguments(bundle);
            addFragmentToLeft(vendorDetailsFragment);

            VendorBelongProductsList vendorBelongProductsList = new VendorBelongProductsList();
            vendorBelongProductsList.setArguments(bundle);
            addFragmentToRight(vendorBelongProductsList);
        }

    }

//    @Override
//    public void sendDataToConsignment(int consignmentType, long vendorId) {
//        Intent intent = new Intent(this, ConsignmentActivity.class);
//        intent.putExtra(VENDOR_ID, vendorId);
//        intent.putExtra(CONSIGNMENT_TYPE, consignmentType);
//        startActivity(intent);
//    }
//
//    @Override
//    public void sendDataToConsignmentList(long vendorId) {
//        Intent intent = new Intent(this, ConsignmentListActivity.class);
//        intent.putExtra(VENDOR_ID, vendorId);
//        startActivity(intent);
//    }
//
//    @Override
//    public void openIncomeConsignmentToProduct(int consignmentType, long vendorId, Long productId) {
//        Intent intent = new Intent(this, ConsignmentActivity.class);
//        intent.putExtra(VENDOR_ID, vendorId);
//        intent.putExtra(PRODUCT_ID, productId);
//        intent.putExtra(CONSIGNMENT_TYPE, consignmentType);
//        startActivity(intent);
//    }
//
//    @Override
//    public void openVendorEditing(long vendorId) {
//        Intent intent = new Intent(this, VendorAddEditActivity.class);
//        intent.putExtra(VENDOR_ID, vendorId);
//        startActivity(intent);
//    }


}
