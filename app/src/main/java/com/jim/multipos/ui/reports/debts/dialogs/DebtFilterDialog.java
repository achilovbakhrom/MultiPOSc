package com.jim.multipos.ui.reports.debts.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 29.03.2018.
 */

public class DebtFilterDialog extends Dialog {

    @BindView(R.id.chbStatic)
    MpCheckbox chbStatic;
    @BindView(R.id.chbManual)
    MpCheckbox chbManual;

    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnApply)
    MpButton btnApply;
    private DebtFilterListener listener;

    public interface DebtFilterListener {
        void onApply(int[] config);
    }

    public DebtFilterDialog(@NonNull Context context, int[] filterConfig, DebtFilterListener listener) {
        super(context);
        this.listener = listener;
        View dialogView = getLayoutInflater().inflate(R.layout.debt_filter_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.y = CommonUtils.dpToPx(190);
        wlp.x = CommonUtils.dpToPx(10);
        wlp.width = CommonUtils.dpToPx(300);
        wlp.height = CommonUtils.dpToPx(240);
        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        window.setAttributes(wlp);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);

        if (filterConfig[0] == 1) chbStatic.setChecked(true);
        else chbStatic.setChecked(false);
        if (filterConfig[1] == 1) chbManual.setChecked(true);
        else chbManual.setChecked(false);


        chbStatic.setTextColor(Color.parseColor("#212121"));
        chbManual.setTextColor(Color.parseColor("#212121"));

        btnApply.setOnClickListener((view -> {
            listener.onApply(new int[]{chbStatic.isChecked() ? 1 : 0, chbManual.isChecked() ? 1 : 0});
            dismiss();
        }));
        btnCancel.setOnClickListener((view -> {
            dismiss();
        }));
    }
}
