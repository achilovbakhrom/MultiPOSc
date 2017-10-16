package com.jim.multipos.ui.first_configure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.first_configure.fragments.AccountFragment;
import com.jim.multipos.ui.first_configure.fragments.CurrencyFragment;
import com.jim.multipos.ui.first_configure.fragments.LeftSideFragment;
import com.jim.multipos.ui.first_configure.fragments.PaymentTypeFragment;
import com.jim.multipos.ui.first_configure.fragments.PosDetailsFragment;
import com.jim.multipos.ui.first_configure.fragments.UnitsFragment;
import com.jim.multipos.utils.RxBusLocal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import lombok.Getter;

import static com.jim.multipos.ui.first_configure.Constants.ACCOUNT_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.CURRENCY_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.PAYMENT_TYPE_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.POS_DETAIL_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.UNITS_FRAGMENT_ID;

/**
 * Created by user on 07.10.17.
 */

public class FirstConfigureActivity extends DoubleSideActivity implements FirstConfigureView {
    @Inject
    @Getter
    protected FirstConfigurePresenter presenter;
    @Inject
    RxBusLocal rxBusLocal;
    private ArrayList<Disposable> subscriptions = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFragmentToRight(new PosDetailsFragment());
        addFragmentToLeft(new LeftSideFragment());
    }

    @Override
    protected void onDestroy() {
        RxBusLocal.removeListners(subscriptions);
        super.onDestroy();
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    @Override
    public void replaceFragment(int position) {
        Log.d("myLogs", "Fragment position is " + position);

        switch (position) {
            case POS_DETAIL_FRAGMENT_ID:
                if (getFragmentByTag(PosDetailsFragment.class.getName()) != null) {
                    Log.d("myLogs", PosDetailsFragment.class.getName() + " is not null");
                    showFragment(getFragmentByTag(PosDetailsFragment.class.getName()));
                } else {
                    Log.d("myLogs", PosDetailsFragment.class.getName() + " is null");
                    addFragmentWithTagToRight(new PosDetailsFragment(), PosDetailsFragment.class.getName());
                }
            case ACCOUNT_FRAGMENT_ID:
                if (getFragmentByTag(AccountFragment.class.getName()) != null) {
                    Log.d("myLogs", AccountFragment.class.getName() + " is not null");
                    showFragment(getFragmentByTag(AccountFragment.class.getName()));
                } else {
                    Log.d("myLogs", AccountFragment.class.getName() + " is null");
                    addFragmentWithTagToRight(new AccountFragment(), AccountFragment.class.getName());
                }
            case CURRENCY_FRAGMENT_ID:
                if (getFragmentByTag(CurrencyFragment.class.getName()) != null) {
                    Log.d("myLogs", CurrencyFragment.class.getName() + " is not null");
                    showFragment(getFragmentByTag(CurrencyFragment.class.getName()));
                } else {
                    Log.d("myLogs", CurrencyFragment.class.getName() + " is null");
                    addFragmentWithTagToRight(new CurrencyFragment(), CurrencyFragment.class.getName());
                }
                break;
            case PAYMENT_TYPE_FRAGMENT_ID:
                if (getFragmentByTag(PaymentTypeFragment.class.getName()) != null) {
                    Log.d("myLogs", PaymentTypeFragment.class.getName() + " is not null");
                    showFragment(getFragmentByTag(PaymentTypeFragment.class.getName()));
                } else {
                    Log.d("myLogs", PaymentTypeFragment.class.getName() + " is null");
                    addFragmentWithTagToRight(new PaymentTypeFragment(), PaymentTypeFragment.class.getName());
                }
                break;
            case UNITS_FRAGMENT_ID:
                if (getFragmentByTag(UnitsFragment.class.getName()) != null) {
                    Log.d("myLogs", UnitsFragment.class.getName() + " is not null");
                    showFragment(getFragmentByTag(UnitsFragment.class.getName()));
                } else {
                    Log.d("myLogs", UnitsFragment.class.getName() + " is null");
                    addFragmentWithTagToRight(new UnitsFragment(), UnitsFragment.class.getName());
                }
                break;
        }
    }

    @Override
    public void openPrevFragment() {
        if (getFragmentCount() == 2) {
            closeActivity();
        } else {
            super.onBackPressed();

            updateLeftSideFragment(getCurrentFragmentPosition());
        }
    }

    @Override
    public void updateLeftSideFragment(int position) {
        ((LeftSideFragment) getCurrentFragmentLeft()).updateAdapter(position);
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public void addAccountItem(Account account) {
        if (getCurrentFragmentRight() instanceof AccountFragment) {
            ((AccountFragment) getCurrentFragmentRight()).updateAccountList(account);
        }
    }

    @Override
    public void addPaymentTypeItem(PaymentType paymentType) {
        if (getCurrentFragmentRight() instanceof PaymentTypeFragment) {
            ((PaymentTypeFragment) getCurrentFragmentRight()).addPaymentTypeItem(paymentType);
        }
    }

    @Override
    public void removeAccountItem(Account account) {
        if (getCurrentFragmentRight() instanceof AccountFragment) {
            ((AccountFragment) getCurrentFragmentRight()).removeAccountItem(account);
        }
    }

    @Override
    public void removePaymentTypeItem(PaymentType paymentType) {
        if (getCurrentFragmentRight() instanceof PaymentTypeFragment) {
            ((PaymentTypeFragment) getCurrentFragmentRight()).removePaymentTypeItem(paymentType);
        }
    }

    @Override
    public void setCurrencySpinnerData(List<Currency> currencies, int position) {
        if (getCurrentFragmentRight() instanceof CurrencyFragment) {
            ((CurrencyFragment) getCurrentFragmentRight()).setCurrencySpinnerData(currencies, position);
        }
    }

    @Override
    public void showPaymentTypeCurrencyToast() {
        if (getCurrentFragmentRight() instanceof PaymentTypeFragment) {
            ((PaymentTypeFragment) getCurrentFragmentRight()).showCurrencyToast();
        }
    }

    @Override
    public void showPaymentTypeAccountToast() {
        if (getCurrentFragmentRight() instanceof PaymentTypeFragment) {
            ((PaymentTypeFragment) getCurrentFragmentRight()).showAccountToast();
        }
    }

    @Override
    public void showPaymentTypeToast() {
        if (getCurrentFragmentRight() instanceof PaymentTypeFragment) {
            ((PaymentTypeFragment) getCurrentFragmentRight()).showPaymentTypeToast();
        }
    }

    @Override
    public void showAccountToast() {
        if (getCurrentFragmentRight() instanceof AccountFragment) {
            ((AccountFragment) getCurrentFragmentRight()).showAccountToast();
        }
    }

    @Override
    public void setPaymentTypeCurrency(Currency currency) {
        if (getCurrentFragmentRight() instanceof PaymentTypeFragment) {
            ((PaymentTypeFragment) getCurrentFragmentRight()).setCurrency(currency);
        }
    }

    @Override
    public void setPaymentTypeAccount(List<Account> accounts) {
        if (getCurrentFragmentRight() instanceof PaymentTypeFragment) {
            ((PaymentTypeFragment) getCurrentFragmentRight()).setAccount(accounts);
        }
    }

    @Override
    public void addWeightUnit(Unit unit) {
        if (getCurrentFragmentRight() instanceof UnitsFragment) {
            ((UnitsFragment) getCurrentFragmentRight()).addWeightUnit(unit);
        }
    }

    @Override
    public void removeWeightUnit(Unit unit) {
        if (getCurrentFragmentRight() instanceof UnitsFragment) {
            ((UnitsFragment) getCurrentFragmentRight()).removeWeightUnit(unit);
        }
    }

    @Override
    public void addLengthUnit(Unit unit) {
        if (getCurrentFragmentRight() instanceof UnitsFragment) {
            ((UnitsFragment) getCurrentFragmentRight()).addLengthUnit(unit);
        }
    }

    @Override
    public void removeLengthUnit(Unit unit) {
        if (getCurrentFragmentRight() instanceof UnitsFragment) {
            ((UnitsFragment) getCurrentFragmentRight()).removeLengthUnit(unit);
        }
    }

    @Override
    public void addAreaUnit(Unit unit) {
        if (getCurrentFragmentRight() instanceof UnitsFragment) {
            ((UnitsFragment) getCurrentFragmentRight()).addAreaUnit(unit);
        }
    }

    @Override
    public void removeAreaUnit(Unit unit) {
        if (getCurrentFragmentRight() instanceof UnitsFragment) {
            ((UnitsFragment) getCurrentFragmentRight()).removeAreaUnit(unit);
        }
    }

    @Override
    public void addVolumeUnit(Unit unit) {
        if (getCurrentFragmentRight() instanceof UnitsFragment) {
            ((UnitsFragment) getCurrentFragmentRight()).addVolumeUnit(unit);
        }
    }

    @Override
    public void removeVolumeUnit(Unit unit) {
        if (getCurrentFragmentRight() instanceof UnitsFragment) {
            ((UnitsFragment) getCurrentFragmentRight()).removeVolumeUnit(unit);
        }
    }

    private int getCurrentFragmentPosition() {
        int position = -1;

        if (getCurrentFragmentRight() instanceof PosDetailsFragment) {
            position = POS_DETAIL_FRAGMENT_ID;
        } else if (getCurrentFragmentRight() instanceof AccountFragment) {
            position = ACCOUNT_FRAGMENT_ID;
        } else if (getCurrentFragmentRight() instanceof CurrencyFragment) {
            position = CURRENCY_FRAGMENT_ID;
        } else if (getCurrentFragmentRight() instanceof PaymentTypeFragment) {
            position = PAYMENT_TYPE_FRAGMENT_ID;
        } else if (getCurrentFragmentRight() instanceof UnitsFragment) {
            position = UNITS_FRAGMENT_ID;
        }

        return position;
    }

    private void showFragment(Fragment fragment) {
        Log.d("myLogs", fragment.getClass().getName() + " is show");
        Log.d("myLogs", getCurrentFragmentRight().getClass().getName() + " is hide");
        activityFragmentManager.beginTransaction().hide(getCurrentFragmentRight()).show(fragment).commit();
    }

    /*@Override
    public boolean hasFragment(int position) {
        boolean hasFragment = false;

        for (Fragment fragment : activityFragmentManager.getFragments()) {
            switch (position) {
                case POS_DETAIL_FRAGMENT_ID:
                    if (fragment instanceof PosDetailsFragment)
                        hasFragment = true;
                    break;
                case ACCOUNT_FRAGMENT_ID:
                    if (fragment instanceof AccountFragment)
                        hasFragment = true;
                    break;
                case CURRENCY_FRAGMENT_ID:
                    if (fragment instanceof CurrencyFragment)
                        hasFragment = true;
                    break;
                case PAYMENT_TYPE_FRAGMENT_ID:
                    if (fragment instanceof PaymentTypeFragment)
                        hasFragment = true;
                    break;
                case UNITS_FRAGMENT_ID:
                    if (fragment instanceof UnitsFragment)
                        hasFragment = true;
                    break;
            }
        }

        return hasFragment;
    }



    @Override
    public Fragment getFragment(int position) {
        Fragment result = null;

        for (Fragment fragment : activityFragmentManager.getFragments()) {
            switch (position) {
                case POS_DETAIL_FRAGMENT_ID:
                    if (fragment instanceof PosDetailsFragment)
                        result = fragment;
                    break;
                case ACCOUNT_FRAGMENT_ID:
                    if (fragment instanceof AccountFragment)
                        result = fragment;
                    break;
                case CURRENCY_FRAGMENT_ID:
                    if (fragment instanceof CurrencyFragment)
                        result = fragment;
                    break;
                case PAYMENT_TYPE_FRAGMENT_ID:
                    if (fragment instanceof PaymentTypeFragment)
                        result = fragment;
                    break;
                case UNITS_FRAGMENT_ID:
                    if (fragment instanceof UnitsFragment)
                        result = fragment;
                    break;
            }
        }

        return result;
    }*/
}