package com.jim.multipos.ui.start_configuration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.ui.lock_screen.LockScreenActivity;
import com.jim.multipos.ui.start_configuration.account.AccountFragment;
import com.jim.multipos.ui.start_configuration.basics.BasicsFragment;
import com.jim.multipos.ui.start_configuration.currency.CurrencyFragment;
import com.jim.multipos.ui.start_configuration.payment_type.PaymentTypeFragment;
import com.jim.multipos.ui.start_configuration.pos_data.PosDataFragment;
import com.jim.multipos.ui.start_configuration.selection_panel.SelectionPanelFragment;
import com.jim.multipos.utils.TestUtils;

import javax.inject.Inject;

public class StartConfigurationActivity extends DoubleSideActivity implements StartConfigurationView {

    private String[] configFragments = {PosDataFragment.class.getName(), CurrencyFragment.class.getName(), AccountFragment.class.getName(), PaymentTypeFragment.class.getName(), BasicsFragment.class.getName()};

    @Inject
    StartConfigurationPresenter presenter;
    @Inject
    DatabaseManager databaseManager;

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE_TWO_SECTION;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.initDefaultData();
    }

    @Override
    public void initViews() {
        TestUtils.createUnits(databaseManager, this);
        addFragmentWithTagToLeft(new SelectionPanelFragment(), SelectionPanelFragment.class.getName());
        initAllViews();
        hideAll();
        showBasicsFragment();
    }

    @Override
    public void restart() {
        finish();
        Intent intent = getIntent();
        startActivity(intent);
    }

    public void onPanelClicked(int position) {
        SelectionPanelFragment selectionPanelFragment = (SelectionPanelFragment) getSupportFragmentManager().findFragmentByTag(SelectionPanelFragment.class.getName());
        if (selectionPanelFragment != null) {
            selectionPanelFragment.changePosition(position);
        }
        switch (position) {
            case 0:
                showBasicsFragment();
                break;
            case 1:
                showPosDetailsFragment();
                break;
            case 2:
                showCurrencyFragment();
                break;
            case 3:
                showAccountFragment();
                break;
            case 4:
                showPaymentTypeFragment();
                break;
        }
    }

    public void showPosDetailsFragment() {
        hideAll();
        PosDataFragment detailsFragment = (PosDataFragment) getSupportFragmentManager().findFragmentByTag(PosDataFragment.class.getName());
        if (detailsFragment == null) {
            detailsFragment = new PosDataFragment();
            addFragmentWithTagToRight(detailsFragment, PosDataFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(detailsFragment).commit();
        }
    }

    public void showBasicsFragment() {
        hideAll();
        BasicsFragment basicsFragment = (BasicsFragment) getSupportFragmentManager().findFragmentByTag(BasicsFragment.class.getName());
        if (basicsFragment == null) {
            basicsFragment = new BasicsFragment();
            addFragmentWithTagToRight(basicsFragment, BasicsFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(basicsFragment).commit();
        }
    }

    public void showCurrencyFragment() {
        hideAll();
        CurrencyFragment currencyFragment = (CurrencyFragment) getSupportFragmentManager().findFragmentByTag(CurrencyFragment.class.getName());
        if (currencyFragment == null) {
            currencyFragment = new CurrencyFragment();
            addFragmentWithTagToRight(currencyFragment, CurrencyFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(currencyFragment).commit();
        }
    }

    public void showAccountFragment() {
        hideAll();
        AccountFragment accountFragment = (AccountFragment) getSupportFragmentManager().findFragmentByTag(AccountFragment.class.getName());
        if (accountFragment == null) {
            accountFragment = new AccountFragment();
            addFragmentWithTagToRight(accountFragment, AccountFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(accountFragment).commit();
        }
    }

    public void showPaymentTypeFragment() {
        hideAll();
        PaymentTypeFragment paymentTypeFragment = (PaymentTypeFragment) getSupportFragmentManager().findFragmentByTag(PaymentTypeFragment.class.getName());
        if (paymentTypeFragment == null) {
            paymentTypeFragment = new PaymentTypeFragment();
            addFragmentWithTagToRight(paymentTypeFragment, PaymentTypeFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(paymentTypeFragment).commit();
        }
    }


    public void hideAll() {
        for (String fragmentName : configFragments) {
            Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(fragmentName);
            if (fragmentByTag != null && fragmentByTag.isVisible()) {
                getSupportFragmentManager().beginTransaction().hide(fragmentByTag).commit();
            }
        }
    }

    public void initAllViews() {
        showBasicsFragment();
        showPosDetailsFragment();
        showCurrencyFragment();
        showAccountFragment();
        showPaymentTypeFragment();
    }

    public void openLockScreen() {
        Intent intent = new Intent(this, LockScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    public void refreshActivity() {
        presenter.clearData();
    }
}
