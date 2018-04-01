package com.jim.multipos.ui.reports.order_history;

import android.graphics.Color;
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

    int dataType[] = {ReportViewConstants.ID, ReportViewConstants.ACTION, ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.STATUS, ReportViewConstants.NAME, ReportViewConstants.AMOUNT,ReportViewConstants.AMOUNT,ReportViewConstants.ACTION};
    String titles[] = {"Order ID", "Till ID", "Closed time", "Customer", "Status", "Cancel Reason", "Sub Total","To Pay Total","Actions"};
    int weights[] = {9, 7, 14, 9, 9, 12, 12,12,10};
    Object[][] statusTypes = {{0, "Closed",R.color.colorGreen }, {1, "Held", R.color.colorBlue}, {2, "Canceled", R.color.colorRed}};
    int aligns[] = {Gravity.CENTER, Gravity.CENTER, Gravity.CENTER, Gravity.LEFT, Gravity.CENTER, Gravity.LEFT, Gravity.RIGHT,Gravity.RIGHT,Gravity.CENTER};


    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        presenter.onCreateView(savedInstanceState);
    }



    @Override
    public void initFakeDataForTable(){
        Object[][] objects = {
                {348,12L, System.currentTimeMillis(), "", 0,"",28050d,28050d,"detials"},
                {349,12L, System.currentTimeMillis()-24*60*60*60*1000, "Jonisbek Bektim", 2,"Edited to Order #356",48996d,48996d,"detials"},

        };


//        FrameLayout fl = new ReportView.Builder()
//                .setContext(getContext())
//                .setTitles(titles)
//                .setDataTypes(dataType)
//                .setWeight(weights)
//                .setDataAlignTypes(aligns)
//                .setObjects(objects)
//                .setDefaultSort(0)
//                .setStatusTypes(statusTypes)
//                .setOnReportViewResponseListener((relivantObjects,row, column) -> {
//
//                })
//                .build();
//
//        setTable(fl);
    }




    @Override
    public void showFilterPanel() {

    }
}
