package com.jim.mpviews.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.jim.mpviews.R;


/**
 * A simple dialog containing month and year picker and also provides callback on positive
 * selection.
 */
public class SimpleTimePickerDialog extends Dialog implements DialogInterface
        .OnClickListener {

    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private View dialogView;
    private TimePicker picker;
    private SimpleDatePickerDelegate mSimpleDatePickerDelegate;
    /**
     * @param context The context the dialog is to run in.
     */
    public SimpleTimePickerDialog(Context context, int hour,
                                  int minute) {
        this(context, 0, hour, minute);
    }

    /**
     * @param context The context the dialog is to run in.
     * @param theme   the theme to apply to this dialog
     */
    @SuppressLint("InflateParams")
    public SimpleTimePickerDialog(Context context, int theme, int hour,
                                  int minute) {
        super(context, theme);

        dialogView = getLayoutInflater().inflate(R.layout.time_picker, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        picker = (TimePicker) dialogView.findViewById(R.id.tpHourTime);
        picker.setIs24HourView(true);

        findViewById(R.id.cancel_btn).setOnClickListener(view -> {
            cancel();
        });
        findViewById(R.id.btnOk).setOnClickListener(view -> {

        });
        findViewById(R.id.btnBack).setOnClickListener(view -> {

        });

    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:

                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }




}
