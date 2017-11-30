package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.utils.Utils;
import com.jim.multipos.R;
import com.jim.multipos.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * Created by Portable-Acer on 28.11.2017.
 */

public class SetQuantityDialog extends Dialog {
    public interface OnClickListener {
        void ok(int value);
    }

    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnOK)
    MpButton btnOK;
    @BindView(R.id.etQuantity)
    MpEditText etQuantity;
    private OnClickListener listener;

    public SetQuantityDialog(@NonNull Context context, OnClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_quantity_dialog);

        getWindow().getDecorView().setBackgroundResource(R.color.colorTransparent);

        ButterKnife.bind(this);

        RxView.clicks(btnCancel).subscribe(o -> dismiss());
        RxView.clicks(btnOK).subscribe(o -> {
            UIUtils.closeKeyboard(etQuantity, getContext());
            listener.ok(Integer.parseInt(etQuantity.getText().toString()));
            dismiss();
        });
    }
}
