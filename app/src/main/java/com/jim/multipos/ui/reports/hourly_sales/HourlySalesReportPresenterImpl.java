package com.jim.multipos.ui.reports.hourly_sales;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderProduct;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

public class HourlySalesReportPresenterImpl extends BasePresenterImpl<HourlySalesReportView> implements HourlySalesReportPresenter {

    private final DecimalFormat decimalFormat;
    private DatabaseManager databaseManager;
    private Object[][] objects;
    private Calendar fromDate;
    private Calendar toDate;

    @Inject
    protected HourlySalesReportPresenterImpl(HourlySalesReportView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat1.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat1.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat = decimalFormat1;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        Date currentDate = new Date();
        fromDate = new GregorianCalendar();
        toDate = new GregorianCalendar();

        toDate.setTime(currentDate);
        toDate.add(Calendar.DAY_OF_MONTH, -1);
        toDate.set(Calendar.HOUR_OF_DAY, 23);
        toDate.set(Calendar.MINUTE, 59);
        toDate.set(Calendar.SECOND, 59);
        fromDate.add(Calendar.DAY_OF_MONTH, -1);
        fromDate.set(Calendar.HOUR_OF_DAY, 0);
        fromDate.set(Calendar.MINUTE, 0);
        fromDate.set(Calendar.SECOND, 0);
        view.updateDateIntervalUi(fromDate, toDate);
        new Handler().postDelayed(()->{
            initReportTable();
            view.initTable(objects);
        },50);

    }

    @Override
    public void onChooseDateInterval(Calendar fromDate, Calendar toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        view.updateDateIntervalUi(fromDate, toDate);
        initReportTable();
        view.updateTable(objects);
    }

    private void initReportTable() {
        double totalSales = 0;
        List<Order> orders = databaseManager.getOrdersInIntervalForReport(fromDate, toDate).blockingGet();
        for (int i = 0; i < orders.size(); i++) {
            totalSales += orders.get(i).getSubTotalValue();
        }
        int difference = (int) ((toDate.getTimeInMillis() - fromDate.getTimeInMillis()) / (1000 * 60 * 60) + 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        objects = new Object[24][6];
        if (difference / 24 == 1) {
            Calendar dateTemp = new GregorianCalendar();
            dateTemp.setTime(fromDate.getTime());
            for (int i = 0; i < difference; i++) {
                Calendar temp = new GregorianCalendar();
                temp.setTime(dateTemp.getTime());
                String fromHour = simpleDateFormat.format(dateTemp.getTimeInMillis());
                dateTemp.add(Calendar.HOUR_OF_DAY, 1);
                dateTemp.set(Calendar.MINUTE, 0);
                dateTemp.set(Calendar.SECOND, 0);
                String toHour = simpleDateFormat.format(dateTemp.getTimeInMillis() - 1000 * 60);
                int transactions = 0;
                int items = 0;
                double avgSales = 0;
                double salesSummary = 0;
                double salesPercent = 0;
                List<Order> hourlyOrders = databaseManager.getOrdersInIntervalForReport(temp, dateTemp).blockingGet();
                if (hourlyOrders.size() != 0) {
                    transactions = hourlyOrders.size();
                    for (int j = 0; j < hourlyOrders.size(); j++) {
                        salesSummary += hourlyOrders.get(j).getSubTotalValue();
                        for (OrderProduct orderProduct : hourlyOrders.get(j).getOrderProducts())
                            items += orderProduct.getCount();
                    }
                    avgSales = salesSummary / hourlyOrders.size();
                    salesPercent = salesSummary * 100 / totalSales;
                }
                objects[i][0] = fromHour + " - " + toHour;
                objects[i][1] = transactions;
                objects[i][2] = items;
                objects[i][3] = avgSales;
                objects[i][4] = salesSummary;
                objects[i][5] = decimalFormat.format(salesPercent) + "%";
            }
        } else {
            for (int i = 0; i < 24; i++) {
                Calendar temp = new GregorianCalendar();
                temp.set(Calendar.HOUR_OF_DAY, i);
                temp.set(Calendar.MINUTE, 0);
                temp.set(Calendar.SECOND, 0);
                String fromHour = simpleDateFormat.format(temp.getTimeInMillis());
                temp.add(Calendar.HOUR_OF_DAY, 1);
                String toHour = simpleDateFormat.format(temp.getTimeInMillis() - 1000 * 60);
                int transactions = 0;
                int items = 0;
                double avgSales = 0;
                double salesSummary = 0;
                double salesPercent = 0;
                Log.d("hours", i + " " + fromHour + " - " + toHour);
                Calendar dateTemp = new GregorianCalendar();
                dateTemp.setTime(fromDate.getTime());
                for (int j = 0; j < difference / 24; j++) {
                    Calendar dateTemp2 = new GregorianCalendar();
                    dateTemp.set(Calendar.HOUR_OF_DAY, i);
                    dateTemp2.setTime(dateTemp.getTime());
                    dateTemp.set(Calendar.MINUTE, 0);
                    dateTemp.set(Calendar.SECOND, 0);
                    dateTemp.add(Calendar.HOUR_OF_DAY, 1);
                    List<Order> hourlyOrders = databaseManager.getOrdersInIntervalForReport(dateTemp2, dateTemp).blockingGet();
                    if (hourlyOrders.size() != 0) {
                        transactions += hourlyOrders.size();
                        for (int k = 0; k < hourlyOrders.size(); k++) {
                            salesSummary += hourlyOrders.get(k).getSubTotalValue();
                            for (OrderProduct orderProduct : hourlyOrders.get(k).getOrderProducts())
                                items += orderProduct.getCount();
                        }
                    }
                    dateTemp.add(Calendar.DAY_OF_MONTH, 1);
                }
                if (transactions != 0) {
                    avgSales = salesSummary / transactions;
                    salesPercent = salesSummary * 100 / totalSales;
                }
                objects[i][0] = fromHour + " - " + toHour;
                objects[i][1] = transactions;
                objects[i][2] = items;
                objects[i][3] = avgSales;
                objects[i][4] = salesSummary;
                objects[i][5] = decimalFormat.format(salesPercent) + "%";
            }
        }
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

    }

    @Override
    public void onClickedExportPDF() {

    }

    @Override
    public void onClickedFilter() {

    }

    @Override
    public void onChoisedPanel(int postion) {

    }

    @Override
    public void onTillPickerClicked() {

    }
}
