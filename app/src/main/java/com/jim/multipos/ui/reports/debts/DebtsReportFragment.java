package com.jim.multipos.ui.reports.debts;

import android.os.Bundle;
import android.view.Gravity;

import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseTableReportFragment;

import javax.inject.Inject;

public class DebtsReportFragment extends BaseTableReportFragment implements DebtsReportView {

    @Inject
    DebtsReportPresenter presenter;
    private ReportView.Builder firstBuilder, secondBuilder, thirdBuilder, forthBuilder;
    private ReportView firstView, secondView, thirdView, forthView;
    private int firstDataType[] = {ReportViewConstants.NAME, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.DATE, ReportViewConstants.NAME};
    private int firstWeights[] = {1, 1, 1, 1, 1};
    private int firstAligns[] = {Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT, Gravity.CENTER, Gravity.LEFT};
    private int secondDataType[] = {ReportViewConstants.NAME, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.QUANTITY, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT};
    private int secondWeights[] = {1, 1, 1, 1, 1, 1};
    private int secondAligns[] = {Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT};
    private int thirdDataType[] = {ReportViewConstants.NAME, ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.STATUS, ReportViewConstants.AMOUNT, ReportViewConstants.NAME, ReportViewConstants.NAME};
    private int thirdWeights[] = {1, 1, 1, 1, 1, 1, 1};
    private int thirdAligns[] = {Gravity.LEFT, Gravity.CENTER, Gravity.CENTER, Gravity.CENTER, Gravity.RIGHT, Gravity.LEFT, Gravity.LEFT};
    private int forthDataType[] = {ReportViewConstants.NAME, ReportViewConstants.DATE, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.DATE, ReportViewConstants.AMOUNT, ReportViewConstants.NAME, ReportViewConstants.DATE};
    private int forthWeights[] = {1, 1, 1, 1, 1, 1, 1, 1};
    private int forthAligns[] = {Gravity.CENTER, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT, Gravity.CENTER, Gravity.RIGHT, Gravity.LEFT, Gravity.CENTER};

    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        disableFilter();
        disableDateIntervalPicker();
        setChoiserPanel(new String[]{getString(R.string.debts_list), getString(R.string.debt_summary), getString(R.string.debt_transactions_list), getString(R.string.orders_with_Debt)});
        initDefaults();
        presenter.onCreateView(savedInstanceState);
    }

    private void initDefaults() {
        Object[][][] statusTypes = new Object[][][]{
                {{0, getContext().getString(R.string.debt_taken), R.color.colorMainText},
                        {1, getContext().getString(R.string.debt_closed), R.color.colorBlue}}};
        String firstTitles[] = new String[]{getString(R.string.name), getString(R.string.total_debt), getString(R.string.total_overdue), getString(R.string.last_visit), getString(R.string.customer_contacts)};
        String secondTitles[] = new String[]{getString(R.string.name), getString(R.string.debt_taken), getString(R.string.debt_closed), getString(R.string.debt_orders_count), getString(R.string.debt_taken_avg), getString(R.string.debt_closed_avg)};
        String thirdTitles[] = new String[]{getString(R.string.name), getString(R.string.date), getString(R.string.order), getString(R.string.type), getString(R.string.amount), getString(R.string.payment_type), getString(R.string.group)};
        String forthTitles[] = new String[]{getString(R.string.order_num), getString(R.string.created_at), getString(R.string.order_amount), getString(R.string.paid_report_text), getString(R.string.last_pay_date), getString(R.string.due_debt), getString(R.string.customer), getString(R.string.should_close_date)};
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
                .setWeight(thirdWeights)
                .setStatusTypes(statusTypes)
                .setDataAlignTypes(thirdAligns)
                .build();
        thirdView = new ReportView(thirdBuilder);
        forthBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(forthTitles)
                .setDataTypes(forthDataType)
                .setWeight(forthWeights)
                .setDataAlignTypes(forthAligns)
                .build();
        forthView = new ReportView(forthBuilder);
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
                firstView.getBuilder().update(objects);
                setTable(firstView.getBuilder().getView());
                disableFilter();
                disableDateIntervalPicker();
                break;
            case 1:
                secondView.getBuilder().update(objects);
                setTable(secondView.getBuilder().getView());
                enableDateIntervalPicker();
                disableFilter();
                break;
            case 2:
                thirdView.getBuilder().update(objects);
                setTable(thirdView.getBuilder().getView());
                enableDateIntervalPicker();
                enableFilter();
                break;
            case 3:
                forthView.getBuilder().update(objects);
                setTable(forthView.getBuilder().getView());
                disableFilter();
                enableDateIntervalPicker();
                break;
        }
    }

    @Override
    public void setSearchResults(Object[][] searchResults, String searchText, int position) {
        switch (position) {
            case 0:
                firstView.getBuilder().searchResults(searchResults, searchText);
                setTable(firstView.getBuilder().getView());
                disableFilter();
                disableDateIntervalPicker();
                break;
            case 1:
                secondView.getBuilder().searchResults(searchResults, searchText);
                setTable(secondView.getBuilder().getView());
                enableDateIntervalPicker();
                disableFilter();
                break;
            case 2:
                thirdView.getBuilder().searchResults(searchResults, searchText);
                setTable(thirdView.getBuilder().getView());
                enableDateIntervalPicker();
                enableFilter();
                break;
            case 3:
                forthView.getBuilder().searchResults(searchResults, searchText);
                setTable(forthView.getBuilder().getView());
                disableFilter();
                enableDateIntervalPicker();
                break;
        }
    }
}
