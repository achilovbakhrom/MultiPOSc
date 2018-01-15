package com.jim.multipos.ui.cash_management.view;

import android.os.Bundle;

import com.jim.mpviews.MpSwitcher;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 13.01.2018.
 */

public class CloseTillThirdStepFragment extends BaseFragment implements CloseTillThirdStepView{

    @BindView(R.id.swCloseTill)
    MpSwitcher swCloseTill;

    @Override
    protected int getLayout() {
        return R.layout.to_next_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        swCloseTill.setSwitcherStateChangedListener((isRight, isLeft) -> {

        });
    }
}
