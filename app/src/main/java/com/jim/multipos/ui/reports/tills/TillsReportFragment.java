package com.jim.multipos.ui.reports.tills;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.core.BaseTableReportFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.till.Till;

import java.text.DecimalFormat;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 27.03.2018.
 */

public class TillsReportFragment extends BaseTableReportFragment implements TillsReportView {

    @Inject
    TillsReportPresenter presenter;
    @Inject
    DatabaseManager databaseManager;
    @Inject
    DecimalFormat decimalFormat;

    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        presenter.onCreateView(savedInstanceState);
        disableFilter();
        setSingleTitle("Till Reports");
    }

    @Override
    public void fillReportView(Object[][] objects) {
        int dataType[] = {
                ReportViewConstants.ID, ReportViewConstants.DATE, ReportViewConstants.DATE, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.ACTION};
        int weights[] = {1, 2, 2, 2, 2, 1};
        int aligns[] = {Gravity.RIGHT, Gravity.CENTER, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT, Gravity.CENTER};
        String titles[] = {"Till ID", "Opened Time", "Closed Time", "Start Money Variance", "Till Amount Variance", "Action"};
        FrameLayout fl = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(titles)
                .setDataTypes(dataType)
                .setWeight(weights)
                .setDataAlignTypes(aligns)
                .setObjects(objects)
                .setOnReportViewResponseListener((objects1, row, column) -> {
                    presenter.openTillDetailsDialog(objects1, row, column);
                })
                .build();
        setTable(fl);
    }

    @Override
    public void openTillDetailsDialog(Till till) {

    }

    @Override
    public void showFilterPanel() {

    }
}
