package com.jim.multipos.ui.reports;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.multipos.core.ReportSingleActivity;
import com.jim.multipos.utils.BarcodeStack;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 12.03.2018.
 */

public class ReportsActivity extends ReportSingleActivity implements ReportsActivityView{
    @Inject
    BarcodeStack barcodeStack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSummaryReportFragment();
        //TODO HORIZANTAL FRAGMENTS
        barcodeStack.register(this::onBarcodeScaned);
    }

    @Override
    protected void onDestroy() {
        barcodeStack.unregister();
        super.onDestroy();
    }

    @Override
    public void openSummaryFragmentAction() {
        showSummaryReportFragment();

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
        showPaymentsRepotFragment();
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
        showInventoryReportFragment();
    }

    @Override
    public void openCustomersFragmentAction() {
        showCustomersReportFragment();
    }

    @Override
    public void openVendorsFragmentAction() {
        showVendorReportFragment();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
