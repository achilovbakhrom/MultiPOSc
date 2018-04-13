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
        showProductProfitReportFragment();
    }



    @Override
    public void openSummaryFragmentAction() {
        showSalesReportFragment();

    }

    @Override
    public void openTillsFragmentAction() {
        showTillsReportFragment();
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
        showHourlySalesReportFragment();
    }

    @Override
    public void openProductProfitFragmentAction() {
        showProductProfitReportFragment();
    }

    @Override
    public void openInventoryFragmentAction() {
        showinventoryReportFragment();
    }

    @Override
    public void openCustomersFragmentAction() {
        showCustomersReportFragment();
    }

    @Override
    public void openVendorsFragmentAction() {

    }

    @Override
    public void openDiscountsFragmentAction() {
        showDiscountReportFragment();
    }

    @Override
    public void openServiceFeesFragmentAction() {
        showServiceFeeReportFragment();
    }

    @Override
    public void openDebtsFragmentAction() {
        showDebtsReportFragment();
    }
}
