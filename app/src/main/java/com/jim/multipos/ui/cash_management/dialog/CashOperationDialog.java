package com.jim.multipos.ui.cash_management.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.customer.CustomerPayment;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillManagementOperation;
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

        DecimalFormat formatter = BaseAppModule.getFormatter();


        double payIn, payOut, bankDrop, payToVendor, incomeDebt, totalStartingCash, cashTransactions = 0, tips = 0;
        Account account = paymentType.getAccount();
        totalStartingCash = databaseManager.getTotalTillManagementOperationsAmount(account.getId(), till.getId(), TillManagementOperation.OPENED_WITH).blockingGet();
        payIn = databaseManager.getTotalTillOperationsAmount(account.getId(), till.getId(), TillOperation.PAY_IN).blockingGet();
        payOut = databaseManager.getTotalTillOperationsAmount(account.getId(), till.getId(), TillOperation.PAY_OUT).blockingGet();
        bankDrop = databaseManager.getTotalTillOperationsAmount(account.getId(), till.getId(), TillOperation.BANK_DROP).blockingGet();

        Calendar fromDate = new GregorianCalendar(), toDate = new GregorianCalendar();
        if (till.getStatus() == Till.OPEN) {
            fromDate.setTimeInMillis(till.getOpenDate());
            toDate.setTimeInMillis(System.currentTimeMillis());
            payToVendor = databaseManager.getBillingOperationsAmountInInterval(account.getId(), fromDate, toDate).blockingGet();
            incomeDebt = databaseManager.getCustomerPaymentsInInterval(account.getId(), fromDate, toDate).blockingGet();
        } else {
            fromDate.setTimeInMillis(till.getOpenDate());
            toDate.setTimeInMillis(till.getCloseDate());
            payToVendor = databaseManager.getBillingOperationsAmountInInterval(account.getId(), fromDate, toDate).blockingGet();
            incomeDebt = databaseManager.getCustomerPaymentsInInterval(account.getId(), fromDate, toDate).blockingGet();
        }

        List<Order> ordersList = databaseManager.getOrdersByTillId(till.getId()).blockingGet();
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
                if (account.getStaticAccountType() == 1)
                    cashTransactions -= change;
            }
            tips += order.getTips();
        }

        tvPayOuts.setText(formatter.format(payOut));
        tvPayIns.setText(formatter.format(payIn));
        tvPayIns.setText(formatter.format(payIn));

        switch (type) {
            case TillOperation.PAY_IN:
                tvOperationTitle.setText("Pay in");
                String payInResult = formatter.format(amount) + " + " + formatter.format(payIn);
                Spannable spannable = new SpannableString(payInResult);
                spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorGreen)), 0, formatter.format(amount).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvPayIns.setText(spannable, TextView.BufferType.SPANNABLE);
                payIn += amount;
                break;
            case TillOperation.BANK_DROP:
                tvOperationTitle.setText("Bank drop");
                String bankDropResult = formatter.format(amount) + " + " + formatter.format(bankDrop);
                Spannable spannable1 = new SpannableString(bankDropResult);
                spannable1.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorGreen)), 0, formatter.format(amount).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvBankDrops.setText(spannable1, TextView.BufferType.SPANNABLE);
                bankDrop += amount;
                break;
            case TillOperation.PAY_OUT:
                tvOperationTitle.setText("Pay out");
                String result = formatter.format(amount) + " + " + formatter.format(payOut);
                Spannable spannable2 = new SpannableString(result);
                spannable2.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorGreen)), 0, formatter.format(amount).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvPayOuts.setText(spannable2, TextView.BufferType.SPANNABLE);
                payOut += amount;
                break;
        }
        double expectedCash = payIn - payOut - payToVendor + incomeDebt - bankDrop + cashTransactions + totalStartingCash;

        if (expectedCash >= 0)
            tvExpectedCash.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        else tvExpectedCash.setTextColor(ContextCompat.getColor(context, R.color.colorRed));

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
