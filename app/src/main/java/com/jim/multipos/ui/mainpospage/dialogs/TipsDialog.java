package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Portable-Acer on 28.11.2017.
 */

public class TipsDialog extends Dialog {
    public interface OnClickListener {
        void onOkClick(double value);
    }

    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnOK)
    MpButton btnOK;
    @BindView(R.id.etTipAmount)
    MpEditText etTipAmount;
    private OnClickListener listener;
    private double change;

    public TipsDialog(@NonNull Context context, OnClickListener listener,double change) {
        super(context);
        this.listener = listener;
        this.change = change;
    }
    double value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tips_dialog);

        getWindow().getDecorView().setBackgroundResource(R.color.colorTransparent);

        ButterKnife.bind(this);
        etTipAmount.setText(Double.toString(change));
        RxView.clicks(btnCancel).subscribe(o -> dismiss());
        RxView.clicks(btnOK).subscribe(o -> {
            if(etTipAmount.getText().toString().isEmpty()){
                etTipAmount.setError(getContext().getString(R.string.ammount_cant_be_zero));
                return;
            }
            try {
                value= Double.parseDouble(etTipAmount.getText().toString());
            }catch (Exception e){
                etTipAmount.setError(getContext().getString(R.string.invalid));
                return;
            }
            if(value == 0){
                etTipAmount.setError(getContext().getString(R.string.ammount_cant_be_zero));
                return;
            }

            UIUtils.closeKeyboard(etTipAmount, getContext());

            new Handler().postDelayed(() -> {
                listener.onOkClick(value);
                dismiss();
            },200);


        });
    }
}
