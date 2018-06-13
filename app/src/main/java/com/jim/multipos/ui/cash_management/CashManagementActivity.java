package com.jim.multipos.ui.cash_management;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.ui.cash_management.view.CashLogFragment;
import com.jim.multipos.ui.cash_management.view.CashOperationsFragment;
import com.jim.multipos.ui.cash_management.view.CloseTillDialogFragment;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 11.01.2018.
 */

public class CashManagementActivity extends DoubleSideActivity implements CashManagementActivityView {

    @Inject
    DatabaseManager databaseManager;

    public static final String TILL_ID = "till_id";

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragmentToRight(new CashOperationsFragment());
        addFragmentToLeft(new CashLogFragment());
    }

    public void openCloseTillDialog(Long tillId) {
        CloseTillDialogFragment closeTillDialogFragment = (CloseTillDialogFragment) getSupportFragmentManager().findFragmentByTag(CloseTillDialogFragment.class.getName());
        if (closeTillDialogFragment == null) {
            closeTillDialogFragment = new CloseTillDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putLong(TILL_ID, tillId);
            closeTillDialogFragment.setArguments(bundle);
            addFragmentWithTagToFullWindow(closeTillDialogFragment, CloseTillDialogFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(closeTillDialogFragment).commit();
        }
    }
}
