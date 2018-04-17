package com.jim.multipos.ui.reports.payments;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseTableReportFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.ui.reports.order_history.dialogs.OrderDetialsDialog;
import com.jim.multipos.ui.reports.payments.dialog.PaymentsFilterDialog;
import com.jim.multipos.ui.reports.tills.dialog.TillDetailsDialog;

import javax.inject.Inject;

public class PaymentsReportFragment extends BaseTableReportFragment implements PaymentsReportView {
    @Inject
    PaymentsReportPresenter presenter;
    private FrameLayout fl;

    private ReportView.Builder summaryBuilder,detailsBuilder;
    private ReportView summaryView,detailsView;

    int summaryType[] = {ReportViewConstants.NAME, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT};
    String summaryTitles[] ;
    int summaryWeights[] = {10,10,8};
    int summaryAligns[] = {Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT};

    int detailsType[] = {ReportViewConstants.NAME,ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.ACTION, ReportViewConstants.ACTION, ReportViewConstants.DATE, ReportViewConstants.AMOUNT};
    String detailsTitles[] ;
    int detailsWeights[] = {10,10,10,8,8,10,10};
    int detailsAligns[] = {Gravity.LEFT, Gravity.LEFT,Gravity.LEFT, Gravity.RIGHT, Gravity.RIGHT, Gravity.CENTER, Gravity.RIGHT};


    @Override
    protected void init(Bundle savedInstanceState) {
        initTableConfigs(getContext());
        init(presenter);
        setChoiserPanel(getContext().getResources().getStringArray(R.array.payments_report));
        summaryBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(summaryTitles)
                .setDataTypes(summaryType)
                .setWeight(summaryWeights)
                .setDataAlignTypes(summaryAligns)
                .setDefaultSort(2)
                .build();
        detailsBuilder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(detailsTitles)
                .setDataTypes(detailsType)
                .setWeight(detailsWeights)
                .setDataAlignTypes(detailsAligns)
                .setDefaultSort(5)
                .setOnReportViewResponseListener((objects, row, column) -> {
                    presenter.onActionPressed(objects,row,column);
                })
                .build();
        summaryView = new ReportView(summaryBuilder);
        detailsView = new ReportView(detailsBuilder);
        presenter.onCreateView(savedInstanceState);

    }
    private void initTableConfigs(Context context){
        summaryTitles = context.getResources().getStringArray(R.array.payments_report_summary_title);
        detailsTitles = context.getResources().getStringArray(R.array.payments_report_details_title);
    }

    @Override
    public void initTable(Object[][] objects, int position) {
        switch (position){
            case 0:
                summaryView.getBuilder().init(objects);
                fl = summaryView.getBuilder().getView();
                setTable(fl);
                disableFilter();
                clearSearch();
                break;
            case 1:
                detailsView.getBuilder().init(objects);
                fl = detailsView.getBuilder().getView();
                setTable(fl);
                enableFilter();
                clearSearch();
                break;
        }
    }

    @Override
    public void updateTable(Object[][] objects, int position) {
        switch (position) {
            case 0:
                summaryView.getBuilder().update(objects);
                fl = summaryView.getBuilder().getView();
                disableFilter();
                clearSearch();
                break;
            case 1:
                detailsView.getBuilder().update(objects);
                fl = detailsView.getBuilder().getView();
                enableFilter();
                clearSearch();
        }
        setTable(fl);
    }

    @Override
    public void searchTable(Object[][] objects, int position, String searchtext) {
        switch (position) {
            case 0:
                summaryView.getBuilder().searchResults(objects,searchtext);
                fl = summaryView.getBuilder().getView();
                disableFilter();

                break;
            case 1:
                detailsView.getBuilder().searchResults(objects,searchtext);
                fl = detailsView.getBuilder().getView();
                enableFilter();

        }
        setTable(fl);
    }

    @Override
    public void showFilterPanel(int[] config) {
        PaymentsFilterDialog paymentsFilterDialog = new PaymentsFilterDialog(getContext(), config, config1 -> {
            presenter.filterConfigsChanged(config1);
            clearSearch();
        });
        paymentsFilterDialog.show();
    }

    @Override
    public void onOrderPressed(Order order) {
        OrderDetialsDialog orderDetialsDialog = new OrderDetialsDialog(getContext(),order);
        orderDetialsDialog.show();
    }

    @Override
    public void onTillPressed(DatabaseManager databaseManager, Till till) {
        TillDetailsDialog dialog = new TillDetailsDialog(getContext(), databaseManager, till);
        dialog.show();
    }

    @Override
    public void onTillNotClosed() {
        Toast.makeText(getActivity(), R.string.till_not_closed, Toast.LENGTH_SHORT).show();
    }
}
