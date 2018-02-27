package com.jim.multipos.ui.mainpospage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.core.MainPageDoubleSideActivity;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.ui.cash_management.CashManagementActivity;
import com.jim.multipos.ui.main_menu.customers_menu.CustomersMenuActivity;
import com.jim.multipos.ui.main_menu.inventory_menu.InventoryMenuActivity;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuActivity;
import com.jim.multipos.ui.mainpospage.view.BarcodeScannerFragment;
import com.jim.multipos.ui.mainpospage.view.CustomerNotificationsFragment;
import com.jim.multipos.ui.mainpospage.view.OrderListFragment;
import com.jim.multipos.ui.mainpospage.view.OrderListHistoryFragment;
import com.jim.multipos.ui.mainpospage.view.ProductPickerFragment;
import com.jim.multipos.ui.mainpospage.view.SearchModeFragment;
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.utils.MainMenuDialog;
import com.jim.multipos.utils.OrderMenuDialog;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.TestUtils;
import com.jim.multipos.utils.managers.BarcodeScannerManager;
import com.jim.multipos.utils.managers.NotifyManager;
import com.jim.multipos.utils.rxevents.MessageEvent;
import com.jim.multipos.utils.rxevents.MessageWithIdEvent;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import lombok.Getter;

import static com.jim.multipos.ui.cash_management.presenter.CloseTillFirstStepPresenterImpl.ORDER_CANCELED;
import static com.jim.multipos.ui.cash_management.presenter.CloseTillFirstStepPresenterImpl.ORDER_CLOSED;
import static com.jim.multipos.ui.cash_management.view.CashLogFragment.TILL_CLOSED;
import static com.jim.multipos.ui.consignment.view.IncomeConsignmentFragment.CONSIGNMENT_UPDATE;

public class MainPosPageActivity extends MainPageDoubleSideActivity implements MainPosPageActivityView {
    @Inject
    @Getter
    MainPosPageActivityPresenter presenter;
    @BindView(R.id.toolbar)
    MpToolbar toolbar;
    @BindView(R.id.tvTime)
    TextClock tvTime;
    @BindView(R.id.tvDate)
    TextView tvDate;
    private Handler handler;
    @Inject
    DatabaseManager databaseManager;
    @Inject
    NotifyManager notifyManager;
    @Inject
    DecimalFormat decimalFormat;
    @Inject
    @Getter
    RxBusLocal rxBusLocal;
    @Inject
    BarcodeScannerManager barcodeScannerManager;
    @Inject
    RxBus rxBus;
    private ArrayList<Disposable> subscriptions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        //test dataset
        TestUtils.createCurrencies(databaseManager, this);
        TestUtils.createAccount(databaseManager);

        initProductPickerFragmentToRight();
        notifyManager.setView(this);

        //timer
        handler = new Handler();
        handler.post(timerUpdate);



