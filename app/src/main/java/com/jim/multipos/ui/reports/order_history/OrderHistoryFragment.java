package com.jim.multipos.ui.reports.order_history;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseTableReportFragment;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.ui.reports.order_history.dialogs.OrderHistoryFilterDialog;
import com.jim.multipos.utils.CustomFilterDialog;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;

public class OrderHistoryFragment extends BaseTableReportFragment implements OrderHistoryView {
    @Inject
    OrderHistoryPresenter presenter;


    int dataType[] = {ReportViewConstants.ID, ReportViewConstants.ACTION, ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.STATUS, ReportViewConstants.NAME, ReportViewConstants.AMOUNT,ReportViewConstants.AMOUNT,ReportViewConstants.ACTION};
    String titles[] ;
    int weights[] = {9, 7, 12, 12, 9, 12, 12,12,10};
    Object[][][] statusTypes ;
    int aligns[] = {Gravity.CENTER, Gravity.CENTER, Gravity.CENTER, Gravity.LEFT, Gravity.CENTER, Gravity.LEFT, Gravity.RIGHT,Gravity.RIGHT,Gravity.CENTER};
    FrameLayout fl;
    ReportView reportView;
    ReportView.Builder builder;

    @Override
    protected void init(Bundle savedInstanceState) {
        initDefaults();
        init(presenter);
        builder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(titles)
                .setDataTypes(dataType)
                .setWeight(weights)
                .setDataAlignTypes(aligns)
                .setDefaultSort(0)
                .setStatusTypes(statusTypes)
                .setOnReportViewResponseListener((relivantObjects,row, column) -> {
                    presenter.onActionClicked(relivantObjects,row,column);
                })
                .build();
        reportView = new ReportView(builder);
        presenter.onCreateView(savedInstanceState);
        setSingleTitle(getContext().getString(R.string.order_history_report_title));
    }



    @Override
    public void setToTable(Object[][] toTable){
        reportView.getBuilder().update(toTable);
        fl = reportView.getBuilder().getView();
        setTable(fl);
    }

    @Override
    public void setToTableFromSearch(Object[][] toTable, String searchedText) {
        reportView.getBuilder().searchResults(toTable, searchedText);
        fl = reportView.getBuilder().getView();
        setTable(fl);
    }


    @Override
    public void showFilterPanel(int[] config) {
        OrderHistoryFilterDialog orderHistoryFilterDialog = new OrderHistoryFilterDialog(getContext(), config, config1 -> {
            presenter.filterConfigsChanged(config1);
            clearSearch();
        });
        orderHistoryFilterDialog.show();
    }

    private void initDefaults(){
        statusTypes = new Object[][][] {
                {{Order.CLOSED_ORDER, getString(R.string.order_status_closed),R.color.colorGreen },
                {Order.HOLD_ORDER, getString(R.string.order_status_held), R.color.colorBlue},
                {Order.CANCELED_ORDER, getString(R.string.order_status_canceled), R.color.colorRed}}
        };
        titles = new String[] {"Order ID", "Till ID", "Closed time", "Customer", "Status", "Cancel Reason", "Sub Total","To Pay Total","Actions"};
    }
}
