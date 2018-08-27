package com.jim.multipos.ui.admin_main_page.fragments.establishment.di;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.EstablishmentAddFragment;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.presenter.EstablishmentFragmentPresenter;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.presenter.EstablishmentFragmentPresenterImpl;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.presenter.EstablishmentFragmentView;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class EstablishmentAddModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(EstablishmentAddFragment fragment);

    @Binds
    @PerFragment
    abstract EstablishmentFragmentView provideView(EstablishmentAddFragment fragment);

    @Binds
    @PerFragment
    abstract EstablishmentFragmentPresenter providePresenter(EstablishmentFragmentPresenterImpl presenter);
}
