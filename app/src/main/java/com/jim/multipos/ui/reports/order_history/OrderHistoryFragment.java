package com.jim.multipos.ui.reports.order_history;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseTableReportFragment;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;

public class OrderHistoryFragment extends BaseTableReportFragment implements OrderHistoryView {
    @Inject
    OrderHistoryPresenter presenter;


    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        presenter.onCreateView(savedInstanceState);
    }



    @Override
    public void initFakeDataForTable(){
        Object[][] objects = {
                {1, "Cristiano Ronaldo Da Silva, Real Madrid CF, Madrid, Spain", 253.236d, 3, System.currentTimeMillis(), "#324", 0},
                {2, "Chris", 6520.08d, 5, System.currentTimeMillis(), "#325", 1},
                {3, "Adam", 1520.333d, 2, System.currentTimeMillis(), "#326", 2},
                {4, "Linkin Park", 2365.23d, 2, System.currentTimeMillis(), "#327", 0}};

        int dataType[] = {ReportViewConstants.ID, ReportViewConstants.NAME, ReportViewConstants.AMOUNT, ReportViewConstants.QUANTITY, ReportViewConstants.DATE, ReportViewConstants.ACTION, ReportViewConstants.STATUS};
        String titles[] = {"Id", "Name", "Amount", "Count", "Date", "Action", "Status"};
        int weights[] = {1, 3, 2, 2, 3, 2, 2};
        Object[][] statusTypes = {{0, "Cancel", R.color.colorRed}, {1, "Close", R.color.colorGreen}, {2, "Hold", R.color.colorBlue}};
        int aligns[] = {Gravity.CENTER, Gravity.LEFT, Gravity.CENTER, Gravity.RIGHT, Gravity.CENTER, Gravity.CENTER, Gravity.CENTER};


        FrameLayout fl = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(titles)
                .setDataTypes(dataType)
                .setWeight(weights)
                .setDataAlignTypes(aligns)
                .setObjects(objects)
                .setDefaultSort(0)
                .setStatusTypes(statusTypes)
                .setOnReportViewResponseListener((relivantObjects,row, column) -> {

                })
                .build();

        setTable(fl);
    }




}
