package com.jim.multipos.ui.mainpospage;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextClock;
import android.widget.TextView;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.BuildConfig;
import com.jim.multipos.R;
import com.jim.multipos.core.MainPageDoubleSideActivity;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.cash_management.CashManagementActivity;
import com.jim.multipos.ui.lock_screen.LockScreenActivity;
import com.jim.multipos.ui.lock_screen.auth.AuthFragment;
import com.jim.multipos.ui.main_menu.customers_menu.CustomersMenuActivity;
import com.jim.multipos.ui.main_menu.inventory_menu.InventoryMenuActivity;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuActivity;
import com.jim.multipos.ui.mainpospage.dialogs.AccessWithEditPasswordDialog;
import com.jim.multipos.ui.mainpospage.dialogs.HeldOrdersDialog;
import com.jim.multipos.ui.mainpospage.dialogs.ReturnsDialog;
import com.jim.multipos.ui.mainpospage.dialogs.TodayOrdersDialog;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;
import com.jim.multipos.ui.mainpospage.view.BarcodeScannerFragment;
import com.jim.multipos.ui.mainpospage.view.OrderListFragment;
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.ui.reports.ReportsActivity;
import com.jim.multipos.ui.settings.SettingsActivity;
import com.jim.multipos.utils.MainMenuDialog;
import com.jim.multipos.utils.OrderMenuDialog;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.SecurityTools;
import com.jim.multipos.utils.managers.NotifyManager;
import com.jim.multipos.utils.printer.CheckPrinter;
import com.jim.multipos.utils.rxevents.main_order_events.DeviceAttachEvent;
import com.jim.multipos.utils.rxevents.main_order_events.DeviceDetachEvent;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;
import com.jim.multipos.utils.rxevents.main_order_events.MainPosActivityRefreshEvent;
import com.jim.multipos.utils.rxevents.main_order_events.OrderEvent;
import com.jim.multipos.utils.rxevents.main_order_events.RefreshUsbDevicesEvent;
import com.jim.multipos.utils.rxevents.till_management_events.TillEvent;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import lombok.Getter;

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
    RxBus rxBus;
    @Inject
    PreferencesHelper preferencesHelper;
    private ArrayList<Disposable> subscriptions;
    private Handler handler;

    private HashMap<String, String> hashesWithSerial = (HashMap<String, String>) BuildConfig.LOCAL_SERIAL_HASH;


    CheckPrinter checkPrinter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        if(preferencesHelper.getSerialValue().equals("") || preferencesHelper.getRegistrationToken().equals("") || ! hashesWithSerial.get(preferencesHelper.getSerialValue()).equals(SecurityTools.hashPassword(preferencesHelper.getSerialValue()+preferencesHelper.getRegistrationToken()))){
            addFullFragment(new AuthFragment());
        }
        if(checkPrinter ==null){
            checkPrinter = new CheckPrinter(this,preferencesHelper,databaseManager);
            checkPrinter.connectDevice();
        }




        //test dataset
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
                    if(preferencesHelper.isCashManagmentProtected()){
                        AccessWithEditPasswordDialog accessWithEditPasswordDialog = new AccessWithEditPasswordDialog(MainPosPageActivity.this, new AccessWithEditPasswordDialog.OnAccsessListner() {
                            @Override
                            public void accsessSuccess() {

                                Intent intent = new Intent(MainPosPageActivity.this, CashManagementActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onBruteForce() {

                            }
                        },preferencesHelper);
                        accessWithEditPasswordDialog.show();
                    }else {

                        Intent intent = new Intent(MainPosPageActivity.this, CashManagementActivity.class);
                        startActivity(intent);
                    }

                }

                @Override
                public void onTurnOnBarcodeScanner() {
                    showBarcodeScannerFragment();
                }

                @Override
                public void onTurnOffBarcodeScanner() {
                    hideBarcodeScannerFragment();
                }

                @Override
                public void onReturn() {
                    ReturnsDialog dialog = new ReturnsDialog(MainPosPageActivity.this, databaseManager, decimalFormat, null, rxBus);
                    dialog.show();

                }

                @Override
                public void onSettings() {
                    if(preferencesHelper.isSettingsProtected()){
                        AccessWithEditPasswordDialog accessWithEditPasswordDialog = new AccessWithEditPasswordDialog(MainPosPageActivity.this, new AccessWithEditPasswordDialog.OnAccsessListner() {
                            @Override
                            public void accsessSuccess() {
                                Intent intent = new Intent(MainPosPageActivity.this, SettingsActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onBruteForce() {

                            }
                        },preferencesHelper);
                        accessWithEditPasswordDialog.show();
                    }else {
                        Intent intent = new Intent(MainPosPageActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    }

                }

                @Override
                public void onLogOut() {
                    startActivity(new Intent(MainPosPageActivity.this, LockScreenActivity.class));
                    finish();
                }
            });
            mainMenuDialog.show();
        });
        toolbar.setOnCustomerClickListener(view -> {
            if(preferencesHelper.isCustomersProtected()){
                AccessWithEditPasswordDialog accessWithEditPasswordDialog = new AccessWithEditPasswordDialog(this, new AccessWithEditPasswordDialog.OnAccsessListner() {
                    @Override
                    public void accsessSuccess() {
                        Intent intent = new Intent(MainPosPageActivity.this, CustomersMenuActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onBruteForce() {

                    }
                },preferencesHelper);
                accessWithEditPasswordDialog.show();
            }else {
                Intent intent = new Intent(this, CustomersMenuActivity.class);
                startActivity(intent);
            }

        });

        toolbar.setOnInventoryClickListener(view -> {
            if(preferencesHelper.isInventoryProtected()){
                AccessWithEditPasswordDialog accessWithEditPasswordDialog = new AccessWithEditPasswordDialog(this, new AccessWithEditPasswordDialog.OnAccsessListner() {
                    @Override
                    public void accsessSuccess() {
                        Intent intent = new Intent(MainPosPageActivity.this, InventoryMenuActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onBruteForce() {

                    }
                },preferencesHelper);
                accessWithEditPasswordDialog.show();
            }else {
                Intent intent = new Intent(this, InventoryMenuActivity.class);
                startActivity(intent);
            }

        });

        toolbar.setOnProductClickListener(view -> {
            if(preferencesHelper.isProductsProtected()){
                AccessWithEditPasswordDialog accessWithEditPasswordDialog = new AccessWithEditPasswordDialog(this, new AccessWithEditPasswordDialog.OnAccsessListner() {
                    @Override
                    public void accsessSuccess() {
                        Intent intent = new Intent(MainPosPageActivity.this, ProductMenuActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onBruteForce() {

                    }
                },preferencesHelper);
                accessWithEditPasswordDialog.show();
            }else {
                Intent intent = new Intent(this, ProductMenuActivity.class);
                startActivity(intent);
            }
        });
        toolbar.setOnReportClickListener(view -> {
            if(preferencesHelper.isRepotsProtected()){
                AccessWithEditPasswordDialog accessWithEditPasswordDialog = new AccessWithEditPasswordDialog(this, new AccessWithEditPasswordDialog.OnAccsessListner() {
                    @Override
                    public void accsessSuccess() {
                        Intent intent = new Intent(MainPosPageActivity.this, ReportsActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onBruteForce() {

                    }
                },preferencesHelper);
                accessWithEditPasswordDialog.show();
            }else {
                Intent intent = new Intent(this, ReportsActivity.class);
                startActivity(intent);
            }


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
            OrderMenuDialog orderMenuDialog = new OrderMenuDialog(this, new OrderMenuDialog.onOrderMenuItemClickListener() {
                @Override
                public void onTodayOrderClick() {
                    TodayOrdersDialog dialog = new TodayOrdersDialog(MainPosPageActivity.this, databaseManager, order -> presenter.onTodayOrderSelected(order));
                    dialog.show();
                }

                @Override
                public void onHeldOrderClick() {
                    HeldOrdersDialog dialog = new HeldOrdersDialog(MainPosPageActivity.this, databaseManager, order -> {
                        presenter.onHeldOrderSelected(order);
                    }, preferencesHelper, rxBus);
                    dialog.show();
                }

                @Override
                public void onNewOrderClick() {
                    presenter.openNewOrder();
                }
            });
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
                    if (o instanceof TillEvent) {
                        TillEvent event = (TillEvent) o;
                        switch (event.getType()) {
                            case GlobalEventConstants.CLOSE: {
                                presenter.onTillClose();
                                break;
                            }

                        }
                    }else if(o instanceof OrderEvent){
                        OrderEvent event = (OrderEvent) o;
                        switch (event.getType()){
                            case GlobalEventConstants.CANCEL:
                                presenter.onOrderCanceledFromOutSide(event.getOrder().getId());
                                break;
                            case GlobalEventConstants.CLOSE:
                                presenter.onOrderClosedFromOutSide(event.getOrder().getId());
                                break;
                        }
                    }else if(o instanceof MainPosActivityRefreshEvent){
                        finish();
                    }else if(o instanceof DeviceDetachEvent){
                        if(((DeviceDetachEvent)o).getProductName()!=null && ((DeviceDetachEvent)o).getProductName().equals("POS58 USB Printer")) {
                            checkPrinter = null;
                        }
                    }else if(o instanceof DeviceAttachEvent){
                        if(((DeviceAttachEvent)o).getProductName()!=null && ((DeviceAttachEvent)o).getProductName().equals("POS58 USB Printer")){
                            checkPrinter = new CheckPrinter(this,preferencesHelper,databaseManager);
                            checkPrinter.connectDevice();
                        }
                    }
                }));
        rxBus.send(new RefreshUsbDevicesEvent());

    }


    @Override
    protected int getToolbarMode() {
        return MpToolbar.MAIN_PAGE_TYPE;
    }

    Runnable timerUpdate = new Runnable() {
        @Override
        public void run() {
            //TODO DO NORMALNIY
            try{
                Date date = new Date();
                tvTime.setText(new SimpleDateFormat("HH:mm").format(date));
                tvDate.setText(new SimpleDateFormat("dd - MMM, yyyy").format(date));
                handler.postDelayed(timerUpdate, 30000);
            }catch (Exception e){
                handler.postDelayed(timerUpdate, 30000);
            }
           }
    };


    @Override
    protected void onDestroy() {
        RxBus.removeListners(subscriptions);
        super.onDestroy();
        notifyManager.setView(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private PackageManager mPackageManager;

    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mAdminComponentName;

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


    public void openAddProductActivity(String lastResult) {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra("PRODUCT_BARCODE", lastResult);
        startActivity(intent);
    }



    /**
     *
     * ***********************/
    @Override
    public void openNewOrderFrame(Long newOrderId) {
        hideOrderListHistoryFragment();
        OrderListFragment orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());
        if (orderListFragment != null && orderListFragment.isAdded() && orderListFragment.isVisible()) {
            orderListFragment.initNewOrderWithNumber(newOrderId);
        } else {
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
        if (orderListFragment != null  && orderListFragment.isVisible() && orderListFragment.isAdded()) {
            orderListFragment.onEditOrder(reason, order, newOrderId);
        }
    }


    public void onEditComplete(String reason, Order order) {
        presenter.onEditComplete(reason, order);
    }

    public void orderAdded(Order order) {
        presenter.orderAdded(order);
    }


    public void onEditOrder(String reason) {
        presenter.onEditOrder(reason);
    }

    public void onCancelOrder(String reason) {
        presenter.onCancelOrder(reason);
    }

    public void onRestoreOrder() {
        presenter.onRestoreOrder();
    }

    public void onContinueOrder(Order order) {
        hideOrderListHistoryFragment();
        OrderListFragment orderListFragment = (OrderListFragment) getSupportFragmentManager().findFragmentByTag(OrderListFragment.class.getName());
        if (orderListFragment != null) {
            orderListFragment.onHoldOrderCountined(order);
        }
    }

    public void holdOrderClosed(Order order) {
        presenter.holdOrderClosed(order);
    }

    ;

    public void newOrderHolded(Order order) {
        presenter.newOrderHolded(order);
    }

    ;

    public void holdOrderHolded(Order order) {
        presenter.holdOrderHolded(order);
    }



    public void editedOrderHolded(String reason, Order order) {
        presenter.editedOrderHolded(reason, order);
    }

    public void hideSearFragmentWithDisableSearchButton(){
        toolbar.disableSearchButton();
        hideSearchFragment();
    }

    public void checkOrder(Order order, DatabaseManager databaseManager, PreferencesHelper preferencesHelper) {
        if(!preferencesHelper.isPrintCheck()) return;
        new Thread(() -> {
        if(checkPrinter ==null){
            checkPrinter = new CheckPrinter(this,preferencesHelper,databaseManager);
            checkPrinter.connectDevice();
        }
        if(checkPrinter.checkConnect()){
            checkPrinter.printCheck(MainPosPageActivity.this,order,false);
        }else {
            checkPrinter.connectDevice();
        }
        }).start();
    }
    public void reprintOrder(Order order, DatabaseManager databaseManager, PreferencesHelper preferencesHelper){
        new Thread(() -> {
            if(checkPrinter ==null){
                checkPrinter = new CheckPrinter(this,preferencesHelper,databaseManager);
                checkPrinter.connectDevice();
            }
            if(checkPrinter.checkConnect()){
                checkPrinter.printCheck(MainPosPageActivity.this,order,true);
            }else {
                checkPrinter.connectDevice();
            }
        }).start();
    }
    public void stockCheckOrder(long tillId, long orderNumber, long now, List<OrderProductItem> orderProducts, Customer customer, DatabaseManager databaseManager, PreferencesHelper preferencesHelper) {
        new Thread(() -> {
            if(checkPrinter == null){
                checkPrinter = new CheckPrinter(this,preferencesHelper,databaseManager);
                checkPrinter.connectDevice();
            }
            if(checkPrinter.checkConnect()){
                checkPrinter.stockChek(MainPosPageActivity.this,tillId,orderNumber,now,orderProducts,customer);
            }else {
                checkPrinter.connectDevice();
            }
        }).start();
    }

}
