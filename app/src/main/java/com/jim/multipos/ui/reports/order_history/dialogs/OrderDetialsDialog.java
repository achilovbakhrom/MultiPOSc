package com.jim.multipos.ui.reports.order_history.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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
import com.jim.mpviews.utils.Utils;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderChangesLog;
import com.jim.multipos.data.db.model.order.OrderProduct;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.utils.ExportDialog;
import com.jim.multipos.utils.ExportToDialog;
import com.jim.multipos.utils.ExportUtils;
import com.jim.multipos.utils.ReportUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jim.multipos.utils.ExportUtils.EXCEL;
import static com.jim.multipos.utils.ExportUtils.PDF;

public class OrderDetialsDialog extends Dialog {
    //max reportView hight 300dp
    @BindView(R.id.llOrderDetials)
    LinearLayout llOrderDetials;
    @BindView(R.id.tvOrderDetials)
    TextView tvOrderDetials;
    @BindView(R.id.flBottomLine)
    FrameLayout flBottomLine;

    @BindView(R.id.llPayments)
    LinearLayout llPayments;
    @BindView(R.id.tvPayments)
    TextView tvPayments;
    @BindView(R.id.flPaymentButton)
    FrameLayout flPaymentButton;

    @BindView(R.id.llLifecycle)
    LinearLayout llLifecycle;
    @BindView(R.id.tvLifecycle)
    TextView tvLifecycle;
    @BindView(R.id.flLifecycleBottom)
    FrameLayout flLifecycleBottom;

    @BindView(R.id.llOrderDetialsFrame)
    LinearLayout llOrderDetialsFrame;
    @BindView(R.id.llOrderPaymentsFrame)
    LinearLayout llOrderPaymentsFrame;
    @BindView(R.id.flTable)
    FrameLayout flTable;
    @BindView(R.id.btnClose)
    MpButton btnClose;
    @BindView(R.id.llExport)
    LinearLayout llExport;
    @BindView(R.id.tvOrderId)
    TextView tvOrderId;
    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @BindView(R.id.tvOrderCreated)
    TextView tvOrderCreated;

    private int position = 0;
    /**
     * Order Detials Table Config
     */
    private int orderDetialsDataType[] = {ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.NAME, ReportViewConstants.AMOUNT, ReportViewConstants.NAME, ReportViewConstants.AMOUNT};
    private String orderDetialsTitles[];
    private int orderDetialsWeights[] = {13, 8, 10, 10, 8, 10, 8, 10, 12};
    private int orderDetialsAligns[] = {Gravity.LEFT, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT, Gravity.LEFT, Gravity.RIGHT, Gravity.LEFT, Gravity.RIGHT};
    /**
     * Payments Table Config
     */
    private int paymentsDataType[] = {ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.ID, ReportViewConstants.AMOUNT};
    private String paymentsTitles[];
    private int paymentsWeights[] = {10, 10, 10, 10};
    private int paymentsAligns[] = {Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.RIGHT};
    /**
     * Lifecycle Table Config
     */
    private int lifecycleDataType[] = {ReportViewConstants.DATE, ReportViewConstants.STATUS, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.ACTION};
    private String lifecycleTitles[];
    private Object[][][] lifecycleStatusTypes;
    private int lifecycleWeights[] = {10, 10, 10, 10, 10};
    private int lifecycleAligns[] = {Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.LEFT, Gravity.CENTER};
    private Context context;
    private Order order;
    private DatabaseManager databaseManager;
    private Object[][] details, payments, lifecycle;
    private String[] orderDetails, paymentDetails;
    private double[] orderValues, paymentValues;
    private String date, orderId, customer = "";
    private DecimalFormat decimalFormat;

