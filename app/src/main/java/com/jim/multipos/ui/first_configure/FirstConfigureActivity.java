package com.jim.multipos.ui.first_configure;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.ui.first_configure.fragments.AccountFragment;
import com.jim.multipos.ui.first_configure.fragments.CurrencyFragment;
import com.jim.multipos.ui.first_configure.fragments.LeftSideFragment;
import com.jim.multipos.ui.first_configure.fragments.PaymentTypeFragment;
import com.jim.multipos.ui.first_configure.fragments.PosDetailsFragment;
import com.jim.multipos.ui.first_configure.fragments.UnitsFragment;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.FirstConfigureActivityEvent;
import java.util.ArrayList;
import java.util.Map;
import javax.inject.Inject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import static com.jim.multipos.ui.first_configure.Constants.ACCOUNT_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.LEFT_SIDE_FRAGMENT_OPENED;
import static com.jim.multipos.ui.first_configure.Constants.OPEN_NEXT_FROM_POS_DETAILS;
import static com.jim.multipos.ui.first_configure.Constants.PAYMENT_TYPE_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.POS_DETAIL_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.SINGLE_CURRENCY_FRAGMENT_ID;
import static com.jim.multipos.ui.first_configure.Constants.UNITS_FRAGMENT_ID;

/**
 * Created by user on 07.10.17.
 */

public class FirstConfigureActivity extends DoubleSideActivity implements FirstConfigureView {
    @Inject
    FirstConfigurePresenter presenter;
    @Inject
    RxBusLocal rxBusLocal;
    private ArrayList<Disposable> subscriptions = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rxConnections();
        addFragmentToRight(new PosDetailsFragment());
        presenter.getLeftSideFragmentData();
    }

    @Override
    protected void onDestroy() {
        RxBusLocal.removeListners(subscriptions);
        super.onDestroy();
    }

    private void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(rxBusLocal.toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
            if (o instanceof FirstConfigureActivityEvent) {
                FirstConfigureActivityEvent event = (FirstConfigureActivityEvent) o;
                if (event.getEventType().equals(OPEN_NEXT_FROM_POS_DETAILS)) {
                    openNextFragment();
                } else if (event.getEventType().equals(LEFT_SIDE_FRAGMENT_OPENED)) {
                    int position = (int) event.getObject();
                    replaceFragment(position);
                }
            }
        }));
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    @Override
    public void showPosDetailsErrors(Map<String, String> errors) {
        PosDetailsFragment posDetailsFragment = getPostDetailsFragment();
        posDetailsFragment.showErrors(errors);
    }

    @Override
    public void replaceFragment(int position, boolean isNextButton) {
        switch (position) {
            case POS_DETAIL_FRAGMENT_ID:
                replaceFragmentToRight(new PosDetailsFragment());
                break;
            case ACCOUNT_FRAGMENT_ID:
                replaceFragmentToRight(new AccountFragment());
                break;
            case SINGLE_CURRENCY_FRAGMENT_ID:
                break;
            case PAYMENT_TYPE_FRAGMENT_ID:
                break;
            case UNITS_FRAGMENT_ID:
                break;
        }
    }

    @Override
    public void replaceFragment(int position) {
        switch (position) {
            case POS_DETAIL_FRAGMENT_ID:
                replaceFragmentToRight(new PosDetailsFragment());
                break;
            case ACCOUNT_FRAGMENT_ID:
                replaceFragmentToRight(new AccountFragment());
                break;
            case SINGLE_CURRENCY_FRAGMENT_ID:
                break;
            case PAYMENT_TYPE_FRAGMENT_ID:
                break;
            case UNITS_FRAGMENT_ID:
                break;
        }
    }

    @Override
    public void closeActivity() {
        finish();
    }

    private PosDetailsFragment getPostDetailsFragment() {
        return (PosDetailsFragment) getCurrentFragmentRight();
    }

    private void openNextFragment() {
        Map<String, String> data = null;
        Fragment fragment = getCurrentFragmentRight();

        if (fragment instanceof PosDetailsFragment) {
            data = ((PosDetailsFragment) fragment).getData();
            presenter.checkPosDetailsFragmentData(data);
        } else if (fragment instanceof  AccountFragment) {

        } else if (fragment instanceof CurrencyFragment) {

        } else if (fragment instanceof PaymentTypeFragment) {

        } else if (fragment instanceof UnitsFragment) {

        }
    }

    @Override
    public void showLeftSideFragment(boolean[] isCompletedFragments) {
        LeftSideFragment fragment = LeftSideFragment.newInstance(isCompletedFragments);
        addFragmentToLeft(fragment);
    }
}