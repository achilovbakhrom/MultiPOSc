package com.jim.multipos.ui.consignment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.consignment.view.IncomeConsignmentFragment;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class ConsignmentActivity extends SimpleActivity implements ConsignmentActivityView {

    protected static final int WITH_TOOLBAR = 1;

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
        addFragment(new IncomeConsignmentFragment());
    }
}
