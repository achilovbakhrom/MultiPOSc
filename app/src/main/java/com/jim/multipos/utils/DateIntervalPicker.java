package com.jim.multipos.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.jim.multipos.R;

/**
 * Created by developer on 04.12.2017.
 */

public class DateIntervalPicker extends Dialog {
    private View dialogView;

    public DateIntervalPicker(@NonNull Context context) {
        super(context);
        dialogView = getLayoutInflater().inflate(R.layout.date_interval_picker_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

    }
}
