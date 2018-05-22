package com.jim.multipos.ui.settings.security.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.utils.SecurityTools;
import com.jim.multipos.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Portable-Acer on 28.11.2017.
 */

public class ChangeWorkerPasswordDialog extends Dialog {

    public interface OnAccessChangePasswordListner {
        void accsessSuccess();
        void onBruteForce();
    }
    int countError = 0;
    @BindView(R.id.etOldPassword)
    EditText etOldPassword;
    @BindView(R.id.etNewPassword)
    EditText etNewPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @BindView(R.id.btnOK)
    MpButton btnOK;
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.tvDialogTitle)
    TextView tvDialogTitle;

    private Context context;
    private OnAccessChangePasswordListner listener;
    private PreferencesHelper preferencesHelper;

    public ChangeWorkerPasswordDialog(@NonNull Context context, OnAccessChangePasswordListner listener, PreferencesHelper preferencesHelper) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_worker_password_dialog);

        getWindow().getDecorView().setBackgroundResource(R.color.colorTransparent);

        ButterKnife.bind(this);

        btnCancel.setOnClickListener(view -> dismiss());
        btnOK.setOnClickListener(view -> {
            String passwordForCheck = etOldPassword.getText().toString();
            if(passwordForCheck.isEmpty()){
                etOldPassword.setError(context.getString(R.string.please_enter_old_password));
                return;
            }else etOldPassword.setError(null);
            if(passwordForCheck.length()!=6){
                etOldPassword.setError(context.getString(R.string.old_password_is_6_digits));
                return;
            }else etOldPassword.setError(null);

            if(!preferencesHelper.getPosDetailPassword().equals(SecurityTools.md5(passwordForCheck))){
                etOldPassword.setError(context.getString(R.string.incorrect_password));
                countError ++;
                if(countError == 5) {
                    new Handler().postDelayed(() -> {
                        listener.onBruteForce();
                        dismiss();
                    },400);
                    UIUtils.closeKeyboard(etOldPassword,getContext());
                }
                return;
            }else etOldPassword.setError(null);


            String newPassword = etNewPassword.getText().toString();
            if(newPassword.isEmpty()){
                etNewPassword.setError(context.getString(R.string.please_enter_password_for_order));
                return;
            }else etNewPassword.setError(null);

            if(newPassword.length()!=6){
                etNewPassword.setError(context.getString(R.string.new_password_should_be_longer_than_6));
                return;
            }else etNewPassword.setError(null);


            String newPasswordConfirm = etConfirmPassword.getText().toString();
            if(newPasswordConfirm.isEmpty()){
                etConfirmPassword.setError(context.getString(R.string.please_confirm_new_password_for_order));
                return;
            }else etConfirmPassword.setError(null);

            if(!newPassword.equals(newPasswordConfirm)){
                etConfirmPassword.setError(context.getString(R.string.not_same_with_new_password));
                return;
            }

            new Handler().postDelayed(() -> {
                preferencesHelper.setPosDetailPassword(SecurityTools.md5(newPassword));
                listener.accsessSuccess();
                dismiss();
            },300);
            UIUtils.closeKeyboard(etConfirmPassword,getContext());


        });


    }
}
