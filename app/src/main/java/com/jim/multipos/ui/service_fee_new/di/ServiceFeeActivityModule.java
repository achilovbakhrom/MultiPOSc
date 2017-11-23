package com.jim.multipos.ui.service_fee_new.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.service_fee_new.ServiceFeeActivity;
import com.jim.multipos.ui.service_fee_new.ServiceFeePresenter;
import com.jim.multipos.ui.service_fee_new.ServiceFeePresenterImpl;
import com.jim.multipos.ui.service_fee_new.ServiceFeeView;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Portable-Acer on 28.10.2017.
 */

@Module(includes = BaseActivityModule.class)
public abstract class ServiceFeeActivityModule {
    @Binds
    @PerActivity
    abstract AppCompatActivity provideServiceFeeActivity(ServiceFeeActivity activity);

    @Binds
    @PerActivity
    abstract ServiceFeeView provideServiceFeeView(ServiceFeeActivity activity);

    @Binds
    @PerActivity
    abstract ServiceFeePresenter provideServiceFeePresenter(ServiceFeePresenterImpl presenter);
}
