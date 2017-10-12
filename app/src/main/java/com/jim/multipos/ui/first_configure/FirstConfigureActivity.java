package com.jim.multipos.ui.first_configure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.ui.first_configure.fragments.AccountFragment;
import com.jim.multipos.ui.first_configure.fragments.CurrencyFragment;
import com.jim.multipos.ui.first_configure.fragments.LeftSideFragment;
import com.jim.multipos.ui.first_configure.fragments.PaymentTypeFragment;
import com.jim.multipos.ui.first_configure.fragments.PosDetailsFragment;
import com.jim.multipos.ui.first_configure.fragments.UnitsFragment;
import com.jim.multipos.utils.RxBusLocal;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import static com.jim.multipos.ui.first_configure.Constants.ACCOUNT_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.PAYMENT_TYPE_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.POS_DETAIL_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.SINGLE_CURRENCY_FRAGMENT_ID;
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
        switch (position) {
            case POS_DETAIL_FRAGMENT_ID:
                addFragmentToRight(new PosDetailsFragment());
                break;
            case ACCOUNT_FRAGMENT_ID:
                addFragmentToRight(new AccountFragment());
                break;
            case SINGLE_CURRENCY_FRAGMENT_ID:
                addFragmentToRight(new CurrencyFragment());
                break;
            case PAYMENT_TYPE_FRAGMENT_ID:
                addFragmentToRight(new PaymentTypeFragment());
                break;
            case UNITS_FRAGMENT_ID:
                addFragmentToRight(new UnitsFragment());
                break;
        }
    }

    @Override
    public void openPrevFragment() {
        popFragmentFromRight();
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
        if (getCurrentFragmentRight() instanceof  AccountFragment) {
            ((AccountFragment) getCurrentFragmentRight()).updateAccountList(account);
        }
    }

    @Override
    public void addPaymentTypeItem(PaymentType paymentType) {
        if (getCurrentFragmentRight() instanceof  PaymentTypeFragment) {
            ((PaymentTypeFragment) getCurrentFragmentRight()).addPaymentTypeItem(paymentType);
        }
    }

    @Override
    public void removeAccountItem(Account account) {
        if (getCurrentFragmentRight() instanceof  AccountFragment) {
            ((AccountFragment) getCurrentFragmentRight()).removeAccountItem(account);
        }
    }

    @Override
    public void removePaymentTypeItem(PaymentType paymentType) {
        if (getCurrentFragmentRight() instanceof  PaymentTypeFragment) {
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
}