package com.jim.multipos.ui.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.db.model.employee.Schedule;
import com.jim.multipos.data.db.model.servercon.UserLogin;
import com.jim.multipos.data.db.model.servercon.UserLoginResponse;
import com.jim.multipos.data.network.MultiPosApiService;
import com.jim.multipos.data.network.oauth2.GetOauthTokenUseCase;
import com.jim.multipos.ui.HasComponent;
import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.registration.di.LoginActivityComponent;
import com.jim.multipos.ui.registration.di.LoginActivityModule;
import com.jim.multipos.ui.registration.fragments.LoginDetailsFragment;
import com.jim.multipos.utils.managers.PosFragmentManager;
import com.jim.multipos.ui.registration.fragments.RegistrationConfirmFragment;
import com.jim.multipos.ui.registration.fragments.RegistrationFragment;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DEV on 25.07.2017.
 */

public class LoginActivity extends BaseActivity implements HasComponent<LoginActivityComponent>, LoginView {
    @Inject
    PosFragmentManager posFragmentManager;
    @Inject
    LoginPresenter presenter;
    @Inject
    GetOauthTokenUseCase getOauthTokenUseCase;
    @Inject
    MultiPosApiService multiPosApiService;
    private LoginActivityComponent loginActivityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_layout);
        presenter.openLoginDetails();

//        getOauthTokenUseCase.login();
//        multiPosApiService.getToken().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(tokenResponse -> {
//            Log.d("retrofitoaxuth", "onResponse: " +tokenResponse);
//        },throwable -> {
//            Log.d("retrofitoauth", "onResponse: " +throwable.toString());
//        });

    }

    @Override
    protected void setupComponent(BaseAppComponent baseAppComponent) {
        loginActivityComponent = baseAppComponent.plus(new LoginActivityModule(this));
        loginActivityComponent.inject(this);
    }

    @Override
    public LoginActivityComponent getComponent() {
        return loginActivityComponent;
    }

    @Override
    public void openLoginDetails() {
        posFragmentManager.displayFragmentWithoutBackStack(new LoginDetailsFragment(), R.id.loginFragment);
    }

    @Override
    public void openRegistration() {
        posFragmentManager.displayFragment(new RegistrationFragment(), R.id.loginFragment);
    }

    @Override
    public void openRegistrationConfirm(RegistrationConfirmFragment confirmFragment) {
        posFragmentManager.displayFragment(confirmFragment, R.id.loginFragment);
    }

    @Override
    public void popFromBackStack() {
        posFragmentManager.popBackStack();
    }

    public void openFirstConfigure() {
        Intent intent = new Intent(this, FirstConfigureActivity.class);
        startActivity(intent);
    }
}
