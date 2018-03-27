package com.jim.multipos.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.ui.reports.adapter.ReportPickerAdapter;
import com.jim.multipos.ui.reports.order_history.OrderHistoryFragment;
import com.jim.multipos.ui.reports.sales.SalesReportFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 12.03.2018.
 */

public abstract class ReportSingleActivity extends BaseActivity {
    @BindView(R.id.tvReportsPicker)
    RecyclerView tvReportsPicker;
    ReportPickerAdapter reportPickerAdapter;
    List<String> reportNames;
    String [] reportsFragmentsTags= {SalesReportFragment.class.getName(),OrderHistoryFragment.class.getName()};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity);
        ButterKnife.bind(this);
        String[] reportNamesString = getResources().getStringArray(R.array.reports_list);
        reportNames =  new ArrayList<>(Arrays.asList(reportNamesString));

        reportPickerAdapter = new ReportPickerAdapter(reportNames, pos -> {
            /*
             <item>Summary</item>
             <item>Tills</item>
             <item>Orders</item>
             <item>Payments</item>
             <item>Hourly sales</item>
             <item>Product profit</item>
             <item>Inventory</item>
             <item>Customers</item>
             <item>Vendors</item>
             <item>Debt</item>
             <item>Discounts</item>
             <item>Service Fee\'s</item>
             */


            switch (pos){
                case 0:
                    openSummaryFragmentAction();
                    break;
                case 1:
                    openTillsFragmentAction();
                    break;
                case 2:
                    openOrdersFragmentAction();
                    break;
                case 3:
                    openPaymentsFragmentAction();
                    break;
                case 4:
                    openHourlySalesFragmentAction();
                    break;
                case 5:
                    openProductProfitFragmentAction();
                    break;
                case 6:
                    openInventoryFragmentAction();
                    break;
                case 7:
                    openCustomersFragmentAction();
                    break;
                case 8:
                    openVendorsFragmentAction();
                    break;
                case 9:
                    openDebtsFragmentAction();
                    break;
                case 10:
                    openDiscountsFragmentAction();
                    break;
                case 11:
                    openServiceFeesFragmentAction();
                    break;
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tvReportsPicker.setLayoutManager(layoutManager);
        tvReportsPicker.setAdapter(reportPickerAdapter);
        tvReportsPicker.setItemAnimator(null);

    }

    public abstract void openSummaryFragmentAction();
    public abstract void openTillsFragmentAction();
    public abstract void openOrdersFragmentAction();
    public abstract void openPaymentsFragmentAction();
    public abstract void openHourlySalesFragmentAction();
    public abstract void openProductProfitFragmentAction();
    public abstract void openInventoryFragmentAction();
    public abstract void openCustomersFragmentAction();
    public abstract void openVendorsFragmentAction();
    public abstract void openDiscountsFragmentAction();
    public abstract void openServiceFeesFragmentAction();
    public abstract void openDebtsFragmentAction();

    public void showOrderHistoryFragment(/*extra*/){
        hideAll();
        OrderHistoryFragment orderHistoryFragment = (OrderHistoryFragment) getSupportFragmentManager().findFragmentByTag(OrderHistoryFragment.class.getName());
        if(orderHistoryFragment == null){
            orderHistoryFragment = new OrderHistoryFragment();
            addFragmentWithTagStatic(R.id.flMain,orderHistoryFragment,OrderHistoryFragment.class.getName());
        }else {
            getSupportFragmentManager().beginTransaction().show(orderHistoryFragment).commit();
        }
    }
    public void showSalesReportFragment(/*extra*/){
        hideAll();
        SalesReportFragment salesReportFragment = (SalesReportFragment) getSupportFragmentManager().findFragmentByTag(SalesReportFragment.class.getName());
        if(salesReportFragment == null){
            salesReportFragment = new SalesReportFragment();
            addFragmentWithTagStatic(R.id.flMain,salesReportFragment,SalesReportFragment.class.getName());
        }else {
            getSupportFragmentManager().beginTransaction().show(salesReportFragment).commit();
        }
    }

    public void hideAll(){
        for (String fragmentName:reportsFragmentsTags){
            Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(fragmentName);
            if(fragmentByTag!=null && fragmentByTag.isVisible()){
                getSupportFragmentManager().beginTransaction().hide(fragmentByTag).commit();
            }
        }
    }
}
