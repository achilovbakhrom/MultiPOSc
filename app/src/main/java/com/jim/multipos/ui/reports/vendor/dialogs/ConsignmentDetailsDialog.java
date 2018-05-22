package com.jim.multipos.ui.reports.vendor.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.utils.ExportDialog;
import com.jim.multipos.utils.ExportToDialog;
import com.jim.multipos.utils.ExportUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jim.multipos.utils.ExportUtils.EXCEL;
import static com.jim.multipos.utils.ExportUtils.PDF;

public class ConsignmentDetailsDialog extends Dialog {

    @BindView(R.id.flTable)
    FrameLayout flTable;
    @BindView(R.id.btnClose)
    MpButton btnClose;
    @BindView(R.id.llExport)
    LinearLayout llExport;
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


    private int dataType[] = {ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT};
    private int weights[] = {10, 5, 10, 10};
    private int aligns[] = {Gravity.LEFT, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT};
    private Context context;
    private Consignment consignment;
    private Object[][] objects;
    private String date, consignmentId, vendor, type;
    private String[] values, names;

    public ConsignmentDetailsDialog(@NonNull Context context, Consignment consignment, DatabaseManager databaseManager) {
        super(context);
        this.context = context;
        this.consignment = consignment;
        DecimalFormat decimalFormat = BaseAppModule.getFormatterGrouping();
        View dialogView = getLayoutInflater().inflate(R.layout.consignment_details_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        initData();
        initTable();
        consignmentId = context.getString(R.string.consignment) + " : " + consignment.getConsignmentNumber();
        tvConsignmentNumber.setText(consignmentId);
        values = new String[5];
        if (consignment.getConsignmentType() == Consignment.INCOME_CONSIGNMENT) {
            tvConsignmentType.setText(context.getString(R.string.type) + " : " + context.getString(R.string.receive));
            type = context.getString(R.string.type) + " : " + context.getString(R.string.receive);
        } else {
            tvConsignmentType.setText(context.getString(R.string.type) + " : " + context.getString(R.string.return_));
            type = context.getString(R.string.type) + " : " + context.getString(R.string.return_);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        tvDate.setText(context.getString(R.string.created_date) + " : " + simpleDateFormat.format(consignment.getCreatedDate()));
        date = context.getString(R.string.created_date) + " : " + simpleDateFormat.format(consignment.getCreatedDate());
        tvVendor.setText(context.getString(R.string.vendor) + " : " + consignment.getVendor().getName());
        vendor = context.getString(R.string.vendor) + " : " + consignment.getVendor().getName();
        tvTotalShouldPay.setText(decimalFormat.format(consignment.getTotalAmount()));
        values[0] = decimalFormat.format(consignment.getTotalAmount());
        if (consignment.getFirstPayId() != null) {
            BillingOperations firstPay = databaseManager.getBillingOperationsById(consignment.getFirstPayId()).blockingGet();
            tvPaid.setText(decimalFormat.format(firstPay.getAmount()));
            values[4] = context.getString(R.string.none);
            if (consignment.getIsFromAccount()) {
                values[4] = firstPay.getAccount().getName();
                tvPaidAccount.setText(firstPay.getAccount().getName());
            } else tvPaidAccount.setText(context.getString(R.string.none));
            tvDebt.setText(decimalFormat.format(consignment.getTotalAmount() - firstPay.getAmount()));
            values[2] = decimalFormat.format(consignment.getTotalAmount() - firstPay.getAmount());
            values[1] = decimalFormat.format(firstPay.getAmount());
        } else {
            values[4] = context.getString(R.string.none);
            tvPaid.setText(decimalFormat.format(0));
            tvDebt.setText(decimalFormat.format(consignment.getTotalAmount()));
            values[1] = decimalFormat.format(0);
            values[2] = decimalFormat.format(consignment.getTotalAmount());
            tvPaidAccount.setText(context.getString(R.string.none));
        }
        values[3] = consignment.getDescription();
        tvDescription.setText(consignment.getDescription());
        names = new String[5];
        names[0] = context.getString(R.string.total_should_pay);
        names[1] = context.getString(R.string.first_pay);
        names[2] = context.getString(R.string.debt_);
        names[3] = context.getString(R.string.description);
        names[4] = context.getString(R.string.account);
        btnClose.setOnClickListener((view -> {
            dismiss();
        }));
        llExport.setOnClickListener(view -> {
            ExportDialog dialog = new ExportDialog(context, 1, new ExportDialog.OnExportItemClick() {
                @Override
                public void onToExcel() {
                    openExportDialog(EXCEL);
                }

                @Override
                public void onToPdf() {
                    openExportDialog(PDF);
                }
            });
            dialog.show();
        });

    }

    private ExportToDialog exportDialog;

    private void openExportDialog(int mode) {
        exportDialog = new ExportToDialog(context, mode, context.getString(R.string.consignment_details), new ExportToDialog.OnExportListener() {
            @Override
            public void onFilePickerClicked() {
                openFilePickerDialog();
            }

            @Override
            public void onSaveToUSBClicked(String filename, UsbFile root) {
                if (mode == EXCEL) {
                    ExportUtils.exportConsignmentToExcelUsb(context, filename, root, objects, weights, dataType, titles, vendor, consignmentId, date, type, values, names);
                } else {
                    ExportUtils.exportConsignmentToPdfUsb(context, filename, root, objects, weights, dataType, titles, vendor, consignmentId, date, type, values, names);
                }
            }

            @Override
            public void onSaveClicked(String filename, String root) {
                if (mode == EXCEL) {
                    ExportUtils.exportConsignmentToExcel(context, filename, root, objects, weights, dataType, titles, vendor, consignmentId, date, type, values, names);
                } else {
                    ExportUtils.exportConsignmentToPdf(context, filename, root, objects, weights, dataType, titles, vendor, consignmentId, date, type, values, names);
                }
            }
        });
        exportDialog.show();
    }

    private void openFilePickerDialog() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.DIR_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;
        FilePickerDialog dialog = new FilePickerDialog(getContext(), properties);
        dialog.setTitle(getContext().getString(R.string.select_a_directory));
        dialog.setDialogSelectionListener(files -> {
            exportDialog.setPath(files);
        });
        dialog.show();
    }

    private void initData() {
        List<ConsignmentProduct> consignmentProducts = consignment.getAllConsignmentProducts();
        objects = new Object[consignmentProducts.size()][4];
        for (int i = 0; i < consignmentProducts.size(); i++) {
            ConsignmentProduct product = consignmentProducts.get(i);
            objects[i][0] = product.getProduct().getName();
            objects[i][1] = product.getCountValue() + " " + product.getProduct().getMainUnit().getAbbr();
            objects[i][2] = product.getCostValue();
            objects[i][3] = product.getCostValue() * product.getCountValue();
        }
    }

    String titles[];

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
