package com.jim.multipos.ui.cash_management;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.ui.cash_management.view.CashLogFragment;
import com.jim.multipos.ui.cash_management.view.CashOperationsFragment;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 11.01.2018.
 */

public class CashManagementActivity extends DoubleSideActivity implements CashManagementActivityView {

    @Inject
    DatabaseManager databaseManager;

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragmentToRight(new CashOperationsFragment());
        addFragmentToLeft(new CashLogFragment());
        if (!databaseManager.hasOpenTill().blockingGet() && !databaseManager.isNoTills().blockingGet()) {
            Till till = new Till();
            till.setOpenDate(System.currentTimeMillis());
            till.setStatus(Till.OPEN);
            databaseManager.insertTill(till).subscribe();
        }
    }
}
