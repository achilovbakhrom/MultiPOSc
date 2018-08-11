package com.jim.multipos.ui.admin_main_page.fragments.dashboard;

import android.os.Bundle;

import com.jim.mpviews.MpCircleWithText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;

import butterknife.BindView;

public class DashboardMainFragment extends BaseFragment {

    @BindView(R.id.revenue)
    MpCircleWithText revenue;
    @BindView(R.id.profit)
    MpCircleWithText profit;
    @BindView(R.id.pay_out)
    MpCircleWithText pay_out;
    @BindView(R.id.dept)
    MpCircleWithText dept;

    @Override
    protected int getLayout() {
        return R.layout.admin_dashboard_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        revenue.setPercentandText(50, "Revenue");
        profit.setPercentandText(70, "Profit");
        pay_out.setPercentandText(40, "Pay Out");
        dept.setPercentandText(80, "Dept");
    }
}
