package com.jim.multipos.ui.cash_management;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.ui.cash_management.view.CashLogFragment;
import com.jim.multipos.ui.cash_management.view.CashOperationsFragment;

/**
 * Created by Sirojiddin on 11.01.2018.
 */

public class CashManagementActivity extends DoubleSideActivity implements CashManagementActivityView{

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
}
