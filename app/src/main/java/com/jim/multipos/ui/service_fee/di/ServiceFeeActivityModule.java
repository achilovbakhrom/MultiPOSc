package com.jim.multipos.ui.service_fee.di;

import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.service_fee.ServiceFeeActivity;
import com.jim.multipos.ui.service_fee.ServiceFeePresenter;
import com.jim.multipos.ui.service_fee.ServiceFeePresenterImpl;
import com.jim.multipos.utils.RxBus;

import dagger.Module;
import dagger.Provides;

/**
 * Created by user on 28.08.17.
 */
@Module
public class ServiceFeeActivityModule {
    private ServiceFeeActivity activity;

    public ServiceFeeActivityModule(ServiceFeeActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public ServiceFeeActivity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityScope
    public ServiceFeePresenter getServiceFeePresenter(RxBus rxBus, ServiceFeeActivity activity, DatabaseManager databaseManager) {
        return new ServiceFeePresenterImpl(rxBus, activity, activity, databaseManager.getServiceFeeOperations(), databaseManager.getCurrencyOperations(), databaseManager.getPaymentTypeOperations());
    }
}
