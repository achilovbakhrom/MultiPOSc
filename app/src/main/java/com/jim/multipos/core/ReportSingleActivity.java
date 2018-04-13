package com.jim.multipos.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.ui.reports.adapter.ReportPickerAdapter;
import com.jim.multipos.ui.reports.customers.CustomerReportFragment;
import com.jim.multipos.ui.reports.debts.DebtsReportFragment;
import com.jim.multipos.ui.reports.discount.DiscountReportFragment;
import com.jim.multipos.ui.reports.hourly_sales.HourlySalesReportFragment;
import com.jim.multipos.ui.reports.inventory.InventoryReportFragment;
import com.jim.multipos.ui.reports.order_history.OrderHistoryFragment;
import com.jim.multipos.ui.reports.product_profit.ProductProfitFragment;
import com.jim.multipos.ui.reports.sales.SalesReportFragment;
import com.jim.multipos.ui.reports.service_fee.ServiceFeeReportFragment;
import com.jim.multipos.ui.reports.tills.TillsReportFragment;

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
    String [] reportsFragmentsTags= {InventoryReportFragment.class.getName(), CustomerReportFragment.class.getName(), SalesReportFragment.class.getName(),TillsReportFragment.class.getName(),OrderHistoryFragment.class.getName(), DiscountReportFragment.class.getName(), ServiceFeeReportFragment.class.getName(), HourlySalesReportFragment.class.getName(),
     DebtsReportFragment.class.getName()};
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

    public void showTillsReportFragment(/*extra*/){
        hideAll();
        TillsReportFragment tillsReportFragment = (TillsReportFragment) getSupportFragmentManager().findFragmentByTag(TillsReportFragment.class.getName());
        if ( tillsReportFragment == null){
            tillsReportFragment = new TillsReportFragment();
            addFragmentWithTagStatic(R.id.flMain, tillsReportFragment, TillsReportFragment.class.getName());
        }else {
            getSupportFragmentManager().beginTransaction().show(tillsReportFragment).commit();
        }
    }

    public void showDiscountReportFragment(/*extra*/){
        hideAll();
        DiscountReportFragment discountReportFragment = (DiscountReportFragment) getSupportFragmentManager().findFragmentByTag(DiscountReportFragment.class.getName());
        if ( discountReportFragment == null){
            discountReportFragment = new DiscountReportFragment();
            addFragmentWithTagStatic(R.id.flMain, discountReportFragment, DiscountReportFragment.class.getName());
        }else {
            getSupportFragmentManager().beginTransaction().show(discountReportFragment).commit();
        }
    }

    public void showProductProfitReportFragment(){
        hideAll();
        ProductProfitFragment productProfitFragment = (ProductProfitFragment) getSupportFragmentManager().findFragmentByTag(ProductProfitFragment.class.getName());
        if(productProfitFragment == null){
            productProfitFragment = new ProductProfitFragment();
            addFragmentWithTagStatic(R.id.flMain,productProfitFragment,ProductProfitFragment.class.getName());
        }else {
            getSupportFragmentManager().beginTransaction().show(productProfitFragment).commit();
        }
    }

    public void showServiceFeeReportFragment(/*extra*/){
        hideAll();
        ServiceFeeReportFragment serviceFeeReportFragment = (ServiceFeeReportFragment) getSupportFragmentManager().findFragmentByTag(ServiceFeeReportFragment.class.getName());
        if ( serviceFeeReportFragment == null){
            serviceFeeReportFragment = new ServiceFeeReportFragment();
            addFragmentWithTagStatic(R.id.flMain, serviceFeeReportFragment, ServiceFeeReportFragment.class.getName());
        }else {
            getSupportFragmentManager().beginTransaction().show(serviceFeeReportFragment).commit();
        }
    }

    public void showHourlySalesReportFragment(/*extra*/){
        hideAll();
        HourlySalesReportFragment hourlySalesReportFragment = (HourlySalesReportFragment) getSupportFragmentManager().findFragmentByTag(HourlySalesReportFragment.class.getName());
        if ( hourlySalesReportFragment == null){
            hourlySalesReportFragment = new HourlySalesReportFragment();
            addFragmentWithTagStatic(R.id.flMain, hourlySalesReportFragment, HourlySalesReportFragment.class.getName());
        }else {
            getSupportFragmentManager().beginTransaction().show(hourlySalesReportFragment).commit();
        }
    }

    public void showDebtsReportFragment(/*extra*/){
        hideAll();
        DebtsReportFragment debtsReportFragment = (DebtsReportFragment) getSupportFragmentManager().findFragmentByTag(DebtsReportFragment.class.getName());
        if ( debtsReportFragment == null){
            debtsReportFragment = new DebtsReportFragment();
            addFragmentWithTagStatic(R.id.flMain, debtsReportFragment, DebtsReportFragment.class.getName());
        }else {
            getSupportFragmentManager().beginTransaction().show(debtsReportFragment).commit();
        }
    }

    public void showCustomersReportFragment(/*extra*/){
        hideAll();
        CustomerReportFragment customerReportFragment = (CustomerReportFragment) getSupportFragmentManager().findFragmentByTag(CustomerReportFragment.class.getName());
        if ( customerReportFragment == null){
            customerReportFragment = new CustomerReportFragment();
            addFragmentWithTagStatic(R.id.flMain, customerReportFragment, CustomerReportFragment.class.getName());
        }else {
            getSupportFragmentManager().beginTransaction().show(customerReportFragment).commit();
        }
    }

    public void showinventoryReportFragment(/*extra*/){
        hideAll();
        InventoryReportFragment inventoryReportFragment = (InventoryReportFragment) getSupportFragmentManager().findFragmentByTag(InventoryReportFragment.class.getName());
        if ( inventoryReportFragment == null){
            inventoryReportFragment = new InventoryReportFragment();
            addFragmentWithTagStatic(R.id.flMain, inventoryReportFragment, InventoryReportFragment.class.getName());
        }else {
            getSupportFragmentManager().beginTransaction().show(inventoryReportFragment).commit();
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
