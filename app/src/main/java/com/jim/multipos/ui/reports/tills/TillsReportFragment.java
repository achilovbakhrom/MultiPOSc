package com.jim.multipos.ui.reports.tills;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jim.mpviews.MpButtonWithImg;
import com.jim.mpviews.MpSearchView;
import com.jim.mpviews.ReportView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.utils.CustomFilterDialog;
import com.jim.multipos.utils.ExportDialog;

import java.text.DecimalFormat;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 27.03.2018.
 */

public class TillsReportFragment extends BaseFragment implements TillsReportView {

    @Inject
    TillsReportPresenter presenter;
    @Inject
    DatabaseManager databaseManager;
    @Inject
    DecimalFormat decimalFormat;

    @BindView(R.id.btnFilter)
    MpButtonWithImg btnFilter;
    @BindView(R.id.btnDateInterval)
    MpButtonWithImg btnDateInterval;
    @BindView(R.id.tvTillTitle)
    TextView tvTillTitle;
    @BindView(R.id.svTillSearch)
    MpSearchView svTillSearch;
    @BindView(R.id.flReportContainer)
    FrameLayout flReportContainer;


    @Override
    protected int getLayout() {
        return R.layout.till_report_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        presenter.initTillReportData();

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnDateInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void fillReportView(Object[][] objects, int[] dataType, String[] titles, int[] weights, int[] aligns) {
        flReportContainer.removeAllViews();
        FrameLayout fl = new ReportView.Builder()
                .setContext(getContext())
                .setTitles(titles)
                .setDataTypes(dataType)
                .setWeight(weights)
                .setDataAlignTypes(aligns)
                .setObjects(objects)
                .setOnReportViewResponseListener((row, column) -> {
                    presenter.openTillDetailsDialog(row, column);
                })
                .build();
        flReportContainer.addView(fl);
    }

    @Override
    public void openTillDetailsDialog(Till till) {

    }
}
