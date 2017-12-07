package com.jim.multipos.ui.billing_vendor;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.mpviews.utils.Test;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment;
import com.jim.multipos.utils.DateIntervalPicker;
import com.jim.multipos.utils.TestUtils;

import java.util.Calendar;

import javax.inject.Inject;

/**
 * Created by developer on 29.11.2017.
 */

public class BillingOperationsActivity extends SimpleActivity {
    public static final String VENDOR_EXTRA_ID = "vendor_id";
    public static final String VENDOR_DEBT = "vendor_debt";

    @Inject
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TestUtils.createAccount(databaseManager);
        TestUtils.createCurrencies(databaseManager,this);
        TestUtils.createVendord(databaseManager);
        BillingOperationFragment billingOperationFragment = new BillingOperationFragment();
        billingOperationFragment.setVendorId(getIntent().getExtras().getLong(VENDOR_EXTRA_ID));
        billingOperationFragment.setTotalDebt(getIntent().getExtras().getDouble(VENDOR_DEBT));
        addFragment(billingOperationFragment);
        Calendar from = (Calendar) Calendar.getInstance().clone();
        Calendar to = (Calendar) Calendar.getInstance().clone();
        from.set(Calendar.MONTH,from.get(Calendar.MONTH)-1);
        toolbar.setDataIntervalPicker(from,to,() -> {
            DateIntervalPicker dateIntervalPicker = new DateIntervalPicker(this);
            dateIntervalPicker.show();
        });
    }


    @Override
    protected int getToolbar() {
        return WITH_TOOLBAR;
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.WITH_CALENDAR_TYPE;
    }
}
