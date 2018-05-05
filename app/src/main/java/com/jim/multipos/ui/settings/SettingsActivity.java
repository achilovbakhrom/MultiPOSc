package com.jim.multipos.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.reports.customers.CustomerReportFragment;
import com.jim.multipos.ui.reports.debts.DebtsReportFragment;
import com.jim.multipos.ui.reports.discount.DiscountReportFragment;
import com.jim.multipos.ui.reports.hourly_sales.HourlySalesReportFragment;
import com.jim.multipos.ui.reports.inventory.InventoryReportFragment;
import com.jim.multipos.ui.reports.order_history.OrderHistoryFragment;
import com.jim.multipos.ui.reports.payments.PaymentsReportFragment;
import com.jim.multipos.ui.reports.product_profit.ProductProfitFragment;
import com.jim.multipos.ui.reports.service_fee.ServiceFeeReportFragment;
import com.jim.multipos.ui.reports.summary_report.SummaryReportFragment;
import com.jim.multipos.ui.reports.tills.TillsReportFragment;
import com.jim.multipos.ui.reports.vendor.VendorReportFragment;
import com.jim.multipos.ui.settings.choice_panel.ChoicePanelFragment;
import com.jim.multipos.ui.settings.print.PrintFragment;
import com.jim.multipos.ui.settings.security.SecurityFragment;

public class SettingsActivity extends DoubleSideActivity implements SettingsView {

    String [] settingsFragments= {SecurityFragment.class.getName(),PrintFragment.class.getName()};


    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE_TWO_SECTION;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragmentToLeft(new ChoicePanelFragment());
//        addFragmentToRight();
    }

    public void onPanelClicked(int position){
        switch (position){
            case 0:
                //TODO BASICS
                break;
            case 1:
                //TODO POS DETAILS
                break;
            case 2:
                //TODO CURRENCY
                break;
            case 3:
                //TODO ACCOUNT
                break;
            case 4:
                //TODO PAYMENTS TYPE
                break;
            case 5:
                //TODO PRINTER
                showPrintFragment();
                break;
            case 6:
                //TODO SECURITY
                showSecurityFragment();
                break;
        }
    }

    public void showSecurityFragment(){
        hideAll();
        SecurityFragment securityFragment = (SecurityFragment) getSupportFragmentManager().findFragmentByTag(SecurityFragment.class.getName());
        if(securityFragment == null){
            securityFragment = new SecurityFragment();
            addFragmentWithTagToRight(securityFragment,SecurityFragment.class.getName());
        }else {
            getSupportFragmentManager().beginTransaction().show(securityFragment).commit();
        }
    }

    public void showPrintFragment(){
        hideAll();
        PrintFragment printFragment = (PrintFragment) getSupportFragmentManager().findFragmentByTag(PrintFragment.class.getName());
        if(printFragment == null){
            printFragment = new PrintFragment();
            addFragmentWithTagToRight(printFragment,PrintFragment.class.getName());
        }else {
            getSupportFragmentManager().beginTransaction().show(printFragment).commit();
        }
    }

    public void hideAll(){
        for (String fragmentName:settingsFragments){
            Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(fragmentName);
            if(fragmentByTag!=null && fragmentByTag.isVisible()){
                getSupportFragmentManager().beginTransaction().hide(fragmentByTag).commit();
            }
        }
    }

}
