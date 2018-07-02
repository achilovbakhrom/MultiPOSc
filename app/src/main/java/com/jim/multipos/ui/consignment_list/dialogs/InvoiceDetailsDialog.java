package com.jim.multipos.ui.consignment_list.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.Invoice;
import com.jim.multipos.data.db.model.consignment.Outvoice;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvoiceDetailsDialog extends Dialog {

    @BindView(R.id.flTable)
    FrameLayout flTable;
    @BindView(R.id.btnClose)
    MpButton btnClose;
    @BindView(R.id.tvConsignmentNumber)
    TextView tvConsignmentNumber;
    @BindView(R.id.tvVendor)
    TextView tvVendor;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvConsignmentType)
    TextView tvConsignmentType;
    @BindView(R.id.tvTotalShouldPay)
    TextView tvTotalShouldPay;
    @BindView(R.id.tvDebt)
    TextView tvDebt;
    @BindView(R.id.tvPaid)
    TextView tvPaid;
    @BindView(R.id.tvPaidAccount)
    TextView tvPaidAccount;
    @BindView(R.id.tvDescription)
    TextView tvDescription;

    private Context context;
    private DatabaseManager databaseManager;
    private int dataType[] = {ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT};
    private int weights[] = {10, 5, 10, 10};
    private int aligns[] = {Gravity.LEFT, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT};
    private Invoice invoice;
    private Outvoice outvoice;
    private Object[][] objects;
    private boolean isInvoiceNull = false;

    public InvoiceDetailsDialog(@NonNull Context context, DatabaseManager databaseManager, Invoice invoice, Outvoice outvoice) {
        super(context);
        this.context = context;
        this.databaseManager = databaseManager;
        this.invoice = invoice;
        this.outvoice = outvoice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DecimalFormat decimalFormat = BaseAppModule.getFormatterGrouping();
        View dialogView = getLayoutInflater().inflate(R.layout.invoice_details_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        isInvoiceNull = invoice == null;
        initData();
        initTable();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if (isInvoiceNull) {
            tvConsignmentNumber.setText("Return invoice" + " : " + outvoice.getConsigmentNumber());
            tvConsignmentType.setText(context.getString(R.string.type) + " : " + context.getString(R.string.return_));
            tvDate.setText(context.getString(R.string.created_date) + " : " + simpleDateFormat.format(outvoice.getCreatedDate()));
            tvVendor.setText(context.getString(R.string.vendor) + " : " + outvoice.getVendor().getName());
            tvTotalShouldPay.setText(decimalFormat.format(outvoice.getTotalAmount()));
            tvDescription.setText(outvoice.getDescription());
        } else {
            tvConsignmentNumber.setText("Purchase invoice" + " : " + invoice.getConsigmentNumber());
            tvConsignmentType.setText(context.getString(R.string.type) + " : " + context.getString(R.string.receive));
            tvDate.setText(context.getString(R.string.created_date) + " : " + simpleDateFormat.format(invoice.getCreatedDate()));
            tvVendor.setText(context.getString(R.string.vendor) + " : " + invoice.getVendor().getName());
            tvTotalShouldPay.setText(decimalFormat.format(invoice.getTotalAmount()));
            tvDescription.setText(invoice.getDescription());
            if (invoice.getFirstPayId() != null) {
                BillingOperations firstPay = databaseManager.getBillingOperationsById(invoice.getFirstPayId()).blockingGet();
                tvPaid.setText(decimalFormat.format(firstPay.getAmount()));
                if (invoice.getIsFromAccount()) {
                    tvPaidAccount.setText(firstPay.getAccount().getName());
                } else tvPaidAccount.setText(context.getString(R.string.none));
                tvDebt.setText(decimalFormat.format(invoice.getTotalAmount() - firstPay.getAmount()));
            } else {
                tvPaid.setText(decimalFormat.format(0));
                tvDebt.setText(decimalFormat.format(invoice.getTotalAmount()));
                tvPaidAccount.setText(context.getString(R.string.none));
            }

        }
        btnClose.setOnClickListener((view -> {
            dismiss();
        }));
    }

    private void initData() {
        if (isInvoiceNull) {
            List<OutcomeProduct> consignmentProducts = outvoice.getOutcomeProducts();
            objects = new Object[consignmentProducts.size()][4];
            for (int i = 0; i < consignmentProducts.size(); i++) {
                OutcomeProduct product = consignmentProducts.get(i);
                objects[i][0] = product.getProduct().getName();
                objects[i][1] = product.getSumCountValue() + " " + product.getProduct().getMainUnit().getAbbr();
                objects[i][2] = product.getSumCostValue();
                objects[i][3] = product.getSumCostValue() * product.getSumCountValue();
            }
        } else {
            List<IncomeProduct> consignmentProducts = invoice.getIncomeProducts();
            objects = new Object[consignmentProducts.size()][4];
            for (int i = 0; i < consignmentProducts.size(); i++) {
                IncomeProduct product = consignmentProducts.get(i);
                objects[i][0] = product.getProduct().getName();
                objects[i][1] = product.getCountValue() + " " + product.getProduct().getMainUnit().getAbbr();
                objects[i][2] = product.getCostValue();
                objects[i][3] = product.getCostValue() * product.getCountValue();
            }
        }

    }

    private String titles[];

    private void initTable() {
        titles = new String[]{context.getString(R.string.product), context.getString(R.string.qty), context.getString(R.string.each_cost), context.getString(R.string.sum_cost)};
        ReportView.Builder builder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(titles)
                .setDataTypes(dataType)
                .setWeight(weights)
                .setViewMaxHeight(250)
                .setDataAlignTypes(aligns)
                .build();
        ReportView reportView = new ReportView(builder);
        reportView.getBuilder().init(objects);
        flTable.removeAllViews();
        flTable.addView(reportView.getBuilder().getView());
    }
}
