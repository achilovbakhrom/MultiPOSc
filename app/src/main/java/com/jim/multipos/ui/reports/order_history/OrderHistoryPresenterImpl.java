package com.jim.multipos.ui.reports.order_history;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.github.mjdev.libaums.fs.UsbFile;
import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.prefs.PreferencesHelper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.BiConsumer;

import static com.jim.multipos.utils.ExportUtils.EXCEL;
import static com.jim.multipos.utils.ExportUtils.PDF;

public class OrderHistoryPresenterImpl extends BasePresenterImpl<OrderHistoryView> implements OrderHistoryPresenter {


    Calendar fromDate, toDate;
    private OrderHistoryView view;
    private DatabaseManager databaseManager;
    private PreferencesHelper preferencesHelper;
    private Context context;
    List<Order> orders;
    Object[][] objects;
    SimpleDateFormat simpleDateFormat;
    DecimalFormat decimalFormat;
    int[] filterConfig;
    private String searchText = "";

    @Inject
    protected OrderHistoryPresenterImpl(OrderHistoryView orderHistoryView, DatabaseManager databaseManager, PreferencesHelper preferencesHelper, Context context) {
        super(orderHistoryView);
        this.view = orderHistoryView;
        this.databaseManager = databaseManager;
        this.preferencesHelper = preferencesHelper;
        this.context = context;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        filterConfig = new int[]{1,1,1};
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        initDateInterval();
        initDecimal();
        new Handler().postDelayed(()->{
            databaseManager.getOrdersInIntervalForReport(fromDate,toDate).subscribe((orders1, throwable) -> {
                orders = orders1;
                updateObejctsForTable();
                view.initTable(objects);
            });
        },50);

    }

