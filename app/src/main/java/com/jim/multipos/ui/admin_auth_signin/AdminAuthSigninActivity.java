package com.jim.multipos.ui.admin_auth_signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.network.model.SigninResponse;
import com.jim.multipos.ui.admin_auth_signup.AdminAuthSignupActivity;
import com.jim.multipos.ui.admin_main_page.AdminMainPageActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminAuthSigninActivity extends BaseActivity implements AdminAuthSigninActivityView {

    @Inject
    AdminAuthSigninActivityPresenter presenter;

    @BindView(R.id.etUsername)
    MpEditText etUsername;
    @BindView(R.id.etPassword)
    MpEditText etPassword;
    @BindView(R.id.toolbar)
    MpToolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_signin_layout);
        ButterKnife.bind(this);
        toolbar.setOnBackArrowClick(this::finish);
    }

    public void SignIn(View view) {
        startActivity(new Intent(this, AdminMainPageActivity.class));
//        presenter.onSignUp(new Signin(AppConstants.GRANT_TYPE, AppConstants.CLIENT_ID, AppConstants.CLIENT_SECRET,
//                etUsername.getText().toString(), etPassword.getText().toString()));
    }

    @Override
    public void onSignInSucces(SigninResponse response) {
        if (response.getCode() == 200)
            Toast.makeText(this, response.getData().getAccess_token(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSignInError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();

    }

    public void SignUp(View view) {
        startActivity(new Intent(this, AdminAuthSignupActivity.class));
    }
}
