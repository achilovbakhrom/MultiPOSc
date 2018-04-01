package com.jim.multipos.ui.reports.tills;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.jim.mpviews.ReportView;
import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.core.BaseTableReportFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.ui.reports.tills.dialog.TillDetailsDialog;

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
    private FrameLayout fl;
    private ReportView.Builder builder;
    private ReportView reportView;
    private int dataType[] = {
            ReportViewConstants.ID, ReportViewConstants.DATE, ReportViewConstants.DATE, ReportViewConstants.AMOUNT, ReportViewConstants.AMOUNT, ReportViewConstants.ACTION};
    private int weights[] = {1, 2, 2, 2, 2, 1};
    private int aligns[] = {Gravity.RIGHT, Gravity.CENTER, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT, Gravity.CENTER};
    private String titles[] = {"Till ID", "Opened Time", "Closed Time", "Start Money Variance", "Till Amount Variance", "Action"};

    @Override
    protected void init(Bundle savedInstanceState) {
        init(presenter);
        disableFilter();
        setSingleTitle("Till Reports");
        builder = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(titles)
                .setDataTypes(dataType)
                .setWeight(weights)
                .setDataAlignTypes(aligns)
                .setOnReportViewResponseListener((objects1, row, column) -> {
                    presenter.openTillDetailsDialog(objects1, row, column);
                })
                .build();
        reportView = new ReportView(builder);
        presenter.onCreateView(savedInstanceState);
    }
    @Override
    public void fillReportView(Object[][] objects) {
        reportView.getBuilder().setSearchedText("");
        reportView.getBuilder().update(objects);
        fl = reportView.getBuilder().getView();
        setTable(fl);
    }

    @Override
    public void openTillDetailsDialog(Till till) {
        TillDetailsDialog dialog = new TillDetailsDialog(getContext(), databaseManager, till);
        dialog.show();
    }

    @Override
    public void setSearchResults(Object[][] objects, String searchText) {
        reportView.getBuilder().setSearchedText(searchText);
        reportView.getBuilder().update(objects);
        fl = reportView.getBuilder().getView();
        setTable(fl);
    }

    @Override
    public void showFilterPanel() {
        /*There is no filter fot tills report*/
    }
}
