package com.jim.multipos.ui.first_configure_last.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCompletedStateView;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.first_configure_last.CompletionMode;
import com.jim.multipos.ui.first_configure_last.ChangeableContent;
import com.jim.multipos.ui.first_configure_last.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure_last.FirstConfigurePresenter;

import butterknife.BindView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.MaxLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

/**
 * Created by Achilov Bakhrom on 10/18/17.
 */

public class POSDetailsFragment extends BaseFragment implements ChangeableContent {

    private final String MODE_KEY = "POS_DETAILS_MODE_KEY";

    @NotEmpty(messageId = R.string.enter_pos_id)
    @BindView(R.id.etPosId)
    MpEditText posId;
    @NotEmpty(messageId = R.string.enter_alias)
    @BindView(R.id.etAlias)
    MpEditText alias;
    @NotEmpty(messageId = R.string.enter_address)
    @BindView(R.id.etAddress)
    MpEditText address;
    @MaxLength(value = 6, messageId = R.string.password_length_not_less_than_6)
    @BindView(R.id.etPassword)
    MpEditText password;
    @MaxLength(value = 6, messageId = R.string.password_length_not_less_than_6)
    @BindView(R.id.etConfirmPassword)
    MpEditText confirmPassword;
    @BindView(R.id.btnNext)
    MpButton next;
    CompletionMode mode = CompletionMode.NEXT;
    private boolean isConfirmationPasswordChanged = false;

    @Override
    protected int getLayout() {
        return R.layout.pos_details_fragment;
    }

    /**
     * For decision, will the fragment use the dagger 2
     * in the content
     *
     * @return true - dagger included, false - dagger will be not used
     */
    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            setMode((CompletionMode) getArguments().getSerializable(MODE_KEY));
        }
        fillPOSDetailsData();
        password.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (isConfirmationPasswordChanged && !password.getText().toString().equals(confirmPassword.getText().toString())) {
                    confirmPassword.setError(getString(R.string.passwords_different));
                }
            }
        });

        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isConfirmationPasswordChanged && confirmPassword.length() != 0)
                    isValid();
            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
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
        confirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    confirmPassword.setError(getString(R.string.passwords_different));
                }
            }
        });
    }

    private void fillPOSDetailsData() {
        FirstConfigurePresenter presenter = ((FirstConfigureActivity) getContext()).getPresenter();
        if (presenter.getPOSId() != null) {
            posId.setText(presenter.getPOSId());
        }
        if (presenter.getPOSAlias() != null) {
            alias.setText(presenter.getPOSAlias());
        }
        if (presenter.getPOSAddress() != null) {
            address.setText(presenter.getPOSAddress());
        }
        if (presenter.getPassword() != null) {
            password.setText(presenter.getPassword());
            confirmPassword.setText(presenter.getPassword());
        }
    }

    @OnClick(value = {R.id.btnRevert, R.id.btnNext})
    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.btnRevert:
                getActivity().finish();
                break;
            case R.id.btnNext:
                if (isValid()) {
                    if (mode == CompletionMode.NEXT) {
                        FirstConfigureActivity activity = (FirstConfigureActivity) getContext();
                        activity.getPresenter().savePOSDetails(posId.getText().toString(),
                                alias.getText().toString(),
                                address.getText().toString(),
                                password.getText().toString());
                        activity.getPresenter().setCompletedForFragment(getClass().getName(), true);
                        activity.getPresenter().openAccount();
                        activity.changeState(FirstConfigurePresenter.POS_DETAILS_POSITION,
                                MpCompletedStateView.COMPLETED_STATE);
                    } else {
                        ((FirstConfigureActivity) getContext()).getPreferencesHelper().setAppRunFirstTimeValue(false);
                        ((FirstConfigureActivity) getActivity()).openLockScreen();
                    }
                } else {
                    ((FirstConfigureActivity) getContext())
                            .changeState(FirstConfigurePresenter.POS_DETAILS_POSITION, MpCompletedStateView.WARNING_STATE);
                }
                break;
        }
    }

    @Override
    public boolean isValid() {
        boolean temp = super.isValid();
        boolean passwordCorrect = password.getText().toString().equals(confirmPassword.getText().toString());
        if (!passwordCorrect) {
            confirmPassword.setError(getString(R.string.passwords_different));
        } else {
            confirmPassword.setError(null);
        }
        if (password.length() == 0) {
            password.setError(getString(R.string.password_length));
            passwordCorrect = false;
        } else if (password.length() != 6) {
            password.setError("The password must contain 6 characters");
            passwordCorrect = false;
        } else password.setError(null);
        return passwordCorrect && temp;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MODE_KEY, mode);
    }

    /**
     * setter for type of next button, which allowed
     * two type: NEXT and FINISH
     *
     * @param mode - for the button, which type of CompletionMode
     */
    @Override
    public void setMode(CompletionMode mode) {
        this.mode = mode;
        switch (mode) {
            case NEXT:
                next.setText(R.string.next);
                break;
            case FINISH:
                next.setText(R.string.finish);
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            ((FirstConfigureActivity) getContext()).getPresenter().checkPOSDetailsCorrection(posId.getText().toString(),
                    alias.getText().toString(), address.getText().toString(),
                    (password.getText().toString().equals(confirmPassword.getText().toString()) && password.getText().toString().length() <= 6),
                    password.getText().toString());
        }
    }
}
