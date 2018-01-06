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
import com.jim.multipos.data.db.model.customer.Customer;
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

    public PayToDebtDialog(Context context, Debt debt, DatabaseManager databaseManager, boolean payToAll, UpdateCustomerDebtsListCallback callback) {
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

        if (payToAll){
            double feeAmount = debt.getFee() * debt.getDebtAmount() / 100;
            double dueSum = debt.getDebtAmount() + feeAmount;
            if (debt.getCustomerPayments().size() > 0) {
                for (CustomerPayment payment : debt.getCustomerPayments()) {
                    dueSum -= payment.getPaymentAmount();
                }
            }
            etAmount.setText(String.valueOf(dueSum));
        }


        btnBack.setOnClickListener(view -> {
            UIUtils.closeKeyboard(btnBack, context);
            dismiss();
        });
        double feeAmount = debt.getFee() * debt.getDebtAmount() / 100;
        double total = debt.getDebtAmount() + feeAmount;
        
        btnPay.setOnClickListener(view -> {
            if (!etAmount.getText().toString().isEmpty()) {
                double amount = Double.parseDouble(etAmount.getText().toString());
                if (total < amount)
                    etAmount.setError("Payment amount cannot be bigger than debt amount");
                else if (total != amount && debt.getDebtType() == Debt.ALL) {
                    etAmount.setError("You cannot pay in participle for this debt");
                } else {
                    double dueSum = total;
                    if (debt.getCustomerPayments().size() > 0) {
                        for (CustomerPayment payment : debt.getCustomerPayments()) {
                            dueSum -= payment.getPaymentAmount();
                        }
                    }
                    CustomerPayment payment = new CustomerPayment();
                    payment.setDebt(debt);
                    payment.setPaymentDate(System.currentTimeMillis());
                    payment.setPaymentType(paymentTypes.get(spPaymentType.getSelectedPosition()));
                    payment.setPaymentAmount(amount);
                    payment.setDebtDue(dueSum - amount);
                    if (dueSum - amount == 0) {
                        debt.setStatus(Debt.CLOSED);
                        databaseManager.addDebt(debt).blockingGet();
                    }
                    databaseManager.addCustomerPayment(payment).subscribe();
                    debt.resetCustomerPayments();
                    callback.onPay(debt.getCustomer());
                    UIUtils.closeKeyboard(btnPay, context);
                    dismiss();
                }
            } else {
                etAmount.setError(context.getString(R.string.enter_amount));
            }
        });

    }

    public interface UpdateCustomerDebtsListCallback {
        void onPay(Customer customer);
    }
}
