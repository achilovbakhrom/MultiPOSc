package com.jim.multipos.ui.lock_screen.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlugRefreshDialog extends Dialog {
    @BindView(R.id.btnOK)
    MpButton btnOK;
    public PlugRefreshDialog(@NonNull Context context) {
        super(context);
        View dialogView = getLayoutInflater().inflate(R.layout.plugin_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        btnOK.setOnClickListener(view -> {
            dismiss();
        });
    }
}
