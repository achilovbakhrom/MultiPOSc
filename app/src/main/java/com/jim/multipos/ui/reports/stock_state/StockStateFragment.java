package com.jim.multipos.ui.reports.stock_state;

import android.os.Bundle;
import android.view.Gravity;

import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseTableReportFragment;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.data.db.model.customer.CustomerPayment;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.ui.reports.customers.model.CustomerGroupOrder;
import com.jim.multipos.ui.reports.customers.model.CustomerPaymentLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class StockStateFragment extends BaseTableReportFragment implements StockStateView {
    @Inject
    StockStatePresenter presenter;

    private ReportView.Builder firstBuilder, secondBuilder, thirdBuilder;
    private ReportView firstView, secondView, thirdView;
    private int firstDataType[] = {ReportViewConstants.NAME,  ReportViewConstants.QUANTITY, ReportViewConstants.NAME, ReportViewConstants.AMOUNT};
    private int firstWeights[] = {10, 10, 5, 15};
    private int firstAligns[] = { Gravity.LEFT, Gravity.CENTER, Gravity.LEFT, Gravity.RIGHT};
    private int secondDataType[] = {ReportViewConstants.NAME,ReportViewConstants.NAME,  ReportViewConstants.QUANTITY, ReportViewConstants.NAME, ReportViewConstants.AMOUNT};
    private int secondWeights[] = {10, 10, 10, 5, 15};
    private int secondAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.CENTER, Gravity.LEFT, Gravity.RIGHT};
    private int thirdDataType[] = {ReportViewConstants.NAME, ReportViewConstants.DATE, ReportViewConstants.DATE, ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.QUANTITY,ReportViewConstants.NAME };
    private int thirdWeights[] = {14, 10, 10, 10, 13, 8, 5};
    private int thirdAligns[] = {Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.LEFT, Gravity.CENTER , Gravity.LEFT};
    private String firstTitles[], secondTitles[], thirdTitles[];

    private String panelNames[];
    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        disableDateIntervalPicker();
        disableFilter();
        panelNames = new String[]{"Inventory", "Vendor Inventory", "Expired Products"};
        setChoiserPanel(panelNames);
        initDefaults();
        presenter.onCreateView(savedInstanceState);
    }

    private void initDefaults() {
        firstTitles = new String[]{"Product","Available","Unit","Summary Cost (uzs)"};
        secondTitles = new String[]{"Product", "Vendor","Available","Unit","Summary Cost (uzs)"};
        thirdTitles = new String[]{"Product", "Created Date","Income Date","Expired Date","Left Date","Count", "Unit"};

        firstBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(firstTitles)
                .setDataTypes(firstDataType)
                .setWeight(firstWeights)
                .setDataAlignTypes(firstAligns)
                .build();
        firstView = new ReportView(firstBuilder);
        secondBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(secondTitles)
                .setDataTypes(secondDataType)
                .setWeight(secondWeights)
                .setDataAlignTypes(secondAligns)
                .build();
        secondView = new ReportView(secondBuilder);
        thirdBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(thirdTitles)
                .setDataTypes(thirdDataType)
                .setDefaultSort(3) // EXPIRED DATE
                //TODO default sort by ASC DESC ->
                .setWeight(thirdWeights)
                .setDataAlignTypes(thirdAligns)
                .build();
        thirdView = new ReportView(thirdBuilder);

    }


    @Override
    public void initTable(Object[][] objects) {
        firstView.getBuilder().init(objects);
        setTable(firstView.getBuilder().getView());
    }


    @Override
    public void updateTable(Object[][] objects, int position) {
        switch (position) {
            case 0:
                clearSearch();
                firstView.getBuilder().update(objects);
                setTable(firstView.getBuilder().getView());
                break;
            case 1:
                clearSearch();
                secondView.getBuilder().update(objects);
                setTable(secondView.getBuilder().getView());
                break;
            case 2:
                clearSearch();
                thirdView.getBuilder().update(objects);
                setTable(thirdView.getBuilder().getView());
                break;
        }
    }

    @Override
    public void setSearchResults(Object[][] searchResults, String searchText, int position) {
        switch (position) {
            case 0:
                firstView.getBuilder().searchResults(searchResults, searchText);
                setTable(firstView.getBuilder().getView());
                break;
            case 1:
                secondView.getBuilder().searchResults(searchResults, searchText);
                setTable(secondView.getBuilder().getView());
                break;
            case 2:
                thirdView.getBuilder().searchResults(searchResults, searchText);
                setTable(thirdView.getBuilder().getView());
                break;
        }
    }

    @Override
    public void showFilterDialog(int[] filterConfig) {

    }

    @Override
    public void showSummaryFilterDialog(int filterValue) {

    }
}
