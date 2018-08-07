package com.jim.multipos.ui.admin_auth_signup.fragments.general;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.Utils;
import com.jim.multipos.utils.rxevents.admin_auth_events.GeneralEvent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralFragment extends BaseFragment implements GeneralView{

    @Inject
    RxBus bus;

    @BindView(R.id.etLogin)
    MpEditText etLogin;

    @BindView(R.id.etPassword)
    MpEditText etPassword;

    @BindView(R.id.etConfirmPassword)
    MpEditText etConfirmPassword;

    @Inject
    GeneralPresenter presenter;

    @OnClick(R.id.nextBtn)
    public void onNextClick(View view){
        presenter.onVerifyData(etLogin.getText().toString(), etPassword.getText().toString(), etConfirmPassword.getText().toString());
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_general;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    public void onSuccess(String mail, String password) {
        bus.send(new GeneralEvent(mail, password));
    }

    @Override
    public void onError(int error, int type) {
        if(type == 1)
            etLogin.setError(getString(error));
        else etConfirmPassword.setError(getString(error));
    }

}
