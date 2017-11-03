package com.jim.multipos.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;

/**
 * Created by DEV on 01.08.2017.
 */

public class WarningDialog extends Dialog {
    private MpButton btnWarningYES, btnWarningNO;
    private TextView tvWarningText;

    public WarningDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public WarningDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected WarningDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.warning_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        btnWarningYES = (MpButton) findViewById(R.id.btnWarningYES);
        btnWarningNO = (MpButton) findViewById(R.id.btnWarningNO);
        tvWarningText = (TextView) findViewById(R.id.tvWarningText);
        onlyText(false);
    }

    public void setWarningText(String warningText) {
        tvWarningText.setText(warningText);
    }

    public void setOnYesClickListener(View.OnClickListener listener) {
        btnWarningYES.setOnClickListener(listener);
    }

    public void setOnNoClickListener(View.OnClickListener listener) {
        btnWarningNO.setOnClickListener(listener);
    }

    public void setYesButtonText(String yesButtonText) {
        btnWarningYES.setText(yesButtonText);
    }

    public void setNoButtonText(String noButtonText) {
        btnWarningNO.setText(noButtonText);
    }

    public void onlyText(boolean state) {
        if (state) {
            btnWarningNO.setVisibility(View.GONE);
            btnWarningYES.setText(getContext().getResources().getString(R.string.ok));
        } else {
            btnWarningNO.setVisibility(View.VISIBLE);
            btnWarningYES.setText(getContext().getResources().getString(R.string.yes));
        }
    }
}
