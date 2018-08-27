package com.jim.multipos.ui.admin_main_page.fragments.company.di;


import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.admin_main_page.fragments.company.CompanyAddFragment;
import com.jim.multipos.ui.admin_main_page.fragments.company.presenter.CompanyFragmentPresenter;
import com.jim.multipos.ui.admin_main_page.fragments.company.presenter.CompanyFragmentPresenterImpl;
import com.jim.multipos.ui.admin_main_page.fragments.company.presenter.CompanyFragmentView;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CompanyFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(CompanyAddFragment companyFragment);

    @Binds
    @PerFragment
    abstract CompanyFragmentView provideView(CompanyAddFragment companyFragment);

    @Binds
    @PerFragment
    abstract CompanyFragmentPresenter providePresenter(CompanyFragmentPresenterImpl presenter);

}
