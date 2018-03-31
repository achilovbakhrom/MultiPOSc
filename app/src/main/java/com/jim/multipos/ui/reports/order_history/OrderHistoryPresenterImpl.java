package com.jim.multipos.ui.reports.order_history;

import android.os.Bundle;
import android.util.Log;

import com.jim.multipos.core.BasePresenterImpl;

import java.util.Calendar;

import javax.inject.Inject;

public class OrderHistoryPresenterImpl extends BasePresenterImpl<OrderHistoryView> implements OrderHistoryPresenter {

    Calendar fromDate, toDate;
    private OrderHistoryView view;

    @Inject
    protected OrderHistoryPresenterImpl(OrderHistoryView orderHistoryView) {
        super(orderHistoryView);
        this.view = orderHistoryView;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        view.initFakeDataForTable();
        view.setChoiserPanel(new String[] {"Sales summary","Till manage logs","Account summary"});

    }

    @Override
    public void onChooseDateInterval(Calendar fromDate, Calendar toDate) {
        //TODO
        this.fromDate = fromDate;
        this.toDate = toDate;

        view.updateDateIntervalUi(fromDate,toDate);
    }

    @Override
    public void onSearchTyped(String searchText) {
        Log.d("testtts", "onSearchTyped: "+searchText);
    }

    @Override
    public void onClickedDateInterval() {
        view.openDateInterval(fromDate,toDate);
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
}
