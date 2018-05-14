package com.jim.multipos.ui.lock_screen.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.lock_screen.LockScreenActivity;
import com.jim.multipos.ui.lock_screen.LockScreenPresenter;
import com.jim.multipos.ui.lock_screen.LockScreenPresenterImpl;
import com.jim.multipos.ui.lock_screen.LockScreenView;
import com.jim.multipos.ui.lock_screen.auth.AuthFragment;
import com.jim.multipos.ui.lock_screen.auth.AuthFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Sirojiddin on 04.08.2017.
 */
@Module(includes = BaseActivityModule.class)
public abstract class LockScreenActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideLoginPageActivity(LockScreenActivity loginPageActivity);

    @Binds
    @PerActivity
    abstract LockScreenView provideLoginPageView(LockScreenActivity loginPageActivity);

    @Binds
    @PerActivity
    abstract LockScreenPresenter provideLoginPagePresenter(LockScreenPresenterImpl presenter);

    @PerFragment
    @ContributesAndroidInjector(modules = AuthFragmentModule.class)
    abstract AuthFragment provideAuthFragment();


}
