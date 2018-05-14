package com.jim.multipos.ui.secure.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.secure.AuthActivity;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 04.08.2017.
 */
@Module(includes = BaseActivityModule.class)
public abstract class AuthActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideLoginPageActivity(AuthActivity forPinnigActivity);


}
