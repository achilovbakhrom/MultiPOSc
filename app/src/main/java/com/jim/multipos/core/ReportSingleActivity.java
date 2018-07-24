package com.jim.multipos.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.ui.reports.adapter.ReportPickerAdapter;
import com.jim.multipos.ui.reports.customers.CustomerReportFragment;
import com.jim.multipos.ui.reports.debts.DebtsReportFragment;
import com.jim.multipos.ui.reports.discount.DiscountReportFragment;
import com.jim.multipos.ui.reports.hourly_sales.HourlySalesReportFragment;
import com.jim.multipos.ui.reports.order_history.OrderHistoryFragment;
import com.jim.multipos.ui.reports.payments.PaymentsReportFragment;
import com.jim.multipos.ui.reports.product_profit.ProductProfitFragment;
import com.jim.multipos.ui.reports.service_fee.ServiceFeeReportFragment;
import com.jim.multipos.ui.reports.stock_operations.StockOperationFragment;
import com.jim.multipos.ui.reports.stock_state.StockStateFragment;
import com.jim.multipos.ui.reports.summary_report.SummaryReportFragment;
import com.jim.multipos.ui.reports.tills.TillsReportFragment;
import com.jim.multipos.ui.reports.vendor.VendorReportFragment;

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
    String[] reportsFragmentsTags = {VendorReportFragment.class.getName(), SummaryReportFragment.class.getName(), TillsReportFragment.class.getName(), OrderHistoryFragment.class.getName(), DiscountReportFragment.class.getName(), ServiceFeeReportFragment.class.getName(), HourlySalesReportFragment.class.getName(),
            DebtsReportFragment.class.getName(), PaymentsReportFragment.class.getName(), ProductProfitFragment.class.getName(), CustomerReportFragment.class.getName(), StockOperationFragment.class.getName(), StockStateFragment.class.getName()};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity);
        ButterKnife.bind(this);
        String[] reportNamesString = getResources().getStringArray(R.array.reports_list);
        reportNames = new ArrayList<>(Arrays.asList(reportNamesString));

        reportPickerAdapter = new ReportPickerAdapter(reportNames, pos -> {
            /*
             <item>Summary</item>
             <item>Tills</item>
             <item>Orders</item>
             <item>Payments</item>
             <item>Hourly sales</item>
             <item>Product profit</item>
             <item>Stock State</item>
             <item>Stock Operation</item>
             <item>Customers</item>
             <item>Vendors</item>
             <item>Debt</item>
             <item>Discounts</item>
             <item>Service Fee\'s</item>
             */
            switch (pos) {
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
                    openStockStateFragmentAction();
                    break;
                case 7:
                    openStockOperationsFragmentAction();
                    break;
                case 8:
                    openCustomersFragmentAction();
                    break;
                case 9:
                    openVendorsFragmentAction();
                    break;
                case 10:
                    openDebtsFragmentAction();
                    break;
                case 11:
                    openDiscountsFragmentAction();
                    break;
                case 12:
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

    public abstract void openStockStateFragmentAction();

    public abstract void openStockOperationsFragmentAction();

    public abstract void openCustomersFragmentAction();

    public abstract void openVendorsFragmentAction();

    public abstract void openDiscountsFragmentAction();

    public abstract void openServiceFeesFragmentAction();

    public abstract void openDebtsFragmentAction();

    public void showOrderHistoryFragment(/*extra*/) {
        hideAll();
        OrderHistoryFragment orderHistoryFragment = (OrderHistoryFragment) getSupportFragmentManager().findFragmentByTag(OrderHistoryFragment.class.getName());
        if (orderHistoryFragment == null) {
            orderHistoryFragment = new OrderHistoryFragment();
            addFragmentWithTagStatic(R.id.flMain, orderHistoryFragment, OrderHistoryFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(orderHistoryFragment).commit();
        }
    }

    public void showSummaryReportFragment(/*extra*/) {
        hideAll();
        SummaryReportFragment summaryReportFragment = (SummaryReportFragment) getSupportFragmentManager().findFragmentByTag(SummaryReportFragment.class.getName());
        if (summaryReportFragment == null) {
            summaryReportFragment = new SummaryReportFragment();
            addFragmentWithTagStatic(R.id.flMain, summaryReportFragment, SummaryReportFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(summaryReportFragment).commit();
        }
    }

    public void showTillsReportFragment(/*extra*/) {
        hideAll();
        TillsReportFragment tillsReportFragment = (TillsReportFragment) getSupportFragmentManager().findFragmentByTag(TillsReportFragment.class.getName());
        if (tillsReportFragment == null) {
            tillsReportFragment = new TillsReportFragment();
            addFragmentWithTagStatic(R.id.flMain, tillsReportFragment, TillsReportFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(tillsReportFragment).commit();
        }
    }

    public void showDiscountReportFragment(/*extra*/) {
        hideAll();
        DiscountReportFragment discountReportFragment = (DiscountReportFragment) getSupportFragmentManager().findFragmentByTag(DiscountReportFragment.class.getName());
        if (discountReportFragment == null) {
            discountReportFragment = new DiscountReportFragment();
            addFragmentWithTagStatic(R.id.flMain, discountReportFragment, DiscountReportFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(discountReportFragment).commit();
        }
    }

    public void showProductProfitReportFragment() {
        hideAll();
        ProductProfitFragment productProfitFragment = (ProductProfitFragment) getSupportFragmentManager().findFragmentByTag(ProductProfitFragment.class.getName());
        if (productProfitFragment == null) {
            productProfitFragment = new ProductProfitFragment();
            addFragmentWithTagStatic(R.id.flMain, productProfitFragment, ProductProfitFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(productProfitFragment).commit();
        }
    }

    public void showServiceFeeReportFragment(/*extra*/) {
        hideAll();
        ServiceFeeReportFragment serviceFeeReportFragment = (ServiceFeeReportFragment) getSupportFragmentManager().findFragmentByTag(ServiceFeeReportFragment.class.getName());
        if (serviceFeeReportFragment == null) {
            serviceFeeReportFragment = new ServiceFeeReportFragment();
            addFragmentWithTagStatic(R.id.flMain, serviceFeeReportFragment, ServiceFeeReportFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(serviceFeeReportFragment).commit();
        }
    }

    public void showHourlySalesReportFragment(/*extra*/) {
        hideAll();
        HourlySalesReportFragment hourlySalesReportFragment = (HourlySalesReportFragment) getSupportFragmentManager().findFragmentByTag(HourlySalesReportFragment.class.getName());
        if (hourlySalesReportFragment == null) {
            hourlySalesReportFragment = new HourlySalesReportFragment();
            addFragmentWithTagStatic(R.id.flMain, hourlySalesReportFragment, HourlySalesReportFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(hourlySalesReportFragment).commit();
        }
    }

    public void showDebtsReportFragment(/*extra*/) {
        hideAll();
        DebtsReportFragment debtsReportFragment = (DebtsReportFragment) getSupportFragmentManager().findFragmentByTag(DebtsReportFragment.class.getName());
        if (debtsReportFragment == null) {
            debtsReportFragment = new DebtsReportFragment();
            addFragmentWithTagStatic(R.id.flMain, debtsReportFragment, DebtsReportFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(debtsReportFragment).commit();
        }
    }

    public void showPaymentsRepotFragment(/*extra*/) {
        hideAll();
        PaymentsReportFragment paymentsReportFragment = (PaymentsReportFragment) getSupportFragmentManager().findFragmentByTag(PaymentsReportFragment.class.getName());
        if (paymentsReportFragment == null) {
            paymentsReportFragment = new PaymentsReportFragment();
            addFragmentWithTagStatic(R.id.flMain, paymentsReportFragment, PaymentsReportFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(paymentsReportFragment).commit();
        }
    }

    public void showCustomersReportFragment(/*extra*/) {
        hideAll();
        CustomerReportFragment customerReportFragment = (CustomerReportFragment) getSupportFragmentManager().findFragmentByTag(CustomerReportFragment.class.getName());
        if (customerReportFragment == null) {
            customerReportFragment = new CustomerReportFragment();
            addFragmentWithTagStatic(R.id.flMain, customerReportFragment, CustomerReportFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(customerReportFragment).commit();
        }
    }

    public void showStockStateReportFragment(/*extra*/) {
        hideAll();
        StockStateFragment stockStateFragment = (StockStateFragment) getSupportFragmentManager().findFragmentByTag(StockStateFragment.class.getName());
        if (stockStateFragment == null) {
            stockStateFragment = new StockStateFragment();
            addFragmentWithTagStatic(R.id.flMain, stockStateFragment, StockStateFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(stockStateFragment).commit();
        }
    }

    public void showStockOperationReportFragment(/*extra*/) {
        hideAll();
        StockOperationFragment stockOperationFragment = (StockOperationFragment) getSupportFragmentManager().findFragmentByTag(StockOperationFragment.class.getName());
        if (stockOperationFragment == null) {
            stockOperationFragment = new StockOperationFragment();
            addFragmentWithTagStatic(R.id.flMain, stockOperationFragment, StockOperationFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(stockOperationFragment).commit();
        }
    }

    public void showVendorReportFragment(/*extra*/) {
        hideAll();
        VendorReportFragment vendorReportFragment = (VendorReportFragment) getSupportFragmentManager().findFragmentByTag(VendorReportFragment.class.getName());
        if (vendorReportFragment == null) {
            vendorReportFragment = new VendorReportFragment();
            addFragmentWithTagStatic(R.id.flMain, vendorReportFragment, VendorReportFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(vendorReportFragment).commit();
        }
    }

    public void onBarcodeScaned(String barcode) {
        Fragment fragment = getVisibleFragment();
        if (fragment != null) {
            if (fragment instanceof CustomerReportFragment && fragment.isVisible() && fragment.isAdded()) {
                ((CustomerReportFragment) fragment).onBarcodeScaned(barcode);
            } else if (fragment instanceof ProductProfitFragment && fragment.isVisible() && fragment.isAdded()) {
                ((ProductProfitFragment) fragment).onBarcodeScaned(barcode);
            } else if (fragment instanceof VendorReportFragment && fragment.isVisible() && fragment.isAdded()) {
                ((VendorReportFragment) fragment).onBarcodeScaned(barcode);
            }
        }
    }

    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    public void hideAll() {
        for (String fragmentName : reportsFragmentsTags) {
            Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(fragmentName);
            if (fragmentByTag != null && fragmentByTag.isVisible()) {
                getSupportFragmentManager().beginTransaction().hide(fragmentByTag).commit();
            }
        }
    }
}