    @Override
    public void onChooseDateInterval(Calendar fromDate, Calendar toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        databaseManager.getOrdersInIntervalForReport(this.fromDate,this.toDate).subscribe((orders1, throwable) -> {
            orders = orders1;
            updateObejctsForTable();
            view.setToTable(objects);
            view.updateDateIntervalUi(fromDate,toDate);
        });
    }
    int prev = -1;
    Object[][] searchResultsTemp;
    @Override
    public void onSearchTyped(String searchText) {
         this.searchText = searchText;
        if(searchText.isEmpty()){
            view.setToTable(objects);
            prev = -1;

        }else {
            if(searchText.length()<=prev || prev == -1 ) {
                int searchRes[] = new int[objects.length];
                for (int i = 0; i < objects.length; i++) {
                    if (String.valueOf((long) objects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }
                    if (String.valueOf((long) objects[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }
                    if (simpleDateFormat.format(new Date((long) objects[i][2])).contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }
                    if (((String) objects[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }

                    String text = "";
                    int orderStatus = (int) objects[i][4];
                    if (orderStatus == Order.CLOSED_ORDER) {
                        text = context.getString(R.string.order_status_closed);
                    } else if (orderStatus == Order.HOLD_ORDER) {
                        text = context.getString(R.string.order_status_held);
                    } else if (orderStatus == Order.CANCELED_ORDER) {
                        text = context.getString(R.string.order_status_canceled);
                    }
                    if (text.toUpperCase().contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }

                    if (((String) objects[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }

                    if (decimalFormat.format((double) objects[i][6]).contains(searchText)) {
                        searchRes[i] = 1;
                        continue;
                    }

                    if (decimalFormat.format((double) objects[i][7]).contains(searchText)) {
                        searchRes[i] = 1;
                        continue;
                    }
                }


                int sumSize = 0;
                for (int i = 0; i < objects.length; i++) {
                    if (searchRes[i] == 1)
                        sumSize++;
                }
                Object[][] objectResults = new Object[sumSize][9];

                int pt = 0;
                for (int i = 0; i < objects.length; i++) {
                    if (searchRes[i] == 1) {
                        objectResults[pt] = objects[i];
                        pt++;
                    }
                }
                searchResultsTemp = objectResults.clone();
                view.setToTableFromSearch(objectResults, searchText);
            }else {
                int searchRes[] = new int[searchResultsTemp.length];
                for (int i = 0; i < searchResultsTemp.length; i++) {
                    if (String.valueOf((long) searchResultsTemp[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }
                    if (String.valueOf((long) searchResultsTemp[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }
                    if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][2])).contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }
                    if (((String) searchResultsTemp[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }

                    String text = "";
                    int orderStatus = (int) searchResultsTemp[i][4];
                    if (orderStatus == Order.CLOSED_ORDER) {
                        text = context.getString(R.string.order_status_closed);
                    } else if (orderStatus == Order.HOLD_ORDER) {
                        text = context.getString(R.string.order_status_held);
                    } else if (orderStatus == Order.CANCELED_ORDER) {
                        text = context.getString(R.string.order_status_canceled);
                    }
                    if (text.toUpperCase().contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }

                    if (((String) searchResultsTemp[i][5]).toUpperCase().contains(searchText.toUpperCase())) {
                        searchRes[i] = 1;
                        continue;
                    }

                    if (decimalFormat.format((double) searchResultsTemp[i][6]).contains(searchText)) {
                        searchRes[i] = 1;
                        continue;
                    }

                    if (decimalFormat.format((double) searchResultsTemp[i][7]).contains(searchText)) {
                        searchRes[i] = 1;
                        continue;
                    }
                }


                int sumSize = 0;
                for (int i = 0; i < searchResultsTemp.length; i++) {
                    if (searchRes[i] == 1)
                        sumSize++;
                }
                Object[][] objectResults = new Object[sumSize][9];

                int pt = 0;
                for (int i = 0; i < searchResultsTemp.length; i++) {
                    if (searchRes[i] == 1) {
                        objectResults[pt] = searchResultsTemp[i];
                        pt++;
                    }
                }
                searchResultsTemp = objectResults.clone();
                view.setToTableFromSearch(objectResults, searchText);
            }
            prev = searchText.length();

        }
    }
    private void updateObejctsForTable(){
        List<Order> result = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            if(filterConfig[0]==1 && orders.get(i).getStatus() == Order.CLOSED_ORDER) {
                result.add(orders.get(i));
                continue;
            }
            if(filterConfig[1]==1 && orders.get(i).getStatus() == Order.HOLD_ORDER){
                result.add(orders.get(i));
                continue;
            }
            if(filterConfig[2]==1 && orders.get(i).getStatus() == Order.CANCELED_ORDER){
                result.add(orders.get(i));
                continue;
            }
        }
        Object[][] objectsTemp = new Object[result.size()][9];
        for (int i = 0; i < result.size(); i++){
            objectsTemp[i][0] = result.get(i).getId();
            objectsTemp[i][1] = result.get(i).getTillId();
            objectsTemp[i][2] = result.get(i).getCreateAt();
            if( result.get(i).getCustomer()!=null)
                objectsTemp[i][3] = result.get(i).getCustomer().getName();
            else
                objectsTemp[i][3] = "";
            objectsTemp[i][4] = result.get(i).getStatus();
            objectsTemp[i][5] = result.get(i).getLastChangeLog().getReason()==null?"":result.get(i).getLastChangeLog().getReason();
            objectsTemp[i][6] = result.get(i).getSubTotalValue();
            objectsTemp[i][7] = result.get(i).getForPayAmmount();
            objectsTemp[i][8] = context.getString(R.string.details);
        }
        this.objects = objectsTemp;

    }

    @Override
    public void onClickedDateInterval() {
        view.openDateInterval(fromDate,toDate);
    }

    @Override
    public void onActionClicked(Object[][] objects, int row, int column) {
        if(column==1){
            long tillId = (long) objects[row][1];
            Till till = databaseManager.getTillById(tillId).blockingGet();
            if (till.getStatus() == Till.CLOSED)
                view.openTillDetailsDialog(till);
            else view.onTillNotClosed();


        }else if(column == 8){
            long orderId = (long) objects[row][0];
            databaseManager.getOrder(orderId).subscribe((Order order, Throwable throwable) -> {
                view.openOrderDetialsDialog(order);
            });
        }
    }

    @Override
    public void filterConfigsChanged(int[] configs) {
        filterConfig = configs;
        updateObejctsForTable();
        view.setToTable(objects);
    }

    @Override
    public void exportExcel(String fileName, String path) {
        String filter = "";
        StringBuilder filters = new StringBuilder();
        if (filterConfig[0] == 1) {
            filters.append(context.getString(R.string.order_status_closed)).append(" ");
        }
        if (filterConfig[1] == 1) {
            filters.append(context.getString(R.string.order_status_held)).append(" ");
        }
        if (filterConfig[2] == 1) {
            filters.append(context.getString(R.string.order_status_canceled));
        }
        filter = filters.toString();
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        if (searchResultsTemp != null) {
            view.exportTableToExcel(fileName, path, searchResultsTemp, date, filter, searchText);
        } else
            view.exportTableToExcel(fileName, path, objects, date, filter, searchText);
    }

    @Override
    public void exportPdf(String fileName, String path) {
        String filter = "";
        StringBuilder filters = new StringBuilder();
        if (filterConfig[0] == 1) {
            filters.append(context.getString(R.string.order_status_closed)).append(" ");
        }
        if (filterConfig[1] == 1) {
            filters.append(context.getString(R.string.order_status_held)).append(" ");
        }
        if (filterConfig[2] == 1) {
            filters.append(context.getString(R.string.order_status_canceled));
        }
        filter = filters.toString();
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        if (searchResultsTemp != null) {
            view.exportTableToPdf(fileName, path, searchResultsTemp, date, filter, searchText);
        } else
            view.exportTableToPdf(fileName, path, objects, date, filter, searchText);
    }

    @Override
    public void exportExcelToUSB(String filename, UsbFile root) {
        String filter = "";
        StringBuilder filters = new StringBuilder();
        if (filterConfig[0] == 1) {
            filters.append(context.getString(R.string.order_status_closed)).append(" ");
        }
        if (filterConfig[1] == 1) {
            filters.append(context.getString(R.string.order_status_held)).append(" ");
        }
        if (filterConfig[2] == 1) {
            filters.append(context.getString(R.string.order_status_canceled));
        }
        filter = filters.toString();
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        if (searchResultsTemp != null) {
            view.exportExcelToUSB(filename, root, searchResultsTemp, date, filter, searchText);
        } else
            view.exportExcelToUSB(filename, root, objects, date, filter, searchText);
    }

    @Override
    public void exportPdfToUSB(String filename, UsbFile root) {
        String filter = "";
        StringBuilder filters = new StringBuilder();
        if (filterConfig[0] == 1) {
            filters.append(context.getString(R.string.order_status_closed)).append(" ");
        }
        if (filterConfig[1] == 1) {
            filters.append(context.getString(R.string.order_status_held)).append(" ");
        }
        if (filterConfig[2] == 1) {
            filters.append(context.getString(R.string.order_status_canceled));
        }
        filter = filters.toString();
        String date = simpleDateFormat.format(fromDate.getTime()) + " - " + simpleDateFormat.format(toDate.getTime());
        if (searchResultsTemp != null) {
            view.exportTableToPdfToUSB(filename, root, searchResultsTemp, date, filter, searchText);
        } else
            view.exportTableToPdfToUSB(filename, root, objects, date, filter, searchText);
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
        view.showFilterPanel(filterConfig);
    }

    @Override
    public void onChoisedPanel(int postion) {

    }

    @Override
    public void onTillPickerClicked() {

    }

    public void initDateInterval(){
        fromDate = new GregorianCalendar();
        fromDate.set(Calendar.HOUR,0);
        fromDate.set(Calendar.MINUTE,0);
        fromDate.set(Calendar.MILLISECOND,0);
        // init first last month
        fromDate.add(Calendar.MONTH,-1);

        toDate = new GregorianCalendar();
        toDate.set(Calendar.HOUR,23);
        toDate.set(Calendar.MINUTE,59);
        toDate.set(Calendar.MILLISECOND,999);
        view.updateDateIntervalUi(fromDate,toDate);
    }
    private void initDecimal(){
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat1.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat1.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat = decimalFormat1;
    }
}
