package com.jim.multipos.ui.cash_management.view;

import android.os.Bundle;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.Account;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 12.01.2018.
 */

public class CashDetailsFragment extends BaseFragment implements CashDetailsView {

    @BindView(R.id.tvStartingCash)
    TextView tvStartingCash;

    @Override
    protected int getLayout() {
        return R.layout.cash_details;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    public void setData(Account account) {

    }
}
