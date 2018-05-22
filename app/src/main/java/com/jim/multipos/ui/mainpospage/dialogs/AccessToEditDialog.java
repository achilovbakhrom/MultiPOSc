package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.utils.SecurityTools;
import com.jim.multipos.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Portable-Acer on 28.11.2017.
 */

public class AccessToEditDialog extends Dialog {

    public interface OnAccsessListner {
        void accsessSuccess(String reason);
        void onBruteForce();
    }
    int countError = 0;
    @BindView(R.id.etPasswordEdit)
    EditText etPasswordEdit;
    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.btnOK)
    MpButton btnOK;
    @BindView(R.id.btnCancel)
    MpButton btnCancel;

    private Context context;
    private OnAccsessListner listener;
    private PreferencesHelper preferencesHelper;

    public AccessToEditDialog(@NonNull Context context, OnAccsessListner listener, PreferencesHelper preferencesHelper) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order_edit_secure_dialog);
        getWindow().getDecorView().setBackgroundResource(R.color.colorTransparent);
        ButterKnife.bind(this);

        btnCancel.setOnClickListener(view -> dismiss());
        btnOK.setOnClickListener(view -> {
            String passwordForCheck = etPasswordEdit.getText().toString();
            if(passwordForCheck.isEmpty()){
                etPasswordEdit.setError(context.getString(R.string.password_cant_be_empty));
                return;
            }

            String reason = etDescription.getText().toString();
            if(reason.isEmpty()){
                etDescription.setError(context.getString(R.string.please_enter_reason_for_edit));
                return;
            }

            if(preferencesHelper.checkEditOrderPassword(SecurityTools.md5(passwordForCheck))){
                new android.os.Handler().postDelayed(() -> {
                    listener.accsessSuccess(reason);
                    dismiss();
                },300);
                UIUtils.closeKeyboard(etPasswordEdit,getContext());

            }else {
                etPasswordEdit.setText("");
                etPasswordEdit.setError(context.getString(R.string.password_is_incorrect));
                countError ++;
                if(countError == 5) {
                    new android.os.Handler().postDelayed(() -> {
                        listener.onBruteForce();
                        dismiss();
                    },400);
                    UIUtils.closeKeyboard(etPasswordEdit,getContext());
                }
            }

        });


    }
}
