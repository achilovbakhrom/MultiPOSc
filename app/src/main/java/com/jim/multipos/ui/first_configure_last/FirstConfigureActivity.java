package com.jim.multipos.ui.first_configure_last;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.ui.first_configure_last.fragment.AccountFragment;
import com.jim.multipos.ui.first_configure_last.fragment.CurrencyFragment;
import com.jim.multipos.ui.first_configure_last.fragment.FirstConfigureListFragment;
import com.jim.multipos.ui.first_configure_last.fragment.POSDetailsFragment;
import com.jim.multipos.ui.first_configure_last.fragment.PaymentTypeFragment;
import com.jim.multipos.ui.lock_screen.LockScreenActivity;
import com.jim.multipos.utils.TestUtils;
import com.jim.multipos.utils.UIUtils;

import javax.inject.Inject;

import lombok.Getter;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;


/**
 * Created by bakhrom on 10/17/17.
 */

public class FirstConfigureActivity extends DoubleSideActivity implements FirstConfigureView{

    public static final String CONFIRM_BUTTON_KEY = "CONFIRM_BUTTON_KEY";

    @Inject
    @Getter
    protected FirstConfigurePresenter presenter;
    @Inject
    DatabaseManager databaseManager;

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onCreateView(savedInstanceState);
        addFragmentToLeft(new FirstConfigureListFragment());
        addFragmentWithTagToRight(new POSDetailsFragment(), POSDetailsFragment.class.getName());
        TestUtils.createUnits(databaseManager, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        presenter.onSaveInstanceState(outState);
    }

    public void openFragment(Fragment fragment, CompletionMode mode) {
        if (fragment != null) {
            String tag = fragment.getClass().getName();
            Fragment foundFragment = activityFragmentManager.findFragmentByTag(tag);
            for (Fragment fr : activityFragmentManager.getFragments()) {
                if (!(fr instanceof FirstConfigureListFragment)) {
                    activityFragmentManager.beginTransaction().hide(fr).commit();
                }
            }
            if (foundFragment == null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(CONFIRM_BUTTON_KEY, mode);
                fragment.setArguments(bundle);
                addFragmentWithTagToRight(fragment, tag);
            } else {
                activityFragmentManager.beginTransaction().show(foundFragment).commit();
                if (foundFragment instanceof ChangeableContent) {
                    ((ChangeableContent) foundFragment).setMode(mode);
                }
            }
        }
    }

    @Override
    public void openPOSDetailsFragment(CompletionMode mode) {
        openFragment(new POSDetailsFragment(), mode);
        select(FirstConfigurePresenter.POS_DETAILS_POSITION);
    }

    @Override
    public void openAccountFragment(CompletionMode mode) {
        openFragment(new AccountFragment(), mode);
        select(FirstConfigurePresenter.ACCOUNT_POSITION);
    }

    @Override
    public void openCurrencyFragment(CompletionMode mode) {
        openFragment(new CurrencyFragment(), mode);
        select(FirstConfigurePresenter.CURRENCY_POSITION);
    }

    @Override
    public void openPaymentTypeFragment(CompletionMode mode) {
        openFragment(new PaymentTypeFragment(), mode);
        select(FirstConfigurePresenter.PAYMENT_TYPE_POSITION);
    }

    @Override
    public void select(int position) {
        ((FirstConfigureListFragment) getCurrentFragmentLeft()).changePosition(position);
    }

    @Override
    public void changeState(int position, int state) {
        ((FirstConfigureListFragment) getCurrentFragmentLeft()).changeStateOfItem(position, state);
    }

    @Override
    public void makeToast(int resId) {
        UIUtils.makeToast(this, getResources().getString(resId), Toast.LENGTH_SHORT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void openLockScreen() {
        Intent intent = new Intent(this, LockScreenActivity.class);
        startActivity(intent);
    }
}
