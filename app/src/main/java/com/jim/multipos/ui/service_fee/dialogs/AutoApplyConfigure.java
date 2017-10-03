package com.jim.multipos.ui.service_fee.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Window;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.ui.service_fee.adapters.PaymentTypeSpinnerAdapter;

import java.util.List;

/**
 * Created by user on 29.08.17.
 */

public class AutoApplyConfigure extends Dialog {
    public interface OnClickDialog {
        void closeAutoApplyDialog();
        void saveAutoApplyDialog(int position);
    }

    private MpSpinner spParent;
    private MpButton btnBack;
    private MpButton btnSave;
    private List<PaymentType> paymentTypes;
    private OnClickDialog onClickCallback;

    public AutoApplyConfigure(@NonNull Context context, List<PaymentType> paymentTypes, OnClickDialog onClickCallback) {
        super(context);
        this.onClickCallback = onClickCallback;
        this.paymentTypes = paymentTypes;
        init();
    }

    public void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.auto_apply_dialog_fragment);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        spParent = (MpSpinner) findViewById(R.id.spParent);
        btnBack = (MpButton) findViewById(R.id.btnBack);
        btnSave = (MpButton) findViewById(R.id.btnSave);

        PaymentTypeSpinnerAdapter adapter = new PaymentTypeSpinnerAdapter(getContext(), R.layout.item_spinner, paymentTypes);
        spParent.setAdapter(adapter);

        btnSave.setOnClickListener(v -> {
            int position = spParent.selectedItem();

            onClickCallback.saveAutoApplyDialog(position);
            dismiss();
        });

        btnBack.setOnClickListener(v -> {
           onClickCallback.closeAutoApplyDialog();
           dismiss();
        });
    }
}
