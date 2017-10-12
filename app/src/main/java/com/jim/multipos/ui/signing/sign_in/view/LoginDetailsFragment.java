package com.jim.multipos.ui.signing.sign_in.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.mpviews.MpButton;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.R;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.login.LoginPageActivity;
import com.jim.multipos.ui.signing.sign_in.LoginDetailsPresenter;
import com.jim.multipos.utils.managers.PosFragmentManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by DEV on 26.07.2017.
 */

public class LoginDetailsFragment extends BaseFragment { //implements LoginDetailsFragmentView {

    @Inject
    PosFragmentManager posFragmentManager;
    @Inject
    LoginDetailsPresenter presenter;
    @Inject
    PreferencesHelper preferencesHelper;

    @BindView(R.id.btnLogin)
    MpButton btnLogin;
    @BindView(R.id.btnRegistration)
    MpButton btnRegistration;
    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
//        this.getComponent(LoginActivityComponent.class).inject(this);
//        presenter.init(this);
        return rootView;
    }

    @Override
    protected int getLayout() {
        return R.layout.login_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void rxConnections() {

    }

    @OnClick(R.id.btnLogin)
    public void login() {
        presenter.loginFounder();
    }

    @OnClick(R.id.btnRegistration)
    public void registration() {
        presenter.registerFounder();
    }


    public void onRegistration() {
//        activity.openRegistration();
//        posFragmentManager.displayFragment(new SignUpFragment(), R.id.loginFragment);
    }


    public void onLogin() {
        Intent intent = null;
//
//        if(preferencesHelper.isFirstConfigured())
            intent = new Intent(getActivity(), LoginPageActivity.class);
//        else
//            intent = new Intent(getActivity(), FirstConfigureActivity.class);
        startActivity(intent);
//        activity.finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
