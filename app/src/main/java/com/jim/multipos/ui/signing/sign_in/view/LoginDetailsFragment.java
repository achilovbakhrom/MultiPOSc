package com.jim.multipos.ui.signing.sign_in.view;

import android.os.Bundle;

import com.jim.mpviews.MpButton;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.R;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.signing.sign_in.presenter.LoginDetailsPresenter;
import com.jim.multipos.utils.managers.PosFragmentManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by DEV on 26.07.2017.
 */

public class LoginDetailsFragment extends BaseFragment implements LoginDetailsView {

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
    public void onLogin() {
        presenter.loginFounder();
    }

    @OnClick(R.id.btnRegistration)
    public void onRegistration() {
        presenter.registerFounder();
    }

}
