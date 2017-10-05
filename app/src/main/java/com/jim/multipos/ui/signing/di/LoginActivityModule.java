package com.jim.multipos.ui.signing.di;


import com.jim.multipos.ui.signing.SignActivity;
import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.utils.managers.PosFragmentManager;
import com.jim.multipos.ui.signing.sign_in.LoginDetailsPresenter;
import com.jim.multipos.ui.signing.sign_in.LoginDetailsPresenterImpl;
import com.jim.multipos.ui.signing.sign_up.RegistrationConfirmPresenter;
import com.jim.multipos.ui.signing.sign_up.RegistrationConfirmPresenterImpl;
import com.jim.multipos.ui.signing.sign_up.RegistrationPresenter;
import com.jim.multipos.ui.signing.sign_up.RegistrationPresenterImpl;
import com.jim.multipos.ui.signing.sign_in.LoginPresenter;
import com.jim.multipos.ui.signing.sign_in.LoginPresenterImpl;


import dagger.Module;
import dagger.Provides;

/**
 * Created by DEV on 27.07.2017.
 */
@Module
public class LoginActivityModule {
    private SignActivity activity;

    public LoginActivityModule(SignActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public SignActivity getActivity() {
        return activity;
    }

    @Provides
    @ActivityScope
    public PosFragmentManager getFragmentManager(SignActivity activity) {
        return new PosFragmentManager(activity);
    }

    @Provides
    @ActivityScope
    public LoginPresenter getLoginPresenter(SignActivity signActivity){
        return new LoginPresenterImpl(signActivity);
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
