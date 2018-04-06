package com.jim.multipos.ui.reports.product_profit;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseTableReportFragment;
import com.jim.multipos.ui.reports.product_profit.dialog.ProductProfitFilterDialog;

import javax.inject.Inject;

public class ProductProfitFragment extends BaseTableReportFragment implements ProductProfitView {
    @Inject
    ProductProfitPresenter presenter;
    private FrameLayout fl;

    private ReportView.Builder profitSummaryBuilder,profitLogBuilder;
    private ReportView profitSummaryView,profitLogView;

    int profitSumType[] = {ReportViewConstants.NAME, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT,ReportViewConstants.AMOUNT};
    String profitSumTitles[] ;
    int profitSumWeights[] = {10,10,10,10,10,10,10,10};
    int profitSumAligns[] = {Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT,Gravity.RIGHT};

    int profitLogType[] = {ReportViewConstants.NAME, ReportViewConstants.DATE, ReportViewConstants.ACTION, ReportViewConstants.QUANTITY, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT,ReportViewConstants.NAME};
    String profitLogTitles[] ;
    int profitLogWeights[] = {10,10,6,10,10,10,10,10};
    int profitLogAligns[] = {Gravity.LEFT, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT, Gravity.RIGHT,Gravity.LEFT};


    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        setChoiserPanel(getContext().getResources().getStringArray(R.array.product_profit_titles));
        initTableConfigs(getContext());
        profitSummaryBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(profitSumTitles)
                .setDataTypes(profitSumType)
                .setWeight(profitSumWeights)
                .setDataAlignTypes(profitSumAligns)
                .setDefaultSort(0)
                .build();
        profitLogBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(profitLogTitles)
                .setDataTypes(profitLogType)
                .setWeight(profitLogWeights)
                .setDataAlignTypes(profitLogAligns)
                .setDefaultSort(1)
                .setOnReportViewResponseListener((objects1, row, column) -> {

                })
                .build();
        profitSummaryView = new ReportView(profitSummaryBuilder);
        profitLogView = new ReportView(profitLogBuilder);
        presenter.onCreateView(savedInstanceState);

    }

    private void initTableConfigs(Context context){
        profitSumTitles = context.getResources().getStringArray(R.array.product_profit_summary_table_titles);
        profitLogTitles = context.getResources().getStringArray(R.array.product_profit_log_table_titles);
    }

    @Override
    public void initTable(Object[][] objects, int position) {
        switch (position){
            case 0:
                profitSummaryView.getBuilder().init(objects);
                fl = profitSummaryView.getBuilder().getView();
                setTable(fl);
                enableFilter();
                clearSearch();
                break;
            case 1:
                profitLogView.getBuilder().init(objects);
                fl = profitLogView.getBuilder().getView();
                setTable(fl);
                disableFilter();
                clearSearch();
                break;
        }

    }

    @Override
    public void updateTable(Object[][] objects, int position) {
        switch (position) {
            case 0:
                profitSummaryView.getBuilder().update(objects);
                fl = profitSummaryView.getBuilder().getView();
                enableFilter();
                clearSearch();
                break;
            case 1:
                profitLogView.getBuilder().update(objects);
                fl = profitLogView.getBuilder().getView();
                disableFilter();
                clearSearch();
        }
        setTable(fl);
        }

    @Override
    public void searchTable(Object[][] objects, int position, String searchtext) {
        switch (position) {
            case 0:
                profitSummaryView.getBuilder().searchResults(objects,searchtext);
                fl = profitSummaryView.getBuilder().getView();
                enableFilter();

                break;
            case 1:
                profitLogView.getBuilder().searchResults(objects,searchtext);
                fl = profitLogView.getBuilder().getView();
                disableFilter();
        }
        setTable(fl);
    }

    @Override
    public void showFilterPanel(int config) {
        ProductProfitFilterDialog productProfitFilterDialog = new ProductProfitFilterDialog(getContext(), config, config1 -> {
            presenter.filterConfigsChanged(config1);
            clearSearch();
        });
        productProfitFilterDialog.show();
    }
}
