package com.jim.multipos.ui.service_fee.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;

/**
 * Created by user on 29.08.17.
 */

public class UsageTypeDialog extends Dialog {
    public interface OnClickDialog {
        void closeUsageTypeDialog();

        void nextDialog(int position);
    }

    private RadioGroup rgConfig;
    private MpButton btnBack;
    private MpButton btnNext;
    private OnClickDialog onClickCallback;

    public UsageTypeDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.usage_type_fragment);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        onClickCallback = (OnClickDialog) context;
        rgConfig = (RadioGroup) findViewById(R.id.rgConfig);
        btnBack = (MpButton) findViewById(R.id.btnBack);
        btnNext = (MpButton) findViewById(R.id.btnNext);
        //rgConfig.getChildAt(0).setSelected(true);
        //rgConfig.getChildAt(1).setSelected(true);
        /*rgConfig.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbHandy:
                    break;
                case R.id.rbAutoApply:
                    break;
                case R.id.rbAll:
                    break;
            }
        });*/

        btnBack.setOnClickListener(v -> {
            onClickCallback.closeUsageTypeDialog();
            this.dismiss();
        });

        btnNext.setOnClickListener(v -> {
            int id = rgConfig.getCheckedRadioButtonId();
            View rb = findViewById(id);
            int position = rgConfig.indexOfChild(rb);

            onClickCallback.nextDialog(position);
            dismiss();
        });
    }
}
