package com.jim.multipos.ui.reports.hourly_sales;

import android.os.Bundle;
import android.view.Gravity;

import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseTableReportFragment;

import javax.inject.Inject;

public class HourlySalesReportFragment extends BaseTableReportFragment implements HourlySalesReportView {

    @Inject
    HourlySalesReportPresenter presenter;
    private ReportView.Builder builder;
    private ReportView reportView;
    private String titles[];
    private int dataType[] = {
            ReportViewConstants.NAME, ReportViewConstants.QUANTITY, ReportViewConstants.QUANTITY, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.NAME};
    private int weights[] = {1, 2, 2, 2, 2, 1};
    private int aligns[] = {Gravity.CENTER, Gravity.CENTER, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT, Gravity.CENTER};

    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        disableFilter();
        disableSearch();
        initDefaults();
        setSingleTitle(getString(R.string.hourly_sales_report));
        builder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(titles)
                .setDataTypes(dataType)
                .setWeight(weights)
                .setDataAlignTypes(aligns)
                .build();
        reportView = new ReportView(builder);
        presenter.onCreateView(savedInstanceState);
    }

    @Override
    public void initTable(Object[][] objects) {
        reportView.getBuilder().init(objects);
        setTable(reportView.getBuilder().getView());
    }

    @Override
    public void updateTable(Object[][] objects) {
        reportView.getBuilder().update(objects);
        setTable(reportView.getBuilder().getView());
    }

    private void initDefaults() {
        titles = new String[]{getContext().getString(R.string.time_interval), getContext().getString(R.string.transactions), getContext().getString(R.string.sold_items), getContext().getString(R.string.avg_sales), getContext().getString(R.string.sales_summary), getContext().getString(R.string.percent_sales)};
    }
}
