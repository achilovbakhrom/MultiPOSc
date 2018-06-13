package com.jim.multipos.ui.lock_screen.auth;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.lock_screen.LockScreenActivity;

import javax.inject.Inject;

import butterknife.BindView;

public class AuthFragment extends BaseFragment implements AuthView {

    @BindView(R.id.tvSerialNumber)
    TextView tvSerialNumber;

    @BindView(R.id.flConfirm)
    FrameLayout flConfirm;

    @BindView(R.id.flCall)
    FrameLayout flCall;

    @BindView(R.id.etToken)
    EditText etToken;

    @Inject
    AuthPresenter presenter;

    @Override
    protected int getLayout() {
        return R.layout.auth_activity;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.onCreateView(savedInstanceState);
        flConfirm.setOnClickListener(view -> {
            if(etToken.getText().toString().length()>0)
            presenter.checkRegistrationToken(etToken.getText().toString());
            else etToken.setError(getString(R.string.erg_tok_cant_be_emp));
        });

    }

    @Override
    public void setSerialNumberToView(String serial) {
        tvSerialNumber.setText(serial);
    }

    @Override
    public void invalidToken() {
        etToken.setError(getString(R.string.reg_token_invalid));
    }

    @Override
    public void valid() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        ((LockScreenActivity)getActivity()).openFirstConfigure();
    }
}
