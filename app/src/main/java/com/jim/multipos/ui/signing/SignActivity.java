package com.jim.multipos.ui.signing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.data.network.MultiPosApiService;
import com.jim.multipos.data.network.oauth2.GetOauthTokenUseCase;
import com.jim.multipos.ui.HasComponent;
import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.signing.di.LoginActivityComponent;
import com.jim.multipos.ui.signing.sign_in.LoginDetailsFragment;
import com.jim.multipos.ui.signing.sign_in.LoginPresenter;
import com.jim.multipos.ui.signing.sign_in.LoginView;
import com.jim.multipos.utils.managers.PosFragmentManager;
import com.jim.multipos.ui.signing.sign_up.RegistrationConfirmFragment;
import com.jim.multipos.ui.signing.sign_up.RegistrationFragment;

import javax.inject.Inject;

/**
 * Created by DEV on 25.07.2017.
 */

public class SignActivity extends SimpleActivity implements SignView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openSignIn();
    }

    @Override
    protected int getToolbar() {
        return WITHOUT_TOOLBAR;
    }

    @Override
    public void openSignUp() {
        replaceFragment(new RegistrationFragment());
    }

    @Override
    public void openSignIn() {
        replaceFragment(new LoginDetailsFragment());
    }

    @Override
    public void openConfirmation() {
        replaceFragment(new RegistrationConfirmFragment());
    }


//    @Override
//    public LoginActivityComponent getComponent() {
//        return loginActivityComponent;
//    }
//
//    @Override
//    public void openLoginDetails() {
//        posFragmentManager.displayFragmentWithoutBackStack(new LoginDetailsFragment(), R.id.loginFragment);
//    }
//
//    @Override
//    public void openRegistration() {
//        posFragmentManager.displayFragment(new RegistrationFragment(), R.id.loginFragment);
//    }
//
//    @Override
//    public void openRegistrationConfirm(RegistrationConfirmFragment confirmFragment) {
//        posFragmentManager.displayFragment(confirmFragment, R.id.loginFragment);
//    }
//
//    @Override
//    public void popFromBackStack() {
//        posFragmentManager.popBackStack();
//    }
//
//    public void openFirstConfigure() {
//        Intent intent = new Intent(this, FirstConfigureActivity.class);
//        startActivity(intent);
//    }
}
