package com.jim.multipos.ui.reports.discount;

import android.content.Context;
import android.os.Bundle;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.DiscountLog;
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
    private final Context context;
    private int[] filterConfig;

    @Inject
    protected DiscountReportPresenterImpl(DiscountReportView view, DatabaseManager databaseManager, Context context) {
        super(view);
        this.databaseManager = databaseManager;
        this.context = context;
        DecimalFormat decimalFormat1 = new DecimalFormat("0.00");
        DecimalFormatSymbols decimalFormatSymbols = decimalFormat1.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormat1.setDecimalFormatSymbols(decimalFormatSymbols);
        decimalFormat = decimalFormat1;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        filterConfig = new int[]{1, 1};
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
                for (Order order : orders1) {
                    for (int i = 0; i < order.getOrderProducts().size(); i++) {
                        if (order.getOrderProducts().get(i).getDiscount() != null) {
                            if (filterConfig[0] == 1 && !order.getOrderProducts().get(i).getDiscount().getIsManual()) {
                                orderProducts.add(order.getOrderProducts().get(i));
                                tillIds.add(order.getTillId());
                                orderDates.add(order.getCreateAt());
                                continue;
                            }
                            if (filterConfig[1] == 1 && order.getOrderProducts().get(i).getDiscount().getIsManual()) {
                                orderProducts.add(order.getOrderProducts().get(i));
                                tillIds.add(order.getTillId());
                                orderDates.add(order.getCreateAt());
                                continue;
                            }

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
                firstObjects[i][5] = orderProduct.getDiscountAmount() * -1;
                firstObjects[i][6] = orderDates.get(i);
                firstObjects[i][7] = orderProduct.getDiscount().getName();
                firstObjects[i][8] = orderProduct.getDiscount().getIsManual() ? 1 : 0;
            }
            view.updateTable(firstObjects, currentPosition);

        } else if (currentPosition == 1) {
            List<Order> orders = new ArrayList<>();
            databaseManager.getClosedOrdersInIntervalForReport(fromDate, toDate).subscribe(orders1 -> {
                for (Order order : orders1) {
                    if (order.getDiscount() != null) {
                        if (filterConfig[0] == 1 && !order.getDiscount().getIsManual()) {
                            orders.add(order);
                            continue;
                        }
                        if (filterConfig[1] == 1 && order.getDiscount().getIsManual()) {
                            orders.add(order);
                            continue;
                        }
                    }
                }
            });
            secondObjects = new Object[orders.size()][8];
            for (int i = 0; i < orders.size(); i++) {
                Order order = orders.get(i);
                secondObjects[i][0] = "#" + order.getId();
                secondObjects[i][1] = "#" + order.getTillId();
                secondObjects[i][2] = order.getSubTotalValue();
                secondObjects[i][3] = order.getCustomer() != null ? order.getCustomer().getName() : "";
                secondObjects[i][4] = order.getDiscountAmount() * -1;
                secondObjects[i][5] = order.getCreateAt();
                secondObjects[i][6] = order.getDiscount().getName();
                secondObjects[i][7] = order.getDiscount().getIsManual() ? 1 : 0;
            }
            view.updateTable(secondObjects, currentPosition);
        } else if (currentPosition == 2) {
            List<DiscountLog> discountLogs = new ArrayList<>();
            databaseManager.getDiscountLogs().subscribe(discountLogs1 -> {
                for (DiscountLog log : discountLogs1) {
                    if (filterConfig[0] == 1 && !log.getDiscount().getIsManual()) {
                        discountLogs.add(log);
                        continue;
                    }
                    if (filterConfig[1] == 1 && log.getDiscount().getIsManual()) {
                        discountLogs.add(log);
                        continue;
                    }
                }
            });
            thirdObjects = new Object[discountLogs.size()][7];
            for (int i = 0; i < discountLogs.size(); i++) {
                DiscountLog log = discountLogs.get(i);
                thirdObjects[i][0] = log.getChangeDate();
                thirdObjects[i][1] = log.getStatus();
                thirdObjects[i][2] = log.getDiscount().getName();
                thirdObjects[i][3] = log.getDiscount().getAmount();
                thirdObjects[i][4] = log.getDiscount().getAmountType();
                thirdObjects[i][5] = log.getDiscount().getUsedType();
                thirdObjects[i][6] = log.getDiscount().getIsManual() ? 1 : 0;
            }
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
    private int prev = -1;
    private Object[][] searchResultsTemp;

    @Override
    public void onSearchTyped(String searchText) {
        if (searchText.isEmpty()) {
            switch (currentPosition) {
                case 0:
                    view.updateTable(firstObjects, currentPosition);
                    break;
                case 1:
                    view.updateTable(secondObjects, currentPosition);
                    break;
                case 2:
                    view.updateTable(thirdObjects, currentPosition);
                    break;
            }
            prev = -1;

        } else {
            switch (currentPosition) {
                case 0:
                    if (searchText.length() <= prev || prev == -1) {
                        int searchRes[] = new int[firstObjects.length];
                        for (int i = 0; i < firstObjects.length; i++) {
                            if (((String) firstObjects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) firstObjects[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) firstObjects[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) firstObjects[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (decimalFormat.format((double) firstObjects[i][4]).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (decimalFormat.format((double) firstObjects[i][5]).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) firstObjects[i][6])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) firstObjects[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int orderStatus = (int) firstObjects[i][8];
                            if (orderStatus == 0) {
                                text = context.getString(R.string.static_type);
                            } if (orderStatus == 1) {
                                text = context.getString(R.string.manual);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < firstObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][6];

                        int pt = 0;
                        for (int i = 0; i < firstObjects.length; i++) {
                            if (searchRes[i] == 1) {
                                objectResults[pt] = firstObjects[i];
                                pt++;
                            }
                        }
                        searchResultsTemp = objectResults.clone();
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    } else {
                        int searchRes[] = new int[searchResultsTemp.length];
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (((String) searchResultsTemp[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (decimalFormat.format((double) searchResultsTemp[i][4]).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (decimalFormat.format((double) searchResultsTemp[i][5]).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][6])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][7]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int orderStatus = (int) searchResultsTemp[i][8];
                            if (orderStatus == 0) {
                                text = context.getString(R.string.static_type);
                            } else if (orderStatus == 1) {
                                text = context.getString(R.string.manual);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }


                        int sumSize = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][6];

                        int pt = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1) {
                                objectResults[pt] = searchResultsTemp[i];
                                pt++;
                            }
                        }
                        searchResultsTemp = objectResults.clone();
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    }
                    prev = searchText.length();
                    break;
                case 2:
                    if (searchText.length() <= prev || prev == -1) {
                        int searchRes[] = new int[thirdObjects.length];
                        for (int i = 0; i < thirdObjects.length; i++) {
                            if (simpleDateFormat.format(new Date((long) thirdObjects[i][0])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int orderStatus = (int) thirdObjects[i][1];
                            if (orderStatus == DiscountLog.DISCOUNT_ADDED) {
                                text = context.getString(R.string.add);
                            } else if (orderStatus == DiscountLog.DISCOUNT_CANCELED) {
                                text = context.getString(R.string.canceled);
                            } else if (orderStatus == DiscountLog.DISCOUNT_DELETED) {
                                text = context.getString(R.string.deleted);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) thirdObjects[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (decimalFormat.format((double) thirdObjects[i][3]).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String amountTypeText = "";
                            int amountType = (int) thirdObjects[i][4];
                            if (amountType == Discount.PERCENT) {
                                amountTypeText = context.getString(R.string.percentage);
                            } else if (amountType == Discount.VALUE) {
                                amountTypeText = context.getString(R.string.value);
                            }
                            if (amountTypeText.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String usageTypeText = "";
                            int usageType = (int) thirdObjects[i][5];
                            if (usageType == Discount.ORDER) {
                                usageTypeText = context.getString(R.string.order);
                            } else if (usageType == Discount.ITEM) {
                                usageTypeText = context.getString(R.string.item);
                            }
                            if (usageTypeText.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String discountText = "";
                            int discountType = (int) thirdObjects[i][6];
                            if (discountType == 0) {
                                discountText = context.getString(R.string.static_type);
                            } if (discountType == 1) {
                                discountText = context.getString(R.string.manual);
                            }
                            if (discountText.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < thirdObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][6];

                        int pt = 0;
                        for (int i = 0; i < thirdObjects.length; i++) {
                            if (searchRes[i] == 1) {
                                objectResults[pt] = thirdObjects[i];
                                pt++;
                            }
                        }
                        searchResultsTemp = objectResults.clone();
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    } else {
                        int searchRes[] = new int[searchResultsTemp.length];
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][0])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int orderStatus = (int) searchResultsTemp[i][1];
                            if (orderStatus == DiscountLog.DISCOUNT_ADDED) {
                                text = context.getString(R.string.add);
                            } else if (orderStatus == DiscountLog.DISCOUNT_CANCELED) {
                                text = context.getString(R.string.canceled);
                            } else if (orderStatus == DiscountLog.DISCOUNT_DELETED) {
                                text = context.getString(R.string.deleted);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][2]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (decimalFormat.format((double) searchResultsTemp[i][3]).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String amountTypeText = "";
                            int amountType = (int) searchResultsTemp[i][4];
                            if (amountType == Discount.PERCENT) {
                                amountTypeText = context.getString(R.string.percentage);
                            } else if (amountType == Discount.VALUE) {
                                amountTypeText = context.getString(R.string.value);
                            }
                            if (amountTypeText.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String usageTypeText = "";
                            int usageType = (int) searchResultsTemp[i][5];
                            if (usageType == Discount.ORDER) {
                                usageTypeText = context.getString(R.string.order);
                            } else if (usageType == Discount.ITEM) {
                                usageTypeText = context.getString(R.string.item);
                            }
                            if (usageTypeText.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String discountText = "";
                            int discountType = (int) searchResultsTemp[i][6];
                            if (discountType == 0) {
                                discountText = context.getString(R.string.static_type);
                            } if (discountType == 1) {
                                discountText = context.getString(R.string.manual);
                            }
                            if (discountText.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }


                        int sumSize = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][6];

                        int pt = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1) {
                                objectResults[pt] = searchResultsTemp[i];
                                pt++;
                            }
                        }
                        searchResultsTemp = objectResults.clone();
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    }
                    prev = searchText.length();
                    break;
                case 1:
                    if (searchText.length() <= prev || prev == -1) {
                        int searchRes[] = new int[secondObjects.length];
                        for (int i = 0; i < secondObjects.length; i++) {
                            if (((String) secondObjects[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) secondObjects[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (decimalFormat.format((double) secondObjects[i][2]).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) secondObjects[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (decimalFormat.format((double) secondObjects[i][4]).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) secondObjects[i][5])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) secondObjects[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int orderStatus = (int) secondObjects[i][7];
                            if (orderStatus == 0) {
                                text = context.getString(R.string.static_type);
                            } if (orderStatus == 1) {
                                text = context.getString(R.string.manual);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }

                        int sumSize = 0;
                        for (int i = 0; i < secondObjects.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][6];

                        int pt = 0;
                        for (int i = 0; i < secondObjects.length; i++) {
                            if (searchRes[i] == 1) {
                                objectResults[pt] = secondObjects[i];
                                pt++;
                            }
                        }
                        searchResultsTemp = objectResults.clone();
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    } else {
                        int searchRes[] = new int[searchResultsTemp.length];
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (((String) searchResultsTemp[i][0]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][1]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (decimalFormat.format((double) searchResultsTemp[i][2]).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][3]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (decimalFormat.format((double) searchResultsTemp[i][4]).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (simpleDateFormat.format(new Date((long) searchResultsTemp[i][5])).contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            if (((String) searchResultsTemp[i][6]).toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                            String text = "";
                            int orderStatus = (int) searchResultsTemp[i][7];
                            if (orderStatus == 0) {
                                text = context.getString(R.string.static_type);
                            } if (orderStatus == 1) {
                                text = context.getString(R.string.manual);
                            }
                            if (text.toUpperCase().contains(searchText.toUpperCase())) {
                                searchRes[i] = 1;
                                continue;
                            }
                        }


                        int sumSize = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1)
                                sumSize++;
                        }
                        Object[][] objectResults = new Object[sumSize][6];

                        int pt = 0;
                        for (int i = 0; i < searchResultsTemp.length; i++) {
                            if (searchRes[i] == 1) {
                                objectResults[pt] = searchResultsTemp[i];
                                pt++;
                            }
                        }
                        searchResultsTemp = objectResults.clone();
                        view.setSearchResults(objectResults, searchText, currentPosition);
                    }
                    prev = searchText.length();
                    break;
            }
        }
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
        view.showFilterDialog(filterConfig);
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

    @Override
    public void filterConfigsHaveChanged(int[] config) {
        this.filterConfig = config;
        initReportContent(currentPosition);
    }
}
