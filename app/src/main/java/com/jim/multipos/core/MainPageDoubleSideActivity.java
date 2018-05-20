package com.jim.multipos.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.ui.mainpospage.view.BarcodeScannerFragment;
import com.jim.multipos.ui.mainpospage.view.OrderListFragment;
import com.jim.multipos.ui.mainpospage.view.OrderListHistoryFragment;
import com.jim.multipos.ui.mainpospage.view.PaymentFragment;
import com.jim.multipos.ui.mainpospage.view.ProductInfoFragment;
import com.jim.multipos.ui.mainpospage.view.ProductPickerFragment;
import com.jim.multipos.ui.mainpospage.view.SearchModeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.jim.multipos.ui.mainpospage.view.OrderListFragment.NEW_ORDER_ID;

/**
 * Created by developer on 05.10.2017.
 */


public abstract class MainPageDoubleSideActivity extends BaseActivity{
    public static final String INIT_ORDER = "INIT_ORDER";
    @BindView(R.id.toolbar)
    MpToolbar toolbar;
    Unbinder bind;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_double_activity_layout);

        bind = ButterKnife.bind(this);
        toolbar.setMode(getToolbarMode());

        PaymentFragment fragment  =  (PaymentFragment) getSupportFragmentManager().findFragmentByTag(PaymentFragment.class.getName());
        if(fragment == null) {
            fragment = new PaymentFragment();
            addFragmentWithTagStatic(R.id.flRightTop, fragment,PaymentFragment.class.getName());
        }else {
            addFragmentWithTagStatic(R.id.flRightTop, fragment,PaymentFragment.class.getName());
        }

        ProductInfoFragment fragment1  =  (ProductInfoFragment) getSupportFragmentManager().findFragmentByTag(ProductInfoFragment.class.getName());
        if(fragment1 == null) {
            fragment1 = new ProductInfoFragment();
            addFragmentWithTagStatic(R.id.flRightTop, fragment1,ProductInfoFragment.class.getName());
        }else {
            addFragmentWithTagStatic(R.id.flRightTop, fragment1,ProductInfoFragment.class.getName());
        }

        OrderListHistoryFragment fragment2  =  (OrderListHistoryFragment) getSupportFragmentManager().findFragmentByTag(OrderListHistoryFragment.class.getName());
        if(fragment2 == null) {
            fragment2 = new OrderListHistoryFragment();
            addFragmentWithTagStatic(R.id.flLeftContainerTop, fragment2,OrderListHistoryFragment.class.getName());
        }else {
            addFragmentWithTagStatic(R.id.flLeftContainerTop, fragment2,OrderListHistoryFragment.class.getName());
        }

        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
        getSupportFragmentManager().beginTransaction().hide(fragment1).commit();
        getSupportFragmentManager().beginTransaction().hide(fragment2).commit();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();

    }

    public void addFullFragment(Fragment fragment){
        addFragment(R.id.flFull,fragment);
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
    public void initOrderListFragmentToLeft(Long newOrderId){
        OrderListFragment orderListFragment  =  (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());
        if(orderListFragment==null){
            orderListFragment = new OrderListFragment();
            Bundle bundle= new Bundle();
            bundle.putLong(NEW_ORDER_ID,newOrderId);
            orderListFragment.setArguments(bundle);
            addFragmentWithTagStatic(R.id.flLeftContainer,orderListFragment,OrderListFragment.class.getName());
        }else {
            Bundle bundle= new Bundle();
            bundle.putLong(NEW_ORDER_ID,newOrderId);
            orderListFragment.setArguments(bundle);
            addFragmentWithTagStatic(R.id.flLeftContainer,orderListFragment,OrderListFragment.class.getName());
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
        SearchModeFragment fragment2 = new SearchModeFragment();
        addFragmentWithTagStatic(R.id.flRightContainer, fragment2,SearchModeFragment.class.getName());
        getSupportFragmentManager().beginTransaction().hide(fragment2).commit();

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
                orderListFragment.visiblePayButton();
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

    public void showProductInfoFragment(){
        ProductInfoFragment productInfoFragment = (ProductInfoFragment) getSupportFragmentManager().findFragmentByTag(ProductInfoFragment.class.getName());
        PaymentFragment paymentFragment = (PaymentFragment) getSupportFragmentManager().findFragmentByTag(PaymentFragment.class.getName());
        OrderListFragment orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());

        if(paymentFragment !=null && paymentFragment.isVisible()){
            if(orderListFragment!=null && orderListFragment.isVisible()){
                orderListFragment.hidePaymentFragment();
                orderListFragment.visiblePayButton();
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
        if(productInfoFragment !=null && productInfoFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(productInfoFragment).commitAllowingStateLoss();
        }
    }
    public void showOrderListHistoryFragment(Order order){
        OrderListHistoryFragment orderListHistoryFragment = (OrderListHistoryFragment) getSupportFragmentManager().findFragmentByTag(OrderListHistoryFragment.class.getName());

        ProductInfoFragment productInfoFragment = (ProductInfoFragment) getSupportFragmentManager().findFragmentByTag(ProductInfoFragment.class.getName());
        PaymentFragment paymentFragment = (PaymentFragment) getSupportFragmentManager().findFragmentByTag(PaymentFragment.class.getName());

        OrderListFragment orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());


        if(paymentFragment !=null && paymentFragment.isVisible()){
            if(orderListFragment!=null && orderListFragment.isVisible()){
                orderListFragment.hidePaymentFragment();
                orderListFragment.visiblePayButton();
            }
            getSupportFragmentManager().beginTransaction().hide(paymentFragment).commitAllowingStateLoss();
        }

        if(productInfoFragment !=null && productInfoFragment.isVisible()){
            if(orderListFragment!=null && orderListFragment.isVisible()){
                orderListFragment.hideInfoProduct();
            }
            getSupportFragmentManager().beginTransaction().hide(productInfoFragment).commitAllowingStateLoss();
        }

        if(orderListFragment!=null /*&& orderListFragment.isVisible()*/){
            orderListFragment.historyOpened();
        }

        if(orderListHistoryFragment ==null){
            orderListHistoryFragment = new OrderListHistoryFragment();
            Bundle bundle = new Bundle();
            bundle.putLong(INIT_ORDER,order.getId());
            orderListHistoryFragment.setArguments(bundle);
            addFragmentWithTagStatic(R.id.flLeftContainerTop,orderListHistoryFragment,OrderListHistoryFragment.class.getName());

        }else {
            if(orderListHistoryFragment.isVisible()){
                orderListHistoryFragment.refreshData(order);
            }else {
                getSupportFragmentManager().beginTransaction().show(orderListHistoryFragment).commitAllowingStateLoss();
                orderListHistoryFragment.refreshData(order);
            }
        }

    }
    public void hideOrderListHistoryFragment(){
        OrderListHistoryFragment orderListHistoryFragment = (OrderListHistoryFragment) getSupportFragmentManager().findFragmentByTag(OrderListHistoryFragment.class.getName());
        if(orderListHistoryFragment !=null && orderListHistoryFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(orderListHistoryFragment).commitAllowingStateLoss();
        }
    }
    public void showPaymentFragment(){
        PaymentFragment paymentFragment = (PaymentFragment) getSupportFragmentManager().findFragmentByTag(PaymentFragment.class.getName());
        ProductInfoFragment productInfoFragment = (ProductInfoFragment) getSupportFragmentManager().findFragmentByTag(ProductInfoFragment.class.getName());
        OrderListFragment orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());

        if(productInfoFragment !=null && productInfoFragment.isVisible()){
            if(orderListFragment!=null && orderListFragment.isVisible()){
                orderListFragment.hideInfoProduct();
            }
            getSupportFragmentManager().beginTransaction().hide(productInfoFragment).commit();
        }
        if(orderListFragment!=null && orderListFragment.isVisible()) {
            orderListFragment.visibleBackButton();
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
        OrderListFragment orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());
        if(orderListFragment!=null && orderListFragment.isVisible()) {
            orderListFragment.visiblePayButton();
            orderListFragment.hidePaymentFragment();
        }
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
                orderListFragment.visiblePayButton();
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
//        super.onBackPressed();
    }

    protected final void addFragmentToTopRight(Fragment fragment){addFragment(R.id.flRightTop,fragment);}

    public void showBarcodeScannerFragment() {
        BarcodeScannerFragment barcodeScannerFragment = (BarcodeScannerFragment) getSupportFragmentManager().findFragmentByTag(BarcodeScannerFragment.class.getName());
        if (barcodeScannerFragment == null) {
            barcodeScannerFragment = new BarcodeScannerFragment();
            addFragmentWithTagAndBackStack(R.id.flFullContainer, barcodeScannerFragment, BarcodeScannerFragment.class.getName());
        } else {
            getSupportFragmentManager().beginTransaction().show(barcodeScannerFragment).commit();
        }
    }

    public void hideBarcodeScannerFragment() {
        getSupportFragmentManager().popBackStack();
    }
}
