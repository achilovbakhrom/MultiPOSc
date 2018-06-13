package com.jim.multipos.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.ui.lock_screen.LockScreenActivity;
import com.jim.multipos.ui.settings.accounts.AccountSettingsFragment;
import com.jim.multipos.ui.settings.choice_panel.ChoicePanelFragment;
import com.jim.multipos.ui.settings.common.CommonConfigFragment;
import com.jim.multipos.ui.settings.currency.CurrencySettingsFragment;
import com.jim.multipos.ui.settings.payment_type.PaymentTypeSettingsFragment;
import com.jim.multipos.ui.settings.pos_details.PosDetailsFragment;
import com.jim.multipos.ui.settings.print.PrintFragment;
import com.jim.multipos.ui.settings.security.SecurityFragment;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.main_order_events.MainPosActivityRefreshEvent;

import javax.inject.Inject;

public class SettingsActivity extends DoubleSideActivity implements SettingsView {

    @Inject
    SettingsPresenter settingsPresenter;
    @Inject
    RxBus rxBus;

    boolean isChanged = false;
    String[] settingsFragments = {SecurityFragment.class.getName(), PrintFragment.class.getName(), CommonConfigFragment.class.getName(), AccountSettingsFragment.class.getName(), CurrencySettingsFragment.class.getName(), PosDetailsFragment.class.getName(), PaymentTypeSettingsFragment.class.getName()};


    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE_TWO_SECTION;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragmentToLeft(new ChoicePanelFragment());
        showCommonConfigFragment();
    }

    public void onPanelClicked(int position) {
        switch (position) {
            case 0:
                //BASICS
                showCommonConfigFragment();
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
                showPaymentTypeSettingsFragment();
                break;
            case 5:
                showPrintFragment();
                break;
            case 6:
                showSecurityFragment();
                break;
        }
    }

    public void showSecurityFragment() {
        hideAll();
        SecurityFragment securityFragment = (SecurityFragment) getSupportFragmentManager().findFragmentByTag(SecurityFragment.class.getName());
        if (securityFragment == null) {
            securityFragment = new SecurityFragment();
            addFragmentWithTagToRight(securityFragment, SecurityFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(securityFragment).commit();
        }
    }

    public void showPrintFragment() {
        hideAll();
        PrintFragment printFragment = (PrintFragment) getSupportFragmentManager().findFragmentByTag(PrintFragment.class.getName());
        if (printFragment == null) {
            printFragment = new PrintFragment();
            addFragmentWithTagToRight(printFragment, PrintFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(printFragment).commit();
        }
    }

    public void showPosDetailsFragment() {
        hideAll();
        PosDetailsFragment detailsFragment = (PosDetailsFragment) getSupportFragmentManager().findFragmentByTag(PosDetailsFragment.class.getName());
        if (detailsFragment == null) {
            detailsFragment = new PosDetailsFragment();
            addFragmentWithTagToRight(detailsFragment, PosDetailsFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(detailsFragment).commit();
        }
    }

    public void showCurrencyFragment() {
        hideAll();
        CurrencySettingsFragment currencySettingsFragment = (CurrencySettingsFragment) getSupportFragmentManager().findFragmentByTag(CurrencySettingsFragment.class.getName());
        if (currencySettingsFragment == null) {
            currencySettingsFragment = new CurrencySettingsFragment();
            addFragmentWithTagToRight(currencySettingsFragment, CurrencySettingsFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(currencySettingsFragment).commit();
        }
    }

    public void showAccountFragment() {
        hideAll();
        AccountSettingsFragment accountSettingsFragment = (AccountSettingsFragment) getSupportFragmentManager().findFragmentByTag(AccountSettingsFragment.class.getName());
        if (accountSettingsFragment == null) {
            accountSettingsFragment = new AccountSettingsFragment();
            addFragmentWithTagToRight(accountSettingsFragment, AccountSettingsFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(accountSettingsFragment).commit();
        }
    }


    public void showPaymentTypeSettingsFragment() {
        hideAll();
        PaymentTypeSettingsFragment paymentTypeSettingsFragment = (PaymentTypeSettingsFragment) getSupportFragmentManager().findFragmentByTag(PaymentTypeSettingsFragment.class.getName());
        if (paymentTypeSettingsFragment == null) {
            paymentTypeSettingsFragment = new PaymentTypeSettingsFragment();
            addFragmentWithTagToRight(paymentTypeSettingsFragment, PaymentTypeSettingsFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(paymentTypeSettingsFragment).commit();
        }
    }

    public void showCommonConfigFragment() {
        hideAll();
        CommonConfigFragment commonConfigFragment = (CommonConfigFragment) getSupportFragmentManager().findFragmentByTag(CommonConfigFragment.class.getName());
        if (commonConfigFragment == null) {
            commonConfigFragment = new CommonConfigFragment();
            addFragmentWithTagToRight(commonConfigFragment, CommonConfigFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(commonConfigFragment).commit();
        }
    }

    public void hideAll() {
        for (String fragmentName : settingsFragments) {
            Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(fragmentName);
            if (fragmentByTag != null && fragmentByTag.isVisible()) {
                getSupportFragmentManager().beginTransaction().hide(fragmentByTag).commit();
            }
        }
    }

    public void openLockScreen() {
        Intent intent = new Intent(SettingsActivity.this, LockScreenActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isChanged)
            rxBus.send(new MainPosActivityRefreshEvent());
    }
}
