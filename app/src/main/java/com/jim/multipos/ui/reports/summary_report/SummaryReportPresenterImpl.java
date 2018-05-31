package com.jim.multipos.ui.reports.summary_report;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.till.TillOperation;
import com.jim.multipos.ui.reports.summary_report.adapter.PairString;
import com.jim.multipos.ui.reports.summary_report.adapter.TripleString;
import com.jim.multipos.utils.ExportUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.jim.multipos.utils.ExportUtils.EXCEL;
import static com.jim.multipos.utils.ExportUtils.PDF;

public class SummaryReportPresenterImpl extends BasePresenterImpl<SummaryReportView> implements SummaryReportPresenter {

    private DatabaseManager databaseManager;
    private Context context;
    Calendar fromDate, toDate;
    DecimalFormat decimalFormat;
    DecimalFormat decimalFormatWithProbel;
    DecimalFormat decimalFormatWithoutFix;
    SimpleDateFormat simpleDateFormat;

    List<PairString> summarys;
    List<TripleString> payments;
    List<PairString> summaryAnalytics;
    List<PairString> paymentsAnalytics;

    PaymentType cashPaymentType;
    String abbr;
    int current = 0;
    boolean isExport = false;

    @Inject
    protected SummaryReportPresenterImpl(SummaryReportView summaryReportView, DatabaseManager databaseManager, Context context) {
        super(summaryReportView);
        this.databaseManager = databaseManager;
        this.context = context;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        summarys = new ArrayList<>();
        payments = new ArrayList<>();
        summaryAnalytics = new ArrayList<>();
        paymentsAnalytics = new ArrayList<>();
        decimalFormatWithProbel = BaseAppModule.getFormatterGroupingPattern("#,##0.00");
        decimalFormatWithoutFix = BaseAppModule.getFormatterGroupingPattern("#,###.##");
        cashPaymentType = databaseManager.getCashPaymentType();
        abbr = databaseManager.getMainCurrency().getAbbr();

    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        initDateInterval();
        initDecimal();

        new Handler().postDelayed(()->{
            updateSummaryList();
        },50);
    }

    @Override
    public void onChooseDateInterval(Calendar fromDate, Calendar toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        if (current == 0) updateSummaryList();
        else updatePaymentsList();
        view.updateDateIntervalUi(fromDate, toDate);
    }

    @Override
    public void onSearchTyped(String searchText) {

    }

    @Override
    public void onClickedDateInterval() {
        view.openDateInterval(fromDate, toDate);
    }

    @Override
    public void onClickedExportExcel() {
        view.openExportDialog(EXCEL);
    }

    @Override
    public void onClickedExportPDF() {
        view.openExportDialog(PDF);
    }

    @Override
    public void onClickedFilter() {

    }

    @Override
    public void onChoisedPanel(int postion) {

    }

    @Override
    public void onSalesSummary() {
        current = 0;
        //TODO ON SALES SUMMARY OPENED
        view.activeSalesSummary();
        updateSummaryList();
    }

    @Override
    public void onPaymentsSummary() {
        isExport = false;
        current = 1;
        //TODO ON PAYMENTS SUMMARY OPENED
        view.activePaymentsSummary();
        updatePaymentsList();
    }

    @Override
    public void exportExcel(String filename, String root) {
        isExport = true;
        updatePaymentsList();
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        String description = "";
        ExportUtils.exportSummaryReportToExcel(context, root, filename, description, date, summarys, summaryAnalytics, payments, paymentsAnalytics);
    }

    @Override
    public void exportPdf(String filename, String root) {
        isExport = true;
        updatePaymentsList();
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        String description = "";
        ExportUtils.exportSummaryReportToPdf(context, root, filename, description, date, summarys, summaryAnalytics, payments, paymentsAnalytics);
    }

    @Override
    public void exportExcelToUSB(String filename, UsbFile root) {
        isExport = true;
        updatePaymentsList();
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        String description = "";
        ExportUtils.exportSummaryReportToExcelUSB(context, root, filename, description, date, summarys, summaryAnalytics, payments, paymentsAnalytics);
    }

