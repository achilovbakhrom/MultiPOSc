package com.jim.multipos.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jim.multipos.R;

import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 29.03.2018.
 */

public class CustomFilterDialog extends Dialog {

    public CustomFilterDialog(@NonNull Context context) {
        super(context);
        View dialogView = getLayoutInflater().inflate(R.layout.custom_filter_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.y = CommonUtils.dpToPx(190);
        wlp.x = CommonUtils.dpToPx(10);
//        wlp.width = CommonUtils.dpToPx(200);
//        wlp.height = CommonUtils.dpToPx(200);
        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        window.setAttributes(wlp);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);
    }
}
