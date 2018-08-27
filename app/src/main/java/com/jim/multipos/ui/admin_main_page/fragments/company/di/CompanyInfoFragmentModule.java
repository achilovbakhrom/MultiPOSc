package com.jim.multipos.ui.admin_main_page.fragments.company.di;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.admin_main_page.fragments.company.CompanyInfoFragment;
import com.jim.multipos.ui.admin_main_page.fragments.company.presenter.CompanyFragmentPresenter;
import com.jim.multipos.ui.admin_main_page.fragments.company.presenter.CompanyFragmentPresenterImpl;
import com.jim.multipos.ui.admin_main_page.fragments.company.presenter.CompanyFragmentView;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CompanyInfoFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideInfoFragment(CompanyInfoFragment fragment);
//
//    @Binds
//    @PerFragment
//    abstract CompanyFragmentView provideView(CompanyInfoFragment fragment);
//
//    @Binds
//    @PerFragment
//    abstract CompanyFragmentPresenter providePresenter(CompanyFragmentPresenterImpl presenter);
}
