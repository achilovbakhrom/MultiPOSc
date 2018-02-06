package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.ui.mainpospage.adapter.PaymentDetailsAdapter;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Portable-Acer on 28.11.2017.
 */

public class PaymentDetialDialog extends Dialog {


    private List<PayedPartitions> payedPartitions;
    private DecimalFormat decimalFormat;
    private Currency mainCurrency;

    public PaymentDetialDialog(@NonNull Context context, List<PayedPartitions> payedPartitions, DecimalFormat decimalFormat, Currency mainCurrency) {
        super(context);
        this.payedPartitions = payedPartitions;
        this.decimalFormat = decimalFormat;
        this.mainCurrency = mainCurrency;
    }

    @BindView(R.id.rvPaymentDetailList)
    RecyclerView rvPaymentDetailList;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_detial_dialog);
        getWindow().getDecorView().setBackgroundResource(R.color.colorTransparent);
        ButterKnife.bind(this);
        btnBack.setOnClickListener(view -> {
            dismiss();
        });
        PaymentDetailsAdapter adapter = new PaymentDetailsAdapter(payedPartitions,decimalFormat,mainCurrency);
        rvPaymentDetailList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPaymentDetailList.setAdapter(adapter);
    }
}
