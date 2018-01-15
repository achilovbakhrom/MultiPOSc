package com.jim.multipos.ui.cash_management.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.jim.mpviews.CircleIndicator;
import com.jim.mpviews.MPosSpinner;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.ui.cash_management.adapter.AccountsSpinnerAdapter;

import java.util.ArrayList;
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

    @Inject
    DatabaseManager databaseManager;

    private FragmentManager fragmentManager;
    private int position = 0;
    private List<Account> accountList;
    public static final String CASH_DETAILS = "Cash Details";

    @Override
    protected int getLayout() {
        return R.layout.cash_log_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        fragmentManager = getActivity().getSupportFragmentManager();
        addFragmentWithTag(R.id.flCashDetails, new CashDetailsFragment(), CASH_DETAILS);
        spAccounts.setSpinnerBackground(R.color.transparent);


        spAccounts.setArrowTint(R.color.colorDarkBlue);


        accountList = databaseManager.getAccounts();

        List<String> accounts = new ArrayList<>();
        for (Account accName : accountList) {
            accounts.add(accName.getName());
        }
        AccountsSpinnerAdapter adapter = new AccountsSpinnerAdapter(getContext(), accounts);
        spAccounts.setAdapter(adapter);
        ciAccountsIndicator.setSpinner(spAccounts);

        spAccounts.setItemSelectionListener((view, pos) -> {
            position = pos;
            setDataToDetailsFragment();
            ciAccountsIndicator.onItemSelected(pos);
        });

        ivLeft.setOnClickListener(view -> {
            if (position != 0)
                position--;
            spAccounts.setSelectedPosition(position);
        });

        ivRight.setOnClickListener(view -> {
            if (position < accounts.size() - 1)
                position++;
            else position = accounts.size() - 1;
            spAccounts.setSelectedPosition(position);
        });
    }

    private void setDataToDetailsFragment() {
        CashDetailsFragment fragment = (CashDetailsFragment) fragmentManager.findFragmentByTag(CASH_DETAILS);
        if (fragment != null) {
            fragment.setData(accountList.get(position));
        }
    }


    private void addFragmentWithTag(@IdRes int containerViewId, Fragment fragment, String tag) {
        fragmentManager.beginTransaction()
                .add(containerViewId, fragment, tag)
                .commit();
    }
}
