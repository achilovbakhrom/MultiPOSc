package com.jim.multipos.ui.customer_debt.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.customer.CustomerPayment;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developer on 04.12.2017.
 */

public class PayToDebtDialog extends Dialog {

    @BindView(R.id.spPaymentType)
    MPosSpinner spPaymentType;
    @BindView(R.id.etAmount)
    MpEditText etAmount;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.btnPay)
    MpButton btnPay;
    @BindView(R.id.tvCurrencyAbbr)
    TextView tvCurrencyAbbr;

    public PayToDebtDialog(Context context, Debt debt, DatabaseManager databaseManager) {
        super(context);

        View dialogView = getLayoutInflater().inflate(R.layout.payment_to_debt, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        tvCurrencyAbbr.setText(databaseManager.getMainCurrency().getAbbr());
        List<PaymentType> paymentTypes = databaseManager.getPaymentTypes();
        List<String> paymentTypeNames = new ArrayList<>();
        for (PaymentType paymentType : paymentTypes) {
            paymentTypeNames.add(paymentType.getName());
        }
        spPaymentType.setAdapter(paymentTypeNames);

        btnBack.setOnClickListener(view -> {
            UIUtils.closeKeyboard(btnBack, context);
            dismiss();
        });

        btnPay.setOnClickListener(view -> {
            if (!etAmount.getText().toString().isEmpty()) {
                double amount = Double.parseDouble(etAmount.getText().toString());
                double dueAmount = 0;
                if (debt.getDebtAmount() < amount)
                    etAmount.setError("Payment amount cannot be bigger than debt amount");
                else if (debt.getDebtAmount() == amount && debt.getDebtType() == Debt.ALL) {
                    etAmount.setError("You should all debt at once, you cannot pay in participle");
                } else {
                    CustomerPayment payment = new CustomerPayment();
                    payment.setDebt(debt);
                    payment.setPaymentDate(System.currentTimeMillis());
                    payment.setPaymentType(paymentTypes.get(spPaymentType.getSelectedPosition()));
                    payment.setPaymentAmount(amount);

                    UIUtils.closeKeyboard(btnPay, context);
                    dismiss();
                }


            } else {
                etAmount.setError(context.getString(R.string.enter_amount));
            }
        });



    }


}
