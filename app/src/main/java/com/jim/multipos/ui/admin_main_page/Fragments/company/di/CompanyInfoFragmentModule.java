package com.jim.multipos.ui.admin_main_page.fragments.company.di;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.admin_main_page.fragments.company.CompanyInfoFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CompanyInfoFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideInfoFragment(CompanyInfoFragment fragment);
}