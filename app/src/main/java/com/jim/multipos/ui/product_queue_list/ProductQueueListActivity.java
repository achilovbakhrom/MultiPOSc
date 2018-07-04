package com.jim.multipos.ui.product_queue_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.consignment.ConsignmentActivity;
import com.jim.multipos.ui.product_queue_list.product_queue.ProductQueueListFragment;
import com.jim.multipos.utils.BundleConstants;

import static com.jim.multipos.ui.consignment.ConsignmentActivity.OPERATION_TYPE;
import static com.jim.multipos.ui.consignment.ConsignmentActivity.PRODUCT_ID;
import static com.jim.multipos.ui.consignment.ConsignmentActivity.VENDOR_ID;

public class ProductQueueListActivity extends SimpleActivity implements ProductQueueListView {

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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Long productId = bundle.getLong(PRODUCT_ID);
            Long vendorId = bundle.getLong(VENDOR_ID);
            ProductQueueListFragment fragment = new ProductQueueListFragment();
            Bundle bundle1 = new Bundle();
            bundle1.putLong(PRODUCT_ID, productId);
            bundle1.putLong(VENDOR_ID, vendorId);
            fragment.setArguments(bundle);
            addFragment(fragment);
        }
    }

    public void openReturnInvoice(Long productId, Long vendorId) {
        Intent intent = new Intent(this, ConsignmentActivity.class);
        intent.putExtra(VENDOR_ID, vendorId);
        intent.putExtra(PRODUCT_ID, productId);
        intent.putExtra(OPERATION_TYPE, BundleConstants.OUTVOICE);
        startActivity(intent);
    }
}
