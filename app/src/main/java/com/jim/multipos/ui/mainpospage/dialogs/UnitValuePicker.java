package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.jim.multipos.R;

import butterknife.ButterKnife;

/**
 * Created by developer on 08.01.2018.
 */

public class UnitValuePicker extends Dialog {
    View dialogView;

    public UnitValuePicker(@NonNull Context context) {
        super(context);
        dialogView = getLayoutInflater().inflate(R.layout.discount_dialog_new, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

    }

}
