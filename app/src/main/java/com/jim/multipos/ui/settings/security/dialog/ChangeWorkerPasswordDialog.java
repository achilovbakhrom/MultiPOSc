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

    private OnAccessChangePasswordListner listener;
    private PreferencesHelper preferencesHelper;

    public ChangeWorkerPasswordDialog(@NonNull Context context, OnAccessChangePasswordListner listener, PreferencesHelper preferencesHelper) {
        super(context);
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
                etOldPassword.setError("Please enter old password !");
                return;
            }else etOldPassword.setError(null);
            if(passwordForCheck.length()!=6){
                etOldPassword.setError("Old password is 6 digits");
                return;
            }else etOldPassword.setError(null);

            if(!preferencesHelper.getPosDetailPassword().equals(SecurityTools.md5(passwordForCheck))){
                etOldPassword.setError("Incorrect password");
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
                etNewPassword.setError("Please enter new password for order !");
                return;
            }else etNewPassword.setError(null);

            if(newPassword.length()!=6){
                etNewPassword.setError("New password should be 6 digits");
                return;
            }else etNewPassword.setError(null);


            String newPasswordConfirm = etConfirmPassword.getText().toString();
            if(newPasswordConfirm.isEmpty()){
                etConfirmPassword.setError("Please confirm new password for order !");
                return;
            }else etConfirmPassword.setError(null);

            if(!newPassword.equals(newPasswordConfirm)){
                etConfirmPassword.setError("Not same with new password");
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
