package com.jim.multipos.ui.service_fee.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Window;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.ui.service_fee.adapters.PaymentTypeSpinnerAdapter;
import com.jim.multipos.ui.service_fee.adapters.PaymentTypeSpinnerAdapterOld;

import java.util.List;

/**
 * Created by user on 29.08.17.
 */

public class AutoApplyConfigure extends Dialog {
    public interface OnClickDialog {
        void closeAutoApplyDialog();
        void saveAutoApplyDialog(int position);
    }

    private MPosSpinner spParent;
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

        spParent = (MPosSpinner) findViewById(R.id.spParent);
        btnBack = (MpButton) findViewById(R.id.btnBack);
        btnSave = (MpButton) findViewById(R.id.btnSave);

        //PaymentTypeSpinnerAdapter adapter = new PaymentTypeSpinnerAdapter(getContext(), R.layout.item_spinner, paymentTypes);
        PaymentTypeSpinnerAdapter adapter = new PaymentTypeSpinnerAdapter(paymentTypes);
        spParent.setAdapter(adapter);

        btnSave.setOnClickListener(v -> {
            onClickCallback.saveAutoApplyDialog(spParent.getSelectedPosition());
            dismiss();
        });

        btnBack.setOnClickListener(v -> {
           onClickCallback.closeAutoApplyDialog();
           dismiss();
        });
    }
}
