package com.jim.multipos.ui.reports.tills;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jim.mpviews.MpButtonWithImg;
import com.jim.mpviews.MpSearchView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 27.03.2018.
 */

public class TillsReportFragment extends BaseFragment implements TillsReportView {

    @BindView(R.id.btnFilter)
    MpButtonWithImg btnFilter;
    @BindView(R.id.btnDateInterval)
    MpButtonWithImg btnDateInterval;
    @BindView(R.id.tvTillTitle)
    TextView tvTillTitle;
    @BindView(R.id.svTillSearch)
    MpSearchView svTillSearch;

    @Override
    protected int getLayout() {
        return R.layout.till_report_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {


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
}
