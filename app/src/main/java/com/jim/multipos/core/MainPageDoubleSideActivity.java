package com.jim.multipos.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.ui.mainpospage.view.OrderListFragment;
import com.jim.multipos.ui.mainpospage.view.PaymentFragment;
import com.jim.multipos.ui.mainpospage.view.ProductInfoFragment;
import com.jim.multipos.ui.mainpospage.view.ProductPickerFragment;
import com.jim.multipos.ui.mainpospage.view.SearchModeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developer on 05.10.2017.
 */

public abstract class MainPageDoubleSideActivity extends BaseActivity{

    @BindView(R.id.toolbar)
    MpToolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_double_activity_layout);
        ButterKnife.bind(this);
        toolbar.setMode(getToolbarMode());
    }

    protected abstract int getToolbarMode();

    public void initOrderListFragmentToLeft(){
        OrderListFragment orderListFragment  =  (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());
        if(orderListFragment==null){
            orderListFragment = new OrderListFragment();
            addFragmentWithTagStatic(R.id.flLeftContainer,orderListFragment,OrderListFragment.class.getName());
        }else {
            getSupportFragmentManager().beginTransaction().show(orderListFragment).commit();
        }
    }
    public void initProductPickerFragmentToRight(){
        ProductPickerFragment productPickerFragment  =  (ProductPickerFragment) getSupportFragmentManager().findFragmentByTag(ProductPickerFragment.class.getName());
        if(productPickerFragment==null){
            productPickerFragment = new ProductPickerFragment();
            addFragmentWithTagStatic(R.id.flRightContainer,productPickerFragment,ProductPickerFragment.class.getName());
        }else {
            getSupportFragmentManager().beginTransaction().show(productPickerFragment).commit();
        }
    }
    public void showSearchFragment(){
        SearchModeFragment searchModeFragment = (SearchModeFragment) getSupportFragmentManager().findFragmentByTag(SearchModeFragment.class.getName());
        ProductInfoFragment productInfoFragment = (ProductInfoFragment) getSupportFragmentManager().findFragmentByTag(ProductInfoFragment.class.getName());
        if(productInfoFragment !=null && productInfoFragment.isVisible()){
            OrderListFragment orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());
            if(orderListFragment!=null && orderListFragment.isVisible()){
                    orderListFragment.hideInfoProduct();
            }

            getSupportFragmentManager().beginTransaction().hide(productInfoFragment).commit();
        }
        PaymentFragment paymentFragment = (PaymentFragment) getSupportFragmentManager().findFragmentByTag(PaymentFragment.class.getName());
        if(paymentFragment !=null && paymentFragment.isVisible()){
            OrderListFragment orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());
            if(orderListFragment!=null && orderListFragment.isVisible()){
                orderListFragment.hidePaymentFragment();
            }
            getSupportFragmentManager().beginTransaction().hide(paymentFragment).commit();
        }
        if(searchModeFragment == null){
            searchModeFragment = new SearchModeFragment();
            addFragmentWithTagStatic(R.id.flRightContainer,searchModeFragment,SearchModeFragment.class.getName());
        }else {
            getSupportFragmentManager().beginTransaction().show(searchModeFragment).commit();
        }
    }
    public void hideSearchFragment(){
        SearchModeFragment searchModeFragment = (SearchModeFragment) getSupportFragmentManager().findFragmentByTag(SearchModeFragment.class.getName());
        if(searchModeFragment != null){
            getSupportFragmentManager().beginTransaction().hide(searchModeFragment).commit();
        }
    }
    public void showHideProductInfoFragment(){
        ProductInfoFragment productInfoFragment = (ProductInfoFragment) getSupportFragmentManager().findFragmentByTag(ProductInfoFragment.class.getName());
        if(productInfoFragment ==null){
            productInfoFragment = new ProductInfoFragment();
            addFragmentWithTagStatic(R.id.flRightContainer,productInfoFragment,ProductInfoFragment.class.getName());
        }else {
            if(productInfoFragment.isVisible()){
                getSupportFragmentManager().beginTransaction().hide(productInfoFragment).commit();
            }else {
                getSupportFragmentManager().beginTransaction().show(productInfoFragment).commit();
                productInfoFragment.refreshData();
            }
        }
    }
    public void showProductInfoFragment(){
        ProductInfoFragment productInfoFragment = (ProductInfoFragment) getSupportFragmentManager().findFragmentByTag(ProductInfoFragment.class.getName());
        PaymentFragment paymentFragment = (PaymentFragment) getSupportFragmentManager().findFragmentByTag(PaymentFragment.class.getName());
        if(paymentFragment !=null && paymentFragment.isVisible()){
            OrderListFragment orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());
            if(orderListFragment!=null && orderListFragment.isVisible()){
                orderListFragment.hidePaymentFragment();
            }
            getSupportFragmentManager().beginTransaction().hide(paymentFragment).commit();
        }
        if(productInfoFragment ==null){
            productInfoFragment = new ProductInfoFragment();
            addFragmentWithTagStatic(R.id.flRightTop,productInfoFragment,ProductInfoFragment.class.getName());
        }else {
            if(productInfoFragment.isVisible()){
                productInfoFragment.refreshData();
            }else {
                getSupportFragmentManager().beginTransaction().show(productInfoFragment).commit();
                productInfoFragment.refreshData();
            }
        }
    }
    public void hideProductInfoFragment(){
        ProductInfoFragment productInfoFragment = (ProductInfoFragment) getSupportFragmentManager().findFragmentByTag(ProductInfoFragment.class.getName());
        if(productInfoFragment !=null) {
            getSupportFragmentManager().beginTransaction().hide(productInfoFragment).commit();
        }
    }
    public void showPaymentFragment(){
        PaymentFragment paymentFragment = (PaymentFragment) getSupportFragmentManager().findFragmentByTag(PaymentFragment.class.getName());
        ProductInfoFragment productInfoFragment = (ProductInfoFragment) getSupportFragmentManager().findFragmentByTag(ProductInfoFragment.class.getName());
        if(productInfoFragment !=null && productInfoFragment.isVisible()){
            OrderListFragment orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());
            if(orderListFragment!=null && orderListFragment.isVisible()){
                orderListFragment.hideInfoProduct();
            }
            getSupportFragmentManager().beginTransaction().hide(productInfoFragment).commit();
        }
        if(paymentFragment == null){
            paymentFragment = new PaymentFragment();
            addFragmentWithTagStatic(R.id.flRightTop,paymentFragment,PaymentFragment.class.getName());
        }else {
            if(paymentFragment.isVisible()){
                paymentFragment.refreshData();
            }else {
                getSupportFragmentManager().beginTransaction().show(paymentFragment).commit();
                paymentFragment.refreshData();
            }
        }
    }
    public void hidePaymentFragment(){
        PaymentFragment paymentFragment = (PaymentFragment) getSupportFragmentManager().findFragmentByTag(PaymentFragment.class.getName());
        if(paymentFragment !=null) {
            getSupportFragmentManager().beginTransaction().hide(paymentFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        PaymentFragment paymentFragment = (PaymentFragment) getSupportFragmentManager().findFragmentByTag(PaymentFragment.class.getName());
        if(paymentFragment !=null && paymentFragment.isVisible()){
            OrderListFragment orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());
            if(orderListFragment!=null && orderListFragment.isVisible()){
                orderListFragment.hidePaymentFragment();
            }
            getSupportFragmentManager().beginTransaction().hide(paymentFragment).commit();
            return;
        }
        ProductInfoFragment productInfoFragment = (ProductInfoFragment) getSupportFragmentManager().findFragmentByTag(ProductInfoFragment.class.getName());
        if(productInfoFragment !=null && productInfoFragment.isVisible()){
            OrderListFragment orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());
            if(orderListFragment!=null && orderListFragment.isVisible()){
                orderListFragment.hideInfoProduct();
            }
            getSupportFragmentManager().beginTransaction().hide(productInfoFragment).commit();
            return;
        }
        super.onBackPressed();
    }

    protected final void addFragmentToTopRight(Fragment fragment){addFragment(R.id.flRightTop,fragment);}

}
