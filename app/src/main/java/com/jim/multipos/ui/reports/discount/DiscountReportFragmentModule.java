package com.jim.multipos.ui.reports.discount;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 20.01.2018.
 */
@Module(
        includes = DiscountReportPresenterModule.class
)
public abstract class DiscountReportFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(DiscountReportFragment discountReportFragment);

    @Binds
    @PerFragment
    abstract DiscountReportView provideDiscountReportView(DiscountReportFragment discountReportFragment);

}
