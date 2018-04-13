package com.jim.multipos.ui.reports.tills.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillDetails;
import com.jim.multipos.data.db.model.till.TillManagementOperation;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 30.03.2018.
 */

public class TillDetailsDialog extends Dialog {

    @BindView(R.id.llAccountContainer)
    LinearLayout llAccountContainer;
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
    @BindView(R.id.tvTotalForTill)
    TextView tvTotalForTill;
    @BindView(R.id.tvTotalClosedAmount)
    TextView tvTotalClosedAmount;
    @BindView(R.id.tvToNexTAmount)
    TextView tvToNexTAmount;
    @BindView(R.id.tvTillAmountVariance)
    TextView tvTillAmountVariance;
    @BindView(R.id.tvCloseAmountDescription)
    TextView tvCloseAmountDescription;
    @BindView(R.id.tvToNextAmountDescription)
    TextView tvToNextAmountDescription;
    @BindView(R.id.btnClose)
    MpButton btnClose;
    @BindView(R.id.tvTillId)
    TextView tvTillId;
    @BindView(R.id.tvFromDate)
    TextView tvFromDate;
    @BindView(R.id.tvToDate)
    TextView tvToDate;
    private List<Account> accountList;
    private Context context;
    private DatabaseManager databaseManager;
    private Till till;
    private int current = 0;
    private DecimalFormat decimalFormat;