        toolbar.setOnSettingsClickListener(view -> {
            boolean isBarcodeShown = false;
            BarcodeScannerFragment barcodeScannerFragment = (BarcodeScannerFragment) getSupportFragmentManager().findFragmentByTag(BarcodeScannerFragment.class.getName());
            if (barcodeScannerFragment != null && barcodeScannerFragment.isVisible())
                isBarcodeShown = true;

            MainMenuDialog mainMenuDialog = new MainMenuDialog(this, databaseManager, decimalFormat, isBarcodeShown, new MainMenuDialog.onMenuItemClickListener() {
                @Override
                public void onCashManagement() {
                    Intent intent = new Intent(getBaseContext(), CashManagementActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onTurnOnBarcodeScanner() {
                    showBarcodeScannerFragment();
                }

                @Override
                public void onTurnOffBarcodeScanner() {
                    hideBarcodeScannerFragment();
                }
            });
            mainMenuDialog.show();
        });
        toolbar.setOnCustomerClickListener(view -> {
            Intent intent = new Intent(this, CustomersMenuActivity.class);
            startActivity(intent);
        });
        toolbar.setOnInventoryClickListener(view -> {
            Intent intent = new Intent(this, InventoryMenuActivity.class);
            startActivity(intent);
        });

        toolbar.setOnProductClickListener(view -> {
            Intent intent = new Intent(this, ProductMenuActivity.class);
            startActivity(intent);
        });
        toolbar.setOnReportClickListener(view -> {
            //TODO REPORT
        });
        toolbar.setOnSearchClickListener(new MpToolbar.CallbackSearchFragmentClick() {
            @Override
            public void onOpen() {
                showSearchFragment();
            }

            @Override
            public void onClose() {
                hideSearchFragment();
            }
        });

        toolbar.setOnOrderClickListener(view -> {
            OrderMenuDialog orderMenuDialog = new OrderMenuDialog(this);
            orderMenuDialog.show();
        });
        toolbar.setOnRightOrderClickListner(view -> {
            presenter.onNextClick();

        });
        toolbar.setOnLeftOrderClickListner(view -> {
            presenter.onPrevClick();
        });
        presenter.onCreateView(savedInstanceState);

        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof MessageEvent) {
                        MessageEvent event = (MessageEvent) o;
                        switch (event.getMessage()) {
                            case TILL_CLOSED: {
                                presenter.onTillClose();
                                break;
                            }

                        }
                    }else if(o instanceof  MessageWithIdEvent){
                        MessageWithIdEvent messageWithIdEvent = (MessageWithIdEvent) o;
                        switch (messageWithIdEvent.getMessage()){
                            case ORDER_CANCELED:
                                presenter.onOrderCanceledFromOutSide(messageWithIdEvent.getId());
                                break;
                            case ORDER_CLOSED:
                                presenter.onOrderClosedFromOutSide(messageWithIdEvent.getId());
                                break;
                        }
                    }
                }));

    }



    @Override
    protected int getToolbarMode() {
        return MpToolbar.MAIN_PAGE_TYPE;
    }

    Runnable timerUpdate = new Runnable() {
        @Override
        public void run() {
            tvDate.setText(new SimpleDateFormat("dd - MMM, yyyy", Locale.ENGLISH).format(new Date()));
            handler.postDelayed(timerUpdate, 30000);
        }
    };


    @Override
    protected void onDestroy() {
        RxBus.removeListners(subscriptions);
        super.onDestroy();
        notifyManager.setView(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void notifyView(Customer customer) {
//        Bundle bundle = new Bundle();
//        bundle.putLong("CUSTOMER_ID", customer.getId());
//        CustomerNotificationsFragment fragment = new CustomerNotificationsFragment();
//        fragment.setArguments(bundle);
//        addFragmentToTopRight(fragment);
    }



    public void openAddProductActivity() {
        Intent intent = new Intent(this, ProductActivity.class);
        startActivity(intent);
    }

    String barcode = "";
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        char pressedKey = (char) event.getUnicodeChar();
        barcode += pressedKey;
        if (keyCode == KeyEvent.KEYCODE_ENTER){
            barcodeScannerManager.onKeyDown(event, barcode);
            barcode = "";
        }
        return true;
    }



    /**
     *
     * ***********************/
    @Override
    public void openNewOrderFrame(Long newOrderId) {
        hideOrderListHistoryFragment();
        OrderListFragment orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());
        if(orderListFragment!=null){
            orderListFragment.initNewOrderWithNumber(newOrderId);
        }else {
            initOrderListFragmentToLeft(newOrderId);
        }
    }

    @Override
    public void updateIndicator(Long orderId) {
        toolbar.setOrderNumber(String.valueOf(orderId));
    }

    @Override
    public void openOrUpdateOrderHistory(Order order) {
        showOrderListHistoryFragment(order);
    }

    @Override
    public void openOrderForEdit(String reason, Order order, Long newOrderId) {
        hideOrderListHistoryFragment();
        OrderListFragment orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());
        if(orderListFragment!=null){
            orderListFragment.onEditOrder(reason,order,newOrderId);
        }
    }


    public void onEditComplete(String reason,Order order){
        presenter.onEditComplete(reason,order);
    }

    public void orderAdded(Order order){
        presenter.orderAdded(order);
    }


    public void onEditOrder(String reason){
        presenter.onEditOrder(reason);
    }
    public void onCancelOrder(String reason){
        presenter.onCancelOrder(reason);
    }
    public void onRestoreOrder(){
        presenter.onRestoreOrder();
    }

    public void onContinueOrder(Order order){
        hideOrderListHistoryFragment();
        OrderListFragment orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());
        if(orderListFragment!=null){
            orderListFragment.onHoldOrderCountined(order);
        }
    }

    public void holdOrderClosed(Order order){
        presenter.holdOrderClosed(order);
    };
    public void newOrderHolded(Order order){
        presenter.newOrderHolded(order);
    };
    public void holdOrderHolded(Order order){
        presenter.holdOrderHolded(order);
    };
    public void editedOrderHolded(String reason, Order order){
        presenter.editedOrderHolded(reason,order);
    };

}
