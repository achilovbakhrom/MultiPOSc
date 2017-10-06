package com.jim.multipos.ui.signing;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.signing.sign_in.view.LoginDetailsFragment;
import com.jim.multipos.ui.signing.sign_up.view.RegistrationConfirmFragment;
import com.jim.multipos.ui.signing.sign_up.view.SignUpFragment;

/**
 * Created by DEV on 25.07.2017.
 */

public class SignActivity extends SimpleActivity implements SignView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openSignUp();
    }

    @Override
    protected int getToolbar() {
        return WITHOUT_TOOLBAR;
    }

    @Override
    public void openSignUp() {
        replaceFragment(new SignUpFragment());
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
//        posFragmentManager.displayFragment(new SignUpFragment(), R.id.loginFragment);
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
