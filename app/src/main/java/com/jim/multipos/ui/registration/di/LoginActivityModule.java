package com.jim.multipos.ui.registration.di;


import com.jim.multipos.ui.registration.LoginActivity;
import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.utils.managers.PosFragmentManager;
import com.jim.multipos.ui.registration.presenters.LoginDetailsPresenter;
import com.jim.multipos.ui.registration.presenters.LoginDetailsPresenterImpl;
import com.jim.multipos.ui.registration.presenters.RegistrationConfirmPresenter;
import com.jim.multipos.ui.registration.presenters.RegistrationConfirmPresenterImpl;
import com.jim.multipos.ui.registration.presenters.RegistrationPresenter;
import com.jim.multipos.ui.registration.presenters.RegistrationPresenterImpl;
import com.jim.multipos.ui.registration.LoginPresenter;
import com.jim.multipos.ui.registration.LoginPresenterImpl;


import dagger.Module;
import dagger.Provides;

/**
 * Created by DEV on 27.07.2017.
 */
@Module
public class LoginActivityModule {
    private LoginActivity activity;

    public LoginActivityModule(LoginActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public LoginActivity getActivity() {
        return activity;
    }

    @Provides
    @ActivityScope
    public PosFragmentManager getFragmentManager(LoginActivity activity) {
        return new PosFragmentManager(activity);
    }

    @Provides
    @ActivityScope
    public LoginPresenter getLoginPresenter(LoginActivity loginActivity){
        return new LoginPresenterImpl(loginActivity);
    }



    @Provides
    @ActivityScope
    public RegistrationPresenter getRegistrationPresenter(DatabaseManager databaseManager) {
        return new RegistrationPresenterImpl(databaseManager);
    }

    @Provides
    @ActivityScope
    public LoginDetailsPresenter getLoginDetailsFragmentPresenter(){
        return new LoginDetailsPresenterImpl();
    }

    @Provides
    @ActivityScope
    public RegistrationConfirmPresenter getRegistrationConfirmPresenter(){
        return new RegistrationConfirmPresenterImpl();
    }
}
