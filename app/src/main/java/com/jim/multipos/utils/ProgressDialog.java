package com.jim.multipos.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.jim.multipos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressDialog extends Dialog {

    @BindView(R.id.loader)
    ProgressBar progressBar;

    public ProgressDialog(@NonNull Context context) {
        super(context);
        View dialogView = getLayoutInflater().inflate(R.layout.progress_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        ButterKnife.bind(this, dialogView);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void setMax(int max){
        progressBar.setMax(max);
    }

    public void setProgress(int progress){
        progressBar.setProgress(progress);
    }

    public void checkProgress(){
        if (progressBar.getProgress() == progressBar.getMax()){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            dismiss();
        }
    }
}
