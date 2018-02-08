package com.jim.multipos.ui.cash_management.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.customer.CustomerPayment;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillOperation;
import com.jim.multipos.utils.UIUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 06.02.2018.
 */

public class CashOperationDialog extends Dialog {

    @BindView(R.id.tvOperationTitle)
    TextView tvOperationTitle;
    @BindView(R.id.tvStartingCash)
    TextView tvStartingCash;
    @BindView(R.id.tvPayOuts)
    TextView tvPayOuts;
    @BindView(R.id.tvPayIns)
    TextView tvPayIns;
    @BindView(R.id.tvPayToVendors)
    TextView tvPayToVendors;
    @BindView(R.id.tvIncomeDebt)
    TextView tvIncomeDebt;
    @BindView(R.id.tvBankDrops)
    TextView tvBankDrops;
    @BindView(R.id.tvCashTransactions)
    TextView tvCashTransactions;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.tvExpectedCash)
    TextView tvExpectedCash;
    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnConfirm)
    MpButton btnConfirm;


    public CashOperationDialog(Context context, DatabaseManager databaseManager, Till till, PaymentType paymentType, int type, double amount, onCashOperationCallback callback) {
        super(context);
        View dialogView = getLayoutInflater().inflate(R.layout.cash_operations_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        DecimalFormat formatter;
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        numberFormat.setMaximumFractionDigits(2);
        formatter = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);

        double payIn = 0, payOut = 0, bankDrop = 0, payToVendor = 0, incomeDebt = 0, cashTransactions = 0, tips = 0;
        Account account = paymentType.getAccount();
        List<TillOperation> allOperations = databaseManager.getTillOperationsByAccountId(account.getId(), till.getId()).blockingGet();
        if (!allOperations.isEmpty()) {
            for (int i = 0; i < allOperations.size(); i++) {
                if (allOperations.get(i).getType() == TillOperation.PAY_IN)
                    payIn += allOperations.get(i).getAmount();
                if (allOperations.get(i).getType() == TillOperation.PAY_OUT)
                    payOut += allOperations.get(i).getAmount();
                if (allOperations.get(i).getType() == TillOperation.BANK_DROP)
                    bankDrop += allOperations.get(i).getAmount();
            }
        }
        Calendar fromDate = new GregorianCalendar(), toDate = new GregorianCalendar();
        fromDate.setTimeInMillis(till.getOpenDate());
        toDate.setTimeInMillis(System.currentTimeMillis());
        List<BillingOperations> billingOperationsList = databaseManager.getBillingOperationsByInterval(fromDate, toDate).blockingGet();
        for (int i = 0; i < billingOperationsList.size(); i++) {
            if (billingOperationsList.get(i).getAccount() != null && billingOperationsList.get(i).getAccount().getId().equals(account.getId()))
                payToVendor += billingOperationsList.get(i).getAmount();
        }

        List<CustomerPayment> customerPayments = databaseManager.getCustomerPaymentsByInterval(fromDate, toDate).blockingGet();
        for (int i = 0; i < customerPayments.size(); i++) {
            if (customerPayments.get(i).getPaymentType().getAccount().getId().equals(account.getId()))
                incomeDebt += customerPayments.get(i).getPaymentAmount();
        }

        List<Order> ordersList = databaseManager.getAllTillOrders().blockingGet();
        for (int i = 0; i < ordersList.size(); i++) {
            Order order = ordersList.get(i);
            for (int j = 0; j < order.getPayedPartitions().size(); j++) {
                PayedPartitions payedPartitions = order.getPayedPartitions().get(j);
                if (payedPartitions.getPaymentType().getAccount().getId().equals(account.getId())) {
                    cashTransactions += payedPartitions.getValue();
                }
            }
            double change = order.getChange();
            if (change > 0) {
                if (account.getCirculation() == 0)
                    cashTransactions -= change;
            }
            tips += order.getTips();
        }
        switch (type) {
            case TillOperation.PAY_IN:
                tvOperationTitle.setText("Pay in");
                tvPayIns.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                payIn += amount;
                break;
            case TillOperation.BANK_DROP:
                tvOperationTitle.setText("Bank drop");
                tvBankDrops.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                bankDrop += amount;
                break;
            case TillOperation.PAY_OUT:
                tvOperationTitle.setText("Pay out");
                tvPayOuts.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                payOut += amount;
                break;
        }

        double expectedCash = payIn - payOut - payToVendor + incomeDebt - bankDrop + cashTransactions;

        if (expectedCash >= 0)
            tvExpectedCash.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        else tvExpectedCash.setTextColor(ContextCompat.getColor(context, R.color.colorRed));

        tvPayOuts.setText(formatter.format(payOut));
        tvPayIns.setText(formatter.format(payIn));
        tvBankDrops.setText(formatter.format(bankDrop));
        tvCashTransactions.setText(formatter.format(cashTransactions));
        tvTips.setText(formatter.format(tips));
        tvIncomeDebt.setText(formatter.format(incomeDebt));
        tvPayToVendors.setText(formatter.format(payToVendor));
        tvExpectedCash.setText(formatter.format(expectedCash));

        btnCancel.setOnClickListener(view -> {
            UIUtils.closeKeyboard(btnCancel, context);
            dismiss();
        });

        btnConfirm.setOnClickListener(view -> {
            if (etDescription.getText().toString().isEmpty())
                etDescription.setError(context.getString(R.string.enter_description));
            else {
                callback.onOperationConfirm(amount, type, etDescription.getText().toString());
                UIUtils.closeKeyboard(btnConfirm, context);
                dismiss();
            }
        });

    }

    public interface onCashOperationCallback {
        void onOperationConfirm(double amount, int operationType, String description);
    }

}
