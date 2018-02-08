package com.jim.multipos.ui.cash_management.view;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.mpviews.CircleIndicator;
import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.ui.cash_management.adapter.AccountsSpinnerAdapter;
import com.jim.multipos.ui.cash_management.connection.CashManagementConnection;
import com.jim.multipos.ui.cash_management.presenter.CashLogPresenter;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 11.01.2018.
 */

public class CashLogFragment extends BaseFragment implements CashLogView {

    @BindView(R.id.spAccounts)
    MPosSpinner spAccounts;
    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.ivRight)
    ImageView ivRight;
    @BindView(R.id.ciAccountsIndicator)
    CircleIndicator ciAccountsIndicator;
    @BindView(R.id.tvOpenedDate)
    TextView tvOpenedDate;
    @BindView(R.id.tvOpenedTime)
    TextView tvOpenedTime;
    @BindView(R.id.tvClosedDate)
    TextView tvClosedDate;
    @BindView(R.id.tvClosedTime)
    TextView tvClosedTime;
    @BindView(R.id.btnBack)
    MpButton btnBack;

    @Inject
    DatabaseManager databaseManager;
    @Inject
    CashLogPresenter presenter;
    @Inject
    CashManagementConnection connection;

    private FragmentManager fragmentManager;
    private int position = 0;
    private int maxCount = 0;

    public static final String CASH_DETAILS = "Cash Details";

    @Override
    protected int getLayout() {
        return R.layout.cash_log_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        connection.setCashLogView(this);
        fragmentManager = getActivity().getSupportFragmentManager();
        addFragmentWithTag(R.id.flCashDetails, new CashDetailsFragment(), CASH_DETAILS);
        spAccounts.setSpinnerBackground(R.color.transparent);
        spAccounts.setArrowTint(R.color.colorDarkBlue);
        presenter.initData();

        spAccounts.setItemSelectionListener((view, pos) -> {
            position = pos;
            presenter.setDataToDetailsFragment(pos);
            ciAccountsIndicator.onItemSelected(pos);
        });

        ivLeft.setOnClickListener(view -> {
            if (position != 0)
                position--;
            spAccounts.setSelectedPosition(position);
        });

        ivRight.setOnClickListener(view -> {
            if (position < maxCount)
                position++;
            else position = maxCount;
            spAccounts.setSelectedPosition(position);
        });

        btnBack.setOnClickListener(view -> getActivity().finish());
    }

    private void addFragmentWithTag(@IdRes int containerViewId, Fragment fragment, String tag) {
        fragmentManager.beginTransaction()
                .add(containerViewId, fragment, tag)
                .commit();
    }

    @Override
    public void setDataToAccountSpinner(List<String> accounts) {
        AccountsSpinnerAdapter adapter = new AccountsSpinnerAdapter(getContext(), accounts);
        spAccounts.setAdapter(adapter);
        ciAccountsIndicator.setSpinner(spAccounts);
        maxCount = accounts.size() - 1;
    }

    @Override
    public void setDataToDetailsFragment(Account account, Till till) {
        CashDetailsFragment fragment = (CashDetailsFragment) fragmentManager.findFragmentByTag(CASH_DETAILS);
        if (fragment != null) {
            fragment.setData(account, till);
        }
    }

    @Override
    public void setTillOpenDateTime(Long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        tvOpenedDate.setText(dateFormat.format(date));
        tvOpenedTime.setText(timeFormat.format(date));
    }

    @Override
    public void changeAccount(Long accountId) {
        presenter.changeAccount(accountId);
    }

    @Override
    public void setAccountSelection(int position) {
        spAccounts.setSelectedPosition(position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        connection.setCashLogView(null);
    }
}
