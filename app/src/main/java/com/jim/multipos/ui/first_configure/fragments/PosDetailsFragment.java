package com.jim.multipos.ui.first_configure.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCompletedStateView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.utils.RxBusLocal;

import javax.inject.Inject;

import butterknife.BindView;
import eu.inmite.android.lib.validations.form.annotations.MaxLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

import static com.jim.multipos.ui.first_configure.Constants.POS_DETAIL_FRAGMENT_ID;

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
    @BindView(R.id.etPassword)
    EditText etPassword;
    @MaxLength(value = 6, messageId = R.string.password_length_not_less_than_6)
    @BindView(R.id.etRepeatPassword)
    EditText etRepeatPassword;
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @BindView(R.id.btnRevert)
    MpButton btnRevert;

    @Override
    protected int getLayout() {
        return R.layout.pos_details_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (((FirstConfigureActivity) getActivity()).getPresenter().isNextButton()) {
            btnNext.setText(R.string.next);
        } else {
            btnNext.setText(R.string.save);
        }

        ((FirstConfigureActivity) getActivity()).getPresenter().fillPosDetailsData(etPosId, etAlias, etAddress, etPassword, etRepeatPassword);

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etPassword.setError(null);
                if (s.length() != 0 && !etRepeatPassword.getText().toString().isEmpty()) {
                    if (!isPasswordValid()) {
                        etRepeatPassword.setError(getString(R.string.passwords_different));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etRepeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etPassword.setError(null);
                if (!isPasswordValid()) {
                    etRepeatPassword.setError(getString(R.string.passwords_different));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        RxView.clicks(btnNext).subscribe(aVoid -> {
            if (isValid()) {
                ((FirstConfigureActivity) getActivity()).getPresenter().setCompletedFragments(MpCompletedStateView.COMPLETED_STATE, POS_DETAIL_FRAGMENT_ID);
                ((FirstConfigureActivity) getActivity()).getPresenter().savePosDetailsData(etPosId.getText().toString(), etAddress.getText().toString(), etAddress.getText().toString(), etPassword.getText().toString());
                ((FirstConfigureActivity) getActivity()).getPresenter().openNextFragment();
            } else {
                ((FirstConfigureActivity) getActivity()).getPresenter().setCompletedFragments(MpCompletedStateView.WARNING_STATE, POS_DETAIL_FRAGMENT_ID);
            }
        });

        RxView.clicks(btnRevert).subscribe(aVoid -> {
            ((FirstConfigureActivity) getActivity()).getPresenter().openPrevFragment(POS_DETAIL_FRAGMENT_ID);
        });
    }

    @Override
    protected void rxConnections() {

    }

    private boolean isPasswordValid() {
        return etPassword.getText().toString().equals(etRepeatPassword.getText().toString());
    }

    @Override
    public boolean isValid() {
        boolean isValid = super.isValid();

        if (!isPasswordValid()) {
            etPassword.setError(getString(R.string.passwords_different));
            etRepeatPassword.setError(getString(R.string.passwords_different));

            return false;
        }

        return isValid;
    }
}
