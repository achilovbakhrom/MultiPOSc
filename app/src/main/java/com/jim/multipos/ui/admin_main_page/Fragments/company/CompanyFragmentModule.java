package com.jim.multipos.ui.admin_main_page.Fragments.company;


import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.reports.customers.CustomerReportFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CompanyFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(CompanyFragment companyFragment);
}
