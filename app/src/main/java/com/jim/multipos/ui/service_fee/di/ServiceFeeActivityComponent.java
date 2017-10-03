package com.jim.multipos.ui.service_fee.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.service_fee.ServiceFeeActivity;

import dagger.Subcomponent;

/**
 * Created by user on 28.08.17.
 */

@ActivityScope
@Subcomponent(modules = {ServiceFeeActivityModule.class})
public interface ServiceFeeActivityComponent {
    void inject(ServiceFeeActivity activity);
}
