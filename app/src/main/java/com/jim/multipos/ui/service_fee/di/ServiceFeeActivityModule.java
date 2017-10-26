package com.jim.multipos.ui.service_fee.di;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.R;
import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.service_fee.ServiceFeeActivity;
import com.jim.multipos.ui.service_fee.ServiceFeePresenter;
import com.jim.multipos.ui.service_fee.ServiceFeePresenterImpl;
import com.jim.multipos.ui.service_fee.ServiceFeeView;
import com.jim.multipos.utils.RxBus;

import javax.inject.Named;

import butterknife.BindView;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by user on 28.08.17.
 */
@Module(includes = BaseActivityModule.class)
public abstract class ServiceFeeActivityModule {
    @Binds
    @PerActivity
    abstract AppCompatActivity provideServiceFeeActivityModule(ServiceFeeActivity activity);

    @Binds
    @PerActivity
    abstract ServiceFeeView provideServiceFeeView(ServiceFeeActivity activity);

    @Binds
    @PerActivity
    abstract ServiceFeePresenter provideServiceFeePresenter(ServiceFeePresenterImpl presenter);

    @PerActivity
    @Provides
    @Named(value = "enter_name")
    static String provideEnterName(Context context) {
        return context.getString(R.string.enter_name);
    }

    @PerActivity
    @Provides
    @Named(value = "enter_amount")
    static String provideEnterAmount(Context context) {
        return context.getString(R.string.enter_amount);
    }

    @PerActivity
    @Provides
    @Named(value = "should_not_be_greater_than_999")
    static String provideshouldNotBeGreaterThan999(Context context) {
        return context.getString(R.string.should_not_be_greater_than_999);
    }

    @PerActivity
    @Provides
    @Named(value = "service_fee_type")
    static String[] provideServiceFeeType(Context context) {
        return context.getResources().getStringArray(R.array.service_fee_type);
    }

    @PerActivity
    @Provides
    @Named(value = "service_fee_app_type")
    static String[] provideServiceFeeAppType(Context context) {
        return context.getResources().getStringArray(R.array.service_fee_app_type);
    }
}
