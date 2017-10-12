package com.jim.multipos.ui.first_configure.fragments;

import android.os.Bundle;
import android.widget.EditText;
import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.first_configure.validators.PasswordValidator;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.FirstConfigureActivityEvent;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import butterknife.BindView;
import eu.inmite.android.lib.validations.form.annotations.Joined;
import eu.inmite.android.lib.validations.form.annotations.MaxLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

import static com.jim.multipos.ui.first_configure.Constants.ADDRESS_DATA;
import static com.jim.multipos.ui.first_configure.Constants.ADDRESS_ERROR;
import static com.jim.multipos.ui.first_configure.Constants.ALIAS_DATA;
import static com.jim.multipos.ui.first_configure.Constants.ALIAS_ERROR;
import static com.jim.multipos.ui.first_configure.Constants.BUTTON_STATE;
import static com.jim.multipos.ui.first_configure.Constants.PASSWORD_DATA;
import static com.jim.multipos.ui.first_configure.Constants.PASSWORD_ERROR;
import static com.jim.multipos.ui.first_configure.Constants.PASSWORD_REPEAT_DATA;
import static com.jim.multipos.ui.first_configure.Constants.PASSWORD_REPEAT_ERROR;
import static com.jim.multipos.ui.first_configure.Constants.POS_ID_DATA;
import static com.jim.multipos.ui.first_configure.Constants.POS_ID_ERROR;
import static com.jim.multipos.ui.first_configure.connector.FirstConfigureActivityConnector.OPEN_NEXT_FROM_POS_DETAILS;

/**
 * Created by user on 07.10.17.
 */

public class PosDetailsFragment extends BaseFragment {
    @Inject
    RxBusLocal rxBusLocal;
    @NotEmpty(messageId = R.string.enter_pos_id)
    @BindView(R.id.etPosId)
    EditText etPosId;
    @NotEmpty(messageId = R.string.enter_alias)
    @BindView(R.id.etAlias)
    EditText etAlias;
    @NotEmpty(messageId = R.string.enter_address)
    @BindView(R.id.etAddress)
    EditText etAddress;
    @MaxLength(value = 6, messageId = R.string.password_length_not_less_than_6)
    @Joined(value = {R.id.etPassword, R.id.etRepeatPassword}, validator = PasswordValidator.class, messageId = R.string.passwords_different)
    @BindView(R.id.etPassword)
    EditText etPassword;
    @MaxLength(value = 6, messageId = R.string.password_length_not_less_than_6)
    @Joined(value = {R.id.etPassword, R.id.etRepeatPassword}, validator = PasswordValidator.class, messageId = R.string.passwords_different)
    @BindView(R.id.etRepeatPassword)
    EditText etRepeatPassword;
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @BindView(R.id.btnRevert)
    MpButton btnRevert;

    public static PosDetailsFragment newInstance(boolean isNextButton) {
        Bundle args = new Bundle();
        args.putBoolean(BUTTON_STATE, isNextButton);

        PosDetailsFragment fragment = new PosDetailsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.pos_details_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        boolean isNextButton = false;
        String btnText = "";

        if (bundle != null) {
            btnText = getButtonText(isNextButton);
        } else {
            btnText = getButtonText(true);
        }

        btnNext.setText(btnText);

        RxView.clicks(btnNext).subscribe(aVoid -> {
            if (isValid()) {
                rxBusLocal.send(new FirstConfigureActivityEvent(OPEN_NEXT_FROM_POS_DETAILS));
            }
        });

        RxView.clicks(btnRevert).subscribe(aVoid -> {
            //TODO MAYBE FINISH PROGRAM
        });
    }

    @Override
    protected void rxConnections() {

    }

    public HashMap<String, String> getData() {
        String posID = etPosId.getText().toString();
        String alias = etAlias.getText().toString();
        String address = etAddress.getText().toString();
        String password = etPassword.getText().toString();
        String passwordRepeat = etRepeatPassword.getText().toString();
        HashMap<String, String> data = new HashMap<>();

        data.put(POS_ID_DATA, posID);
        data.put(ALIAS_DATA, alias);
        data.put(ADDRESS_DATA, address);
        data.put(PASSWORD_DATA, password);
        data.put(PASSWORD_REPEAT_DATA, passwordRepeat);

        return data;
    }

    public void showErrors(Map<String, String> errors) {
        if (!errors.isEmpty()) {
            String posIdError = errors.get(POS_ID_ERROR);
            String aliasError = errors.get(ALIAS_ERROR);
            String addressError = errors.get(ADDRESS_ERROR);
            String passwordError = errors.get(PASSWORD_ERROR);
            String passwordRepeatError = errors.get(PASSWORD_REPEAT_ERROR);

            if (posIdError != null && !posIdError.isEmpty()) {
                showPosIdError(posIdError);
            }

            if (aliasError != null && !aliasError.isEmpty()) {
                showAliasError(aliasError);
            }

            if (addressError != null && !addressError.isEmpty()) {
                showAddressError(addressError);
            }

            if (passwordError != null && !passwordError.isEmpty()) {
                showPasswordError(passwordError);
            }

            if (passwordRepeatError != null && !passwordRepeatError.isEmpty()) {
                showPasswordRepeatError(passwordRepeatError);
            }
        }
    }

    public void showPosIdError(String error) {
        etPosId.setError(error);
    }

    public void showAliasError(String error) {
        etAlias.setError(error);
    }

    public void showAddressError(String error) {
        etAddress.setError(error);
    }

    public void showPasswordError(String error) {
        etPassword.setError(error);
    }

    public void showPasswordRepeatError(String error) {
        etRepeatPassword.setError(error);
    }

    private String getButtonText(boolean isNextButton) {
        String txt;

        if (isNextButton) {
            txt = getString(R.string.next);
        } else {
            txt = getString(R.string.save);
        }

        return txt;
    }
}
