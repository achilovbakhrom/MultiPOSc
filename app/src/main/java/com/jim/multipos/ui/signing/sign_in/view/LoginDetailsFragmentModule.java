package com.jim.multipos.ui.signing.sign_in.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.signing.sign_in.presenter.LoginDetailsPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 12.10.2017.
 */

@Module(includes = {
        LoginDetailsPresenterModule.class
})
public abstract class LoginDetailsFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(LoginDetailsFragment detailsFragment);

    @Binds
    @PerFragment
    abstract LoginDetailsView provideLoginDetailsView(LoginDetailsFragment detailsFragment);
}
