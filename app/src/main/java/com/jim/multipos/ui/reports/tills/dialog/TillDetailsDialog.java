package com.jim.multipos.ui.reports.tills.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;

import java.text.DecimalFormat;

import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 30.03.2018.
 */

public class TillDetailsDialog extends Dialog {


    public TillDetailsDialog(Context context, DatabaseManager databaseManager, DecimalFormat decimalFormat) {
        super(context);
        View dialogView = getLayoutInflater().inflate(R.layout.till_details_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
    }
}