    @Override
    public void exportPdfToUSB(String filename, UsbFile root) {
        isExport = true;
        updatePaymentsList();
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        String description = "";
        ExportUtils.exportSummaryReportToPdfUSB(context, root, filename, description, date, summarys, summaryAnalytics, payments, paymentsAnalytics);
    }

    //ANALYTICS
    int totalOrderCount = 0;
    double sumOrderToPay = 0;
    double minOrderToPay = 0;
    double maxOrderToPay = 0;

    HashMap<Long, Double> customersSales;

    int totalManualDiscountCount = 0;
    int totalStaticDiscountCount = 0;
    int totalManualServiceFeeCount = 0;
    int totalStaticServiceFeeCount = 0;

    double totalChanges = 0;
    int totalCanceledOrderCount = 0;

    //Summary
    double totalSales = 0;
    double totalServiceFee = 0;
    double totalDiscounts = 0;
    double totalDebtSales = 0;
    double totalDebtIncome = 0;
    double totalTips = 0;
    double totalPayIns = 0;
    double totalPayOuts = 0;
    double totalBankDrops = 0;
    int payinsCount = 0;
    int payoutCount = 0;
    int bankdrobsCount = 0;


    public void updateSummaryList() {
        summarys.clear();
        summaryAnalytics.clear();

        //ANALYTICS
        totalOrderCount = 0;
        sumOrderToPay = 0;
        minOrderToPay = 0;
        maxOrderToPay = 0;

        customersSales = new HashMap<>();

        totalManualDiscountCount = 0;
        totalStaticDiscountCount = 0;
        totalManualServiceFeeCount = 0;
        totalStaticServiceFeeCount = 0;

        totalChanges = 0;
        totalCanceledOrderCount = 0;

        //Summary
        totalSales = 0;
        totalServiceFee = 0;
        totalDiscounts = 0;
        totalDebtSales = 0;
        totalTips = 0;
        totalPayIns = 0;
        totalPayOuts = 0;
        totalBankDrops = 0;


        payinsCount = 0;
        payoutCount = 0;
        bankdrobsCount = 0;

        databaseManager.getOrdersInIntervalForReport(fromDate, toDate).subscribe((orders, throwable) -> {
            totalOrderCount = orders.size();
            for (int i = 0; i < totalOrderCount; i++) {
                if (orders.get(i).getStatus() == Order.CLOSED_ORDER) {
                    sumOrderToPay += orders.get(i).getForPayAmmount();

                    if (minOrderToPay == 0) minOrderToPay = orders.get(i).getForPayAmmount();

                    if (minOrderToPay > orders.get(i).getForPayAmmount())
                        minOrderToPay = orders.get(i).getForPayAmmount();

                    if (maxOrderToPay < orders.get(i).getForPayAmmount())
                        maxOrderToPay = orders.get(i).getForPayAmmount();

                    if (orders.get(i).getCustomer_id() != 0) {
                        double v = customersSales.get(orders.get(i).getCustomer_id()) == null ? 0 : customersSales.get(orders.get(i).getCustomer_id());
                        v += orders.get(i).getForPayAmmount();
                        customersSales.put(orders.get(i).getCustomer_id(), v);
                    }

                    if (orders.get(i).getDiscountId() != 0) {
                        if (orders.get(i).getDiscount().getIsManual())
                            totalManualDiscountCount++;
                        else totalStaticDiscountCount++;
                    }

                    if (orders.get(i).getServiceFeeId() != 0) {
                        if (orders.get(i).getServiceFee().getIsManual())
                            totalManualServiceFeeCount++;
                        else totalStaticServiceFeeCount++;
                    }

                    totalChanges += orders.get(i).getChange();
                    totalSales += orders.get(i).getSubTotalValue();
                    totalServiceFee += orders.get(i).getServiceTotalValue();
                    totalDiscounts += orders.get(i).getDiscountTotalValue();
                    totalDebtSales += orders.get(i).getToDebtValue();
                    totalTips += orders.get(i).getTips();

                    for (int j = 0; j < orders.get(i).getOrderProducts().size(); j++) {

                        if(orders.get(i).getOrderProducts().get(j).getDiscount()!=null){
                            if(orders.get(i).getOrderProducts().get(j).getDiscount().getIsManual())
                                totalManualDiscountCount ++ ;
                            else totalStaticDiscountCount ++;
                        }

                        if(orders.get(i).getOrderProducts().get(j).getServiceFee()!=null){
                            if(orders.get(i).getOrderProducts().get(j).getServiceFee().getIsManual())
                                totalManualServiceFeeCount ++ ;
                            else totalStaticServiceFeeCount ++;
                        }
                    }

                } else if (orders.get(i).getStatus() == Order.CANCELED_ORDER)
                    totalCanceledOrderCount++;
            }

            databaseManager.getTillOperationsInterval(fromDate, toDate).subscribe((tillOperations, throwable1) -> {
                for (int j = 0; j < tillOperations.size(); j++) {
                    if (tillOperations.get(j).getType() == TillOperation.PAY_IN) {
                        totalPayIns += tillOperations.get(j).getAmount();
                        payinsCount++;
                    }
                    if (tillOperations.get(j).getType() == TillOperation.PAY_OUT) {
                        totalPayOuts += tillOperations.get(j).getAmount();
                        payoutCount++;
                    }
                    if (tillOperations.get(j).getType() == TillOperation.BANK_DROP) {
                        totalBankDrops += tillOperations.get(j).getAmount();
                        bankdrobsCount++;
                    }
                }

                summarys.add(new PairString(context.getString(R.string.total_product_sales), decimalFormatWithProbel.format(totalSales)));
                summarys.add(new PairString(context.getString(R.string.total_service_fee), decimalFormatWithProbel.format(totalServiceFee)));
                summarys.add(new PairString(context.getString(R.string.total_discount), decimalFormatWithProbel.format(totalDiscounts)));
                summarys.add(new PairString(context.getString(R.string.total_debt_sales), decimalFormatWithProbel.format(totalDebtSales)));
                summarys.add(new PairString(context.getString(R.string.total_tips), decimalFormatWithProbel.format(totalTips)));
                summarys.add(new PairString(context.getString(R.string.total_pay_ins), decimalFormatWithProbel.format(totalPayIns)));
                summarys.add(new PairString(context.getString(R.string.total_pay_outs), decimalFormatWithProbel.format(totalPayOuts)));
                summarys.add(new PairString(context.getString(R.string.total_bank_drops), decimalFormatWithProbel.format(totalBankDrops)));

                summaryAnalytics.add(new PairString(context.getString(R.string.total_orders_count), decimalFormatWithoutFix.format(totalOrderCount) + " " + context.getString(R.string.pcs)));
                summaryAnalytics.add(new PairString(context.getString(R.string.average_order), decimalFormatWithProbel.format(sumOrderToPay / ((totalOrderCount == 0) ? 1 : totalOrderCount)) + " " + abbr));
                summaryAnalytics.add(new PairString(context.getString(R.string.minimum_order), decimalFormatWithProbel.format(minOrderToPay) + " " + abbr));
                summaryAnalytics.add(new PairString(context.getString(R.string.maximum_order), decimalFormatWithProbel.format(maxOrderToPay) + " " + abbr));
                summaryAnalytics.add(new PairString(context.getString(R.string.payins_count), decimalFormatWithoutFix.format(payinsCount) + " " + context.getString(R.string.pcs)));
                summaryAnalytics.add(new PairString(context.getString(R.string.pay_out_counts), decimalFormatWithoutFix.format(payoutCount) + " " + context.getString(R.string.pcs)));
                summaryAnalytics.add(new PairString(context.getString(R.string.bank_drop_count), decimalFormatWithoutFix.format(bankdrobsCount) + " " + context.getString(R.string.pcs)));

                Map.Entry<Long, Double> maxEntry = null;
                for (Map.Entry<Long, Double> entry : customersSales.entrySet()) {
                    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                        maxEntry = entry;
                    }
                }
                String customerName = "";
                if (maxEntry != null) {
                    Customer customer = databaseManager.getCustomerById(maxEntry.getKey()).blockingGet();
                    customerName = customer.getName();
                }
                summaryAnalytics.add(new PairString(context.getString(R.string.the_best_customer), customerName));
                summaryAnalytics.add(new PairString(context.getString(R.string.total_manual_discounts_count), decimalFormatWithoutFix.format(totalManualDiscountCount) + " " + context.getString(R.string.pcs)));
                summaryAnalytics.add(new PairString(context.getString(R.string.total_static_discounts_count), decimalFormatWithoutFix.format(totalStaticDiscountCount) + " " + context.getString(R.string.pcs)));
                summaryAnalytics.add(new PairString(context.getString(R.string.total_manual_service_count), decimalFormatWithoutFix.format(totalManualServiceFeeCount) + " " + context.getString(R.string.pcs)));
                summaryAnalytics.add(new PairString(context.getString(R.string.total_static_service_count), decimalFormatWithoutFix.format(totalStaticServiceFeeCount) + " " + context.getString(R.string.pcs)));
                summaryAnalytics.add(new PairString(context.getString(R.string.total_changes), decimalFormatWithProbel.format(totalChanges) + " " + abbr));
                summaryAnalytics.add(new PairString(context.getString(R.string.total_canceled_orders_count), decimalFormatWithoutFix.format(totalManualServiceFeeCount) + " " + context.getString(R.string.pcs)));

                view.updateRecyclerViewSummary(summarys);
                view.updatecyclerViewAnalitcs(summaryAnalytics);
            });

        });


    }


    double totalPayments = 0;
    double totalDebt = 0;
    int debtUsedCount = 0;
    HashMap<Long, Double> hmPayments;
    HashMap<Long, Integer> hmPaymentsCount;

    public void updatePaymentsList() {
        hmPayments = new HashMap<>();
        hmPaymentsCount = new HashMap<>();
        totalPayments = 0;
        totalDebt = 0;
        debtUsedCount = 0;
        databaseManager.getOrdersInIntervalForReport(fromDate, toDate).subscribe((orders, throwable) -> {
            List<PaymentType> paymentTypes = databaseManager.getPaymentTypes();
            for (int i = 0; i < paymentTypes.size(); i++) {
                hmPayments.put(paymentTypes.get(i).getId(), 0d);

            }
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).getStatus() == Order.CLOSED_ORDER) {
                    for (int j = 0; j < orders.get(i).getPayedPartitions().size(); j++) {
                        if (orders.get(i).getPayedPartitions().get(j).getPaymentType().getTypeStaticPaymentType() != PaymentType.DEBT_PAYMENT_TYPE) {
                            double value = hmPayments.get(orders.get(i).getPayedPartitions().get(j).getPaymentId()) == null ? 0 : hmPayments.get(orders.get(i).getPayedPartitions().get(j).getPaymentId());
                            value += orders.get(i).getPayedPartitions().get(j).getValue();
                            hmPayments.put(orders.get(i).getPayedPartitions().get(j).getPaymentId(), value);

                            int count = hmPaymentsCount.get(orders.get(i).getPayedPartitions().get(j).getPaymentId()) == null ? 0 : hmPaymentsCount.get(orders.get(i).getPayedPartitions().get(j).getPaymentId());
                            count++;
                            hmPaymentsCount.put(orders.get(i).getPayedPartitions().get(j).getPaymentId(), count);

                        } else {
                            totalDebt += orders.get(i).getPayedPartitions().get(j).getValue();
                            debtUsedCount++;
                        }
                    }
                }
                if (orders.get(i).getChange() != 0) {
                    double value = hmPayments.get(cashPaymentType.getId()) == null ? 0 : hmPayments.get(cashPaymentType.getId());
                    value -= orders.get(i).getChange();
                    hmPayments.put(cashPaymentType.getId(), value);
                }
            }
            List<TripleString> tripleStrings = new ArrayList<>();
            paymentsAnalytics = new ArrayList<>();

            Map.Entry<Long, Double> maxEntry = null;
            for (Map.Entry<Long, Double> entry : hmPayments.entrySet()) {
                if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                    maxEntry = entry;
                }
            }

            Map.Entry<Long, Double> maxEntry2 = null;
            for (Map.Entry<Long, Double> entry : hmPayments.entrySet()) {
                if (maxEntry2 == null || entry.getValue().compareTo(maxEntry2.getValue()) > 0) {
                    maxEntry2 = entry;
                }
            }

            for (double v : hmPayments.values())
                totalPayments += v;
            for (Map.Entry<Long, Double> longDoubleEntry : hmPayments.entrySet()) {
                TripleString tripleString = new TripleString(databaseManager.getPaymentTypeById(longDoubleEntry.getKey()).getName(), decimalFormat.format((longDoubleEntry.getValue() / ((totalPayments == 0 ? 1 : totalPayments))) * 100) + "%", decimalFormatWithProbel.format(longDoubleEntry.getValue()), false);
                tripleStrings.add(tripleString);
            }
            TripleString tripleString = new TripleString(context.getString(R.string.total_payments), decimalFormat.format(100)+ "%", decimalFormatWithProbel.format(totalPayments), true);
            tripleStrings.add(tripleString);
            double sum = totalPayments + totalDebt;
            TripleString tripleStringDebt = new TripleString(context.getString(R.string.debt_report), decimalFormat.format((totalDebt / ((sum == 0 ? 1 : sum))) * 100) + "%", decimalFormatWithProbel.format(totalDebt), false);
            tripleStrings.add(tripleStringDebt);
            TripleString tripleString1 = new TripleString(context.getString(R.string.payments_report), decimalFormat.format((totalPayments / ((sum == 0 ? 1 : sum))) * 100) + "%", decimalFormatWithProbel.format(totalPayments), false);
            tripleStrings.add(tripleString1);
            TripleString tripleString2 = new TripleString(context.getString(R.string.total_cap), decimalFormat.format(100)+ "%", decimalFormatWithProbel.format(sum), true);
            tripleStrings.add(tripleString2);

            for (Map.Entry<Long, Integer> longDoubleEntry : hmPaymentsCount.entrySet()) {
                paymentsAnalytics.add(new PairString(databaseManager.getPaymentTypeById(longDoubleEntry.getKey()).getName() + " " +context.getString(R.string.count_min), decimalFormatWithoutFix.format(longDoubleEntry.getValue()) + " " + context.getString(R.string.pcs)));
            }
            paymentsAnalytics.add(new PairString(context.getString(R.string.debts_count), decimalFormatWithoutFix.format(debtUsedCount) + " " + context.getString(R.string.pcs)));
            payments.clear();
            payments.addAll(tripleStrings);
            if (!isExport) {
                view.updateRecyclerViewPayments(tripleStrings);
                view.updatecyclerViewAnalitcs(paymentsAnalytics);
            }
        });
    }

    public void initDateInterval() {
        fromDate = new GregorianCalendar();
        fromDate.set(Calendar.HOUR, 0);
        fromDate.set(Calendar.MINUTE, 0);
        fromDate.set(Calendar.MILLISECOND, 0);
        // init first last month
        fromDate.add(Calendar.MONTH, -1);

        toDate = new GregorianCalendar();
        toDate.set(Calendar.HOUR, 23);
        toDate.set(Calendar.MINUTE, 59);
        toDate.set(Calendar.MILLISECOND, 999);
        view.updateDateIntervalUi(fromDate, toDate);
    }

    private void initDecimal() {
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat1.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat1.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat = decimalFormat1;
    }
}
