package com.jim.multipos.ui.consignment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.consignment.view.IncomeConsignmentFragment;
import com.jim.multipos.ui.consignment.view.ReturnConsignmentFragment;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class ConsignmentActivity extends SimpleActivity implements ConsignmentActivityView {

    protected static final int WITH_TOOLBAR = 1;
    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String VENDOR_ID = "VENDOR_ID";
    public static final String CONSIGNMENT_TYPE = "CONSIGNMENT_TYPE";
    private Long productId, vendorId;
    private int type;

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
            productId = bundle.getLong(PRODUCT_ID);
            vendorId = bundle.getLong(VENDOR_ID);
            type = bundle.getInt(CONSIGNMENT_TYPE);
            if (type == 0) {
                IncomeConsignmentFragment fragment = new IncomeConsignmentFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putLong(PRODUCT_ID, productId);
                bundle1.putLong(VENDOR_ID, vendorId);
                fragment.setArguments(bundle);
                addFragment(fragment);
            } else if (type == 1){
                ReturnConsignmentFragment fragment = new ReturnConsignmentFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putLong(PRODUCT_ID, productId);
                bundle1.putLong(VENDOR_ID, vendorId);
                fragment.setArguments(bundle);
                addFragment(fragment);
            }
        }

    }
}
