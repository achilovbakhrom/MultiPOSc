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
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.TestUtils;
import com.jim.multipos.utils.managers.BarcodeScannerManager;
import com.jim.multipos.utils.managers.NotifyManager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        TestUtils.createCurrencies(databaseManager, this);
        TestUtils.createAccount(databaseManager);
        initOrderListFragmentToLeft();
        initProductPickerFragmentToRight();
        notifyManager.setView(this);
        handler = new Handler();
        handler.post(timerUpdate);

        toolbar.setOnClickListener(view -> {
        });

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
            OrderListHistoryFragment orderListHistoryFragment = (OrderListHistoryFragment) getSupportFragmentManager().findFragmentByTag(OrderListHistoryFragment.class.getName());
            if(orderListHistoryFragment !=null && orderListHistoryFragment.isVisible())
                orderListHistoryFragment.onNextOrder();


        });
        toolbar.setOnLeftOrderClickListner(view -> {
            OrderListHistoryFragment orderListHistoryFragment = (OrderListHistoryFragment) getSupportFragmentManager().findFragmentByTag(OrderListHistoryFragment.class.getName());
            if(orderListHistoryFragment !=null && orderListHistoryFragment.isVisible()){
                orderListHistoryFragment.onPrevOrder();
            }else
                showOrderListHistoryFragment();
        });

    }

    public void showOrderListFragmentWhenOrderHistoryEnds(){
        hideOrderListHistoryFragment();
    }

    public void setOrderNo(Long orderId){
        toolbar.setOrderNumber(String.valueOf(orderId));
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

    public void closeProductInfoFragment() {
        activityFragmentManager.popBackStack();
    }

    @Override
    protected void onDestroy() {
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
}
