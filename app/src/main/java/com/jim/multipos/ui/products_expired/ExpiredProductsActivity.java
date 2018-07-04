package com.jim.multipos.ui.products_expired;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.consignment.ConsignmentActivity;
import com.jim.multipos.ui.products_expired.expired_products.ExpiredProductsFragment;
import com.jim.multipos.utils.BundleConstants;

import static com.jim.multipos.ui.consignment.ConsignmentActivity.OPERATION_TYPE;
import static com.jim.multipos.ui.consignment.ConsignmentActivity.PRODUCT_ID;
import static com.jim.multipos.ui.consignment.ConsignmentActivity.VENDOR_ID;

public class ExpiredProductsActivity extends SimpleActivity implements ExpiredProductsView{
    @Override
    protected int getToolbar() {
        return WITH_TOOLBAR;
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(new ExpiredProductsFragment());
    }

    public void openReturnInvoice(Long productId, Long vendorId) {
        Intent intent = new Intent(this, ConsignmentActivity.class);
        intent.putExtra(VENDOR_ID, vendorId);
        intent.putExtra(PRODUCT_ID, productId);
        intent.putExtra(OPERATION_TYPE, BundleConstants.OUTVOICE);
        startActivity(intent);
    }
}
