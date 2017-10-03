package com.jim.multipos.ui.first_configure.di;

import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.first_configure.FirstConfigurePresenter;
import com.jim.multipos.ui.first_configure.FirstConfigurePresenterImpl;
import com.jim.multipos.ui.first_configure.presenters.AccountFragmentPresenter;
import com.jim.multipos.ui.first_configure.presenters.AccountFragmentPresenterImpl;
import com.jim.multipos.ui.first_configure.presenters.CurrencyFragmentPresenter;
import com.jim.multipos.ui.first_configure.presenters.CurrencyFragmentPresenterImpl;
import com.jim.multipos.ui.first_configure.presenters.FirstConfigureLeftSidePresenter;
import com.jim.multipos.ui.first_configure.presenters.FirstConfigureLeftSidePresenterImpl;
import com.jim.multipos.ui.first_configure.presenters.PaymentTypeFragmentPresenter;
import com.jim.multipos.ui.first_configure.presenters.PaymentTypeFragmentPresenterImpl;
import com.jim.multipos.ui.first_configure.presenters.PosFragmentPresenter;
import com.jim.multipos.ui.first_configure.presenters.PosFragmentPresenterImpl;
import com.jim.multipos.ui.first_configure.presenters.StockFragmentPresenter;
import com.jim.multipos.ui.first_configure.presenters.StockFragmentPresenterImpl;
import com.jim.multipos.ui.first_configure.presenters.UnitsFragmentPresenter;
import com.jim.multipos.ui.first_configure.presenters.UnitsFragmentPresenterImpl;
import com.jim.multipos.utils.managers.PosFragmentManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by user on 31.07.17.
 */
@Module
public class FirstConfigureActivityModule {
    private FirstConfigureActivity activity;

    public FirstConfigureActivityModule(FirstConfigureActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public FirstConfigureActivity getActivity(){
        return activity;
    }

    @Provides
    @ActivityScope
    public PosFragmentManager getFragmentManager(FirstConfigureActivity activity) {
        return new PosFragmentManager(activity);
    }

    @Provides
    @ActivityScope
    public FirstConfigurePresenter getFirstConfigurePresenter() {
        return new FirstConfigurePresenterImpl();
    }

    @Provides
    @ActivityScope
    public FirstConfigureLeftSidePresenter getFirstConfigureLeftSidePresenter(FirstConfigureActivity activity) {
        return new FirstConfigureLeftSidePresenterImpl(activity);
    }

    @Provides
    @ActivityScope
    public PosFragmentPresenter getPosFragmentPresenter(FirstConfigureActivity activity) {
        return new PosFragmentPresenterImpl(activity);
    }

    @Provides
    @ActivityScope
    public AccountFragmentPresenter getAccountFragmentPresenter(FirstConfigureActivity activity, PaymentTypeFragmentPresenter paymentTypeFragmentPresenter, DatabaseManager databaseManager) {
        return new AccountFragmentPresenterImpl(activity, paymentTypeFragmentPresenter, databaseManager.getAccountOperations());
    }

    @Provides
    @ActivityScope
        public PaymentTypeFragmentPresenter getPaymentTypeFragmentPresenter(FirstConfigureActivity activity, DatabaseManager databaseManager) {
        return new PaymentTypeFragmentPresenterImpl(activity, databaseManager.getPaymentTypeOperations(), databaseManager.getAccountOperations(), databaseManager.getCurrencyOperations());
    }

    @Provides
    @ActivityScope
    public CurrencyFragmentPresenter getCurrencyFragmentPresenter(FirstConfigureActivity activity, PaymentTypeFragmentPresenter paymentTypeFragmentPresenter, DatabaseManager databaseManager) {
        return new CurrencyFragmentPresenterImpl(activity, paymentTypeFragmentPresenter, databaseManager.getCurrencyOperations());
    }

    @Provides
    @ActivityScope
    public UnitsFragmentPresenter getUnitsFragmentPresenter(FirstConfigureActivity activity, DatabaseManager databaseManager) {
        return new UnitsFragmentPresenterImpl(activity, databaseManager.getUnitCategoryOperations(), databaseManager.getUnitOperations());
    }

    @Provides
    @ActivityScope
    public StockFragmentPresenter getStockFragmentPresenter(FirstConfigureActivity activity, DatabaseManager databaseManager) {
        return new StockFragmentPresenterImpl(activity, databaseManager.getStockOperations());
    }
}
