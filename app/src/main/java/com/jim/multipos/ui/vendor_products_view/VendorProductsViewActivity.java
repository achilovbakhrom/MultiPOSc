package com.jim.multipos.ui.vendor_products_view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.ui.consignment.ConsignmentActivity;
import com.jim.multipos.ui.consignment_list.ConsignmentListActivity;
import com.jim.multipos.ui.vendor.add_edit.VendorAddEditActivity;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorDetailsFragment;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorDetailsList;

import java.text.DecimalFormat;

import javax.inject.Inject;

import lombok.Getter;

import static com.jim.multipos.ui.consignment.ConsignmentActivity.PRODUCT_ID;
import static com.jim.multipos.ui.inventory.InventoryActivity.CONSIGNMENT_TYPE;
import static com.jim.multipos.ui.inventory.InventoryActivity.VENDOR_ID;

public class VendorProductsViewActivity extends DoubleSideActivity implements VendorProductsView {
    @Inject
    @Getter
    VendorProductsViewPresenter presenter;
    @Inject
    @Getter
    DecimalFormat decimalFormat;

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
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
}
