package com.jim.multipos.ui.cash_management.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 13.02.2018.
 */

public class ExtraOptionsDialog extends Dialog {

    @BindView(R.id.tvReturn)
    TextView tvReturn;
    @BindView(R.id.tvCloseOrder)
    TextView tvCloseOrder;
    @BindView(R.id.tvGoToOrder)
    TextView tvGoToOrder;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    private ExtraOptionsDialog.onExtraOptionClicked onExtraOptionClicked;

    public ExtraOptionsDialog(@NonNull Context context, onExtraOptionClicked onExtraOptionClicked) {
        super(context);
        this.onExtraOptionClicked = onExtraOptionClicked;
        View dialogView = getLayoutInflater().inflate(R.layout.extra_options_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        btnBack.setOnClickListener(view -> dismiss());

        tvCloseOrder.setOnClickListener(view -> {
            onExtraOptionClicked.onClose();
            dismiss();
        });

        tvGoToOrder.setOnClickListener(view -> {
            onExtraOptionClicked.onGoToOrder();
            dismiss();
        });

        tvReturn.setOnClickListener(view -> {
        onExtraOptionClicked.onReturn();
            dismiss();
        });
    }

    public interface onExtraOptionClicked{
        void onReturn();
        void onClose();
        void onGoToOrder();
    }
}