    public TillDetailsDialog(Context context, DatabaseManager databaseManager, Till till) {
        super(context);
        this.context = context;
        this.databaseManager = databaseManager;
        this.till = till;
        View dialogView = getLayoutInflater().inflate(R.layout.till_details_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        decimalFormat = BaseAppModule.getFormatter();
        accountList = databaseManager.getAccounts();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        tvTillId.setText(context.getString(R.string.details_of_till) + till.getId());
        if (till.getOpenDate() != null)
            tvFromDate.setText(simpleDateFormat.format(new Date(till.getOpenDate())));
        if (till.getCloseDate() != null)
        tvToDate.setText(simpleDateFormat.format(new Date(till.getCloseDate())));

        createAccountPanel();

        for (int i = 0; i < llAccountContainer.getChildCount(); i++) {
            int count = i;
            if (llAccountContainer.getChildAt(i) instanceof LinearLayout) {
                LinearLayout element = (LinearLayout) llAccountContainer.getChildAt(i);
                element.setOnClickListener(view -> {
                    current = count;
                    changePanelElement();
                    fillTillData();
                });
            }
        }
        fillTillData();
        btnClose.setOnClickListener(view -> dismiss());
    }

    private void fillTillData() {
        double payIn = 0, payOut = 0, bankDrop = 0, payToVendor = 0,
                incomeDebt = 0, cashTransactions = 0, tips = 0, totalStartingCash = 0,
                totalAmount = 0, tillAmountVariance = 0, closedAmount = 0, toNextAmount = 0;

        if (current == 0) {
            for (int i = 0; i < accountList.size(); i++) {
                TillDetails tillDetails = databaseManager.getTillDetailsByAccountId(accountList.get(i).getId(), till.getId()).blockingGet();
                payIn += tillDetails.getTotalPayIns();
                payOut += tillDetails.getTotalPayOuts();
                bankDrop += tillDetails.getTotalBankDrops();
                payToVendor += tillDetails.getTotalPayToVendors();
                incomeDebt += tillDetails.getTotalDebtIncome();
                cashTransactions += tillDetails.getTotalSales();
                tips += tillDetails.getTips();
                totalStartingCash += tillDetails.getTotalStartingCash();
                totalAmount += tillDetails.getExpectedTillAmount();
            }

            List<TillManagementOperation> thisTillOperations = databaseManager.getTillManagementOperationsByTillId(till.getId()).blockingGet();
            for (TillManagementOperation operation : thisTillOperations) {
                if (operation.getType() == TillManagementOperation.CLOSED_WITH)
                    closedAmount += operation.getAmount();
                if (operation.getType() == TillManagementOperation.TO_NEW_TILL)
                    toNextAmount += operation.getAmount();
            }
            tvCloseAmountDescription.setText("");
            tvToNextAmountDescription.setText("");
            tillAmountVariance = totalAmount - closedAmount - toNextAmount;
        } else {
            Account account = accountList.get(current - 1);
            TillDetails tillDetails = databaseManager.getTillDetailsByAccountId(account.getId(), till.getId()).blockingGet();
            payIn = tillDetails.getTotalPayIns();
            payOut = tillDetails.getTotalPayOuts();
            bankDrop = tillDetails.getTotalBankDrops();
            payToVendor = tillDetails.getTotalPayToVendors();
            incomeDebt = tillDetails.getTotalDebtIncome();
            cashTransactions = tillDetails.getTotalSales();
            tips = tillDetails.getTips();
            totalStartingCash = tillDetails.getTotalStartingCash();
            totalAmount = tillDetails.getExpectedTillAmount();
            List<TillManagementOperation> thisTillOperations = databaseManager.getTillManagementOperationsByTillId(till.getId()).blockingGet();
            for (TillManagementOperation operation : thisTillOperations) {
                if (account.getId().equals(operation.getAccountId())) {
                    if (operation.getType() == TillManagementOperation.CLOSED_WITH) {
                        closedAmount = operation.getAmount();
                        if (!operation.getDescription().isEmpty()) {
                            tvCloseAmountDescription.setText("(" + operation.getDescription() + ")");
                        } else tvCloseAmountDescription.setText("");
                    }
                    if (operation.getType() == TillManagementOperation.TO_NEW_TILL) {
                        toNextAmount = operation.getAmount();
                        if (!operation.getDescription().isEmpty()) {
                            tvToNextAmountDescription.setText("(" + operation.getDescription() + ")");
                        } else tvToNextAmountDescription.setText("");
                    }
                }
            }
            tillAmountVariance = totalAmount - closedAmount - toNextAmount;
        }
        tvStartingCash.setText(decimalFormat.format(totalStartingCash));
        tvPayOuts.setText(decimalFormat.format(payOut));
        tvPayIns.setText(decimalFormat.format(payIn));
        tvPayToVendors.setText(decimalFormat.format(payToVendor));
        tvIncomeDebt.setText(decimalFormat.format(incomeDebt));
        tvBankDrops.setText(decimalFormat.format(bankDrop));
        tvCashTransactions.setText(decimalFormat.format(cashTransactions));
        tvTips.setText(decimalFormat.format(tips));
        tvTotalForTill.setText(decimalFormat.format(totalAmount));
        tvTotalClosedAmount.setText(decimalFormat.format(closedAmount));
        tvToNexTAmount.setText(decimalFormat.format(toNextAmount));
        tvTillAmountVariance.setText(decimalFormat.format(tillAmountVariance));
    }

    private void changePanelElement() {
        for (int k = 0; k < llAccountContainer.getChildCount(); k++) {
            if (llAccountContainer.getChildAt(k) instanceof LinearLayout) {
                LinearLayout element1 = (LinearLayout) llAccountContainer.getChildAt(k);
                for (int j = 0; j < element1.getChildCount(); j++) {
                    if (element1.getChildAt(j) instanceof FrameLayout) {
                        FrameLayout frameLayout = (FrameLayout) element1.getChildAt(j);
                        if (current == k)
                            frameLayout.setVisibility(View.VISIBLE);
                        else frameLayout.setVisibility(View.GONE);
                    }
                    if (element1.getChildAt(j) instanceof TextView) {
                        TextView textView = (TextView) element1.getChildAt(j);
                        if (current == k)
                            textView.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));
                        else
                            textView.setTextColor(ContextCompat.getColor(context, R.color.colorSecondaryText));
                    }
                }
            }
        }
    }

    private void createAccountPanel() {
        LinearLayout panelElement = new LinearLayout(context);
        panelElement.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        panelElement.setLayoutParams(layoutParams);
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        textParams.weight = 1;
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                context.getResources().getDimension(R.dimen.sixteen_dp));
        textView.setTypeface(textView.getTypeface(), Typeface.NORMAL);
        textView.setPadding(20, 0, 20, 0);
        textView.setLayoutParams(textParams);
        textView.setText("All summary");
        FrameLayout borderLine = new FrameLayout(context);
        LinearLayout.LayoutParams borderParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
        borderLine.setLayoutParams(borderParams);
        borderLine.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlue));
        panelElement.addView(textView);
        panelElement.addView(borderLine);
        llAccountContainer.addView(panelElement);

        for (int i = 0; i < accountList.size(); i++) {
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(layoutParams);
            TextView view = new TextView(context);
            view.setTextColor(ContextCompat.getColor(context, R.color.colorSecondaryText));
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    context.getResources().getDimension(R.dimen.sixteen_dp));
            view.setGravity(Gravity.CENTER_VERTICAL);
            view.setTypeface(textView.getTypeface(), Typeface.NORMAL);
            view.setPadding(20, 0, 20, 0);
            view.setLayoutParams(textParams);
            view.setText(accountList.get(i).getName());
            FrameLayout frameLayout = new FrameLayout(context);
            frameLayout.setLayoutParams(borderParams);
            frameLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlue));
            frameLayout.setVisibility(View.GONE);
            layout.addView(view);
            layout.addView(frameLayout);
            llAccountContainer.addView(layout);
        }
    }
}
