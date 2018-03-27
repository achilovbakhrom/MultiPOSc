package com.jim.multipos.ui.reports;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.multipos.core.ReportSingleActivity;

/**
 * Created by Sirojiddin on 12.03.2018.
 */

public class ReportsActivity extends ReportSingleActivity implements ReportsActivityView{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSalesReportFragment();
    }



    @Override
    public void openSummaryFragmentAction() {
        showSalesReportFragment();

    }

    @Override
    public void openTillsFragmentAction() {

    }

    @Override
    public void openOrdersFragmentAction() {
        showOrderHistoryFragment();
    }

    @Override
    public void openPaymentsFragmentAction() {

    }

    @Override
    public void openHourlySalesFragmentAction() {

    }

    @Override
    public void openProductProfitFragmentAction() {

    }

    @Override
    public void openInventoryFragmentAction() {

    }

    @Override
    public void openCustomersFragmentAction() {

    }

    @Override
    public void openVendorsFragmentAction() {

    }

    @Override
    public void openDiscountsFragmentAction() {

    }

    @Override
    public void openServiceFeesFragmentAction() {

    }

    @Override
    public void openDebtsFragmentAction() {

    }
}