    public OrderDetialsDialog(@NonNull Context context, Order order, DatabaseManager databaseManager) {
        super(context);
        this.context = context;
        this.order = order;
        this.databaseManager = databaseManager;
        decimalFormat = BaseAppModule.getFormatterGroupingPattern("#,###.##");

        View dialogView = getLayoutInflater().inflate(R.layout.order_details_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        tvOrderId.setText(context.getString(R.string.order) + " #" + order.getId());
        if (order.getCustomer() != null) {
            customer = order.getCustomer().getName();
            tvCustomerName.setText(context.getString(R.string.customer) + ": " + order.getCustomer().getName());
        } else tvCustomerName.setText(context.getString(R.string.customer) + ": ");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        tvOrderCreated.setText(simpleDateFormat.format(order.getCreateAt()));
        orderId = "#" + order.getId();
        date = simpleDateFormat.format(order.getCreateAt());
        initTableConfigs(context);
        initOrderPaymentsTable();
        initOrderLifecycleTable();
        fillOrderPayments();
        disableLifecycle();
        disablePayments();
        enableOrderDetials();
        initOrderProductTable();
        fillOrderDetials();
        llOrderDetials.setOnClickListener((view -> {
            if (position == 0) return;
            changePanel(position, 0);
        }));
        llPayments.setOnClickListener((view) -> {
            if (position == 1) return;
            changePanel(position, 1);
        });
        llLifecycle.setOnClickListener((view) -> {
            if (position == 2) return;
            changePanel(position, 2);
        });
        btnClose.setOnClickListener((view -> this.dismiss()));
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
        exportDialog = new ExportToDialog(context, mode, context.getString(R.string.order_details) + " " + orderId, new ExportToDialog.OnExportListener() {
            @Override
            public void onFilePickerClicked() {
                openFilePickerDialog();
            }

            @Override
            public void onSaveToUSBClicked(String filename, UsbFile root) {
                if (mode == EXCEL)
                    ExportUtils.exportOrderDetailsToExcelUsb(context, filename, root, orderId, customer, date, details, payments, lifecycle, orderDetails, orderValues, paymentValues, paymentDetails, orderDetialsDataType, orderDetialsTitles, orderDetialsWeights, paymentsDataType, paymentsTitles, paymentsWeights, lifecycleDataType, lifecycleTitles, lifecycleWeights, lifecycleStatusTypes);
                else
                    ExportUtils.exportOrderDetailsToPdfUsb(context, filename, root, orderId, customer, date, details, payments, lifecycle, orderDetails, orderValues, paymentValues, paymentDetails, orderDetialsDataType, orderDetialsTitles, orderDetialsWeights, paymentsDataType, paymentsTitles, paymentsWeights, lifecycleDataType, lifecycleTitles, lifecycleWeights, lifecycleStatusTypes);

            }

            @Override
            public void onSaveClicked(String filename, String root) {
                if (mode == EXCEL)
                    ExportUtils.exportOrderDetailsToExcel(context, filename, root, orderId, customer, date, details, payments, lifecycle, orderDetails, orderValues, paymentValues, paymentDetails, orderDetialsDataType, orderDetialsTitles, orderDetialsWeights, paymentsDataType, paymentsTitles, paymentsWeights, lifecycleDataType, lifecycleTitles, lifecycleWeights, lifecycleStatusTypes);
                else
                    ExportUtils.exportOrderDetailsToPdf(context, filename, root, orderId, customer, date, details, payments, lifecycle, orderDetails, orderValues, paymentValues, paymentDetails, orderDetialsDataType, orderDetialsTitles, orderDetialsWeights, paymentsDataType, paymentsTitles, paymentsWeights, lifecycleDataType, lifecycleTitles, lifecycleWeights, lifecycleStatusTypes);
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
        dialog.setNegativeBtnName(getContext().getString(R.string.cancel));
        dialog.setPositiveBtnName(getContext().getString(R.string.select));
        dialog.setTitle(getContext().getString(R.string.select_a_directory));
        dialog.setDialogSelectionListener(files -> exportDialog.setPath(files));
        dialog.show();
    }

    private void changePanel(int oldPos, int newPos) {
        switch (oldPos) {
            case 0:
                disableOrderDetials();
                llOrderDetialsFrame.setVisibility(View.GONE);
                break;
            case 1:
                disablePayments();
                llOrderPaymentsFrame.setVisibility(View.GONE);
                break;
            case 2:
                disableLifecycle();
                break;
        }
        switch (newPos) {
            case 0:
                enableOrderDetials();
                llOrderDetialsFrame.setVisibility(View.VISIBLE);
                initOrderProductTable();
                fillOrderDetials();
                break;
            case 1:
                enablePayments();
                llOrderPaymentsFrame.setVisibility(View.VISIBLE);
                initOrderPaymentsTable();
                fillOrderPayments();
                break;
            case 2:
                enableLifecycle();
                initOrderLifecycleTable();
                break;
        }
        position = newPos;
    }

    private void disableOrderDetials() {
        tvOrderDetials.setTextColor(Color.parseColor("#a9a9a9"));
        flBottomLine.setVisibility(View.INVISIBLE);
    }

    private void disablePayments() {
        tvPayments.setTextColor(Color.parseColor("#a9a9a9"));
        flPaymentButton.setVisibility(View.INVISIBLE);
    }

    private void disableLifecycle() {
        tvLifecycle.setTextColor(Color.parseColor("#a9a9a9"));
        flLifecycleBottom.setVisibility(View.INVISIBLE);
    }

    private void enableOrderDetials() {
        tvOrderDetials.setTextColor(Color.parseColor("#2e91cc"));
        flBottomLine.setVisibility(View.VISIBLE);
    }

    private void enablePayments() {
        tvPayments.setTextColor(Color.parseColor("#2e91cc"));
        flPaymentButton.setVisibility(View.VISIBLE);
    }

    private void enableLifecycle() {
        tvLifecycle.setTextColor(Color.parseColor("#2e91cc"));
        flLifecycleBottom.setVisibility(View.VISIBLE);
    }

    private void initTableConfigs(Context context) {
        orderDetialsTitles = context.getResources().getStringArray(R.array.order_products_titles_report);
        paymentsTitles = context.getResources().getStringArray(R.array.order_payments_titles_report);
        lifecycleTitles = context.getResources().getStringArray(R.array.order_lifecycle_titles_report);

        lifecycleStatusTypes = new Object[][][]{
                {
                        {Order.CLOSED_ORDER, context.getString(R.string.order_status_closed), R.color.colorGreen},
                        {Order.HOLD_ORDER, context.getString(R.string.order_status_held), R.color.colorBlue},
                        {Order.CANCELED_ORDER, context.getString(R.string.order_status_canceled), R.color.colorRed}
                }
        };
    }

    private void initOrderProductTable() {
        List<OrderProduct> orderProducts = order.getOrderProducts();
        Object[][] objects = new Object[orderProducts.size()][9];
        for (int i = 0; i < orderProducts.size(); i++) {
            objects[i][0] = ReportUtils.getProductName(orderProducts.get(i).getProduct());
            objects[i][1] = ReportUtils.getQty(orderProducts.get(i).getCount(), orderProducts.get(i).getProduct(), decimalFormat);
            objects[i][2] = orderProducts.get(i).getPrice();
            objects[i][3] = orderProducts.get(i).getPrice() * orderProducts.get(i).getCount();
            objects[i][4] = orderProducts.get(i).getDiscountAmount();
            objects[i][5] = orderProducts.get(i).getDiscount() == null ? "" : orderProducts.get(i).getDiscount().getName();
            objects[i][6] = orderProducts.get(i).getServiceAmount();
            objects[i][7] = orderProducts.get(i).getServiceFee() == null ? "" : orderProducts.get(i).getServiceFee().getName();
            objects[i][8] = orderProducts.get(i).getPrice() * orderProducts.get(i).getCount() + orderProducts.get(i).getDiscountAmount() + orderProducts.get(i).getServiceAmount();
        }
        details = objects;
        ReportView.Builder builder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(orderDetialsTitles)
                .setDataTypes(orderDetialsDataType)
                .setWeight(orderDetialsWeights)
                .setDataAlignTypes(orderDetialsAligns)
                .setDefaultSort(0)
                .setViewMaxHeight(250)
                .build();
        ReportView reportView = new ReportView(builder);
        reportView.getBuilder().init(objects);
        flTable.removeAllViews();
        flTable.addView(reportView.getBuilder().getView());
    }

    @BindView(R.id.tvSubtTotal)
    TextView tvSubtTotal;
    @BindView(R.id.tvSummary)
    TextView tvSummary;
    @BindView(R.id.tvOrderDiscountName)
    TextView tvOrderDiscountName;
    @BindView(R.id.tvOrderDiscountAmmount)
    TextView tvOrderDiscountAmmount;
    @BindView(R.id.tvOrderServiceFeeName)
    TextView tvOrderServiceFeeName;
    @BindView(R.id.tvOrderServiceFeeAmmount)
    TextView tvOrderServiceFeeAmmount;
    @BindView(R.id.tvTotalToPay)
    TextView tvTotalToPay;
    @BindView(R.id.llOrderDiscount)
    LinearLayout llOrderDiscount;
    @BindView(R.id.llOrderServiceFee)
    LinearLayout llOrderServiceFee;

    private void fillOrderDetials() {
        orderDetails = new String[5];
        orderValues = new double[5];
        tvSubtTotal.setText(decimalFormat.format(order.getSubTotalValue()));
        double summary = 0;
        List<OrderProduct> orderProducts = order.getOrderProducts();
        for (int i = 0; i < orderProducts.size(); i++) {
            summary += orderProducts.get(i).getPrice() * orderProducts.get(i).getCount() + orderProducts.get(i).getDiscountAmount() + orderProducts.get(i).getServiceAmount();
        }
        tvSummary.setText(decimalFormat.format(summary));
        if (order.getDiscount() == null) {
            llOrderDiscount.setVisibility(View.GONE);
            orderDetails[3] = "";
            orderValues[3] = 0;
        } else {
            tvOrderDiscountName.setText(order.getDiscount().getName() + " (" + decimalFormat.format(order.getDiscount().getAmount()) + (order.getDiscount().getAmountType() == Discount.PERCENT ? "%" : "") + " " + getContext().getString(R.string.discount_one) + "):");
            tvOrderDiscountAmmount.setText(decimalFormat.format(order.getDiscountAmount()));
            orderDetails[3] = order.getDiscount().getName() + " (" + decimalFormat.format(order.getDiscount().getAmount()) + (order.getDiscount().getAmountType() == Discount.PERCENT ? "%" : "") + " " + getContext().getString(R.string.discount) + ")";
            orderValues[3] = order.getDiscountAmount();
        }
        if (order.getServiceFee() == null) {
            llOrderServiceFee.setVisibility(View.GONE);
            orderDetails[4] = "";
            orderValues[4] = 0;
        } else {
            tvOrderServiceFeeName.setText(order.getServiceFee().getName() + " (" + decimalFormat.format(order.getServiceFee().getAmount()) + (order.getServiceFee().getType() == Discount.PERCENT ? "%" : "") + " " + getContext().getString(R.string.service_fee) + "):");
            tvOrderServiceFeeAmmount.setText("+" + decimalFormat.format(order.getServiceAmount()));
            orderDetails[4] = order.getServiceFee().getName() + " (" + decimalFormat.format(order.getServiceFee().getAmount()) + (order.getServiceFee().getType() == Discount.PERCENT ? "%" : "") + " " + getContext().getString(R.string.service_fee) + ")";
            orderValues[4] = order.getServiceAmount();
        }
        tvTotalToPay.setText(decimalFormat.format(order.getTotalPayed()));
        orderDetails[0] = context.getString(R.string.subtotal);
        orderValues[0] = order.getSubTotalValue();
        orderDetails[1] = context.getString(R.string.summary);
        orderValues[1] = summary;
        orderDetails[2] = context.getString(R.string.to_pay);
        orderValues[2] = order.getTotalPayed();
    }


    private void initOrderPaymentsTable() {
        List<PayedPartitions> payedPartitions = order.getPayedPartitions();
        Object[][] objects = new Object[payedPartitions.size()][4];
        for (int i = 0; i < payedPartitions.size(); i++) {
            objects[i][0] = payedPartitions.get(i).getPaymentType().getName();
            objects[i][1] = payedPartitions.get(i).getPaymentType().getAccount().getName();
            if (payedPartitions.get(i).getPaymentType().getAccount().getStaticAccountType() == Account.DEBT_ACCOUNT)
                objects[i][2] = order.getDebtId();
            else objects[i][2] = "";
            objects[i][3] = payedPartitions.get(i).getValue();
        }
        ReportView.Builder builder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(paymentsTitles)
                .setDataTypes(paymentsDataType)
                .setWeight(paymentsWeights)
                .setDataAlignTypes(paymentsAligns)
                .setDefaultSort(0)
                .setViewMaxHeight(250)
                .build();
        payments = objects;
        ReportView reportView = new ReportView(builder);
        reportView.getBuilder().init(objects);
        flTable.removeAllViews();
        flTable.addView(reportView.getBuilder().getView());
    }

    @BindView(R.id.tvTotalPayed)
    TextView tvTotalPayed;
    @BindView(R.id.tvTotalToPayPayment)
    TextView tvTotalToPayPayment;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.tvChangeFromCash)
    TextView tvChangeFromCash;

    private void fillOrderPayments() {
        paymentDetails = new String[4];
        paymentValues = new double[4];
        tvTotalPayed.setText(decimalFormat.format(order.getTotalPayed()));
        tvTotalToPayPayment.setText(decimalFormat.format(order.getForPayAmmount()));
        tvTips.setText(decimalFormat.format(order.getTips()));
        tvChangeFromCash.setText(decimalFormat.format(order.getChange()));
        paymentDetails[0] = context.getString(R.string.total_paid);
        paymentDetails[1] = context.getString(R.string.total_to_pay);
        paymentDetails[2] = context.getString(R.string.coin);
        paymentDetails[3] = context.getString(R.string.change_from_cashbox);
        paymentValues[0] = order.getTotalPayed();
        paymentValues[1] = order.getForPayAmmount();
        paymentValues[2] = order.getTips();
        paymentValues[3] = order.getChange();

    }


    private void initOrderLifecycleTable() {
        List<OrderChangesLog> orderChangesLogsHistory = order.getOrderChangesLogsHistory();
        Object[][] objects = new Object[orderChangesLogsHistory.size()][5];
        for (int i = 0; i < orderChangesLogsHistory.size(); i++) {
            objects[i][0] = orderChangesLogsHistory.get(i).getChangedAt();
            objects[i][1] = orderChangesLogsHistory.get(i).getToStatus();
            String changeType = "";
            switch (orderChangesLogsHistory.get(i).getChangedCauseType()) {
                case OrderChangesLog.HAND:
                    changeType = getContext().getString(R.string.hand_change_type_report);
                    break;
                case OrderChangesLog.EDITED:
                    changeType = getContext().getString(R.string.edited_change_type);
                    break;
                case OrderChangesLog.PAYED:
                    changeType = getContext().getString(R.string.payed_change_type);
                    break;
                case OrderChangesLog.CONTINUE:
                    changeType = getContext().getString(R.string.continue_change_type);
                    break;
                case OrderChangesLog.HAND_AT_CLOSE_TILL:
                    changeType = getContext().getString(R.string.hand_close_till_change_type);
                    break;
            }
            objects[i][2] = changeType;
            objects[i][3] = orderChangesLogsHistory.get(i).getReason() == null ? "" : orderChangesLogsHistory.get(i).getReason();
            objects[i][4] = orderChangesLogsHistory.get(i).getRelationshipOrderId() == 0 ? "" : orderChangesLogsHistory.get(i).getRelationshipOrderId();
        }
        ReportView.Builder builder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(lifecycleTitles)
                .setDataTypes(lifecycleDataType)
                .setWeight(lifecycleWeights)
                .setDataAlignTypes(lifecycleAligns)
                .setDefaultSort(0)
                .setStatusTypes(lifecycleStatusTypes)
                .setViewMaxHeight(250)
                .setOnReportViewResponseListener((relivantObjects, row, column) -> {
                     if (relivantObjects[row][4] instanceof Long){
                        Long id = (Long) relivantObjects[row][4];
                        Order relavantOrder = databaseManager.getOrder(id).blockingGet();
                        OrderDetialsDialog dialog = new OrderDetialsDialog(context, relavantOrder, databaseManager);
                        dialog.show();
                        dismiss();
                     }
                })
                .build();
        lifecycle = objects;
        ReportView reportView = new ReportView(builder);
        reportView.getBuilder().init(objects);
        flTable.removeAllViews();
        flTable.addView(reportView.getBuilder().getView());
    }
}
