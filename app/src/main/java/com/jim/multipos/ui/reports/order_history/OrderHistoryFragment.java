package com.jim.multipos.ui.reports.order_history;

import android.os.Bundle;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;

public class OrderHistoryFragment extends BaseFragment implements OrderHistoryView {
    @Override
    protected int getLayout() {
        return R.layout.order_history_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }
}
