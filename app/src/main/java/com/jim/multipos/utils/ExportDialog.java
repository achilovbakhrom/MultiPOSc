package com.jim.multipos.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jim.multipos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 29.03.2018.
 */

public class ExportDialog extends Dialog {

    @BindView(R.id.btnToExcel)
    TextView btnToExcel;
    @BindView(R.id.btnToPdf)
    TextView btnToPdf;
    @BindView(R.id.btnCancel)
    TextView btnCancel;
    private OnExportItemClick callback;

    public ExportDialog(@NonNull Context context, OnExportItemClick onExportItemClick) {
        super(context);
        this.callback = onExportItemClick;
        View dialogView = getLayoutInflater().inflate(R.layout.export_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.y = CommonUtils.dpToPx(190);
        wlp.x = CommonUtils.dpToPx(950);
        wlp.width = CommonUtils.dpToPx(170);
        wlp.height = CommonUtils.dpToPx(185);
        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        window.setAttributes(wlp);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);
        btnToExcel.setOnClickListener(view -> {
            callback.onToExcel();
            dismiss();
        });
        btnToExcel.setOnClickListener(view -> {
            callback.onToPdf();
            dismiss();
        });
        btnCancel.setOnClickListener(view -> dismiss());
    }

    public interface OnExportItemClick {
        void onToExcel();
        void onToPdf();
    }
}
