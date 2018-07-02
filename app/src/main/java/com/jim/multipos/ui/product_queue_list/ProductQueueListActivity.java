package com.jim.multipos.ui.product_queue_list;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.product_queue_list.product_queue.ProductQueueListFragment;

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
}
