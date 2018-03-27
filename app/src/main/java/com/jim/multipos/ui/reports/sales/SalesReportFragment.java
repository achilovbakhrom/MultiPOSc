package com.jim.multipos.ui.reports.sales;

import android.os.Bundle;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;

/**
 * Created by Sirojiddin on 12.03.2018.
 */

public class SalesReportFragment extends BaseFragment implements SalesReportView{

    @Override
    protected int getLayout() {
        return R.layout.sales_summary_report;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }
}
