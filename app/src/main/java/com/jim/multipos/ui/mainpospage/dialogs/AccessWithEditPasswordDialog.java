package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Window;
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

public class AccessWithEditPasswordDialog extends Dialog {

    public interface OnAccsessListner {
        void accsessSuccess();
        void onBruteForce();
    }
    int countError = 0;
    @BindView(R.id.etPasswordEdit)
    EditText etPasswordEdit;
    @BindView(R.id.btnOK)
    MpButton btnOK;
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.tvDialogTitle)
    TextView tvDialogTitle;

    private Context context;
    private OnAccsessListner listener;
    private PreferencesHelper preferencesHelper;
    private boolean isCancel;

    public AccessWithEditPasswordDialog(@NonNull Context context, OnAccsessListner listener, PreferencesHelper preferencesHelper) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(R.color.colorTransparent);
        setContentView(R.layout.accsess_edit_password_dialog);

        ButterKnife.bind(this);
        btnCancel.setOnClickListener(view -> dismiss());
        btnOK.setOnClickListener(view -> {
            String passwordForCheck = etPasswordEdit.getText().toString();
            if(passwordForCheck.isEmpty()) {
                etPasswordEdit.setError(context.getString(R.string.password_cant_be_empty));
                return;
            }


            if(preferencesHelper.checkEditOrderPassword(SecurityTools.md5(passwordForCheck))){
                new Handler().postDelayed(() -> {
                    listener.accsessSuccess();
                    dismiss();
                },300);
                UIUtils.closeKeyboard(etPasswordEdit,getContext());

            }else {
                etPasswordEdit.setText("");
                etPasswordEdit.setError(context.getString(R.string.password_is_incorrect));
                countError ++;
                if(countError == 5) {
                    new Handler().postDelayed(() -> {
                        listener.onBruteForce();
                        dismiss();
                    },400);
                    UIUtils.closeKeyboard(etPasswordEdit,getContext());
                }
            }

        });


    }
}
