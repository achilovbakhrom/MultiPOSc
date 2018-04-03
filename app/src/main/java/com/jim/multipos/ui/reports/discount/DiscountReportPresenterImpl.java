package com.jim.multipos.ui.reports.discount;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderProduct;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

public class DiscountReportPresenterImpl extends BasePresenterImpl<DiscountReportView> implements DiscountReportPresenter {

    private Object[][] firstObjects;
    private Object[][] secondObjects;
    private Object[][] thirdObjects;
    private int currentPosition = 0;
    private Calendar fromDate;
    private Calendar toDate;
    private DecimalFormat decimalFormat;
    private SimpleDateFormat simpleDateFormat;
    private DatabaseManager databaseManager;

    @Inject
    protected DiscountReportPresenterImpl(DiscountReportView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat1.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat1.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat = decimalFormat1;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        Date currentDate = new Date();
        fromDate = new GregorianCalendar();
        toDate = new GregorianCalendar();

        toDate.setTime(currentDate);
        toDate.set(Calendar.HOUR_OF_DAY, 23);
        toDate.set(Calendar.MINUTE, 59);
        toDate.set(Calendar.SECOND, 59);

        fromDate.add(Calendar.MONTH, -1);
        fromDate.set(Calendar.HOUR_OF_DAY, 0);
        fromDate.set(Calendar.MINUTE, 0);
        fromDate.set(Calendar.SECOND, 0);
        view.updateDateIntervalUi(fromDate, toDate);
        initReportContent(currentPosition);
        view.initTable(firstObjects);
    }

    private void initReportContent(int currentPosition) {
        if (currentPosition == 0) {
            List<OrderProduct> orderProducts = new ArrayList<>();
            List<Long> tillIds = new ArrayList<>();
            List<Long> orderDates = new ArrayList<>();
            databaseManager.getClosedOrdersInIntervalForReport(fromDate, toDate).subscribe(orders1 -> {
                for (Order order: orders1){
                    for (int i = 0; i < order.getOrderProducts().size(); i++) {
                        if (order.getOrderProducts().get(i).getDiscount() != null){
                            orderProducts.add(order.getOrderProducts().get(i));
                            tillIds.add(order.getTillId());
                            orderDates.add(order.getCreateAt());
                        }
                    }
                }
            });
            firstObjects = new Object[orderProducts.size()][9];
            for (int i = 0; i < orderProducts.size(); i++) {
                OrderProduct orderProduct = orderProducts.get(i);
                firstObjects[i][0] = "#" + orderProduct.getOrderId();
                firstObjects[i][1] = "#" + tillIds.get(i);
                firstObjects[i][2] = orderProduct.getProduct().getName();
                firstObjects[i][3] = orderProduct.getProduct().getSku();
                firstObjects[i][4] = orderProduct.getCount();
                firstObjects[i][5] = orderProduct.getDiscountAmount();
                firstObjects[i][6] = orderDates.get(i);
                firstObjects[i][7] = orderProduct.getDiscount().getName();
                firstObjects[i][8] = orderProduct.getDiscount().getIsManual() ? 1 : 0;
            }
            view.updateTable(firstObjects, currentPosition);

        } else if (currentPosition == 1) {

            view.updateTable(secondObjects, currentPosition);
        } else if (currentPosition == 2) {

            view.updateTable(thirdObjects, currentPosition);
        }
    }

    @Override
    public void onChooseDateInterval(Calendar fromDate, Calendar toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        initReportContent(currentPosition);
        view.updateDateIntervalUi(fromDate, toDate);
    }

    @Override
    public void onSearchTyped(String searchText) {

    }

    @Override
    public void onClickedDateInterval() {

    }

    @Override
    public void onClickedExportExcel() {

    }

    @Override
    public void onClickedExportPDF() {

    }

    @Override
    public void onClickedFilter() {

    }

    @Override
    public void onChoisedPanel(int position) {
        currentPosition = position;
        switch (position) {
            case 0:
                initReportContent(position);
                break;
            case 1:
                initReportContent(position);
                break;
            case 2:
                initReportContent(position);
                break;
        }
    }
}
