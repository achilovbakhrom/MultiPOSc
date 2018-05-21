package com.jim.multipos.ui.start_configuration.pos_data;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.utils.CompletionMode;
import com.jim.multipos.ui.start_configuration.StartConfigurationActivity;
import com.jim.multipos.ui.start_configuration.connection.StartConfigurationConnection;

import javax.inject.Inject;

import butterknife.BindView;

public class PosDataFragment extends BaseFragment implements PosDataView {

    @BindView(R.id.etPosId)
    MpEditText etPosId;
    @BindView(R.id.etPosAddress)
    MpEditText etPosAddress;
    @BindView(R.id.etPosAlias)
    MpEditText etPosAlias;
    @BindView(R.id.etPosPhone)
    MpEditText etPosPhone;
    @BindView(R.id.etPassword)
    MpEditText etPassword;
    @BindView(R.id.etConfirmPassword)
    MpEditText etConfirmPassword;
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @Inject
    PosDataPresenter presenter;
    @Inject
    StartConfigurationConnection connection;
    private CompletionMode mode = CompletionMode.NEXT;
    private boolean isConfirmationPasswordChanged = false;

    @Override
    protected int getLayout() {
        return R.layout.pos_data_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        connection.setPosDataView(this);
        etPosPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isConfirmationPasswordChanged && etConfirmPassword.length() != 0)
                    isValid();
            }
        });
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isConfirmationPasswordChanged)
                    isConfirmationPasswordChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnNext.setOnClickListener(view -> {
            if (!etPosId.getText().toString().isEmpty() && !etPosAddress.getText().toString().isEmpty() && !etPosAlias.getText().toString().isEmpty() && !etPosPhone.getText().toString().isEmpty() && isValid()) {
                presenter.savePosDetails(etPosId.getText().toString(), etPosAlias.getText().toString(), etPosAddress.getText().toString(), etPosPhone.getText().toString(), etPassword.getText().toString());
            } else {
                if (etPosId.getText().toString().isEmpty()) {
                    etPosId.setError(getString(R.string.enter_pos_id));
                }
                if (etPosPhone.getText().toString().isEmpty()) {
                    etPosPhone.setError(getString(R.string.enter_phone));
                }
                if (etPosAddress.getText().toString().isEmpty()) {
                    etPosAddress.setError(getString(R.string.enter_pos_address));
                }
                if (etPosAlias.getText().toString().isEmpty()) {
                    etPosAlias.setError(getString(R.string.enter_pos_alias));
                }
                isValid();
                connection.setPosDataCompletion(false);
            }
        });
    }

    public boolean isValid() {
        boolean passwordCorrect = etPassword.getText().toString().equals(etConfirmPassword.getText().toString());
        if (!passwordCorrect) {
            etConfirmPassword.setError(getString(R.string.passwords_different));
        } else {
            etConfirmPassword.setError(null);
        }
        if (etPassword.length() == 0) {
            etPassword.setError(getString(R.string.password_length));
            passwordCorrect = false;
        } else if (etPassword.length() != 6) {
            etPassword.setError(getString(R.string.password_length));
            passwordCorrect = false;
        } else etPassword.setError(null);
        return passwordCorrect;
    }

    @Override
    public void setMode(CompletionMode mode) {
        this.mode = mode;
        if (mode == CompletionMode.NEXT) {
            btnNext.setText(getContext().getString(R.string.next));
        } else btnNext.setText(getContext().getString(R.string.finish));
    }

    @Override
    public void checkPosDataCompletion() {
        connection.setPosDataCompletion(!etPosId.getText().toString().isEmpty() && !etPosAddress.getText().toString().isEmpty() && !etPosAddress.getText().toString().isEmpty() && !etPosPhone.getText().toString().isEmpty() && isValid());
        if (etPosId.getText().toString().isEmpty()) {
            etPosId.setError(getString(R.string.enter_pos_id));
        }
        if (etPosPhone.getText().toString().isEmpty()) {
            etPosPhone.setError(getString(R.string.enter_phone));
        }
        if (etPosAddress.getText().toString().isEmpty()) {
            etPosAddress.setError(getString(R.string.enter_pos_address));
        }
        if (etPosAlias.getText().toString().isEmpty()) {
            etPosAlias.setError(getString(R.string.enter_pos_alias));
        }
    }

    @Override
    public void onComplete() {
        if (mode == CompletionMode.NEXT) {
            connection.setPosDataCompletion(true);
            connection.openNextFragment(2);
        } else {
            presenter.setAppRunFirstTimeValue(false);
            ((StartConfigurationActivity) getActivity()).openLockScreen();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        connection.setPosDataView(null);
    }
}
