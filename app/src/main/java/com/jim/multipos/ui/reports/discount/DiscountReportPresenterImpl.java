package com.jim.multipos.ui.reports.discount;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;

import java.util.Calendar;

import javax.inject.Inject;

public class DiscountReportPresenterImpl extends BasePresenterImpl<DiscountReportView> implements DiscountReportPresenter {

    private Object[][] objects = new Object[][]{};

    @Inject
    protected DiscountReportPresenterImpl(DiscountReportView view, DatabaseManager databaseManager) {
        super(view);
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        view.initTable(objects);
    }

    @Override
    public void onChooseDateInterval(Calendar fromDate, Calendar toDate) {

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
    public void onChoisedPanel(int postion) {
        switch (postion) {
            case 0:
                view.updateTable(objects, postion);
                break;
            case 1:
                view.updateTable(objects, postion);
                break;
            case 2:
                view.updateTable(objects, postion);
                break;
        }
    }
}
